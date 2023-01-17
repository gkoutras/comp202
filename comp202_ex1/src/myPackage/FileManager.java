package myPackage;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.nio.charset.StandardCharsets;
import org.tuc.ece.util.*;

public class FileManager {
		
	static final int pageSize = 128;
	static final int recSize = 32;
	static final int keySize = 4;
	static final int keyRange = 1000000;
	static final int totalSerialSearches = 20;
	
	private final int keyTotalPageZero = pageSize / recSize;
	private final int indexPagesPerBlock = recSize / (keySize + keySize);
	public static int diskAccessTotal;
			
	static byte[] buffer = new byte[pageSize];
	
	public static RandomAccessFile raf;
	
	FileHandle fileHandle;
	
	static KeyNode[] keyNodesA = new KeyNode[FileHandle.keyTotal];
	static KeyNode[] keyNodesB = new KeyNode[FileHandle.keyTotal];
			
	// - create and manage file methods -------------------------------------------------------------------------------
	
	// option 1 - create file
	public int createFile(String fileName) {
						
		try {
			raf = new RandomAccessFile(fileName, "rw");
			
			raf.setLength(0);

			fileHandle = new FileHandle(fileName, 0, 0);
			fileHandle.updateZeroBlock();
			
		} catch(IOException e) {
			System.out.println("Error. I/O exception.");
			return 0;
		}
		
		System.out.println("File created.");
						
		return 1;
	}
	
	// option 2 - open file
	public int openFile(String fileName) {
		
		int pageTotal;
			
		File f = new File(fileName);

		if (!f.isFile()) {
			System.out.println("Error. No file exists.");
			return 0;
		} 
		
		try {
			raf = new  RandomAccessFile(fileName, "rw");
			
			pageTotal = (((int)raf.length()) / pageSize) - 1;
						
			fileHandle = new FileHandle(fileName, pageTotal, 0);
			
			if (fileHandle.checkZeroBlock() == 1)
				fileHandle.updateZeroBlock();
			
		} catch(IOException e) {
			System.out.println("Error. I/O exception.");
			return 0;
		}
		
		System.out.println("File opened.");
		
		return pageTotal;
	}
	
	// option 3 - read block
	public int readBlock(int selectedPage) {
		
		try {
			if (selectedPage > fileHandle.lastPage) {
				System.out.println("Error. Cannot read a nonexistent page.");
				return 0;
			} 
			
			raf.seek(selectedPage * pageSize);
			raf.read(buffer);
			
			fileHandle.currentPage = selectedPage;
			
			if (fileHandle.checkZeroBlock() == 1)
				fileHandle.updateZeroBlock();
			
		} catch(IOException | NullPointerException e) {
			System.out.println("Error. No file exists or no file is opened.");
			return 0;
		}
		
		return 1;
	}
	
	// option 4 - read next block
	public int readNextBlock() {
		
		try {			
			return readBlock(fileHandle.currentPage + 1);
			
		} catch(NullPointerException e) {
			System.out.println("Error. No file exists or no file is opened.");
			return 0;
		}
	}
	
	// option 5 - read previous block (unimplemented)
	public int readPrevBlock() {
		
		System.out.println("Error. Option unimplemented.");
		return 0;
	}
	
	// option 6 - write block
	public int writeBlock(int selectedPage) {
		
		try {
			if (selectedPage > fileHandle.lastPage) {
				System.out.println("Error. Cannot write on a nonexistent page.");
				return 0;
			} else if (selectedPage == 0  && fileHandle.checkZeroBlock() == 1) {
				System.out.println("Error. Cannot write on block zero.");
				return 0;
			} else if (fileHandle.currentPage == 0 && fileHandle.checkZeroBlock() == 1) {
				System.out.println("Error. Buffer contains block zero. Cannot write this this block on a page.");
				return 0;
			}
			
			raf.seek(selectedPage * pageSize);
			raf.write(buffer);
						
			fileHandle.currentPage = selectedPage;
			
			if (fileHandle.checkZeroBlock() == 1)
				fileHandle.updateZeroBlock();

		} catch(IOException | NullPointerException e) {
			System.out.println("Error. No file exists or no file is opened.");
			return 0;
		}

		return 1;
	}
	
	// option 7 - write next block
	public int writeNextBlock() {
		
		try {
			if (fileHandle.currentPage != fileHandle.lastPage)
				return writeBlock(fileHandle.currentPage + 1);
			else
				return appendBlock();
				
		} catch(NullPointerException e) {
			System.out.println("Error. No file exists or no file is opened.");
			return 0;
		}	
	}
	
	// option 8 - append block
	public int appendBlock() {
		
		try {
			fileHandle.lastPage++;
			fileHandle.currentPage = fileHandle.lastPage;
			
			if (fileHandle.checkZeroBlock() == 1)
				fileHandle.updateZeroBlock();
			
			raf.seek(fileHandle.lastPage * pageSize);
			raf.write(buffer);
						
		} catch(IOException | NullPointerException e) {
			System.out.println("Error. No file exists or no file is opened.");
			return 0;
		}
				
		return 1;		
	}
	
	// option 9 - delete block
	public int deleteBlock(int selectedPage) {
		
		try {
			if (selectedPage > fileHandle.lastPage) {
				System.out.println("Error. Cannot delete a nonexistent page.");
				return 0;
			} else if (selectedPage == 0 && fileHandle.checkZeroBlock() == 1) {
				System.out.println("Error. Cannot delete this first paeg.");
				return 0;
			}
			
			raf.seek(fileHandle.lastPage * pageSize);
			raf.read(buffer);
			raf.seek(selectedPage * pageSize);
			raf.write(buffer);
			
			fileHandle.currentPage = selectedPage;
			fileHandle.lastPage--;

			if (fileHandle.checkZeroBlock() == 1)
				fileHandle.updateZeroBlock();
			
		} catch(IOException | NullPointerException e) {
			System.out.println("Error. No file exists or no file is opened.");
			return 0;
		}

		return 1;
	}
	
	// option 10 - close file
	public int closeFile() {
		
		try {
			raf.close();
			
		} catch(IOException e) {
			System.out.println("Error. No file exists or no file is opened.");
			return 0;
		}
		
		System.out.println("File closed.");
								
		return 1;
	}
	
	// - other methods ------------------------------------------------------------------------------------------------
	
	// array of nodes filling and buffer updates (newly created files only)
	public void initializePage(int selectedPage, String method, boolean indicesFileCreationState) throws IOException {
				
		int key, integer1, integer2, page;
		String alphaNumericString;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		if (!indicesFileCreationState) {
			
			Random r = new Random();
			UtilitiyMethods um = new UtilitiyMethods();
							
			for (int i = 0; i < pageSize / recSize; i++) {
				
				if (method != "C") {
					
					key = r.nextInt(keyRange - 1) + 1;
									
					alphaNumericString = um.getAlphaNumericString(recSize - (3 * keySize));
	
					integer1 = r.nextInt();
	
					integer2 = r.nextInt();
					
					/* storing keys & information on an array of nodes for later initializations and sorting
					 */
					if (method == "A") 
						keyNodesA[(i + (selectedPage * keySize)) - keyTotalPageZero] = new KeyNode(key, alphaNumericString, integer1, integer2, selectedPage);
					if (method == "B") 
						keyNodesB[(i + (selectedPage * keySize)) - keyTotalPageZero] = new KeyNode(key, alphaNumericString, integer1, integer2, selectedPage);
				} else {
							
					/* picking keys & information from the sorted array of nodes 
					 */
					key = keyNodesA[(i + (selectedPage * keySize)) - keyTotalPageZero].getKey();
					
					alphaNumericString = keyNodesA[(i + (selectedPage * keySize)) - keyTotalPageZero].getAlphaNumeric();
					
					integer1 = keyNodesA[(i + (selectedPage * keySize)) - keyTotalPageZero].getInteger1();
					
					integer2 = keyNodesA[(i + (selectedPage * keySize)) - keyTotalPageZero].getInteger2();
				}
				
				dos.writeInt(key);
				dos.writeBytes(alphaNumericString);
				dos.writeInt(integer1);
				dos.writeInt(integer2);
			} 
		} else {
				
			for (int i = 0; i < pageSize / recSize; i++) {
				
				for (int j = 0; j < indexPagesPerBlock; j++) {
					
					/* picking keys from the sorted array of nodes
					 */
					key = keyNodesB[(j + (i * indexPagesPerBlock)) + ((selectedPage - 1) * indexPagesPerBlock * keySize)].getKey();
					dos.writeInt(key);
					
					page = keyNodesB[(j + (i * indexPagesPerBlock)) + ((selectedPage - 1) * indexPagesPerBlock * keySize)].getPage();
					dos.writeInt(page);			
				}
			}
		}
		
		buffer = bos.toByteArray();
		bos.reset();
	}
	
	// updates for the array of nodes of each file (newly created files only)
	public void updateKeyNodesPerPage(int selectedPage, boolean indicesFileCreationState) throws IOException {
		
		int key, integer1, integer2, page;
		String alphaNumericString;
				
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
		DataInputStream dis = new DataInputStream(bis);
		
		if (!indicesFileCreationState) {
			
			for (int i = 0; i < pageSize / recSize; i++) {
				
				key = dis.readInt();
				
				byte[] bytesForDataString = new byte[recSize - (3 * keySize)];
				dis.read(bytesForDataString, 0, recSize - (3 * keySize));
				alphaNumericString = new String(bytesForDataString, StandardCharsets.US_ASCII);
				
				integer1 = dis.readInt();
				integer2 = dis.readInt();
				
				/* storing keys & information on an array of nodes for later initializations and sorting
				 */
				keyNodesA[(i + (selectedPage * keySize)) - keyTotalPageZero] = new KeyNode(key, alphaNumericString, integer1, integer2, selectedPage);
				
			}
		} else {
			
			for (int i = 0; i < pageSize / recSize; i++) {
				
				for (int j = 0; j < indexPagesPerBlock; j++) {
					
					key = dis.readInt();
					page = dis.readInt();
					
					/* storing keys on an array of nodes for later initializations and sorting
					 */
					keyNodesB[(j + (i * indexPagesPerBlock)) + ((selectedPage - 1) * indexPagesPerBlock * keySize)] = new KeyNode(key, "", 0, 0, page);
				}
			}
		}
	}
	
	// key serial search (newly created files only)
	public void serialSearch(int key, int currentPage, int lastPage, boolean indicesFileCreationState) throws IOException {
		
		int size;
		boolean successState = false;
					
		Random r = new Random();
					
		/* picking random keys, already used to initialize the blocks, for serial comparisons
		 */
		if (!indicesFileCreationState) {
			
			if (key == 0)
				key = keyNodesA[r.nextInt(FileHandle.keyTotal)].getKey();
			size = recSize;
		} else {
			
			if (key == 0)
				key = keyNodesB[r.nextInt(FileHandle.keyTotal)].getKey();
			size = keySize + keySize;
		}
		
		diskAccessTotal++;
					
		int j = 0;
		while (j < pageSize / size && !successState) {
				
			raf.seek((currentPage * pageSize) + (j * size));
			raf.read(buffer);	
							
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			DataInputStream dis = new DataInputStream(bis);
			
			try {
				if (dis.readInt() == key)
					successState = true;
				
			} catch(EOFException e) {}
			
			dis.close();
			bis.close();
			
			j++;
		}
		
		if (currentPage != lastPage && !successState)
			serialSearch(key, currentPage + 1, lastPage, indicesFileCreationState);
	} 
	
	// internal sorting of the array of nodes for each file (newly created files only)
	public void initiateFileSorting(boolean indicesFileCreationState) throws IOException {
		
		if (!indicesFileCreationState)
			Arrays.sort(keyNodesA);
		else
			Arrays.sort(keyNodesB);
	}
	
	// key binary search (newly created files only)
	public void binarySearch(int key, int minimumPage, int maximumPage, boolean indicesFileCreationState) throws IOException {
		
		int size, middlePage = minimumPage + (maximumPage - minimumPage) / 2;
		boolean successState = false;
		
		Random r = new Random();
		
		/* picking random keys, already used to initialize the blocks, for binary comparisons
		 */	
		if (!indicesFileCreationState) {
			
			if (key == 0)
				key = keyNodesA[r.nextInt(FileHandle.keyTotal)].getKey();
			size = recSize;
		} else {
			
			if (key == 0)
				key = keyNodesB[r.nextInt(FileHandle.keyTotal)].getKey();
			size = keySize + keySize;
		}
		
		diskAccessTotal++;

		int i = 0;
		while (i < pageSize / size && !successState) {
				
			raf.seek((middlePage * pageSize) + (i * size));
			raf.read(buffer);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			DataInputStream dis = new DataInputStream(bis);
			
			try {
				if (dis.readInt() == key)
					successState = true;
				
			} catch(EOFException e) {}
			
			dis.close();
			bis.close();
			
			i++;
		}
		
		raf.seek(middlePage * pageSize);
		
		if (raf.readInt() > key && !successState)
			binarySearch(key, minimumPage, middlePage - 1, indicesFileCreationState);
		
		raf.seek(((middlePage + 1) * pageSize) - size);

		if (raf.readInt() < key && !successState)
			binarySearch(key, middlePage + 1, maximumPage, indicesFileCreationState);
	} 
	
//	// print block to user
//	public void printBlock(int selectedPage, boolean indicesFileCreationState) {
//		
//		try {
//			if (selectedPage > fileHandle.lastPage) {
//				System.out.println("Error. Cannot print a nonexistent page.");
//				return;
//			}
//			
//		} catch(NullPointerException e) {
//			System.out.println("Error. No file exists or no file is opened.");
//			return;
//		}
//		
//		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
//		DataInputStream dis = new DataInputStream(bis);
//		
//		try {
//			if (!indicesFileCreationState) {
//				
//				if (selectedPage == 0 && fileHandle.checkZeroBlock() == 1)
//					System.out.print("\n(Total pages: " + dis.readInt() + ")\n");		
//				else {
//					
//					System.out.println("\nPage read to buffer as:");					
//
//					while (true) {
//						
//						System.out.print("(Key: " + dis.readInt() + ", Key info: ");
//						
//						byte[] bytesForDataString = new byte[recSize - (3 * keySize)];
//						dis.read(bytesForDataString, 0, recSize - (3 * keySize));
//						String alphaNumericString = new String(bytesForDataString, StandardCharsets.US_ASCII);
//						
//						System.out.print(alphaNumericString + " - " + dis.readInt() + " - " + dis.readInt());
//						
//						System.out.print(")\n");
//					}
//				}
//			} else {
//				
//				if (selectedPage == 0 && fileHandle.checkZeroBlock() == 1)
//					System.out.print("\n(Total pages: " + dis.readInt() + ")\n");		
//				else {
//					
//					System.out.println("\nPage read to buffer as:");					
//
//					while (true) {
//												
//						for (int i = 0; i < pagesPerBlock; i++)
//							System.out.print("(Key: " + dis.readInt() + ", Key page: "  + dis.readInt() + ")\t");
//						
//						System.out.print("\n");
//					}
//				}
//			}
//			
//			dis.close();
//			bis.close();	
//			
//		} catch(IOException | NullPointerException e) {}			
//	}
	
}

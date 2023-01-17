package myPackage;

import java.io.IOException;

public class FileMethodManager {
	
	private static final int keysPerPage = 4;
	private static final int indexPagesPerBlock = 4;
	private static final int serialSearchTotal = 20;
	private static final int firstPage = 1;
	
	public static final String methodA = "A";
	public static final String methodB = "B";
	public static final String methodC = "C";
	public static final String methodD = "D";

	FileManager fileManager = new FileManager();
				
	// - create file with different methods ---------------------------------------------------------------------------

	// - method A -----------------------------------------------------------------------------------------------------
	
	// option 11 - create file (keys & information)
	public void createFileA(String fileName, boolean indicesFileCreationState) throws IOException {
		
		int pageTotal = FileHandle.keyTotal / keysPerPage;
		
		fileManager.createFile(fileName);
						
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.initializePage(i, methodA, indicesFileCreationState);
			fileManager.writeNextBlock();
		}
		
		fileManager.closeFile();
	}
	
	// - method B -----------------------------------------------------------------------------------------------------
	
	// option 13 - create file (keys & information)
	public void createFileB1(String fileNameInfo, String fileNameIndex, boolean indicesFileCreationState) throws IOException {
		
		int pageTotal = FileHandle.keyTotal / keysPerPage;

		fileManager.createFile(fileNameInfo);
						
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.initializePage(i, methodB, indicesFileCreationState);
			fileManager.writeNextBlock();
		}
		
		fileManager.closeFile();											
		
		createFileB2(fileNameIndex, !indicesFileCreationState);
	}
	
	// option 12 - create file (keys & indices)
	public void createFileB2(String fileName, boolean indicesFileCreationState) throws IOException {
		
		int pageTotal = (FileHandle.keyTotal / keysPerPage) / indexPagesPerBlock ;

		fileManager.createFile(fileName);
						
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.initializePage(i, methodB, indicesFileCreationState);
			fileManager.writeNextBlock();
		}
				
		fileManager.closeFile();											
	}
	
	// - serial search ------------------------------------------------------------------------------------------------
	
	// option 12/option 14 - initiate serial search on file (keys & information)/(keys & indices)
	public void initiateSerialSearch(String fileName, boolean indicesFileCreationState) throws IOException {
		
		int pageTotal = fileManager.openFile(fileName);
		
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.readBlock(i);
			fileManager.updateKeyNodesPerPage(i, indicesFileCreationState);
		}
		
		FileManager.diskAccessTotal = 0;
		for (int i = 0; i < serialSearchTotal; i++)
			fileManager.serialSearch(0, firstPage, pageTotal, indicesFileCreationState);
		
		System.out.println("");
		
		if (!indicesFileCreationState)
			System.out.println("Serial search completed with " + (FileManager.diskAccessTotal / serialSearchTotal) + " disk accesses (keys & info file).\n");
		else
			System.out.println("Serial search completed with " + (FileManager.diskAccessTotal / serialSearchTotal) + " disk accesses (keys only file).\n");

		
		fileManager.closeFile();											
	}
		
	// - method C -----------------------------------------------------------------------------------------------------
	
	// option 15 - sort method A file (keys & information)
	public void createFileC(String fileNameInfo, String fileNameInfoSorted, boolean indicesFileCreationState) throws IOException {
		
		int pageTotal = fileManager.openFile(fileNameInfo);
		
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.readBlock(i);
			fileManager.updateKeyNodesPerPage(i, indicesFileCreationState);
		}
		
		fileManager.initiateFileSorting(indicesFileCreationState);		
		fileManager.createFile(fileNameInfoSorted);
						
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.initializePage(i, methodC, indicesFileCreationState);
			fileManager.writeNextBlock();
		}
		
		fileManager.closeFile();
	}
	
	// - method D -----------------------------------------------------------------------------------------------------
	
	// option 17 - sort method B file (keys & indices)
	public void createFileD(String fileNameIndex, String fileNameIndexSorted, boolean indicesFileCreationState) throws IOException {
		
		int pageTotal = fileManager.openFile(fileNameIndex);
		
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.readBlock(i);
			fileManager.updateKeyNodesPerPage(i, indicesFileCreationState);
		}
		
		fileManager.initiateFileSorting(indicesFileCreationState);		
		fileManager.createFile(fileNameIndexSorted);
						
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.initializePage(i, methodD, indicesFileCreationState);
			fileManager.writeNextBlock();
		}
		
		fileManager.closeFile();
	}	
	
	// - binary search ------------------------------------------------------------------------------------------------
	
	// option 16/option 18 - initiate binary search on sorted file (keys & information)/(keys & indices)
	public void initiateBinarySearch(String fileName, boolean indicesFileCreationState) throws IOException {
		
		int pageTotal = fileManager.openFile(fileName);
		
		for (int i = 1; i <= pageTotal; i++) {
			
			fileManager.readBlock(i);
			fileManager.updateKeyNodesPerPage(i, indicesFileCreationState);
		}
		
		FileManager.diskAccessTotal = 0;
		fileManager.binarySearch(0, firstPage, pageTotal, indicesFileCreationState);
		
		System.out.println("");

		if (!indicesFileCreationState)
			System.out.println("Serial search completed with " + FileManager.diskAccessTotal + " disk accesses (keys & info file).\n");
		else
			System.out.println("Serial search completed with " + FileManager.diskAccessTotal + " disk accesses (keys only file).\n");
		
		fileManager.closeFile();											
	}
}

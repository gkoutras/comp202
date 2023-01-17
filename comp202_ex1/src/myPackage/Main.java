package myPackage;

import java.io.IOException;
import org.tuc.ece.util.*;

public class Main {
	
	public static final String fileName = "file.bin";
	public static final String fileNameA = "fileA.bin";
	public static final String fileNameBInfo = "fileB_keyInfo.bin";
	public static final String fileNameBIndex = "fileB_keyIndex.bin";
	public static final String fileNameC = "fileC.bin";
	public static final String fileNameD = "fileD.bin";
	
	public static final boolean indicesFileCreationState = false;

	public static void main(String[] args) throws IOException {
		
		int option = 1;
		int page;
		
		FileManager fileManager = new FileManager();
		FileMethodManager fileMethodManager = new FileMethodManager();
		
		while(option != 0) {
			
			printOptions();
			
			StandardInputRead sir = new StandardInputRead();
			
			option = sir.readPositiveInt("\nChoose an option: ");
			
			switch(option) {
				
				case 0:
					System.out.println("Exited.");
					break;
				case 1:
					fileManager.createFile(fileName);
					break;
				case 2:
					fileManager.openFile(fileName);
					break;
				case 3:
					page = sir.readPositiveInt("Choose page to read from: ");
					fileManager.readBlock(page);
//					fileManager.printBlock(page, indicesFileCreationState);
					break;
				case 4:
					fileManager.readNextBlock();
					break;
				case 5:
					fileManager.readPrevBlock();
					break;
				case 6:
					page = sir.readPositiveInt("Choose page to write on: ");
					fileManager.writeBlock(page);
					break;
				case 7:
					fileManager.writeNextBlock();
					break;
				case 8:
					fileManager.appendBlock();
					break;
				case 9:
					page = sir.readPositiveInt("Choose page to delete: ");
					fileManager.deleteBlock(page);
					break;
				case 10:
					fileManager.closeFile();
					break;
				case 11:
					fileMethodManager.createFileA(fileNameA, indicesFileCreationState);
					break;
				case 12:
					fileMethodManager.initiateSerialSearch(fileNameA, indicesFileCreationState);
					break;
				case 13:
					fileMethodManager.createFileB1(fileNameBInfo, fileNameBIndex, indicesFileCreationState);
					break;
				case 14:
					fileMethodManager.initiateSerialSearch(fileNameBIndex, !indicesFileCreationState);
					break;
				case 15:
					fileMethodManager.createFileC(fileNameA, fileNameC, indicesFileCreationState);
					break;
				case 16:
					fileMethodManager.initiateBinarySearch(fileNameC, indicesFileCreationState);
					break;
				case 17:
					fileMethodManager.createFileD(fileNameBIndex, fileNameD, !indicesFileCreationState);
					break;
				case 18:
					fileMethodManager.initiateBinarySearch(fileNameD, !indicesFileCreationState);
					break;
				default:
					System.out.println("Error. Choice not an option.");
					break;
			}
		}
	}
	
	public static void printOptions() {
		
		System.out.println("");
		System.out.println("---- Options ------------------------------------------------------------------------------");
		System.out.println("[ 1] Create file                       (empty)");
		System.out.println("[ 2] Open file");
		System.out.println("[ 3] Read block");
		System.out.println("[ 4] Read next block");
		System.out.println("[ 5] Read previous block               (unimplemented)");
		System.out.println("[ 6] Write block");
		System.out.println("[ 7] Write next block");
		System.out.println("[ 8] Append block");
		System.out.println("[ 9] Delete block");
		System.out.println("[10] Close file");
		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("[11] (method A) Create file            (keys - information)");
		System.out.println("[12] (method A) Initiate serial search (keys - information)");
		System.out.println("[13] (method B) Create files           (keys - information & keys - indices)");
		System.out.println("[14] (method B) Initiate serial search (keys - indices)");
		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("[15] (method C) Sort method A file     (keys - information)");
		System.out.println("[16] (method C) Initiate binary search (keys - information)");
		System.out.println("[17] (method D) Sort method B file     (keys - indices)");
		System.out.println("[18] (method D) Initiate binary search (keys - indices)");
		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("[ 0] Exit");
		System.out.println("-------------------------------------------------------------------------------------------");
	}
	
}

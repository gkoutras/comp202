package performancePackage;

import treeImplementationPackage.BinarySearchTree;
import treeImplementationPackage.ThreadedBinarySearchTree;

import org.tuc.ece.util.StandardInputRead;

public class Main {

	public static void main(String[] args) {
		
		int option = 1;
		
		StandardInputRead sir = new StandardInputRead();

		while(option != 0) {
			
			printOptions();
			option = sir.readPositiveInt("\nChoose an option: ");
			
			switch(option) {
				
				case 0:
					System.out.println("Program exited.");
					break;
				case 1:
					option1();
					break;
				case 2:
					option2();
					break;
				case 3:
					option3();
					System.out.println("\nProgram exited.");
					option = 0;
					break;
				case 4:
					option4();
					System.out.println("\nProgram exited.");
					option = 0;
					break;
				default:
					System.out.println("Error. Choice not an option.");
					break;
			}
		}
	}
	
	// - part A -------------------------------------------------------------------------------------------------------------------------------------
	
	final static void option1() {
		
		int suboption = 1;
		int head = 0;
		boolean keysInserted = false;
		
		StandardInputRead sir = new StandardInputRead();

		int N = sir.readPositiveInt("Choose the number of keys that the tree will consist of: ");
		
		BinarySearchTree bst = new BinarySearchTree(N);
		
		while(suboption != 0) {
			
			printSuboptions1();
			suboption = sir.readPositiveInt("\nChoose an option: ");

			switch(suboption) {
				
				case 0:
					System.out.println("Returned to inital options.");
					break;
				case 1:					
					int i = 0; while (i < N) {
        				
        				int key = sir.readPositiveInt("\n(Root " + i + ") - Choose the value of the integer key to be inserted: ");
        				bst.insert(head, key);
        				
        				System.out.println("The array of the inserted keys is as follows:");
        				bst.printTree();
        				
        				i++;
					} keysInserted = true;

					break;
				case 2:
					if (!keysInserted) {
						
        				System.out.println("Keys need to be inserted first in the tree.");
        				break;
        			}
					
    				int key = sir.readPositiveInt("\nChoose the value of the integer key to be searched: ");
        			boolean result = bst.search(head, key);
        			
        			if (result)
        				System.out.println("Key search completed successfully, key exists.");
        			else
        				System.out.println("Key search completed unsuccessfully, key does not exist.");
        			
					break;
				case 3:
					if (!keysInserted) {
						
        				System.out.println("Keys need to be inserted first in the tree.");
        				break;
        			}
					
        			int key1 = sir.readPositiveInt("\nChoose the value of the integer key with which the range begins: ");
        			int key2 = sir.readPositiveInt("Choose the value of the integer key with which the range ends: ");

					System.out.println("The keys inside the given range are as follows:");
        			bst.printRange(head, key1, key2, true);
					System.out.println("");

        			break;
				default:
					System.out.println("Error. Choice not an option.");
					break;
			}
		}
	}
	
	// - part B -------------------------------------------------------------------------------------------------------------------------------------

	final static void option2() {
		
		int suboption = 1;
		int head = 0;
		boolean keysInserted = false;
		
		StandardInputRead sir = new StandardInputRead();

		int N = sir.readPositiveInt("Choose the number of keys that the tree will consist of: ");
		
		ThreadedBinarySearchTree tbst = new ThreadedBinarySearchTree(N);
		
		while(suboption != 0) {
			
			printSuboptions2();
			suboption = sir.readPositiveInt("\nChoose an option: ");

			switch(suboption) {
				
				case 0:
					System.out.println("Returned to inital options.");
					break;
				case 1:					
					int i = 0; while (i < N) {
        				
        				int key = sir.readPositiveInt("\n(Root " + i + ") - Choose the value of the integer key to be inserted: ");
        				tbst.insert(head, key);
        				
        				System.out.println("The threaded array of the inserted keys is as follows:");
        				tbst.printTree();
        				
        				i++;
					} keysInserted = true;

					break;
				case 2:
					if (!keysInserted) {
						
        				System.out.println("Keys need to be inserted first in the tree.");
        				break;
        			}
					
					int key = sir.readPositiveInt("\nChoose the value of the integer key to be searched: ");
        			boolean result = tbst.search(head, key);
        			
        			if (result)
        				System.out.println("Key search completed successfully, key exists.");
        			else
        				System.out.println("Key search completed unsuccessfully, key does not exist.");
        			
					break;
				case 3:
//					if (!keysInserted) {
//						
//        				System.out.println("Keys need to be inserted first in the tree.");
//        				break;
//        			}
//					
//					int key1 = sir.readPositiveInt("\nChoose the value of the integer key with which the range begins: ");
//        			int key2 = sir.readPositiveInt("Choose the value of the integer key with which the range ends: ");
//
//					System.out.println("The keys inside the given range are as follows:");
//        			tbst.printRange(head, key1, key2, true); System.out.println("");
        			
					break;
				default:
					System.out.println("Error. Choice not an option.");
					break;
			}
		}
	}
	
	// - part C -------------------------------------------------------------------------------------------------------------------------------------
	
	final static void option3() {
		
		PerformanceTest pt = new PerformanceTest();
		pt.printResultTable();
	}
	
	final static void option4() {
		
		StandardInputRead sir = new StandardInputRead();

		String fileName = sir.readString("Type the full path directory of the input binary file: ");
		
		ManualPerformanceTest mpt = new ManualPerformanceTest(fileName);
		mpt.printResultTable();
	}
	
	// - menu options -------------------------------------------------------------------------------------------------------------------------------
	final static void printOptions() {
		
		System.out.println("");
		System.out.println("- Options ---------------------------------------------------------------------------------------------------------------");
		System.out.println("[1] - Create search binary tree manually (array BST)");
		System.out.println("[2] - Create search binary tree manually (threaded BST)");
		System.out.println("[3] - Test the performance of each structure (array BST, threaded BST, sorted field)");
		System.out.println("[4] - Test the performance of each structure with custom input file (array BST, threaded BST, sorted field)");
		System.out.println("[0] - Exit program");		
	}
	
	final static void printSuboptions1() {
		
		System.out.println("");
		System.out.println("- Suboptions for Option 1 (array BST) ----------------------------------------------------------");
		System.out.println("[1] - Insert keys of given value");
		System.out.println("[2] - Search a key of given value");
		System.out.println("[3] - Print keys in given range");
		System.out.println("[0] - Return");
	}
	
	final static void printSuboptions2() {
		
		System.out.println("");
		System.out.println("- Suboptions for Option 2 (threaded BST) -------------------------------------------------------");
		System.out.println("[1] - Insert keys of given value");
		System.out.println("[2] - Search a key of given value");
		System.out.println("[3] - Print keys in given range (unimplemented)");
		System.out.println("[0] - Return");
	}
	
}

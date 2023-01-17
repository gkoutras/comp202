package performancePackage;

import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import treeImplementationPackage.BinarySearchTree;
import treeImplementationPackage.ThreadedBinarySearchTree;

import org.tuc.ece.util.MultiCounter;

public class PerformanceTest {
	
	private static int insertBST;
	private static int searchBST;
	private static int rangeBST1;
	private static int rangeBST2;
	
	private static int insertTBST;
	private static int searchTBST;
	private static int rangeTBST1;
	private static int rangeTBST2;
	
	private static int searchSF;
	private static int rangeSF1;
	private static int rangeSF2;
	
	private static int keyTotal = 10000;
	private static int keyRangeTotal = 1000000;
	
	private static int keyRange1 = 100;
	private static int keyRange2 = 1000;
	
	private static int repetitionTotal = 100;
	private static int head = 0;
	private static int nullValue = -1;
	
	final static int sfSearchCounter = 7;
	final static int sfRangeCounter = 8;	

	List<Integer> treeList = new ArrayList<Integer>();
	Integer[] fieldArray = new Integer[keyTotal];
	
	BinarySearchTree bst = null;
	ThreadedBinarySearchTree tbst = null;
	
	public PerformanceTest() {
		
		generateIntegers(true);
		
		bst = new BinarySearchTree(keyTotal);
		
		insertBST();
		searchBST();
		printRangeBST(keyRange1);
		printRangeBST(keyRange2);

		tbst = new ThreadedBinarySearchTree(keyTotal);
		
		insertTBST();	
		searchTBST();
		
		// printRange test is unimplemeted for threaded BST
		printRangeTBST(keyRange1);
		printRangeTBST(keyRange2);
		
		generateIntegers(false);
		
		searchSF();
		rangeSF(keyRange1);
		rangeSF(keyRange2);
	}
	
	// - array BST performance test -----------------------------------------------------------------------------------------------------------------
	
	public void insertBST() {
		
		MultiCounter.resetCounter(BinarySearchTree.bstInsertCounter);

		for (int i = 0; i < keyTotal; i++)
			bst.insert(head, treeList.get(i));

		insertBST = MultiCounter.getCount(BinarySearchTree.bstInsertCounter) / keyTotal;
	}
	
	public void searchBST() {
		
		MultiCounter.resetCounter(BinarySearchTree.bstSearchCounter);
				
		Random r = new Random();

		for (int i = 0; i < repetitionTotal; i++)
			bst.search(head, treeList.get(r.nextInt(keyTotal)));
		
		searchBST = MultiCounter.getCount(BinarySearchTree.bstSearchCounter) / repetitionTotal;
	}
	
	public void printRangeBST(int range) {
		
		MultiCounter.resetCounter(BinarySearchTree.bstRangeCounter);
		
		Random r = new Random();
		
		for (int i = 0; i < repetitionTotal; i++) {
			
			int key1 = treeList.get(r.nextInt(keyTotal));
			int key2 = key1 + range;
			
			bst.printRange(head, key1, key2, false);
		}
		
		if (range == keyRange1)
			rangeBST1 = MultiCounter.getCount(BinarySearchTree.bstRangeCounter) / repetitionTotal;
		else
			rangeBST2 = MultiCounter.getCount(BinarySearchTree.bstRangeCounter) / repetitionTotal;
	}
	
	// - threaded BST performance test --------------------------------------------------------------------------------------------------------------

	public void insertTBST() {
		
		MultiCounter.resetCounter(ThreadedBinarySearchTree.tbstInsertCounter);
		
		for (int i = 0; i < keyTotal; i++)
			tbst.insert(head, treeList.get(i));
		
		insertTBST = MultiCounter.getCount(ThreadedBinarySearchTree.tbstInsertCounter) / keyTotal;
	}
	
	public void searchTBST() {
		
		MultiCounter.resetCounter(ThreadedBinarySearchTree.tbstSearchCounter);

		Random r = new Random();

		for (int i = 0; i < repetitionTotal; i++)
			tbst.search(head, treeList.get(r.nextInt(keyTotal)));
		
		searchTBST = MultiCounter.getCount(ThreadedBinarySearchTree.tbstSearchCounter) / repetitionTotal;
	}
	
	// unimplemented method
	public void printRangeTBST(int range) {
		
		if (range == keyRange1)
			rangeTBST1 = 0;
		else
			rangeTBST2 = 0;
	}
	
	// - sorted field performance test --------------------------------------------------------------------------------------------------------------

	public void searchSF() {
		
		Random r = new Random();

		MultiCounter.resetCounter(sfSearchCounter);
		
		for (int i = 0; i < repetitionTotal; i++)
			binarySearch(fieldArray, head, keyTotal - 1, treeList.get(r.nextInt(keyTotal)), sfSearchCounter);
		
		searchSF = MultiCounter.getCount(sfSearchCounter) / repetitionTotal;
		
		}
	
	public void rangeSF(int range) {
		
		MultiCounter.resetCounter(sfRangeCounter);
		
		Random r = new Random();
		
		for (int i = 0; i < repetitionTotal; i++) {
			
			int key1 = treeList.get(r.nextInt(keyTotal));
			int key2 = key1 + range;
			
			int min = head, tmp = head;
			for (int j = key1; j <= key2; j++) {
				
				tmp = binarySearch(fieldArray, min, keyTotal - 1, j, sfRangeCounter);
	
				min = (tmp != nullValue && MultiCounter.increaseCounter(sfRangeCounter)) ? tmp : min;
			}
		}
		
		if (range == keyRange1)
			rangeSF1 = MultiCounter.getCount(sfRangeCounter) / repetitionTotal;
		else
			rangeSF2 = MultiCounter.getCount(sfRangeCounter) / repetitionTotal;	
	}
	
	public int binarySearch(Integer array[], int min, int max, int key, int counter) {
		
        if (max >= min && MultiCounter.increaseCounter(counter)) { 
            int mid = min + (max - min) / 2; 
  
            if (array[mid] == key && MultiCounter.increaseCounter(counter)) 
                return mid; 
            
            if (array[mid] > key && MultiCounter.increaseCounter(counter)) 
                return binarySearch(array, min, mid - 1, key, counter); 
  
            return binarySearch(array, mid + 1, max, key, counter); 
        } 
         
        return -1; 
    }
	
	// - performance results table ------------------------------------------------------------------------------------------------------------------
	
	public void printResultTable() {
		
		System.out.println("Result table for " + keyTotal + " total keys is as follows:\n");
		System.out.println("\t\t\t(avg comparisons)\t(avg comparisons)\t(avg comparisons)\t(avg comparisons)");
		System.out.println("\t\t\t(insertion)\t\t(search)\t\t(range of 100)\t\t(range of 1000)\n");
		System.out.println("(array BST)\t\t" + insertBST + "\t\t\t" + searchBST + "\t\t\t" + rangeBST1 + "\t\t\t" + rangeBST2 + "\n");
		System.out.println("(threaded BST)\t\t" + insertTBST + "\t\t\t" + searchTBST + "\t\t\t" + rangeTBST1 + " (unimplemented)" + "\t" + rangeTBST2 + " (unimplemented)\n");
		System.out.println("(sorted field)\t\t" + "-" + "\t\t\t" + searchSF + "\t\t\t" + rangeSF1 + "\t\t\t" + rangeSF2);
	}
	
	// - other --------------------------------------------------------------------------------------------------------------------------------------

	// generate unique random integers for testing
	public void generateIntegers(boolean state) {
		
		Set<Integer> keys = new HashSet<>();

		Random r = new Random();
		
		// unique random unsorted integers
		if (state) {	
			
			while (keys.size() < keyTotal)
				keys.add(r.nextInt(keyRangeTotal - 1) + 1);
			
			fieldArray = keys.toArray(fieldArray);
			
			for (int i = 0; i < keyTotal; i++)
				treeList.add(fieldArray[i]);
		}
		
		// unique random sorted integers
		else {
			
			for (int i = 0; i < keyTotal; i++)
				fieldArray[i] = treeList.get(i);
			Arrays.sort(fieldArray);
		}
	}
	
}

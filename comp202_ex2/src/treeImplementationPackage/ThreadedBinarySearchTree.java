package treeImplementationPackage;

import org.tuc.ece.util.MultiCounter;

public class ThreadedBinarySearchTree {
	
	private int n;
	private int avail;

	private static int data = 0;
	private static int left = 1;
	private static int right = 2;
	private static int leftThread = 3;
	private static int rightThread = 4;
	
	private int ptr1, ptr2;
	
	private static int head = 0;
	private static int nullValue = -1;
	private static int trueState = 1;
	private static int falseState = 0;
	
	public static int tbstInsertCounter = 4;
	public static int tbstSearchCounter = 5;
	public static int tbstRangeCounter = 6;	
	
	private static int[][] dataArray;
				
	public ThreadedBinarySearchTree(int n) {
				
		dataArray = new int[n][5];	
		this.n = n - 1;
		
		avail = head;
		
		initialize();
	}
	
	// - initialization -----------------------------------------------------------------------------------------------------------------------------
	
	public void initialize() {
				
		for (int i = 0; i <= n; i++) {
			
			if (i == n) 
				setInstance(i, nullValue, nullValue, nullValue);
			else
				setInstance(i, nullValue, i + 1, nullValue);
			
			dataArray[i][leftThread] = falseState;
			dataArray[i][leftThread] = falseState;
		}
	}
	
	// - insertion ----------------------------------------------------------------------------------------------------------------------------------
	
	// threaded bst suboption 1
	public void insert(int root, int key) {
		
		// insert first root key
		if (dataArray[root][data] == nullValue && MultiCounter.increaseCounter(tbstInsertCounter))			
			setInstance(head, key, nullValue, nullValue);
		
		// insert the rest keys
		else 
			insertRest(root, key); 
		
		avail++;
	}
	
	public void insertRest(int root, int key) {
				
		if (dataArray[root][data] != nullValue && MultiCounter.increaseCounter(tbstInsertCounter)) {
			
			// moving on left subtree
			if (key < dataArray[root][data] && MultiCounter.increaseCounter(tbstInsertCounter)) {
				
				if ((dataArray[root][left] == nullValue || dataArray[root][leftThread] == trueState) && MultiCounter.increaseCounter(tbstInsertCounter)) {
					
					dataArray[root][leftThread] = falseState;
					dataArray[root][left] = avail;
					
					dataArray[avail][rightThread] = trueState;
					ptr1 = root;
					
					insertRest(avail, key);
				} else {
					
					dataArray[avail][rightThread] = trueState;
					ptr1 = root;
					
					insertRest(dataArray[root][left], key);
				}
			} 
			
			// moving on right subtree
			else if (key > dataArray[root][data] && MultiCounter.increaseCounter(tbstInsertCounter)) {
				
				if ((dataArray[root][right] == nullValue || dataArray[root][rightThread] == trueState) && MultiCounter.increaseCounter(tbstInsertCounter)) {
					
					dataArray[root][rightThread] = falseState;
					dataArray[root][right] = avail;
					
					dataArray[avail][leftThread] = trueState;
					ptr2 = root;
					
					insertRest(avail, key);
				} else {
					
					dataArray[avail][leftThread] = trueState;
					ptr2 = root;
					
					insertRest(dataArray[root][right], key);
				}
			}
		} else {
			
			if ((dataArray[root][leftThread] == falseState && dataArray[root][rightThread] == falseState) && MultiCounter.increaseCounter(tbstInsertCounter))
				setInstance(root, key, nullValue, nullValue);
			else if ((dataArray[root][leftThread] == trueState && dataArray[root][rightThread] == falseState) && MultiCounter.increaseCounter(tbstInsertCounter))
				setInstance(root, key, nullValue, ptr2);
			else if ((dataArray[root][leftThread] == falseState && dataArray[root][rightThread] == trueState) && MultiCounter.increaseCounter(tbstInsertCounter))
				setInstance(root, key, ptr1, nullValue);
			else
				setInstance(root, key, ptr1, ptr2);
		}
	}
	
	public void setInstance(int root, int key, int ptr1, int ptr2) {
		
		dataArray[root][data] = key;
		dataArray[root][left] = ptr2;
		dataArray[root][right] = ptr1;
	}
	
	// - search -------------------------------------------------------------------------------------------------------------------------------------
	
	// threaded bst suboption 2
	public boolean search(int root, int key) {
				
		if (root == nullValue && MultiCounter.increaseCounter(tbstSearchCounter))
			return false;
			
		if (dataArray[root][data] == key && MultiCounter.increaseCounter(tbstSearchCounter)) 
            return true;
      
        if ((dataArray[root][data] > key && dataArray[root][leftThread] == falseState) && MultiCounter.increaseCounter(tbstSearchCounter)) 
            return search(dataArray[root][left], key); 
        else if ((dataArray[root][data] < key && dataArray[root][rightThread] == falseState) && MultiCounter.increaseCounter(tbstSearchCounter))
        	return search(dataArray[root][right], key);
        
        return false;
	}
	
//	// - range --------------------------------------------------------------------------------------------------------------------------------------
//	
//	// threaded bst suboption 3
//	public void printRange(int root, int key1, int key2, boolean state) {
//		
//		if (root == nullValue && MultiCounter.increaseCounter(tbstRangeCounter))
//            return; 
//  
//        if (key1 < dataArray[root][data] && MultiCounter.increaseCounter(tbstRangeCounter))
//        	printRange(dataArray[root][left], key1, key2, state); 
//  
//        if ((key1 <= dataArray[root][data] && key2 >= dataArray[root][data]) && MultiCounter.increaseCounter(tbstRangeCounter)) {
//            
//        	if (state == true)
//        		System.out.print(dataArray[root][data] + "\t"); 
//        }
//  
//        if (key2 > dataArray[root][data] && MultiCounter.increaseCounter(tbstRangeCounter))
//        	printRange(dataArray[root][right], key1, key2, state); 
//	} 
	
	// - other --------------------------------------------------------------------------------------------------------------------------------------

	public void printTree() {
		
		System.out.println("\t\t(i)\t(L)\t(R)\t(L thd)\t(R thd)");
		for (int i = 0; i <= n; i++)
			System.out.println("(root " + i + ")\t " + dataArray[data][i] + "\t " + dataArray[left][i] + "\t " + dataArray[right][i] + "\t " + dataArray[i][leftThread] + "\t " + dataArray[i][rightThread]);
	}
	
}

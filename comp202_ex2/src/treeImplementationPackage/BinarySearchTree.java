package treeImplementationPackage;

import org.tuc.ece.util.MultiCounter;

public class BinarySearchTree {
	
	private int n;
	private int avail;
	
	private static int data = 0;
	private static int left = 1;
	private static int right = 2;
	
	private static int head = 0;
	private static int nullValue = -1;
	
	public static int bstInsertCounter = 1;
	public static int bstSearchCounter = 2;
	public static int bstRangeCounter = 3;
	
	private static int[][] dataArray;	
	
	public BinarySearchTree(int n) {
		
		dataArray = new int[n][3];	
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
		}
	}
	
	// - insertion ----------------------------------------------------------------------------------------------------------------------------------

	// array bst suboption 1
	public void insert(int root, int key) {
		
		// insert first root key
		if (dataArray[root][data] == nullValue && MultiCounter.increaseCounter(bstInsertCounter))
			setInstance(head, key, nullValue, nullValue);
		
		// insert the rest keys
		else 
			insertRest(root, key); 
		
		avail++;
	}
	
	public void insertRest(int root, int key) {
				
		if (dataArray[root][data] != nullValue && MultiCounter.increaseCounter(bstInsertCounter)) {
			
			// moving on left subtree
			if (key < dataArray[root][data] && MultiCounter.increaseCounter(bstInsertCounter)) {
				
				if (dataArray[root][left] == nullValue && MultiCounter.increaseCounter(bstInsertCounter)) {
					
					dataArray[root][left] = avail;
					insertRest(avail, key);	
				} else
					insertRest(dataArray[root][left], key);
			} 
			
			// moving on right subtree
			else if (key > dataArray[root][data] && MultiCounter.increaseCounter(bstInsertCounter)) {
				
				if (dataArray[root][right] == nullValue && MultiCounter.increaseCounter(bstInsertCounter)) {
					
					dataArray[root][right] = avail;
					insertRest(avail, key);
				} else 
					insertRest(dataArray[root][right], key);
			}
		} else
			setInstance(root, key, nullValue, nullValue);
	}
	
	public void setInstance(int root, int key, int ptr1, int ptr2) {
		
		dataArray[root][data] = key;
		dataArray[root][left] = ptr2;
		dataArray[root][right] = ptr1;
	}
	
	// - search -------------------------------------------------------------------------------------------------------------------------------------

	// array bst suboption 2
	public boolean search(int root, int key) {
				
		if (root == nullValue && MultiCounter.increaseCounter(bstSearchCounter))
			return false;
			
		if (dataArray[root][data] == key && MultiCounter.increaseCounter(bstSearchCounter)) 
            return true;
      
        if (dataArray[root][data] > key && MultiCounter.increaseCounter(bstSearchCounter)) 
            return search(dataArray[root][left], key); 
        else if (dataArray[root][data] < key && MultiCounter.increaseCounter(bstSearchCounter))
        	return search(dataArray[root][right], key);
        
        return false;
	}

	// - range --------------------------------------------------------------------------------------------------------------------------------------

	// array bst suboption 3
	public void printRange(int root, int key1, int key2, boolean state) {
				
		if (root == nullValue && MultiCounter.increaseCounter(bstRangeCounter))
            return; 
  
        if (key1 < dataArray[root][data] && MultiCounter.increaseCounter(bstRangeCounter))
        	printRange(dataArray[root][left], key1, key2, state); 
  
        if ((key1 <= dataArray[root][data] && key2 >= dataArray[root][data]) && MultiCounter.increaseCounter(bstRangeCounter)) {
            
        	if (state == true)
        		System.out.print(dataArray[root][data] + "\t"); 
        }
  
        if (key2 > dataArray[root][data] && MultiCounter.increaseCounter(bstRangeCounter))
        	printRange(dataArray[root][right], key1, key2, state); 
	} 
	
	// - other --------------------------------------------------------------------------------------------------------------------------------------

	public void printTree() {
		
		System.out.println("\t\t(i)\t(L)\t(R)");
		for (int i = 0; i <= n; i++)
			System.out.println("(root " + i + ")\t " + dataArray[i][data] + "\t " + dataArray[i][left] + "\t " + dataArray[i][right]);
	}
	
}
	
package myPackage;

import java.io.*;

public class FileHandle {
	
	static final int keyTotal = 10000;
	static final int keyTotalPageZero = 4;
	static final int secondKeyPageZeroPos = 32;
	
	public String fileName;
	public int lastPage;
    public int currentPage;

    public static RandomAccessFile raf;
    
    // - file handle object -------------------------------------------------------------------------------------------

    public FileHandle(String fileName, int lastPage, int currentPage) {
    	
        this.fileName = fileName;
        this.lastPage = lastPage;
        this.currentPage = currentPage;
    }
        
    // page zero information updates (newly created files only)
	public void updateZeroBlock() {
		
		try {
			raf = new RandomAccessFile(fileName, "rw");

			/* store the last page & current page numbers on block zero
			 */
			raf.seek(0);
			raf.writeInt(lastPage);
			raf.writeInt(currentPage);
			
		} catch(IOException | NullPointerException e) {}			
	}
	
    // page zero check (check if the opened file has an extra starting block for storing useful information)
	public int checkZeroBlock() {
		
		int secondKeyPageZero, returnValue;
		
		try {
			raf = new RandomAccessFile(fileName, "rw");

			/* if the last page & current page numbers are stored in block zero, then all other positions are empty 
			 * e.g. position 32 (position of the second overall key) which should have zero value in these cases
			 */
			raf.seek(secondKeyPageZeroPos);			
			secondKeyPageZero = raf.readInt();
			
			returnValue = (secondKeyPageZero == 0) ? 1 : -1;
			
			return returnValue;

		} catch(IOException | NullPointerException e) {
			return 0;
		}		
	}

}

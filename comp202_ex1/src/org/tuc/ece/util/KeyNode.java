package org.tuc.ece.util;

public class KeyNode implements Comparable<KeyNode> {
	
	private int key;
	private String alphaNumeric;
	private int  integer1;
	private int  integer2;
	private int page;

	public KeyNode(int key, String alphaNumeric, int integer1, int integer2, int page) {
		
		this.key = key;
		this.alphaNumeric = alphaNumeric;
		this.integer1 = integer1;
		this.integer2 = integer2;
		this.page = page;
	}
	
	public int getKey() {
		
		return key;
	}
	
	public String getAlphaNumeric() {
		
		return alphaNumeric;
	}
	public int getInteger1() {
		
		return integer1;
	}
	public int getInteger2() {
		
		return integer2;
	}
	
	public int getPage() {
		
		return page;
	}
	
	@Override
	public int compareTo(KeyNode otherNode) {
		
		if (this.getKey() == otherNode.getKey())
			return 0; 
		else if (this.getKey() > otherNode.getKey())
			return 1; 
		else
			return -1;
	}
	
}

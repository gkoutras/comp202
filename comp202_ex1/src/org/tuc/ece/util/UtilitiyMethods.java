package org.tuc.ece.util;

public class UtilitiyMethods {
	
	public String getAlphaNumericString(int length)  {
			
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz"; 

		// create StringBuffer size of AlphaNumericString 
		StringBuilder sb = new StringBuilder(length); 

		for (int i = 0; i < length; i++) { 

		    // generate a random number between 0 to AlphaNumericString variable length 
		    int index = (int)(AlphaNumericString.length() * Math.random()); 

		    // add Character one by one in end of sb 
		    sb.append(AlphaNumericString.charAt(index)); 
		} 

		return sb.toString(); 
	}
	
}

package com.RMQ.MQActions;


/**
*
* @author vijesh
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.*;

import com.neotys.extensions.action.engine.Context;

public class Parser {

	//public static void main(String[] args) throws IOException {
	public static String parse(Context context,String message) throws IOException{
		// TODO Auto-generated method stub
		
		String parsedstring="";
		 
		   Reader reader ;
		// TODO Auto-generated method stub
		
		//	File log= new File("D:\\log.txt");
		String search = "<variablevalue>";  // <- changed to work with String.replaceAll()
		String replacement = "<variablevalue>";
		//file reading
		
		String s;
		try {
		  

		    while (message != null) {
		    	  String result="";
		    	// search for NL varaiable using regx
		    	
		    	
		    	String mydata = "some string with 'the data i want' inside";
		    	Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		    	Matcher matcher = pattern.matcher(message);
		    	if (matcher.find())
		    	{
		    	    System.out.println(matcher.group(1));
		    	    String replacevalue=context.getVariableManager().getValue(matcher.group(1));
		    	    //get correspoding NL varaiable value
		    	     result= message.replaceAll("\\$\\{(.*?)\\}", replacevalue);
		    	    // System.out.println(result);
		    	}
		    	else{
		    		result=result.concat(message);
		    	//	System.out.println(result);
		    	}
		    	
		    	 
		    	parsedstring=parsedstring.concat(result+"\n");
		    	    	    	 
		  
		        
		        //write back to file
		    	
		    	
		        
		    }
		    System.out.println(parsedstring);
		   // reader = new StringReader(parsedstring);
		    
		}
		finally{
			
		}
		return parsedstring;

	}

}

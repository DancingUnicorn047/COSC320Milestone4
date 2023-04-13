import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class KMP {
		static int[][] PREFIX_FUNCTION(String ppwd){
		int m = ppwd.length();
		int[][] e = new int[2][m]; // 
		int k = 0;
		int spaces = 0;
		
		for(int q = 1; q < m; q++) {
			while(k > 0 && ppwd.charAt(k) != ppwd.charAt(q)) {
				k = e[0][k];
			}
			
			//char t1 = ppwd.charAt(k); for testing
			//char t2 = ppwd.charAt(q);
			
			if(ppwd.charAt(k) == ppwd.charAt(q)) {
				k = k+1;
				
			}
			if(q+1 == ppwd.length() || ppwd.charAt(q) == ' ') {
				spaces = spaces +1;
				//counts number of spaces in the longest prefix that is also a suffix 
			}
			e[0][q] = k;
			e[1][q] = spaces;
		}
		return e;
	}
	
	static int KMP_MATCHER(String comment, String ppwd, int[][] e) { 
		int n = comment.length();
		//e pre-calculated
		int q = 0;
	
		int temp, prefix_spaces,tc = 0, count = 0;
		
		for(int i = 0; i< n; i++) {
			char t = ppwd.charAt(q);
			while(q>0 && ppwd.charAt(q) != comment.charAt(i)) {
				temp = q;
				q = e[0][q];
				prefix_spaces = e[0][temp]-e[0][q]; //finds the number of spaces between where q is and where it is set to
				tc = tc - prefix_spaces; //makes sure not to double count prefixes
				if(tc >= 3) {
					count = count + tc;
					tc = 0;
				}
				tc = 0; //this just insures that tc is set back to 0 if a letter does not match and there are no prefix spaces
				tc = tc + prefix_spaces;
			}
			if(ppwd.charAt(q) == comment.charAt(i) || q == ppwd.length()) { 
				if(i+1 == comment.length() || comment.charAt(i) == ' ') { //word end
					tc = tc + 1;
				}
				else if(q+1 == ppwd.length()) { //if q == ppwd.length() then you have read to the end of the test comment and so the last word must be counted and you must break to avoid getting > 100%
					tc = tc+1;
					if(tc >= 3) {
						count = count + tc;
						tc = 0;
					}
					break;
				}
				q=q+1;
			}
			if(q>0 && ppwd.charAt(q-1) != comment.charAt(i)) {
				if(tc >= 3) {
					count = count + tc;
					tc = 0;
				}
			}
		}
		if(tc >= 3) {
			count = count + tc;
			tc = 0;
		}
		return count;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String ppwd = "This is a great app";
		
		String temparary = "";
		ppwd = ppwd.toLowerCase();
		int words_in_ppwd = 0;
		System.out.println(ppwd);
		for(int i = 0; i< ppwd.length(); i++) {
			if(Character.isAlphabetic(ppwd.charAt(i)) || Character.isWhitespace(ppwd.charAt(i))){
				if(Character.isWhitespace(ppwd.charAt(i)) && i != 0 && !Character.isWhitespace(ppwd.charAt(i-1))) {
					temparary = temparary + ' ';
					words_in_ppwd ++;
				}
				else {
					temparary = temparary + ppwd.charAt(i);
				}
			}
		}
		
		
		int [][] e = PREFIX_FUNCTION(ppwd);
		
		int how_many_document_to_check_against = 100000; 
		String string_location = "C:\\Users\\jiyih\\Downloads\\NiceFiles";
			
		File file_locations = new File(string_location);
		String[] directory = file_locations.list(); //this is an array that holds all of the names for all of the csv  files
		String[][] strings = new String[1][]; //one string for every comment
		
		ppwd = temparary;
		
		int max = 0, temp;
		double percent;
		int fileCounter = 0;
		
		String comment_match = "";
		for(int i = 0; i< how_many_document_to_check_against; i++) {
			//System.out.println(fileCounter + " " + directory.length);
			if(fileCounter >= directory.length) {
				break;
			}
			String string_file_location = string_location + "/" + "/NiceFiles_" + Integer.toString(i) + ".txt";
			
			try {
				File file = new File(string_file_location);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				int file_comment_count = 0;
				while (reader.readLine() != null) {
					file_comment_count++;
				}
				reader.close();
				
								
				strings[0] = new String[file_comment_count+1];
				for(int j = 0; j< strings[0].length; j++) {
					strings[0][j] = ""; //Initializes all of the strings
				}
					
				Scanner reader1 = new Scanner(file).useDelimiter(";;;");
				int j = 0;
				while(reader1.hasNext()) {
					j++;
					strings[0][j-1] = reader1.next().replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase(); 
				}
				reader1.close();
					
				//System.out.println(file.toString() +":  "+ strings[0][strings[0].length-1]);
			}
			catch(Exception e1) {
				//System.out.println("Can't read file \n" +e1);
				continue;
			}
			fileCounter++;

			for(int j = 0; j< strings[0].length; j++) {
				temp = KMP_MATCHER(strings[0][j], ppwd, e);
				if(temp > max) {
					max = temp;
					comment_match = strings[0][j];
				}
			}
			if(fileCounter % 500 == 0) {
				System.out.println("Files Checked: " + fileCounter + " Time Taken: " + (System.currentTimeMillis() - start));
			}
		}
		
		
		percent = (max/words_in_ppwd)*100;
		if(percent > 24) {
			System.out.println("there is a " + percent + "% match between the document and one of the corpus document, it is likely that this document has been plagarized\n\n");
			System.out.println("the comment which matches the most with the presesnted document is\n");
			System.out.println(comment_match);
		}
		else {
			System.out.println("there is a " + percent + "% match between the document and one of the corpus documents, \nit is unlikely thatg the document has been plagarized against the data set");
		}
		System.out.println("Files Checked: " + fileCounter + " Time Taken: " + (System.currentTimeMillis() - start));
	}
}

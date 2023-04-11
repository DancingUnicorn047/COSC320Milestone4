import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class KMP {
	static String[][] docs (String location, int how_many_document_to_check_against){
		String[][] dummy = new String[0][0];
		try {
			String string_location = location;
				
			File file_locations = new File(string_location);
			String[] directory = file_locations.list(); //this is an array that holds all of the names for all of the csv  files
			File[] files = new File[directory.length];
			String[][] strings = new String[directory.length][]; //one string for every comment
				
			Scanner reader1 = new Scanner("");
			String t1;
			BufferedReader in;
			for(int i = 0; i< how_many_document_to_check_against; i++) { //directory.length    this initializes string[][], string[i] holds files, string[][j] holds individual comments
											 //I used this format for possible expansion in case there where too many comments for a computeres memory the job could be split by strings[i]
				String string_file_location = string_location + "/" + "/NiceFiles_" + Integer.toString(i) + ".txt";;
				files[i] = new File(string_file_location);

				in = new BufferedReader(new FileReader(files[i]));
					
				int file_comment_count = 0;
				int endl = 0;
				char tc = ' ';
				int ti = 0;
				t1 = "";
					
				while(ti != -1) {
					ti = in.read();
					tc = (char) ti;
						
					if(Character.isAlphabetic(tc)) {
							t1 = t1 + Character.toLowerCase(tc);;
					}
					else if(Character.isWhitespace(tc)){
						if(t1.length() != 0 && !Character.isWhitespace(t1.charAt(t1.length()-1)) && t1.charAt(t1.length()-1) != ';') {
								t1 = t1 + ' ';
							}
						}
					else if(tc == ';') {
						endl ++;
						if(endl == 3) {
							file_comment_count ++;
							endl = 0;
							t1 = t1 + ";;;";
						}
					}
				}
				in.close();
									
				strings[i] = new String[file_comment_count];
				for(int j = 0; j< strings[i].length; j++) {
					strings[i][j] = ""; //Initializes all of the strings
				}
					
				reader1 = new Scanner(t1).useDelimiter(";;;");
				int j = 0;
				while(reader1.hasNext()) {
					j++;
					strings[i][j-1] = reader1.next();
				}
				reader1.close();
					
				System.out.println(files[i].toString() +":  "+ strings[i][strings[i].length-1]);
			}
			return strings;
		}
		///*
		catch(Exception e) {
			System.out.println("not working \n" +e);
			return dummy;
		}
	}
	
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
				if(k == ppwd.length() && (q+1 == ppwd.length() || ppwd.charAt(q+1) == ' ')||(ppwd.charAt(k) == ' ' && (q+1 == ppwd.length() ||ppwd.charAt(q+1) == ' '))) {
					spaces = spaces +1;
					//counts number of spaces in the longest prefix that is also a suffix 
				}
			}
			e[0][q] = k;
			e[1][q] = spaces;
		}
		return e;
	}
	
	static int KMP_MATCHER(String comment, String ppwd, int[][] e) { 
		int n = comment.length();
		int m = ppwd.length();
		//e pre-calculated
		int q = 0;
		
		int temp, prefix_spaces,tc = 0, count = 0;
		
		for(int i = 0; i< n; i++) {
			while(q>0 && ppwd.charAt(q) != comment.charAt(i)) {
				temp = q;
				q = e[0][q];
				prefix_spaces = e[0][temp];
				tc = tc - prefix_spaces; //makes sure not to double count prefixes
				if(tc >= 3) {
					count = count + tc;
					tc = 0;
				}
				tc = tc + prefix_spaces;
			}
			if(ppwd.charAt(q) == comment.charAt(i) || q == ppwd.length()) { //if q == ppwd.length() then you have read to the end of the test comment and so the last word must be counted
				if(i+1 == comment.length() || comment.charAt(i) == ' ') { //word end
					tc = tc + 1;
				}
				q=q+1;
			}
			if(q>0 && ppwd.charAt(q-1) != comment.charAt(i) || q == ppwd.length()) {
				if(tc >= 3) {
					count = count + tc;
					tc = 0;
				}
			}
			if(q == ppwd.length()) { //if this is the case then the whole test comment has been found in another comment
				break;
			}
		}
		if(tc >= 3) {
			count = count + tc;
			tc = 0;
		}
		return count;
	}

	public static void main(String[] args) {
		
		String ppwd = "So awesome. Read my";
		int how_many_document_to_check_against = 50; //must choose <=71
		String[][] docs = docs("C:\\Users\\jiyih\\Downloads\\NiceFiles", how_many_document_to_check_against);

		//ppwd to proper formatter 
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
		
		ppwd = temparary;
		
		int [][] e = PREFIX_FUNCTION(ppwd);
		
		int max = 0, temp;
		double percent;
		
		String comment_match = "";
		for(int j = 0; j< how_many_document_to_check_against; j++) {
			for(int i = 0; i< docs[j].length; i++) {
				temp = KMP_MATCHER(docs[j][i], ppwd, e);
				//
				//System.out.println(temp + "  "  + i);
				//
				
				if(temp > max) {
					max = temp;
					comment_match = docs[j][i];
				}
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
	}
}

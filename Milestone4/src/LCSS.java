import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class LCSS {
	static String[][] docs (String location, int number_of_files_to_use_in_corpus){
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
			for(int i = 0; i< number_of_files_to_use_in_corpus; i++) { //directory.length    this initializes string[][], string[i] holds files, string[][j] holds individual comments
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
			System.out.println("files done loading");
			return strings;
		}
		catch(Exception e) {
			System.out.println("not working \n" +e);
			return dummy;
		}
	}
	
	
	static int LCS(String ppwd, String corp) {
		int [][] lcs = new int[corp.length()+1][ppwd.length()+1];
		
		for(int i = 1; i< corp.length()+1; i++) {
			for(int j = 1; j< ppwd.length()+1; j++) {
				char ppwdc = ppwd.charAt(j-1);
				char corpc = corp.charAt(i-1);
				if(ppwdc==corpc) {
					lcs[i][j] = 1 + lcs[i-1][j-1];
				}
				else {
					lcs[i][j] = Math.max(lcs[i-1][j], lcs[i][j-1]);
				}
			}
		}
		return lcs[corp.length()][ppwd.length()];
	}
	
	
	public static void main(String[] args) {
		int number_of_files_to_use_in_corpus = 50; // choose <= 71
		String[][] docs = docs("C:/Users/samue/Downloads/NiceFiles", number_of_files_to_use_in_corpus);
		//*/
		String ppwd = "Hello my name is Samuel"; //"Great app and worth paying for the"
		String temparay = "";
		ppwd = ppwd.toLowerCase();
		for(int i = 0; i< ppwd.length(); i++) {
			if(Character.isAlphabetic(ppwd.charAt(i))){
				temparay = temparay + ppwd.charAt(i);
			}
		}
		ppwd = temparay;
		int lcs = 0;
		double max_similarities = 0;
		double temp = 0;
		for(int i = 0; i< docs[0].length; i++) {
			lcs = LCS(ppwd, docs[0][i]);
			temp = lcs/Math.max((Math.min(docs[0][i].length(), ppwd.length())), 15);
			if(temp > max_similarities) {
				max_similarities = temp;
			}
		}
	
		max_similarities = max_similarities*100;
		if(max_similarities > 24) {
			System.out.println("this document shows signs of potentially being plagiarized and has a lcs that contains " + max_similarities + "% similarity with at least one tested document");
		}
		else{
			System.out.println("this document has similarities that are within tolerance and has not likely been plagiarized");
		}
		System.out.println("done");
	}
}

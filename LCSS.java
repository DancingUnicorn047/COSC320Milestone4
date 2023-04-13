import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class LCSS {
	static String[][] docs (String location, int number_of_files_to_use_in_corpus, int i) throws Exception{
		File file_locations = new File(location);
		String[] directory = file_locations.list(); //this is an array that holds all of the names for all of the csv  files
		String[][] strings = new String[1][]; //one string for every comment
		
		int fileCounter = 0;
		
		//System.out.println(fileCounter + " " + directory.length);
		String string_file_location = location + "/" + "/NiceFiles_" + Integer.toString(i) + ".txt";
		
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
		return strings;
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
				/* I just left this here, it was used for error finding, but it is shows how to work out an example which is kind of cool
				for(int k = 0; k< corp.length()+1; k++) {
					for(int l = 0; l< ppwd.length()+1; l++) {
						System.out.print(lcs[k][l] + "  ");
					}
					System.out.println("\n");
				}
				System.out.println("\n\n");
				*/
			}
		}
		return lcs[corp.length()][ppwd.length()];
	}
	
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int number_of_files_to_use_in_corpus = 1000000; 
		String string_location = "C:\\Users\\jiyih\\Downloads\\NiceFiles";
		
		
		String ppwd = "This is a great app";
		String temparay = "";
		String most_matched; 
		ppwd = ppwd.toLowerCase();
		for(int i = 0; i< ppwd.length(); i++) {
			if(Character.isAlphabetic(ppwd.charAt(i))){
				temparay = temparay + ppwd.charAt(i);
			}
		}
		ppwd = temparay;
		double lcs = 0;
		double max_similarities = 0;
		double temp = 0;
		int fileCounter = 0;
		String[][] docs = null;
		File file_locations = new File(string_location);
		String[] directory = file_locations.list(); //this is an array that holds all of the names for all of the csv  files
		for(int i = 0; i< number_of_files_to_use_in_corpus ; i++) {
			if(fileCounter >= directory.length) {
				break;
			}
			try {
				docs = docs(string_location, number_of_files_to_use_in_corpus, i);
			}
			catch(Exception e1) {
				//System.out.println("Can't read file \n" +e1);
				continue;
			}
			fileCounter++;
			for(int j = 0; j< docs[0].length; j++) {
				lcs = LCS(ppwd, docs[0][j]);
				temp = lcs/Math.max((Math.min(docs[0][j].length(), ppwd.length())), 25);//the 25 here is just to 
				//prevent a comment that is ~5 letters long from being counted as plagarism against a longer comment
				//this is not a perfect system, but will keep misclassification to a minimum
				if(temp > max_similarities) {
					max_similarities = temp;
					most_matched = docs[0][j];
				}
			}
			if(fileCounter % 500 == 0) {
				System.out.println("Files Checked: " + fileCounter + " Time Taken: " + (System.currentTimeMillis() - start));
			}
		}
	
		max_similarities = max_similarities*100;
		if(max_similarities > 24) {
			System.out.println("this document shows signs of potentially being plagiarized and has a lcs that contains " + max_similarities + "% similarity with at least one tested document");
		}
		else{
			System.out.println("this document has similarities that are within tolerance and has not likely been plagiarized (" + max_similarities +"%)");
		}
		System.out.println("Files Checked: " + fileCounter + " Time Taken: " + (System.currentTimeMillis() - start));
	}
}

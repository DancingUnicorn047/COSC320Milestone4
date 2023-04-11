import java.io.File;
import java.util.*;

public class RKF {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// inputs
		String file_location = "C:\\Users\\jiyih\\Downloads\\NiceFiles";
		String test_document = "This is a great app";
		
		// Separate into words
		String[] test_words = test_document.split(" ");
		
		// hash each words
		ArrayList<Long> test_hash = new ArrayList<Long>();
		for(String w:test_words) {
			// remove all but number and character and convert to lower case
			w = w.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(); 
			long hash = w.hashCode();
			test_hash.add(hash);
			//System.out.println(w + " " +hash);
		}
		System.out.println(test_hash.size());
		// store documents that might be plagiarized
		ArrayList<String> Potentially_Plagiarized = new ArrayList<String>();
		
		File dir = new File(file_location);
		String[] Allfilenames = dir.list(); //this is an array that holds all of the names for all of the csv  files
		
		int n = 2;// number of input documents
		
		//prevent index out of bound
		if(n > Allfilenames.length) {
			n = Allfilenames.length;
		}
		String[] filenames = new String[n];
		for(int i = 0; i < n; i++) {
			filenames[i] = Allfilenames[i];
		}
		for(String filename:filenames) {
			// read all content in the file into a single string
			String temp_file_location = file_location + "/" + filename;
			File temp_file = new File(temp_file_location);
			String file_content = "";
			try(Scanner reader = new Scanner(temp_file).useDelimiter(";;;")) {
				while(reader.hasNext()) { //This will have to be fixed
					String temp = reader.nextLine();
					file_content = file_content + " " + temp.substring(0, temp.length()-3);
				}
				//System.out.println("File read");
				reader.close();
			}
			catch(Exception e) {
				//System.out.println("Can't read file \n" +e);
			}
			// Separate into words
			String[] document_words = file_content.split(" ");
			// hash each words
			ArrayList<Long> document_hash = new ArrayList<Long>();
			for(String w:document_words) {
				// remove all but number and character and convert to lower case
				w = w.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(); 
				long hash = w.hashCode();
				document_hash.add(hash);
				//System.out.println(w + " " +hash);
			}
			
			// compare test and document hash
			int counter = 0;
			for(int i = 0; i < document_hash.size();) {
				for(int j = 0; j < test_hash.size();) {
					//System.out.println(document_hash.get(i) + " " + test_hash.get(j) + " " + document_hash.get(i).equals(test_hash.get(j)));
					int repeated_words = 0;
					if(i >= document_hash.size() || j >= test_hash.size()) {
						break;
					}
					while(document_hash.get(i).equals(test_hash.get(j))) {
						i++;
						j++;
						repeated_words++;
						if(i >= document_hash.size() || j >= test_hash.size()) {
							break;
						}
					}
					if(repeated_words >= 3) {
						counter += repeated_words;
					}
					j++;
				}
				i++;
			}
			// if more than 24% matches
			System.out.println("File: " + filename + " Repeated Words: " + counter + "  Percentage: " + 100*(float)counter/test_hash.size() + "%");
			if((float)counter/test_hash.size() > 0.24) {
				Potentially_Plagiarized.add(filename);
			}
		}
		System.out.println("Number of Documents Potentially Plagiarized: " + Potentially_Plagiarized.size());
		for(String file:Potentially_Plagiarized) {
			System.out.println(file);
		}
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;
		System.out.println("Files Checked: " + filenames.length);
		System.out.println("Time Taken: " + timeElapsed);
	}
}
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
		
		for(String w:test_words) {
			// remove all but number and character and convert to lower case
			w = w.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(); 
			//System.out.println(w + " " +hash);
		}
		//System.out.println(test_hash.size());
		// store documents that might be plagiarized
		ArrayList<String> Potentially_Plagiarized = new ArrayList<String>();
		
		File dir = new File(file_location);
		String[] Allfilenames = dir.list(); //this is an array that holds all of the names for all of the csv  files
		
		int n = 50;// number of input documents
		
		//prevent index out of bound
		if(n > Allfilenames.length) {
			n = Allfilenames.length;
		}
		for(int f = 0; f < n; f++) {
			String filename = "NiceFiles_" + Integer.toString(f) + ".txt";;
			// read all content in the file into a single string
			String temp_file_location = file_location + "/" + filename;
			File temp_file = new File(temp_file_location);
			String file_content = "";
			try(Scanner reader = new Scanner(temp_file).useDelimiter(";;;")) {
				while(reader.hasNext()) {
					String temp = reader.nextLine();
					file_content = file_content + " " + temp.substring(0, temp.length()-3);
				}
				//System.out.println("File read");
				reader.close();
			}
			catch(Exception e) {
				System.out.println("Can't read file \n" +e);
			}
			// Separate into words
			String[] document_words = file_content.split(" ");
			for(String w:document_words) {
				// remove all but number and character and convert to lower case
				w = w.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(); 
				//System.out.println(w + " " +hash);
			}
			
			// compare test and document hash
			int counter = 0;
			for(int i = 0; i < document_words.length;i++) {
				for(int j = 0; j < test_words.length;j++) {
					int repeated_words = 0;
					if(i >= document_words.length || j >= test_words.length) {
						break;
					}
					String document_input = document_words[i];
					String test_input = test_words[j];
					long document_hash = document_input.hashCode();
					long test_hash = test_input.hashCode();
					while(document_hash == test_hash) {
						i++;
						j++;
						if(i >= document_words.length || j >= test_words.length) {
							break;
						}
						repeated_words++;
						document_input += document_words[i];
						test_input += test_words[j];
						document_hash = document_input.hashCode();
						test_hash = test_input.hashCode();
					}
					if(repeated_words >= 3) {
						counter += repeated_words;
					}
					
				}
				
			}
			// if more than 24% matches
			float percent = 100*(float)counter/test_words.length;
			System.out.println("File: " + filename + " Repeated Words: " + counter + "  Percentage: " + percent + "%");
			if(percent > 0.24) {
				Potentially_Plagiarized.add(filename);
			}
		}
		System.out.println("Number of Documents Potentially Plagiarized: " + Potentially_Plagiarized.size());
		for(String file:Potentially_Plagiarized) {
			System.out.println(file);
		}
		System.out.println("Files Checked: " + n);
		System.out.println("Time Taken: " + (System.currentTimeMillis() - start));
	}
}
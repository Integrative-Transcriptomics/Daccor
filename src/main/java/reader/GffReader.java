package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Friederike
 * Gff files are read in. A list is created for each entry. The complete file is stored as a list of lists. Additionallz the header is a object field. Only files ending on .gff are recognized
 */
public class GffReader {
	
	private List<String> header;
	private List<ArrayList<String>> entries;
	private String inputFilePath;
	
	public GffReader(String inputFilePath){
		this.inputFilePath = inputFilePath;
		entries = new ArrayList<ArrayList<String>>();
		header = new ArrayList<String>();
		readIn();
	}

	private void readIn(){
		
		if(inputFilePath.endsWith(".gff")){
			
			try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {

				String line = null;

				while ((line = br.readLine()) != null) {
					if(line.startsWith("#")){
						header.add(line);
					}else{
						String[] lineFetched = line.split("\t");
						ArrayList<String> temp = new ArrayList<String>();
						for(String s : lineFetched){
							if(!s.equals("")){
								temp.add(s);
							}
						}
						entries.add(temp);
					}
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}else{
			System.err.println("No .gff file specified. Please ensure your file ends on .gff");
		}
	}
	
	public List<String> getHeader() {
		return header;
	}

	public List<ArrayList<String>> getEntries() {
		return entries;
	}
	

	
}

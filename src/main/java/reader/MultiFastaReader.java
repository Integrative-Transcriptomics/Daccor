package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Friederike Hanssen
 * Fasta files containing any number of sequences are read in and stored in a map. NOrmally used file endings are recognized: .fasta, .fna, and .fa.
 */
public class MultiFastaReader {

	private Map<String,String> seqCollection = new HashMap<String,String>();
	private String inputFilePath;

	public MultiFastaReader(String inputFilePath) {
		this.inputFilePath = inputFilePath;
		readIn();
	}

	private void readIn() {
		String identity = "";
		String sequence = "";
		
		//normally used file endings are recognized
		if (inputFilePath.endsWith(".fasta") || inputFilePath.endsWith(".fna") || inputFilePath.endsWith(".fa")) {
			try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
		
				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = br.readLine()) != null) {
					if (line.startsWith(">")) {
						if(!identity.isEmpty()){
							sequence = sb.toString();
							if(seqCollection.containsKey(identity)){ 
								System.err.println("Sequencename was not unique. Please ensure uniqueness of your identities.");
							}
							seqCollection.put(identity, sequence);
						}
						identity = line;					
						sb = new StringBuilder();
					} else {
						sb.append(line);
					}

				}
				sequence = sb.toString();
				if(seqCollection.containsKey(identity)){ 
					System.err.println("Sequencename was not unique. Please ensure uniqueness of your identities.");
				}
				seqCollection.put(identity, sequence);
				br.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}else{
			System.err.println("No fasta file was specified");
		}

	}
	public Map<String,String> getSeqCollection(){
		return seqCollection;
	}

}

package automaticReconstruction;
/**
 * 
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import reader.MultiFastaReader;
import utilities.Utilities;

/**
 * @author Alexander Seitz
 *
 */
public class CombineReconstructions {

	private Map<String, File> genomeReconstructions;
	private Map<String, List<File>> repeatReconstructions;
//	private String outputFolder = "";
	private String genomeConfig = "";
	private String repeatConfig = "";

	public CombineReconstructions(Map<String, File> genomeReconstructions, Map<String, List<File>> repeatReconstructions){
		this.genomeReconstructions = genomeReconstructions;
		this.repeatReconstructions = repeatReconstructions;
//		this.outputFolder = outputFolder;
		reconstruct();
	}

	/**
	 * @param reconstructedGenomes
	 * @param reconstructedRepeats
	 * @param outputFolder
	 */
	public CombineReconstructions(String reconstructedGenomes, String reconstructedRepeats, String outputFolder) {
		this.genomeConfig = reconstructedGenomes;
		this.repeatConfig = reconstructedRepeats;
//		this.outputFolder = outputFolder;
		parseReconstructedGenomes();
		parseReconstructedRepeats();
		reconstruct();
	}

	/**
	 * 
	 */
	private void parseReconstructedRepeats() {
		this.repeatReconstructions = new HashMap<String, List<File>>();
		int numGenomes = 0;
		if(new File(this.repeatConfig).exists()){
			try {
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(new FileReader(new File(this.repeatConfig)));
				String currLine = "";
				while((currLine=br.readLine())!= null){
					String[] splitted = currLine.split("\t");
					if(splitted.length==2){
						if(new File(splitted[1]).exists()){
							List<File> repeats = new LinkedList<File>();
							if(this.repeatReconstructions.containsKey(splitted[0])){
								repeats = repeatReconstructions.get(splitted[0]);
							}
							repeats.add(new File(splitted[1]));
							this.repeatReconstructions.put(splitted[0], repeats);
							numGenomes++;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("genome config file not found");
		}
		System.out.println("found "+numGenomes+" repeats to reconstruct");
	}

	/**
	 * 
	 */
	private void parseReconstructedGenomes() {
		this.genomeReconstructions = new HashMap<String, File>();
		int numGenomes = 0;
		System.out.println(this.genomeConfig);
		if(new File(this.genomeConfig).exists()){
			try {
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(new FileReader(new File(this.genomeConfig)));
				String currLine = "";
				while((currLine=br.readLine())!= null){
					String[] splitted = currLine.split("\t");
					if(splitted.length==2){
						if(new File(splitted[1]).exists()){
							this.genomeReconstructions.put(splitted[0], new File(splitted[1]));
							numGenomes++;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("genome config file not found");
		}
		System.out.println("found "+numGenomes+" repeats to reconstruct");
	}

	/**
	 * 
	 */
	private void reconstruct() {
		for(String sample: this.genomeReconstructions.keySet()){
			System.out.println("combining reconstruction for "+sample);
			if(this.repeatReconstructions.containsKey(sample)){
				String genomeFile = getGenomeFileFromEagerRun(this.genomeReconstructions.get(sample));
				if(genomeFile != null && genomeFile.length()>0){
					MultiFastaReader genome = new MultiFastaReader(genomeFile);
					if(genome.getSeqCollection().size()!= 1){
						System.err.println("reconstructed file does not contain exactly one sequence:");
						System.err.println(genomeFile);
						continue;
					}
					// get the reconstructed genome
					StringBuilder genomeSequence = new StringBuilder();
					for(String s: genome.getSeqCollection().keySet()){
						genomeSequence = new StringBuilder(genome.getSeqCollection().get(s));
					}
					// for each reconstructed repeat sequence
					Set<Integer> reconstructed = new TreeSet<Integer>();
					Set<Integer> notReconstructed = new TreeSet<Integer>();
					for(File f: this.repeatReconstructions.get(sample)){
						String currSequenceFile = getGenomeFileFromEagerRun(f);
						if(currSequenceFile != null && currSequenceFile.length()>0){
							// get the position and margin of the repeat in the genome sequence
							String[] splitted = f.getName().split("_");
							Integer pos = -1;
							Integer margin = 0;
							for(int i=0; i<splitted.length; i++){
								if("pos".equals(splitted[i]) && splitted.length>i){
									pos = Integer.parseInt(splitted[i+1].substring(1, splitted[i+1].length()-1));
								}else if("margin".equals(splitted[i]) && splitted.length>i){
									margin = Integer.parseInt(splitted[i+1]);
								}
							}
							MultiFastaReader currSequence = new MultiFastaReader(currSequenceFile);
							if(currSequence.getSeqCollection().size()!= 1){
								System.err.println("reconstructed file does not contain exactly one sequence:");
								System.err.println(currSequenceFile);
								continue;
							}
							// get the reconstructed repeat
							String repeatSequence = "";
							for(String s: currSequence.getSeqCollection().keySet()){
								repeatSequence = currSequence.getSeqCollection().get(s);
							}
							for(int i=0; i<repeatSequence.length(); i++){
								char genomeChar=genomeSequence.charAt(pos-margin+i);
								char repeatChar = repeatSequence.charAt(i);
								if('N' == genomeChar){
									genomeSequence.setCharAt(pos-margin+i, repeatChar);
									if('N' == repeatChar){
										notReconstructed.add(pos-margin+i);
									}else{
										reconstructed.add(pos-margin+i);
									}
								}
							}
						}
					}
					writeNewGenomeToFile(sample, genomeSequence, reconstructed, notReconstructed);
				}
			}
		}
	}

	/**
	 * @param sample
	 * @param genomeSequence 
	 * @param reconstructed 
	 */
	private void writeNewGenomeToFile(String sample, StringBuilder genomeSequence, Set<Integer> reconstructed, Set<Integer> notReconstructed) {
		String outputGenomeFilename = sample+".fasta";
		if(this.genomeReconstructions.containsKey(sample)){
			outputGenomeFilename=this.genomeReconstructions.get(sample).getParent()+"/"+sample+".fasta";
		}
		String outputReconstructedFile = outputGenomeFilename+"_reconstructed.txt";
		String outputNotReconstructedFile = outputGenomeFilename+"_not_reconstructed.txt";
		StringBuffer genome = new StringBuffer();
		genome.append(">"+sample);
		genome.append("\n");
		String[] lines = genomeSequence.toString().split("(?<=\\G.{80})");
		for(String line:lines){
			genome.append(line);
			genome.append("\n");
		}
		Utilities.writeToFile(genome.toString(), new File(outputGenomeFilename));
		Utilities.writeToFile(createLineByLineOutput(reconstructed).toString(), new File(outputReconstructedFile));
		Utilities.writeToFile(createLineByLineOutput(notReconstructed).toString(), new File(outputNotReconstructedFile));

	}

	/**
	 * @param reconstructed
	 * @return
	 */
	private StringBuffer createLineByLineOutput(Set<Integer> reconstructed) {
		StringBuffer reconstructedPositions = new StringBuffer();
		for(Integer pos: reconstructed){
			reconstructedPositions.append(pos);
			reconstructedPositions.append("\n");
		}
		return reconstructedPositions;
	}

	/**
	 * @return
	 */
	private String getGenomeFileFromEagerRun(File eagerFolder) {
		String[] eagerContent = eagerFolder.list();
		String genomeFolder = "";
		for(String file: eagerContent){
			if(file.startsWith("12-")){
				genomeFolder=eagerFolder.getAbsolutePath()+"/"+file;
				break;
			}
		}
		if(genomeFolder.length()==0){
			return "";
		}
		String  genomeFile = "";
		String[] genomeFolderContent = new File(genomeFolder).list();
		for(String file:genomeFolderContent){
			if(file.endsWith(".fasta") && !file.endsWith("nr1234.fasta") && !file.endsWith("refMod.fasta")){
				genomeFile=genomeFolder+"/"+file;
			}
		}
		if(genomeFile.length()==0){
			return "";
		}
		return genomeFile;
	}

}

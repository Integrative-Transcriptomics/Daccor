/**
 * 
 */
package inputHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import outputUtils.MultiFastaOutput;
import storage.SortedStorage;
import storage.WorkingStorage;
import utilities.Pair;
import utilities.Utilities;
import writer.MultiFastaWriter;

/**
 * @author Alexander Seitz
 *
 */
public class ParseVmatch {
	
	private String inputFilename;
	private String outputFilename;
	private Integer threshold;
	private String vmatchFile;
	private Integer margin;
	private Boolean writeSeparately;
//	private Map<String, BedFile> bedFiles = new HashMap<String, BedFile>();
	private Map<String, MultiFastaOutput> multiFastaFiles = new HashMap<String, MultiFastaOutput>();
	private Runner runner;
	
	private Map<Integer,Set<Pair<Integer, Integer>>> repeats;
//	WorkingStorage workingStorage = new WorkingStorage();
	
	private Map<String, List<File>> separateOutputFiles = new HashMap<String, List<File>>();
	
	public ParseVmatch(String inputFilename, String outputFilename, Integer threshold, String vmatchFile, Integer margin, boolean writeSeparately) {
		this.inputFilename = inputFilename;
		this.outputFilename = outputFilename;
		this.threshold = threshold;
		this.vmatchFile = vmatchFile;
		this.margin = margin;
		this.writeSeparately = writeSeparately;
		this.repeats = new TreeMap<Integer,Set<Pair<Integer, Integer>>>();
		this.runner = new Runner(this.inputFilename, this.outputFilename, this.threshold, this.margin);
		parseVmatchFile();
		getRepeats();
		writeOutput();
	}

	/**
 * 
 */
private void writeOutput() {
	try {
		MultiFastaWriter mfw = new MultiFastaWriter(multiFastaFiles, margin, runner);
		mfw.writeToFile(this.outputFilename);
		if(this.writeSeparately) {
			this.separateOutputFiles = mfw.writeToSeparateFiles(this.outputFilename);
		}
//		new BedWriter(this.bedFiles).writeToFile(this.outputFilename);;
	} catch (IOException e) {
		e.printStackTrace();
	}
}

	/**
	 * 
	 */
	private void getRepeats() {
		BufferedReader br = Utilities.getReader(this.inputFilename);
		String currLine = "";
		String currHeader = "";
		Integer currSeqNumber = -1;
		StringBuffer currSeq = new StringBuffer(); 
		try {
			while((currLine = br.readLine()) != null) {
				if(currLine.startsWith(";")) {
					continue;
				}
				if(currLine.startsWith(">")) {
					if(currHeader.length() == 0) {
						currHeader = currLine.trim().substring(1);
					}else {
						resolveCurrentRepeats(currHeader, currSeq, currSeqNumber);
						currHeader = currLine.trim().substring(1);
					}
					currSeqNumber++;
					currSeq = new StringBuffer();
				}else {
					currSeq.append(currLine.trim());
				}
			}
			resolveCurrentRepeats(currHeader, currSeq, currSeqNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param currSeq
	 * @param currSeqNumber 
	 */
	private void resolveCurrentRepeats(String header, StringBuffer currSeq, Integer currSeqNumber) {
		if(this.repeats.containsKey(currSeqNumber)) {
			WorkingStorage workingStorage = new WorkingStorage();
			for(Pair<Integer,Integer> p: this.repeats.get(currSeqNumber)) {
				if(p.getSecond() < this.threshold) {
					continue;
				}
				String repeat = currSeq.substring(p.getFirst(), p.getFirst()+p.getSecond());
				workingStorage.storeRepeatAtSingleIndex(repeat, p.getFirst());
			}
			SortedStorage sorted = new SortedStorage(workingStorage);
//			BedFile bf = new BedFile(sorted, header);
//			bf.generate(new HashMap<String, Long>());
//			this.bedFiles.put(header, bf);
			Map<String, Long> offsets = new HashMap<String, Long>();
			offsets.put(header, 0l);
			MultiFastaOutput mo = new MultiFastaOutput(sorted, header, this.margin, this.runner);
			mo.generate(offsets);
			this.multiFastaFiles.put(header, mo);
		}
	}

	/**
	 * 
	 */
	private void parseVmatchFile() {
		BufferedReader br = Utilities.getReader(this.vmatchFile);
		String currLine = "";
		try {
			while((currLine = br.readLine()) != null) {
				if(currLine.startsWith("#")) {
					continue;
				}
				currLine = currLine.trim();
				String[] splitted = currLine.split(" +");
				Integer length = Integer.parseInt(splitted[0]);
				int seqNum = Integer.parseInt(splitted[1]);
				Integer start = Integer.parseInt(splitted[2]);
				Set<Pair<Integer, Integer>> currSet = new TreeSet<Pair<Integer,Integer>>();
				if(this.repeats.containsKey(seqNum)) {
					currSet = this.repeats.get(seqNum);
				}
				this.repeats.put(seqNum, currSet);
				currSet.add(new Pair<Integer,Integer>(start, length));
				length = Integer.parseInt(splitted[4]);
				seqNum = Integer.parseInt(splitted[5]);
				start = Integer.parseInt(splitted[6]);
				Set<Pair<Integer, Integer>> currSet2 = new TreeSet<Pair<Integer,Integer>>();
				if(this.repeats.containsKey(seqNum)) {
					currSet2 = this.repeats.get(seqNum);
				}
				currSet2.add(new Pair<Integer,Integer>(start, length));
				this.repeats.put(seqNum, currSet2);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		args = new String[] {"/share/home/seitza/tests/daccor_thor/vmatch_results/vmatch_1_1_K12.fna_mut_1minLength101"};
		if (args.length != 1) {
			System.err.println("Wrong number of input parameters");
			System.err.println("Usage: <input>");
			System.exit(1);
		}
		new ParseVmatch("/share/home/seitza/references/ecoli/K12.fna", "test", 101, args[0], 0, true);
		
	}

	/**
	 * @return
	 */
	public Set<String> getReferences() {
		return this.runner.getFastaReader().getSeqCollection().keySet();
	}

	/**
	 * @return
	 */
	public List<File> getRepeatFiles(String ref) {
		List<File> result = new LinkedList<File>();
		if(this.separateOutputFiles.containsKey(ref)){
			result = this.separateOutputFiles.get(ref);
		}else{
			System.out.println("dont know key: "+ref);
			System.out.println("possible keys: ");
			System.out.println(this.separateOutputFiles.keySet());
		}
		return result;
	}
}

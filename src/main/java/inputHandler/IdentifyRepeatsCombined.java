package inputHandler;

import java.util.HashSet;
import java.util.concurrent.Callable;

import elongation.Elongator;
import storage.SortedStorage;
import storage.Storage;
import storage.WorkingStorage;

public class IdentifyRepeatsCombined implements Callable<IdentifyRepeatsCombined> {
	
	private String header = "";
//	private String sequence = "";
	private Integer kMerSize = 17;
	private Integer numMismatches = 0;
	private Integer lengthThreshold = 0;
	private Boolean showAllSmall = false;
	private Long totalSeqLength = 0l;
	
	private WorkingStorage combinedStorage;
	
	private Integer threads;
	
	private Runner runner;
	
	public IdentifyRepeatsCombined(String header, String sequence, Integer k, Integer numMismatches, Integer lengthThreshold, Boolean showAllSmall, Runner runner, Integer threads, WorkingStorage combinedStorage, Long totalSeqLength){
		this.header = header;
//		this.sequence = sequence;
		this.kMerSize = k;
		this.numMismatches = numMismatches;
		this.lengthThreshold = lengthThreshold;
		this.showAllSmall = showAllSmall;
		this.runner = runner;
		this.threads = threads;
		this.combinedStorage = combinedStorage;
		this.totalSeqLength = totalSeqLength;
	}

	@Override
	public IdentifyRepeatsCombined call() throws Exception {
		processSequence();
		// TODO Auto-generated method stub
		return this;
	}
	


	private void processSequence() {

		SortedStorage sorted = new SortedStorage(combineKMers());

		System.out.println("Repetitive regions for sequence " + header + " are identified.");
		generateOutput(header, sorted);
		System.out.println("Output was generated");

	}
	


	private Storage<HashSet<Integer>> combineKMers() {
		Elongator elongator = new Elongator(null, this.totalSeqLength, this.kMerSize, this.numMismatches, this.lengthThreshold, this.showAllSmall, this.threads);
		
		try {
			elongator.initialize(this.combinedStorage);
			elongator.runMergeOnly();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return elongator.getRepeats();
	}
	

	
	private void generateOutput(String identity, SortedStorage sorted) {
		this.runner.generateOutput(identity, sorted);
	}

}

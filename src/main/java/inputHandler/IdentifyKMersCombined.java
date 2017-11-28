package inputHandler;

import java.util.HashSet;
import java.util.concurrent.Callable;

import elongation.Elongator;
import storage.SortedStorage;
import storage.Storage;

public class IdentifyKMersCombined implements Callable<IdentifyKMersCombined> {
	
	private String header = "";
	private String sequence = "";
	private Integer kMerSize = 17;
	private Integer numMismatches = 0;
	private Integer lengthThreshold = 0;
	private Boolean showAllSmall = false;
	private long offset = 0l;
	
	private SortedStorage sorted;
	
	private Integer threads;
	
	private Runner runner;
	
	public IdentifyKMersCombined(String header, String sequence, Integer k, Integer numMismatches, Integer lengthThreshold, Boolean showAllSmall, Runner runner, Integer threads, long offset){
		this.header = header;
		this.sequence = sequence;
		this.kMerSize = k;
		this.numMismatches = numMismatches;
		this.lengthThreshold = lengthThreshold;
		this.showAllSmall = showAllSmall;
		this.runner = runner;
		this.threads = threads;
		this.offset = offset;
	}

	@Override
	public IdentifyKMersCombined call() throws Exception {
		identifyKMers();
		// TODO Auto-generated method stub
		return this;
	}
	


	private void identifyKMers() {

		this.sorted = new SortedStorage(getKmers(this.sequence));

		System.out.println("Repetitive kmers for sequence " + header + " are identified.");
		addToCombinedStorage(header, sorted);
//		System.out.println("Output was generated");

	}
	


	private Storage<HashSet<Integer>> getKmers(String identity) {
		Elongator elongator = new Elongator(identity, (long)identity.length(), this.kMerSize, this.numMismatches, this.lengthThreshold, this.showAllSmall, this.threads, this.offset);
		
		try {
			elongator.initialize();
			elongator.findSubstringsOfWindowSizeWithoutRemoval();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return elongator.getRepeats();
	}
	

	
	/**
	 * @return the sorted
	 */
	public SortedStorage getSorted() {
		return sorted;
	}

	private void addToCombinedStorage(String identity, SortedStorage sorted) {
		this.runner.addToCombinedStorage(identity, sorted);
	}

}

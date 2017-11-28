package inputHandler;

import java.util.HashSet;
import java.util.concurrent.Callable;

import elongation.Elongator;
import storage.SortedStorage;
import storage.Storage;

public class IdentifyRepeatsSeparate implements Callable<IdentifyRepeatsSeparate> {
	
	private String header = "";
	private String sequence = "";
	private Integer kMerSize = 17;
	private Integer numMismatches = 0;
	private Integer lengthThreshold = 0;
	private Boolean showAllSmall = false;
	
	private Integer threads;
	
	private Runner runner;
	
	public IdentifyRepeatsSeparate(String header, String sequence, Integer k, Integer numMismatches, Integer lengthThreshold, Boolean showAllSmall, Runner runner, Integer threads){
		this.header = header;
		this.sequence = sequence;
		this.kMerSize = k;
		this.numMismatches = numMismatches;
		this.lengthThreshold = lengthThreshold;
		this.showAllSmall = showAllSmall;
		this.runner = runner;
		this.threads = threads;
	}

	@Override
	public IdentifyRepeatsSeparate call() throws Exception {
		processSequence();
		// TODO Auto-generated method stub
		return this;
	}
	


	private void processSequence() {

		SortedStorage sorted = new SortedStorage(getRepeats(this.sequence));

		System.out.println("Repetitive regions for sequence " + header + " are identified.");
		generateOutput(header, sorted);
		System.out.println("Output was generated");

	}
	


	private Storage<HashSet<Integer>> getRepeats(String identity) {
		Elongator elongator = new Elongator(identity, (long)identity.length(), this.kMerSize, this.numMismatches, this.lengthThreshold, this.showAllSmall, this.threads);
		
		try {
			elongator.initialize();
			elongator.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return elongator.getRepeats();
	}
	

	
	private void generateOutput(String identity, SortedStorage sorted) {
		this.runner.generateOutput(identity, sorted);
	}

}

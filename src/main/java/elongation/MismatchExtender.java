package elongation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import storage.Storage;

/**
 * 
 * @author Friederike Hanssen Class adds to each known repeat a mismatch marker,
 *         if the overall number of mismatches or sequence length is not
 *         exceeded.
 */

public class MismatchExtender extends Extender {

	private final int MISMATCH_THRESHOLD;
	private final long TOTAL_SEQ_LENGTH;

//	public MismatchExtender(int mismatchThreshold, long seqLength) {
//		this(mismatchThreshold, seqLength, 1);
//	}

	public MismatchExtender(int mismatchThreshold, long seqLength) {
		super();
		TOTAL_SEQ_LENGTH = seqLength;
		MISMATCH_THRESHOLD = mismatchThreshold;
	}

	@Override
	public void setParameters(Storage<HashSet<Integer>> repeats, Map<String, Set<Integer>> addables) {
		this.workingStorage = repeats;
		results = new HashMap<String, Set<Integer>>();
		usedEntries = null;
	}

	@Override
	public void elongate() {
		addMarker();
		validateMap(results);
	}

	/**
	 * Adds mismatch marker and all respective start indices as long as mismatch
	 * marker could be added in the first plce
	 */
	private void addMarker() {
		String[] repeats = workingStorage.getMapRepeats().keySet()
				.toArray(new String[workingStorage.getMapRepeats().keySet().size()]);

//		ExecutorService es = Executors.newFixedThreadPool(this.THREADS);

		for (int i = 0; i < repeats.length; i++) {

			String mismatch = addMismatchesToKey(repeats[i]);
			
//			 Only proceed if anything actually got added to the initial key
			if (!workingStorage.getMapRepeats().containsKey(mismatch)) {
				
				Set<Integer> startIndicesOfCurrentRepeat = workingStorage.getMapRepeats().get(repeats[i]);
				Set<Integer> startIndicesOfCurrentMismatch = new HashSet<Integer>();
				
				// Find all start indices of current mismatch
				for (Integer currentStartIndex : startIndicesOfCurrentRepeat) {
					if (mismatch.length() + currentStartIndex <= TOTAL_SEQ_LENGTH) {
						startIndicesOfCurrentMismatch.add(currentStartIndex);
					}
				}
				setResults(mismatch, startIndicesOfCurrentMismatch);
			}

//			es.submit(new AddMarkerToOneKmer(this, repeats[i]));

		}

//		es.shutdown();
//		try {
//			es.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

//		}

	}

	public synchronized void setResults(String mismatch, Set<Integer> startIndicesOfCurrentMismatch) {

			if (!startIndicesOfCurrentMismatch.isEmpty()) {
				if (results.containsKey(mismatch)) {
					startIndicesOfCurrentMismatch.addAll(results.get(mismatch));
				}
				results.put(mismatch, startIndicesOfCurrentMismatch);
			}
	}

	public String addMismatchesToKey(String key) {

		if (getNumberOfMutations(key) < MISMATCH_THRESHOLD) {
			key = key + MISMATCH_MARKER;
		}
		return key;
	}

	/**
	 * @return the tOTAL_SEQ_LENGTH
	 */
	public long getTOTAL_SEQ_LENGTH() {
		return TOTAL_SEQ_LENGTH;
	}

}

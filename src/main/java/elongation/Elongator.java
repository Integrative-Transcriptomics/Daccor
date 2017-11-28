package elongation;

import java.util.HashSet;
import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen This class directs steps needed for the elongation
 *         processes.
 *
 *
 */
public class Elongator {

	private Integer MISMATCH_THRESHOLD = 0;
	private Integer WIN_SIZE = 17;
	private String SEQ = "";
	private Long SEQ_LENGTH = 0l;
	private Integer MIN_LENGTH = 0;
	private boolean IS_SMALL = false;
	private long offset = 0l;

	private Storage<HashSet<Integer>> repeats;
	private Storage<HashSet<Integer>> orphanRepeats;
	private Storage<HashSet<Integer>> smallStuff;

	private Extender joinExtender;
	private Extender lowComplexMerger;
	private Extender mismatchExtender;
	private Extender repeatsMerger;

	private Integer numberOfAddedSubstrings;
	
	private Integer threads = 1;

	public Elongator(String seq, int winS, int mismatchThreshold, int minLengthOfResults, boolean isSmall) {
		this(seq, winS, mismatchThreshold, minLengthOfResults, isSmall, 1);
	}

	public Elongator(String seq, int winS, int mismatchThreshold, int minLengthOfResults, boolean isSmall, Integer threads) {
		this(seq, (long)seq.length(), winS, mismatchThreshold, minLengthOfResults, isSmall, threads);
	}
	
	public Elongator(String seq, long seqLength, int winS, int mismatchThreshold, int minLengthOfResults, boolean isSmall, Integer threads) {
		this(seq, seqLength, winS, mismatchThreshold, minLengthOfResults, isSmall, threads, 0l);
	}

	/**
	 * @param identity
	 * @param sequence length
	 * @param kMerSize
	 * @param numMismatches
	 * @param lengthThreshold
	 * @param showAllSmall
	 * @param threads2
	 * @param offset
	 */
	public Elongator(String seq, Long totalSeqLength, int winS, int mismatchThreshold, int minLengthOfResults, boolean isSmall, Integer threads, long offset) {
		SEQ = seq;
		this.SEQ_LENGTH = totalSeqLength;
		WIN_SIZE = winS;
		MISMATCH_THRESHOLD = mismatchThreshold;
		MIN_LENGTH = minLengthOfResults;
		IS_SMALL = isSmall;
		this.threads = threads;
		this.offset = offset;
	}

	public void initialize() {
		initialize(new WorkingStorage());

	}
	


	public void initialize(WorkingStorage storage) {
		repeats = storage;
		orphanRepeats = new WorkingStorage();

		smallStuff = new WorkingStorage();

		joinExtender = new JoinExtender(MISMATCH_THRESHOLD);
		lowComplexMerger = new LowComplexMerger();
		mismatchExtender = new MismatchExtender(MISMATCH_THRESHOLD, this.SEQ_LENGTH);
		repeatsMerger = new KmerMerger(IS_SMALL, this.threads);
		numberOfAddedSubstrings = 0;

	}

	/**
	 * Finds initial repeating kmers, merges to longest exact repeats, does a
	 * sanity check to ensure all exact repeats are feasible and adds mismatches
	 * subsequently. Finally the results are filtered
	 */
	public void run() {

		// find all repeating kmers
		findSubstringsOfWindowSize();

		// merge kmers to obtain longest possible repeating ones
		mergeKmers();
		// Merge low complex regions, such AAAA,AAAA, to AAAAA, even if this
		// only occurs once
		mergeLowComplexRegions();
		((WorkingStorage) repeats).removeEmptyEntries();

		// do a sanity check: it is checked, if each exact maximal repeat occurs
		// at the given position in the input sequence
		if (isSane()) {
			// add orphan repeats, kmers that were partially used in other
			// operations, but were not all were extended, thus
			// maintaining to possibly participate in mismatches
			((WorkingStorage) repeats).merge(orphanRepeats);
			// Iterate until convergence is reached
			do {

				addMismatches();
				addNewEntries();

				((WorkingStorage) repeats).removeStartIndicesOfMultipleRepeats(joinExtender.getUsedEntries());
				((WorkingStorage) repeats).removeEmptyEntries();
				((WorkingStorage) repeats).merge(((JoinExtender) joinExtender).getOrphans());

				numberOfAddedSubstrings = joinExtender.results.size() + mismatchExtender.results.size();
			} while (numberOfAddedSubstrings != 0);

			// removes terminal mismatches and filters for length
			filter();

		} else {
			System.err.println("Sth. went wrong whilst generating maximal exact repeats.");
			System.exit(0);
		}

	}

	/**
	 * storage of repeats already set
	 * Merges to longest exact repeats, does a
	 * sanity check to ensure all exact repeats are feasible and adds mismatches
	 * subsequently. Finally the results are filtered
	 */
	public void runMergeOnly() {

		// merge kmers to obtain longest possible repeating ones
		System.out.println("merging regions");
		mergeKmers();
		// Merge low complex regions, such AAAA,AAAA, to AAAAA, even if this
		// only occurs once
		System.out.println("merging low complex regions");
		mergeLowComplexRegions();
		((WorkingStorage) repeats).removeEmptyEntries();

		// do a sanity check: it is checked, if each exact maximal repeat occurs
		// at the given position in the input sequence
		if (isSane()) {
			// add orphan repeats, kmers that were partially used in other
			// operations, but were not all were extended, thus
			// maintaining to possibly participate in mismatches
			((WorkingStorage) repeats).merge(orphanRepeats);
			// Iterate until convergence is reached
			do {

				addMismatches();
				addNewEntries();

				((WorkingStorage) repeats).removeStartIndicesOfMultipleRepeats(joinExtender.getUsedEntries());
				((WorkingStorage) repeats).removeEmptyEntries();
				((WorkingStorage) repeats).merge(((JoinExtender) joinExtender).getOrphans());

				numberOfAddedSubstrings = joinExtender.results.size() + mismatchExtender.results.size();
			} while (numberOfAddedSubstrings != 0);
			
			System.out.println("filtering");

			// removes terminal mismatches and filters for length
			filter();

		} else {
			System.err.println("Sth. went wrong whilst generating maximal exact repeats.");
			System.exit(0);
		}

	}

	/**
	 * Scans input sequences, stores all kmers and removes ones that only occur
	 * once
	 */
	public void findSubstringsOfWindowSize() {
		for (int i = 0; i <= SEQ.length() - WIN_SIZE; i++) {
			String key = SEQ.substring(i, i + WIN_SIZE);
			((WorkingStorage) repeats).storeRepeatAtSingleIndex(key, i+(int)this.offset);
		}
		((WorkingStorage) repeats).removeEntriesOccuringAtSingleIndex();
		((WorkingStorage) repeats).removeEmptyEntries();
	}
	
	/**
	 * Scans input sequences, stores all kmers and removes ones that only occur
	 * once
	 */
	public void findSubstringsOfWindowSizeWithoutRemoval() {
		for (int i = 0; i <= SEQ.length() - WIN_SIZE; i++) {
			String key = SEQ.substring(i, i + WIN_SIZE);
			((WorkingStorage) repeats).storeRepeatAtSingleIndex(key, i+(int)this.offset);
		}
	}

	/**
	 * Kmers are merged to maximal exact repeats. If not all kmers of a set
	 * could be merged, they are stored as orphans, as there might be a
	 * possibility to extend them with mismatches
	 */
	private void mergeKmers() {

		do {
			repeatsMerger.setParameters(repeats, null);
			repeatsMerger.elongate();
			((WorkingStorage) repeats).removeStartIndicesOfMultipleRepeats(repeatsMerger.getUsedEntries());
			((WorkingStorage) repeats).addMapOfEntries(repeatsMerger.getResults());
			((WorkingStorage) orphanRepeats).merge(((KmerMerger) repeatsMerger).getOrphans());
			((WorkingStorage) smallStuff).merge(((KmerMerger) repeatsMerger).getSmall());
			numberOfAddedSubstrings = repeatsMerger.getResults().size();
		} while (numberOfAddedSubstrings != 0);

	}

	/**
	 * Takes care of low complex regions by producing longest possible ones,
	 * even if it only occurs once
	 */
	private void mergeLowComplexRegions() {
		do {
			lowComplexMerger.setParameters(repeats, null);
			lowComplexMerger.elongate();
			((WorkingStorage) repeats).removeStartIndicesOfMultipleRepeats(lowComplexMerger.getUsedEntries());
			((WorkingStorage) repeats).addMapOfEntries(lowComplexMerger.getResults());
		} while (lowComplexMerger.getResults().size() > 0);
	}

	/**
	 * Checks if all exact repeats found and their start indices match back
	 * towards the original sequence
	 * 
	 * @return
	 */
	private boolean isSane() {

		String[] substrings = repeats.getMapRepeats().keySet()
				.toArray(new String[repeats.getMapRepeats().keySet().size()]);
		for (int i = 0; i < substrings.length; i++) {

			Integer[] startIndices = repeats.getMapRepeats().get(substrings[i])
					.toArray(new Integer[repeats.getMapRepeats().get(substrings[i]).size()]);
			//TODO sanity check for multi-chromosome mapping
//			for (int j = 0; j < startIndices.length; j++) {
//				if (!SEQ.regionMatches(startIndices[j], substrings[i], 0, substrings[i].length())) {
//					System.err.println("This substring was not in the sequence: " + substrings[i]);
//					System.out.println(startIndices[j]);
//					System.out.println(SEQ.substring(startIndices[j], startIndices[j]+substrings[i].length()));
//					return false;
//				}
//			}
		}
		return true;
	}

	/**
	 * Mismatch marker is added and attempted to be joined with exact repeats
	 */
	private void addMismatches() {

		mismatchExtender.setParameters(repeats, null);
		mismatchExtender.elongate();

		joinExtender.setParameters(repeats, mismatchExtender.getResults());
		joinExtender.elongate();
	}

	private void addNewEntries() {
		((WorkingStorage) repeats).addMapOfEntries(joinExtender.getResults());
		((WorkingStorage) repeats).addMapOfEntries(mismatchExtender.getResults());

	}

	/**
	 * filter out all final mismatch markers(AAA0000 -> AAAA), partial matches,
	 * and threshold
	 **/
	private void filter() {
		Filter filter = new Filter(repeats);
		filter.removeTerminalMismatches();
		filter.filterForPartialRepeats();
		if (IS_SMALL) {
			((WorkingStorage) repeats).merge(smallStuff);
			((WorkingStorage) repeats).merge(orphanRepeats);
		}
		filter.filterForThreshold(MIN_LENGTH);
		repeats = filter.getWorkingStorage();
	}

	public Storage<HashSet<Integer>> getRepeats() {
		return repeats;
	}

	public synchronized void saveToStorage(Storage<HashSet<Integer>> repeats2) {
		((WorkingStorage) repeats).merge(repeats2);
	}

}

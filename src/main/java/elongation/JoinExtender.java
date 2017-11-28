package elongation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 * This class compares all known repeats, to all the repeats with an added mismatch marker in order to find new feasible repetitive regions
 */

public class JoinExtender extends Extender{

	public final Integer MISMATCH_THRESHOLD;
	private Map<String,Set<Integer>> mismatches;
	
	private Storage<HashSet<Integer>> orphansStore;


	public JoinExtender(Integer mismatchThreshold) {
		super();
		MISMATCH_THRESHOLD = mismatchThreshold;
	}

	@Override
	public void setParameters(Storage<HashSet<Integer>> repeatsStorage, Map<String, Set<Integer>> mismatches) {

		this.orphansStore = new WorkingStorage();

		this.workingStorage = repeatsStorage;
		this.mismatches = mismatches;
		results = new HashMap<String, Set<Integer>>();
		usedEntries = new HashMap<String, Set<Integer>>();

	}

	@Override
	public void elongate() {
		findJoins();
//		validateUsedEntries();
	}

	/**
	 * All known repeats are compared to new repeats with added mismatch marker and if feasible joined
	 */
	private void findJoins() {
		
		String[] repeatsExtendedByMismatch = mismatches.keySet().toArray(new String[mismatches.keySet().size()]);
		for (int i = 0; i < repeatsExtendedByMismatch.length; i++) {
			
			String currRepeatWithMismatch = repeatsExtendedByMismatch[i];
			
			String[] repeats = workingStorage.getMapRepeats().keySet().toArray(new String[workingStorage.getMapRepeats().keySet().size()]);
			for (int j = 0; j < repeats.length; j++) {
				
				String currRepeat = repeats[j];
				if (joinable(currRepeatWithMismatch, currRepeat)) {
					join(currRepeatWithMismatch,currRepeat);
				}
			}

		}
	}
	
	/**
	 * Returns true if it is joinable for at least two positions, else false
	 * @param currMismatch
	 * @param currRepeat
	 * @return
	 */
	private boolean joinable(String currMismatch, String currRepeat) {

		if (getNumberOfMutations(currMismatch) + getNumberOfMutations(currRepeat) <= MISMATCH_THRESHOLD) {
			
			int counter = 0;
			Integer[] startIndicesOfMismatch = mismatches.get(currMismatch).toArray(new Integer[mismatches.get(currMismatch).size()]);
			
			for (int i = 0; i < startIndicesOfMismatch.length; i++) {
				if (particularPositionIsJoinable(currMismatch, currRepeat, startIndicesOfMismatch[i])) {
					counter++;
					if (counter > 1) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 *  Returns true if the particular position with given start indices is joinable
	 * @param currMismatch
	 * @param currRepeat
	 * @param startIndexOfMismatch
	 * @return
	 */
	private boolean particularPositionIsJoinable(String currMismatch, String currRepeat, Integer startIndexOfMismatch) {
		HashSet<Integer> startIndicesOfRepeat = workingStorage.getMapRepeats().get(currRepeat);
		if (startIndicesOfRepeat.contains((startIndexOfMismatch + currMismatch.length()))) {
			return true;
		}
		return false;
	}

	/**
	 * Two are joined and their startindices assigned to the results. the used ones are added to usedEntries
	 * @param currRepeatWithMismatch
	 * @param currRepeat
	 */
	private void join(String currRepeatWithMismatch, String currRepeat) {
		
		String joinedRepeat = currRepeatWithMismatch.concat(currRepeat);

		Set<Integer> startIndicesOfJoin; 
		if (results.containsKey(joinedRepeat)) {
			startIndicesOfJoin = results.get(joinedRepeat);
		}else{
			startIndicesOfJoin = new HashSet<Integer>();
		}

		Integer[] startIndicesOfRepeatWithMismatch = mismatches.get(currRepeatWithMismatch).toArray(new Integer[mismatches.get(currRepeatWithMismatch).size()]);
		for (int i = 0; i < startIndicesOfRepeatWithMismatch.length; i++) {
			if (particularPositionIsJoinable(currRepeatWithMismatch, currRepeat, startIndicesOfRepeatWithMismatch[i])) {
				startIndicesOfJoin.add(startIndicesOfRepeatWithMismatch[i]);
				Integer startIndexOfRepeat = startIndicesOfRepeatWithMismatch[i] + currRepeatWithMismatch.length();
				setUsedEntries(currRepeatWithMismatch, currRepeat, startIndicesOfRepeatWithMismatch[i], startIndexOfRepeat);
			}
		}
		results.put(joinedRepeat, startIndicesOfJoin);
		
	}
	/**
	 * Check if any orphans
	 */
	private void validateUsedEntries() {

		// not all entries in working Storage are used, meaning there are some
		// unextendable ones
		if (!usedEntries.equals(workingStorage)) {
			String[] usedRepeats = usedEntries.keySet().toArray(new String[usedEntries.keySet().size()]);
			for (int i = 0; i < usedRepeats.length; i++) {

				// so if not all indices for a key are used, then store all
				// indices as orphans for later use
				if (!usedEntries.get(usedRepeats[i]).equals(workingStorage.getMapRepeats().get(usedRepeats[i]))) {
					((WorkingStorage) orphansStore).storeRepeatAtSetOfIndices(usedRepeats[i],
							workingStorage.getMapRepeats().get(usedRepeats[i]));
				}
			}
		}

		if (orphansStore.getMapRepeats().equals(results) || results.isEmpty()) {
			orphansStore = new WorkingStorage();
		}

	}

	public Storage<HashSet<Integer>> getOrphans() {
		return orphansStore;
	}

}

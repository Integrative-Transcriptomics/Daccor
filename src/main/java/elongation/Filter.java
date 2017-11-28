package elongation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen This class applies all the filters needed. The
 *         length threshold, the removal of final mismatches
 */

public class Filter {

	private static final char MISMATCH_MARKER = '0';

	private Storage<HashSet<Integer>> workingStorage;

	public Filter(Storage<HashSet<Integer>> workingStorage) {
		this.workingStorage = workingStorage;
	}

	public Storage<HashSet<Integer>> getWorkingStorage() {
		return workingStorage;
	}

	/**
	 * removes all terminal mutations: AAA000 -> AAA, A0A000-> A0A
	 * 
	 * @param workingStorage
	 * @return
	 */
	public void removeTerminalMismatches() {

		String[] repeats = this.workingStorage.getMapRepeats().keySet()
				.toArray((new String[this.workingStorage.getMapRepeats().keySet().size()]));

		for (int i = 0; i < repeats.length; i++) {

			String finalSubstring = repeats[i];
			while (finalSubstring.charAt(finalSubstring.length() - 1) == (MISMATCH_MARKER)) {
				finalSubstring = finalSubstring.substring(0, finalSubstring.length() - 1);

			}
			Set<Integer> startIndices = this.workingStorage.getMapRepeats().get(repeats[i]);
			((WorkingStorage) this.workingStorage).removeEntry(repeats[i]);
			((WorkingStorage) this.workingStorage).storeRepeatAtSetOfIndices(finalSubstring, startIndices);
		}

	}

	public void filterForPartialRepeats() {

		Map<String, Set<Integer>> contained = new HashMap<String, Set<Integer>>();
		Map<String, Set<Integer>> removables = new HashMap<String, Set<Integer>>();

		// iterate over all entries
		String[] repeats = this.workingStorage.getMapRepeats().keySet()
				.toArray((new String[this.workingStorage.getMapRepeats().keySet().size()]));
		for (int i = 0; i < repeats.length; i++) {

			// take startIndices of current substring
			Set<Integer> startIndicesOfRepeat = this.workingStorage.getMapRepeats().get(repeats[i]);

			// for each index this substring occurs at...AAAA->[4,8,17]
			for (Integer currStartIndexOfRepeat : startIndicesOfRepeat) {
				Map<String,Set<Integer>> temp = getPartialsAtThisIndex(repeats[i], currStartIndexOfRepeat);
				String[] entries = temp.keySet().toArray(new String[temp.keySet().size()]);
				for(int j = 0; j < entries.length; j++){
					Set<Integer> s = new HashSet<Integer>();
					if(contained.containsKey(entries[j])){
						s = contained.get(entries[j]);
					}
					s.addAll(temp.get(entries[j]));
					contained.put(entries[j],s);
				}
			}
		}
		
	
		// for each enty in contained: compare to entry in original, if same
		// then add to removables
		
		String[] c = contained.keySet().toArray(new String[contained.keySet().size()]);
		for(int i = 0; i < c.length; i++){
			if(contained.get(c[i]).equals(workingStorage.getMapRepeats().get(c[i]))){
				removables.put(c[i],contained.get(c[i]));
			}
		}
		
		((WorkingStorage)this.workingStorage).removeStartIndicesOfMultipleRepeats(removables);
	}

	private Map<String, Set<Integer>> getPartialsAtThisIndex(String repeat, Integer currStartIndexOfRepeat) {

		Map<String, Set<Integer>> contained = new HashMap<String, Set<Integer>>();
		// go along the substring and check for each each index if
		// another substring starts there
		for (int p = currStartIndexOfRepeat; p < (currStartIndexOfRepeat + repeat.length()); p++) {

			// if such a substring exists and lies WITHIN the current
			// one
			if (this.workingStorage.getMapIndex().containsKey(p)) {

				// get all sequences that start at p
				Set<String> partials = this.workingStorage.getMapIndex().get(p);

				for (String s : partials) {
					// check if any of the once starting at p are completely
					// contained, and the constellation occurs twice

					if (s.length() < repeat.length()) {
						{
							Set<Integer> removeInd;
							if (contained.containsKey(s)) {
								removeInd = contained.get(s);
							} else {
								removeInd = new HashSet<Integer>();
							}
							removeInd.add(p);
							contained.put(s, removeInd);
						}
					}
				}
			}
		}

		return contained;

	}

	/**
	 * Remove all repetitive regions shorter then a given threshold(user input)
	 * 
	 * @param workingStorage
	 * @param threshold
	 * @return
	 */
	public Storage<HashSet<Integer>> filterForThreshold(int threshold) {

		String[] repeats = workingStorage.getMapRepeats().keySet()
				.toArray((new String[workingStorage.getMapRepeats().keySet().size()]));
		for (int i = 0; i < repeats.length; i++) {
			if (repeats[i].length() < threshold) {
				((WorkingStorage) workingStorage).removeEntry(repeats[i]);
			}
		}

		return workingStorage;
	}

}

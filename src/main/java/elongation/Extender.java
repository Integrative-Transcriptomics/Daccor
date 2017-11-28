package elongation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import storage.Storage;

/**
 * 
 * @author Friederike Hanssen Abstract class specifying the methods that all
 *         extender and merger classes need to hold as well as methods needed
 *         for all classes.
 */

public abstract class Extender {

	protected static final char MISMATCH_MARKER = '0';

	protected Storage<HashSet<Integer>> workingStorage;
	protected Map<String, Set<Integer>> results;
	protected Map<String, Set<Integer>> usedEntries;
	protected Integer THREADS = 1;

	abstract public void elongate();

	abstract public void setParameters(Storage<HashSet<Integer>> repeats, Map<String, Set<Integer>> addables);

	public Storage<HashSet<Integer>> getStorage() {
		return workingStorage;
	}

	public Map<String, Set<Integer>> getResults() {
		return results;
	}

	public Map<String, Set<Integer>> getUsedEntries() {
		return usedEntries;
	}

	/**
	 * Checks if all entries in a map have values of size larger than 1
	 * 
	 * @param map
	 * @return
	 */
	protected Map<String, Set<Integer>> validateMap(Map<String, Set<Integer>> map) {
		String[] keys = map.keySet().toArray(new String[map.keySet().size()]);
		for (int i = 0; i < keys.length; i++) {
			if (map.get(keys[i]).size() < 2) {
				map.remove(keys[i]);
			}
		}
		return map;
	}

	/**
	 * Returns number of mutation in a given string
	 * 
	 * @param key
	 * @return
	 */
	protected int getNumberOfMutations(String key) {
		int counter = 0;
		for (int i = 0; i < key.length(); i++) {
			if (key.charAt(i) == (MISMATCH_MARKER)) {
				counter++;
			}
		}
		return counter;
	}

	protected void setUsedEntries(String first, String second, Integer startIndexFirst, Integer startIndexSecond){
		
		Set<Integer> startIndicesOfFirst;
		if (usedEntries.containsKey(first)) {
			 startIndicesOfFirst = usedEntries.get(first);
		}else{
			 startIndicesOfFirst = new HashSet<Integer>();
		}
		 startIndicesOfFirst.add(startIndexFirst);
		usedEntries.put(first, startIndicesOfFirst);

		Set<Integer> startIndicesOfSecond;
		if (usedEntries.containsKey(second)) {
			startIndicesOfSecond = usedEntries.get(second);
		}else{
			startIndicesOfSecond = new HashSet<Integer>();
		}
		startIndicesOfSecond.add(startIndexSecond);
		usedEntries.put(second, startIndicesOfSecond);
	}
}

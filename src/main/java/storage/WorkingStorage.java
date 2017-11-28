package storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Friederike Hanssen 
 */
/**
 * This class extends the Storage class. It holds substrings and their start
 * indices, not sorted. It can insert new substrings and new indices. It can
 * delete entries, merge two working storages, and add maps of entries.
 */

public class WorkingStorage extends Storage<HashSet<Integer>> {

	// Constructor
	public WorkingStorage() {
		super();
		this.mapRepeats = new HashMap<String, HashSet<Integer>>();
		this.mapIndex = new HashMap<Integer, Set<String>>();
	}

	//Copy Constructor
	public WorkingStorage(Storage<HashSet<Integer>> copy){
		super();
		this.mapRepeats = new HashMap<String,HashSet<Integer>>();
		copyMapRepeats(copy.mapRepeats);
		
		this.mapIndex = new HashMap<Integer,Set<String>>();
		copyMapIndex(copy.mapIndex);
	}
	
	private void copyMapRepeats(Map<String, HashSet<Integer>> copyMapRepeats) {
		String[] repeats = copyMapRepeats.keySet().toArray(new String[copyMapRepeats.keySet().size()]);
		for(int i = 0; i < repeats.length;i++){
			HashSet<Integer> temp = new HashSet<Integer>();
			for(Integer k : copyMapRepeats.get(repeats[i])){
				temp.add(k);
			}
			this.mapRepeats.put(repeats[i], temp);
		}
	}
	
	private void copyMapIndex(Map<Integer, Set<String>> copyMapIndex) {
		Integer[] startIndices = copyMapIndex.keySet().toArray(new Integer[copyMapIndex.keySet().size()]);
		for(int i = 0; i < startIndices.length;i++){
			HashSet<String> temp = new HashSet<String>();
			for(String k : copyMapIndex.get(startIndices[i])){
				temp.add(k);
			}
			this.mapIndex.put(startIndices[i], temp);
		}
		
	}
	
	
	// Adding entries:

	/**
	 * Adds a repeat as key with the respecting start index as value. The second
	 * map index -> repeat is updated
	 * 
	 * @param repeat
	 * @param startIndex
	 */
	public void storeRepeatAtSingleIndex(String repeat, Integer startIndex) {

		if (startIndex != null && repeat != null && !repeat.isEmpty() && startIndex >= 0) {
			HashSet<Integer> assignedStartIndices = getAssignedValues(repeat);
			assignedStartIndices.add(startIndex);
			this.mapRepeats.put(repeat, assignedStartIndices);

			updateIndexMap(startIndex, repeat);
		}
	}

	/**
	 * Adds a repeat key with multiple start indices as value. The second map
	 * index -> repeat is updated for all input parameters.
	 * 
	 * @param repeat
	 * @param startIndices:
	 *            Set of the respective start indices
	 */
	public void storeRepeatAtSetOfIndices(String repeat, Set<Integer> startIndices) {

		if (startIndices != null && !startIndices.isEmpty() && repeat != null && !repeat.isEmpty()) {
			HashSet<Integer> assignedStartIndices = getAssignedValues(repeat);
			assignedStartIndices.addAll(startIndices);
			this.mapRepeats.put(repeat, assignedStartIndices);

			updateIndexMap(assignedStartIndices, repeat);
		}
	}

	/**
	 * A map, containing repeats as keys and their respective start indices as
	 * values, can be added.
	 * 
	 * @param entries:
	 *            map with repeat -> set of start indices
	 */
	public void addMapOfEntries(Map<String, Set<Integer>> entries) {
		String[] repeats = entries.keySet().toArray(new String[entries.keySet().size()]);
		for (int i = 0; i < repeats.length; i++) {
			storeRepeatAtSetOfIndices(repeats[i], entries.get(repeats[i]));
		}

	}

	/**
	 * Two storages are merged
	 * 
	 * @param mergeable Storage
	 *      
	 */
	public void merge(Storage<HashSet<Integer>> mergeable) {

		if (mergeable.getMapRepeats() != null && mergeable.getMapIndex() != null && !mergeable.getMapRepeats().isEmpty()
				&& !mergeable.getMapIndex().isEmpty()) {
			String[] mergingRepeats = mergeable.getMapRepeats().keySet()
					.toArray(new String[mergeable.getMapRepeats().keySet().size()]);
			for (int i = 0; i < mergingRepeats.length; i++) {
				Set<Integer> startIndicesOfMergeable = mergeable.getMapRepeats().get(mergingRepeats[i]);
				storeRepeatAtSetOfIndices(mergingRepeats[i], startIndicesOfMergeable);
			}
		}

	}

	private HashSet<Integer> getAssignedValues(String repeat) {

		if (this.mapRepeats.containsKey(repeat)) {
			return this.mapRepeats.get(repeat);
		} else {
			return new HashSet<Integer>();
		}
	}

	private Set<String> getAssignedRepeats(int startIndex) {

		if (this.mapIndex.containsKey(startIndex)) {
			return this.mapIndex.get(startIndex);
		} else {
			return new HashSet<String>();
		}

	}

	private void updateIndexMap(int startIndex, String repeat) {

		Set<String> assignedRepeats = getAssignedRepeats(startIndex);
		assignedRepeats.add(repeat);
		mapIndex.put(startIndex, assignedRepeats);
	}

	private void updateIndexMap(HashSet<Integer> startIndices, String repeat) {
		for (Integer i : startIndices) {
			updateIndexMap(i, repeat);
		}
	}

	// Removing entries

	/**
	 * All repeat keys, which only have a single start index assigned to them
	 * are removed
	 */
	public void removeEntriesOccuringAtSingleIndex() {
		String[] repeats = this.mapRepeats.keySet().toArray(new String[this.mapRepeats.keySet().size()]);
		for (int i = 0; i < repeats.length; i++) {
		
			if (this.mapRepeats.get(repeats[i]).size() <= 1) {
				// since the set of startIndices has at most size 1,
				// iterator.next returns this startIdices
				// Remove in this order, to have all needed information
				// available
				removeRepeatFromMapIndex(this.mapRepeats.get(repeats[i]).iterator().next(), repeats[i]);
				this.mapRepeats.remove(repeats[i]);
			}
		}
		removeEmptyEntries();
	}

	/**
	 * Entries in mapRepeats and mapIndex with no assigned value are removed
	 */
	public void removeEmptyEntries() {
		String[] repeats = this.mapRepeats.keySet().toArray(new String[this.mapRepeats.keySet().size()]);
		for (int i = 0; i < repeats.length; i++) {
			if (this.mapRepeats.get(repeats[i]).isEmpty()) {
				this.mapRepeats.remove(repeats[i]);
			}
		}

		Integer[] startIndices = this.mapIndex.keySet().toArray(new Integer[this.mapIndex.keySet().size()]);
		for (int i = 0; i < startIndices.length; i++) {
			if (this.mapIndex.get(startIndices[i]).isEmpty()) {
				this.mapIndex.remove(startIndices[i]);
			}
		}

	}

	/**
	 * Removes certain start indices of a repeat in both maps
	 * 
	 * @param repeat
	 * @param startIndicesOfRepeat
	 */
	public void removeStartIndicesOfSingleRepeat(String repeat, Set<Integer> startIndicesToRemoveOfRepeat) {

		if (startIndicesToRemoveOfRepeat != null && !startIndicesToRemoveOfRepeat.isEmpty() && repeat != null
				&& !repeat.isEmpty() && this.mapRepeats.containsKey(repeat)) {

			// Remove from mapRepeats
			HashSet<Integer> allStartIndicesOfRepeat = this.mapRepeats.get(repeat);
			HashSet<Integer> remainingStartIndices = new HashSet<Integer>();

			for (Integer i : allStartIndicesOfRepeat) {
				if (!startIndicesToRemoveOfRepeat.contains(i)) {
					remainingStartIndices.add(i);
				}
			}
			this.mapRepeats.put(repeat, remainingStartIndices);

			// Update mapIndex
			for (Integer i : startIndicesToRemoveOfRepeat) {
				removeRepeatFromMapIndex(i, repeat);
			}
		}
	}

	/**
	 * Removes a map of entries with specific start indices and updates both
	 * maps
	 * 
	 * @param entries
	 *            map with repeatToRemove -> startIndicesToRemove
	 */
	public void removeStartIndicesOfMultipleRepeats(Map<String, Set<Integer>> entries) {
		if (entries != null && !entries.isEmpty()) {
			String[] repeats = entries.keySet().toArray(new String[entries.keySet().size()]);
			for (int i = 0; i < repeats.length; i++) {
				removeStartIndicesOfSingleRepeat(repeats[i], entries.get(repeats[i]));
			}
		}
		removeEmptyEntries();
	}

	/**
	 * A particular repeat is removed in both maps with all its start indices
	 * 
	 * @param repeatToRemove
	 */
	public void removeEntry(String repeatToRemove) {
		if (repeatToRemove != null && !repeatToRemove.isEmpty() && this.mapRepeats.containsKey(repeatToRemove)) {

			HashSet<Integer> assignedStartIndices = this.mapRepeats.get(repeatToRemove);

			this.mapRepeats.remove(repeatToRemove);

			for (Integer i : assignedStartIndices) {
				removeRepeatFromMapIndex(i, repeatToRemove);
			}

			removeEmptyEntries();
		}

	}

	private void removeRepeatFromMapIndex(Integer startIndex, String repeatToRemove) {
		Set<String> repeatsAtIndex = this.mapIndex.get(startIndex);
		repeatsAtIndex.remove(repeatToRemove);
		this.mapIndex.put(startIndex, repeatsAtIndex);
	}

}

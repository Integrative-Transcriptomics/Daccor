package storage;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Friederike Hanssen
 */
/**
 * This class takes a storage object and sorts it. mapRepeats has their values
 * sorted by size as a tree set and their keys by first occurrence in the genome
 * (e.g. by first value in tree set). mapIndex has their sort their keys by size.
 * The values are not sorted, as in the final need for SortedStorage, each index
 * will only have a single value.
 *
 */

public class SortedStorage extends Storage<TreeSet<Integer>> {
	
	public SortedStorage(Storage<HashSet<Integer>> store) {
		super();
		
		Storage<HashSet<Integer>> copiedWorkingStore = new WorkingStorage(store);
		
		this.mapRepeats = new LinkedHashMap<String, TreeSet<Integer>>();
		initializeMapRepeats(copiedWorkingStore);
		
		this.mapIndex = new TreeMap<Integer, Set<String>>();
		this.mapIndex.putAll(store.getMapIndex());
	}

	//Sort mapRepeats 
	private void initializeMapRepeats(Storage<HashSet<Integer>> unsortedStore) {
		
		String[] repeats = unsortedStore.getMapRepeats().keySet().toArray(new String[unsortedStore.getMapRepeats().keySet().size()]);
		Map<String, TreeSet<Integer>> sortedMap = new HashMap<String, TreeSet<Integer>>();
		
		//Create TreeSet containing sorted start indices
		for (int i = 0; i < repeats.length; i++) {
			TreeSet<Integer> sortedIndices = new TreeSet<Integer>(unsortedStore.getMapRepeats().get(repeats[i]));
			sortedMap.put(repeats[i], sortedIndices);
		}
		
		//Sort all entries by the smallest start indices
		List<Map.Entry<String,TreeSet<Integer>>> list = new LinkedList<Map.Entry<String,TreeSet<Integer>>>(sortedMap.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String, TreeSet<Integer>>>(){
			public int compare(Map.Entry<String, TreeSet<Integer>> o1,
					Map.Entry<String,TreeSet<Integer>> o2){
				return (o1.getValue().first().compareTo(o2.getValue().first()));
			}
		});
		
		
		for(Map.Entry<String, TreeSet<Integer>> entry :list){
			this.mapRepeats.put(entry.getKey(), entry.getValue());
		}
	}

}

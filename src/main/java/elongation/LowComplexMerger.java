package elongation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import storage.Storage;

/**
 * 
 * @author Friederike Hanssen
 * This class merges two repetitive regions if the startindices of the repetitive regions, which are exactly the same, follow each other.
 */

public class LowComplexMerger extends Extender {
	
	public LowComplexMerger() {
		super();
	}
	
	@Override
	public void setParameters(Storage<HashSet<Integer>> repeats, Map<String, Set<Integer>> mismatches) {
		this.workingStorage = repeats;
		results = new HashMap<String, Set<Integer>>();
		usedEntries = new HashMap<String, Set<Integer>>();
	}
	
	@Override
	public void elongate() {
		findLowComplex();
	}

	private void findLowComplex() {
	
		String[] repeats = workingStorage.getMapRepeats().keySet().toArray(new String[workingStorage.getMapRepeats().keySet().size()]);
		for (int i = 0; i < repeats.length; i++) {
			
			Set<Integer> newStartIndices;
			Set<Integer> startIndicesOfCurrentRepeat = workingStorage.getMapRepeats().get(repeats[i]);
			for (Integer currentStartIndexOfCurrentReapet : startIndicesOfCurrentRepeat) {
				
				//if next index is also assigned to same repeat
				if (startIndicesOfCurrentRepeat.contains(currentStartIndexOfCurrentReapet + 1)) {
					
					//Then add last position
					String lowComplexRepeat = repeats[i].concat(repeats[i].substring(repeats[i].length() - 1));
					
					//Add new gained sequence and update results and usedEntries
					if (results.containsKey(lowComplexRepeat)) {
						newStartIndices = results.get(lowComplexRepeat);
					}else{
						newStartIndices = new HashSet<Integer>();
					}
					newStartIndices.add(currentStartIndexOfCurrentReapet);
					results.put(lowComplexRepeat, newStartIndices);
					setUsedEntries(repeats[i], repeats[i], currentStartIndexOfCurrentReapet, currentStartIndexOfCurrentReapet+1);
				}
			}
		}

	}

}

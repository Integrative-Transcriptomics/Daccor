package elongation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import storage.Storage;


public abstract class UtilExtender {
	
	protected static final char MISMATCH_MARKER = '0';
	
	protected Storage<HashSet<Integer>> repeats;
	protected Map<String,Set<Integer>> results;
	protected Map<String,Set<Integer>> usedEntries;
	
	abstract public void elongate();
	abstract public void setParameters(Storage<HashSet<Integer>> repeats);
	abstract public void setMismatches(Map<String, Set<Integer>> mismatches);
	
	public Storage<HashSet<Integer>> getRepeats() {
		return repeats;
	}
	
	public Map<String,Set<Integer>> getResults(){
		return results;
	}
	
	public Map<String,Set<Integer>> getUsedEntries(){
		return usedEntries;
	}

	protected Map<String,Set<Integer>> validateMap(Map<String,Set<Integer>> map){
		String[] keys = map.keySet().toArray(new String[map.keySet().size()]);
		for(int i = 0; i <keys.length; i++){
			if(map.get(keys[i]).size() < 2){
				map.remove(keys[i]);
			}
		}
		return map;
	}
	
	protected int getNumberOfMutations(String key) {
		int counter = 0;
		for (int i = 0; i < key.length(); i++) {
			if (key.charAt(i) == (MISMATCH_MARKER)) {
				counter++;
			}
		}
		return counter;
	}
}
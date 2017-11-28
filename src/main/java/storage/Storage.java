package storage;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Friederike Hanssen
 * This abstract class contains the printing methods for the storage classes, as well as some generic getters. 
 * Classes extending the Storage class need to hold two hashmaps, one mapping from substring to a set of start indices,
 * one mapping from start index to a set of substrings. They also need to specifiy, what type of integer set they hold
 * Additionally, it overrides the hashCode and the equals methods.
 * 
 * @param T: type of integer set: treeSet for sorted, hashset for unsorted
 */

public abstract class Storage<T> {
	
	protected Map<String,T> mapRepeats;
	protected Map<Integer,Set<String>> mapIndex;
	
	public void printMapRepeats(){
		String[] repeats = mapRepeats.keySet().toArray(new String[mapRepeats.keySet().size()]);
		for(int i = 0; i < repeats.length; i++){
			System.out.println(repeats[i] + " ---> " + mapRepeats.get(repeats[i]).toString());
		}
	}
	
	public void printMapIndex(){
		Integer[] indices = mapIndex.keySet().toArray(new Integer[mapIndex.keySet().size()]);
		for(int i = 0; i < indices.length; i++){
			System.out.println(indices[i] + " ---> " + mapRepeats.get(indices[i]).toString());
		}
	}

	
	public Map<String, T> getMapRepeats() {
		return mapRepeats;
	}
	

	public Map<Integer, Set<String>> getMapIndex() {
		return mapIndex;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mapIndex == null) ? 0 : mapIndex.hashCode());
		result = prime * result + ((mapRepeats == null) ? 0 : mapRepeats.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Storage other = (Storage) obj;
		if (mapIndex == null) {
			if (other.mapIndex != null)
				return false;
		} else if (!mapIndex.equals(other.mapIndex))
			return false;
		if (mapRepeats == null) {
			if (other.mapRepeats != null)
				return false;
		} else if (!mapRepeats.equals(other.mapRepeats))
			return false;
		return true;
	}

}

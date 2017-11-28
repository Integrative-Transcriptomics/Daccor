package outputUtils;

import java.util.Map;
import java.util.TreeSet;

import storage.Storage;
import utilities.Utilities;

/**
 * 
 * @author Friederike Hanssen
 * Computes general statistics about repeat set for each genome
 */

public class SimpleStats {

	private String name;
	Map<String, Long> offsets;

	private final Storage<TreeSet<Integer>> REPEATS;
	private int numberOfSequencesWithMutations = 0;
	private int numberOfRepeats = 0;
	private int minimumLength = 0;
	private int maximumLength = 0;
	private int averageLength = 0;
	private int overallNumberOfSequences = 0;
	private int numberMatchingWithOthersequences = 0;

	public SimpleStats(Storage<TreeSet<Integer>> repeats, String name, Map<String, Long> offsets) {
		this.REPEATS = repeats;
		this.name = name;
		this.offsets = offsets;
	}

	public void generate() {
		setOverallNumberOfSequences();
		setNumberOfSequencesWithMutations();
		setNumberOfRepeats();
		setExtremValues();
		setAverageLength();
		setNumberMatchingWithOters();
	}

	private void setOverallNumberOfSequences() {
		String[] keys = REPEATS.getMapRepeats().keySet().toArray(new String[REPEATS.getMapRepeats().keySet().size()]);
		for (int i = 0; i < keys.length; i++) {
			if(this.offsets.containsKey(this.name)){
				for(Integer index: REPEATS.getMapRepeats().get(keys[i])){
					if(this.name.equals(Utilities.findBelongingSequenceName(offsets, index))){
						overallNumberOfSequences++;
					}
				}
			}else{
				overallNumberOfSequences += REPEATS.getMapRepeats().get(keys[i]).size();
			}
		}		
	}

	private void setNumberOfSequencesWithMutations() {
		String[] keys = REPEATS.getMapRepeats().keySet().toArray(new String[REPEATS.getMapRepeats().keySet().size()]);
		if(this.offsets.containsKey(this.name)){
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].contains("0") && Utilities.belongsToMe(this.name,keys[i],REPEATS, offsets)) {
					numberOfSequencesWithMutations++;
				}
			}
		}else{
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].contains("0")) {
					numberOfSequencesWithMutations++;
				}
			}
		}
	}

	private void setNumberOfRepeats() {
		if(this.offsets.containsKey(this.name)){
			String[] keys = REPEATS.getMapRepeats().keySet().toArray(new String[REPEATS.getMapRepeats().keySet().size()]);
			for (int i = 0; i < keys.length; i++) {
				if (Utilities.belongsToMe(this.name,keys[i],REPEATS, offsets)) {
					numberOfRepeats++;
				}
			}
		}else{
			numberOfRepeats = REPEATS.getMapRepeats().size();
		}
	}

	private void setExtremValues() {

		String[] substrings = REPEATS.getMapRepeats().keySet()
				.toArray(new String[REPEATS.getMapRepeats().keySet().size()]);
		if (substrings.length > 0) {
			int min = substrings[0].length();
			int max = substrings[0].length();
			for (int i = 1; i < substrings.length; i++) {
				if(this.offsets.containsKey(this.name) && !Utilities.belongsToMe(this.name,substrings[i],REPEATS, offsets)){
					continue;
				}

				int currLength = substrings[i].length();
				if (currLength < min)
					min = currLength;
				if (currLength > max)
					max = currLength;
			}
			minimumLength = min;
			maximumLength = max;
		}
	}

	private void setAverageLength() {

		String[] substrings = REPEATS.getMapRepeats().keySet()
				.toArray(new String[REPEATS.getMapRepeats().keySet().size()]);
		double sum = 0;
		int totalNumberOfStrings = 0;
		for (int i = 0; i < substrings.length; i++) {
			if(this.offsets.containsKey(this.name) && !Utilities.belongsToMe(this.name,substrings[i],REPEATS, offsets)){
				continue;
			}
			double currValue = substrings[i].length() * REPEATS.getMapRepeats().get(substrings[i]).size();
			totalNumberOfStrings = totalNumberOfStrings + REPEATS.getMapRepeats().get(substrings[i]).size();
			sum += currValue;
		}
		if (totalNumberOfStrings > 0) {
			averageLength = (int) sum / totalNumberOfStrings;
		}
	}

	private void setNumberMatchingWithOters(){
		if(this.offsets.containsKey(this.name)){
			String[] keys = REPEATS.getMapRepeats().keySet().toArray(new String[REPEATS.getMapRepeats().keySet().size()]);
			for (int i = 0; i < keys.length; i++) {
				if(Utilities.belongsToMe(this.name,keys[i],REPEATS, offsets)){
					for(Integer index: REPEATS.getMapRepeats().get(keys[i])){
						if(!this.name.equals(Utilities.findBelongingSequenceName(offsets, index))){
							this.numberMatchingWithOthersequences++;
							break;
						}
					}
				}
			}
		}
	}

	public int getOverallNumberOfSequences() {
		return overallNumberOfSequences;
	}

	public int getNumberOfRepeats() {
		return numberOfRepeats;
	}

	public int getMinimumLength() {
		return minimumLength;
	}

	public int getMaximumLength() {
		return maximumLength;
	}

	public int getAverageLength() {
		return averageLength;
	}

	public int getNumberOfSequencesWithMutations() {
		return numberOfSequencesWithMutations;
	}

	public int getNumberMatchingWithOtherSequences(){
		return this.numberMatchingWithOthersequences;
	}
}

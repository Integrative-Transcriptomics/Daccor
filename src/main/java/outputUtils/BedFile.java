package outputUtils;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import storage.Storage;
import utilities.Utilities;

/**
 * 
 * @author Friederike Hanssen 
 * 
 * 		Class generates bed files out of the obtained repetitive
 *         regions with the following fields:
 *         chrom,chromStart,chromEnd,name,score,strand,thickStart,thickEnd,itemRgb,blockCount,blockSizes,blockStarts
 *         Example: fasta name, startPos,endPos, fasta name, 0, +,
 *         chromStart,chromEnd, off, numberOfRepeats, length listed as many
 *         times as occurs, allStarting positions of this repeat
 */
public class BedFile {
	
	private Map<Integer, String[]> bedEntries;
	private final Storage<TreeSet<Integer>> sorted;
	private final Integer numberOfColumns = 6;
	private final String SEQ_NAME;

	public BedFile(Storage<TreeSet<Integer>> sorted, String seqName) {
		this.sorted = sorted;
		this.bedEntries = new TreeMap<Integer, String[]>();
		this.SEQ_NAME = seqName;

	}

	public void generate(Map<String, Long> offsets) {
		String[] substrings = sorted.getMapRepeats().keySet().toArray(new String[sorted.getMapRepeats().keySet().size()]);

		for (int i = 0; i < substrings.length; i++) {
			Integer[] startIndices = sorted.getMapRepeats().get(substrings[i])
					.toArray(new Integer[sorted.getMapRepeats().get(substrings[i]).size()]);
			for (int j = 0; j < startIndices.length; j++) {
				Long offset = 0L;
				if(offsets.containsKey(this.SEQ_NAME)){
					offset = offsets.get(this.SEQ_NAME);
					String belongsTo = Utilities.findBelongingSequenceName(offsets, startIndices[j]);
					if(!this.SEQ_NAME.equals(belongsTo)){
						continue;
					}
				}
				String[] temp = new String[numberOfColumns];
				temp[0] = SEQ_NAME.substring(1);
				temp[1] = ""+((long)startIndices[j]-offset);
				temp[2] = String.valueOf(((long)startIndices[j]-offset) + substrings[i].length());
				temp[3] = "repeat_" + i;
				temp[4] = "0";
				temp[5] = "+";
				bedEntries.put(startIndices[j], temp);
			}
		}

	}

	public Map<Integer, String[]> getValues() {
		return bedEntries;
	}

}

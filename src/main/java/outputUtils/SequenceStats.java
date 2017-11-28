package outputUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Friederike Hanssen
 * Computes statistics for each repeat
 *
 */
public class SequenceStats {
	
	private final MultiFastaOutput REPEATS;
	private final BedFile BEDFILE;
	private static final char MISMATCH_MARKER = '0';
	
	private String name;

	
	private Map<Integer,List<String>> sequenceStats;
	
	public SequenceStats(MultiFastaOutput repeats, BedFile bed, String name) {
		this.REPEATS = repeats;
		this.BEDFILE = bed;
		this.name = name;
		sequenceStats = new LinkedHashMap<Integer,List<String>>();
	}
	
	
	public void generate(Map<String, Long> offsets){
		
		Integer[] bedFileKeys = BEDFILE.getValues().keySet().toArray(new Integer[BEDFILE.getValues().keySet().size()]);
		for(int i = 0; i < bedFileKeys.length; i++){
			long offset = 0;
			if(offsets.containsKey(this.name)){
				offset = offsets.get(this.name);
			}

			String[] fastaNames = REPEATS.getOutput().keySet().toArray(new String[REPEATS.getOutput().keySet().size()]);
			for(int j= 0; j < fastaNames.length; j++){
				String bedFileKey = ""+((long)bedFileKeys[i] - offset);
				if(fastaNames[j].contains(bedFileKey)){
					
					List<String> currValues = new ArrayList<String>();
					
					//FastaName
					currValues.add(fastaNames[j].replaceAll("\\n", "").replace(">", ""));
					//Bedentry name
					currValues.add(BEDFILE.getValues().get(bedFileKeys[i])[3]);
					//startindex
					currValues.add(Integer.toString(bedFileKeys[i]));
					//endindex
					String seq = REPEATS.getOutput().get(fastaNames[j]);
					currValues.add(Integer.toString(seq.length() + bedFileKeys[i]));
					//repeat length
					currValues.add(Integer.toString(seq.length()));
					//number of mismatches
					currValues.add(Integer.toString(seq.length()- seq.replace(String.valueOf(MISMATCH_MARKER)," ").length()));
					//Mismatch positions
					currValues.add(getMismatchPositions(seq).toString().replace("[","").replace("]", ""));
					sequenceStats.put(bedFileKeys[i], currValues);
					
				}
			}
		}
		
	}
	
	private List<Integer> getMismatchPositions(String seq) {
		int currPosition = seq.indexOf(MISMATCH_MARKER);
		List<Integer> indices = new ArrayList<Integer>();
		while(currPosition >= 0){
			indices.add(currPosition);
			currPosition = seq.indexOf(MISMATCH_MARKER,currPosition+1);
		}
		return indices;
	}


	public Map<Integer,List<String>> getSequenceStats(){
		return sequenceStats;
	}
	

}

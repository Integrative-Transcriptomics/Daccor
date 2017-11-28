package outputUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import inputHandler.Runner;
import storage.Storage;
import utilities.Utilities;

/**
 * 
 * @author Friederike Hanssen Writes multifasta output
 *
 */
public class MultiFastaOutput {

	private final Storage<TreeSet<Integer>> REPEATS;
	private Map<String,String> output;
	private Map<String,String> outputSeparatelyWithMargin;
	private String name;
	private Integer margin;
	private Runner runner;

	public MultiFastaOutput(Storage<TreeSet<Integer>> repeats,String name, Integer margin, Runner runner){ 
		this.REPEATS = repeats;
		this.output = new LinkedHashMap<String,String>();
		this.outputSeparatelyWithMargin = new LinkedHashMap<String,String>();
		this.name = name;
		this.margin = margin;
		this.runner = runner;
	}

	public void generate(Map<String, Long> offsets){

		String[] sequences = REPEATS.getMapRepeats().keySet().toArray(new String[REPEATS.getMapRepeats().keySet().size()]);
		
		for(int i = 0; i < sequences.length; i++){
			String headerCont = name.concat("_pos_");
			
			String subSeqHeader = "";
			if(offsets.containsKey(name)){
				Set<Integer> startIndices = REPEATS.getMapRepeats().get(sequences[i]);
				StringBuffer startPositions = new StringBuffer();
				startPositions.append("[");
				boolean belongsToMe = false;
				for(Integer startIndex: startIndices){
					if(Utilities.belongsToMe(this.name, sequences[i], startIndex, offsets)){
						belongsToMe = true;
						String marginBefore = this.runner.getSequence(name, startIndex-this.margin, startIndex);
						String marginAfter = this.runner.getSequence(name, startIndex+sequences[i].length(), startIndex+sequences[i].length()+this.margin);
						String sequence = marginBefore+sequences[i]+marginAfter;
						String header = headerCont + "[" + startIndex + "]_length_"+sequence.length()+"_margin_"+this.margin+"_\n";
						this.outputSeparatelyWithMargin.put(header,sequence);
					}
					String belongsTo = Utilities.findBelongingSequenceName(offsets, startIndex);
					Long offset = offsets.get(belongsTo);
					if(!belongsTo.equals(name)){
						startPositions.append(belongsTo.substring(1)+":");
					}
					startPositions.append(startIndex-offset);
					startPositions.append(",");
				}
				if(!belongsToMe){
					continue;
				}
				startPositions.deleteCharAt(startPositions.length()-1);
				startPositions.append("]");
				subSeqHeader = headerCont.concat(startPositions.toString());
			}else{
				subSeqHeader = headerCont.concat(REPEATS.getMapRepeats().get(sequences[i]).toString());
			}
			subSeqHeader = subSeqHeader.replaceAll("\\s", "");
			String name = subSeqHeader.concat("_length_" + sequences[i].length() + "\n");
			output.put(name, sequences[i]);

		}
		

	}

	public Map<String, String> getOutput() {
		return output;
	}

	public Map<String, String> getOutputWithMargins() {
		return outputSeparatelyWithMargin;
	}
}
	

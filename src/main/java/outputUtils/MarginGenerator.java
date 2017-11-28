package outputUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import storage.AStorage;

/**
 * 
 * @author Friederike Hanssen 
 * Adds margin to each repetitive region
 *
 */

//TODO needs more work
public class MarginGenerator {

	private final AStorage<TreeSet<Integer>> REPEATS;
	private final String NAME;
	private final String SEQUENCE;
	
	private Map<String,String> output;


	public MarginGenerator(AStorage<TreeSet<Integer>> repeats,String name, String sequence){ 
		this.REPEATS = repeats;
		this.NAME = name;
		this.SEQUENCE = sequence;
		
		this.output = new LinkedHashMap<String,String>();
	}

	public void generate(){

		String[] repeats = REPEATS.getMapRepeats().keySet().toArray(new String[REPEATS.getMapRepeats().keySet().size()]);
		
		for(int i = 0; i < repeats.length; i++){
			String headerCont = NAME.concat("_pos_");
	
			//TODO

		}
		

	}

	public Map<String, String> getOutput() {
		return output;
	}
}

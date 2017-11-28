

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence6 {
	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	
	public Sequence6(){
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}
	
	public void setUp() {
		seq = "xxxxaaaaxxxxbbbbaaaaqqqqaaaaqqqq";
		setFinalResult();
		setFinalResultSmall();

	}

	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(8);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxx", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(4);
		startIndices.add(16);
		startIndices.add(24);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(16);
		startIndices.add(24);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("aaaaqqqq", startIndices);
	}	
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(8);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(4);
		startIndices.add(16);
		startIndices.add(24);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(16);
		startIndices.add(24);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("aaaaqqqq", startIndices);

	}
}

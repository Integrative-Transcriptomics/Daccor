

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence17{

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	public Sequence17() {
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}

	public void setUp() {
		seq = "aaaaqmxxxxwbbbbz123aaaatnxxxxybbbbq456";
		setFinalResult();
		setFinalResultSmall();

	}

	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(19);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("aaaa00xxxx0bbbb", startIndices);	
		
	}

	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(19);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("aaaa00xxxx0bbbb", startIndices);	
	}
	
}

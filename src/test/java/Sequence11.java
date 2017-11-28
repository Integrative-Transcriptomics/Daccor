

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence11 {
	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	public Sequence11(){
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}
	
	public void setUp() {
		seq = "xxxxcaaaa123xxxxbaaaa456xxxxdeeee";
		setFinalResult();
		setFinalResultSmall();

	}
	
	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxx0aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		startIndices.add(24);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxx", startIndices);
	}	
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx0aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		startIndices.add(24);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx", startIndices);
	}
}

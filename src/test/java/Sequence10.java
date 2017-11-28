

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence10 {
	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	
	public Sequence10(){
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();
	}
	
	public void setUp() {
		seq = "xxxxaaaa12345678xxxxbaaaa";
		setFinalResult();
		setFinalResultSmall();
		
	}
	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(16);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxx", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(4);
		startIndices.add(21);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("aaaa", startIndices);
		
	}	
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(16);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(4);
		startIndices.add(21);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("aaaa", startIndices);

	}
}

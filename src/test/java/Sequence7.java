

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence7 {
	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	public Sequence7(){
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}
	
	public void setUp() {
		seq = "xxxxaabbxxxxaaabqqqqbbbsqqqq";
		setFinalResult();
		setFinalResultSmall();

	}
	
	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(8);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxxaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(16);
		startIndices.add(24);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("qqqq", startIndices);
	}	
	

	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(8);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxxaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(16);
		startIndices.add(24);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqq", startIndices);

	}
}

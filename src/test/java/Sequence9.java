

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence9 {

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;


	public Sequence9() {
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}

	public void setUp() {
		seq = "xxxxcaaaa12345678xxxxbaaaa";
		setFinalResult();
		setFinalResultSmall();

	}

	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(17);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxx0aaaa", startIndices);
	}
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(17);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx0aaaa", startIndices);
	}
}



import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence15 {

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	public Sequence15() {
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}

	public void setUp() {
		seq = "abcde1234567fghijklmnoabcde9876543fghij";
		setFinalResult();
		setFinalResultSmall();

	}

	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(22);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("abcde0000000fghij", startIndices);

	}
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(22);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("abcde0000000fghij", startIndices);
	}

}

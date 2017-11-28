

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence1  {

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;
	
	public Sequence1() {
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();
	}

	public void setUp() {
		seq = "wxyzabcdwertzuiosdfgwxyzabcd";
		setFinalResult();
		setFinalResultSmall();
	}
		
	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(20);
		((WorkingStorage)finalResult).storeRepeatAtSetOfIndices("wxyzabcd", startIndices);

	}
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(20);
		((WorkingStorage)finalResult_small).storeRepeatAtSetOfIndices("wxyzabcd", startIndices);

	}
}



import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence19{

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;

	public Sequence19() {
		finalResult = new WorkingStorage();
	}

	public void setUp() {
		seq = "AAAATTTTCCCCAAAACCCCAAAATTTT123456789AAAA";
		setFinalResult();

	}

	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(20);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("AAAATTTT", startIndices);	
		startIndices = new HashSet<Integer>();
		startIndices.add(8);
		startIndices.add(16);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("CCCCAAAA", startIndices);	
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		startIndices.add(20);
		startIndices.add(37);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("AAAA", startIndices);	
		
		
		
	}

}

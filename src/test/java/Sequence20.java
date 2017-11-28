

import java.util.HashSet;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence20 {

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;

	public Sequence20() {
		finalResult = new WorkingStorage();
	}

	public void setUp() {
		seq = "TTTTTCAAAAACGCTTTTCCAAAAGGGTTTTTGAAAAA";
		setFinalResult();

	}

	private void setFinalResult() {
//		Set<Integer> startIndices = new HashSet<Integer>();
//		startIndices.add(0);
//		startIndices.add(19);
//		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("aaaa00xxxx0bbbb", startIndices);	
		
	}
}

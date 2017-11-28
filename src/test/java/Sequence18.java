

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence18{

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;

	public Sequence18() {
		finalResult = new WorkingStorage();
	}

	public void setUp() {
		seq = "TTTTTCAAAAACGCTTTTCCAAAAGGGTTTTTGAAAAAQQQTTTTTCAAAAA";
		setFinalResult();

	}

	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(19);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("", startIndices);	
		
	}

}



import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence16{

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	public Sequence16() {
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}

	public void setUp() {
		seq = "xxxxbaaaa123xxxxbqaaaa345xxxxdaaaa678xxxxeaaaa";
		setFinalResult();
		setFinalResultSmall();

	}

	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(25);
		startIndices.add(37);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxx0aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxxb", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(5);
		startIndices.add(18);
		startIndices.add(30);
		startIndices.add(42);

		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("aaaa", startIndices);
		
		
	}

	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(25);
		startIndices.add(37);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx0aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxxb", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		startIndices.add(25);
		startIndices.add(37);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(5);
		startIndices.add(18);
		startIndices.add(30);
		startIndices.add(42);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("aaaa", startIndices);
	}
}

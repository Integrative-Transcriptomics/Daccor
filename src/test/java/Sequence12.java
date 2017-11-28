

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence12 {
	
	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;
	
	public Sequence12(){
		
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}
	
	public void setUp() {
		seq = "xxxxbaaaa123xxxxbbaaaa456xxxxdaaaa";
		setFinalResult();
		setFinalResultSmall();

	}

	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(25);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxx0aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxxb", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(4);
		startIndices.add(17);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("baaaa", startIndices);
			
	}	
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(25);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx0aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxxb", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(5);
		startIndices.add(18);
		startIndices.add(30);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(4);
		startIndices.add(17);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("baaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		startIndices.add(25);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx", startIndices);
	}
}

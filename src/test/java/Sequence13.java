

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence13 {
	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	public Sequence13(){
		
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}
	
	public void setUp() {
		seq = "xxxxxbaaaaa123xxxxbbaaaa456xxxxxdaaaaa";
		setFinalResult();
		setFinalResultSmall();

	}
	
	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(27);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxxx0aaaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(1);
		startIndices.add(14);
		
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxxb", startIndices);
			
		startIndices = new HashSet<Integer>();
		startIndices.add(5);
		startIndices.add(19);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("baaaa", startIndices);
		
	}	
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(27);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxxx0aaaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(1);
		startIndices.add(14);
		
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxxb", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(1);
		startIndices.add(14);
		startIndices.add(27);
		startIndices.add(28);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(6);
		startIndices.add(7);
		startIndices.add(20);
		startIndices.add(33);
		startIndices.add(34);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("aaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(5);
		startIndices.add(19);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("baaaa", startIndices);
	}
}

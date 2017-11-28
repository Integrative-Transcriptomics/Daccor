

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence3 {

	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;
	
	public Sequence3(){
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}
	
	public void setUp() {
		seq = "xxxxaaaabbbbxxxxbbbbxxxxaaaa";
		setFinalResult();
		setFinalResultSmall();
	}
	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(20);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxxaaaa", startIndices);
				
		startIndices = new HashSet<Integer>();
		startIndices.add(8);
		startIndices.add(16);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("bbbbxxxx", startIndices);
		
	}	
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(20);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxxaaaa", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(12);
		startIndices.add(20);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(8);
		startIndices.add(16);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("bbbbxxxx", startIndices);

	}
}

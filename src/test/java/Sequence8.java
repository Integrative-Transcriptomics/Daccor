

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence8 {
	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;

	public Sequence8(){
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();

	}
	
	public void setUp() {
		seq = "xxxxaaaa123bbbbxxxxbbbbxxxxaaaa123";
		setFinalResult();
		setFinalResultSmall();
	}
	
	private void setFinalResult() {
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(23);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxxaaaa123", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(11);
		startIndices.add(19);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("bbbbxxxx", startIndices);
				
	}	
	
	private void setFinalResultSmall(){
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(23);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxxaaaa123", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(11);
		startIndices.add(19);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("bbbbxxxx", startIndices);
		
		startIndices = new HashSet<Integer>();
		startIndices.add(0);
		startIndices.add(15);
		startIndices.add(23);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxx", startIndices);

	}
}

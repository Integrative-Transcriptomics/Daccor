

import java.util.HashSet;
import java.util.Set;

import storage.Storage;
import storage.WorkingStorage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Sequence2{
	
	public static String seq;
	public static Storage<HashSet<Integer>> finalResult;
	public static Storage<HashSet<Integer>> finalResult_small;
	
	public Sequence2(){
		finalResult = new WorkingStorage();
		finalResult_small = new WorkingStorage();
	}
	
	public void setUp() {
		seq = "xxxxaaaaqqqqqqqqqqqqqqqxxxxaaaa";
		setFinalResult();
		setFinalResultSmall();
		
	}	
	
	private static void setFinalResult() {
		Set<Integer> setOfIndices = new HashSet<Integer>();
		setOfIndices.add(0);
		setOfIndices.add(23);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("xxxxaaaa", setOfIndices);
		
		setOfIndices = new HashSet<Integer>();
		setOfIndices.add(8);
		((WorkingStorage) finalResult).storeRepeatAtSetOfIndices("qqqqqqqqqqqqqqq", setOfIndices);
	}

	private void setFinalResultSmall(){
		Set<Integer> setOfIndices = new HashSet<Integer>();
		setOfIndices.add(0);
		setOfIndices.add(23);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("xxxxaaaa", setOfIndices);
		
		setOfIndices = new HashSet<Integer>();
		setOfIndices.add(8);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqqqqqqqqqqq", setOfIndices);
		setOfIndices.add(9);
		setOfIndices.add(10);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqqqqqqqqq", setOfIndices);
		setOfIndices.add(11);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqqqqqqqq", setOfIndices);
		setOfIndices.add(12);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqqqqqqq", setOfIndices);
		setOfIndices.add(13);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqqqqqq", setOfIndices);
		setOfIndices.add(14);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqqqqq", setOfIndices);
		setOfIndices.add(15);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqqqq", setOfIndices);
		setOfIndices.add(16);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqqq", setOfIndices);
		setOfIndices.add(17);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqqq", setOfIndices);
		setOfIndices.add(18);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqqq", setOfIndices);
		setOfIndices.add(19);
		((WorkingStorage) finalResult_small).storeRepeatAtSetOfIndices("qqqq", setOfIndices);
		

	}
	

}

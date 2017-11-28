

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import elongation.JoinExtender;
import storage.WorkingStorage;
import storage.Storage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class TestJoinExtender {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExtend() {
		
		Storage<HashSet<Integer>> repeats = new WorkingStorage();
		Map<String,Set<Integer>> mismatches = new HashMap<String,Set<Integer>>();
		//1.Join when twice occuring, only at mutation and #Mut doesn't exceed total
		
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 6);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 15);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("qqqq", 20);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("qqqq", 30);
		
		Set<Integer> mismatchStartIndices = new HashSet<Integer>();
		mismatchStartIndices.add(1);
		mismatchStartIndices.add(10);
		mismatches.put("xxxx0", mismatchStartIndices);
		
		mismatchStartIndices = new HashSet<Integer>();
		mismatchStartIndices.add(15);
		mismatchStartIndices.add(25);
		mismatches.put("b0bb0", mismatchStartIndices);

		JoinExtender joinExt = new JoinExtender(2);
		joinExt.setParameters(repeats,mismatches);
		joinExt.elongate();
		
		Map<String,Set<Integer>> expected_Results = new HashMap<String,Set<Integer>>();
		Set<Integer> expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(10);
		expected_Results.put("xxxx0aaaa", expectedStartIndices);
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(15);
		expectedStartIndices.add(25);
		expected_Results.put("b0bb0qqqq", expectedStartIndices);
		assertEquals(expected_Results,joinExt.getResults());
		
		Map<String,Set<Integer>> expected_Used = new HashMap<String,Set<Integer>>();
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(10);
		expected_Used.put("xxxx0", expectedStartIndices);
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(6);
		expectedStartIndices.add(15);
		expected_Used.put("aaaa", expectedStartIndices);
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(15);
		expectedStartIndices.add(25);
		expected_Used.put("b0bb0", expectedStartIndices);
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(20);
		expectedStartIndices.add(30);
		expected_Used.put("qqqq", expectedStartIndices);
		
		assertEquals(expected_Used,joinExt.getUsedEntries());

		//2. No joins expected
		repeats = new WorkingStorage();
		mismatches = new HashMap<String,Set<Integer>>();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 6);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 10);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 15);
		mismatchStartIndices = new HashSet<Integer>();
		mismatchStartIndices.add(15);
		mismatchStartIndices.add(25);
		mismatches.put("bbbb0", mismatchStartIndices);
		mismatchStartIndices = new HashSet<Integer>();
		mismatchStartIndices.add(20);
		mismatchStartIndices.add(30);
		mismatches.put("q0qq", mismatchStartIndices);
		
		
		joinExt = new JoinExtender(1);
		joinExt.setParameters(repeats,mismatches);
		joinExt.elongate();
		
		expected_Results = new HashMap<String,Set<Integer>>();
		assertEquals(expected_Results,joinExt.getResults());
		
		expected_Used = new HashMap<String,Set<Integer>>();
		assertEquals(expected_Used,joinExt.getUsedEntries());
	}

	@Test
	public void testSetParameters() {
	}

	@Test
	public void testJoinExtender() {
	}

}
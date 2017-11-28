

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import elongation.LowComplexMerger;
import storage.WorkingStorage;
import storage.Storage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class TestLowComplexMerger {

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
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("qqqq", 6);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("qqqq", 7);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("qqqq", 8);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("bbbb", 10);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("bbbb", 12);
		
		
		LowComplexMerger lowComplex = new LowComplexMerger();
		lowComplex.setParameters(repeats,null);
		lowComplex.elongate();
		
		Map<String,Set<Integer>> expectedResults = new HashMap<String,Set<Integer>>();
		Set<Integer> expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedResults.put("xxxxx", expectedStartIndices);
		
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(6);
		expectedStartIndices.add(7);
		expectedResults.put("qqqqq", expectedStartIndices);
		
		Map<String,Set<Integer>> expectedUsed = new HashMap<String,Set<Integer>>();
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(2);
		expectedUsed.put("xxxx", expectedStartIndices);
		
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(6);
		expectedStartIndices.add(7);
		expectedStartIndices.add(8);
		expectedUsed.put("qqqq", expectedStartIndices);
		
		assertEquals(expectedResults,lowComplex.getResults());
		assertEquals(expectedUsed,lowComplex.getUsedEntries());
	}

	@Test
	public void testSetParameters() {
	}

	@Test
	public void testLowComplexExtender() {
	}

}


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

import elongation.Extender;
import elongation.KmerMerger;
import storage.Storage;
import storage.WorkingStorage;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class TestKmerMerger {

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

		// 1. Extend by one with substrings of each startindex in relation to
		// original, check if all entries were used accordingly
		Storage<HashSet<Integer>> repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaat", 2);

		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 11);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaat", 12);
		
		KmerMerger merger = new KmerMerger(false, 1);
		merger.setParameters(repeats,null);
		merger.elongate();

		HashMap<String, Set<Integer>> expected_Used = new HashMap<String, Set<Integer>>();
		Set<Integer> expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(11);
		expected_Used.put("aaaa", expectedStartIndices);
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(2);
		expectedStartIndices.add(12);
		expected_Used.put("aaat", expectedStartIndices);

		assertEquals(expected_Used, merger.getUsedEntries());

		HashMap<String, Set<Integer>> expected_Results = new HashMap<String, Set<Integer>>();
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(11);
		expected_Results.put("aaaat", expectedStartIndices);
		assertEquals(expected_Results, merger.getResults());
		
		// 2. Try and extend but result only occurs once
		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaat", 2);

		merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		merger.elongate();

		expected_Used = new HashMap<String, Set<Integer>>();

		assertEquals(expected_Used, merger.getUsedEntries());

		expected_Results = new HashMap<String, Set<Integer>>();
		assertEquals(expected_Results, merger.getResults());

		// 4. Try and extend when indices would work but substrings don't
		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("wxyz", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("abc", 3);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("ef", 4);

		merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		merger.elongate();

		expected_Used = new HashMap<String, Set<Integer>>();
		assertEquals(expected_Used, merger.getUsedEntries());
		
		expected_Results = new HashMap<String, Set<Integer>>();
		assertEquals(expected_Results, merger.getResults());

		// 5. Store with no extendable substrings neither matching strings
		// nor indices
		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("wxyz", 4);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("abc", 8);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("ef", 6);

		merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		merger.elongate();

		expected_Used = new HashMap<String, Set<Integer>>();
		assertEquals(expected_Used, merger.getUsedEntries());
		
		expected_Results = new HashMap<String, Set<Integer>>();
		assertEquals(expected_Results, merger.getResults());

		// 6. Store where matching substrings would extend by more then one
		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaatt", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaatt", 11);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 6);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxa", 7);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 16);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxa", 17);

		merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		merger.elongate();

		expected_Used = new HashMap<String, Set<Integer>>();

		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(6);
		expectedStartIndices.add(16);
		expected_Used.put("xxxx", expectedStartIndices);
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(7);
		expectedStartIndices.add(17);
		expected_Used.put("xxxa", expectedStartIndices);
		assertEquals(expected_Used, merger.getUsedEntries());
		
		expected_Results = new HashMap<String, Set<Integer>>();
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(6);
		expectedStartIndices.add(16);
		expected_Results.put("xxxxa", expectedStartIndices);
		
		assertEquals(expected_Results, merger.getResults());

		// 7. Don't Extend when the same substring is used for multiple
		// extensions but the extensions don't occur
		// more then once
		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("baaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("baaa", 9);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("gaaa", 19);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 20);

		merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		merger.elongate();

		expected_Used = new HashMap<String, Set<Integer>>();
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(9);
		expected_Used.put("baaa", expectedStartIndices);
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(2);
		expectedStartIndices.add(10);
		expected_Used.put("aaaa", expectedStartIndices);

		assertEquals(expected_Used, merger.getUsedEntries());
		
		expected_Results = new HashMap<String, Set<Integer>>();
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(9);
		expected_Results.put("baaaa", expectedStartIndices);
		assertEquals(expected_Results, merger.getResults());

		// 11. Don't Extend when the same substring is used for multiple
		// extensions but the extensions don't occur
		// more then once
		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("baaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("daaa", 9);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("gaaa", 19);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 20);

		merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		merger.elongate();

		expected_Used = new HashMap<String, Set<Integer>>();

		assertEquals(expected_Used, merger.getUsedEntries());
		
		expected_Results = new HashMap<String, Set<Integer>>();
		assertEquals(expected_Results, merger.getResults());
	}

	@Test
	public void testSetParameters() {
	}

	@Test
	public void testRepeatsExtender() {
	}

	@Test
	public void testExtendByOne() {

		// 1. Try and extend when indices would work but substrings don't
		Storage<HashSet<Integer>>repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("wxyz", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("abc", 3);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("ef", 4);

		Extender merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		((KmerMerger) merger).mergeKmers();

		Map<String,Set<Integer>> expected_Results = new HashMap<String, Set<Integer>>();
		assertEquals(expected_Results, merger.getResults());

		// 2. Store with no extendable substrings neither matching strings
		// nor indices
		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("wxyz", 4);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("abc", 8);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("ef", 6);

		merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		((KmerMerger) merger).mergeKmers();

		expected_Results = new HashMap<String, Set<Integer>>();
		assertEquals(expected_Results, merger.getResults());

		// 3. Store where matching substrings would extend by more then one
		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaatt", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 6);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxa", 7);

		merger = new KmerMerger(false);
		merger.setParameters(repeats,null);
		((KmerMerger) merger).mergeKmers();

		expected_Results = new HashMap<String, Set<Integer>>();

		Set<Integer> expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(6);
		expected_Results.put("xxxxa", expectedStartIndices);
		assertEquals(expected_Results, merger.getResults());
	}

}
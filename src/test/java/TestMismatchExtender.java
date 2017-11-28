

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

import elongation.MismatchExtender;
import storage.WorkingStorage;
import storage.Storage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class TestMismatchExtender {

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
		
		//1. Test for no mutations, exceeding sequence and empty sequence
		Storage<HashSet<Integer>> repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 8);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("last", 24);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("", 2);
		
		MismatchExtender noMismatches = new MismatchExtender(0, 28);
		noMismatches.setParameters(repeats,null);
		noMismatches.elongate();
		
		Map<String,Set<Integer>> expected_NoMismatches = new HashMap<String,Set<Integer>>();
	
		assertEquals(expected_NoMismatches,noMismatches.getResults());
		
		
		//2.Test for one mutation
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("x0xx", 4);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("x0xx", 12);
		
		MismatchExtender oneMismatch = new MismatchExtender(1,28);
		oneMismatch.setParameters(repeats,null);
		oneMismatch.elongate();
		
		Map<String,Set<Integer>> expected_OneMismatch = new HashMap<String,Set<Integer>>();
		Set<Integer> expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(8);
		expected_OneMismatch.put("aaaa0", expectedStartIndices);
		assertEquals(expected_OneMismatch,oneMismatch.getResults());
		
		//3. Test for more then one mutation
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("x0x0", 16);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("x0x0", 20);

		MismatchExtender twoMismatches = new MismatchExtender(2,28);
		twoMismatches.setParameters(repeats,null);
		twoMismatches.elongate();
		
		Map<String,Set<Integer>> expected_TwoMismatches = new HashMap<String,Set<Integer>>();
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(1);
		expectedStartIndices.add(8);
		expected_TwoMismatches.put("aaaa0", expectedStartIndices);
		
		expectedStartIndices = new HashSet<Integer>();
		expectedStartIndices.add(4);
		expectedStartIndices.add(12);
		expected_TwoMismatches.put("x0xx0", expectedStartIndices);
		
		assertEquals(expected_TwoMismatches, twoMismatches.getResults());
		
		
	}

	@Test
	public void testSetParameters() {
		
	}

	@Test
	public void testMutationExtender() {
	}

}
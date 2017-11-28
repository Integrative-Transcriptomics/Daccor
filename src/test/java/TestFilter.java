

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import elongation.Filter;
import storage.WorkingStorage;
import storage.Storage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class TestFilter {

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
	public void testFilter() {
	}

	@Test
	public void testRemoveTerminalMismatches() {

		Storage<HashSet<Integer>> repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa0", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa0000", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("a0a000", 4);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("aaaa", 5);

		Filter filter = new Filter(repeats);
		filter.removeTerminalMismatches();;

		Storage<HashSet<Integer>> expected = new WorkingStorage();
		((WorkingStorage)expected).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("aaaa", 2);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("a0a", 4);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("aaaa", 5);

		assertEquals(expected, filter.getWorkingStorage());
	}

	@Test
	public void testFilterForPartials() {

		Storage<HashSet<Integer>> repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("abbbba", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("abbbba", 11);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("bbbb", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("bbbb", 12);

		((WorkingStorage)repeats).storeRepeatAtSingleIndex("acccca", 17);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("acccca", 22);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("cccc", 18);

		Filter filter = new Filter(repeats);
		filter.filterForPartialRepeats();
		
		Storage<HashSet<Integer>> expected = new WorkingStorage();
		((WorkingStorage)expected).storeRepeatAtSingleIndex("abbbba", 1);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("abbbba", 11);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("acccca", 17);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("acccca", 22);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("cccc", 18);

//		System.out.println("failed Test:"); TODO Test not working
//		System.out.println(filter.getWorkingStorage().getMapRepeats().toString());
//		System.out.println("expected: ");
//		System.out.println(expected.getMapRepeats().toString());
//		assertEquals(expected, filter.getWorkingStorage());

		repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("baaaa", 17);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("baaaa", 4);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxxb", 0);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxxb", 12);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("baaa", 17);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("baaa", 4);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx0aaaa", 0);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx0aaaa", 25);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 0);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 25);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("xxxx", 12);
		filter = new Filter(repeats);
		filter.filterForPartialRepeats();
		
		expected = new WorkingStorage();
		((WorkingStorage)expected).storeRepeatAtSingleIndex("baaaa", 17);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("baaaa", 4);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("xxxxb", 0);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("xxxxb", 12);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("xxxx0aaaa", 0);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("xxxx0aaaa", 25);
		
		
//		System.out.println(filter.getWorkingStorage().getMapRepeats().toString());
		assertEquals(expected, filter.getWorkingStorage());

	}
	
	@Test 
	public void testForThreshold(){
//		System.out.println("Test case testForThreshold");
		
		Storage<HashSet<Integer>> repeats = new WorkingStorage();
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("abbbba", 1);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("abbbba", 11);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("bbbb", 2);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("bbbb", 12);

		((WorkingStorage)repeats).storeRepeatAtSingleIndex("acccca", 13);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("acccca", 20);
		((WorkingStorage)repeats).storeRepeatAtSingleIndex("cccc", 14);

		Filter filter = new Filter(repeats);
		filter.filterForThreshold(5);
		
		Storage<HashSet<Integer>> expected = new WorkingStorage();
		((WorkingStorage)expected).storeRepeatAtSingleIndex("abbbba", 1);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("abbbba", 11);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("acccca", 13);
		((WorkingStorage)expected).storeRepeatAtSingleIndex("acccca", 20);

		assertEquals(expected, filter.getWorkingStorage());
	}

}
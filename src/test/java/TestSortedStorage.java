

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import storage.WorkingStorage;
import storage.SortedStorage;
import storage.Storage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class TestSortedStorage {

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
	public void testSortedStorage() {

		Storage<HashSet<Integer>> unsortedStore = new WorkingStorage();

		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aatt", 3);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("attt", 4);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("tttt", 5);

		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aatt", 12);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("attt", 13);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("tttt", 14);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("tttt", 20);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("tttt", 24);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("tttt", 18);

		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaaatttt", 1);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaaatttt", 10);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaatttt", 2);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaatttt", 11);


		
		Storage<TreeSet<Integer>> sortedStore = new SortedStorage(unsortedStore);

	}

	@Test
	public void testEqualsObject() {

		Storage<HashSet<Integer>> unsortedStore = new WorkingStorage();

		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aatt", 12);

		Storage<HashSet<Integer>> temp = new WorkingStorage(unsortedStore);
		Storage<TreeSet<Integer>> sortedStore1 = new SortedStorage(temp);
		Storage<TreeSet<Integer>> sortedStore2 = new SortedStorage(temp);

		assertTrue(sortedStore1.equals(sortedStore1));

		assertTrue(sortedStore1.getMapRepeats().equals(sortedStore2.getMapRepeats()));
		assertEquals(sortedStore1.getMapIndex(), sortedStore2.getMapIndex());

		assertTrue(sortedStore2.equals(sortedStore1));
		assertTrue(sortedStore1.equals(sortedStore2));
		assertEquals(sortedStore1, sortedStore2);
		assertFalse(sortedStore1.equals(unsortedStore));

		Storage<HashSet<Integer>> unsortedStore2 = new WorkingStorage();

		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaat", 2);

		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage) unsortedStore).storeRepeatAtSingleIndex("aatt", 12);

		temp = new WorkingStorage(unsortedStore2);
		Storage<TreeSet<Integer>> sortedStore3 = new SortedStorage(temp);
		assertFalse(sortedStore3.equals(sortedStore1));

	}

}
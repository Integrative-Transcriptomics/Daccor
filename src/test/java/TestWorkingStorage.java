

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import storage.WorkingStorage;
import storage.Storage;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class TestWorkingStorage {

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
	public void testStoreSubstringAtIndex() {

		Storage<HashSet<Integer>> actual = new WorkingStorage();
		
		
		((WorkingStorage)actual).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("aaaa", null);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("aaaa", 2);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("tttt", null);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("cccc", null);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("cccc", 1);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("cccc", 3);
		((WorkingStorage)actual).storeRepeatAtSingleIndex(null, null);
		((WorkingStorage)actual).storeRepeatAtSingleIndex(null, 1);

		Map<String, Set<Integer>> expectedString = new HashMap<String, Set<Integer>>();
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(1);
		startIndices.add(2);
		expectedString.put("aaaa", startIndices);
		startIndices = new HashSet<Integer>();
		startIndices.add(1);
		startIndices.add(3);
		expectedString.put("cccc", startIndices);

		Map<Integer, Set<String>> expectedInteger = new HashMap<Integer, Set<String>>();
		Set<String> strings = new HashSet<String>();
		strings.add("aaaa");
		strings.add("cccc");
		expectedInteger.put(1, strings);
		strings = new HashSet<String>();
		strings.add("aaaa");
		expectedInteger.put(2, strings);
		strings = new HashSet<String>();
		strings.add("cccc");
		expectedInteger.put(3, strings);

		assertEquals(expectedString, actual.getMapRepeats());
		assertEquals(expectedInteger, actual.getMapIndex());

	}

	@Test
	public void testStoreSubstringAtIndices() {

		Storage<HashSet<Integer>> actual = new WorkingStorage();
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(1);
		startIndices.add(2);
		((WorkingStorage)actual).storeRepeatAtSetOfIndices("aaaa", startIndices);

		((WorkingStorage)actual).storeRepeatAtSingleIndex("cccc", 5);
		startIndices = new HashSet<Integer>();
		startIndices.add(15);
		startIndices.add(20);
		((WorkingStorage)actual).storeRepeatAtSetOfIndices("cccc", startIndices);

		startIndices = new HashSet<Integer>();
		((WorkingStorage)actual).storeRepeatAtSetOfIndices("tttt", startIndices);
		((WorkingStorage)actual).storeRepeatAtSetOfIndices(null, startIndices);
		((WorkingStorage)actual).storeRepeatAtSetOfIndices("tttt", null);
		((WorkingStorage)actual).storeRepeatAtSetOfIndices(null, null);

		startIndices = new HashSet<Integer>();
		startIndices.add(25);
		startIndices.add(30);
		((WorkingStorage)actual).storeRepeatAtSetOfIndices("aaaa", startIndices);
		startIndices = new HashSet<Integer>();
		startIndices.add(25);
		startIndices.add(30);
		((WorkingStorage)actual).storeRepeatAtSetOfIndices("aaaat", startIndices);

		Map<String, Set<Integer>> expectedString = new HashMap<String, Set<Integer>>();
		startIndices = new HashSet<Integer>();
		startIndices.add(1);
		startIndices.add(2);
		startIndices.add(25);
		startIndices.add(30);
		expectedString.put("aaaa", startIndices);
		startIndices = new HashSet<Integer>();
		startIndices.add(5);
		startIndices.add(15);
		startIndices.add(20);
		expectedString.put("cccc", startIndices);
		startIndices = new HashSet<Integer>();
		startIndices.add(25);
		startIndices.add(30);
		expectedString.put("aaaat", startIndices);

		Map<Integer, Set<String>> expectedInteger = new HashMap<Integer, Set<String>>();
		Set<String> strings = new HashSet<String>();
		strings.add("aaaa");
		expectedInteger.put(1, strings);
		expectedInteger.put(2, strings);
		strings = new HashSet<String>();
		strings.add("aaaa");
		strings.add("aaaat");
		expectedInteger.put(25, strings);
		expectedInteger.put(30, strings);
		strings = new HashSet<String>();
		strings.add("cccc");
		expectedInteger.put(5, strings);
		expectedInteger.put(15, strings);
		expectedInteger.put(20, strings);

		assertEquals(expectedString, actual.getMapRepeats());
		assertEquals(expectedInteger, actual.getMapIndex());

	}

	@Test
	public void testAddEntries() {
		Storage<HashSet<Integer>> actual_PreFilled = new WorkingStorage();

		// aaaatt --> 1,10
		((WorkingStorage)actual_PreFilled).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_PreFilled).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_PreFilled).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_PreFilled).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_PreFilled).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_PreFilled).storeRepeatAtSingleIndex("aatt", 12);

		Map<String, Set<Integer>> entriesToAdd = new HashMap<String, Set<Integer>>();
		Set<Integer> indicesToAdd = new HashSet<Integer>();
		indicesToAdd.add(17);
		entriesToAdd.put("aaaa", indicesToAdd);
		indicesToAdd = new HashSet<Integer>();
		indicesToAdd.add(18);
		entriesToAdd.put("aaat", indicesToAdd);
		indicesToAdd = new HashSet<Integer>();
		indicesToAdd.add(19);
		entriesToAdd.put("aatt", indicesToAdd);

		((WorkingStorage)actual_PreFilled).addMapOfEntries(entriesToAdd);

		Storage<HashSet<Integer>> expected_PreFilled = new WorkingStorage();

		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aaaa", 17);
		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aaat", 18);
		((WorkingStorage)expected_PreFilled).storeRepeatAtSingleIndex("aatt", 19);

		assertEquals(expected_PreFilled, actual_PreFilled);

		////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_Empty = new WorkingStorage();
		((WorkingStorage)actual_Empty).addMapOfEntries(entriesToAdd);

		Storage<HashSet<Integer>> expected_Empty = new WorkingStorage();

		((WorkingStorage)expected_Empty).storeRepeatAtSingleIndex("aaaa", 17);
		((WorkingStorage)expected_Empty).storeRepeatAtSingleIndex("aaat", 18);
		((WorkingStorage)expected_Empty).storeRepeatAtSingleIndex("aatt", 19);

		assertEquals(expected_Empty, actual_Empty);

		///////////////////////////////////////////////////////////////////

		Map<String, Set<Integer>> entriesToAdd_Empty = new HashMap<String, Set<Integer>>();
		Storage<HashSet<Integer>> actual_PreFilled_NothingAdded = new WorkingStorage();

		// aaaatt --> 1,10
		((WorkingStorage)actual_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aatt", 12);
		((WorkingStorage)actual_PreFilled_NothingAdded).addMapOfEntries(entriesToAdd_Empty);

		Storage<HashSet<Integer>> expected_PreFilled_NothingAdded = new WorkingStorage();

		// aaaatt --> 1,10
		((WorkingStorage)expected_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_PreFilled_NothingAdded).storeRepeatAtSingleIndex("aatt", 12);
		assertEquals(expected_PreFilled_NothingAdded, actual_PreFilled_NothingAdded);

		///////////////////////////////////////////////////////////////////

	}

	@Test
	public void testRemoveEntriesOccuringAtSingleIndex() {

		Storage<HashSet<Integer>> actual_Normal_OneToDeleteAtOwnIndex = new WorkingStorage();
		((WorkingStorage)actual_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("cccc", 4);

		((WorkingStorage)actual_Normal_OneToDeleteAtOwnIndex).removeEntriesOccuringAtSingleIndex();

		Storage<HashSet<Integer>> expected_Normal_OneToDeleteAtOwnIndex = new WorkingStorage();
		((WorkingStorage)expected_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_Normal_OneToDeleteAtOwnIndex).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_Normal_OneToDeleteAtOwnIndex, actual_Normal_OneToDeleteAtOwnIndex);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_Empty = new WorkingStorage();
		((WorkingStorage)actual_Empty).removeEntriesOccuringAtSingleIndex();

		Storage<HashSet<Integer>> expected_Empty = new WorkingStorage();

		assertEquals(expected_Empty, actual_Empty);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_NothingToRemove = new WorkingStorage();
		((WorkingStorage)actual_NothingToRemove).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_NothingToRemove).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_NothingToRemove).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_NothingToRemove).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_NothingToRemove).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_NothingToRemove).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_NothingToRemove).removeEntriesOccuringAtSingleIndex();

		Storage<HashSet<Integer>> expected_NothingToRemove = new WorkingStorage();
		((WorkingStorage)expected_NothingToRemove).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_NothingToRemove).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_NothingToRemove).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_NothingToRemove).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_NothingToRemove).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_NothingToRemove).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_NothingToRemove, actual_NothingToRemove);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_DeleteMultiple = new WorkingStorage();
		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("cccc", 4);
		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("xxxx", 5);
		((WorkingStorage)actual_DeleteMultiple).storeRepeatAtSingleIndex("wwww", 6);

		((WorkingStorage)actual_DeleteMultiple).removeEntriesOccuringAtSingleIndex();

		Storage<HashSet<Integer>> expected_DeleteMultiple = new WorkingStorage();
		((WorkingStorage)expected_DeleteMultiple).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_DeleteMultiple).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_DeleteMultiple).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_DeleteMultiple).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_DeleteMultiple).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_DeleteMultiple).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_DeleteMultiple, actual_DeleteMultiple);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_DeleteAtSameIndex = new WorkingStorage();
		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("cccc", 1);
		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("xxxx", 1);
		((WorkingStorage)actual_DeleteAtSameIndex).storeRepeatAtSingleIndex("wwww", 3);

		((WorkingStorage)actual_DeleteAtSameIndex).removeEntriesOccuringAtSingleIndex();

		Storage<HashSet<Integer>> expected_DeleteAtSameIndex = new WorkingStorage();
		((WorkingStorage)expected_DeleteAtSameIndex).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_DeleteAtSameIndex).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_DeleteAtSameIndex).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_DeleteAtSameIndex).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_DeleteAtSameIndex).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_DeleteAtSameIndex).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_DeleteAtSameIndex, actual_DeleteAtSameIndex);

	}

	@Test
	public void testRemoveEntriesSubstring() {

		Storage<HashSet<Integer>> actual_Normal = new WorkingStorage();
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_Normal).removeEntry("aaaa");

		Storage<HashSet<Integer>> expected_Normal = new WorkingStorage();
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_Normal, actual_Normal);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_Multiple = new WorkingStorage();
		((WorkingStorage)actual_Multiple).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_Multiple).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_Multiple).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_Multiple).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_Multiple).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_Multiple).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_Multiple).removeEntry("aaaa");
		((WorkingStorage)actual_Multiple).removeEntry("aaat");

		Storage<HashSet<Integer>> expected_Multiple = new WorkingStorage();
		((WorkingStorage)expected_Multiple).storeRepeatAtSingleIndex("aatt", 3);
		((WorkingStorage)expected_Multiple).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_Multiple, actual_Multiple);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_SubstringNotContained = new WorkingStorage();
		((WorkingStorage)actual_SubstringNotContained).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_SubstringNotContained).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_SubstringNotContained).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_SubstringNotContained).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_SubstringNotContained).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_SubstringNotContained).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_SubstringNotContained).removeEntry("dddd");

		Storage<HashSet<Integer>> expected_SubstringNotContained = new WorkingStorage();
		((WorkingStorage)expected_SubstringNotContained).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_SubstringNotContained).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_SubstringNotContained).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_SubstringNotContained).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_SubstringNotContained).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_SubstringNotContained).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_SubstringNotContained, actual_SubstringNotContained);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_EmptySubstring = new WorkingStorage();
		((WorkingStorage)actual_EmptySubstring).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_EmptySubstring).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_EmptySubstring).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_EmptySubstring).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_EmptySubstring).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_EmptySubstring).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_EmptySubstring).removeEntry("");

		Storage<HashSet<Integer>> expected_EmptySubstring = new WorkingStorage();
		((WorkingStorage)expected_EmptySubstring).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_EmptySubstring).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_EmptySubstring).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_EmptySubstring).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_EmptySubstring).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_EmptySubstring).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_EmptySubstring, actual_EmptySubstring);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_NullSubstring = new WorkingStorage();
		((WorkingStorage)actual_NullSubstring).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_NullSubstring).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_NullSubstring).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_NullSubstring).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_NullSubstring).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_NullSubstring).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_NullSubstring).removeEntry(null);

		Storage<HashSet<Integer>> expected_NullSubstring = new WorkingStorage();
		((WorkingStorage)expected_NullSubstring).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_NullSubstring).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_NullSubstring).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_NullSubstring).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_NullSubstring).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_NullSubstring).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_NullSubstring, actual_NullSubstring);

	}

	@Test
	public void testRemoveEntriesSubstringAtIndices() {

		Storage<HashSet<Integer>> actual_Normal = new WorkingStorage();
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aatt", 12);

		Set<Integer> startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		((WorkingStorage)actual_Normal).removeStartIndicesOfSingleRepeat("aaaa", startIndicesRemove);

		Storage<HashSet<Integer>> expected_Normal = new WorkingStorage();
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_Normal).removeEmptyEntries();
		assertEquals(expected_Normal, actual_Normal);

		//////////////////////////////////////////////////////////////////////////////////////////
		Storage<HashSet<Integer>> actual_MultipleIndices = new WorkingStorage();
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aatt", 12);

		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		startIndicesRemove.add(10);
		((WorkingStorage)actual_MultipleIndices).removeStartIndicesOfSingleRepeat("aaaa", startIndicesRemove);

		Storage<HashSet<Integer>> expected_MultipleIndices = new WorkingStorage();
		((WorkingStorage)expected_MultipleIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_MultipleIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_MultipleIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_MultipleIndices).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_MultipleIndices).removeEmptyEntries();
		assertEquals(expected_MultipleIndices, actual_MultipleIndices);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_MultipleSubstrings = new WorkingStorage();
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aatt", 12);

		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		((WorkingStorage)actual_MultipleSubstrings).removeStartIndicesOfSingleRepeat("aaaa", startIndicesRemove);

		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(11);
		((WorkingStorage)actual_MultipleSubstrings).removeStartIndicesOfSingleRepeat("aaat", startIndicesRemove);

		Storage<HashSet<Integer>> expected_MultipleSubstrings = new WorkingStorage();
		((WorkingStorage)expected_MultipleSubstrings).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_MultipleSubstrings).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_MultipleSubstrings).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_MultipleSubstrings).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_MultipleSubstrings).removeEmptyEntries();
		assertEquals(expected_MultipleSubstrings, actual_MultipleSubstrings);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_NullIndices = new WorkingStorage();
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_NullIndices).removeStartIndicesOfSingleRepeat("aaat", null);

		Storage<HashSet<Integer>> expected_NullIndices = new WorkingStorage();
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_NullIndices).removeEmptyEntries();
		assertEquals(expected_NullIndices, actual_NullIndices);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_Null = new WorkingStorage();
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_Null).removeStartIndicesOfSingleRepeat(null, null);

		Storage<HashSet<Integer>> expected_Null = new WorkingStorage();
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_Null).removeEmptyEntries();
		assertEquals(expected_Null, actual_Null);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_NullString = new WorkingStorage();
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aatt", 12);

		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		((WorkingStorage)actual_NullString).removeStartIndicesOfSingleRepeat(null, startIndicesRemove);

		Storage<HashSet<Integer>> expected_NullString = new WorkingStorage();
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_NullString).removeEmptyEntries();
		assertEquals(expected_NullString, actual_NullString);

	}

	@Test
	public void testRemoveEmptyEntries() {
	}

	@Test
	public void testRemoveEntries() {

		Storage<HashSet<Integer>> actual_Normal = new WorkingStorage();
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_Normal).storeRepeatAtSingleIndex("aatt", 12);

		Map<String, Set<Integer>> removables = new HashMap<String, Set<Integer>>();
		Set<Integer> startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		removables.put("aaaa", startIndicesRemove);
		((WorkingStorage)actual_Normal).removeStartIndicesOfMultipleRepeats(removables);

		Storage<HashSet<Integer>> expected_Normal = new WorkingStorage();
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_Normal).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_Normal, actual_Normal);

		//////////////////////////////////////////////////////////////////////////////////////////
		Storage<HashSet<Integer>> actual_MultipleIndices = new WorkingStorage();
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_MultipleIndices).storeRepeatAtSingleIndex("aatt", 12);

		removables = new HashMap<String, Set<Integer>>();
		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		startIndicesRemove.add(10);
		removables.put("aaaa", startIndicesRemove);
		((WorkingStorage)actual_MultipleIndices).removeStartIndicesOfMultipleRepeats(removables);

		Storage<HashSet<Integer>> expected_MultipleIndices = new WorkingStorage();
		((WorkingStorage)expected_MultipleIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_MultipleIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_MultipleIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_MultipleIndices).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_MultipleIndices, actual_MultipleIndices);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_MultipleSubstrings = new WorkingStorage();
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_MultipleSubstrings).storeRepeatAtSingleIndex("aatt", 12);

		removables = new HashMap<String, Set<Integer>>();
		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		removables.put("aaaa", startIndicesRemove);

		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(11);
		removables.put("aaat", startIndicesRemove);
		((WorkingStorage)actual_MultipleSubstrings).removeStartIndicesOfMultipleRepeats(removables);

		Storage<HashSet<Integer>> expected_MultipleSubstrings = new WorkingStorage();
		((WorkingStorage)expected_MultipleSubstrings).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_MultipleSubstrings).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_MultipleSubstrings).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_MultipleSubstrings).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_MultipleSubstrings, actual_MultipleSubstrings);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_NullIndices = new WorkingStorage();
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_NullIndices).storeRepeatAtSingleIndex("aatt", 12);

		removables = new HashMap<String, Set<Integer>>();
		removables.put("aaaa", null);
		((WorkingStorage)actual_NullIndices).removeStartIndicesOfMultipleRepeats(removables);

		Storage<HashSet<Integer>> expected_NullIndices = new WorkingStorage();
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_NullIndices).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_NullIndices, actual_NullIndices);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_Null = new WorkingStorage();
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_Null).storeRepeatAtSingleIndex("aatt", 12);

		((WorkingStorage)actual_Null).removeStartIndicesOfMultipleRepeats(null);

		Storage<HashSet<Integer>> expected_Null = new WorkingStorage();
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_Null).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_Null, actual_Null);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_NullString = new WorkingStorage();
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_NullString).storeRepeatAtSingleIndex("aatt", 12);

		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		removables = new HashMap<String, Set<Integer>>();
		removables.put(null, startIndicesRemove);
		((WorkingStorage)actual_NullString).removeStartIndicesOfMultipleRepeats(removables);

		Storage<HashSet<Integer>> expected_NullString = new WorkingStorage();
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_NullString).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_NullString, actual_NullString);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_EmptyString = new WorkingStorage();
		((WorkingStorage)actual_EmptyString).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_EmptyString).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_EmptyString).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_EmptyString).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_EmptyString).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_EmptyString).storeRepeatAtSingleIndex("aatt", 12);

		startIndicesRemove = new HashSet<Integer>();
		startIndicesRemove.add(1);
		removables = new HashMap<String, Set<Integer>>();
		removables.put("", startIndicesRemove);
		((WorkingStorage)actual_EmptyString).removeStartIndicesOfMultipleRepeats(removables);

		Storage<HashSet<Integer>> expected_EmptyString = new WorkingStorage();
		((WorkingStorage)expected_EmptyString).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_EmptyString).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_EmptyString).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_EmptyString).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_EmptyString).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_EmptyString).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_EmptyString, actual_EmptyString);

		//////////////////////////////////////////////////////////////////////////////////////////

		Storage<HashSet<Integer>> actual_EmptyIndices = new WorkingStorage();
		((WorkingStorage)actual_EmptyIndices).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual_EmptyIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)actual_EmptyIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)actual_EmptyIndices).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)actual_EmptyIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)actual_EmptyIndices).storeRepeatAtSingleIndex("aatt", 12);

		startIndicesRemove = new HashSet<Integer>();
		removables = new HashMap<String, Set<Integer>>();
		removables.put("", startIndicesRemove);
		((WorkingStorage)actual_EmptyIndices).removeStartIndicesOfMultipleRepeats(removables);

		Storage<HashSet<Integer>> expected_EmptyIndices = new WorkingStorage();
		((WorkingStorage)expected_EmptyIndices).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)expected_EmptyIndices).storeRepeatAtSingleIndex("aaat", 2);
		((WorkingStorage)expected_EmptyIndices).storeRepeatAtSingleIndex("aatt", 3);

		((WorkingStorage)expected_EmptyIndices).storeRepeatAtSingleIndex("aaaa", 10);
		((WorkingStorage)expected_EmptyIndices).storeRepeatAtSingleIndex("aaat", 11);
		((WorkingStorage)expected_EmptyIndices).storeRepeatAtSingleIndex("aatt", 12);

		assertEquals(expected_EmptyIndices, actual_EmptyIndices);

	}
	
	@Test 
	public void testMerge(){
		
		//1. Merge a "normal" store
		Storage<HashSet<Integer>> actual = new WorkingStorage();
		
		((WorkingStorage)actual).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("aaaa", 2);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("cccc", 1);
		((WorkingStorage)actual).storeRepeatAtSingleIndex("cccc", 3);
		
		Storage<HashSet<Integer>> merge = new WorkingStorage();
		((WorkingStorage)merge).storeRepeatAtSingleIndex("aaaa", 3);
		((WorkingStorage)merge).storeRepeatAtSingleIndex("dddd", 4);
		((WorkingStorage)merge).storeRepeatAtSingleIndex("cccc", 1);
	
		((WorkingStorage)actual).merge(merge);
		
		Map<String, Set<Integer>> expectedString = new HashMap<String, Set<Integer>>();
		Set<Integer> startIndices = new HashSet<Integer>();
		startIndices.add(1);
		startIndices.add(2);
		startIndices.add(3);
		expectedString.put("aaaa", startIndices);
		startIndices = new HashSet<Integer>();
		startIndices.add(1);
		startIndices.add(3);
		expectedString.put("cccc", startIndices);
		startIndices = new HashSet<Integer>();
		startIndices.add(4);
		expectedString.put("dddd", startIndices);

		Map<Integer, Set<String>> expectedInteger = new HashMap<Integer, Set<String>>();
		Set<String> strings = new HashSet<String>();
		strings.add("aaaa");
		strings.add("cccc");
		expectedInteger.put(1, strings);
		strings = new HashSet<String>();
		strings.add("aaaa");
		expectedInteger.put(2, strings);
		strings = new HashSet<String>();
		strings.add("cccc");
		strings.add("aaaa");
		expectedInteger.put(3, strings);
		strings = new HashSet<String>();
		strings.add("dddd");
		expectedInteger.put(4, strings);

		assertEquals(expectedString, actual.getMapRepeats());
		assertEquals(expectedInteger, actual.getMapIndex());

		
	}

	@Test
	public void testWorkingStorageNew() {
	}

	@Test
	public void testHasIndex() {

	}

	@Test
	public void testGetNumberStrings() {

	}

	@Test
	public void testGetMapString() {
	}

	@Test
	public void testGetMapIndex() {
	}

	@Test
	public void testEquals() {

		Storage<HashSet<Integer>> store1 = new WorkingStorage();
		Storage<HashSet<Integer>> store2 = new WorkingStorage();

		assertTrue(store1.equals(store2));
		assertTrue(store2.equals(store1));

		((WorkingStorage)store1).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)store1).storeRepeatAtSingleIndex("aaaa", 3);
		((WorkingStorage)store1).storeRepeatAtSingleIndex("cccc", 2);
		((WorkingStorage)store1).storeRepeatAtSingleIndex("cccc", 4);

		((WorkingStorage)store2).storeRepeatAtSingleIndex("aaaa", 1);
		((WorkingStorage)store2).storeRepeatAtSingleIndex("aaaa", 3);
		((WorkingStorage)store2).storeRepeatAtSingleIndex("cccc", 2);
		((WorkingStorage)store2).storeRepeatAtSingleIndex("cccc", 4);

		assertTrue(store1.equals(store2));
		assertTrue(store2.equals(store1));

		((WorkingStorage)store1).storeRepeatAtSingleIndex("dddd", 1);
		((WorkingStorage)store2).storeRepeatAtSingleIndex("dddd", 3);

		assertFalse(store1.equals(store2));
		assertFalse(store2.equals(store1));
	}

}
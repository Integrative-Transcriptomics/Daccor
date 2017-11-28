package elongation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import storage.Storage;
import storage.WorkingStorage;
import utilities.Pair;

/**
 * 
 * @author Friederike Hanssen Class merges all given kmers to maximal exact
 *         repetitive regions
 */

public class KmerMerger extends Extender {

	private Storage<HashSet<Integer>> orphansStore;
	private Storage<HashSet<Integer>> smallStuff;
	private final boolean IS_SMALL;
	private Set<String> removableList;
	
	private String[] kmers;
	


	public KmerMerger(boolean isSmall) {
		super();
		this.IS_SMALL = isSmall;
		this.THREADS = 1;
	}

	public KmerMerger(boolean isSmall, Integer threads) {
		super();
		this.IS_SMALL = isSmall;
		this.THREADS = threads;
	}

	@Override
	public void setParameters(Storage<HashSet<Integer>> repeats, Map<String, Set<Integer>> addables) {

		this.orphansStore = new WorkingStorage();
		this.smallStuff = new WorkingStorage();

		this.workingStorage = repeats;
		results = new HashMap<String, Set<Integer>>();
		usedEntries = new HashMap<String, Set<Integer>>();
		this.kmers = workingStorage.getMapRepeats().keySet()
				.toArray((new String[workingStorage.getMapRepeats().keySet().size()]));
		this.removableList = new HashSet<String>();
	}

	@Override
	public void elongate() {
		mergeKmers();
		
		setUsedEntries();
		validateUsedEntries();
		
	}

	public void mergeKmers() {
		
		ExecutorService es = Executors.newFixedThreadPool(this.THREADS);

		for (int i = 0; i < kmers.length; i++) {

			es.submit(new HandleOneKmer(this, i));
		}
		
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public Pair<String, Integer> mergeKmers(Integer kmerIndex, Integer currentStartIndexOfKmer) {
		
		String kmer = this.kmers[kmerIndex];

		Set<String> kmersForExtension = workingStorage.getMapIndex().get(currentStartIndexOfKmer + 1);

		for (String currPotExt : kmersForExtension) {

			// Only two kmers of same length can be extended, as their start
			// indices are just moved by one
			if (kmer.length() == currPotExt.length()) {

				// E.g. aaaax & aaaxx, dann aaax=aaax and merged: aaaaxx
				if (kmer.substring(1).equals(currPotExt.substring(0, currPotExt.length() - 1))) {

					String merged = kmer.concat(currPotExt.substring(currPotExt.length() - 1));

					return new Pair<String, Integer>(merged,currentStartIndexOfKmer);
//					results.put(merged, startIndexOfMerged);
				}
			}
		}
		return new Pair<String, Integer>("", -1);
	}

	private void setUsedEntries() {

		String[] repeats = results.keySet().toArray(new String[results.keySet().size()]);
		ExecutorService es = Executors.newFixedThreadPool(this.THREADS);

		for (int i = 0; i < repeats.length; i++) {

			es.submit(new SetOneUsedKmer(this, repeats[i], this.IS_SMALL));
		}
		
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(String r : removableList){
			results.remove(r);
		}

	}

	/**
	 * Check if any orphans
	 */
	private void validateUsedEntries() {

		// not all entries in working Storage are used, meaning there are some
		// unextendable ones
		if (!usedEntries.equals(workingStorage)) {
			String[] usedRepeats = usedEntries.keySet().toArray(new String[usedEntries.keySet().size()]);
			ExecutorService es = Executors.newFixedThreadPool(this.THREADS);
			for (int i = 0; i < usedRepeats.length; i++) {

				es.submit(new ValidateUsedEntriesParallel(this,usedRepeats[i]));
			}
			
			es.shutdown();
			try {
				es.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (orphansStore.getMapRepeats().equals(results) || results.isEmpty()) {
			orphansStore = new WorkingStorage();
		}

	}

	public Storage<HashSet<Integer>> getOrphans() {
		return orphansStore;
	}
	
	public Storage<HashSet<Integer>> getSmall() {
		return smallStuff;
	}

	public Set<Integer> getAllStartIndices(Integer kmerIndex) {
		return workingStorage.getMapRepeats().get(kmers[kmerIndex]);
	}
	
	public Set<Integer> getAllStartIndices(String kmer) {
		return workingStorage.getMapRepeats().get(kmer);
	}
	
	
	public Set<Integer> getAllStartIndicesFromResults(String kmer) {
		return results.get(kmer);
	}
	
	
	public boolean hasKmer(Integer kmerIndex){
		return workingStorage.getMapIndex().containsKey(kmerIndex);
	}
	
	public synchronized void addMergedKmers(Map<String, Set<Integer>> newResults){
		for(String repeat: newResults.keySet()){
			Set<Integer> combinedSet = new HashSet<Integer>();
			if(this.results.containsKey(repeat)){
				combinedSet = results.get(repeat);
			}
			combinedSet.addAll(newResults.get(repeat));
			this.results.put(repeat, combinedSet);
		}
//		System.out.println("bla1:"+(this.results).toString());
//		results.put(mergedKMer, startIndices);TODO
	}
	
	public synchronized void updateUsedEntries(String substring, String substring2, Integer j, int i){
		this.setUsedEntries(substring, substring2, j, i);
	}
	
	public Set<Integer> getUsedStartIndices(String repeat){
		return this.usedEntries.get(repeat);
	}
	
	public synchronized void addToRemovable(String removeKmer){
		removableList.add(removeKmer);
		
	}
	
	public Integer getNumberOfStartIndices(String repeat){
		return results.get(repeat).size();
	}
	
	public Integer getNumberOfStartIndicesUsed(String repeat){
		return usedEntries.get(repeat).size();
	}
	
	public synchronized void updateSmallEntries(String substring, Set<Integer> startIndices){
		((WorkingStorage) smallStuff).storeRepeatAtSetOfIndices(substring, startIndices);
	}
	
	public synchronized void updateOrphans(String substring, Set<Integer> startIndices){
		((WorkingStorage) orphansStore).storeRepeatAtSetOfIndices(substring, startIndices);
	}
	
	
}

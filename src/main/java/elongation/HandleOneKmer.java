package elongation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import utilities.Pair;

public class HandleOneKmer implements Callable<HandleOneKmer>{
//	public Storage<HashSet<Integer>> orphansStore;
//	public Storage<HashSet<Integer>> smallStuff;
//	public boolean IS_SMALL;
	
	private Integer kmerIndex = 0;
	
	private KmerMerger kmerMerger;
	
	protected Map<String, Set<Integer>> results;

	public HandleOneKmer(KmerMerger kmerMerger, Integer kmerIndex) {
		this.kmerMerger = kmerMerger;
		this.kmerIndex = kmerIndex;
		this.results = new HashMap<String, Set<Integer>>();
	}

	@Override
	public HandleOneKmer call() throws Exception {
		// For current kmer, find for all start indices, the following start
		// index and check if anything is there
		Set<Integer> startIndicesOfCurrentKmer = kmerMerger.getAllStartIndices(this.kmerIndex);
		for (Integer currentStartIndexOfKmer : startIndicesOfCurrentKmer) {
			if (kmerMerger.hasKmer(currentStartIndexOfKmer + 1)) {
				
				Pair<String, Integer> mergedKmers = kmerMerger.mergeKmers(kmerIndex, currentStartIndexOfKmer);
				if(!"".equals(mergedKmers.getFirst()) && mergedKmers.getSecond()>=0){
//					kmerMerger.addMergedKmers(mergedKmers.getFirst(), mergedKmers.getSecond());
					// Set results
					Set<Integer> startIndexOfMerged = new HashSet<Integer>();
					if (results.containsKey(mergedKmers.getFirst())) {
						startIndexOfMerged = results.get(mergedKmers.getFirst());
					}
					startIndexOfMerged.add(mergedKmers.getSecond());
					results.put(mergedKmers.getFirst(), startIndexOfMerged);
				}

			}
		}
		kmerMerger.addMergedKmers(this.results);
		return this;
	}

}
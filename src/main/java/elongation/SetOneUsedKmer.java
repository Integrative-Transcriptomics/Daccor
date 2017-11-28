package elongation;

import java.util.concurrent.Callable;

public class SetOneUsedKmer implements Callable<SetOneUsedKmer> {
	
	private KmerMerger kmerMerger;
	private String repeat;
	
	private Boolean issmall = false;
 
	public SetOneUsedKmer(KmerMerger kmerMerger,String repeat, Boolean issmall) {
		this.kmerMerger = kmerMerger;
		this.repeat = repeat;
		this.issmall = issmall;
	}
	
	@Override
	public SetOneUsedKmer call() throws Exception {
		if (kmerMerger.getNumberOfStartIndices(repeat) > 1) {
			for (Integer j : kmerMerger.getAllStartIndicesFromResults(repeat)) {
				this.kmerMerger.updateUsedEntries(repeat.substring(0, repeat.length() - 1), repeat.substring(1), j, j + 1);
			}
			if(this.issmall){
				String first = repeat.substring(0, repeat.length() - 1);
				String second = repeat.substring(1);
				if(this.kmerMerger.getNumberOfStartIndices(repeat) != kmerMerger.getNumberOfStartIndicesUsed(first)){
					kmerMerger.updateSmallEntries(first, kmerMerger.getAllStartIndices(first));
			
				}
				if(this.kmerMerger.getNumberOfStartIndices(repeat) != kmerMerger.getNumberOfStartIndicesUsed(second)){
					kmerMerger.updateSmallEntries(second, kmerMerger.getAllStartIndices(second));
				}
			}
		} else {
			kmerMerger.addToRemovable(repeat);
		}
		return this;
		
	}

}

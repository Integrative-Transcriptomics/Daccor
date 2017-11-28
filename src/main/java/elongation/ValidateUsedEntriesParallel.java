package elongation;

import java.util.concurrent.Callable;

public class ValidateUsedEntriesParallel implements Callable<ValidateUsedEntriesParallel>{
	
	private String usedRepeat = "";
	private KmerMerger kmerMerger;
	
	public ValidateUsedEntriesParallel(KmerMerger kmerMerger, String usedRepeat) {
		this.kmerMerger = kmerMerger;
		this.usedRepeat = usedRepeat;
		// TODO Auto-generated constructor stub
	}

	@Override
	public ValidateUsedEntriesParallel call() throws Exception {
		// TODO Auto-generated method stub
		// so if not all indices for a key are used, then store all
		// indices as orphans for later use
		if (!this.kmerMerger.getUsedStartIndices(usedRepeat).equals(this.kmerMerger.getAllStartIndices(usedRepeat))) {
			this.kmerMerger.updateOrphans(usedRepeat, this.kmerMerger.getAllStartIndices(usedRepeat));
		}
		return this;
	}

}

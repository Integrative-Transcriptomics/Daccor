/**
 * 
 */
package elongation;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Alexander Seitz
 *
 */
public class AddMarkerToOneKmer implements Callable<AddMarkerToOneKmer> {
	
	private MismatchExtender mismatchExtender;
	private String repeat;
	
	public AddMarkerToOneKmer(MismatchExtender mismatchExtender, String repeat){
		this.mismatchExtender = mismatchExtender;
		this.repeat = repeat;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public AddMarkerToOneKmer call() throws Exception {
		
		String mismatch = this.mismatchExtender.addMismatchesToKey(this.repeat);
		
		if(!mismatchExtender.getStorage().getMapRepeats().containsKey(mismatch)){
			Set<Integer> startIndicesOfCurrentRepeat = this.mismatchExtender.getStorage().getMapRepeats().get(this.repeat);
			Set<Integer> startIndicesOfCurrentMismatch = new HashSet<Integer>();

			// Find all start indices of current mismatch
			for (Integer currentStartIndex : startIndicesOfCurrentRepeat) {
				if (mismatch.length() + currentStartIndex <= this.mismatchExtender.getTOTAL_SEQ_LENGTH()) {
					startIndicesOfCurrentMismatch.add(currentStartIndex);
				}
			}
			this.mismatchExtender.setResults(mismatch, startIndicesOfCurrentMismatch);
		}
		// TODO Auto-generated method stub
		return this;
	}

}

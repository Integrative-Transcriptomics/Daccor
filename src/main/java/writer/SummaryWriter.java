package writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import outputUtils.SequenceStats;
import outputUtils.SimpleStats;

/**
 * 
 * @author Friederike Hanssen
 *
 */

public class SummaryWriter {
	private final Map<String, SimpleStats> SIMPLESTATS_COLLECTION;
	private final Map<String, SequenceStats> SEQUENCESTATS_COLLECTION;
	private final String TAB = "\t";
	private final String HEADER_SIMPLE = "category" + TAB + "value";
	private final String HEADER_SEQ = "fastaName" + TAB + "repeatName" + TAB + "startindex" + TAB + "endIndex" + TAB
			+ "length" + TAB + "number mismatches" + TAB + "indices of mismatches";

	public SummaryWriter(Map<String, SimpleStats> simpleStats, Map<String, SequenceStats> sequenceStats) {
		this.SIMPLESTATS_COLLECTION = simpleStats;
		this.SEQUENCESTATS_COLLECTION = sequenceStats;
	}

	public void writeToFile(String filename) throws IOException {

		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filename.concat("_Summary.csv")), "utf-8"));
		String[] identities = SIMPLESTATS_COLLECTION.keySet()
				.toArray(new String[SIMPLESTATS_COLLECTION.keySet().size()]);
		for (int i = 0; i < identities.length; i++) {
			SimpleStats currSummary = SIMPLESTATS_COLLECTION.get(identities[i]);
			String currName = identities[i];

			bw.write(currName.concat("\n"));
			bw.write(HEADER_SIMPLE.concat("\n"));
			bw.write("Total number of repetitive regions" + TAB + currSummary.getOverallNumberOfSequences() + "\n");
			bw.write("Total number of different repetitive regions" + TAB + currSummary.getNumberOfRepeats() + "\n");

			bw.write("Minimum length" + TAB + currSummary.getMinimumLength() + "\n");
			bw.write("Maximum length" + TAB + currSummary.getMaximumLength() + "\n");

			bw.write("Average length" + TAB + currSummary.getAverageLength() + "\n");

			bw.write("Number of repetitive regions containing mutations" + TAB
					+ currSummary.getNumberOfSequencesWithMutations() + "\n");
			
			bw.write("Number of repetitive regions also contained in other sequences" + TAB + currSummary.getNumberMatchingWithOtherSequences() + "\n");
		}

		bw.write(HEADER_SEQ.concat("\n"));
		identities = SEQUENCESTATS_COLLECTION.keySet().toArray(new String[SEQUENCESTATS_COLLECTION.keySet().size()]);

		for (int i = 0; i < identities.length; i++) {
			SequenceStats currSummary = SEQUENCESTATS_COLLECTION.get(identities[i]);
			Integer[] keys = currSummary.getSequenceStats().keySet()
					.toArray(new Integer[currSummary.getSequenceStats().keySet().size()]);
			for (int j = 0; j < keys.length; j++) {
				for (int k = 0; k < currSummary.getSequenceStats().get(keys[j]).size(); k++) {
					bw.write(currSummary.getSequenceStats().get(keys[j]).get(k) + TAB);
				}
				bw.write("\n");
			}

		}

		bw.close();

	}
}

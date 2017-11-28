package outputUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Friederike Hanssen Class modifies the gff entries, based on the bed
 *         entries. From the original gff file, the correct entries are chosen.
 *         This is done for each original sequence fasta entry.
 */
public class GffModifier {

	private final Map<String, BedFile> BED_ENTRIES;
	private List<ArrayList<String>> gffEntries;
	private List<ArrayList<String>> modifiedEntries;

	public GffModifier(Map<String, BedFile> bedEntries, List<ArrayList<String>> gffEntries) {
		this.BED_ENTRIES = bedEntries;
		this.gffEntries = gffEntries;
		modifiedEntries = new ArrayList<ArrayList<String>>();
	}

	public void modify() {

		if (BED_ENTRIES != null && !BED_ENTRIES.isEmpty() && gffEntries != null && !gffEntries.isEmpty()) {

			//Go over each gff entry 
			for (ArrayList<String> gffEntry : gffEntries) {

				String[] bedFilesNames = BED_ENTRIES.keySet().toArray(new String[BED_ENTRIES.keySet().size()]);

				// iterate over different bed files
				for (int j = 0; j < bedFilesNames.length; j++) {
					String bedName = bedFilesNames[j];

					// remove fasta marker
					if (bedName.startsWith(">")) {
						bedName = bedFilesNames[j].substring(1);
					}

					// if id entries are the same: same name
					if (bedName.equals(gffEntry.get(0))) {

						findEntries(gffEntry, bedFilesNames[j]);
					}
				}

			}
		}
	}

	private void findEntries(ArrayList<String> gffEntry, String bedFileName) {
		
		Integer[] mapEntries = BED_ENTRIES.get(bedFileName).getValues().keySet()
				.toArray(new Integer[BED_ENTRIES.get(bedFileName).getValues().keySet().size()]);
		for (int i = 0; i < mapEntries.length; i++) {
			String[] list = BED_ENTRIES.get(bedFileName).getValues().get(mapEntries[i]);

			int startIndexMap = Integer.valueOf(list[1]);
			int startIndexGFF = Integer.valueOf(gffEntry.get(3));
			int endIndexGFF = Integer.valueOf(gffEntry.get(4));
			
			if (startIndexMap >= startIndexGFF && startIndexMap <= endIndexGFF
					|| startIndexGFF >= startIndexMap && endIndexGFF <= startIndexMap) {
				modifiedEntries.add(gffEntry);
			}

			int endIndexMap = Integer.valueOf(list[2]);

			if (endIndexMap >= startIndexGFF && endIndexMap <= endIndexGFF
					|| endIndexGFF >= startIndexMap && endIndexGFF <= endIndexMap) {
				modifiedEntries.add(gffEntry);
			}
		}
	}

	public List<ArrayList<String>> getModifiedEntries() {
		return modifiedEntries;
	}

}

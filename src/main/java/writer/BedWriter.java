package writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import outputUtils.BedFile;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class BedWriter {

	private final Map<String, BedFile> BED_FILE_COLLECTION;
	private final String TAB = "\t";

	public BedWriter(Map<String, BedFile> file) {
		this.BED_FILE_COLLECTION = file;
	}

	public void writeToFile(String filename) throws IOException {

		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filename.concat(".bed")), "utf-8"));

		String[] identities = BED_FILE_COLLECTION.keySet().toArray(new String[BED_FILE_COLLECTION.keySet().size()]);
		for (int i = 0; i < identities.length; i++) {
			Integer[] values = BED_FILE_COLLECTION.get(identities[i]).getValues().keySet().toArray(new Integer[BED_FILE_COLLECTION.get(identities[i]).getValues().keySet().size()]);
			for (int j = 0; j < values.length; j++) {
				String[] mapThings = BED_FILE_COLLECTION.get(identities[i]).getValues().get(values[j]);
				bw.write(mapThings[0].split("\\s+")[0] + TAB);
				for (int k = 1; k < mapThings.length; k++) {
					bw.write(mapThings[k] + TAB);
				}
				bw.write("\n");

			}
		}
		bw.close();
	}
}

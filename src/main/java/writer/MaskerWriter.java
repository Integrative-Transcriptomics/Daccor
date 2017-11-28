package writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MaskerWriter {

	private final String SEQ_NAME;
	private final String MASKED_SEQ;

	public MaskerWriter(String seqName, String maskedSequence) {
		this.SEQ_NAME = seqName;
		this.MASKED_SEQ = maskedSequence;
	}

	public void write(String outputFilename) throws IOException {

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilename), "utf-8"));
		bw.write(SEQ_NAME.concat("\n"));

		// Standard line length in .fasta = 80 (regex can't take variables)
		String[] lines = MASKED_SEQ.split("(?<=\\G.{80})");
		for (String s : lines) {
			bw.write(s);
			bw.write("\n");
		}
		bw.write("\n");

		bw.close();
	}
}

package writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import inputHandler.Runner;
import outputUtils.MultiFastaOutput;

/**
 * 
 * @author Friederike Hanssen
 *
 */

public class MultiFastaWriter {
	
	private final Map<String, MultiFastaOutput> OUTPUT;
//	private final Integer margin;
	private Runner runner;
	
	
	public MultiFastaWriter(Map<String, MultiFastaOutput> output, Integer margin, Runner runner) {
		this.OUTPUT = output;
//		this.margin = margin;
		this.runner = runner;
	}
	
	public Map<String, List<File>> writeToSeparateFiles(String filename) throws IOException {
		// get the current date as string
		Map<String, List<File>> result = new HashMap<String, List<File>>();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String currentDate = sdf.format(cal.getTime());
		// create the output folder
		String outFolder = new File(new File(filename).getAbsolutePath()).getParent();
		File tmpOutFolder = new File(outFolder+"/"+currentDate+"rep_sep_output");
		this.runner.setFolderWithSeparateRepeats(tmpOutFolder.getAbsolutePath());
		tmpOutFolder.mkdirs();
		// write the sequences to the files
		String[] seqIdentities = OUTPUT.keySet().toArray(new String[OUTPUT.keySet().size()]);
		
		for (int j = 0; j < seqIdentities.length; j++) {
			String[] headers = OUTPUT.get(seqIdentities[j]).getOutputWithMargins().keySet()
					.toArray(new String[OUTPUT.get(seqIdentities[j]).getOutputWithMargins().keySet().size()]);
			List<File> currResult = new LinkedList<File>();

			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				String currFilename = tmpOutFolder.getAbsolutePath()+"/"+i+"_"+j+"_"+header.trim().substring(1).replace(" ", "_")+".fasta";
				currResult.add(new File(currFilename));
				runner.addSeparateOutputFile(seqIdentities[j], new File(currFilename));
				StringBuffer sequence = new StringBuffer();
				String[] lines = OUTPUT.get(seqIdentities[j]).getOutputWithMargins().get(headers[i]).split("(?<=\\G.{80})");
				for(String s: lines){
					sequence.append(s.replace('0', 'N'));
					sequence.append("\n");
				}
				
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currFilename), "utf-8"));
				bw.write(header);
				bw.write(sequence.toString());
				bw.flush();
				bw.close();
			}
			result.put(seqIdentities[j], currResult);
		}
		return result;
	}

	public void writeToFile(String filename) throws IOException {

		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filename.concat(".fasta")), "utf-8"));
	
		String[] seqIdentities = OUTPUT.keySet().toArray(new String[OUTPUT.keySet().size()]);
		
		for (int j = 0; j < seqIdentities.length; j++) {
			String[] header = OUTPUT.get(seqIdentities[j]).getOutput().keySet()
					.toArray(new String[OUTPUT.get(seqIdentities[j]).getOutput().keySet().size()]);

			for (int i = 0; i < header.length; i++) {
				bw.write(header[i]);

				// Standard line length in .fasta = 80 (regex can't take
				// variables)
				String[] lines = OUTPUT.get(seqIdentities[j]).getOutput().get(header[i]).split("(?<=\\G.{80})");
				for (String s : lines) {
					bw.write(s.replace('0', 'N'));
					bw.write("\n");
				}

			}
		}

		bw.close();
	}
	
}

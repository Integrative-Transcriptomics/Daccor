/**
 * 
 */
package automaticReconstruction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import IO.Communicator;

/**
 * @author Alexander Seitz
 *
 */
public class ReconstructSequences {
	
	private String mainFolder = "";
	private String reference = "";
	private List<File> repeatFiles;
	private Map<String, File> genomeReconstructions = new HashMap<String, File>();
	private Map<String, List<File>> repeatReconstructions = new HashMap<String, List<File>>();
	private Map<String, String> outputPaths = new HashMap<String, String>();
	private Map<String, String[]> fastQFiles;
	private String fastqConfig = "";
	private String referenceEagerConfig = "";
	private String repeatConfig = "";
//	private MultiFastaReader referenceReader;
	
	public ReconstructSequences(List<File> repeats, String inputFileName, String fastqsConfigFile,
			String referenceEagerConfig) {
		this.repeatFiles = repeats;
		this.repeatConfig = "";
		this.fastqConfig = fastqsConfigFile;
		this.reference = inputFileName;
		this.referenceEagerConfig = referenceEagerConfig;
		for(File f: repeats){
			this.mainFolder = f.getParent();
			break;
		}
		parseReferenceEagerRunsConfig();
		parseFastQConfig();
		createEagerConfigsAndRunReconstruction();
	}
	
	
//	public ReconstructSequences(String inFolder, String reference, String fastqConfig, String referenceEAGERRun){
//		this.mainFolder = inFolder;
//		this.reference = reference;
//		this.repeatFiles = new LinkedList<String>();
//		this.fastQFiles = new HashMap<String, String[]>();
//		this.fastqConfig = fastqConfig;
//		identifyReferenceEagerRun(referenceEAGERRun);
//		parseFastQConfig();
//		parseRepeatConfigFile();
//		findFilesToReconstruct();
//		createEagerConfigsAndRunReconstruction();
////		this.referenceReader = new MultiFastaReader(this.reference);
//	}

	/**
	 * @param repeatConfigFile
	 * @param inputFileName
	 * @param fastqsConfigFile
	 * @param eagerReferenceFolder
	 * @param outputFolder
	 */
	public ReconstructSequences(String repeatConfigFile, String inputFileName, String fastqsConfigFile,
			String eagerReferenceconfig, String outputFolder) {
		this.repeatConfig = repeatConfigFile;
		this.fastqConfig = fastqsConfigFile;
		this.reference = inputFileName;
		this.mainFolder = outputFolder;
		this.referenceEagerConfig = eagerReferenceconfig;
		parseReferenceEagerRunsConfig();
		parseRepeatConfigFile();
		parseFastQConfig();
		createEagerConfigsAndRunReconstruction();
	}

	/**
 * @param referenceEAGERRun
 */
private void parseReferenceEagerRunsConfig() {
	this.genomeReconstructions = new HashMap<String, File>();
	int numGenomes = 0;
	if(new File(this.referenceEagerConfig).exists()){
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(new File(this.referenceEagerConfig)));
			String currLine = "";
			while((currLine=br.readLine())!= null){
				String[] splitted = currLine.split("\t");
				if(splitted.length==2 && new File(splitted[1].trim()).exists()){
					this.genomeReconstructions.put(splitted[0],new File(splitted[1]));
					numGenomes++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}else{
		System.out.println("reference eager config file not found");
	}
	System.out.println("found "+numGenomes+" already reconstructed genomes");
}

private void parseRepeatConfigFile(){
	this.repeatFiles = new LinkedList<File>();
	int numRepeats = 0;
	if(new File(this.repeatConfig).exists()){
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(new File(this.repeatConfig)));
			String currLine = "";
			while((currLine=br.readLine())!= null){
				if(new File(currLine.trim()).exists()){
					this.repeatFiles.add(new File(currLine.trim()));
					numRepeats++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}else{
		System.out.println("repeat config file not found");
	}
	System.out.println("found "+numRepeats+" repeats to reconstruct");
}

	/**
 * 
 */
private void parseFastQConfig() {
	this.fastQFiles = new HashMap<String, String[]>();
	int numSamples = 0;
	if(new File(this.fastqConfig).exists()){
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(new File(this.fastqConfig)));
			String currLine = "";
			while((currLine=br.readLine())!= null){
				String[] splitted = currLine.split("\t");
				if(splitted.length >= 2 && splitted.length <=3){
					String name = splitted[0];
					String[] fqFiles = new String[splitted.length-1];
					for(int i=1; i<splitted.length; i++){
						fqFiles[i-1] = splitted[i];
					}
					this.fastQFiles.put(name, fqFiles);
					numSamples++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}else{
		System.out.println("fastq config file not found");
	}
	System.out.println("found "+numSamples+" samples to reconstruct");
}

	/**
 * 
 */
private void createEagerConfigsAndRunReconstruction() {
	
	System.out.println("starting reconstruction for "+this.fastQFiles.size()+" samples");
	
	for(String sampleName: this.fastQFiles.keySet()){
		System.out.println("\t for sample "+sampleName);
		// first reconstruct the whole genome
		File refOutFolder = new File(this.mainFolder+"/"+sampleName);
		this.outputPaths.put(sampleName, refOutFolder.getAbsolutePath());
		refOutFolder.mkdirs();
		String[] fastqFiles = this.fastQFiles.get(sampleName);
		File eagerFolderReference = new File(" ");
		System.out.println("reconstructing for sample: "+sampleName);
		if(this.genomeReconstructions.containsKey(sampleName)){
			eagerFolderReference = this.genomeReconstructions.get(sampleName);
		}
		if(!eagerFolderReference.exists()){
			this.genomeReconstructions.put(sampleName, refOutFolder);
//			this.genomeReconstruction.put(sampleName, eagerFolderReference);
			Communicator c = createCommunicator(refOutFolder, fastqFiles, this.reference);
			String configFile = writeCommunicatorToFile(refOutFolder, c);

			String[] runEager = new String[]{"eagercli", configFile}; 

			try {
				Process process = new ProcessBuilder(runEager).start();
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// then reconstruct each sequence individually
		for(File repeatFile: this.repeatFiles){
			File repeatOutFolder = new File(refOutFolder.getAbsoluteFile()+"/tmp_reconstruction/"+"/EagerTmp_"+repeatFile.getName());
			List<File> reconstructions = new LinkedList<File>();
			if(this.repeatReconstructions.containsKey(sampleName)){
				reconstructions = this.repeatReconstructions.get(sampleName);
			}
			reconstructions.add(repeatOutFolder);
			this.repeatReconstructions.put(sampleName, reconstructions);
			repeatOutFolder.mkdirs();

			Communicator c1 = createCommunicator(repeatOutFolder, fastqFiles, repeatFile.getAbsolutePath());

			// write the config file
			String configFile = writeCommunicatorToFile(repeatOutFolder, c1);
			String[] linkCM = new String[]{"ln", "-s", this.genomeReconstructions.get(sampleName).getAbsolutePath()+"/1-AdapClip", repeatOutFolder+"/1-AdapClip"};
			String[] runEager = new String[]{"eagercli", configFile};
			try {
				Process process = new ProcessBuilder(linkCM).start();
				process.waitFor();
				process = new ProcessBuilder(runEager).start();
				process.waitFor();
				if(new File(repeatFile.getParent()).exists()){
					for(String filename: new File(repeatFile.getParent()).list()){
						if(filename.startsWith("DONE")){
							File doneFile = new File(new File(repeatFile.getParent()).getAbsolutePath()+"/"+filename);
							if(doneFile.isFile()){
								doneFile.delete();
							}
						}
					}
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	}

	/**
	 * @param outFolder
	 * @param c
	 */
	private String writeCommunicatorToFile(File outFolder, Communicator c) {
		String configFile = outFolder + "/" + "conf-EAGER.xml";
		XStream xstream = new XStream(new StaxDriver());
		String xml = xstream.toXML(c);
		FileWriter fw;
		try {
			fw = new FileWriter(new File(configFile));
			fw.write(xml);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return configFile;
	}

	/**
	 * @param outFolder
	 * @param forwardFq
	 * @param reverseFq
	 * @return
	 */
	private Communicator createCommunicator(File outFolder, String[] fastqFiles, String reference) {
		Communicator c = new Communicator();
		// set the values for the communicator class
		c.setGUI_filepathresults(outFolder.getAbsolutePath());
		c.setGUI_resultspath(outFolder.getAbsolutePath());
		ArrayList<String> inputFastqFiles = new ArrayList<String>();
		if(fastqFiles.length == 1){
			inputFastqFiles.add(new File(fastqFiles[0]).getAbsolutePath());
			c.setMerge_type("SINGLE");
		}else if(fastqFiles.length == 2){
			inputFastqFiles.add(new File(fastqFiles[0]).getAbsolutePath());
			inputFastqFiles.add(new File(fastqFiles[1]).getAbsolutePath());
			c.setMerge_type("PAIRED");
		}
		c.setGUI_inputfiles(inputFastqFiles);
		c.setGUI_reference(reference);
		
		// run rest pipeline
		c.setRun_fastqc(false);
		c.setRun_clipandmerge(true);
		c.setRun_qualityfilter(false);
		c.setRun_mapping(true);
		c.setRun_coveragecalc(true);
		c.setRun_mapdamage(false);
		c.setRun_complexityestimation(false);
		c.setRun_coveragecalc(true);
		c.setRun_gatksnpcalling(true);
		c.setRun_gatksnpfiltering(false);
		c.setRun_vcf2draft(true);
		c.setRun_cleanup(true);
		c.setRun_reportgenerator(true);
		c.setRun_mapping_extractmappedandunmapped(false);
		c.setRun_mt_capture_mode(false);
		c.setRun_pmdtools(false);
		// BWA
		c.setMapper_to_use("BWAMem");
		
		//CM
		c.setMerge_only_clipping(false);
		c.setMerge_keep_only_merged(false);
		c.setQuality_minreadquality(20);
		c.setQuality_readlength(30);
		c.setMerge_tool("Clip&Merge");
		
		// GATK
		c.setGatk_caller("UnifiedGenotyper");
		c.setGatk_emit_all_sites(true);
		
		// coverage for reconstruction
		c.setVcf2dmincov(3);
		return c;
	}

	/**
	 * @return
	 */
	public Map<String, File> getGenomeReconstruction() {
		return this.genomeReconstructions;
	}

	/**
	 * @return
	 */
	public Map<String, List<File>> getRepeatReconstructions() {
		return this.repeatReconstructions;
	}

	/**
	 * @return
	 */
	public Map<String, String> getOutputPaths() {
		return this.outputPaths;
	}

}

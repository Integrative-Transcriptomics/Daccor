package userinterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import automaticReconstruction.CombineReconstructions;
import automaticReconstruction.ReconstructSequences;
import inputHandler.ParseVmatch;
import inputHandler.Runner;

/**
 * 
 * @author Friederike Hanssen
 * 
 * This class holds all parameters needed for the command-line interface, as well as providing the default parameters
 *
 */
public class CommandLineInterface {

	private Options identifyOptions;
	private Options reconstructionOptions;
	private Options combineOptions;
	private Options pipelineOptions;
	
	private static final Integer DEFAULT_THRESHOLD = 51;
	private static final Integer DEFAULT_WINSIZE = 17;
	private static final Integer DEFAULT_NUM_MISMATCHES = 0;
	private static final boolean DEFAULT_IS_SMALL = false;
	private static final boolean DEFAULT_SEPARATELY = false;
//	private static final Integer  DEFAULT_THREADS_PER_CHROMOSOME = 1;
	private static final Integer  DEFAULT_THREADS = 1;
	private static final boolean DEFAULT_WRITE_FASTA_SEPARATELY = false;
	private static final Integer  DEFAULT_MARGIN_SIZE = 0;
	
	public String CLASS_NAME = "DACCOR";
	public String VERSION = "0.1";
	
	// variables for analyze
	private static Integer windowSize = setDefaultWinSize();
	private static Integer numberOfMismatches = setDefaultNumOfMismatches();
	private static Integer threshold = setDefaultThreshold();
	private static String inputFileName;
	private static String outputFileName;
	private static String gffFileName = "";
	private static boolean isSmall = setDefaultIsSmall();
	private static Integer readlength = setDefaultReadLength();
//	private static Integer threadsPerChromosome = setDefaultNumberThreadsPerChromosome();
	private static Integer threads = setDefaultNumberThreads();
	private static boolean writeFastaSeparately = setDefaultWriteFastaSeparately();
	private static Integer marginSize = setDefaultMarginSize();
	private static boolean runSeparately = setDefaultIsSeparately();
	
	// variables for reconstruction
	private String repeatConfigFile = "";
	private String fastqsConfigFile = "";
	private String outputFolder = "";
	private String eagerReferenceFolder = "";
	
	// variables for combine reconstruction
	private String reconstructedGenomes = "";
	private String reconstructedRepeats = "";
	private String vmatchFile = "";

	public CommandLineInterface(String[] inputParameters) {
		
		loadMetadata();

		if (!(inputParameters.length > 0)) {
			printMainHelp();
		} else {
			switch(inputParameters[0]){
				case "identify": runIdentify(Arrays.copyOfRange(inputParameters, 1, inputParameters.length)); break;
				case "reconstruct": runReconstruction(Arrays.copyOfRange(inputParameters, 1, inputParameters.length)); break;
				case "combine": runCombineReconstruction(Arrays.copyOfRange(inputParameters, 1, inputParameters.length)); break;
				case "pipeline": runPipelineReconstruction(Arrays.copyOfRange(inputParameters, 1, inputParameters.length)); break;
				default:
					printMainHelp();
					System.exit(1);
			}
		}
	}

	/**
	 * @param copyOfRange
	 */
	private void runPipelineReconstruction(String[] inputParameters) {
		this.pipelineOptions = setPipelineOptions();
		if (!(inputParameters.length > 0)) {
			printPipelineHelp();
		}else{
			parsePipelineOptions(inputParameters);
			Set<String> references = new HashSet<String>();
			Map<String,List<File>> repeatFiles = new HashMap<String, List<File>>();
			if(this.vmatchFile.length()>0 && new File(this.vmatchFile).exists()) {
				ParseVmatch pv = new ParseVmatch(inputFileName, outputFileName, threshold, vmatchFile, marginSize, writeFastaSeparately);
				references = pv.getReferences();
				for(String ref: references) {
					repeatFiles.put(ref, pv.getRepeatFiles(ref));
				}
			}else {
				Runner runner = new Runner(inputFileName,outputFileName,gffFileName,windowSize,numberOfMismatches,threshold, isSmall, runSeparately, threads, writeFastaSeparately, marginSize);
				runner.run();
				references = runner.getReferences();
				for(String ref: references) {
					repeatFiles.put(ref, runner.getRepeatFiles(ref));
				}
			}
			System.out.println("finished analyzing reference");
			for(String ref: references){
				System.out.println("starting to reconstruct "+ref);
				ReconstructSequences rs = new ReconstructSequences(repeatFiles.get(ref),inputFileName, fastqsConfigFile, eagerReferenceFolder);
				new CombineReconstructions(rs.getGenomeReconstruction(), rs.getRepeatReconstructions());
			}
		}
		
	}

	/**
	 * @param copyOfRange
	 */
	private void runCombineReconstruction(String[] inputParameters) {
		this.combineOptions = setCombineOptions();
		if (!(inputParameters.length > 0)) {
			printCombineHelp();
		}else{
			parseCombineOptions(inputParameters);
			new CombineReconstructions(this.reconstructedGenomes, this.reconstructedRepeats, this.outputFolder);
		}
	}

	/**
	 * @param copyOfRange
	 */
	private void runReconstruction(String[] inputParameters) {
		this.reconstructionOptions = setReconstructionOptions();
		if (!(inputParameters.length > 0)) {
			printReconstructionHelp();
		}else{
			parseReconstructionOptions(inputParameters);
			new ReconstructSequences(repeatConfigFile, inputFileName, fastqsConfigFile, eagerReferenceFolder, outputFolder);
		}
	}

	/**
	 * @param inputParameters 
	 * 
	 */
	private void runIdentify(String[] inputParameters) {
		this.identifyOptions = setIdentifyOptions();
		if (!(inputParameters.length > 0)) {
			printAnalyzeHelp();
		}else{
			parseAnalyzeInput(inputParameters);
			if(this.vmatchFile.length()>0 && new File(this.vmatchFile).exists()) {
				new ParseVmatch(inputFileName, outputFileName, threshold, vmatchFile, marginSize, writeFastaSeparately);
			}else {
				validateK();
				Runner runner = new Runner(inputFileName,outputFileName,gffFileName,windowSize,numberOfMismatches,threshold, isSmall, runSeparately, threads, writeFastaSeparately, marginSize);
				runner.run();
			}
		}
	}

	//Only large enough,uneven k's are permitted, If k is even, subtract 1 
	private void validateK() {
	
		if(windowSize > 16){
			if(windowSize%2 == 0){
				windowSize = windowSize-1;
			}
		}else{
			System.err.println("Please enter an odd k >= 17");
			printAnalyzeHelp();
		}
			
		
	}
	
	private Options setCombineOptions(){
		final Options options = new Options();
		
		Option genomes = Option.builder("g")
				.longOpt("genomes")
				.required()
				.hasArg()
				.desc("config file for reconstructed genomes, REQUIRED")
				.build();
		options.addOption(genomes);
		
		Option repeats = Option.builder("r")
				.longOpt("repeats")
				.required()
				.hasArg()
				.desc("config file for reconstructed repeats, REQUIRED")
				.build();
		options.addOption(repeats);
		
		Option output = Option.builder("o").longOpt("output").required(true).hasArg(true).desc("output folder, REQUIRED").build();
		options.addOption(output);
		options.addOption("h", "help", false, "Prints options");
		
		return options;
	}
	
	private Options setReconstructionOptions(){
		final Options options = new Options();
		
		Option repeats = Option.builder("r")
							.longOpt("repeats")
							.required()
							.hasArg()
							.desc("config file for repeat input files, REQUIRED")
							.build();
		options.addOption(repeats);
		
		Option input = Option.builder("i").longOpt("input").required(true).hasArg(true).desc("input filename (reference), REQUIRED").build();
		options.addOption(input);
		
		Option config = Option.builder("f")
							.longOpt("fastqs")
							.required()
							.hasArg()
							.desc("config file for fastq files, REQUIRED")
							.build();
		options.addOption(config);
		
		Option output = Option.builder("o").longOpt("output").required(true).hasArg(true).desc("output folder, REQUIRED").build();
		options.addOption(output);
		
		options.addOption("e", "eager", true, "config file for reconstructed samples already reconstructed with EAGER");
		options.addOption("h", "help", false, "Prints options");
		
		return options;
	}

	private Options setIdentifyOptions() {
		
		final Options options = new Options();
		
		Option input = Option.builder("i").longOpt("input").required(true).hasArg(true).desc("input filename, REQUIRED").build();
		options.addOption(input);
		
		Option output = Option.builder("o").longOpt("output").required(true).hasArg(true).desc("output filename, REQUIRED").build();
		options.addOption(output);
		
		options.addOption("k", "kmersize", true, "size of initial kmer [readlength/2 OR "+DEFAULT_WINSIZE+"]");
		options.addOption("m", "mismatches", true, "number of mismatches allowed ["+DEFAULT_NUM_MISMATCHES+"]");
		options.addOption("t", "threshold", true, "Min length of displayed results [readlength OR " + DEFAULT_THRESHOLD+"]"); 
		options.addOption("g","gff",true,"Path of GFF file");
		options.addOption("h", "help", false, "Prints options");
//		options.addOption("s", "small", false, "Return partial substrings");
		options.addOption("ws", "writeSeparately", false, "write entries into separate files ["+DEFAULT_SEPARATELY+"]");
		options.addOption("S", "separately", false, "Analyze each sequence separately ["+DEFAULT_SEPARATELY+"]");
		options.addOption("rl", "readlength", true, "readlength ["+DEFAULT_WINSIZE+"]");
//		options.addOption("ps", "threadssequence", true, "Number of threads per chromosome [" + DEFAULT_THREADS_PER_CHROMOSOME+"]"); 
		options.addOption("p","processes",true,"Number of threads per genome [" + DEFAULT_THREADS+"]");
		options.addOption("M", "margin", true, "Margin around repeat to extract ["+DEFAULT_MARGIN_SIZE+"]");
		options.addOption("v", "vmatch", true, "don't analyze but parse vmatch output");
		
		return options;
	}

	/**
	 * @return
	 */
	private Options setPipelineOptions() {
		final Options options = new Options();
		
		Option input = Option.builder("i").longOpt("input").required(true).hasArg(true).desc("input filename, REQUIRED").build();
		options.addOption(input);
		
		Option output = Option.builder("o").longOpt("output").required(true).hasArg(true).desc("output filename, REQUIRED").build();
		options.addOption(output);
		
		Option config = Option.builder("f")
				.longOpt("fastqs")
				.required()
				.hasArg()
				.desc("config file for fastq files, REQUIRED")
				.build();
		options.addOption(config);
		
		options.addOption("k", "kmersize", true, "size of initial kmer [readlength/2 OR "+DEFAULT_WINSIZE+"]");
		options.addOption("m", "mismatches", true, "number of mismatches allowed ["+DEFAULT_NUM_MISMATCHES+"]");
		options.addOption("t", "threshold", true, "Min length of displayed results [readlength OR " + DEFAULT_THRESHOLD+"]"); 
		options.addOption("g","gff",true,"Path of GFF file");
		options.addOption("h", "help", false, "Prints options");
		options.addOption("S", "separately", false, "Analyze each sequence separately ["+DEFAULT_SEPARATELY+"]");
		options.addOption("rl", "readlength", true, "readlength ["+DEFAULT_WINSIZE+"]");
//		options.addOption("ps", "threadssequence", true, "Number of threads per chromosome [" + DEFAULT_THREADS_PER_CHROMOSOME+"]"); 
		options.addOption("p","processes",true,"Number of threads per genome [" + DEFAULT_THREADS+"]");
		options.addOption("M", "margin", true, "Margin around repeat to extract ["+DEFAULT_MARGIN_SIZE+"]");
		options.addOption("e", "eager", true, "config file for reconstructed samples already reconstructed with EAGER");
		options.addOption("v", "vmatch", true, "don't analyze but parse vmatch output");
		
		return options;
	}
	


	private void printMainHelp( ) {
		
		final PrintWriter writer = new PrintWriter(System.out);
		writer.println("Program: "+this.CLASS_NAME);
		writer.println("Version: "+this.VERSION);
		writer.println();
		writer.print("usage:\tDaccor <tool> <options>");
		writer.println();
		writer.println("--HELP--");
		writer.println("\tTool\t\tDescription");
		writer.println();
		writer.println("\tidentify\tAnalyze a reference file and identify repetitive regions");
		writer.println("\treconstruct\tReconstruct each region separately with EAGER");
		writer.println("\tcombine\t\tCombine the reconstructed regions with the reconstructed references");
		writer.println("\tpipeline\tPipeline from the identification of repetitive regions\n\t\t\tto the combined reconstruction of the genome");
		writer.println("--End of HELP--");
		writer.close();

	}

	private void printCombineHelp( ) {
		
		final String commandLineSyntax = " ";
		final PrintWriter writer = new PrintWriter(System.out);
		final HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(writer, 80, commandLineSyntax, "\n--HELP--", combineOptions, 5,
				3, "--End of HELP--", true);
		writer.close();

	}

	private void printReconstructionHelp( ) {
		
		final String commandLineSyntax = " ";
		final PrintWriter writer = new PrintWriter(System.out);
		final HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(writer, 80, commandLineSyntax, "\n--HELP--", reconstructionOptions, 5,
				3, "--End of HELP--", true);
		writer.close();

	}

	private void printAnalyzeHelp( ) {
		
		final String commandLineSyntax = " ";
		final PrintWriter writer = new PrintWriter(System.out);
		final HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(writer, 80, commandLineSyntax, "\n--HELP--", identifyOptions, 5,
				3, "--End of HELP--", true);
		writer.close();

	}

	/**
	 * 
	 */
	private void printPipelineHelp() {
		final String commandLineSyntax = " ";
		final PrintWriter writer = new PrintWriter(System.out);
		final HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(writer, 80, commandLineSyntax, "\n--HELP--", pipelineOptions, 5,
				3, "--End of HELP--", true);
		writer.close();
	}

	private void loadMetadata(){
		Properties properties = new Properties();
		try {
			//load version
			InputStream in = CommandLineInterface.class.getResourceAsStream("/version.properties");
			properties.load(in);
			this.VERSION = properties.getProperty("version");
			in.close();
			// load title
			in = CommandLineInterface.class.getResourceAsStream("/title.properties");
			properties.load(in);
			this.CLASS_NAME = properties.getProperty("title");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseCombineOptions(final String[] inputParameters){
		final CommandLineParser parser = new DefaultParser();
		CommandLine commandLine;
		try{
			commandLine = parser.parse(combineOptions, inputParameters);
			if(commandLine.hasOption("h") || commandLine.hasOption("help")){
				printCombineHelp();
			}

			if(commandLine.hasOption("g")){
				reconstructedGenomes = commandLine.getOptionValue("g");
			}
			if(commandLine.hasOption("g")){
				reconstructedRepeats = commandLine.getOptionValue("r");
			}
			if(commandLine.hasOption("o")){
				outputFolder = commandLine.getOptionValue("o");
			}
		}catch(ParseException e){
			printCombineHelp();
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	private void parseReconstructionOptions(final String[] inputParameters){
		final CommandLineParser parser = new DefaultParser();
		CommandLine commandLine;
		try{
			commandLine = parser.parse(reconstructionOptions, inputParameters);
			if(commandLine.hasOption("h") || commandLine.hasOption("help")){
				printReconstructionHelp();
			}
			if(commandLine.hasOption("r")){
				repeatConfigFile = commandLine.getOptionValue("r");
			}
			if(commandLine.hasOption("i")){
				inputFileName = commandLine.getOptionValue("i");
			}
			if(commandLine.hasOption("f")){
				fastqsConfigFile = commandLine.getOptionValue("f");
			}
			if(commandLine.hasOption("o")){
				outputFolder = commandLine.getOptionValue("o");
			}
			if(commandLine.hasOption("e")){
				eagerReferenceFolder = commandLine.getOptionValue("e");
			}
		}catch(ParseException e){
			printReconstructionHelp();
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	private void parseAnalyzeInput(final String[] inputParameters){

		final CommandLineParser parser = new DefaultParser();
		CommandLine commandLine;
		try{
			commandLine = parser.parse(identifyOptions, inputParameters);
			if(commandLine.hasOption("h") || commandLine.hasOption("help")){
				printAnalyzeHelp();
			}
			if(commandLine.hasOption("i")){
				inputFileName = commandLine.getOptionValue("i");
			}
			if( commandLine.hasOption("input")){
				inputFileName = commandLine.getOptionValue("input");
			}
			if(commandLine.hasOption("rl")){
				readlength = Integer.parseInt(commandLine.getOptionValue("rl"));
				if(!(commandLine.hasOption("k") && commandLine.hasOption("kmersize"))){
					windowSize = readlength/2;
				}
				if(!(commandLine.hasOption("t") && commandLine.hasOption("threshold"))){
					threshold = readlength;
				}
			}
			if(commandLine.hasOption("readlength")){
				readlength = Integer.parseInt(commandLine.getOptionValue("readlength"));
				if(!(commandLine.hasOption("k") && commandLine.hasOption("kmersize"))){
					windowSize = readlength/2;
				}	
				if(!(commandLine.hasOption("t") && commandLine.hasOption("threshold"))){
					threshold = readlength;
				}
			}
			if(commandLine.hasOption("k")){
				windowSize = Integer.parseInt(commandLine.getOptionValue("k"));
			}
			if(commandLine.hasOption("kmersize")){
				windowSize = Integer.parseInt(commandLine.getOptionValue("kmersize"));
			}
			if(commandLine.hasOption("m")){
				numberOfMismatches = Integer.parseInt(commandLine.getOptionValue("m"));
			}
			if(commandLine.hasOption("mismatches")){
				numberOfMismatches = Integer.parseInt(commandLine.getOptionValue("mismatches"));
			}
			if(commandLine.hasOption("t")){
				threshold = Integer.parseInt(commandLine.getOptionValue("t"));
			}
			if(commandLine.hasOption("threshold")){
				threshold = Integer.parseInt(commandLine.getOptionValue("threshold"));
			}
			if(commandLine.hasOption("g")){
				gffFileName = commandLine.getOptionValue("g");
			}
			if(commandLine.hasOption("gff")){
				gffFileName = commandLine.getOptionValue("gff");
			}
			
			if(commandLine.hasOption("o")){
				outputFileName = commandLine.getOptionValue("o").concat("_k"+windowSize).concat("_Mis" + numberOfMismatches).concat("_Thres"+threshold);
			}
			if( commandLine.hasOption("output")){
				outputFileName = commandLine.getOptionValue("output").concat("_k"+windowSize).concat("_Mis" + numberOfMismatches).concat("_Thres"+threshold);;
				
			}
//			if(commandLine.hasOption("s") || commandLine.hasOption("small")){
//				isSmall = true;
//			}
			if(commandLine.hasOption("S") || commandLine.hasOption("separately")){
				runSeparately = true;
			}
//			if(commandLine.hasOption("ps")){
//				threadsPerChromosome = Integer.parseInt(commandLine.getOptionValue("ps"));
//			}
//			if(commandLine.hasOption("threadssequence")){
//				threadsPerChromosome = Integer.parseInt(commandLine.getOptionValue("threadssequence"));
//			}
			if(commandLine.hasOption("p")){
				threads = Integer.parseInt(commandLine.getOptionValue("p"));
			}
			if(commandLine.hasOption("processes")){
				threads = Integer.parseInt(commandLine.getOptionValue("processes"));
			}
			if(commandLine.hasOption("ws")){
				writeFastaSeparately = true;
			}
			if(commandLine.hasOption("M")){
				marginSize = Integer.parseInt(commandLine.getOptionValue("M"));
			}
			if(commandLine.hasOption("v")) {
				this.vmatchFile  = commandLine.getOptionValue("v");
			}

		}catch(ParseException e){
			printAnalyzeHelp();
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	


	/**
	 * @param inputParameters
	 */
	private void parsePipelineOptions(String[] inputParameters) {
		final CommandLineParser parser = new DefaultParser();
		CommandLine commandLine;
		try{
			commandLine = parser.parse(pipelineOptions, inputParameters);
			if(commandLine.hasOption("h") || commandLine.hasOption("help")){
				printPipelineHelp();
			}
			if(commandLine.hasOption("i")){
				inputFileName = commandLine.getOptionValue("i");
			}
			if( commandLine.hasOption("input")){
				inputFileName = commandLine.getOptionValue("input");
			}
			if(commandLine.hasOption("rl")){
				readlength = Integer.parseInt(commandLine.getOptionValue("rl"));
				if(!(commandLine.hasOption("k") && commandLine.hasOption("kmersize"))){
					windowSize = readlength/2;
				}
				if(!(commandLine.hasOption("t") && commandLine.hasOption("threshold"))){
					threshold = readlength;
				}
			}
			if(commandLine.hasOption("readlength")){
				readlength = Integer.parseInt(commandLine.getOptionValue("readlength"));
				if(!(commandLine.hasOption("k") && commandLine.hasOption("kmersize"))){
					windowSize = readlength/2;
				}	
				if(!(commandLine.hasOption("t") && commandLine.hasOption("threshold"))){
					threshold = readlength;
				}
			}
			if(commandLine.hasOption("k")){
				windowSize = Integer.parseInt(commandLine.getOptionValue("k"));
			}
			if(commandLine.hasOption("kmersize")){
				windowSize = Integer.parseInt(commandLine.getOptionValue("kmersize"));
			}
			if(commandLine.hasOption("m")){
				numberOfMismatches = Integer.parseInt(commandLine.getOptionValue("m"));
			}
			if(commandLine.hasOption("mismatches")){
				numberOfMismatches = Integer.parseInt(commandLine.getOptionValue("mismatches"));
			}
			if(commandLine.hasOption("t")){
				threshold = Integer.parseInt(commandLine.getOptionValue("t"));
			}
			if(commandLine.hasOption("threshold")){
				threshold = Integer.parseInt(commandLine.getOptionValue("threshold"));
			}
			if(commandLine.hasOption("g")){
				gffFileName = commandLine.getOptionValue("g");
			}
			if(commandLine.hasOption("gff")){
				gffFileName = commandLine.getOptionValue("gff");
			}
			
			if(commandLine.hasOption("o")){
				outputFileName = commandLine.getOptionValue("o").concat("_k"+windowSize).concat("_Mis" + numberOfMismatches).concat("_Thres"+threshold);
			}
			if( commandLine.hasOption("output")){
				outputFileName = commandLine.getOptionValue("output").concat("_k"+windowSize).concat("_Mis" + numberOfMismatches).concat("_Thres"+threshold);;
				
			}
			if(commandLine.hasOption("S") || commandLine.hasOption("separately")){
				runSeparately = true;
			}
//			if(commandLine.hasOption("ps")){
//				threadsPerChromosome = Integer.parseInt(commandLine.getOptionValue("ps"));
//			}
//			if(commandLine.hasOption("threadssequence")){
//				threadsPerChromosome = Integer.parseInt(commandLine.getOptionValue("threadssequence"));
//			}
			if(commandLine.hasOption("p")){
				threads = Integer.parseInt(commandLine.getOptionValue("p"));
			}
			if(commandLine.hasOption("processes")){
				threads = Integer.parseInt(commandLine.getOptionValue("processes"));
			}
			writeFastaSeparately = true;
			if(commandLine.hasOption("M")){
				marginSize = Integer.parseInt(commandLine.getOptionValue("M"));
			}
			if(commandLine.hasOption("e")){
				eagerReferenceFolder = commandLine.getOptionValue("e");
			}
			if(commandLine.hasOption("f")){
				fastqsConfigFile = commandLine.getOptionValue("f");
			}
		}catch(ParseException e){
			printPipelineHelp();
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	private static Integer setDefaultThreshold() {
		return DEFAULT_THRESHOLD;
	}

	private static Integer setDefaultNumOfMismatches() {
		return DEFAULT_NUM_MISMATCHES;
	}

	private static Integer setDefaultWinSize() {
		return DEFAULT_WINSIZE;
	}
	
	private static boolean setDefaultIsSmall(){
		return DEFAULT_IS_SMALL;
	}
	
	private static boolean setDefaultIsSeparately(){
		return DEFAULT_SEPARATELY;
	}
	
	private static Integer setDefaultReadLength(){
		return DEFAULT_WINSIZE;
	}
	
//	private static Integer setDefaultNumberThreadsPerChromosome(){
//		return DEFAULT_THREADS_PER_CHROMOSOME;
//	}
	
	private static Integer setDefaultNumberThreads(){
		return DEFAULT_THREADS;
	}
	
	private static boolean setDefaultWriteFastaSeparately(){
		return DEFAULT_WRITE_FASTA_SEPARATELY;
	}
	
	private static Integer setDefaultMarginSize(){
		return DEFAULT_MARGIN_SIZE;
	}
}

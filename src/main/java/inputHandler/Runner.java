package inputHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import outputUtils.BedFile;
import outputUtils.GffModifier;
import outputUtils.MultiFastaOutput;
import outputUtils.SequenceStats;
import outputUtils.SimpleStats;
import reader.GffReader;
import reader.MultiFastaReader;
import storage.SortedStorage;
import storage.WorkingStorage;
import utilities.Utilities;
import writer.BedWriter;
import writer.GffWriter;
import writer.MultiFastaWriter;
import writer.SummaryWriter;

/**
 * 
 * @author Friederike Hanssen
 *
 */
public class Runner {

	private final String INPUTFILENAME;
	private final String OUTPUTFILENAME;
	private final String GFFFILENAME;
	private final Integer WINDOWSIZE;
	private final Integer NUMBERMISMATCHES;
	private final Integer THRESHOLD;
	private final boolean ISSMALL;
	private final boolean RUNSEPARATELY;

	private final Integer THREADSPERGENOME;
//	private final Integer THREADSPERSEQUENCE;

	private MultiFastaReader fastaReader;
	/**
	 * @return the fastaReader
	 */
	public MultiFastaReader getFastaReader() {
		return fastaReader;
	}

	private Map<String, MultiFastaOutput> repeats;
	private Map<String, SimpleStats> simpleStats;
	private Map<String, SequenceStats> seqStats;
	private Map<String, BedFile> bedFiles;
	private Map<String, Long> offsets = new HashMap<String, Long>();
	private Long totalSeqLength = 0l;
	private Integer marginSize = 0;
	
	private Map<String, List<File>> separateOutputFiles = new HashMap<String, List<File>>();
	
	private String folderWithSeparateSequences = "";

	private WorkingStorage combinedStorage;
	private boolean writeSeparately = false;
	
	public Runner(String inputFilename, String outputFilename, Integer threshold, Integer marginSize) {
		this.WINDOWSIZE = -1;
		this.INPUTFILENAME = inputFilename;
		this.OUTPUTFILENAME = outputFilename;
		this.GFFFILENAME = "";
		this.NUMBERMISMATCHES = 0;
		this.THRESHOLD = threshold;
		this.ISSMALL = false;
		this.RUNSEPARATELY = false;
		this.THREADSPERGENOME = 1;
		initialize();
	}

	public Runner(String inputFilename, String outputFilename, String gffFileName, Integer winS,
			Integer numOfMismatches, Integer threshold, boolean isSmall, boolean runSeparately, Integer threadsPerGenome, boolean writeSeparately, Integer marginSize) {
		this.INPUTFILENAME = inputFilename;
		this.OUTPUTFILENAME = outputFilename;
		this.GFFFILENAME = gffFileName;
		this.WINDOWSIZE = winS;
		this.NUMBERMISMATCHES = numOfMismatches;
		this.THRESHOLD = threshold;
		this.ISSMALL = isSmall;
		this.RUNSEPARATELY = runSeparately;
		this.THREADSPERGENOME = threadsPerGenome;
//		this.THREADSPERSEQUENCE = threadsPerSequence;
		this.writeSeparately = writeSeparately;
		this.marginSize = marginSize;
	}

	public void run() {
		initialize();
		if(this.RUNSEPARATELY){
			startMulti();
		}else{
			runCombinedAnalysis();
		}
	}

	/**
	 * 
	 */
	private void runCombinedAnalysis() {
		this.combinedStorage = new WorkingStorage();
		this.offsets = new HashMap<String, Long>();
		String[] identities = fastaReader.getSeqCollection().keySet()
				.toArray(new String[fastaReader.getSeqCollection().keySet().size()]);
		ExecutorService es = Executors.newFixedThreadPool(this.THREADSPERGENOME);
		long offset = 0;

		// identify k-mers in parallel
		for (int i = 0; i < identities.length; i++) {

			// If any identity appears twice, identities are not unique
			if (repeats.containsKey(identities[i])) {
				System.err.println("Sequencename was not unique. Please ensure uniqueness of your identities.");
				System.exit(0);// potentially hav it throw sth rather and handle
				// all system exit cases together in a class, to
				// ensure hierachy
			} else {
				IdentifyKMersCombined identifyKMers = new IdentifyKMersCombined(identities[i], fastaReader.getSeqCollection().
						get(identities[i]),	this.WINDOWSIZE, this.NUMBERMISMATCHES, this.THRESHOLD, 
						this.ISSMALL, this, this.THREADSPERGENOME, offset);
				es.submit(identifyKMers);
				this.offsets.put(identities[i], offset);
				offset += fastaReader.getSeqCollection().get(identities[i]).length() + this.NUMBERMISMATCHES + 1;
			}
		}
		this.totalSeqLength = offset - this.NUMBERMISMATCHES - 1;

		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// remove k-mers occuring only once
		combinedStorage.removeEntriesOccuringAtSingleIndex();
		combinedStorage.removeEmptyEntries();

		// calculate repetitive sequences

		//		for (int i = 0; i < identities.length; i++) {
		//				IdentifyRepeatsCombined identifyKMers = new IdentifyRepeatsCombined(null, fastaReader.getSeqCollection().
		//							get(identities[i]),	this.WINDOWSIZE, this.NUMBERMISMATCHES, this.THRESHOLD, 
		//							this.ISSMALL, this, this.THREADSPERGENOME, this.combinedStorage);
		IdentifyRepeatsCombined identifyKMers = new IdentifyRepeatsCombined(null, null,	this.WINDOWSIZE, this.NUMBERMISMATCHES, this.THRESHOLD, 
				this.ISSMALL, this, this.THREADSPERGENOME, this.combinedStorage, this.totalSeqLength);
		try {
			identifyKMers.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		}

		writeOutputFiles(repeats, simpleStats, seqStats, bedFiles);
		if(! (GFFFILENAME == null || GFFFILENAME.isEmpty())){
			createGffOutput();
		}

	}

	private void initialize() {
		fastaReader = new MultiFastaReader(INPUTFILENAME);
		repeats = new LinkedHashMap<String, MultiFastaOutput>();
		simpleStats = new LinkedHashMap<String, SimpleStats>();
		seqStats = new LinkedHashMap<String, SequenceStats>();
		bedFiles = new LinkedHashMap<String, BedFile>();
	}

	public synchronized void generateOutput(String identity, SortedStorage sorted){
		if(identity == null){
			String[] identities = fastaReader.getSeqCollection().keySet()
					.toArray(new String[fastaReader.getSeqCollection().keySet().size()]);
			for (int i = 0; i < identities.length; i++) {
				generateOutputForIdentity(identities[i], sorted);
			}
		}else{
			generateOutputForIdentity(identity, sorted);			
		}
	}

	/**
	 * @param identity
	 * @param sorted
	 */
	private void generateOutputForIdentity(String identity, SortedStorage sorted) {
		MultiFastaOutput output = new MultiFastaOutput(sorted,identity, this.marginSize, this);
		output.generate(this.offsets);
		repeats.put(identity, output);


		BedFile bf = new BedFile(sorted, identity);
		bf.generate(this.offsets);
		bedFiles.put(identity, bf);
		SimpleStats currSimpleStats = new SimpleStats(sorted, identity, this.offsets);
		currSimpleStats.generate();
		simpleStats.put(identity, currSimpleStats);

		SequenceStats currSeqStats = new SequenceStats(output,bf, identity);
		currSeqStats.generate(this.offsets);
		seqStats.put(identity, currSeqStats);
	}

	public synchronized void addToCombinedStorage(String identity, SortedStorage sorted){
		String[] mergingRepeats = sorted.getMapRepeats().keySet()
				.toArray(new String[sorted.getMapRepeats().keySet().size()]);
		for (int i = 0; i < mergingRepeats.length; i++) {
			Set<Integer> startIndicesOfMergeable = sorted.getMapRepeats().get(mergingRepeats[i]);
			this.combinedStorage.storeRepeatAtSetOfIndices(mergingRepeats[i], startIndicesOfMergeable);
		}
	}

	private void startMulti() {

		String[] identities = fastaReader.getSeqCollection().keySet()
				.toArray(new String[fastaReader.getSeqCollection().keySet().size()]);
		ExecutorService es = Executors.newFixedThreadPool(this.THREADSPERGENOME);
		for (int i = 0; i < identities.length; i++) {

			// If any identity appears twice, identities are not unique
			if (repeats.containsKey(identities[i])) {
				System.err.println("Sequencename was not unique. Please ensure uniqueness of your identities.");
				System.exit(0);// potentially hav it throw sth rather and handle
				// all system exit cases together in a class, to
				// ensure hierachy
			} else {
				this.offsets.put(identities[i], 0l);
				//				processSequence(identities[i]);
				IdentifyRepeatsSeparate identifyRepeats = new IdentifyRepeatsSeparate(identities[i], fastaReader.getSeqCollection().
						get(identities[i]),	this.WINDOWSIZE, this.NUMBERMISMATCHES, this.THRESHOLD, 
						this.ISSMALL, this, this.THREADSPERGENOME);
				es.submit(identifyRepeats);
			}
		}

		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		writeOutputFiles(repeats, simpleStats, seqStats, bedFiles);
		if(! (GFFFILENAME == null || GFFFILENAME.isEmpty())){
			createGffOutput();
		}
	}

	private void writeOutputFiles(Map<String, MultiFastaOutput> results, Map<String, SimpleStats> simpleStats,
			Map<String, SequenceStats> seqStats, Map<String, BedFile> bedFiles) {

		MultiFastaWriter mw = new MultiFastaWriter(results, this.marginSize, this);
		SummaryWriter sw = new SummaryWriter(simpleStats,seqStats);
		BedWriter bw = new BedWriter(bedFiles);


		try {
			mw.writeToFile(OUTPUTFILENAME);
			if(this.writeSeparately){
				mw.writeToSeparateFiles(OUTPUTFILENAME);
			}
			sw.writeToFile(OUTPUTFILENAME);
			bw.writeToFile(OUTPUTFILENAME);

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private void createGffOutput() {
		GffReader gffReader = new GffReader(GFFFILENAME);
		GffModifier gm = new GffModifier(bedFiles, gffReader.getEntries());
		gm.modify();

		GffWriter gw = new GffWriter(gm, gffReader.getHeader());
		try {
			gw.writeToFile(OUTPUTFILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public String getSequence(String name, int beginIndex, int endIndex){
		String result = "";
		if(name == null){
			String seq1 = Utilities.findBelongingSequenceName(offsets, beginIndex);
			String seq2 = Utilities.findBelongingSequenceName(offsets, endIndex);
			if("".equals(seq1) || "".equals(seq2)){
				result = "";
			}else if(!seq1.equals(seq2)){
					result = "";
			}else{
				long offset = this.offsets.get(seq1);
				result = this.fastaReader.getSeqCollection().get(seq1).substring(beginIndex-(int)offset, endIndex-(int)offset);
			}
		}else{
			if(this.fastaReader.getSeqCollection().containsKey(name)){
				long offset = 0l;
				if(offsets.containsKey(name)){
					offset = offsets.get(name);
				}
				result = this.fastaReader.getSeqCollection().get(name).substring(beginIndex-(int)offset, endIndex-(int)offset);
			}
		}
		return result;
	}

	/**
	 * 
	 */
	public String getFolderWithSeparateRepeats() {
		return this.folderWithSeparateSequences;
	}
	
	public void setFolderWithSeparateRepeats(String folder) {
		this.folderWithSeparateSequences = folder;
	}

	/**
	 * @return
	 */
	public Set<String> getReferences() {
		return this.fastaReader.getSeqCollection().keySet();
	}
	
	public void addSeparateOutputFile(String ref, File f){
		List<File> lis = new LinkedList<File>();
		if(this.separateOutputFiles.containsKey(ref)){
			lis = this.separateOutputFiles.get(ref);
		}
		lis.add(f);
		this.separateOutputFiles.put(ref, lis);
	}

	/**
	 * @param ref
	 * @return
	 */
	public List<File> getRepeatFiles(String ref) {
		List<File> result = new LinkedList<File>();
		if(this.separateOutputFiles.containsKey(ref)){
			result = this.separateOutputFiles.get(ref);
		}else{
			System.out.println("dont know key: "+ref);
			System.out.println("possible keys: ");
			System.out.println(this.separateOutputFiles.keySet());
		}
		return result;
	}

}

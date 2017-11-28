/**
 * 
 */
package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

import storage.Storage;

/**
 * @author Alexander Seitz
 *
 */
public class Utilities {

	public static BufferedReader getReader(String file){
		BufferedReader br = null;
		try {
			if(file.endsWith(".gz")){
				InputStream fileStream = new FileInputStream(file);
				InputStream gzipStream = new GZIPInputStream(fileStream);
				Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
				br = new BufferedReader(decoder);
			}else{
				br = new BufferedReader(new FileReader(new File(file)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return br;
	}

	public static boolean belongsToMe(String myName, String repeatName, Storage<TreeSet<Integer>> REPEATS, Map<String, Long> offsets){
		for(Integer index: REPEATS.getMapRepeats().get(repeatName)){
			if(myName.equals(Utilities.findBelongingSequenceName(offsets, index))){
				return true;
			}
		}
		return false;
	}

	public static boolean belongsToMe(String myName, String repeatName, Integer index, Map<String, Long> offsets){
			if(myName.equals(Utilities.findBelongingSequenceName(offsets, index))){
				return true;
			}
		return false;
	}


	/**
	 * @param offsets
	 * @return
	 */
	public static String findBelongingSequenceName(Map<String, Long> offsets, Integer index) {
		Long maxMatchingOffset = -1l;
		String matchingSequenceName = "";
		for(String sequenceName: offsets.keySet()){
			Long offset = offsets.get(sequenceName);
			if((long)index >= offset && (long)index > maxMatchingOffset){
				maxMatchingOffset = offset;
				matchingSequenceName = sequenceName;
			}
		}
		return matchingSequenceName;
	}
	
	public static void writeToFile(String s, File f){
		try {
			PrintWriter outMerged = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
			outMerged.println(s);
			outMerged.flush();
			outMerged.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

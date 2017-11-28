package writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import outputUtils.GffModifier;

/**
 * 
 * @author Friederike Hanssen
 *
 */

public class GffWriter {
	private final List<ArrayList<String>> gffValues;
	private final List<String> header;
	private final String TAB = "\t";
	
	public GffWriter(GffModifier gffValues,List<String> header){
		this.gffValues = gffValues.getModifiedEntries();
		this.header = header;
		
	}
	
	public void writeToFile(String filename) throws IOException{
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filename.concat("_modified.gff")), "utf-8"));

		for (String s: header) {
			bw.write(s);
			bw.write("\n");
		}
	
		for(ArrayList<String> l : gffValues){
			String[] string = l.toArray(new String[l.size()]);
			for(int i = 0; i< string.length; i++){
				bw.write(string[i]+TAB);
			}
			bw.write("\n");
		}
		bw.close();
		
	}

}



package org.renci.databridge.contrib.ingest.util;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.annotations.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.Analyzer.*;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.*;
import org.apache.lucene.util.Constants;
import org.apache.lucene.analysis.core.StopAnalyzer;



/**
 * This class holds the data for a single clinical trial read from the json file
 * 
 * @author Howard Lander -RENCI (www.renci.org)
 * 
 */
public class IngestUtils {
    protected Logger logger = null;

    /**
     * Run the lucene stop analyzer to get rid of stop words in the input
     * @param input The input string on which to run the stop analyzer
     * @return output The input string without the stop words.
     */
    public static String removeStopWords(String input) throws IOException{
       StringBuilder sb = new StringBuilder();
       StopAnalyzer analyzer = new StopAnalyzer();
       TokenStream tokenStream = analyzer.tokenStream("field", input);
       CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
       tokenStream.reset();
       while(tokenStream.incrementToken()) {
          sb.append(term.toString());
          sb.append(" ");
       }
       // Delete the last space, as the TokenStream doesn't seem to have an easy way 
       // to test the next token
       if (sb.length() > 0) {
          sb.deleteCharAt(sb.length() - 1);
       }
       tokenStream.end();
       tokenStream.close();

       return sb.toString();
    }

}

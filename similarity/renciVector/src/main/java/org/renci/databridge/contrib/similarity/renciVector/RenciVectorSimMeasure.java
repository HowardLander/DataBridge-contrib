package org.renci.databridge.contrib.similarity.renciVector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.renci.databridge.persistence.metadata.*;
import java.lang.IllegalArgumentException;
import com.rabbitmq.client.*;

/**
 *
 * @author Lander
 */
public abstract class RenciVectorSimMeasure implements SimilarityProcessor {

   public static final String VECTOR_DATA_COLLECTION = "collection";
   public static final String VECTOR_DATA_FILE = "file";
   public static final String VECTOR_DATA_ALL = "all";

   /**
    * This code implements a similarity for different types of "vector" data. These are
    * normally data where the similarity is going to be some function of a collection of attributes.
    * For example, for the schizConnect SAPS data it retrieves all the "files" for each collection.  
    * Each of these represents a single question in the SAPS assessment for a subject.  We are 
    * assuming the same number of files for each collection, though that may have to eventually be 
    * relaxed in this another similarity processor.  
    * 
    * For each collection, we build a vector of assessment values
    * then perform a similarity. The actual similarity is calculated by the call to computeSimilarity,
    * which is an abstract function implemented in it's own class and using one of a number of
    * possible algorithms.
    *
    * The params string for this function tells us where in the collectionTransfer object to find
    * the data and what field, if any to look for. The data is normally going to be in the "extra" field
    * of either the collection or the files of the collection. The param string will conform to the format
    *
    *        source:field
    *
    * where source is one of
    * 
    *         collection: the data comes from the extra field in the collection transfer objects
    *         file: the data comes from the extra field in the file transfer objects
    *
    *         and field is the key to use in the extra HashMap or the string "all" indicating that
    *         we are to use all of the values in the specified HashMap. Note that all is currently only
    *         supported for source type of collection.
    * 
    * @param collection1 The first collection object
    * @param collection2 The second collection object
    * @param params Any parameters needed for the execution.
    * @return The similarity calculated between the 2 collections.
    */
    public double compareCollections (CollectionTransferObject collection1,
                                      CollectionTransferObject collection2,
                                      String params) {
       double similarity = 0;
       double[] vector1 = null;
       double[] vector2 = null;

       // Split the params
       String[] splitParams = params.split(":");
       if (splitParams.length != 2) {
          throw new IllegalArgumentException("couldn't properly split " + params);
       }

       String source = splitParams[0];
       String field = splitParams[1];
       
       if (source.equalsIgnoreCase(VECTOR_DATA_FILE)) {
          // Produce the vectors for each collection. For each collection we want to grab all
          // of the files and make a vector of the values
          ArrayList<FileTransferObject> list1 = collection1.getFileList(); 
          ArrayList<FileTransferObject> list2 = collection2.getFileList(); 
          if (list1.size() != list2.size()) {
             // Some kind of error.  In theory we could also throw an exception here.
             similarity = -1.;
          } else {
             vector1 = new double[list1.size()];   
             vector2 = new double[list2.size()];   
 
             for (int i = 0; i < list1.size(); i++) {

                 FileTransferObject fto1 = list1.get(i);
                 HashMap<String, String> extra1 = fto1.getExtra();
                 String value1 = (String) extra1.get(field);

                 FileTransferObject fto2 = list2.get(i);
                 HashMap<String, String> extra2 = fto2.getExtra();
                 String value2 = (String) extra2.get(field);

                 // At this point we are going to assume the order is not an issue.
                 // We should probably add an index field to the extra array so this
                 // is not an issue.
                 vector1[i] = Double.parseDouble(value1);
                 vector2[i] = Double.parseDouble(value2);
             }
          }
       } else if (source.equalsIgnoreCase(VECTOR_DATA_COLLECTION)) {
          // Produce the vectors for each collection. For each collection we want to grab 
          // the extra array
          HashMap<String, String> extra1 = collection1.getExtra();
          HashMap<String, String> extra2 = collection2.getExtra();
          if (extra1.size() != extra2.size()) {
             // Some kind of error.  In theory we could also throw an exception here.
             similarity = -1.;
          } else {
             if (field.equalsIgnoreCase(VECTOR_DATA_ALL)) {
                vector1 = new double[extra1.size()];   
                vector2 = new double[extra2.size()];   
 
                // At this point we are going to assume the order is not an issue.
                // This is a reasonable assumption because the keys should all be the same
                // which means the key hash values should be the same and the values will be
                // returned in order of the hash.
                int i = 0;
                for (String value1 : extra1.values()) {
                    vector1[i] = Double.parseDouble(value1);
                    System.out.println("vector1 of " + i + " is " + vector1[i]);
                    i++;
                }
                i = 0;
                for (String value2 : extra2.values()) {
                    vector2[i] = Double.parseDouble(value2);
                    System.out.println("vector2 of " + i + " is " + vector2[i]);
                    i++;
                }
             } else {
                // The user just wants one field.  Maybe this could eventually be comma separated list..
                vector1 = new double[1];   
                vector2 = new double[1];   
 
                String value1 = (String) extra1.get(field);
                String value2 = (String) extra2.get(field);
                vector1[0] = Double.parseDouble(value1);
                vector2[0] = Double.parseDouble(value2);
             }
          }
       } else {
          throw new IllegalArgumentException("unsupported source argument: " + source);
       }
       return (computeSimilarity(vector1, vector2));
    }

    /**
     * @param vector1 vector of metadata from the first transfer object.
     * @param vector2 vector of metadata from the second transfer object.
     * @return the calculated similarity value
     */
   abstract double computeSimilarity(double[] vector1, double[] vector2);
}

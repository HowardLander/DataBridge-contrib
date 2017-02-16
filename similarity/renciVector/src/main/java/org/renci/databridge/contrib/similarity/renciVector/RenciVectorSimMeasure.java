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
import com.rabbitmq.client.*;

/**
 *
 * @author Lander
 */
public abstract class RenciVectorSimMeasure implements SimilarityProcessor {

   /**
    * This code implements a cosine similarity for the schizConnect SAPS data. 
    * To do this, it retrieves all the "files" for each collection.  Each of these 
    * represents a single question in the SAPS assessment for a subject.  We are assuming the same
    * number of files for each collection, though that may have to eventually be relaxed in this
    * or another similarity processor.  For each collection, we build a vector of assessment values
    * then perform a cosine similarity.
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

              // NOTE: We may want to do something more sophisticated with the params..
              FileTransferObject fto1 = list1.get(i);
              HashMap<String, String> extra1 = fto1.getExtra();
              String value1 = (String) extra1.get(params);

              FileTransferObject fto2 = list2.get(i);
              HashMap<String, String> extra2 = fto2.getExtra();
              String value2 = (String) extra2.get(params);

              // At this point we are going to assume the order is not an issue.
              // We should probably add an index field to the extra array so this
              // is not an issue.
              vector1[i] = Double.parseDouble(value1);
              vector2[i] = Double.parseDouble(value2);
          }
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

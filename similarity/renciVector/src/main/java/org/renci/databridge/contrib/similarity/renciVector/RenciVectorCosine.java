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
public class RenciVectorCosine extends RenciVectorSimMeasure {

   /**
    * This code performs a classic cosine similarity on 2 vectors, The code came
    * originally from 
    * http://stackoverflow.com/questions/520241/how-do-i-calculate-the-cosine-similarity-of-two-vectors
    *
    * Note this only works with vectors of equal length. We are going to depend on the code 
    * calling this to ensure that.
    * @param vector1 The first vector
    * @param vector2 The second vector
    * @return The similarity calculated between the 2 vectors.
    */
   double computeSimilarity(double[] vector1, double[] vector2) {
       double dotProduct = 0.0;
       double normA = 0.0;
       double normB = 0.0;
       for (int i = 0; i < vector1.length; i++) {
           dotProduct += vector1[i] * vector2[i];
           normA += Math.pow(vector1[i], 2);
           normB += Math.pow(vector2[i], 2);
       }   
       return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
   }
}

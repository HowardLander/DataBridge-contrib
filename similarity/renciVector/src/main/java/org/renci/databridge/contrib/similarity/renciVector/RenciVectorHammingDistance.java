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
public class RenciVectorHammingDistance extends RenciVectorSimMeasure {

   /**
    * This code performs a Hamming Distance calculation. We are going to return a similarity by
    * caluculating 1 - (Hamming Distance / length of vector). For a description of the Hamming
    * distance see
    * 
    *  https://en.wikipedia.org/wiki/Hamming_distance
    *
    * Note this only works with vectors of equal length. We are going to depend on the code 
    * calling this to ensure that.
    * @param vector1 The first vector
    * @param vector2 The second vector
    * @return The similarity calculated between the 2 vectors.
    */
   double computeSimilarity(double[] vector1, double[] vector2) {

       // We need doubles for these so the math is done correctly. Else we only
       // return 1 or 0.
       double nDifferences = 0.;
       double lengthOfVector = (double) vector1.length;

       for (int i = 0; i < vector1.length; i++) {
          if (vector1[i] != vector2[i]) {
             nDifferences ++;
          }
       }   
       return (1. - (nDifferences/lengthOfVector));
   }
}

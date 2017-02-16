package org.renci.databridge.contrib.similarity.renciVector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import static java.lang.Math.*;
import org.renci.databridge.persistence.metadata.*;
import com.rabbitmq.client.*;

/**
 *
 * @author Lander
 */
public class RenciVectorNormalizedHammingDistance extends RenciVectorSimMeasure {

   /**
    * This code performs a calculation similar to the Hamming distance. In this case
    * we are going to sum up all of the differences and sum up each vector. Than we 
    * are going to divide the difference by the larger of the two vector sums. Then we
    * subtract this from 1.  I couldn't find a similar algorithm on the web, but surely
    * someone else has tried this.
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
       double sumOfDifferences = 0.;
       double lengthOfVector = (double) vector1.length;
       double sumOfVector1 = 0.;
       double sumOfVector2 = 0.;

       for (int i = 0; i < vector1.length; i++) {
          sumOfDifferences += abs(vector1[i] - vector2[i]);
          sumOfVector1 += vector1[i];
          sumOfVector2 += vector2[i];
       }   
       
       System.out.println("Sum of difference: " + sumOfDifferences);
       double divisor = (sumOfVector1 > sumOfVector2) ? sumOfVector1 : sumOfVector2;
       System.out.println("Divisor: " + divisor);
       return (1. - (sumOfDifferences/divisor));
   }
}

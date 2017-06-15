package org.renci.databridge.contrib.similarity.renciVector;

import java.io.*;
import java.util.*;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.matchers.JUnitMatchers;
import org.junit.Rule;

import org.renci.databridge.persistence.metadata.*;
import org.renci.databridge.util.*;

public class RenciVectorTest {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }
  
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void TestRenciVector () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testRenciVector");
     String params = RenciVectorSimMeasure.VECTOR_DATA_FILE + ":question_value";
     String newLine = System.getProperty("line.separator");

     try {
         CollectionTransferObject collection0 = new CollectionTransferObject();
         ArrayList<FileTransferObject> files0 = new ArrayList<FileTransferObject>();
         FileTransferObject fto0 = new FileTransferObject();
         HashMap<String, String> extra0 = new HashMap<String, String>();
         extra0.put("question_value", "0.0001");
         fto0.setExtra(extra0);

         // Add 4 zeros
         files0.add(fto0);
         files0.add(fto0);
         files0.add(fto0);
         files0.add(fto0);
         collection0.setFileList(files0);

         CollectionTransferObject collection1 = new CollectionTransferObject();
         ArrayList<FileTransferObject> files1 = new ArrayList<FileTransferObject>();
         FileTransferObject fto1 = new FileTransferObject();
         HashMap<String, String> extra1 = new HashMap<String, String>();
         extra1.put("question_value", "1");
         fto1.setExtra(extra1);

         // Add 4 ones
         files1.add(fto1);
         files1.add(fto1);
         files1.add(fto1);
         files1.add(fto1);
         collection1.setFileList(files1);

         CollectionTransferObject collection2 = new CollectionTransferObject();
         ArrayList<FileTransferObject> files2 = new ArrayList<FileTransferObject>();
         FileTransferObject fto2 = new FileTransferObject();
         HashMap<String, String> extra2 = new HashMap<String, String>();
         extra2.put("question_value", "2");
         fto2.setExtra(extra2);

         // Add something mixed
         files2.add(fto0);
         files2.add(fto1);
         files2.add(fto1);
         files2.add(fto2);
         collection2.setFileList(files2);

         RenciVectorCosine theCosine = new RenciVectorCosine();
         double result;
         System.out.println(System.lineSeparator() + "Starting Cosine tests");
         result = theCosine.compareCollections(collection0, collection0, params);
         System.out.println("Similarity of collection0 vs collection0: " + result);
         TestCase.assertTrue("collection0 vs collection0 failed", result == 1.);

         result = theCosine.compareCollections(collection0, collection1, params);
         System.out.println("Similarity of collection0 vs collection1: " + result);
         TestCase.assertTrue("collection0 vs collection1 failed", result == 1.);

         System.out.println(System.lineSeparator() + "Starting Hammings tests");
         RenciVectorHammingDistance theHammingDistance = new RenciVectorHammingDistance();
         result = theHammingDistance.compareCollections(collection0, collection0, params);
         System.out.println("Similarity of collection0 vs collection0: " + result);
         TestCase.assertTrue("Hamming collection0 vs collection0 failed", result == 1.);

         result = theHammingDistance.compareCollections(collection0, collection1, params);
         System.out.println("Similarity of collection0 vs collection1: " + result);
         TestCase.assertTrue("Hamming collection0 vs collection1 failed", result == 0.);

         result = theHammingDistance.compareCollections(collection0, collection2, params);
         System.out.println("Similarity of collection0 vs collection2: " + result);
         TestCase.assertTrue("Hamming collection0 vs collection2 failed", result == 0.25);

         result = theHammingDistance.compareCollections(collection1, collection2, params);
         System.out.println("Similarity of collection1 vs collection2: " + result);

         System.out.println(System.lineSeparator() + "Starting Normalized Hammings tests");
         RenciVectorNormalizedHammingDistance theNormalizedHammingDistance = 
             new RenciVectorNormalizedHammingDistance();
         result = theNormalizedHammingDistance.compareCollections(collection0, collection0, params);
         System.out.println("Normalized similarity of collection0 vs collection0: " + result + 
             System.lineSeparator());
         TestCase.assertTrue("Hamming collection0 vs collection0 failed", result == 1.);

         result = theNormalizedHammingDistance.compareCollections(collection0, collection1, params);
         System.out.println("Normalized similarity of collection0 vs collection1: " + result + 
             System.lineSeparator());
         TestCase.assertTrue("Hamming collection0 vs collection1 failed", result <= 0.001);

         result = theNormalizedHammingDistance.compareCollections(collection0, collection2, params);
         System.out.println("Normalized similarity of collection0 vs collection2: " + result +
            System.lineSeparator());

         result = theNormalizedHammingDistance.compareCollections(collection1, collection2, params);
         System.out.println("Normalized similarity of collection1 vs collection2: " + result +
            System.lineSeparator());
     }  catch (Exception e) {
         e.printStackTrace();
     }
  }

  @Test
  public void TestRenciVectorCollection () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testRenciVectorCollection");
     String params = RenciVectorSimMeasure.VECTOR_DATA_COLLECTION + ":all";
     String newLine = System.getProperty("line.separator");

     try {
         CollectionTransferObject collection0 = new CollectionTransferObject();
         HashMap<String, String> extra0 = new HashMap<String, String>();
         extra0.put("demographics", "1");
         extra0.put("extra_pyramidal", "1");
         extra0.put("positive_symptoms", "1");
         extra0.put("negative_symptoms", "1");
         extra0.put("mood", "1");

         collection0.setExtra(extra0);

         CollectionTransferObject collection1 = new CollectionTransferObject();
         HashMap<String, String> extra1 = new HashMap<String, String>();

         extra1.put("demographics", "0");
         extra1.put("extra_pyramidal", "0");
         extra1.put("positive_symptoms", "0");
         extra1.put("negative_symptoms", "0");
         extra1.put("mood", "0");
         collection1.setExtra(extra1);

         CollectionTransferObject collection2 = new CollectionTransferObject();
         HashMap<String, String> extra2 = new HashMap<String, String>();

         // Add something mixed
         extra2.put("demographics", "1");
         extra2.put("extra_pyramidal", "0");
         extra2.put("positive_symptoms", "1");
         extra2.put("negative_symptoms", "0");
         extra2.put("mood", "1");
         collection2.setExtra(extra2);

         RenciVectorCosine theCosine = new RenciVectorCosine();
         double result;
         System.out.println(System.lineSeparator() + "Starting Cosine tests");
         result = theCosine.compareCollections(collection0, collection0, params);
         System.out.println("Similarity of collection0 vs collection0: " + result);
         TestCase.assertTrue("collection0 vs collection0 failed", result > .99);

         result = theCosine.compareCollections(collection0, collection1, params);
         System.out.println("Similarity of collection0 vs collection1: " + result);
         boolean nan = Double.isNaN(result);
         TestCase.assertTrue("collection0 vs collection1 failed", nan == true);

         System.out.println(System.lineSeparator() + "Starting Hammings tests");
         RenciVectorHammingDistance theHammingDistance = new RenciVectorHammingDistance();
         result = theHammingDistance.compareCollections(collection0, collection0, params);
         System.out.println("Similarity of collection0 vs collection0: " + result);
         TestCase.assertTrue("Hamming collection0 vs collection0 failed", result > .99);

         result = theHammingDistance.compareCollections(collection0, collection1, params);
         System.out.println("Similarity of collection0 vs collection1: " + result);
         TestCase.assertTrue("Hamming collection0 vs collection1 failed", result == 0.);

         result = theHammingDistance.compareCollections(collection0, collection2, params);
         System.out.println("Similarity of collection0 vs collection2: " + result);
         TestCase.assertTrue("Hamming collection0 vs collection2 failed", result < 1.);

         result = theHammingDistance.compareCollections(collection1, collection2, params);
         System.out.println("Similarity of collection1 vs collection2: " + result);

         System.out.println(System.lineSeparator() + "Starting Normalized Hammings tests");
         RenciVectorNormalizedHammingDistance theNormalizedHammingDistance = 
             new RenciVectorNormalizedHammingDistance();
         result = theNormalizedHammingDistance.compareCollections(collection0, collection0, params);
         System.out.println("Normalized similarity of collection0 vs collection0: " + result + 
             System.lineSeparator());
         TestCase.assertTrue("Hamming collection0 vs collection0 failed", result == 1.);

         result = theNormalizedHammingDistance.compareCollections(collection0, collection1, params);
         System.out.println("Normalized similarity of collection0 vs collection1: " + result + 
             System.lineSeparator());
         TestCase.assertTrue("Hamming collection0 vs collection1 failed", result <= 0.001);

         result = theNormalizedHammingDistance.compareCollections(collection0, collection2, params);
         System.out.println("Normalized similarity of collection0 vs collection2: " + result +
            System.lineSeparator());

         result = theNormalizedHammingDistance.compareCollections(collection1, collection2, params);
         System.out.println("Normalized similarity of collection1 vs collection2: " + result +
            System.lineSeparator());
     }  catch (Exception e) {
         e.printStackTrace();
     }
  }
  @Rule
  public ExpectedException thrown = ExpectedException.none();

}

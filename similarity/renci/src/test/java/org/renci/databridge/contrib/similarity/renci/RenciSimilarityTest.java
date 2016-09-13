package org.renci.databridge.contrib.similarity.renci;

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
import org.renci.databridge.contrib.similarity.ncat.*;

public class RenciSimilarityTest {
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
  public void testRenciCompare () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testRenciCompare");
     boolean result;
     String params = "keywords|studyDesign|studyType";
     String params2 = "currentPrimaryOutcomeMeasures";
     String params3 = "studyType";

     MetadataDAOFactory theFactory =
        MetadataDAOFactory.getMetadataDAOFactory(MetadataDAOFactory.MONGODB, "install-test", "localhost", 27017);

     try {
         CollectionTransferObject collection1 = null;
         CollectionTransferObject collection2 = null;

         CollectionDAO theCollectionDAO = theFactory.getCollectionDAO();
         HashMap<String, String> searchMap = new HashMap<String, String>();
         searchMap.put("nameSpace", "clinicalTrials");

         System.out.println("testing the Similarity method");
         Iterator<CollectionTransferObject> collectionIterator = theCollectionDAO.getCollections(searchMap);
         if (collectionIterator.hasNext()) {
             collection1 = collectionIterator.next();
         } else {
             System.out.println("Couldn't find first collection");
         }

         if (collectionIterator.hasNext()) {
             collection2 = collectionIterator.next();
         } else {
             System.out.println("Couldn't find second collection");
         }

         TestCase.assertTrue("collection2 is null", collection2 != null);
         RenciLevenshtein  theLev = new RenciLevenshtein();
         RenciCosine  theCosine = new RenciCosine();
         RenciOverlap theRenciOverlap = new RenciOverlap();
         RenciGeneralizedOverlap theGenRenciOverlap = new RenciGeneralizedOverlap();
         RenciBlockDistance theBlock = new RenciBlockDistance();
         Overlap theNcatOverlap = new Overlap();
         Eskin   theEskin = new Eskin();
         double similarity = theLev.compareCollections(collection1, collection2, params);
         double similarityCos = theCosine.compareCollections(collection1, collection2, params);
         double similarityRenciOverlap = theRenciOverlap.compareCollections(collection1, collection2, params);
         double similarityGenRenciOverlap = theGenRenciOverlap.compareCollections(collection1, collection2, params);
         double similarityNcatOverlap = theNcatOverlap.compareCollections(collection1, collection2, params);
         double similarityBlock = theBlock.compareCollections(collection1, collection2, params);
         System.out.println("levenshtein distance is " + similarity);
         System.out.println("cosine similarity is " + similarityCos);
         System.out.println("renci overlap similarity is " + similarityRenciOverlap);
         System.out.println("renci generalized overlap similarity is " + similarityGenRenciOverlap);
         System.out.println("renci block distance similarity is " + similarityBlock);
         System.out.println("NCat overlap similarity is " + similarityNcatOverlap);

         double similarity2 = theLev.compareCollections(collection1, collection2, params2);
         double similarityCos2 = theCosine.compareCollections(collection1, collection2, params2);
         System.out.println("second levenshtein distance is " + similarity2);
         System.out.println("second cosine similarity is " + similarityCos2);

         double similarity3 = theLev.compareCollections(collection1, collection2, params3);
         double similarityCos3 = theCosine.compareCollections(collection1, collection2, params3);
         System.out.println("third levenshtein distance is " + similarity3);
         System.out.println("third cosine similarity is " + similarityCos3);

     }  catch (Exception e) {
         e.printStackTrace();
     }
  }

  public void runTestSet(ArrayList<RenciSimMeasure> theMeasures, 
                         ArrayList<String> theMeasureNames,
                         CollectionTransferObject cto1,
                         CollectionTransferObject cto2,
                         String params) {
     for (int i = 0; i < theMeasures.size(); i++) {
         Double similarity = theMeasures.get(i).compareCollections(cto1, cto2, params);
         System.out.println(theMeasureNames.get(i) + " " + similarity);
     }
  }

  @Test
  public void testRenciCompareWithTestData () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testRenciCompareWithTestData");
     boolean result;
     String params = "description";
     String newLine = System.getProperty("line.separator");

     try {
         CollectionTransferObject collection1 = new CollectionTransferObject();
         CollectionTransferObject collection2 = new CollectionTransferObject();
         CollectionTransferObject collection3 = new CollectionTransferObject();
         CollectionTransferObject collection4 = new CollectionTransferObject();

         collection1.setDescription("one two three four five six seven eight nine ten");
         collection2.setDescription("one two three four five one two three four five");
         collection3.setDescription("one two three four five");
         collection4.setDescription("one two three four five eleven twelve thirteen fourteen fifteen");

         ArrayList<RenciSimMeasure> theMeasures = new ArrayList<RenciSimMeasure>();
         ArrayList<String> theMeasureNames = new ArrayList<String>();

         theMeasures.add(new RenciBlockDistance());
         theMeasureNames.add("Block Distance         ");
         theMeasures.add(new RenciCosine());
         theMeasureNames.add("Cosine                 ");
         theMeasures.add(new RenciDamerauLevenshtein());
         theMeasureNames.add("Damerau Levenshtein    ");
         theMeasures.add(new RenciDice());
         theMeasureNames.add("Dice                   ");
         theMeasures.add(new RenciEuclideanDistance());
         theMeasureNames.add("Euclidean Distance     ");
         theMeasures.add(new RenciGeneralizedJaccard());
         theMeasureNames.add("Generalized Jaccard    ");
         theMeasures.add(new RenciGeneralizedOverlap());
         theMeasureNames.add("Generalize Overlap     ");
         theMeasures.add(new RenciJaccard());
         theMeasureNames.add("Jaccard                ");
         theMeasures.add(new RenciJaro());
         theMeasureNames.add("Jaro                   ");
         theMeasures.add(new RenciJaroWinkler());
         theMeasureNames.add("Jaro Winkler           ");
         theMeasures.add(new RenciLevenshtein());
         theMeasureNames.add("Levenshtein            ");
         theMeasures.add(new RenciMongeElkan());
         theMeasureNames.add("Monge Elkin            ");
         theMeasures.add(new RenciNeedlemanWunch());
         theMeasureNames.add("Needleman Wunch        ");
         theMeasures.add(new RenciOverlap());
         theMeasureNames.add("Overlap                ");
         theMeasures.add(new RenciSimonWhite());
         theMeasureNames.add("Simon White            ");
         theMeasures.add(new RenciSmithWaterman());
         theMeasureNames.add("Smith Waterman         ");
         theMeasures.add(new RenciSmithWatermanGotoh());
         theMeasureNames.add("Smith Waterman Gotoh   ");
         theMeasures.add(new RenciTanimotoCoefficient());
         theMeasureNames.add("Tanimoto Coefficient   ");
        
         System.out.println("comparing \"" + ANSI_BLUE + collection1.getDescription() + ANSI_RESET + "\""); 
         System.out.println("to        \"" + ANSI_BLUE + collection2.getDescription() + ANSI_RESET + "\"");
         runTestSet(theMeasures, theMeasureNames, collection1, collection2, params);
         System.out.println(newLine);

         System.out.println("comparing \"" + ANSI_BLUE + collection1.getDescription() + ANSI_RESET + "\""); 
         System.out.println("to        \"" + ANSI_BLUE + collection3.getDescription() + ANSI_RESET + "\"");
         runTestSet(theMeasures, theMeasureNames, collection1, collection3, params);
         System.out.println(newLine);

         System.out.println("comparing \"" + ANSI_BLUE + collection1.getDescription() + ANSI_RESET + "\""); 
         System.out.println("to        \"" + ANSI_BLUE + collection4.getDescription() + ANSI_RESET + "\"");
         runTestSet(theMeasures, theMeasureNames, collection1, collection4, params);
         System.out.println(newLine);

     }  catch (Exception e) {
         e.printStackTrace();
     }
  }
  @Rule
  public ExpectedException thrown = ExpectedException.none();

}

package org.renci.databridgecontrib.similarity.mocksimilarity;

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

public class MockSimilarityTest {


  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }
  
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void testMockCompare () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testMockCompare");
     boolean result;

     CollectionTransferObject collection1 = new CollectionTransferObject();
     collection1.setURL("http://www.renci.org");
     collection1.setTitle("title");
     collection1.setDescription("here's an example description");
     collection1.setProducer("producer");
     collection1.setSubject("physics");
     collection1.setNameSpace("test");
     collection1.setVersion(1);
     ArrayList<String> keywords = new ArrayList<String>();

     keywords.add("Keyword1");
     keywords.add("Keyword2");
     keywords.add("Keyword3");
     collection1.setKeywords(keywords);

     HashMap<String, String> extra = new HashMap<String, String>();
     extra.put("author", "Howard Lander");
     extra.put("reason", "Testing the code");
     collection1.setExtra(extra);

     CollectionTransferObject collection2 = new CollectionTransferObject();
     collection2.setURL("http://www.renci.org");
     collection2.setTitle("title");
     collection2.setDescription("here's an example description");
     collection2.setProducer("producer");
     collection2.setSubject("physics");
     collection2.setNameSpace("test");
     collection2.setVersion(1);
     keywords = new ArrayList<String>();

     keywords.add("Keyword1");
     keywords.add("Keyword2");
     keywords.add("Keyword3");
     collection2.setKeywords(keywords);

     extra = new HashMap<String, String>();
     extra.put("author", "Howard Lander");
     extra.put("reason", "Testing the code");
     collection2.setExtra(extra);

     try {
         System.out.println("testing the MockSimilarity method");
         double similarity;
         MockSimilarity theSim = new MockSimilarity();
         similarity = theSim.compareCollections(collection1, collection2);
         System.out.println("returned similarity is " + similarity);

         TestCase.assertTrue("Error in similarity method", similarity <= 1.0);
     }  catch (Exception e) {
         e.printStackTrace();
     }
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

}

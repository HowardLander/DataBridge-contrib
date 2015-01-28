package org.renci.databridge.contrib.sna.mocksna;

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
import org.renci.databridge.persistence.network.*;
import org.renci.databridge.util.*;

public class MockSNATest {


  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void testMockSNA () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testMockSNA");
     boolean result;

     try {
         System.out.println("testing the MockSNA method");
         // Note that this will not work if the data isn't in the database.

         ArrayList<NetworkDyadTransferObject> testDyads = new ArrayList<NetworkDyadTransferObject>();
         for (int i = 0; i < 6; i++) {
             NetworkDyadTransferObject thisDyad = new NetworkDyadTransferObject();
             thisDyad.setNode1DataStoreId(Integer.toString(i % 6));
             thisDyad.setNode2DataStoreId(Integer.toString((i +1 ) % 5));
             System.out.println("Ids: " + thisDyad.getNode1DataStoreId() + " " + thisDyad.getNode2DataStoreId());
             testDyads.add(thisDyad);
         }
         MockSNA theMockSNA = new MockSNA();
         HashMap<String, String[]> theClusters = theMockSNA.processNetwork(testDyads.iterator(), null);

        System.out.println("number of clusters: " + theClusters.size());
        for (Map.Entry<String, String[]> entry : theClusters.entrySet()) {
            System.out.println("cluster id: " + entry.getKey());
            String theNodes[] = entry.getValue();
            for (int i = 0; i < theNodes.length; i++) {
                System.out.println("node id: " + i + " " + theNodes[i]);
            }
        }
     }  catch (Exception e) {
         e.printStackTrace();
     }
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

}

package org.renci.databridge.contrib.signature.schizConnect;

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
import org.apache.jena.rdf.model.ModelFactory.*;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.*;

public class SchizConnectSignatureTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }
  
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Test
  public void testSchizConnectSignature () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testSchizConnectSignature");

     try {
         // Create a cto for input and a pointer for the function return
         // As it happens, we don't actually need to fill out anything
         // but the file list in the inputCTO
         CollectionTransferObject inputCTO = new CollectionTransferObject();
         CollectionTransferObject returnCTO = null;
         ArrayList<FileTransferObject> instrumentList = new ArrayList<FileTransferObject>();
         FileTransferObject ftoToAdd = null;
         
         String URL = "http://maven.renci.org/ontologies/databridgeforneuroscience.owl";
         String Prefix = "http://maven.renci.org/ontologies/databridgeforneuroscience";
         String Root = "Clinical";

         // Let's add a couple of FTOs
         ftoToAdd = new FileTransferObject();
         ftoToAdd.setName("Socioeconomic_Status");
         instrumentList.add(ftoToAdd);

         FileTransferObject ftoToAdd2 = new FileTransferObject();
         ftoToAdd2.setName("SCIDNP");
         instrumentList.add(ftoToAdd2);

         FileTransferObject ftoToAdd3 = new FileTransferObject();
         ftoToAdd3.setName("FTND");
         instrumentList.add(ftoToAdd3);

         // This one should not be found, so nothing like it should appear in the result
         FileTransferObject ftoToAdd4 = new FileTransferObject();
         ftoToAdd4.setName("UNKNOWN");
         instrumentList.add(ftoToAdd4);

         inputCTO.setFileList(instrumentList);
         
         String params = 
       SchizConnectClinical.ONTOLOGY_URL + SchizConnectClinical.SEP1 + URL + SchizConnectClinical.SEP2 +
       SchizConnectClinical.ONTOLOGY_PREFIX + SchizConnectClinical.SEP1 + Prefix + SchizConnectClinical.SEP2 +
       SchizConnectClinical.ONTOLOGY_ROOT + SchizConnectClinical.SEP1 + Root;
 
         System.out.println("params: " + params);
                        
         SchizConnectClinical theProcessor = new SchizConnectClinical();
         returnCTO = theProcessor.extractSignature(inputCTO, params);
         TestCase.assertTrue("returnCTO is null", returnCTO != null);
         HashMap <String, String> instrumentMap = returnCTO.getExtra();
         TestCase.assertTrue("instrumentMap is null", instrumentMap != null);
         int nFound = 0;
         for (String thisInstrument:instrumentMap.keySet()) {
            System.out.println(thisInstrument + " " + instrumentMap.get(thisInstrument));
            if (instrumentMap.get(thisInstrument).equals("1")) {
               nFound ++;
            }
         }

         TestCase.assertTrue("nFound != 3", nFound == 3);
     }  catch (Exception e) {
         e.printStackTrace();
     }
  }

  @Test
  public void testFindSecondDegree () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testFindSecondDegree");

     try {
         SchizConnectClinical theClinical = new SchizConnectClinical();
         String ontologyURL = "http://maven.renci.org/ontologies/databridgeforneuroscience.owl";
         String prefix= "http://maven.renci.org/ontologies/databridgeforneuroscience";
         String DBfN= "DBfN";
         String root = "Clinical";

         OntClass theClass = theClinical.findSecondDegree(ontologyURL, prefix, root, "BIF");
         System.out.println("theClass label is: " + theClass.getLabel(null));
         TestCase.assertTrue("couldn't find second degree of BIF", 
                             theClass.getLabel(null).equalsIgnoreCase("Demographics") == true);

         theClass = theClinical.findSecondDegree(ontologyURL, prefix, root, "AIMS");
         System.out.println("theClass label is: " + theClass.getLabel(null));
         TestCase.assertTrue("couldn't find second degree of AIMS", 
                             theClass.getLabel(null).equalsIgnoreCase("Extrapyramidal_Symptoms") == true);

         // This should return null because "AIM" is not in the ontology
         theClass = theClinical.findSecondDegree(ontologyURL, prefix, root, "AIM");
         TestCase.assertTrue("function should return null",  theClass == null);

     }  catch (Exception e) {
         e.printStackTrace();
     }
  }

  @Test
  public void testcreateSubjectHashMap () throws Exception {

     System.out.println("");
     System.out.println("");
     System.out.println("beginning testcreateSubjectHashMap");

     try {
         SchizConnectClinical theClinical = new SchizConnectClinical();
         String ontologyURL = "http://maven.renci.org/ontologies/databridgeforneuroscience.owl";
         String prefix= "http://maven.renci.org/ontologies/databridgeforneuroscience";
         String DBfN= "DBfN";
         String root = "Clinical";

         HashMap <String, String> returnMap = theClinical.createSubjectHashMap(ontologyURL, prefix, root);
         Iterator it = returnMap.entrySet().iterator();
         int size = returnMap.size();
         System.out.println("num entries in hash map: " + size);
         while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
         }
  
         TestCase.assertTrue("Wrong number of entries in hashmap: should be 20: is: " + size, 
                             size  == 20);

     }  catch (Exception e) {
         e.printStackTrace();
     }
  }



  @Rule
  public ExpectedException thrown = ExpectedException.none();

}

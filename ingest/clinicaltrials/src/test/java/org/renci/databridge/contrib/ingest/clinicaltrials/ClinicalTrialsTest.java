package org.renci.databridge.contrib.ingest.clinicaltrials;

import org.renci.databridge.contrib.ingest.clinicaltrials.*;
import org.renci.databridge.persistence.metadata.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.matchers.JUnitMatchers;
import org.junit.Rule;
import org.la4j.*;
import java.io.*;
import com.google.gson.*;
import com.google.gson.annotations.*;

public class ClinicalTrialsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testIngestFileToJson() {
        String fileName = "processed-clinicalTrials.9999";
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            ClinicalTrialJson theObject = ClinicalTrialJson.readJsonFromFile(filePath);
            String theURL = theObject.getSourceURL();
            System.out.println("read Url: " + theURL);
            TestCase.assertTrue("could not read data", theURL != null);

            System.out.println("testing basic serialization");
            byte[] theBytes = ClinicalTrialJson.serialize(theObject);
            ClinicalTrialJson theReturnedObject = ClinicalTrialJson.deserialize(theBytes);
            System.out.println("theReturnedURL: " + theReturnedObject.getSourceURL());
            TestCase.assertTrue("serialize/deserialize loop failed", 
                                 theURL.compareTo(theReturnedObject.getSourceURL()) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testArraySerialization() {
        String fileName = "processed-clinicalTrials.9999"; //NCT00867139
        String fileName2 = "processed-clinicalTrials.9998"; //NCT00867035
        ArrayList<ClinicalTrialJson> trialList = new ArrayList<ClinicalTrialJson>();
        ArrayList<ClinicalTrialJson> returnedList = new ArrayList<ClinicalTrialJson>();
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            ClinicalTrialJson theObject = ClinicalTrialJson.readJsonFromFile(filePath);
            trialList.add(theObject);

            String filePath2 = classLoader.getResource(fileName2).getFile();
            ClinicalTrialJson theObject2 = ClinicalTrialJson.readJsonFromFile(filePath2);
            String theURL = theObject2.getSourceURL();
            trialList.add(theObject2);

            System.out.println("testing ArrayList serialization");
            byte[] theBytes = ClinicalTrialJson.serializeArrayList(trialList);
            returnedList = ClinicalTrialJson.deserializeArrayList(theBytes);
            ClinicalTrialJson returnedObject = returnedList.get(1);
            System.out.println("theURL: " + theURL);
            System.out.println("theReturnedURL: " + returnedObject.getSourceURL());
            TestCase.assertTrue("serialize/deserialize loop failed", 
                                theURL.compareTo(returnedObject.getSourceURL()) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testGetBytes() {
        String fileName = "processed-clinicalTrials.9999"; //NCT00867139
        String fileName2 = "processed-clinicalTrials.9998"; //NCT00867035
        ArrayList<ClinicalTrialJson> trialList = new ArrayList<ClinicalTrialJson>();
        ArrayList<ClinicalTrialJson> returnedList = new ArrayList<ClinicalTrialJson>();
        ArrayList<ClinicalTrialJson> fromGetList = new ArrayList<ClinicalTrialJson>();

        Logger logger = Logger.getLogger("org.renci.databridge.contrib.ingest.clinicaltrials");
        ClinicalTrialFormatter mf = new ClinicalTrialFormatter();
        mf.setLogger(logger);
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            ClinicalTrialJson theObject = ClinicalTrialJson.readJsonFromFile(filePath);
            trialList.add(theObject);

            String filePath2 = classLoader.getResource(fileName2).getFile();
            ClinicalTrialJson theObject2 = ClinicalTrialJson.readJsonFromFile(filePath2);
            String theURL = theObject2.getSourceURL();
            trialList.add(theObject2);
            byte[] theBytes = ClinicalTrialJson.serializeArrayList(trialList);
            returnedList = ClinicalTrialJson.deserializeArrayList(theBytes);
            ClinicalTrialJson returnedObject = returnedList.get(1);
            String theReturnedURL = returnedObject.getSourceURL();

            // Now try using getBytes
            System.out.println("testing getBytes");
            File resourcesDirectory = new File("src/test/resources");
            byte[] fromGetBytes = mf.getBytes(resourcesDirectory.getAbsolutePath());
            // Make sure what we got from getBytes matches what we got from reading directlu
            fromGetList = ClinicalTrialJson.deserializeArrayList(fromGetBytes);
            ClinicalTrialJson returnedFromGetBytes = fromGetList.get(1);
            String getBytesURL = returnedFromGetBytes.getSourceURL();

            System.out.println("theReturnedURL: " + theReturnedURL);
            System.out.println("getBytesURL: " + getBytesURL);
            TestCase.assertTrue("getBytesFailed",
                                theReturnedURL.compareTo(getBytesURL) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testClinicalTrialFormatter() {
        String fileName = "processed-clinicalTrials.9999"; //NCT00867139
        String fileName2 = "processed-clinicalTrials.9998"; //NCT00867035
        ArrayList<ClinicalTrialJson> trialList = new ArrayList<ClinicalTrialJson>();
        ArrayList<ClinicalTrialJson> returnedList = new ArrayList<ClinicalTrialJson>();
        Logger logger = Logger.getLogger("org.renci.databridge.contrib.ingest.clinicaltrials");
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            ClinicalTrialJson theObject = ClinicalTrialJson.readJsonFromFile(filePath);
            trialList.add(theObject);

            String filePath2 = classLoader.getResource(fileName2).getFile();
            ClinicalTrialJson theObject2 = ClinicalTrialJson.readJsonFromFile(filePath2);
            String theURL = theObject2.getSourceURL();
            trialList.add(theObject2);

            System.out.println("testing formatting code");
            byte[] theBytes = ClinicalTrialJson.serializeArrayList(trialList);

            List<MetadataObject> metadataObjects = null;
            ClinicalTrialFormatter mf = new ClinicalTrialFormatter();
            mf.setLogger(logger);
            metadataObjects = mf.format (theBytes);
            MetadataObject thisMeta = (MetadataObject) metadataObjects.get(1);
            System.out.println("thisMeta: " + thisMeta.getCollectionTransferObject());
            TestCase.assertTrue("format failed",
                                theURL.compareTo(thisMeta.getCollectionTransferObject().getURL()) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}


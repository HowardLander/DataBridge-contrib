package org.renci.databridge.contrib.ingest.schizConnect;

import org.renci.databridge.contrib.ingest.schizConnect.*;
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

public class SchizConnectClinicalMetadataTest {

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
        String fileName = "schizConnect-clinical-metadata-12-22-2016.json";
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            ArrayList<SchizConnectClinicalMetadataJson> theObjectList = SchizConnectClinicalMetadataJson.readJsonArrayFromFile(filePath);
            String theStudy = theObjectList.get(0).getStudy();
            System.out.println("read Study: " + theStudy);
            ArrayList<String> instrumentList = theObjectList.get(0).getInstrumentList();
            System.out.println("read Instrument 2: " + instrumentList.get(2));
            SchizConnectClinicalMetadataJson[] theObjectArray = 
              (SchizConnectClinicalMetadataJson[]) theObjectList.toArray(new SchizConnectClinicalMetadataJson[0]);
            TestCase.assertTrue("could not read data", theStudy != null);

            System.out.println("testing basic serialization");
            byte[] theBytes = SchizConnectClinicalMetadataJson.serialize(theObjectArray);
            SchizConnectClinicalMetadataJson[] theReturnedObjectList = null;
            theReturnedObjectList = SchizConnectClinicalMetadataJson.deserialize(theBytes);
            System.out.println("theReturnedStudy: " + theReturnedObjectList[0].getStudy());
            TestCase.assertTrue("serialize/deserialize loop failed", 
                                 theStudy.compareTo(theReturnedObjectList[0].getStudy()) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    @Test
    public void testArraySerialization() {
        ArrayList<SchizConnectClinicalMetadataJson> returnedList = null;
        String fileName = "schizConnect-clinical-metadata-12-22-2016.json";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            ArrayList<SchizConnectClinicalMetadataJson> originalList = SchizConnectClinicalMetadataJson.readJsonArrayFromFile(filePath);


            System.out.println("testing ArrayList serialization");
            byte[] theBytes = SchizConnectClinicalMetadataJson.serializeArrayList(originalList);
            returnedList = SchizConnectClinicalMetadataJson.deserializeArrayList(theBytes);
            SchizConnectClinicalMetadataJson returnedObject = returnedList.get(1);
            System.out.println("the original study name: " + originalList.get(1).getStudy());
            System.out.println("the returned study name: " + returnedObject.getStudy());
            TestCase.assertTrue("serialize/deserialize loop failed", 
                                originalList.get(1).getStudy().compareTo(returnedObject.getStudy()) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testShizConnectFormatter() {
        String fileName = "schizConnect-clinical-metadata-12-22-2016.json";
        ArrayList<SchizConnectClinicalMetadataJson> schizList = 
           new ArrayList<SchizConnectClinicalMetadataJson>();
        ArrayList<SchizConnectClinicalMetadataJson> returnedList = 
           new ArrayList<SchizConnectClinicalMetadataJson>();
        Logger logger = Logger.getLogger("org.renci.databridge.contrib.ingest.schizConnect");
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            schizList = SchizConnectClinicalMetadataJson.readJsonArrayFromFile(filePath);

            System.out.println("testing formatting code");
            byte[] theBytes = SchizConnectClinicalMetadataJson.serializeArrayList(schizList);

            List<MetadataObject> metadataObjects = null;
            SchizConnectClinicalMetadataFormatter mf = new SchizConnectClinicalMetadataFormatter();
            mf.setLogger(logger);
            metadataObjects = mf.format (theBytes);
            System.out.println("number of metadata objects: " + metadataObjects.size());
            for (int i = 0; i < metadataObjects.size(); i++) {
               MetadataObject thisMeta = (MetadataObject) metadataObjects.get(i);
               System.out.println(thisMeta.getCollectionTransferObject());
            }
            MetadataObject thisMeta = (MetadataObject) metadataObjects.get(0);
            System.out.println("thisMeta: " + thisMeta.getCollectionTransferObject());
            List<FileTransferObject> thisFTOList = thisMeta.getFileTransferObjects();
            System.out.println("number of file objects: " + thisFTOList.size());
            System.out.println("This title: " + thisMeta.getCollectionTransferObject().getTitle());
            //TestCase.assertTrue("format failed", thisFTOList.size() == 34);
            for (int i = 0; i < thisFTOList.size(); i++) {
                System.out.println(thisFTOList.get(i));
            }
            System.out.println("*** End of test ***" + System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testShizGetBytes() {
        String fileName = "schizConnect-clinical-metadata-12-22-2016.json";
        System.out.println("Entering testShizGetBytes");
        ArrayList<SchizConnectClinicalMetadataJson> schizList = 
           new ArrayList<SchizConnectClinicalMetadataJson>();
        ArrayList<SchizConnectClinicalMetadataJson> returnedList = 
           new ArrayList<SchizConnectClinicalMetadataJson>();
        Logger logger = Logger.getLogger("org.renci.databridge.contrib.ingest.schizConnect");
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            String params = filePath + "&";
            System.out.println("params: " + params);

            SchizConnectClinicalMetadataFormatter theFormatter = new SchizConnectClinicalMetadataFormatter();
            byte[] theBytes = theFormatter.getBytes((Object) params);
            TestCase.assertTrue("byte array should not be null", theBytes != null);

            System.out.println("*** End of test ***" + System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}


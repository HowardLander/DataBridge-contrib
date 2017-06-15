package org.renci.databridge.contrib.ingest.generic.DataBridgeGenericJsonTest;

import org.renci.databridge.contrib.ingest.generic.DataBridgeGeneric.*;
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

public class DataBridgeGenericJsonTest {

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
        String fileName = "testGeneric.json";
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            ArrayList<DataBridgeGenericJson> theObjectList = DataBridgeGenericJson.readJsonArrayFromFile(filePath);
            TestCase.assertTrue("could not find theObjectList", theObjectList != null);
            System.out.println("theObjectList: " + theObjectList);
            DataBridgeGenericJson thisGenericJson = theObjectList.get(0); 
            String thisDescription = thisGenericJson.description;

            String theTitle = thisGenericJson.title;
            TestCase.assertTrue("title not returned correctly", theTitle.compareTo("firstTest") == 0);
            System.out.println("read title: " + theTitle);
 
            ArrayList<DataBridgeGenericJson.GenericJsonFileTransferObject> fileList = thisGenericJson.fileList;
            DataBridgeGenericJson.GenericJsonFileTransferObject firstFile = fileList.get(0);
            DataBridgeGenericJson.GenericJsonFileTransferObject thisFile = fileList.get(1);
            TestCase.assertTrue("name not returned correctly", thisFile.name.compareTo("secondFileTest") == 0);
            System.out.println("thisFile: " + thisFile);
            ArrayList<DataBridgeGenericJson.GenericJsonVariableTransferObject> theVars = 
               firstFile.variableList;
            DataBridgeGenericJson.GenericJsonVariableTransferObject thisVar = theVars.get(0);
            System.out.println("thisVar: " + thisVar);
            TestCase.assertTrue("description not returned correctly", thisVar.description.compareTo("the First Variable") == 0);

            DataBridgeGenericJson[] theObjectArray = 
              (DataBridgeGenericJson[]) theObjectList.toArray(new DataBridgeGenericJson[0]);
            TestCase.assertTrue("could not read data", theObjectArray != null);

            System.out.println("testing basic serialization");
            byte[] theBytes = DataBridgeGenericJson.serialize(theObjectArray);
            DataBridgeGenericJson[] theReturnedObjectList = null;
            theReturnedObjectList = DataBridgeGenericJson.deserialize(theBytes);
            System.out.println("the returned description: " + theReturnedObjectList[0].description);
            TestCase.assertTrue("serialize/deserialize loop failed", 
                                 thisDescription.compareTo(theReturnedObjectList[0].description) == 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testArraySerialization() {
        ArrayList<DataBridgeGenericJson> returnedList = null;
        String fileName = "testGeneric.json";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            ArrayList<DataBridgeGenericJson> originalList = DataBridgeGenericJson.readJsonArrayFromFile(filePath);


            System.out.println("testing ArrayList serialization");
            byte[] theBytes = DataBridgeGenericJson.serializeArrayList(originalList);
            returnedList = DataBridgeGenericJson.deserializeArrayList(theBytes);
            DataBridgeGenericJson returnedObject = returnedList.get(0);
            System.out.println("the original description: " + originalList.get(0).description);
            System.out.println("the returned description: " + returnedObject.description);
            TestCase.assertTrue("serialize/deserialize loop failed", 
                                originalList.get(0).description.compareTo(returnedObject.description) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testGenericFormatter() {
        String fileName = "testGeneric.json";
        ArrayList<DataBridgeGenericJson> genericList = new ArrayList<DataBridgeGenericJson>();
        ArrayList<DataBridgeGenericJson> returnedList = new ArrayList<DataBridgeGenericJson>();
        Logger logger = Logger.getLogger("org.renci.databridge.contrib.ingest.generic.DataBridgeGeneric");
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            genericList = DataBridgeGenericJson.readJsonArrayFromFile(filePath);

            System.out.println("testing formatting code");
            byte[] theBytes = DataBridgeGenericJson.serializeArrayList(genericList);

            List<MetadataObject> metadataObjects = null;
            DataBridgeGenericFormatter mf = new DataBridgeGenericFormatter();
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
            System.out.println("*** End of test ***" + System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testGenericGetBytes() {
        String fileName = "testGeneric.json";
        System.out.println("Entering testGenericGetBytes");
        ArrayList<DataBridgeGenericJson> genericList = new ArrayList<DataBridgeGenericJson>();
        ArrayList<DataBridgeGenericJson> returnedList = new ArrayList<DataBridgeGenericJson>();
        Logger logger = Logger.getLogger("org.renci.databridge.contrib.ingest.generic.DataBridgeGeneric");
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            String params = filePath + "&";
            System.out.println("params: " + params);

            DataBridgeGenericFormatter theFormatter = new DataBridgeGenericFormatter();
            byte[] theBytes = theFormatter.getBytes((Object) params);
            TestCase.assertTrue("byte array should not be null", theBytes != null);

            System.out.println("*** End of test ***" + System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}


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

public class SchizConnectTest {

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
        String fileName = "schizConnect-clinical-12-22-2016.json";
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            SchizConnectJson[] theObjectArray = SchizConnectJson.readJsonArrayFromFile(filePath);
            String theSubjectId = theObjectArray[0].getSubjectid();
            System.out.println("read SubjectId: " + theSubjectId);
            TestCase.assertTrue("could not read data", theSubjectId != null);

            System.out.println("testing basic serialization");
            byte[] theBytes = SchizConnectJson.serialize(theObjectArray);
            SchizConnectJson[] theReturnedObjectArray = SchizConnectJson.deserialize(theBytes);
            System.out.println("theReturnedSubjectId: " + theReturnedObjectArray[0].getSubjectid());
            TestCase.assertTrue("serialize/deserialize loop failed", 
                                 theSubjectId.compareTo(theReturnedObjectArray[0].getSubjectid()) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    @Test
    public void testArraySerialization() {
        ArrayList<SchizConnectJson> returnedList = new ArrayList<SchizConnectJson>();
        String fileName = "schizConnect-clinical-12-22-2016.json";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            SchizConnectJson[] theObjectArray = SchizConnectJson.readJsonArrayFromFile(filePath);

            returnedList.add(theObjectArray[0]);
            returnedList.add(theObjectArray[1]);

            System.out.println("testing ArrayList serialization");
            byte[] theBytes = SchizConnectJson.serializeArrayList(returnedList);
            returnedList = SchizConnectJson.deserializeArrayList(theBytes);
            SchizConnectJson returnedObject = returnedList.get(1);
            System.out.println("the original SubjectId: " + theObjectArray[1].getSubjectid());
            System.out.println("the returned SubjectId: " + returnedObject.getSubjectid());
            TestCase.assertTrue("serialize/deserialize loop failed", 
                                theObjectArray[1].getSubjectid().compareTo(returnedObject.getSubjectid()) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testFilterIngestFileToJson() {
        String fileName = "schizConnect-clinical-12-22-2016.json";
        String filter = "study:nMorphCH|subjectid:CH5994|assessment_description:Scale for the Assessment of Positive Symptoms";
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            ArrayList<SchizConnectJson> theObjectList = 
               SchizConnectJson.readFilteredJsonArrayFromFile(filePath, filter);
            System.out.println("Number of items returned: " + theObjectList.size());
            TestCase.assertTrue("Wrong number of items returned", theObjectList.size() == 34);
            System.out.println("First item returned: " + theObjectList.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testGetBytes() {
        String fileName = "schizConnect-clinical-12-22-2016.json";
        String filter = "study:nMorphCH|subjectid:CH5994|assessment_description:Scale for the Assessment of Positive Symptoms";
        ArrayList<SchizConnectJson> trialList = null;
        ArrayList<SchizConnectJson> fromGetList = null;
        ArrayList<SchizConnectJson> returnedList = null;

        Logger logger = Logger.getLogger("org.renci.databridge.contrib.ingest.schizConnect");
        SchizConnectFormatter mf = new SchizConnectFormatter();
        mf.setLogger(logger);
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            trialList = SchizConnectJson.readFilteredJsonArrayFromFile(filePath, filter);

            byte[] theBytes = SchizConnectJson.serializeArrayList(trialList);
            returnedList = SchizConnectJson.deserializeArrayList(theBytes);
            SchizConnectJson returnedObject = returnedList.get(1);
            String theReturnedId = returnedObject.getSubjectid();

            // Now try using getBytes
            System.out.println("testing getBytes");
            File resourcesDirectory = new File("src/test/resources");
            String input = resourcesDirectory.getAbsolutePath() + "/" + fileName + "&" + filter;
            byte[] fromGetBytes = mf.getBytes(input);
            // Make sure what we got from getBytes matches what we got from reading directlu
            fromGetList = SchizConnectJson.deserializeArrayList(fromGetBytes);
            SchizConnectJson returnedFromGetBytes = fromGetList.get(1);
            String getBytesId = returnedFromGetBytes.getSubjectid();

            System.out.println("theReturnedId: " + theReturnedId);
            System.out.println("getBytesId: " + getBytesId);
            TestCase.assertTrue("getBytesFailed",
                                theReturnedId.compareTo(getBytesId) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @Test
    public void testShizConnectFormatter() {
        String fileName = "schizConnect-clinical-12-22-2016.json";
        //String filter = "study:nMorphCH|subjectid:CH5994|assessment_description:Scale for the Assessment of Positive Symptoms";
        String filter = "study:nMorphCH|assessment_description:Scale for the Assessment of Positive Symptoms|visit:0";
        ArrayList<SchizConnectJson> schizList = new ArrayList<SchizConnectJson>();
        ArrayList<SchizConnectJson> returnedList = new ArrayList<SchizConnectJson>();
        Logger logger = Logger.getLogger("org.renci.databridge.contrib.ingest.schizConnect");
        try {
            // Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();

            String filePath = classLoader.getResource(fileName).getFile();
            schizList = SchizConnectJson.readFilteredJsonArrayFromFile(filePath, filter);

            System.out.println("testing formatting code");
            byte[] theBytes = SchizConnectJson.serializeArrayList(schizList);

            List<MetadataObject> metadataObjects = null;
            SchizConnectFormatter mf = new SchizConnectFormatter();
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
}


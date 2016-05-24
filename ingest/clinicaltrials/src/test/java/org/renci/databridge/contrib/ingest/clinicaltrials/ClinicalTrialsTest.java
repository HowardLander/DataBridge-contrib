package org.renci.databridge.contrib.ingest.clinicaltrials;

import org.renci.databridge.contrib.ingest.clinicaltrials.*;
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
            File file = new File(classLoader.getResource(fileName).getFile());
            BufferedReader nodeReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

           // Create the Gson object and read the file
           Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
           ClinicalTrialJson theObject = gson.fromJson(nodeReader, ClinicalTrialJson.class);
           String theURL = theObject.getSourceURL();
           System.out.println("read Url: " + theURL);
           TestCase.assertTrue("could not read data", theURL != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}


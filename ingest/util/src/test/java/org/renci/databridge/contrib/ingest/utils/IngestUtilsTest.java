package org.renci.databridge.contrib.ingest.util;

import org.renci.databridge.contrib.ingest.util.*;
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

public class IngestUtilsTest {

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
    public void testStopWordProcessor() {
        try {
            System.out.println("testing stop word processing");
            String testString = "This is a test string";
            String stoppedString = "test string";
            System.out.println("testString: " + testString);
            System.out.println("testString after stops:" + IngestUtils.removeStopWords(testString) + ":");
            TestCase.assertTrue("stop word processing failed",
                                stoppedString.compareTo(IngestUtils.removeStopWords(testString)) == 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}


package org.renci.databridge.contrib.similarity.util;

import org.renci.databridge.contrib.similarity.util.*;
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

public class SimilarityUtilsTest {

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
    public void testStopWordSet() {
        try {
            System.out.println("testing stop word set");
            Set<String> theWords = SimilarityUtils.getStopWords();
            TestCase.assertTrue("stop word set failed", theWords.size() == 429);


        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

}


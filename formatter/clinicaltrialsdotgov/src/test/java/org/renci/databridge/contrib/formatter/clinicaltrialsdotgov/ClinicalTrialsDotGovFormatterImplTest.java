package org.renci.databridge.contrib.formatter.clinicaltrialsdotgov;

import java.util.List;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.matchers.JUnitMatchers;
import org.junit.Rule;

import org.jsoup.Jsoup;

import org.renci.databridge.persistence.metadata.MetadataObject;
import org.renci.databridge.persistence.metadata.CollectionTransferObject;
import org.renci.databridge.persistence.metadata.FileTransferObject;
import org.renci.databridge.persistence.metadata.VariableTransferObject;
import org.renci.databridge.formatter.MetadataFormatter;

public class ClinicalTrialsDotGovFormatterImplTest {

    protected static String htmlString;

    @BeforeClass
    public static void setup () throws Exception {

      StringWriter sw = new StringWriter ();
      // https://clinicaltrials.gov/ct2/show/record/NCT00041938?term=%22Heart+attack%22+AND+%22Los+Angeles%22&rank=5
      // X https://clinicaltrials.gov/ct2/show/study/NCT00041938?term=%22Heart+attack%22+AND+%22Los+Angeles%22&rank=5
      try (InputStream is = ClinicalTrialsDotGovFormatterImplTest.class.getResourceAsStream ("/test0.htm")) {
        int c;
        while ((c = is.read ()) != -1 ) {
          sw.write (c);
        }
      }
      htmlString = sw.toString ();

    }

    @Test
    public void testFormatAPage () throws Exception {

      System.out.println ("Testing format a ClinicalTrials.gov HTML page...");

      ClinicalTrialsDotGovFormatterImpl ctdgfi = new ClinicalTrialsDotGovFormatterImpl ();
      List<MetadataObject> metadataObjects = ctdgfi.format (htmlString.getBytes ());
      for (MetadataObject mo : metadataObjects) {
        CollectionTransferObject cto = mo.getCollectionTransferObject ();
        TestCase.assertTrue ("Returned object is null", cto != null);

        //System.out.println (cto);

        //System.out.println ("FILE TRANSFER OBJECTS: " + mo.getFileTransferObjects ());
        //System.out.println ("VARIABLE TRANFER OBJECTS: " + mo.getVariableTransferObjects ());

        // TestCase.assertTrue ("CollectionTransferObject.subject has incorrect value ", ((cto.getProducer () != null) && (cto.getProducer ().startsWith ("Louis Harris"))));

      }

    }

    @Ignore
    @Test
    public void testEmitAUrlList () throws Exception {

      System.out.println ("Emitting files from a ClinicalTrials.cov URL list...");

      // get file URL list
      StringWriter sw = new StringWriter ();
      try (InputStream is = ClinicalTrialsDotGovFormatterImplTest.class.getResourceAsStream ("/urllist.txt")) {
        int c;
        while ((c = is.read ()) != -1 ) {
          sw.write (c);
        }
      }
      String urlList = sw.toString ();
System.out.println (urlList); 

      ClinicalTrialsDotGovFormatterImpl ctdgfi = new ClinicalTrialsDotGovFormatterImpl ();

      BufferedReader br = new BufferedReader (new StringReader (urlList));
      String url = null;
      // this will be the directory of this sub-project 
      String targetDirPath = new File (".").getCanonicalPath () + "/target/emitted-json";
      System.out.println ("Target path: " + targetDirPath);
      new File (targetDirPath).mkdir ();
      while ((url = br.readLine ()) != null) {

        // ignore blank lines and comments
        if (!url.startsWith ("#") && !url.equals ("")) {
          String html = Jsoup.connect (url).get ().html ();
          List<MetadataObject> metadataObjects = ctdgfi.format (htmlString.getBytes ());
          for (MetadataObject mo : metadataObjects) {
            CollectionTransferObject cto = mo.getCollectionTransferObject ();
            Map<String,String> extra = cto.getExtra ();
            String recordName = url.substring (url.lastIndexOf ("/") + 1, url.indexOf ("?"));
            System.out.println ("Emitting JSON for record '" + recordName + "'");
            PrintWriter out = new PrintWriter (targetDirPath + "/" + recordName + ".json");
            out.println (extra.toString ());
            out.close ();
          }
        }

      }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

}

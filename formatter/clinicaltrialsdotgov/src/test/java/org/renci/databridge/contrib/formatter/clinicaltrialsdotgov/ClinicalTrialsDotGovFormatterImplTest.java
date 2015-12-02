package org.renci.databridge.contrib.formatter.clinicaltrialsdotgov;

import java.util.List;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;
import java.util.HashMap;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

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

    @Ignore
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

    /**
     * File must be in format from clinicalTrials.gov download feature.
     */
    @Test
    public void testEmitJsonFromDownloadedStudyList () throws Exception {

      System.out.println ("Emitting files from a ClinicalTrials.cov URL list...");

      // get file with URL list
      StringWriter sw = new StringWriter ();
      try (InputStream is = ClinicalTrialsDotGovFormatterImplTest.class.getResourceAsStream ("/all_studies_list.txt")) {
        int c;
        while ((c = is.read ()) != -1 ) {
          sw.write (c);
        }
      }
      String allStudiesList = sw.toString ();

      ClinicalTrialsDotGovFormatterImpl ctdgfi = new ClinicalTrialsDotGovFormatterImpl ();

      BufferedReader br = new BufferedReader (new StringReader (allStudiesList));
      String line = null;
      // this will be the directory of this maven sub-project 
      String targetDirPath = new File (".").getCanonicalPath () + "/target/emitted-json";
      System.out.println ("Target path: " + targetDirPath);
      new File (targetDirPath).mkdir ();
      while ((line = br.readLine ()) != null) {

        // ignore lines that don't begin with "URL"
        line = line.trim ();
        if (line.startsWith ("URL:")) {
         
          String [] split = line.split ("\\s+"); 
          String originalUrl = split [1];
          // transform https://ClinicalTrials.gov/show/NCT02525536
          // to https://clinicaltrials.gov/ct2/show/record/NCT02525536
          String recordName = originalUrl.substring (originalUrl.lastIndexOf ("/") + 1, originalUrl.length ());
          String url = "https://clinicaltrials.gov/ct2/show/record/" + recordName;
          System.out.println ("Processing transformed URL: " + url);
          try { 
            String html = Jsoup.connect (url).get ().html ();
            // delay a bit to be polite
            Thread.sleep (1000);
            // append source URL to source HTML so formatter can retrieve it
            // is it cricket to put comment after closing html tag?
            html = html + "<!-- srcURL: " + url + " -->";

            List<MetadataObject> metadataObjects = ctdgfi.format (html.getBytes ());
            Gson gson = new GsonBuilder ().serializeNulls ().setPrettyPrinting ().create ();
            Type mapType = new TypeToken<Map<String,String>> () {}.getType ();
            for (MetadataObject mo : metadataObjects) {
              CollectionTransferObject cto = mo.getCollectionTransferObject ();
              Map<String,String> extra = cto.getExtra ();
              System.out.println ("Emitting JSON for record '" + recordName + "'");
              PrintWriter out = new PrintWriter (targetDirPath + "/" + recordName + ".json");

              // must unpack JSON from Map<String,String> values, reconstitute into objects, and then reJSONize the entire Map...
              Map<String,Object> fixedExtra = new HashMap<String,Object> ();
              String srcUrl = extra.get ("SOURCE_URL");
              String plainTextJson = extra.get ("TABULAR_VIEW_MAP_JSON");
              String lemmatizedJson = extra.get ("TABULAR_VIEW_MAP_LEMMATIZED_JSON");
              if (srcUrl != null) fixedExtra.put ("SOURCE_URL", srcUrl);
              fixedExtra.put ("TABULAR_VIEW_MAP_JSON", gson.fromJson (plainTextJson, Object.class));
              fixedExtra.put ("TABULAR_VIEW_MAP_LEMMATIZED_JSON", gson.fromJson (lemmatizedJson, Object.class));              
              String jsonizedMap = gson.toJson (fixedExtra);
              out.println (jsonizedMap);
              // out.println (extra.toString ());
              out.close ();
            }
          } catch (Exception e) {
            System.err.println ("Error:");
            e.printStackTrace ();
          }
        }

      }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

}

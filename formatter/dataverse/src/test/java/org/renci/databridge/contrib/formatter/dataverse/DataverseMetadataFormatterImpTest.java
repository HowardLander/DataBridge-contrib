package org.renci.databridge.contrib.formatter.dataverse;

import java.util.List;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.matchers.JUnitMatchers;
import org.junit.Rule;

import org.renci.databridge.persistence.metadata.MetadataObject;
import org.renci.databridge.persistence.metadata.CollectionTransferObject;
import org.renci.databridge.persistence.metadata.FileTransferObject;
import org.renci.databridge.persistence.metadata.VariableTransferObject;
import org.renci.databridge.formatter.MetadataFormatter;

public class DataverseMetadataFormatterImpTest {

    protected static String oaiPmhString;
    protected static String codeBookString;

    @BeforeClass
    public static void setup () throws Exception {

      StringWriter sw = new StringWriter ();
      try (InputStream is = DataverseMetadataFormatterImpTest.class.getResourceAsStream ("/45713.xml")) {
        int c;
        while ((c = is.read ()) != -1 ) {
          sw.write (c);
        }
      }
      codeBookString = sw.toString ();

      sw = new StringWriter ();
      try (InputStream is = DataverseMetadataFormatterImpTest.class.getResourceAsStream ("/OAI_Odum_Harris.xml")) {
        int c;
        while ((c = is.read ()) != -1 ) {
          sw.write (c);
        }
      }
      oaiPmhString = sw.toString ();

    }

    @Test
    public void testUnmarshalACodeBook () throws Exception {

      System.out.println ("Testing unmarshal CodeBook...");

      DataverseMetadataFormatterImpl dmfi = new DataverseMetadataFormatterImpl ();
      CodeBook cb = dmfi.unmarshal (codeBookString, CodeBook.class);
      TestCase.assertTrue ("Returned object is null",  cb != null);

      // ListRecordsType lrt = ot.getListRecords ();
      // List<RecordType> lr = lrt.getRecord ();
      // RecordType r = lr.get (0);
      // HeaderType h = r.getHeader ();
      // String i = h.getIdentifier ();

      // TestCase.assertTrue ("Record identifier is incorrect.", "Harris//hdl:1902.29/H-15085".equals (i));

    }

    @Test
    public void testUnmarshalAnOaiPmh () throws Exception {

      System.out.println ("Testing unmarshal OAI-PMH...");

      DataverseMetadataFormatterImpl dmfi = new DataverseMetadataFormatterImpl ();
      OAIPMHtype ot = dmfi.unmarshal (oaiPmhString, OAIPMHtype.class);
      TestCase.assertTrue ("Returned object is null",  ot != null);

      ListRecordsType lrt = ot.getListRecords ();
      List<RecordType> lr = lrt.getRecord ();
      RecordType r = lr.get (0);
      HeaderType h = r.getHeader ();
      String i = h.getIdentifier ();

      TestCase.assertTrue ("Record identifier is incorrect.", "Harris//hdl:1902.29/H-15085".equals (i));

    }

    @Test
    @Ignore
    public void testFormatACodeBook () throws Exception {

      System.out.println ("Testing format a CodeBook...");

      DataverseMetadataFormatterImpl dmfi = new DataverseMetadataFormatterImpl ();
      List<MetadataObject> metadataObjects = dmfi.format (codeBookString.getBytes ());
      for (MetadataObject mo : metadataObjects) {
        CollectionTransferObject cto = mo.getCollectionTransferObject ();
        TestCase.assertTrue ("Returned object is null", cto != null);
        System.out.println (cto);
        // TestCase.assertTrue ("CollectionTransferObject.subject has incorrect value ", ((cto.getProducer () != null) && (cto.getProducer ().startsWith ("Louis Harris"))));
      }

      // @todo add remaining tests
    }

    @Test
    public void testFormatAnOaiPmh () throws Exception {

      System.out.println ("Testing format an OAIPMH...");

      DataverseMetadataFormatterImpl dmfi = new DataverseMetadataFormatterImpl ();
      List<MetadataObject> metadataObjects = dmfi.format (oaiPmhString.getBytes ());
      for (MetadataObject mo : metadataObjects) {
        CollectionTransferObject cto = mo.getCollectionTransferObject ();
        TestCase.assertTrue ("Returned object is null", cto != null);
        System.out.println (cto); 
        // TestCase.assertTrue ("CollectionTransferObject.subject has incorrect value ", ((cto.getProducer () != null) && (cto.getProducer ().startsWith ("Louis Harris"))));
      }

      // @todo add remaining tests
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

}


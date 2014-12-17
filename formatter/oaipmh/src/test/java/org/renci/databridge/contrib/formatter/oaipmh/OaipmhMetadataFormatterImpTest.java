package org.renci.databridge.contrib.formatter.oaipmh;

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

import org.renci.databridge.persistence.metadata.CollectionTransferObject;
import org.renci.databridge.formatter.MetadataFormatter;

/**
 * Test OaipmhMetadataFormatterImpl.
 */
public class OaipmhMetadataFormatterImpTest {

    protected static String oaiPmhString;

    @BeforeClass
    public static void setup () throws Exception {

      StringWriter sw = new StringWriter ();

      try (InputStream is = OaipmhMetadataFormatterImpTest.class.getResourceAsStream ("/OAI_Odum_Harris.xml")) {
        int c;
        while ((c = is.read ()) != -1 ) {
          sw.write (c);
        }
      }

      oaiPmhString = sw.toString ();

    }

    @Test
    public void testUnmarshalAnOaiPmh () throws Exception {

      System.out.println ("Testing unmarshal OAI-PMH...");

      OaipmhMetadataFormatterImpl omfi = new OaipmhMetadataFormatterImpl ();
      OAIPMHtype ot = omfi.unmarshal (oaiPmhString);
      TestCase.assertTrue ("Returned object is null",  ot != null);

      ListRecordsType lrt = ot.getListRecords ();
      List<RecordType> lr = lrt.getRecord ();
      RecordType r = lr.get (0);
      HeaderType h = r.getHeader ();
      String i = h.getIdentifier ();

      TestCase.assertTrue ("Record identifier is incorrect.", "Harris//hdl:1902.29/H-15085".equals (i));

    }

    @Test
    public void testFormat () throws Exception {

      System.out.println ("Testing formatter...");

      OaipmhMetadataFormatterImpl omfi = new OaipmhMetadataFormatterImpl ();
      CollectionTransferObject cto = omfi.format (oaiPmhString.getBytes ());
      TestCase.assertTrue ("Returned object is null", cto != null);

      System.out.println (cto); 
      TestCase.assertTrue ("CollectionTransferObject.subject has incorrect value ", ((cto.getProducer () != null) && (cto.getProducer ().startsWith ("Louis Harris"))));

      // @todo add remaining tests
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

}


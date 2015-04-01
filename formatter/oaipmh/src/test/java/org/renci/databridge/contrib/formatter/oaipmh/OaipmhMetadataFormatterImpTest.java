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

import org.renci.databridge.persistence.metadata.MetadataObject;
import org.renci.databridge.persistence.metadata.CollectionTransferObject;
import org.renci.databridge.persistence.metadata.FileTransferObject;
import org.renci.databridge.persistence.metadata.VariableTransferObject;
import org.renci.databridge.formatter.MetadataFormatter;

import org.renci.databridge.contrib.formatter.codebook.CodeBook;

public class OaipmhMetadataFormatterImpTest {

  protected static String oaiPmhString;
  protected static String oaiPmhMultiString;
  protected static String oaiPmhWithBlankMetadataInRecordString;

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

    sw = new StringWriter ();
    try (InputStream is = OaipmhMetadataFormatterImpTest.class.getResourceAsStream ("/no-metadata-element-in-record.xml")) {
      int c;
      while ((c = is.read ()) != -1 ) {
        sw.write (c);
      }
    }
    oaiPmhWithBlankMetadataInRecordString = sw.toString ();
      
    sw = new StringWriter ();
    try (InputStream is = OaipmhMetadataFormatterImpTest.class.getResourceAsStream ("/odum-harvest-extract.xml")) {
    // try (InputStream is = OaipmhMetadataFormatterImpTest.class.getResourceAsStream ("/crabtreetest3162015")) {
      int c;
      while ((c = is.read ()) != -1 ) {
        sw.write (c);
      }
    }
    oaiPmhMultiString = sw.toString ();

  }

  @Ignore
  @Test
  public void testUnmarshalAnOaiPmh () throws Exception {

    System.out.println ("Testing unmarshal OAI-PMH...");

    OaipmhMetadataFormatterImpl omfi = new OaipmhMetadataFormatterImpl ();
    OAIPMHtype ot = omfi.unmarshal (oaiPmhString, OAIPMHtype.class, OAIPMHtype.class, CodeBook.class);
    TestCase.assertTrue ("Returned object is null",  ot != null);

    ListRecordsType lrt = ot.getListRecords ();
    List<RecordType> lr = lrt.getRecord ();
    RecordType r = lr.get (0);
    HeaderType h = r.getHeader ();
    String i = h.getIdentifier ();

    // @todo assert CodeBook objects are unmarshalled

    TestCase.assertTrue ("Record identifier is incorrect.", "Harris//hdl:1902.29/H-15085".equals (i));

  }

  @Ignore
  @Test
  public void testUnmarshalMultipleOaiPmh () throws Exception {

    System.out.println ("Testing unmarshal multiple OAI-PMH elements...");

    OaipmhMetadataFormatterImpl omfi = new OaipmhMetadataFormatterImpl ();
    OAIPMHtype ot = omfi.unmarshal (oaiPmhMultiString, OAIPMHtype.class, OAIPMHtype.class, CodeBook.class);
    TestCase.assertTrue ("Returned object is null",  ot != null);
 
    ListRecordsType lrt = ot.getListRecords ();
    TestCase.assertTrue ("Returned ListRecordsType is null",  lrt != null);

    List<RecordType> lr = lrt.getRecord ();
    RecordType r = lr.get (0);
    HeaderType h = r.getHeader ();
    String i = h.getIdentifier ();

    // @todo assert CodeBook objects are unmarshalled
    // @todo assert correct objects came out

  }

  @Test
  public void testFormatAnOaiPmh () throws Exception {

    System.out.println ("Testing format an OAIPMH...");

    OaipmhMetadataFormatterImpl omfi = new OaipmhMetadataFormatterImpl ();
    List<MetadataObject> metadataObjects = omfi.format (oaiPmhString.getBytes ());
    log (metadataObjects);
    // TestCase.assertTrue ("CollectionTransferObject.subject has incorrect value ", ((cto.getProducer () != null) && (cto.getProducer ().startsWith ("Louis Harris"))));

      // @todo add remaining tests
  }

  @Test
  public void testFormatAnOaiPmhWithMissingMetadataElement () throws Exception {

    System.out.println ("Testing format an OAIPMH with missing metadata element...");

    OaipmhMetadataFormatterImpl omfi = new OaipmhMetadataFormatterImpl ();
    List<MetadataObject> metadataObjects = omfi.format (oaiPmhWithBlankMetadataInRecordString.getBytes ());
    log (metadataObjects);
    // TestCase.assertTrue ("CollectionTransferObject.subject has incorrect value ", ((cto.getProducer () != null) && (cto.getProducer ().startsWith ("Louis Harris"))));

    // @todo add remaining tests
  }

  @Test
  public void testFormatMultipleOaiPmh () throws Exception {

    System.out.println ("Testing format a multiple OAIPMH...");

    OaipmhMetadataFormatterImpl omfi = new OaipmhMetadataFormatterImpl ();
    List<MetadataObject> metadataObjects = omfi.format (oaiPmhMultiString.getBytes ());
    log (metadataObjects);
    // TestCase.assertTrue ("CollectionTransferObject.subject has incorrect value ", ((cto.getProducer () != null) && (cto.getProducer ().startsWith ("Louis Harris"))));

    // @todo add remaining tests
  }

  protected void log (List<MetadataObject> metadataObjects) {

    for (MetadataObject mo : metadataObjects) {
      CollectionTransferObject cto = mo.getCollectionTransferObject ();
      TestCase.assertTrue ("Returned object is null", cto != null);
      System.out.println (cto);
      System.out.println ("FTOs: " + mo.getFileTransferObjects ());
      System.out.println ("VTOs: " + mo.getVariableTransferObjects ());
    }

  } 

  @Rule
  public ExpectedException thrown = ExpectedException.none();

}


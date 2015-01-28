package org.renci.databridge.contrib.formatter.codebook;

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

public class CodeBookMetadataFormatterImpTest {

    protected static String codeBookString;

    @BeforeClass
    public static void setup () throws Exception {

      StringWriter sw = new StringWriter ();
      try (InputStream is = CodeBookMetadataFormatterImpTest.class.getResourceAsStream ("/45713.xml")) {
        int c;
        while ((c = is.read ()) != -1 ) {
          sw.write (c);
        }
      }
      codeBookString = sw.toString ();

    }

    @Test
    public void testUnmarshalACodeBook () throws Exception {

      System.out.println ("Testing unmarshal CodeBook...");

      CodeBookMetadataFormatterImpl cbmfi = new CodeBookMetadataFormatterImpl ();
      CodeBook cb = cbmfi.unmarshal (codeBookString, CodeBook.class);
      TestCase.assertTrue ("Returned object is null",  cb != null);

      // ListRecordsType lrt = ot.getListRecords ();
      // List<RecordType> lr = lrt.getRecord ();
      // RecordType r = lr.get (0);
      // HeaderType h = r.getHeader ();
      // String i = h.getIdentifier ();

      // TestCase.assertTrue ("Record identifier is incorrect.", "Harris//hdl:1902.29/H-15085".equals (i));

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

}

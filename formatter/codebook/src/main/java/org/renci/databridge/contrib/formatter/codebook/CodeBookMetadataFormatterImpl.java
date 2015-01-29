package org.renci.databridge.contrib.formatter.codebook;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.StringReader;
import java.io.Serializable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.bind.JAXBElement;

import org.renci.databridge.formatter.JaxbMetadataFormatter;
import org.renci.databridge.formatter.FormatterException;

import org.renci.databridge.persistence.metadata.MetadataObject;
import org.renci.databridge.persistence.metadata.CollectionTransferObject;
import org.renci.databridge.persistence.metadata.FileTransferObject;
import org.renci.databridge.persistence.metadata.VariableTransferObject;

/**
 * MetadataFormatter implementation for DDI CodeBook object.
 * 
 * @author mrshoffn
 */
public class CodeBookMetadataFormatterImpl extends JaxbMetadataFormatter {

  private Logger logger = Logger.getLogger ("org.renci.databridge.formatter.codebook");

  @Override
  public List<MetadataObject> format (byte [] bytes) throws FormatterException {

    List<MetadataObject> metadataObjects = new ArrayList<MetadataObject> ();
    String metadataString = new String (bytes);
    this.logger.log (Level.FINER, "bytes: '" + metadataString + "'");

    this.logger.log (Level.FINER, "Processing a CodeBook.");
    processCodeBook (metadataObjects, metadataString); 

    return metadataObjects;

  }
 
  /**
   * @param metadataObjects is modified by this method.
   */
  protected void processCodeBook (List<MetadataObject> metadataObjects, String metadataString) throws FormatterException {

    CodeBook cb = unmarshal (metadataString, CodeBook.class);

    MetadataObject mo = new MetadataObject ();
    metadataObjects.add (mo);
    CollectionTransferObject cto = new CollectionTransferObject ();
    mo.setCollectionTransferObject (cto);

// codebook extraction
      List<StdyDscrType> sdtList = cb.getStdyDscr ();
      StdyDscrType sdt = sdtList.get (0);

      List<CitationType> ctList = sdt.getCitation ();
      if (ctList != null && ctList.size () > 0) {
        CitationType ct = ctList.get (0);

        // title | stdyDscr->citation->titlStmt->titl
        TitlStmtType ts = ct.getTitlStmt ();
        TitlType tt = ts.getTitl ();
        cto.setTitle (flatten (tt.getContent ()));

        // producer | stdyDscr->citation->prodStmt->producer
        ProdStmtType pst = ct.getProdStmt ();
        List<ProducerType> prodList = pst.getProducer ();
        if (prodList != null && prodList.size () > 0) {
          ProducerType pt = prodList.get (0);
          cto.setProducer (flatten (pt.getContent ()));
        }

      }

      List<StdyInfoType> sitList = sdt.getStdyInfo ();
      if (sitList != null && sitList.size () > 0) {
        StdyInfoType sit = sitList.get (0);

        // description | stdyDscr->stdyinfo->abstract
        List<AbstractType> atList = sit.getAbstract ();
        if (atList != null && atList.size () > 0) {
          AbstractType at = atList.get (0);
          cto.setDescription (flatten (at.getContent ()));
        }

        // subject | stdyDscr->stdyinfo->subject
        SubjectType st = sit.getSubject ();
        // @todo cto.setSubject ();

        // keywords | stdyDscr->stdyInfo->subject->keyword
        ArrayList<String> keywords = new ArrayList<String> ();
        List<KeywordType> kwList = st.getKeyword ();
        for (KeywordType kt : kwList) {
          keywords.add (flatten (kt.getContent ()));
        }

        // extra | HashMap of fields under stdyDscr->stdyinfo->sumDscr
        Map<String, String> extra = new HashMap<String, String> ();
        // extra.put ("", "");
        // cto.setExtra (cto);

      }
// end codebook extraction

  }
  
}

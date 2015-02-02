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

    String metadataString = new String (bytes);
    this.logger.log (Level.FINER, "bytes: '" + metadataString + "'");
    CodeBook cb = unmarshal (metadataString, CodeBook.class);

    List<MetadataObject> metadataObjects = new ArrayList<MetadataObject> ();
    this.logger.log (Level.FINER, "Processing a CodeBook.");
    metadataObjects.add (extractCodeBook (cb)); 

    return metadataObjects;

  }
 
  /**
   *  
   */
  public MetadataObject extractCodeBook (CodeBook codeBook) throws FormatterException {

    MetadataObject mo = new MetadataObject ();
    CollectionTransferObject cto = new CollectionTransferObject ();
    mo.setCollectionTransferObject (cto);
    List<FileTransferObject> ftos = new ArrayList<FileTransferObject> ();
    List<VariableTransferObject> vtos = new ArrayList<VariableTransferObject> ();

    // codebook extraction
      List<StdyDscrType> sdtList = codeBook.getStdyDscr ();
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

    return mo;

  }
  
}

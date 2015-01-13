package org.renci.databridge.contrib.formatter.dataverse;

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

import org.renci.databridge.formatter.MetadataFormatter;
import org.renci.databridge.formatter.FormatterException;

import org.renci.databridge.persistence.metadata.MetadataObject;
import org.renci.databridge.persistence.metadata.CollectionTransferObject;
import org.renci.databridge.persistence.metadata.FileTransferObject;
import org.renci.databridge.persistence.metadata.VariableTransferObject;

/**
 * MetadataFormatter implementation for Dataverse document, which can be either OAI-PMH XML document or a CodeBook document.
 *
 * Every record in an OAI-PMH document is a CodeBook object that maps to a MetadataObject.
 *
 * @author mrshoffn
 */
public class DataverseMetadataFormatterImpl implements MetadataFormatter {

  private Logger logger = Logger.getLogger ("org.renci.databridge.formatter.oaipmh");

  @Override
  public List<MetadataObject> format (byte [] bytes) throws FormatterException {

    List<MetadataObject> metadataObjects = new ArrayList<MetadataObject> ();
    String metadataString = new String (bytes);
    this.logger.log (Level.FINER, "bytes: '" + metadataString + "'");

    // a GOF "Chain of Responsibility"
    this.logger.log (Level.FINER, "Trying for an OAIPMHtype.");
    processOAIPMHtype (metadataObjects, metadataString);
    if (metadataObjects.size () == 0) { 
      this.logger.log (Level.FINER, "Not an OAIPMHtype, so will try CodeBook.");
      processCodeBook (metadataObjects, metadataString); 
    }

    return metadataObjects;

  }
 
  /**
   * @param metadataObjects is modified by this method.
   */
  protected void processOAIPMHtype (List<MetadataObject> metadataObjects, String metadataString) throws FormatterException {

    OAIPMHtype ot = unmarshal (metadataString, OAIPMHtype.class);
    ListRecordsType lrt = ot.getListRecords ();

    // @todo A bit gross, but JAXB returns an "empty" if asked to parse by specific type and the type does not exist in the input.
    List<RecordType> rtList = null;
    try {     
      rtList = lrt.getRecord ();
    } catch (NullPointerException npe) {
      this.logger.log (Level.FINER, "Got an NPE, so we take that as no OAIPMH content object.");
      return;
    }

    Iterator<RecordType> i = rtList.iterator ();
    while (i.hasNext ()) {

      RecordType r = i.next ();
      HeaderType h = r.getHeader ();

      MetadataObject mo = new MetadataObject ();
      metadataObjects.add (mo);
      CollectionTransferObject cto = new CollectionTransferObject ();
      mo.setCollectionTransferObject (cto);
      List<FileTransferObject> ftos = new ArrayList<FileTransferObject> ();
      List<VariableTransferObject> vtos = new ArrayList<VariableTransferObject> ();
 
      // URL | record->header->indentifier e.g., Harris//hdl:1902.29/H-15085
      cto.setURL (constructUrl (h.getIdentifier ()));

      MetadataType mt = r.getMetadata ();
      Object any = mt.getAny ();
      if (!(any instanceof CodeBook)) {
        throw new FormatterException (any.getClass ().getName () + " is unknown ANY element for OAI-PMH. A schema for this element probably needs to added to the build for this formatter.");
      }
      CodeBook cb = (CodeBook) any;
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

      // nameSpace | From User
      cto.setNameSpace ("test");

      // version | Start with 1 (Do we need to add a timestamp in data model?)
      cto.setVersion (1);

      // dataStoreId | Generated by the DAO code 

    }

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
  
  /**
   * "Flattens" content object from XmlMixed type to a string.
   * @returns first entry of list, cast to String, or null
   */
  protected String flatten (List<Serializable> list) {
    String s = null;
    if (list != null && list.size () > 0) {
      s = (String) list.get (0);
    }
    return s;
  }

  protected <X> X unmarshal (String xml, Class<X> clazz) throws FormatterException {

    X content = null;
    try { 

      JAXBContext jc = JAXBContext.newInstance (clazz);
      Unmarshaller unmarshaller = jc.createUnmarshaller ();
      StreamSource ss = new StreamSource (new StringReader (xml));
      // JAXBElement<X> root = unmarshaller.unmarshal (ss, clazz);
      Object o = unmarshaller.unmarshal (ss, clazz);
      // @todo this is gross but JAXB returns some content object roots in a wrapper. May be fixable with JAXB configuration.
      if (o instanceof JAXBElement) {
        content = ((JAXBElement<X>) o).getValue ();
      } else {
        content = (X) o;
      }

    } catch (JAXBException je) {

      throw new FormatterException (je);

    }

    return content;

  }

  /**
   * @todo
   * Input: Harris//hdl:1902.29/H-15085
   * Output: hdl.handle.net/1902.29/H-15085
   */
  protected String constructUrl (String headerIdentifier) {

return headerIdentifier; 

  }

}

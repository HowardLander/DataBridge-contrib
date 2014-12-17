package org.renci.databridge.contrib.formatter.oaipmh;

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

import org.renci.databridge.persistence.metadata.CollectionTransferObject;

/**
 * MetadataFormatter implementation for OAI-PMH.
 
 * @author mrshoffn
 */
public class OaipmhMetadataFormatterImpl implements MetadataFormatter {

  private Logger logger = Logger.getLogger ("org.renci.databridge.formatter.oaipmh");

  @Override
  public CollectionTransferObject format (byte [] bytes) throws FormatterException {

    String metadataString = new String (bytes);
    this.logger.log (Level.FINER, "bytes: '" + metadataString + "'");

    CollectionTransferObject cto = new CollectionTransferObject ();

    OAIPMHtype ot = unmarshal (metadataString);
    ListRecordsType lrt = ot.getListRecords ();
    List<RecordType> rtList = lrt.getRecord ();
    Iterator<RecordType> i = rtList.iterator ();
    while (i.hasNext ()) {
      RecordType r = i.next ();
      HeaderType h = r.getHeader ();

      // URL | record->header->indentifier e.g., Harris//hdl:1902.29/H-15085
      cto.setURL (constructUrl (h.getIdentifier ()));

      MetadataType mt = r.getMetadata ();
      Object any = mt.getAny ();
      if (!(any instanceof CodeBook)) {
        throw new FormatterException (any.getClass ().getName () + " is unknown ANY element for OAI-PMH. A schema for this element probably needs to added to the build for this formatter.");
      }
      CodeBook cb = (CodeBook) any;
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

      // nameSpace | From User
      cto.setNameSpace ("test");

      // version | Start with 1 (Do we need to add a timestamp in data model?)
      cto.setVersion (1);

      // dataStoreId | Generated by the DAO code 

    }

    return cto;

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

  protected OAIPMHtype unmarshal (String xml) throws FormatterException {

    OAIPMHtype ot = null;

    try { 

      JAXBContext jc = JAXBContext.newInstance (OAIPMHtype.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller ();
      StreamSource ss = new StreamSource (new StringReader (xml));
      JAXBElement<OAIPMHtype> root = unmarshaller.unmarshal (ss, OAIPMHtype.class);
      ot = root.getValue ();

    } catch (JAXBException je) {

      throw new FormatterException (je);

    }

    return ot;

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

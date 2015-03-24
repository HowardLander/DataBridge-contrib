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
 * MetadataFormatter implementation for DDI-Lite CodeBook object.
 *
 * @todo Harden against missing fields
 *
 * @author mrshoffn
 */
public class CodeBookMetadataFormatterImpl extends JaxbMetadataFormatter {

  private Logger logger = Logger.getLogger ("org.renci.databridge.formatter.codebook");

  public CodeBookMetadataFormatterImpl () {
    // logger may be replaced later if parent calls superclass setLogger method
    setLogger (Logger.getLogger ("org.renci.databridge.contrib.formatter.codebook"));
  }

  @Override
  public List<MetadataObject> format (byte [] bytes) throws FormatterException {

    String metadataString = new String (bytes);
    this.logger.log (Level.FINER, "bytes: '" + metadataString + "'");
    CodeBook cb = unmarshal (metadataString, CodeBook.class, CodeBook.class);

    List<MetadataObject> metadataObjects = new ArrayList<MetadataObject> ();
    this.logger.log (Level.FINER, "Processing a CodeBook.");
    metadataObjects.add (extractCodeBook (cb)); 

    return metadataObjects;

  }
 
  /**
   * @returns MetadataObject that contains a CollectionTransferObject, which generally has empty values instead of nulls set for missing values.
   * Impl defaults to using the first element in a list.
   */
  public MetadataObject extractCodeBook (CodeBook codeBook) throws FormatterException {

    MetadataObject mo = new MetadataObject ();
    CollectionTransferObject cto = new CollectionTransferObject ();
    mo.setCollectionTransferObject (cto);
    List<FileTransferObject> ftos = new ArrayList<FileTransferObject> ();
    List<VariableTransferObject> vtos = new ArrayList<VariableTransferObject> ();
    mo.setFileTransferObjects (ftos);
    mo.setVariableTransferObjects (vtos);

    // @todo put an "empty" check here like in OAIPMH formatter

    List<DocDscrType> ddtList = codeBook.getDocDscr ();
    DocDscrType ddt = ddtList.get (0);
    CitationType ct = ddt.getCitation ();

    // title | docDscr->citation->titlStmt->titl
    TitlStmtType ts = ct.getTitlStmt ();
    TitlType tt = ts.getTitl ();
    cto.setTitle (flatten (tt.getContent ()));

    // URL | docDscr->citation->holdings:URI
    List<HoldingsType> htList = ct.getHoldings ();
    if (htList != null && htList.size () > 0) {
      HoldingsType ht = htList.get (0);
      if (ht != null) {
        cto.setURL (ht.getURI ());
      }
    }

    List<StdyDscrType> sdtList = codeBook.getStdyDscr ();
    StdyDscrType sdt = sdtList.get (0);

    // producer | stdyDscr->citation->prodStmt->producer
    List<CitationType> ctList = sdt.getCitation ();
    if (ctList != null && ctList.size () > 0) {
      CitationType ct1 = ctList.get (0);
      ProdStmtType pst = ct1.getProdStmt ();
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

      // subject | empty, as stdyDscr->stdyinfo->subject has no relevant content
      cto.setSubject ("");

      // keywords | stdyDscr->stdyInfo->subject->keyword
      SubjectType st = sit.getSubject ();
      ArrayList<String> keywords = new ArrayList<String> ();
      if (st != null) {
        List<KeywordType> kwList = st.getKeyword ();
        if (kwList != null) {
          for (KeywordType kt : kwList) {
            keywords.add (flatten (kt.getContent ()));
          }
        }
      }
      cto.setKeywords (keywords);

      // extra | HashMap of fields under stdyDscr->stdyinfo->sumDscr
      HashMap<String, String> extra = new HashMap<String, String> ();
      List<SumDscrType> sdtList1 = sit.getSumDscr ();
      if (sdtList1 != null && sdtList1.size () > 0) {
        SumDscrType sdt1 = sdtList1.get (0);
        
        List<TimePrdType> tptList = sdt1.getTimePrd ();
        if (tptList != null && tptList.size () > 0) {
          TimePrdType tpt = tptList.get (0);
          extra.put ("TimePrd", flatten (tpt.getContent ()));
        }

        List<CollDateType> cdtList = sdt1.getCollDate ();
        if (cdtList != null && cdtList.size () > 0) {
          CollDateType cdt = cdtList.get (0);
          extra.put ("CollDate", flatten (cdt.getContent ()));
        }

        List<NationType> ntList = sdt1.getNation ();
        if (ntList != null && ntList.size () > 0) {
          NationType nt = ntList.get (0);
          extra.put ("Nation", flatten (nt.getContent ()));
        }

        List<GeogCoverType> gctList = sdt1.getGeogCover ();
        if (gctList != null && gctList.size () > 0) {
          GeogCoverType gct = gctList.get (0);
          extra.put ("GeogCover", flatten (gct.getContent ()));
        }

        List<GeogUnitType> gutList = sdt1.getGeogUnit ();
        if (gutList != null && gutList.size () > 0) {
          GeogUnitType gut = gutList.get (0);
          extra.put ("GeogUnit", flatten (gut.getContent ()));
        }

        List<AnlyUnitType> autList = sdt1.getAnlyUnit ();
        if (gutList != null && autList.size () > 0) {
          AnlyUnitType aut = autList.get (0);
          extra.put ("AnlyUnit", flatten (aut.getContent ()));
        }

        List<UniverseType> utList = sdt1.getUniverse ();
        if (utList != null && utList.size () > 0) {
          UniverseType ut = utList.get (0);
          extra.put ("Universe", flatten (ut.getContent ()));
        }

        List<DataKindType> dktList = sdt1.getDataKind ();
        if (dktList != null && dktList.size () > 0) {
          DataKindType dkt = dktList.get (0);
          extra.put ("DataKind", flatten (dkt.getContent ()));
        }

      }
      cto.setExtra (extra);

    }

    /// FileTransferObjects 
    List<FileDscrType> fdtList = codeBook.getFileDscr ();
    for (FileDscrType fdt : fdtList) { 

      FileTransferObject fto = new FileTransferObject ();

      // URL | fileDscr:URI
      fto.setURL (fdt.getURI ());

      // name | fileDscr->fileTxt->fileName
      List<FileTxtType> fttList = fdt.getFileTxt ();
      if (fttList != null && fttList.size () > 0) {
        FileTxtType ftt = fttList.get (0);
        FileNameType fnt = ftt.getFileName ();
        fto.setName (flatten (fnt.getContent ()));
      }

      // description | fileDscr->notes
      List<NotesType> ntList = fdt.getNotes ();
      if (ntList != null && ntList.size () > 0) {
        NotesType nt = ntList.get (0);
        fto.setDescription (flatten (nt.getContent ()));
      }

      fto.setVersion (1);

      /*
        fto.setDataStoreId (String);
        fto.setCollectionDataStoreId (String);
        fto.setExtra (HashMap<String, String>);
       */

      ftos.add (fto);

    }

    /// VariableTransferObjects
    List<DataDscrType> ddtList1 = codeBook.getDataDscr ();
    for (DataDscrType ddt1 : ddtList1) {

      VariableTransferObject vto = new VariableTransferObject ();

      List<VarType> vtList = ddt1.getVar ();
      if (vtList != null && vtList.size () > 0) {
 
        // name | codeBook->dataDscr->var:name
        VarType vt = vtList.get (0);
        vto.setName (vt.getName ());

        // description | codeBook->dataDscr->notes
        List<NotesType> ntList = vt.getNotes ();
        if (ntList != null && ntList.size () > 0) {
          NotesType nt = ntList.get (0);
          vto.setDescription (flatten (nt.getContent ()));
        }
 
      }

      vto.setVersion (1);

      /*
        vto.setDataStoreId (String);
        vto.setFileDataStoreId(String (String);
        vto.setExtra (HashMap<String, String>);
       */

      vtos.add (vto);

    }

    // nameSpace | From User
    cto.setNameSpace ("test");

    // version | Start with 1 (Do we need to add a timestamp in data model?)
    cto.setVersion (1);

    // dataStoreId | Generated by the DAO code

    return mo;

  }
  
}

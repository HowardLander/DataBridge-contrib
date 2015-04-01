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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.renci.databridge.formatter.JaxbMetadataFormatter;
import org.renci.databridge.formatter.FormatterException;

import org.renci.databridge.persistence.metadata.MetadataObject;
import org.renci.databridge.persistence.metadata.CollectionTransferObject;
import org.renci.databridge.persistence.metadata.FileTransferObject;
import org.renci.databridge.persistence.metadata.VariableTransferObject;

import org.renci.databridge.contrib.formatter.codebook.CodeBook;
import org.renci.databridge.contrib.formatter.codebook.CodeBookMetadataFormatterImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Implementation for OAI-PMH document. An OAI-PMH document is a container that holds a variable number of metadata objects. Currently supports only embeded CodeBook record type.
 *
 * This formatter depends on CodeBookMetadataFormatterImpl for its CodeBook unmarshalling and processing.
 *
 * @author mrshoffn
 */
public class OaipmhMetadataFormatterImpl extends JaxbMetadataFormatter {

  public OaipmhMetadataFormatterImpl () {
    // logger may be replaced later if parent calls superclass setLogger method
    setLogger (Logger.getLogger ("org.renci.databridge.contrib.formatter.oaipmh"));
  }

  @Override
  public List<MetadataObject> format (byte [] bytes) throws FormatterException {

    String metadataString = new String (bytes);
    List<MetadataObject> metadataObjects = new ArrayList<MetadataObject> ();

    this.logger.log (Level.FINE, "Processing a document containing OAI-PMH element(s).");
 
    // preprocess to chop into OAI-PMH element substrings bc JAXB doesn't want to unmarshal a collection of things that are under the doc root element. 
    List<String> oaipmhElementXmlList = tokenize (metadataString);

    Iterator<String> i = oaipmhElementXmlList.iterator ();
    while (i.hasNext ()) {
      String oaipmhElementXml = i.next (); 
      OAIPMHtype ot = unmarshal (oaipmhElementXml, OAIPMHtype.class, OAIPMHtype.class, CodeBook.class);
      this.logger.log (Level.FINE, "Processing an OAIPMHtype.");
      if (ot.getListRecords () != null) {
        // skip OAIPMH objects that have a null ListRecords element value
        extractOAIPMHtype (ot, metadataObjects);
      } else {
        this.logger.log (Level.FINE, "Ignoring the OAI-PMH element because it has no ListRecords.");
      }
 
    }

    return metadataObjects;

  }

  @Override
  protected File getValidationSchema () throws IOException {
    return null;
  }
 
  /**
   * @param oaipmhType oaipmhType.getListRecords () must != null
   * @param metadataObjects is modified by this method.
   */
  protected void extractOAIPMHtype (OAIPMHtype oaipmhType, List<MetadataObject> metadataObjects) throws FormatterException {

    ListRecordsType lrt = oaipmhType.getListRecords ();
    List<RecordType> rtList = lrt.getRecord ();

    // for delegating codebook extration
    CodeBookMetadataFormatterImpl cbmfi = new CodeBookMetadataFormatterImpl ();
    cbmfi.setLogger (this.logger);

    Iterator<RecordType> i = rtList.iterator ();
    while (i.hasNext ()) {

      RecordType r = i.next ();
      HeaderType h = r.getHeader ();

      MetadataType mt = r.getMetadata ();
      if (mt != null) {
        Object any = mt.getAny ();
        if (!(any instanceof CodeBook)) {
          throw new FormatterException (any.getClass ().getName () + " is unknown ANY element for OAI-PMH. A schema for CodeBook probably needs to added to the build for this formatter.");
        }
        CodeBook cb = (CodeBook) any;
        MetadataObject mo = cbmfi.extractCodeBook (cb); 
        metadataObjects.add (mo);
      } else {
        this.logger.log (Level.INFO, "Skipping <record> that has no <metadata> element.");
      }

    }

  }

  /**
   * @returns invididual OAI-PMH element XML from a doc containing mulitples.   
   */
  protected List<String> tokenize (String xmlDoc) {

    List<String> tokens = new ArrayList<String> ();
    // final String s = "<OAI-PMH((.*))</OAI-PMH>"; 
    final String s = "<OAI-PMH((.+?))</OAI-PMH>"; 
    final Pattern p = Pattern.compile (s, Pattern.DOTALL); 
    final Matcher m = p.matcher (xmlDoc); 
    this.logger.log (Level.FINER, "Matching for pattern '" + s + "'");
    int i = 1;
    while (m.find ()) {
      String g = m.group ();
      this.logger.log (Level.FINEST, "Adding '" + g + "'");       
      tokens.add (g);
    }
    return tokens;

  }

}

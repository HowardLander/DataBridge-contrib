package org.renci.databridge.contrib.formatter.clinicaltrialsdotgov;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.StringReader;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;

import org.renci.databridge.formatter.FormatterException;
import org.renci.databridge.formatter.MetadataFormatter;

import org.renci.databridge.persistence.metadata.MetadataObject;
import org.renci.databridge.persistence.metadata.CollectionTransferObject;
import org.renci.databridge.persistence.metadata.FileTransferObject;
import org.renci.databridge.persistence.metadata.VariableTransferObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * MetadataFormatter implementation for a ClinicalTrials.gov page.
 *
 * @author mrshoffn
 */
public class ClinicalTrialsDotGovFormatterImpl implements MetadataFormatter {

  private Logger logger = Logger.getLogger ("org.renci.databridge.formatter.clinicaltrialsdotgov");

  public ClinicalTrialsDotGovFormatterImpl () {
  }

  @Override
  public List<MetadataObject> format (byte [] bytes) throws FormatterException {

    String metadataString = new String (bytes);
    List<MetadataObject> metadataObjects = new ArrayList<MetadataObject> ();
    this.logger.log (Level.FINER, "Processing a ClinicalTrials.gov page.");
    metadataObjects.add (extractClinicalTrialsDotGov (metadataString)); 

    return metadataObjects;

  }

  protected MetadataObject extractClinicalTrialsDotGov (String html) {

    MetadataObject mo = new MetadataObject ();
    CollectionTransferObject cto = new CollectionTransferObject ();
    mo.setCollectionTransferObject (cto);
    // ftos and vtos are not actually used so they are "empty"
    List<FileTransferObject> ftos = new ArrayList<FileTransferObject> ();
    List<VariableTransferObject> vtos = new ArrayList<VariableTransferObject> ();
    mo.setFileTransferObjects (ftos);
    mo.setVariableTransferObjects (vtos);

    // set up lemmatizer with init params: http://nlp.stanford.edu/software/corenlp.shtml
    StanfordLemmatizer sl = new StanfordLemmatizer ();

    // process HTML 
    Document doc = Jsoup.parse (html);
    // http://jsoup.org/apidocs/org/jsoup/select/Selector.html 
    // there should be only one element with CSS class "data_table"
    Element data_table = doc.select (".data_table").first ();
    // for some reason jsoup seems to insert a tbody element during parse
    Element tbody = data_table.child (0); 
    Iterator<Element> trs = tbody.children ().iterator ();
    Map plainTextMap = new HashMap ();
    Map lemmatizedTextMap = new HashMap ();
    while (trs.hasNext ()) {
      Element tr = trs.next ();
      // in tabular page format th child contains "key" and td child has "value"
      Element th = tr.select ("th").first ();
      String thText = (th != null) ? th.text () : null;
      // System.out.println ("thText: " + thText);
      Element td = tr.select ("td").first ();
      String tdText = (td != null) ? td.text () : null;
      // System.out.println ("tdText: " + tdText);

      if (thText != null) {
        plainTextMap.put (thText, tdText);
        if (tdText != null) {
          List<String> lemmatizedText = sl.lemmatize (tdText);
          lemmatizedTextMap.put (thText, lemmatizedText);
        }
      }
    }
    // srcUrl is expecetd to be the last comment in html string
    int srcUrlStartIdx = html.lastIndexOf ("<!-- srcURL: ") + 13;
    int srcUrlEndIdx = html.indexOf (" -->", srcUrlStartIdx);
    String srcUrl = html.substring (srcUrlStartIdx, srcUrlEndIdx);
    System.out.println ("srcURL: '" + srcUrl + "'");

    // jsonize
    // Gson gson = new Gson ();
    Gson gson = new GsonBuilder ().serializeNulls ().setPrettyPrinting ().create (); 
    String plainTextJson = gson.toJson (plainTextMap); 
    String lemmatizedJson = gson.toJson (lemmatizedTextMap); 

    // package json into "extra" hashmap
    // @todo data structure need to be altered slightly
    HashMap<String, String> extra = new HashMap<String, String> ();
    if (srcUrl != null) extra.put ("SOURCE_URL", srcUrl);
    extra.put ("TABULAR_VIEW_MAP_JSON", plainTextJson);
    extra.put ("TABULAR_VIEW_MAP_LEMMATIZED_JSON", lemmatizedJson); 
    cto.setExtra (extra);

    return mo;

  }

  public void setLogger (Logger logger) {
    this.logger = logger;
  }

}

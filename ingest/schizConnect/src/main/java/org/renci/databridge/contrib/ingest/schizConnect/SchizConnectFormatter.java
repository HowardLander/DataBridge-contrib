package org.renci.databridge.contrib.ingest.schizConnect;
import org.renci.databridge.contrib.ingest.util.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.annotations.*;
import org.renci.databridge.formatter.*;
import org.renci.databridge.persistence.metadata.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This class holds the data for a single clinical trial read from the json file
 * 
 * @author Howard Lander -RENCI (www.renci.org)
 * 
 */
public class SchizConnectFormatter implements MetadataFormatter{
    protected Logger logger = null;

    /**
     * @param bytes "Document" that implementor understands the format for. In this case
     *               it is an ArrayList of SchizConnectJson objects.
     * @return the metadata elements from the bytes. 
     */
    @Override
    public List<MetadataObject> format (byte [] bytes) throws FormatterException {
       ArrayList<MetadataObject> objectList = new ArrayList<MetadataObject>();

       try {
           // First order of business: convert the byte array back to the structure.
           ArrayList<SchizConnectJson> schizList = SchizConnectJson.deserializeArrayList(bytes);
           this.logger.log (Level.INFO, "after deserializeArrayList: found "  + schizList.size() + " objects");

           // Now traverse the list of schiz connect structure. In this case, there is a series of
           // structures for each unique subject id. Our strategy in this case to have one collection
           // record for each subject, and a series of "File" records for each of the records for the
           // patient. This is, of course, a bit of a bastardization of the original structure of the
           // metadata, however it allows us to represent the hierarchy we need.
           int i = 0;
           String currentSubject = null;
           ArrayList<FileTransferObject> ftoList =null;
           MetadataObject thisMeta = null;

           for (SchizConnectJson thisSchizConnect: schizList) {
              if (i % 100 == 0) {
                 this.logger.log (Level.INFO, "processing schizRecord " + i);
              }
              i++;
              // Note: we are assuming that the JSON structures come sorted by first by patient id and 
              // then by question_id.  This seems to be the case, but if it isn't we can sort the list 
              // with a Comparator. Ex,see 
              // http://www.codejava.net/java-core/collections/sorting-a-list-by-multiple-attributes-example

              if ((currentSubject == null) ||
                 (currentSubject.compareTo(thisSchizConnect.getSubjectid()) != 0)) {
                 // Either the first record or the first record on a new subject.
                 // So this only happens once for each subject.
                 // IMPORTANT NOTE: This code currently is assuming that the subject id's are unique
                 // per assessment. In fact a subject can be assessed more than once...
                 currentSubject = thisSchizConnect.getSubjectid();
                 thisMeta = new MetadataObject();
                 ftoList = new ArrayList<FileTransferObject>(); 
                 thisMeta.setFileTransferObjects(ftoList);
                 objectList.add(thisMeta);

                 CollectionTransferObject thisCTO = new CollectionTransferObject();
                 thisCTO.setURL(thisSchizConnect.getAssessment_description());
                 thisCTO.setTitle(thisSchizConnect.getSubjectid());
                 thisCTO.setDescription(thisSchizConnect.getAssessment_description());
                 thisCTO.setProducer(thisSchizConnect.getSource());
                 thisCTO.setSubject(thisSchizConnect.getStudy());
                 HashMap<String, String> extra = new HashMap<String, String>();
                 extra.put("site", thisSchizConnect.getSite());
                 extra.put("visit", thisSchizConnect.getVisit());
                 thisCTO.setExtra(extra); 
                 thisMeta.setCollectionTransferObject(thisCTO);
              } 
        
              // Now setup the file object. Note this is unconditional, so this 
              // happens for each subject as many times as there are filtered records for that subject.
              FileTransferObject thisFTO = new FileTransferObject();
              thisFTO.setName(thisSchizConnect.getAssessment_description());
              HashMap<String, String> fileExtra = new HashMap<String, String>();
              fileExtra.put("question_id", thisSchizConnect.getQuestion_id());
              fileExtra.put("question_value", thisSchizConnect.getQuestion_value());
              thisFTO.setExtra(fileExtra); 
              ftoList.add(thisFTO);
           }
       } catch (Exception e) {
         this.logger.log (Level.SEVERE, "Caught in format: " + e.getMessage(),e);
         throw new FormatterException("Caught in format: " + e.getMessage());
       }
       return objectList;
    }

  /**
   * @param input A class specific object that tells the function where to find the objects
   *              to turn into bytes. In this case, it's a String containing the URI for a 
   *              file containing the Json data as well as a set of key,value filter pairs
   *              to pass to the readFilteredJsonArrayFromFile call. Input looks like:
   *              FileName&key1:value1|key2:value2
   * @return byte array representing the objects.
   */
  public byte[] getBytes(Object input) throws FormatterException {
     String stringInput = (String) input;
     String[] parsedInput = stringInput.split("&");
     String fileName = parsedInput[0];
     String filters = parsedInput[1];
     byte [] bytes = null;

     try {
        ArrayList<SchizConnectJson> theSchizJsons = 
           SchizConnectJson.readFilteredJsonArrayFromFile(fileName, filters);
        bytes = SchizConnectJson.serializeArrayList(theSchizJsons);
    
     } catch (Exception e) {
        this.logger.log (Level.SEVERE, "Problems with the schiz connect input:  " + fileName, e);
        throw new FormatterException("Problems with the schiz connect input:  " + fileName);
     }
     return bytes;
  }
    /**
     * Set a logger (e.g., parent's logger).
     */
    @Override
    public void setLogger (Logger logger) {
        this.logger = logger;
    }
}

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
public class SchizConnectClinicalMetadataFormatter implements MetadataFormatter{
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
           ArrayList<SchizConnectClinicalMetadataJson> schizList = 
              SchizConnectClinicalMetadataJson.deserializeArrayList(bytes);
           this.logger.log (Level.INFO, "after deserializeArrayList: found "  + schizList.size() + " objects");

           // Now traverse the list of schiz connect clinical metadata structures. In this case, 
           // there is a series of structures, one for each study. Each study has an arraylist of 
           // of instruments.  We are going to represent each study as a collection and each
           // instrument as a file attached to that instrument.
           int i = 0;
           ArrayList<FileTransferObject> ftoList = null;
           MetadataObject thisMeta = null;

           for (SchizConnectClinicalMetadataJson thisStudy: schizList) {
              String currentStudy = thisStudy.getStudy();
              thisMeta = new MetadataObject();
              ftoList = new ArrayList<FileTransferObject>(); 
              thisMeta.setFileTransferObjects(ftoList);
              objectList.add(thisMeta);

              CollectionTransferObject thisCTO = new CollectionTransferObject();
              thisCTO.setURL(thisStudy.getStudy());
              thisCTO.setTitle(thisStudy.getStudy());
              thisCTO.setDescription(thisStudy.getStudy());
              thisCTO.setProducer(thisStudy.getStudy());
              thisCTO.setSubject(thisStudy.getStudy());
              thisMeta.setCollectionTransferObject(thisCTO);
        
              // Now setup the file objects for this study.
              ArrayList<String> theInstruments = thisStudy.getInstrumentList();
              for (String thisInstrument: theInstruments) {
                 FileTransferObject thisFTO = new FileTransferObject();
                 thisFTO.setName(thisInstrument);
                 ftoList.add(thisFTO);
              }
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
   *              file containing the Json data.
   * @return byte array representing the objects.
   */
  public byte[] getBytes(Object input) throws FormatterException {
     String stringInput = (String) input;
     String[] parsedInput = stringInput.split("&");
     String fileName = parsedInput[0];
     byte [] bytes = null;

     try {
        ArrayList<SchizConnectClinicalMetadataJson> theSchizJsons = 
           SchizConnectClinicalMetadataJson.readJsonArrayFromFile(fileName);
        bytes = SchizConnectClinicalMetadataJson.serializeArrayList(theSchizJsons);
    
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

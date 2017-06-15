package org.renci.databridge.contrib.ingest.generic.DataBridgeGeneric;
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
public class DataBridgeGenericFormatter implements MetadataFormatter{
    protected Logger logger = null;

    /**
     * @param bytes "Document" that implementor understands the format for. In this case
     *               it is an ArrayList of DataBridgeGenericJson objects.
     * @return the metadata elements from the bytes. 
     */
    @Override
    public List<MetadataObject> format (byte [] bytes) throws FormatterException {
       ArrayList<MetadataObject> objectList = new ArrayList<MetadataObject>();

       try {
           // First order of business: convert the byte array back to the structure.
           ArrayList<DataBridgeGenericJson> genericList = 
              DataBridgeGenericJson.deserializeArrayList(bytes);
           this.logger.log (Level.INFO, "after deserializeArrayList: found "  + genericList.size() + " objects");

           // Now traverse the list of generic metadata structures. We will map this directly into
           // our 3 layer metadata hierarchy
           int i = 0;
           MetadataObject thisMeta = null;

           for (DataBridgeGenericJson thisGeneric: genericList) {
              thisMeta = new MetadataObject();
              objectList.add(thisMeta);

              CollectionTransferObject thisCTO = new CollectionTransferObject();
              thisCTO.setURL(thisGeneric.URL);
              thisCTO.setTitle(thisGeneric.title);
              thisCTO.setDescription(thisGeneric.description);
              thisCTO.setProducer(thisGeneric.producer);
              thisCTO.setSubject(thisGeneric.subject);
              thisCTO.setKeywords(thisGeneric.keywords);
              thisCTO.setExtra(thisGeneric.extra);
              thisMeta.setCollectionTransferObject(thisCTO);
        
              // Now setup the file objects for this study.
              if (thisGeneric.fileList != null) {
                 ArrayList<FileTransferObject> ftoList = new ArrayList<FileTransferObject>(); 
                 for (DataBridgeGenericJson.GenericJsonFileTransferObject thisFile: thisGeneric.fileList) {
                    FileTransferObject thisFTO = new FileTransferObject();
                    thisFTO.setURL(thisFile.URL);
                    thisFTO.setName(thisFile.name);
                    thisFTO.setDescription(thisFile.description);
                    thisFTO.setExtra(thisFile.extra);
                    ftoList.add(thisFTO);

                    // Let's not forget about the variables
                    if (thisFile.variableList != null) {
                       ArrayList<VariableTransferObject> varList = new ArrayList<VariableTransferObject>(); 
                       for (DataBridgeGenericJson.GenericJsonVariableTransferObject thisVar: 
                           thisFile.variableList) {
                          VariableTransferObject thisVTO = new VariableTransferObject();
                          thisVTO.setName(thisVar.name);
                          thisVTO.setDescription(thisVar.description);
                          thisVTO.setExtra(thisVar.extra);
                          varList.add(thisVTO);
                       }
                       thisFTO.setVariableList(varList);
                    }
                 }
                 thisCTO.setFileList(ftoList);
                 thisMeta.setFileTransferObjects(ftoList);
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
        ArrayList<DataBridgeGenericJson> theGenericJsons = 
           DataBridgeGenericJson.readJsonArrayFromFile(fileName);
        bytes = DataBridgeGenericJson.serializeArrayList(theGenericJsons);
    
     } catch (Exception e) {
        this.logger.log (Level.SEVERE, "Problems with the generic connect input:  " + fileName, e);
        throw new FormatterException("Problems with the generic connect input:  " + fileName);
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

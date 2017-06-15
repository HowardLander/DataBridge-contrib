package org.renci.databridge.contrib.ingest.generic.DataBridgeGeneric;
import org.renci.databridge.contrib.ingest.util.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.annotations.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.lang.reflect.*;


/**
 * This class holds the data for a the generic JSON file.  This provides a common
 * interface for users to get metadata into the DataBridge system. It does support
 * all 3 levels of the DataBridge metadata format (Collections, Files and Variables).
 * The top level class represents the collections. The GenericJsonFileTransferObject internal
 * class is used to represent the file objects associated with a collection and the 
 * GenericJsonVariableTransferObject represents variables associated with a file.
 * This maps to the 3 level hierarchy in which we store data in our persistent
 * metadata storage.  If you have looked at our persistence code, you may notice
 * that these 3 objects also map to the Collection, File and Variable transfer objects
 * with only the user supplied fields. 
 * 
 * @author Howard Lander -RENCI (www.renci.org)
 * 
 */
public class DataBridgeGenericJson implements Serializable{

    public class GenericJsonVariableTransferObject implements Serializable{
       @SerializedName("name") public String name;
       @SerializedName("description") public String description;    // Free text metadata about the study
       @SerializedName("extra") public HashMap<String, String> extra;

       public String toString() {
          return (this.name + " " + this.description + " " + this.extra);
       }
    }

    public class GenericJsonFileTransferObject implements Serializable{
       @SerializedName("URL") public String URL;
       @SerializedName("name") public String name;
       @SerializedName("description") public String description;    // Free text metadata about the study
       @SerializedName("extra") public HashMap<String, String> extra;
       @SerializedName("variableList") public ArrayList<GenericJsonVariableTransferObject>  variableList; 

       public String toString() {
          return (this.URL + " " + this.name + " " + this.description + " " + this.extra);
       }
    }

    // This set of fields maps directly to the collection transfer object
    @SerializedName("URL") public String URL;
    @SerializedName("title") public String title;
    @SerializedName("description") public String description;    // Free text metadata about the study
    @SerializedName("producer") public String producer;               // Producer of the collection
    @SerializedName("subject") public String subject;                // Subject of the study
    @SerializedName("keywords") public ArrayList<String>   keywords;  // The keywords
    @SerializedName("extra") public HashMap<String, String> extra;
    @SerializedName("fileList") public ArrayList<GenericJsonFileTransferObject>  fileList;  // The keywords

       public String toString() {
          return (this.URL + " " + this.title + " " + this.description + " " + this.producer + " " +
                  this.subject + " "  + this.keywords + " " + this.extra);
       }

   /**
    * Serialize the object
    *
    * @param The object to convert to an array of bytes
    * @return The array of bytes representing the object
    */
    public static byte[] serialize(DataBridgeGenericJson[] obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

   /**
    * Deserialize the object
    *
    * @param The array of bytes representing the object to convert to a SchizConnectJson object.
    * @return The deserialized object
    */
    public static DataBridgeGenericJson[] deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        DataBridgeGenericJson[] theTrialArray = 
            (DataBridgeGenericJson[]) is.readObject();
        return theTrialArray;
    }

   /**
    * Serialize an array of objects
    *
    * @param The objects to convert to an array of bytes
    * @return The array of bytes representing the object
    */
    public static byte[] serializeArrayList(ArrayList<DataBridgeGenericJson> obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

   /**
    * Deserialize the objects
    *
    * @param The array of bytes representing the objects to convert to an array of SchizConnectJson objects.
    * @return The deserialized array op objects
    */
    public static ArrayList<DataBridgeGenericJson> deserializeArrayList(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        @SuppressWarnings("unchecked")
        ArrayList<DataBridgeGenericJson> theTrials = (ArrayList<DataBridgeGenericJson>) is.readObject();
        return theTrials;
    }
 

   /**
    * Read an array of DataBridgeGenericJson objects from a json file
    *
    * @param the file name to read
    * @return The array of objects containing an in memory version of the named file.
    */
    public static ArrayList<DataBridgeGenericJson> readJsonArrayFromFile(String fileName) {
        DataBridgeGenericJson[] theObjectArray = null;
        ArrayList<DataBridgeGenericJson> theReturnList = null;
        try {
            // Get file 
            File file = new File(fileName);
            BufferedReader nodeReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

           // Create the Gson object and read the file
           Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
           theObjectArray = gson.fromJson(nodeReader, DataBridgeGenericJson[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        theReturnList = new ArrayList<DataBridgeGenericJson>(Arrays.asList(theObjectArray));
        return theReturnList;
    }

}

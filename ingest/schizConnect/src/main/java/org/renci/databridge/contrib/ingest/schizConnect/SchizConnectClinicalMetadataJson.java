package org.renci.databridge.contrib.ingest.schizConnect;
import org.renci.databridge.contrib.ingest.util.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.annotations.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.lang.reflect.*;


/**
 * This class holds the data for a single clinical trial read from the json file
 * 
 * @author Howard Lander -RENCI (www.renci.org)
 * 
 */
public class SchizConnectClinicalMetadataJson implements Serializable{
    @SerializedName("study") public String study;
    @SerializedName("instrumentList") public ArrayList<String> instrumentList;

  public String toString() {
     return (this.study + " " + this.instrumentList.toString());
  }

   /**
    * Serialize the object
    *
    * @param The object to convert to an array of bytes
    * @return The array of bytes representing the object
    */
    public static byte[] serialize(SchizConnectClinicalMetadataJson[] obj) throws IOException {
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
    public static SchizConnectClinicalMetadataJson[] deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        SchizConnectClinicalMetadataJson[] theTrialArray = 
            (SchizConnectClinicalMetadataJson[]) is.readObject();
        return theTrialArray;
    }

   /**
    * Serialize an array of objects
    *
    * @param The objects to convert to an array of bytes
    * @return The array of bytes representing the object
    */
    public static byte[] serializeArrayList(ArrayList<SchizConnectClinicalMetadataJson> obj) throws IOException {
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
    public static ArrayList<SchizConnectClinicalMetadataJson> deserializeArrayList(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        @SuppressWarnings("unchecked")
        ArrayList<SchizConnectClinicalMetadataJson> theTrials = (ArrayList<SchizConnectClinicalMetadataJson>) is.readObject();
        return theTrials;
    }
 

   /**
    * Read an array of SchizConnectClinicalMetadataJson objects from a json file
    *
    * @param the file name to read
    * @return The array of objects containing an in memory version of the named file.
    */
    public static ArrayList<SchizConnectClinicalMetadataJson> readJsonArrayFromFile(String fileName) {
        SchizConnectClinicalMetadataJson[] theObjectArray = null;
        ArrayList<SchizConnectClinicalMetadataJson> theReturnList = null;
        try {
            // Get file 
            File file = new File(fileName);
            BufferedReader nodeReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

           // Create the Gson object and read the file
           Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
           theObjectArray = gson.fromJson(nodeReader, SchizConnectClinicalMetadataJson[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        theReturnList = new ArrayList<SchizConnectClinicalMetadataJson>(Arrays.asList(theObjectArray));
        return theReturnList;
    }

 
 /**
  * Get study.
  *
  * @return study as String.
  */
 public String getStudy()
 {
     return study;
 }
 
 /**
  * Set study.
  *
  * @param study the value to set.
  */
 public void setStudy(String study)
 {
     this.study = study;
 }
 
 /**
  * Get instrumentList.
  *
  * @return instrumentList as ArrayList<String>.
  */
 public ArrayList<String> getInstrumentList()
 {
     return instrumentList;
 }
 
 /**
  * Set instrumentList.
  *
  * @param instrumentList the value to set.
  */
 public void setInstrumentList(ArrayList<String> instrumentList)
 {
     this.instrumentList = instrumentList;
 }
}

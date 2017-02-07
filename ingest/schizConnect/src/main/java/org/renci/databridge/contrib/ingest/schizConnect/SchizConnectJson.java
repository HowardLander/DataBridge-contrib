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
public class SchizConnectJson implements Serializable{
    @SerializedName("source") public String source;
    @SerializedName("study") public String study;
    @SerializedName("site") public String site;
    @SerializedName("subjectid") public String subjectid;
    @SerializedName("visit") public String visit;
    @SerializedName("assessment") public String assessment;
    @SerializedName("assessment_description") public String assessment_description;
    @SerializedName("question_id") public String question_id;
    @SerializedName("question_value") public String question_value;

  public String toString() {
     return (this.source + " " + this.study + " " + this.site + " " + this.subjectid + " " +
             this.visit + " " +  this.assessment + " " + this.assessment_description  + " " +
             this.question_id + " "  + this.question_value);
  }

   /**
    * Serialize the object
    *
    * @param The object to convert to an array of bytes
    * @return The array of bytes representing the object
    */
    public static byte[] serialize(SchizConnectJson[] obj) throws IOException {
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
    public static SchizConnectJson[] deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        SchizConnectJson[] theTrialArray = (SchizConnectJson[]) is.readObject();
        return theTrialArray;
    }

   /**
    * Serialize an array of objects
    *
    * @param The objects to convert to an array of bytes
    * @return The array of bytes representing the object
    */
    public static byte[] serializeArrayList(ArrayList<SchizConnectJson> obj) throws IOException {
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
    public static ArrayList<SchizConnectJson> deserializeArrayList(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        @SuppressWarnings("unchecked")
        ArrayList<SchizConnectJson> theTrials = (ArrayList<SchizConnectJson>) is.readObject();
        return theTrials;
    }
 

   /**
    * Read an array of SchizConnectJson objects from a json file
    *
    * @param the file name to read
    * @return The array of objects containing an in memory version of the named file.
    */
    public static SchizConnectJson[] readJsonArrayFromFile(String fileName) {
        SchizConnectJson[] theObjectArray = null;
        try {
            // Get file 
            File file = new File(fileName);
            BufferedReader nodeReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

           // Create the Gson object and read the file
           Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
           theObjectArray = gson.fromJson(nodeReader, SchizConnectJson[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return theObjectArray;
    }

   /**
    * Read a filtered arrayList of SchizConnectJson objects from a json file
    *
    * @param the file name to read
    * @param a delimited set of key,value filter pairs like "key1:value1|key2:value2"
    * @return The filtered ArrayList of objects
    */
    public static ArrayList<SchizConnectJson> readFilteredJsonArrayFromFile(String fileName, String filters) {
        ArrayList<SchizConnectJson> filteredList = new ArrayList<SchizConnectJson>();
        String[] filtersArray = filters.split("\\|");
        String[] fieldNames = new String[filtersArray.length];
        String[] fieldValues = new String[filtersArray.length];

        // Split each filter and assign the name and value elements.
        for (int i = 0; i < filtersArray.length; i++) {
           String[] keyValuePair = filtersArray[i].split(":");
           fieldNames[i] = keyValuePair[0]; 
           fieldValues[i] = keyValuePair[1]; 
        }
        try {
            // Get the array of objects from the file. I don't think there is a way to get the Gson
            // library to do the filtering. We could consider the streaming API, but it looks pretty 
            // cumbersome, so we use reflection. Not as bad as it sounds!
            SchizConnectJson[] theObjectArray = readJsonArrayFromFile(fileName);

            Class theClass = org.renci.databridge.contrib.ingest.schizConnect.SchizConnectJson.class;
            // iterate through each json member
            for (SchizConnectJson theJson: theObjectArray) {
               boolean addThisOne = true;
               for (int i = 0; i < fieldNames.length; i++) {
                  // Get the value of the field using reflection
                  Field thisField = theClass.getField(fieldNames[i]);
                  String valueInJson = (String) thisField.get(theJson);
                  String valueInFilter = fieldValues[i];
                  if (valueInJson.compareToIgnoreCase(valueInFilter) != 0) {
                     // This filter not a match, we are only supporting "and"
                     // so this one doesn't qualify
                     addThisOne = false;

                     // Go to the next json struct
                     break;
                  }
               }
               if (addThisOne) {
                  filteredList.add(theJson);
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredList;
    }


   /**
    * Create a single string object from an array.  Used for some of fields in the 
    * SchizConnectJson object
    *
    * @param the array of strings
    * @param whether or not to call the stop word code from the IngestUtil class
    * @return A single string with all of the members of the input array separated.
    */
   public static String strJoin(String[] theArray, boolean stop) {
        String theSeparator = " ";
        StringBuilder sb = new StringBuilder();
        Logger logger = Logger.getLogger ("org.renci.databridge.contrib.ingest.schizConnect");
        if (theArray == null) {
            return new String("");
        }
        if (theArray.length == 0) {
            return new String("");
        } else {
           for (int i = 0; i < theArray.length; i++) {
               if (i > 0) {
                   // No separator at the beginning
                   sb.append(theSeparator);
               }
               sb.append(theArray[i]);
           }
           if (stop) {
               try {
                   return IngestUtils.removeStopWords(sb.toString());
               } catch (Exception e) {
                   logger.log (Level.SEVERE, "Caught in format processing " +  ":" + sb.toString() + ": " + e.getMessage(),e);
                   return new String("");
               }
           } else {
               return sb.toString();
           }
        }
    }
 
 
 /**
  * Get source.
  *
  * @return source as String.
  */
 public String getSource()
 {
     return source;
 }
 
 /**
  * Set source.
  *
  * @param source the value to set.
  */
 public void setSource(String source)
 {
     this.source = source;
 }
 
 /**
  * Get site.
  *
  * @return site as String.
  */
 public String getSite()
 {
     return site;
 }
 
 /**
  * Set site.
  *
  * @param site the value to set.
  */
 public void setSite(String site)
 {
     this.site = site;
 }
 
 /**
  * Get visit.
  *
  * @return visit as String.
  */
 public String getVisit()
 {
     return visit;
 }
 
 /**
  * Set visit.
  *
  * @param visit the value to set.
  */
 public void setVisit(String visit)
 {
     this.visit = visit;
 }
 
 /**
  * Get assessment.
  *
  * @return assessment as String.
  */
 public String getAssessment()
 {
     return assessment;
 }
 
 /**
  * Set assessment.
  *
  * @param assessment the value to set.
  */
 public void setAssessment(String assessment)
 {
     this.assessment = assessment;
 }
 
 
 /**
  * Get subjectid.
  *
  * @return subjectid as String.
  */
 public String getSubjectid()
 {
     return subjectid;
 }
 
 /**
  * Set subjectid.
  *
  * @param subjectid the value to set.
  */
 public void setSubjectid(String subjectid)
 {
     this.subjectid = subjectid;
 }
 
 /**
  * Get question_id.
  *
  * @return question_id as String.
  */
 public String getQuestion_id()
 {
     return question_id;
 }
 
 /**
  * Set question_id.
  *
  * @param question_id the value to set.
  */
 public void setQuestion_id(String question_id)
 {
     this.question_id = question_id;
 }
 
 /**
  * Get question_value.
  *
  * @return question_value as String.
  */
 public String getQuestion_value()
 {
     return question_value;
 }
 
 /**
  * Set question_value.
  *
  * @param question_value the value to set.
  */
 public void setQuestion_value(String question_value)
 {
     this.question_value = question_value;
 }
 
 /**
  * Get assessment_description.
  *
  * @return assessment_description as String.
  */
 public String getAssessment_description()
 {
     return assessment_description;
 }
 
 /**
  * Set assessment_description.
  *
  * @param assessment_description the value to set.
  */
 public void setAssessment_description(String assessment_description)
 {
     this.assessment_description = assessment_description;
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
}

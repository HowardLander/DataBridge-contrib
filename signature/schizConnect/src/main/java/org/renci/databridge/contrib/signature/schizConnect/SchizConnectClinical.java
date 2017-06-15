package org.renci.databridge.contrib.signature.schizConnect;
import java.util.*;
import org.renci.databridge.persistence.metadata.*;
import org.apache.jena.rdf.model.ModelFactory.*;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.*;
import org.apache.jena.system.*;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * Signature processing class for the schizConnectClinical data. Uses the databridgeforneuroscience
 * ontology to process the collection metadata.  Produces a vector showing which of the
 * toplevel terms in the ontology is represented in the input collection.
 *
 * @author Howard Lander -RENCI (www.renci.org)
 *
 */

public class SchizConnectClinical implements SignatureProcessor {
   protected Logger logger = null;
   public static final String ONTOLOGY_URL = "ontologyURL";
   public static final String ONTOLOGY_PREFIX = "ontologyPrefix";
   public static final String ONTOLOGY_ROOT = "ontologyRoot";
   public static final String SEP1 = ",";
   public static final String SEP2 = "|";
   public static final String SEP2REGEX = "\\|";

   /*
    *  Convert the parameters from key1:value1|key2:value2 to a hashmap of string,string where
    *  ":" is sep1 and the "|" is sep 2. So if sep1 is # and sep 2 is "|" than the format will be
    *
    *          key1#value1|key2#value2
    *
    *  @params params The params to convert.
    *
    *  @return A HashMap<String, String> with the parameters.
    */
  private HashMap<String, String> processParameters(String params, String sep1, String sep2) {

     HashMap<String, String> theReturnMap = new HashMap<String, String>();
     String[] pairs = params.split(SEP2REGEX);
     for (int i = 0; i < pairs.length; i++) {
        String[] thisParam = pairs[i].split(SEP1);
        theReturnMap.put(thisParam[0], thisParam[1]);
     }

     return theReturnMap;
  }

   /*
    *  Convert the parameters from key1:value1|key2:value2 to a hashmap of string,string
    *
    *  @params params The params to convert.  Must be in the format:
    *
    *          key1:value1|key2:value2
    *
    *  @return A HashMap<String, String> with the parameters.
    */
  private HashMap<String, String> processParameters(String params) {

     HashMap<String, String> theReturnMap = new HashMap<String, String>();
     String[] pairs = params.split("|");
     for (int i = 0; i < pairs.length; i++) {
        String[] thisParam = pairs[i].split(":");
        theReturnMap.put(thisParam[0], thisParam[1]);
     }

     return theReturnMap;
  }

   /*
    *  The method that is called to perform the signature extraction and which implements the
    *  extractSignature abstract method in the SignatureProcessor interface. In this case, we 
    *  are going to find every instrument name (stored as files in the CollectionTransferObject) and
    *  use the defined ShizConnect ontology to attempt to classify the name into one of the high
    *  level categories. In any case, we need to have one file object for each of the "canonical"
    *  high level categories. If it's there, the value is 1, otherwise the value is 0.
    * 
    *  @param theCollection The CollectionTransferObject from which to extract a signature
    *  @params params Class/instantiation parameters to be passed to the implementing class. In this
    *          case the params have to include the URL for the ontology, the prefix of interest in
    *          the ontology and the root node of the ontology. The parameters are input as
    *
    *          key1:value1|key2:value2
    *
    *  @return A collection transfer object containing the extracted signature.
    */
   public CollectionTransferObject extractSignature(CollectionTransferObject theCollection, String params) {
       CollectionTransferObject theReturnedObject = new CollectionTransferObject();
       HashMap <String, String> instrumentMap = null;

       if (this.logger == null) {
          this.logger = Logger.getLogger ("org.renci.databridge.contrib.signature.SchizConnectClinical");
       }

      try {
         JenaSystem.init();
         HashMap<String, String>hashedParams = processParameters(params, SEP1, SEP2);

         // We need to get the ontologyURL, prefix and root from the params. If they aren't there we
         // have a problem
         String ontologyURL = hashedParams.get(ONTOLOGY_URL);
         if (ontologyURL == null) {
           this.logger.log (Level.SEVERE, "No ontologyURL");
           return null;
         }

         String prefix = hashedParams.get(ONTOLOGY_PREFIX);
         if (prefix == null) {
           this.logger.log (Level.SEVERE, "No ontologyPrefix");
           return null;
         }

         String root = hashedParams.get(ONTOLOGY_ROOT);
         if (root == null) {
           this.logger.log (Level.SEVERE, "No ontologyRoot");
           return null;
         }

         // Now we get the set of all possible highest level instruments. Initially all of the 
         // values will be set to "0".  For each instrument in this study we find using getSecondDegree
         // we set the value to "1".
         instrumentMap = createSubjectHashMap(ontologyURL, prefix, root);

         ArrayList<FileTransferObject> subjectList = theCollection.getFileList();

         for (int i = 0; i < subjectList.size(); i++) {

            // For each File Transfer Object, we get the name. The name is an instrument
            // that exists in this collection
            FileTransferObject fto = subjectList.get(i);
            String thisInstrument = fto.getName();
            OntClass thisOntClass = findSecondDegree(ontologyURL, prefix, root, thisInstrument);
            if (null != thisOntClass) {
               String thisLable = thisOntClass.getLabel(null);
               instrumentMap.put(thisLable, "1");
            } else {
               this.logger.log (Level.FINE, "failed to map: " + thisInstrument);
            }
         }

         // returned object to the incoming one.
         theCollection.setExtra(instrumentMap);
         theReturnedObject = theCollection;
       } catch (Exception e) {
           this.logger.log (Level.SEVERE, "Caught in extractSignature: " + e.getMessage(),e);
       } finally {
          return theReturnedObject;
       }

   }

   /*
    *  This method is used to find the desired ancestor for a desired token in the given
    *  ontology and prefix.  This is a little trickey as we are not looking for the highest
    *  level class (using the rdfs:subClassof relationships) but the second highest class. This
    *  is because we have constructed the ontology so that everything descends from a highest
    *  level ancestor (currently called clinical).
    * 
    *  @param ontologyURL The url for the ontology file
    *  @params prefix The prefix for the subject of interest
    *  @params root The root node for which we are seeking the first direct ancestor
    *  @params subject The subject for which we are seeking the first direct ancestor
    *  @return The OntClass for the first direct ancestor
    */
   public OntClass findSecondDegree(String ontologyURL, String prefix, String root, String subject) {
       OntClass returnClass = null;
       if (this.logger == null) {
          this.logger = Logger.getLogger ("org.renci.databridge.contrib.signature.SchizConnectClinical");
       }
      
       try {
          OntModel base = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
          base.read( ontologyURL, "TURTLE" );

          // create the reasoning model using the base
          OntModel inf = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF, base );

          // Get the class for the subject node
          OntClass subjectClass = inf.getOntClass( prefix + "/" + subject );
          returnClass = subjectClass;

          // subject class can be null if the subject is not found.
          if (subjectClass != null) {
             while (subjectClass.hasSuperClass() != false) {
                returnClass = subjectClass;
                subjectClass = subjectClass.getSuperClass();
                if (root.equalsIgnoreCase(subjectClass.getLabel(null)) == true) {
                   break;
                }
             }
          }
       } catch (Exception e) {
         this.logger.log (Level.SEVERE, "Caught in findSecondDegree: " + e.getMessage(),e);
       } finally {
         return returnClass;
       }
   }

   /*
    *  This function returns a hash map of <String, String> that has all of the direct subclasses
    *  of the supplied root node. The key is the label of the node, the values are all going to be
    *  false so that later code can set the ones that are found later can be set to true.
    *
    *  @param ontologyURL The url for the ontology file
    *  @params prefix The prefix for the root node of interest
    *  @params root The root node for which we are seeking all of the direct ancestors
    */

   HashMap <String, String> createSubjectHashMap(String ontologyURL, String prefix, String root) {
      HashMap <String, String> returnMap = new HashMap <String, String>();
      if (this.logger == null) {
          this.logger = Logger.getLogger ("org.renci.databridge.contrib.signature.SchizConnectClinical");
      }

      try {
          OntModel base = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
          Model retModel = base.read( ontologyURL, "TURTLE" );
          if (retModel == null) {
             this.logger.log (Level.SEVERE, "no return from base.read");
          }

          // create the reasoning model using the base
          OntModel inf = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF, base );

          // Get the class for the root node
          OntClass rootClass = inf.getOntClass( prefix + "/" + root ); 
          ExtendedIterator<OntClass> rootSubClasses = rootClass.listSubClasses(true);
          while (rootSubClasses.hasNext()) {
             OntClass thisClass = rootSubClasses.next();
             returnMap.put(thisClass.getLabel(null), "0");
          }
       } catch (Exception e) {
         this.logger.log (Level.SEVERE, "Caught in createSubjectHashMap: " + e.getMessage(),e);
       } finally {
         return returnMap;
       }
   }

    /**
     * Set a logger (e.g., parent's logger).
     */
    public void setLogger (Logger logger) {
        this.logger = logger;
    }
}

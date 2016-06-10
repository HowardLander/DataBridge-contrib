package org.renci.databridge.contrib.ingest.clinicaltrials;
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
public class ClinicalTrialFormatter implements MetadataFormatter{
    protected Logger logger = null;

    /**
     * @param bytes "Document" that implementor understands the format for. In this case
     *               it is an ArrayList of ClinicalTrialsJson objects.
     * @return the metadata elements from the bytes. 
     */
    @Override
    public List<MetadataObject> format (byte [] bytes) throws FormatterException {
       ArrayList<MetadataObject> objectList = new ArrayList<MetadataObject>();

       try {
           // First order of business: convert the byte array back to the structure.
           ArrayList<ClinicalTrialJson> trialList = ClinicalTrialJson.deserializeArrayList(bytes);
           this.logger.log (Level.INFO, "after deserializeArrayList " );

           // Now traverse the list of clinical trials.  For each one, allocate a MetadataObject
           // and fill out the Collection Transfer object.  Note that this metadata has neither
           // files or variables.
           int i = 0;
           for (ClinicalTrialJson thisClinicalTrial: trialList) {
              if (i % 100 == 0) {
                 this.logger.log (Level.INFO, "processing trialList " + i);
              }
              i++;
              MetadataObject thisMeta = new MetadataObject();
              CollectionTransferObject thisCTO = new CollectionTransferObject();
              thisCTO.setURL(thisClinicalTrial.getSourceURL());
              thisCTO.setTitle(ClinicalTrialJson.strJoin(thisClinicalTrial.getOfficialTitle(), false));
              thisCTO.setDescription(
                 ClinicalTrialJson.strJoin(thisClinicalTrial.getDetailedDescription(), true));
              thisCTO.setProducer(ClinicalTrialJson.strJoin(thisClinicalTrial.getStudySponsor(), true));
              thisCTO.setSubject(ClinicalTrialJson.strJoin(thisClinicalTrial.getBriefSummary(), true));
              ArrayList<String> keywordList = new ArrayList<String>(Arrays.asList(thisClinicalTrial.getDetailedDescription()));
              thisCTO.setKeywords(keywordList);
              HashMap<String, String> extra = new HashMap<String, String>();
              extra.put("hasDataMonitoringCommitte", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getHasDataMonitoringCommitte(), true));
              extra.put("investigators", ClinicalTrialJson.strJoin(thisClinicalTrial.getInvestigators(), true));
              extra.put("eligibilityCriteria", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getEligibilityCriteria(), true));
              extra.put("originalPrimaryOutcomeMeasures", 
                        ClinicalTrialJson.strJoin(thisClinicalTrial.getOriginalPrimaryOutcomeMeasures(), true));
              extra.put("collaborators", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getCollaborators(), true));
              extra.put("startDate", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getStartDate(), false));
              extra.put("informationProvidedBy", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getInformationProvidedBy(), true));
              extra.put("changeHistory", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getChangeHistory(), true));
              extra.put("originalSecondaryOutcomeMeasures", 
                       ClinicalTrialJson.strJoin(thisClinicalTrial.getOriginalSecondaryOutcomeMeasures(), true));
              extra.put("studyDesign", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getStudyDesign(), true));
              extra.put("briefTitle", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getBriefTitle(), true));
              extra.put("otherStudyIDNumbers", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getOtherStudyIDNumbers(), false));
              extra.put("nctNumber", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getNctNumber(), false));
              extra.put("studyPhase", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getStudyPhase(), true));
              extra.put("verificationDate", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getVerificationDate(), false));
              extra.put("ages", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getAges(), false));
              extra.put("completionDate", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getCompletionDate(), false));
              extra.put("currentPrimaryOutcomeMeasures", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getCurrentPrimaryOutcomeMeasures(), true));
              extra.put("intervention", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getIntervention(), true));
              extra.put("primaryCompletionDate", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getPrimaryCompletionDate(), false));
              extra.put("listedLocationCountries", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getListedLocationCountries(), true));
              extra.put("contacts", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getContacts(), true));
              extra.put("currentOtherOutcomeMeasures", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getCurrentOtherOutcomeMeasures(), true));
              extra.put("recruitmentStatus", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getRecruitmentStatus(), true));
              extra.put("condition", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getCondition(), true));
              extra.put("currentSecondaryOutcomeMeasures", 
                       ClinicalTrialJson.strJoin(thisClinicalTrial.getCurrentSecondaryOutcomeMeasures(), true));
              extra.put("acceptsHealthyVolunteers", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getAcceptsHealthyVolunteers(), false));
              extra.put("publications", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getPublications(), true));
              extra.put("gender", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getGender(), true));
              extra.put("lastUpdatedDate", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getLastUpdatedDate(), false));
              extra.put("firstReceivedDate", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getFirstReceivedDate(), false));
              extra.put("studyArms", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getStudyArms(), true));
              extra.put("responsibleParty", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getResponsibleParty(), true));
              extra.put("originalOtherOutcomeMeasures", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getOriginalOtherOutcomeMeasures(), true));
              extra.put("studyType", 
                         ClinicalTrialJson.strJoin(thisClinicalTrial.getStudyType(), true));
              thisCTO.setExtra(extra); 
              // add this to the list
              thisMeta.setCollectionTransferObject(thisCTO);
              objectList.add(thisMeta);
           }
       } catch (Exception e) {
         this.logger.log (Level.SEVERE, "Caught in format: " + e.getMessage(),e);
         throw new FormatterException("Caught in format: " + e.getMessage());
       }
       return objectList;
    }

  /**
   * @param input A class specific object that tells the function where to find the objects
   *              to turn into bytes. In this case, it's a string representing the directory
   *              containing the JSON clinical files
   * @return byte array representing the objects.
   */
  public byte[] getBytes(Object input) throws FormatterException {
     String inputDir = (String) input;
     byte [] bytes = null;
     ArrayList<ClinicalTrialJson> trialList = new ArrayList<ClinicalTrialJson>();

     try {
         File theFolder = new File(inputDir);
         // At the moment, we are assuming that the only files in the input dir
         // are the files we want.  We could relax that assumption by adding a pattern
         // parameter
         for (File thisFile : theFolder.listFiles()) {
            if (thisFile.isFile()) {
              // gets rid of . and .. as well as any dirs.
              // Read the file into memory
              ClinicalTrialJson thisJson =
                 ClinicalTrialJson.readJsonFromFile(thisFile.getAbsolutePath());
              trialList.add(thisJson);
            }
         }
         bytes = ClinicalTrialJson.serializeArrayList(trialList);
    
     } catch (Exception e) {
        this.logger.log (Level.SEVERE, "Problems with the inputDir:  " + inputDir, e);
        throw new FormatterException("Problems with the inputDir:  " + inputDir);
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

package org.renci.databridge.contrib.ingest.clinicaltrials;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.annotations.*;


/**
 * This class holds the data for a single clinical trial read from the json file
 * 
 * @author Howard Lander -RENCI (www.renci.org)
 * 
 */
public class ClinicalTrialJson {
    @SerializedName("SOURCE_URL") private String sourceURL;
    @SerializedName("Has Data Monitoring Committee") private String[] hasDataMonitoringCommitte;
    @SerializedName("Investigators") private String[] investigators;
    @SerializedName("Eligibility Criteria") private String[] eligibilityCriteria;
    @SerializedName("Original Primary Outcome Measures") private String[] originalPrimaryOutcomeMeasures;
    @SerializedName("Collaborators") private String[] collaborators;
    @SerializedName("Study Sponsor") private String[] studySponsor;
    @SerializedName("Start Date") private String[] startDate;
    @SerializedName("Information Provided By") private String[] informationProvidedBy;
    @SerializedName("Official Title") private String[] officialTitle;
    @SerializedName("Change History") private String[] changeHistory;
    @SerializedName("Original Secondary Outcome Measures") private String[] originalSecondaryOutcomeMeasures;
    @SerializedName("Brief Summary") private String[] briefSummary;
    @SerializedName("Study Design") private String[] studyDesign;
    @SerializedName("Brief Title") private String[] briefTitle;
    @SerializedName("Other Study ID Numbers") private String[] otherStudyIDNumbers;
    @SerializedName("NCT Number") private String[] nctNumber;
    @SerializedName("Study Phase") private String[] studyPhase;
    @SerializedName("Verification Date") private String[] verificationDate;
    @SerializedName("Ages") private String[] ages;
    @SerializedName("Completion Date") private String[] completionDate;
    @SerializedName("Current Primary Outcome Measures") private String[] currentPrimaryOutcomeMeasures;
    @SerializedName("Intervention") private String[] intervention;
    @SerializedName("Primary Completion Date") private String[] primaryCompletionDate;
    @SerializedName("Listed Location Countries") private String[] listedLocationCountries;
    @SerializedName("Contacts") private String[] contacts;
    @SerializedName("Current Other Outcome Measures") private String[] currentOtherOutcomeMeasures;
    @SerializedName("Recruitment Status") private String[] recruitmentStatus;
    @SerializedName("Condition") private String[] condition;
    @SerializedName("Detailed Description") private String[] detailedDescription;
    @SerializedName("Current Secondary Outcome Measures") private String[] currentSecondaryOutcomeMeasures;
    @SerializedName("Accepts Healthy Volunteers") private String[] acceptsHealthyVolunteers;
    @SerializedName("Publications *") private String[] publications;
    @SerializedName("Gender") private String[] gender;
    @SerializedName("Last Updated Date") private String[] lastUpdatedDate;
    @SerializedName("First Received Date") private String[] firstReceivedDate;
    @SerializedName("Study Arm (s)") private String[] studyArms;
    @SerializedName("Responsible Party") private String[] responsibleParty;
    @SerializedName("Original Other Outcome Measures") private String[] originalOtherOutcomeMeasures;
    @SerializedName("Study Type") private String[] studyType;
 
 /**
  * Get sourceURL.
  *
  * @return sourceURL as String.
  */
 public String getSourceURL()
 {
     return sourceURL;
 }
 
 /**
  * Set sourceURL.
  *
  * @param sourceURL the value to set.
  */
 public void setSourceURL(String sourceURL)
 {
     this.sourceURL = sourceURL;
 }
 
 /**
  * Get investigators.
  *
  * @return investigators as String[].
  */
 public String[] getInvestigators()
 {
     return investigators;
 }
 
 /**
  * Get investigators element at specified index.
  *
  * @param index the index.
  * @return investigators at index as String.
  */
 public String getInvestigators(int index)
 {
     return investigators[index];
 }
 
 /**
  * Set investigators.
  *
  * @param investigators the value to set.
  */
 public void setInvestigators(String[] investigators)
 {
     this.investigators = investigators;
 }
 
 /**
  * Set investigators at the specified index.
  *
  * @param investigators the value to set.
  * @param index the index.
  */
 public void setInvestigators(String investigators, int index)
 {
     this.investigators[index] = investigators;
 }
 
 /**
  * Get eligibilityCriteria.
  *
  * @return eligibilityCriteria as String[].
  */
 public String[] getEligibilityCriteria()
 {
     return eligibilityCriteria;
 }
 
 /**
  * Get eligibilityCriteria element at specified index.
  *
  * @param index the index.
  * @return eligibilityCriteria at index as String.
  */
 public String getEligibilityCriteria(int index)
 {
     return eligibilityCriteria[index];
 }
 
 /**
  * Set eligibilityCriteria.
  *
  * @param eligibilityCriteria the value to set.
  */
 public void setEligibilityCriteria(String[] eligibilityCriteria)
 {
     this.eligibilityCriteria = eligibilityCriteria;
 }
 
 /**
  * Set eligibilityCriteria at the specified index.
  *
  * @param eligibilityCriteria the value to set.
  * @param index the index.
  */
 public void setEligibilityCriteria(String eligibilityCriteria, int index)
 {
     this.eligibilityCriteria[index] = eligibilityCriteria;
 }
 
 /**
  * Get originalPrimaryOutcomeMeasures.
  *
  * @return originalPrimaryOutcomeMeasures as String[].
  */
 public String[] getOriginalPrimaryOutcomeMeasures()
 {
     return originalPrimaryOutcomeMeasures;
 }
 
 /**
  * Get originalPrimaryOutcomeMeasures element at specified index.
  *
  * @param index the index.
  * @return originalPrimaryOutcomeMeasures at index as String.
  */
 public String getOriginalPrimaryOutcomeMeasures(int index)
 {
     return originalPrimaryOutcomeMeasures[index];
 }
 
 /**
  * Set originalPrimaryOutcomeMeasures.
  *
  * @param originalPrimaryOutcomeMeasures the value to set.
  */
 public void setOriginalPrimaryOutcomeMeasures(String[] originalPrimaryOutcomeMeasures)
 {
     this.originalPrimaryOutcomeMeasures = originalPrimaryOutcomeMeasures;
 }
 
 /**
  * Set originalPrimaryOutcomeMeasures at the specified index.
  *
  * @param originalPrimaryOutcomeMeasures the value to set.
  * @param index the index.
  */
 public void setOriginalPrimaryOutcomeMeasures(String originalPrimaryOutcomeMeasures, int index)
 {
     this.originalPrimaryOutcomeMeasures[index] = originalPrimaryOutcomeMeasures;
 }
 
 /**
  * Get collaborators.
  *
  * @return collaborators as String[].
  */
 public String[] getCollaborators()
 {
     return collaborators;
 }
 
 /**
  * Get collaborators element at specified index.
  *
  * @param index the index.
  * @return collaborators at index as String.
  */
 public String getCollaborators(int index)
 {
     return collaborators[index];
 }
 
 /**
  * Set collaborators.
  *
  * @param collaborators the value to set.
  */
 public void setCollaborators(String[] collaborators)
 {
     this.collaborators = collaborators;
 }
 
 /**
  * Set collaborators at the specified index.
  *
  * @param collaborators the value to set.
  * @param index the index.
  */
 public void setCollaborators(String collaborators, int index)
 {
     this.collaborators[index] = collaborators;
 }
 
 /**
  * Get studySponsor.
  *
  * @return studySponsor as String[].
  */
 public String[] getStudySponsor()
 {
     return studySponsor;
 }
 
 /**
  * Get studySponsor element at specified index.
  *
  * @param index the index.
  * @return studySponsor at index as String.
  */
 public String getStudySponsor(int index)
 {
     return studySponsor[index];
 }
 
 /**
  * Set studySponsor.
  *
  * @param studySponsor the value to set.
  */
 public void setStudySponsor(String[] studySponsor)
 {
     this.studySponsor = studySponsor;
 }
 
 /**
  * Set studySponsor at the specified index.
  *
  * @param studySponsor the value to set.
  * @param index the index.
  */
 public void setStudySponsor(String studySponsor, int index)
 {
     this.studySponsor[index] = studySponsor;
 }
 
 /**
  * Get startDate.
  *
  * @return startDate as String[].
  */
 public String[] getStartDate()
 {
     return startDate;
 }
 
 /**
  * Get startDate element at specified index.
  *
  * @param index the index.
  * @return startDate at index as String.
  */
 public String getStartDate(int index)
 {
     return startDate[index];
 }
 
 /**
  * Set startDate.
  *
  * @param startDate the value to set.
  */
 public void setStartDate(String[] startDate)
 {
     this.startDate = startDate;
 }
 
 /**
  * Set startDate at the specified index.
  *
  * @param startDate the value to set.
  * @param index the index.
  */
 public void setStartDate(String startDate, int index)
 {
     this.startDate[index] = startDate;
 }
 
 /**
  * Get informationProvidedBy.
  *
  * @return informationProvidedBy as String[].
  */
 public String[] getInformationProvidedBy()
 {
     return informationProvidedBy;
 }
 
 /**
  * Get informationProvidedBy element at specified index.
  *
  * @param index the index.
  * @return informationProvidedBy at index as String.
  */
 public String getInformationProvidedBy(int index)
 {
     return informationProvidedBy[index];
 }
 
 /**
  * Set informationProvidedBy.
  *
  * @param informationProvidedBy the value to set.
  */
 public void setInformationProvidedBy(String[] informationProvidedBy)
 {
     this.informationProvidedBy = informationProvidedBy;
 }
 
 /**
  * Set informationProvidedBy at the specified index.
  *
  * @param informationProvidedBy the value to set.
  * @param index the index.
  */
 public void setInformationProvidedBy(String informationProvidedBy, int index)
 {
     this.informationProvidedBy[index] = informationProvidedBy;
 }
 
 /**
  * Get officialTitle.
  *
  * @return officialTitle as String[].
  */
 public String[] getOfficialTitle()
 {
     return officialTitle;
 }
 
 /**
  * Get officialTitle element at specified index.
  *
  * @param index the index.
  * @return officialTitle at index as String.
  */
 public String getOfficialTitle(int index)
 {
     return officialTitle[index];
 }
 
 /**
  * Set officialTitle.
  *
  * @param officialTitle the value to set.
  */
 public void setOfficialTitle(String[] officialTitle)
 {
     this.officialTitle = officialTitle;
 }
 
 /**
  * Set officialTitle at the specified index.
  *
  * @param officialTitle the value to set.
  * @param index the index.
  */
 public void setOfficialTitle(String officialTitle, int index)
 {
     this.officialTitle[index] = officialTitle;
 }
 
 /**
  * Get changeHistory.
  *
  * @return changeHistory as String[].
  */
 public String[] getChangeHistory()
 {
     return changeHistory;
 }
 
 /**
  * Get changeHistory element at specified index.
  *
  * @param index the index.
  * @return changeHistory at index as String.
  */
 public String getChangeHistory(int index)
 {
     return changeHistory[index];
 }
 
 /**
  * Set changeHistory.
  *
  * @param changeHistory the value to set.
  */
 public void setChangeHistory(String[] changeHistory)
 {
     this.changeHistory = changeHistory;
 }
 
 /**
  * Set changeHistory at the specified index.
  *
  * @param changeHistory the value to set.
  * @param index the index.
  */
 public void setChangeHistory(String changeHistory, int index)
 {
     this.changeHistory[index] = changeHistory;
 }
 
 /**
  * Get originalSecondaryOutcomeMeasures.
  *
  * @return originalSecondaryOutcomeMeasures as String[].
  */
 public String[] getOriginalSecondaryOutcomeMeasures()
 {
     return originalSecondaryOutcomeMeasures;
 }
 
 /**
  * Get originalSecondaryOutcomeMeasures element at specified index.
  *
  * @param index the index.
  * @return originalSecondaryOutcomeMeasures at index as String.
  */
 public String getOriginalSecondaryOutcomeMeasures(int index)
 {
     return originalSecondaryOutcomeMeasures[index];
 }
 
 /**
  * Set originalSecondaryOutcomeMeasures.
  *
  * @param originalSecondaryOutcomeMeasures the value to set.
  */
 public void setOriginalSecondaryOutcomeMeasures(String[] originalSecondaryOutcomeMeasures)
 {
     this.originalSecondaryOutcomeMeasures = originalSecondaryOutcomeMeasures;
 }
 
 /**
  * Set originalSecondaryOutcomeMeasures at the specified index.
  *
  * @param originalSecondaryOutcomeMeasures the value to set.
  * @param index the index.
  */
 public void setOriginalSecondaryOutcomeMeasures(String originalSecondaryOutcomeMeasures, int index)
 {
     this.originalSecondaryOutcomeMeasures[index] = originalSecondaryOutcomeMeasures;
 }
 
 /**
  * Get briefSummary.
  *
  * @return briefSummary as String[].
  */
 public String[] getBriefSummary()
 {
     return briefSummary;
 }
 
 /**
  * Get briefSummary element at specified index.
  *
  * @param index the index.
  * @return briefSummary at index as String.
  */
 public String getBriefSummary(int index)
 {
     return briefSummary[index];
 }
 
 /**
  * Set briefSummary.
  *
  * @param briefSummary the value to set.
  */
 public void setBriefSummary(String[] briefSummary)
 {
     this.briefSummary = briefSummary;
 }
 
 /**
  * Set briefSummary at the specified index.
  *
  * @param briefSummary the value to set.
  * @param index the index.
  */
 public void setBriefSummary(String briefSummary, int index)
 {
     this.briefSummary[index] = briefSummary;
 }
 
 /**
  * Get studyDesign.
  *
  * @return studyDesign as String[].
  */
 public String[] getStudyDesign()
 {
     return studyDesign;
 }
 
 /**
  * Get studyDesign element at specified index.
  *
  * @param index the index.
  * @return studyDesign at index as String.
  */
 public String getStudyDesign(int index)
 {
     return studyDesign[index];
 }
 
 /**
  * Set studyDesign.
  *
  * @param studyDesign the value to set.
  */
 public void setStudyDesign(String[] studyDesign)
 {
     this.studyDesign = studyDesign;
 }
 
 /**
  * Set studyDesign at the specified index.
  *
  * @param studyDesign the value to set.
  * @param index the index.
  */
 public void setStudyDesign(String studyDesign, int index)
 {
     this.studyDesign[index] = studyDesign;
 }
 
 /**
  * Get briefTitle.
  *
  * @return briefTitle as String[].
  */
 public String[] getBriefTitle()
 {
     return briefTitle;
 }
 
 /**
  * Get briefTitle element at specified index.
  *
  * @param index the index.
  * @return briefTitle at index as String.
  */
 public String getBriefTitle(int index)
 {
     return briefTitle[index];
 }
 
 /**
  * Get otherStudyIDNumbers.
  *
  * @return otherStudyIDNumbers as String[].
  */
 public String[] getOtherStudyIDNumbers()
 {
     return otherStudyIDNumbers;
 }
 
 /**
  * Get otherStudyIDNumbers element at specified index.
  *
  * @param index the index.
  * @return otherStudyIDNumbers at index as String.
  */
 public String getOtherStudyIDNumbers(int index)
 {
     return otherStudyIDNumbers[index];
 }
 
 /**
  * Set otherStudyIDNumbers.
  *
  * @param otherStudyIDNumbers the value to set.
  */
 public void setOtherStudyIDNumbers(String[] otherStudyIDNumbers)
 {
     this.otherStudyIDNumbers = otherStudyIDNumbers;
 }
 
 /**
  * Set otherStudyIDNumbers at the specified index.
  *
  * @param otherStudyIDNumbers the value to set.
  * @param index the index.
  */
 public void setOtherStudyIDNumbers(String otherStudyIDNumbers, int index)
 {
     this.otherStudyIDNumbers[index] = otherStudyIDNumbers;
 }
 
 /**
  * Get nctNumber.
  *
  * @return nctNumber as String[].
  */
 public String[] getNctNumber()
 {
     return nctNumber;
 }
 
 /**
  * Get nctNumber element at specified index.
  *
  * @param index the index.
  * @return nctNumber at index as String.
  */
 public String getNctNumber(int index)
 {
     return nctNumber[index];
 }
 
 /**
  * Set nctNumber.
  *
  * @param nctNumber the value to set.
  */
 public void setNctNumber(String[] nctNumber)
 {
     this.nctNumber = nctNumber;
 }
 
 /**
  * Set nctNumber at the specified index.
  *
  * @param nctNumber the value to set.
  * @param index the index.
  */
 public void setNctNumber(String nctNumber, int index)
 {
     this.nctNumber[index] = nctNumber;
 }
 
 /**
  * Get studyPhase.
  *
  * @return studyPhase as String[].
  */
 public String[] getStudyPhase()
 {
     return studyPhase;
 }
 
 /**
  * Get studyPhase element at specified index.
  *
  * @param index the index.
  * @return studyPhase at index as String.
  */
 public String getStudyPhase(int index)
 {
     return studyPhase[index];
 }
 
 /**
  * Set studyPhase.
  *
  * @param studyPhase the value to set.
  */
 public void setStudyPhase(String[] studyPhase)
 {
     this.studyPhase = studyPhase;
 }
 
 /**
  * Set studyPhase at the specified index.
  *
  * @param studyPhase the value to set.
  * @param index the index.
  */
 public void setStudyPhase(String studyPhase, int index)
 {
     this.studyPhase[index] = studyPhase;
 }
 
 /**
  * Get verificationDate.
  *
  * @return verificationDate as String[].
  */
 public String[] getVerificationDate()
 {
     return verificationDate;
 }
 
 /**
  * Get verificationDate element at specified index.
  *
  * @param index the index.
  * @return verificationDate at index as String.
  */
 public String getVerificationDate(int index)
 {
     return verificationDate[index];
 }
 
 
 /**
  * Get ages.
  *
  * @return ages as String[].
  */
 public String[] getAges()
 {
     return ages;
 }
 
 /**
  * Get ages element at specified index.
  *
  * @param index the index.
  * @return ages at index as String.
  */
 public String getAges(int index)
 {
     return ages[index];
 }
 
 /**
  * Set ages.
  *
  * @param ages the value to set.
  */
 public void setAges(String[] ages)
 {
     this.ages = ages;
 }
 
 /**
  * Set ages at the specified index.
  *
  * @param ages the value to set.
  * @param index the index.
  */
 public void setAges(String ages, int index)
 {
     this.ages[index] = ages;
 }
 
 /**
  * Get completionDate.
  *
  * @return completionDate as String[].
  */
 public String[] getCompletionDate()
 {
     return completionDate;
 }
 
 /**
  * Get completionDate element at specified index.
  *
  * @param index the index.
  * @return completionDate at index as String.
  */
 public String getCompletionDate(int index)
 {
     return completionDate[index];
 }
 
 /**
  * Set completionDate.
  *
  * @param completionDate the value to set.
  */
 public void setCompletionDate(String[] completionDate)
 {
     this.completionDate = completionDate;
 }
 
 /**
  * Set completionDate at the specified index.
  *
  * @param completionDate the value to set.
  * @param index the index.
  */
 public void setCompletionDate(String completionDate, int index)
 {
     this.completionDate[index] = completionDate;
 }
 
 /**
  * Get currentPrimaryOutcomeMeasures.
  *
  * @return currentPrimaryOutcomeMeasures as String[].
  */
 public String[] getCurrentPrimaryOutcomeMeasures()
 {
     return currentPrimaryOutcomeMeasures;
 }
 
 /**
  * Get currentPrimaryOutcomeMeasures element at specified index.
  *
  * @param index the index.
  * @return currentPrimaryOutcomeMeasures at index as String.
  */
 public String getCurrentPrimaryOutcomeMeasures(int index)
 {
     return currentPrimaryOutcomeMeasures[index];
 }
 
 /**
  * Set currentPrimaryOutcomeMeasures.
  *
  * @param currentPrimaryOutcomeMeasures the value to set.
  */
 public void setCurrentPrimaryOutcomeMeasures(String[] currentPrimaryOutcomeMeasures)
 {
     this.currentPrimaryOutcomeMeasures = currentPrimaryOutcomeMeasures;
 }
 
 /**
  * Set currentPrimaryOutcomeMeasures at the specified index.
  *
  * @param currentPrimaryOutcomeMeasures the value to set.
  * @param index the index.
  */
 public void setCurrentPrimaryOutcomeMeasures(String currentPrimaryOutcomeMeasures, int index)
 {
     this.currentPrimaryOutcomeMeasures[index] = currentPrimaryOutcomeMeasures;
 }
 
 /**
  * Get intervention.
  *
  * @return intervention as String[].
  */
 public String[] getIntervention()
 {
     return intervention;
 }
 
 /**
  * Get intervention element at specified index.
  *
  * @param index the index.
  * @return intervention at index as String.
  */
 public String getIntervention(int index)
 {
     return intervention[index];
 }
 
 /**
  * Set intervention.
  *
  * @param intervention the value to set.
  */
 public void setIntervention(String[] intervention)
 {
     this.intervention = intervention;
 }
 
 /**
  * Set intervention at the specified index.
  *
  * @param intervention the value to set.
  * @param index the index.
  */
 public void setIntervention(String intervention, int index)
 {
     this.intervention[index] = intervention;
 }
 
 /**
  * Get primaryCompletionDate.
  *
  * @return primaryCompletionDate as String[].
  */
 public String[] getPrimaryCompletionDate()
 {
     return primaryCompletionDate;
 }
 
 /**
  * Get primaryCompletionDate element at specified index.
  *
  * @param index the index.
  * @return primaryCompletionDate at index as String.
  */
 public String getPrimaryCompletionDate(int index)
 {
     return primaryCompletionDate[index];
 }
 
 /**
  * Set primaryCompletionDate.
  *
  * @param primaryCompletionDate the value to set.
  */
 public void setPrimaryCompletionDate(String[] primaryCompletionDate)
 {
     this.primaryCompletionDate = primaryCompletionDate;
 }
 
 /**
  * Set primaryCompletionDate at the specified index.
  *
  * @param primaryCompletionDate the value to set.
  * @param index the index.
  */
 public void setPrimaryCompletionDate(String primaryCompletionDate, int index)
 {
     this.primaryCompletionDate[index] = primaryCompletionDate;
 }
 
 /**
  * Get listedLocationCountries.
  *
  * @return listedLocationCountries as String[].
  */
 public String[] getListedLocationCountries()
 {
     return listedLocationCountries;
 }
 
 /**
  * Get listedLocationCountries element at specified index.
  *
  * @param index the index.
  * @return listedLocationCountries at index as String.
  */
 public String getListedLocationCountries(int index)
 {
     return listedLocationCountries[index];
 }
 
 /**
  * Set listedLocationCountries.
  *
  * @param listedLocationCountries the value to set.
  */
 public void setListedLocationCountries(String[] listedLocationCountries)
 {
     this.listedLocationCountries = listedLocationCountries;
 }
 
 /**
  * Set listedLocationCountries at the specified index.
  *
  * @param listedLocationCountries the value to set.
  * @param index the index.
  */
 public void setListedLocationCountries(String listedLocationCountries, int index)
 {
     this.listedLocationCountries[index] = listedLocationCountries;
 }
 
 /**
  * Get contacts.
  *
  * @return contacts as String[].
  */
 public String[] getContacts()
 {
     return contacts;
 }
 
 /**
  * Get contacts element at specified index.
  *
  * @param index the index.
  * @return contacts at index as String.
  */
 public String getContacts(int index)
 {
     return contacts[index];
 }
 
 /**
  * Set contacts.
  *
  * @param contacts the value to set.
  */
 public void setContacts(String[] contacts)
 {
     this.contacts = contacts;
 }
 
 /**
  * Set contacts at the specified index.
  *
  * @param contacts the value to set.
  * @param index the index.
  */
 public void setContacts(String contacts, int index)
 {
     this.contacts[index] = contacts;
 }
 
 /**
  * Get currentOtherOutcomeMeasures.
  *
  * @return currentOtherOutcomeMeasures as String[].
  */
 public String[] getCurrentOtherOutcomeMeasures()
 {
     return currentOtherOutcomeMeasures;
 }
 
 /**
  * Get currentOtherOutcomeMeasures element at specified index.
  *
  * @param index the index.
  * @return currentOtherOutcomeMeasures at index as String.
  */
 public String getCurrentOtherOutcomeMeasures(int index)
 {
     return currentOtherOutcomeMeasures[index];
 }
 
 /**
  * Set currentOtherOutcomeMeasures.
  *
  * @param currentOtherOutcomeMeasures the value to set.
  */
 public void setCurrentOtherOutcomeMeasures(String[] currentOtherOutcomeMeasures)
 {
     this.currentOtherOutcomeMeasures = currentOtherOutcomeMeasures;
 }
 
 /**
  * Set currentOtherOutcomeMeasures at the specified index.
  *
  * @param currentOtherOutcomeMeasures the value to set.
  * @param index the index.
  */
 public void setCurrentOtherOutcomeMeasures(String currentOtherOutcomeMeasures, int index)
 {
     this.currentOtherOutcomeMeasures[index] = currentOtherOutcomeMeasures;
 }
 
 /**
  * Get recruitmentStatus.
  *
  * @return recruitmentStatus as String[].
  */
 public String[] getRecruitmentStatus()
 {
     return recruitmentStatus;
 }
 
 /**
  * Get recruitmentStatus element at specified index.
  *
  * @param index the index.
  * @return recruitmentStatus at index as String.
  */
 public String getRecruitmentStatus(int index)
 {
     return recruitmentStatus[index];
 }
 
 /**
  * Set recruitmentStatus.
  *
  * @param recruitmentStatus the value to set.
  */
 public void setRecruitmentStatus(String[] recruitmentStatus)
 {
     this.recruitmentStatus = recruitmentStatus;
 }
 
 /**
  * Set recruitmentStatus at the specified index.
  *
  * @param recruitmentStatus the value to set.
  * @param index the index.
  */
 public void setRecruitmentStatus(String recruitmentStatus, int index)
 {
     this.recruitmentStatus[index] = recruitmentStatus;
 }
 
 /**
  * Get condition.
  *
  * @return condition as String[].
  */
 public String[] getCondition()
 {
     return condition;
 }
 
 /**
  * Get condition element at specified index.
  *
  * @param index the index.
  * @return condition at index as String.
  */
 public String getCondition(int index)
 {
     return condition[index];
 }
 
 /**
  * Set condition.
  *
  * @param condition the value to set.
  */
 public void setCondition(String[] condition)
 {
     this.condition = condition;
 }
 
 /**
  * Set condition at the specified index.
  *
  * @param condition the value to set.
  * @param index the index.
  */
 public void setCondition(String condition, int index)
 {
     this.condition[index] = condition;
 }
 
 /**
  * Get detailedDescription.
  *
  * @return detailedDescription as String[].
  */
 public String[] getDetailedDescription()
 {
     return detailedDescription;
 }
 
 /**
  * Get detailedDescription element at specified index.
  *
  * @param index the index.
  * @return detailedDescription at index as String.
  */
 public String getDetailedDescription(int index)
 {
     return detailedDescription[index];
 }
 
 /**
  * Set detailedDescription.
  *
  * @param detailedDescription the value to set.
  */
 public void setDetailedDescription(String[] detailedDescription)
 {
     this.detailedDescription = detailedDescription;
 }
 
 /**
  * Set detailedDescription at the specified index.
  *
  * @param detailedDescription the value to set.
  * @param index the index.
  */
 public void setDetailedDescription(String detailedDescription, int index)
 {
     this.detailedDescription[index] = detailedDescription;
 }
 
 /**
  * Get currentSecondaryOutcomeMeasures.
  *
  * @return currentSecondaryOutcomeMeasures as String[].
  */
 public String[] getCurrentSecondaryOutcomeMeasures()
 {
     return currentSecondaryOutcomeMeasures;
 }
 
 /**
  * Get currentSecondaryOutcomeMeasures element at specified index.
  *
  * @param index the index.
  * @return currentSecondaryOutcomeMeasures at index as String.
  */
 public String getCurrentSecondaryOutcomeMeasures(int index)
 {
     return currentSecondaryOutcomeMeasures[index];
 }
 
 /**
  * Set currentSecondaryOutcomeMeasures.
  *
  * @param currentSecondaryOutcomeMeasures the value to set.
  */
 public void setCurrentSecondaryOutcomeMeasures(String[] currentSecondaryOutcomeMeasures)
 {
     this.currentSecondaryOutcomeMeasures = currentSecondaryOutcomeMeasures;
 }
 
 /**
  * Set currentSecondaryOutcomeMeasures at the specified index.
  *
  * @param currentSecondaryOutcomeMeasures the value to set.
  * @param index the index.
  */
 public void setCurrentSecondaryOutcomeMeasures(String currentSecondaryOutcomeMeasures, int index)
 {
     this.currentSecondaryOutcomeMeasures[index] = currentSecondaryOutcomeMeasures;
 }
 
 /**
  * Get acceptsHealthyVolunteers.
  *
  * @return acceptsHealthyVolunteers as String[].
  */
 public String[] getAcceptsHealthyVolunteers()
 {
     return acceptsHealthyVolunteers;
 }
 
 /**
  * Get acceptsHealthyVolunteers element at specified index.
  *
  * @param index the index.
  * @return acceptsHealthyVolunteers at index as String.
  */
 public String getAcceptsHealthyVolunteers(int index)
 {
     return acceptsHealthyVolunteers[index];
 }
 
 /**
  * Set acceptsHealthyVolunteers.
  *
  * @param acceptsHealthyVolunteers the value to set.
  */
 public void setAcceptsHealthyVolunteers(String[] acceptsHealthyVolunteers)
 {
     this.acceptsHealthyVolunteers = acceptsHealthyVolunteers;
 }
 
 /**
  * Set acceptsHealthyVolunteers at the specified index.
  *
  * @param acceptsHealthyVolunteers the value to set.
  * @param index the index.
  */
 public void setAcceptsHealthyVolunteers(String acceptsHealthyVolunteers, int index)
 {
     this.acceptsHealthyVolunteers[index] = acceptsHealthyVolunteers;
 }
 
 /**
  * Get publications.
  *
  * @return publications as String[].
  */
 public String[] getPublications()
 {
     return publications;
 }
 
 /**
  * Get publications element at specified index.
  *
  * @param index the index.
  * @return publications at index as String.
  */
 public String getPublications(int index)
 {
     return publications[index];
 }
 
 /**
  * Set publications.
  *
  * @param publications the value to set.
  */
 public void setPublications(String[] publications)
 {
     this.publications = publications;
 }
 
 /**
  * Set publications at the specified index.
  *
  * @param publications the value to set.
  * @param index the index.
  */
 public void setPublications(String publications, int index)
 {
     this.publications[index] = publications;
 }
 
 /**
  * Get gender.
  *
  * @return gender as String[].
  */
 public String[] getGender()
 {
     return gender;
 }
 
 /**
  * Get gender element at specified index.
  *
  * @param index the index.
  * @return gender at index as String.
  */
 public String getGender(int index)
 {
     return gender[index];
 }
 
 /**
  * Set gender.
  *
  * @param gender the value to set.
  */
 public void setGender(String[] gender)
 {
     this.gender = gender;
 }
 
 /**
  * Set gender at the specified index.
  *
  * @param gender the value to set.
  * @param index the index.
  */
 public void setGender(String gender, int index)
 {
     this.gender[index] = gender;
 }
 
 /**
  * Get lastUpdatedDate.
  *
  * @return lastUpdatedDate as String[].
  */
 public String[] getLastUpdatedDate()
 {
     return lastUpdatedDate;
 }
 
 /**
  * Get lastUpdatedDate element at specified index.
  *
  * @param index the index.
  * @return lastUpdatedDate at index as String.
  */
 public String getLastUpdatedDate(int index)
 {
     return lastUpdatedDate[index];
 }
 
 /**
  * Set lastUpdatedDate.
  *
  * @param lastUpdatedDate the value to set.
  */
 public void setLastUpdatedDate(String[] lastUpdatedDate)
 {
     this.lastUpdatedDate = lastUpdatedDate;
 }
 
 /**
  * Set lastUpdatedDate at the specified index.
  *
  * @param lastUpdatedDate the value to set.
  * @param index the index.
  */
 public void setLastUpdatedDate(String lastUpdatedDate, int index)
 {
     this.lastUpdatedDate[index] = lastUpdatedDate;
 }
 
 /**
  * Get firstReceivedDate.
  *
  * @return firstReceivedDate as String[].
  */
 public String[] getFirstReceivedDate()
 {
     return firstReceivedDate;
 }
 
 /**
  * Get firstReceivedDate element at specified index.
  *
  * @param index the index.
  * @return firstReceivedDate at index as String.
  */
 public String getFirstReceivedDate(int index)
 {
     return firstReceivedDate[index];
 }
 
 /**
  * Set firstReceivedDate.
  *
  * @param firstReceivedDate the value to set.
  */
 public void setFirstReceivedDate(String[] firstReceivedDate)
 {
     this.firstReceivedDate = firstReceivedDate;
 }
 
 /**
  * Set firstReceivedDate at the specified index.
  *
  * @param firstReceivedDate the value to set.
  * @param index the index.
  */
 public void setFirstReceivedDate(String firstReceivedDate, int index)
 {
     this.firstReceivedDate[index] = firstReceivedDate;
 }
 
 /**
  * Get studyArms.
  *
  * @return studyArms as String[].
  */
 public String[] getStudyArms()
 {
     return studyArms;
 }
 
 /**
  * Get studyArms element at specified index.
  *
  * @param index the index.
  * @return studyArms at index as String.
  */
 public String getStudyArms(int index)
 {
     return studyArms[index];
 }
 
 /**
  * Set studyArms.
  *
  * @param studyArms the value to set.
  */
 public void setStudyArms(String[] studyArms)
 {
     this.studyArms = studyArms;
 }
 
 /**
  * Set studyArms at the specified index.
  *
  * @param studyArms the value to set.
  * @param index the index.
  */
 public void setStudyArms(String studyArms, int index)
 {
     this.studyArms[index] = studyArms;
 }
 
 /**
  * Get responsibleParty.
  *
  * @return responsibleParty as String[].
  */
 public String[] getResponsibleParty()
 {
     return responsibleParty;
 }
 
 /**
  * Get responsibleParty element at specified index.
  *
  * @param index the index.
  * @return responsibleParty at index as String.
  */
 public String getResponsibleParty(int index)
 {
     return responsibleParty[index];
 }
 
 /**
  * Set responsibleParty.
  *
  * @param responsibleParty the value to set.
  */
 public void setResponsibleParty(String[] responsibleParty)
 {
     this.responsibleParty = responsibleParty;
 }
 
 /**
  * Set responsibleParty at the specified index.
  *
  * @param responsibleParty the value to set.
  * @param index the index.
  */
 public void setResponsibleParty(String responsibleParty, int index)
 {
     this.responsibleParty[index] = responsibleParty;
 }
 
 /**
  * Get originalOtherOutcomeMeasures.
  *
  * @return originalOtherOutcomeMeasures as String[].
  */
 public String[] getOriginalOtherOutcomeMeasures()
 {
     return originalOtherOutcomeMeasures;
 }
 
 /**
  * Get originalOtherOutcomeMeasures element at specified index.
  *
  * @param index the index.
  * @return originalOtherOutcomeMeasures at index as String.
  */
 public String getOriginalOtherOutcomeMeasures(int index)
 {
     return originalOtherOutcomeMeasures[index];
 }
 
 /**
  * Set originalOtherOutcomeMeasures.
  *
  * @param originalOtherOutcomeMeasures the value to set.
  */
 public void setOriginalOtherOutcomeMeasures(String[] originalOtherOutcomeMeasures)
 {
     this.originalOtherOutcomeMeasures = originalOtherOutcomeMeasures;
 }
 
 /**
  * Set originalOtherOutcomeMeasures at the specified index.
  *
  * @param originalOtherOutcomeMeasures the value to set.
  * @param index the index.
  */
 public void setOriginalOtherOutcomeMeasures(String originalOtherOutcomeMeasures, int index)
 {
     this.originalOtherOutcomeMeasures[index] = originalOtherOutcomeMeasures;
 }
 
 /**
  * Get studyType.
  *
  * @return studyType as String[].
  */
 public String[] getStudyType()
 {
     return studyType;
 }
 
 /**
  * Get studyType element at specified index.
  *
  * @param index the index.
  * @return studyType at index as String.
  */
 public String getStudyType(int index)
 {
     return studyType[index];
 }
 
 /**
  * Set studyType.
  *
  * @param studyType the value to set.
  */
 public void setStudyType(String[] studyType)
 {
     this.studyType = studyType;
 }
 
 /**
  * Set studyType at the specified index.
  *
  * @param studyType the value to set.
  * @param index the index.
  */
 public void setStudyType(String studyType, int index)
 {
     this.studyType[index] = studyType;
 }
 
 /**
  * Get hasDataMonitoringCommitte.
  *
  * @return hasDataMonitoringCommitte as String[].
  */
 public String[] getHasDataMonitoringCommitte()
 {
     return hasDataMonitoringCommitte;
 }
 
 /**
  * Get hasDataMonitoringCommitte element at specified index.
  *
  * @param index the index.
  * @return hasDataMonitoringCommitte at index as String.
  */
 public String getHasDataMonitoringCommitte(int index)
 {
     return hasDataMonitoringCommitte[index];
 }
 
 /**
  * Set hasDataMonitoringCommitte.
  *
  * @param hasDataMonitoringCommitte the value to set.
  */
 public void setHasDataMonitoringCommitte(String[] hasDataMonitoringCommitte)
 {
     this.hasDataMonitoringCommitte = hasDataMonitoringCommitte;
 }
 
 /**
  * Set hasDataMonitoringCommitte at the specified index.
  *
  * @param hasDataMonitoringCommitte the value to set.
  * @param index the index.
  */
 public void setHasDataMonitoringCommitte(String hasDataMonitoringCommitte, int index)
 {
     this.hasDataMonitoringCommitte[index] = hasDataMonitoringCommitte;
 }
}

package com.kaleido.klinops.domain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Clinical Study and holds metadata about the study\n@author Mark Schreiber
 */
@ApiModel(description = "Represents a Clinical Study and holds metadata about the study\n@author Mark Schreiber")
@Entity
@Table(name = "clinical_study")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClinicalStudy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * The unique ID of the Study
     */
    
    @ApiModelProperty(value = "The unique ID of the Study")
    @Column(name = "study_identifier", unique = true)
    private String studyIdentifier;

    /**
     * The phase of the study
     */
    @ApiModelProperty(value = "The phase of the study")
    @Column(name = "phase")
    private String phase;

    /**
     * The status of the study
     */
    @ApiModelProperty(value = "The status of the study")
    @Column(name = "status")
    private String status;

    /**
     * Numbered Sequence of the Clinical Study
     */
    @ApiModelProperty(value = "Numbered Sequence of the Clinical Study")
    @Column(name = "sequence")
    private Integer sequence;

    /**
     * The year of the Study
     */
    @Min(value = 1900)
    @ApiModelProperty(value = "The year of the Study")
    @Column(name = "study_year")
    private Integer studyYear;

    /**
     * The name of the study
     */
    @Size(max = 500)
    @ApiModelProperty(value = "The name of the study")
    @Column(name = "name", length = 500)
    private String name;

    /**
     * Description of the study design
     */
    @ApiModelProperty(value = "Description of the study design")
    @Column(name = "design")
    private String design;

    /**
     * Number of Cohorts in the Study
     */
    @Min(value = 0)
    @ApiModelProperty(value = "Number of Cohorts in the Study")
    @Column(name = "number_of_cohorts")
    private Integer numberOfCohorts;

    /**
     * Number of subjects per cohort in the study design
     */
    @Min(value = 0)
    @ApiModelProperty(value = "Number of subjects per cohort in the study design")
    @Column(name = "intended_subjects_per_cohort")
    private Integer intendedSubjectsPerCohort;

    /**
     * The disease state of the study population
     */
    @ApiModelProperty(value = "The disease state of the study population")
    @Column(name = "population_disease_state")
    private String populationDiseaseState;

    /**
     * Minimum age for enrollment
     */
    @Min(value = 0)
    @ApiModelProperty(value = "Minimum age for enrollment")
    @Column(name = "minimum_age")
    private Integer minimumAge;

    /**
     * Maximum age for enrollment
     */
    @Min(value = 0)
    @ApiModelProperty(value = "Maximum age for enrollment")
    @Column(name = "maximum_age")
    private Integer maximumAge;

    /**
     * Number of subjects enrolled to date
     */
    @Min(value = 0)
    @ApiModelProperty(value = "Number of subjects enrolled to date")
    @Column(name = "subjects_enrolled")
    private Integer subjectsEnrolled;

    /**
     * Are females eligible for inclusion in the study
     */
    @ApiModelProperty(value = "Are females eligible for inclusion in the study")
    @Column(name = "females_eligible")
    private Boolean femalesEligible;

    /**
     * Are males eligible for inclusion in the study
     */
    @ApiModelProperty(value = "Are males eligible for inclusion in the study")
    @Column(name = "males_eligible")
    private Boolean malesEligible;

    /**
     * A short name for the study
     */
    @NotNull
    @Size(max = 128)
    @ApiModelProperty(value = "A short name for the study", required = true)
    @Column(name = "study_short_name", length = 128, nullable = false)
    private String studyShortName;

    /**
     * The project manager of the trial
     */
    @NotNull
    @ApiModelProperty(value = "The project manager of the trial", required = true)
    @Column(name = "project_manager", nullable = false)
    private String projectManager;

    /**
     * The principal physician attached to the trial
     */
    @ApiModelProperty(value = "The principal physician attached to the trial")
    @Column(name = "principal_physician")
    private String principalPhysician;

    /**
     * The Research Representative of the trial
     */
    @ApiModelProperty(value = "The Research Representative of the trial")
    @Column(name = "research_representative")
    private String researchRepresentative;

    /**
     * The analysis (statistics or data scientist) Representative
     */
    @ApiModelProperty(value = "The analysis (statistics or data scientist) Representative")
    @Column(name = "analysis_representative")
    private String analysisRepresentative;

    /**
     * The data manager assigned to the trial
     */
    @ApiModelProperty(value = "The data manager assigned to the trial")
    @Column(name = "data_manager")
    private String dataManager;

    /**
     * The study master file
     */
    @ApiModelProperty(value = "The study master file")
    @OneToOne
    @JoinColumn(unique = true)
    private TrialMasterFile masterFile;

    /**
     * The endpoints for a Study
     */
    @ApiModelProperty(value = "The endpoints for a Study")
    @OneToMany(mappedBy = "study")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StudyEndPoint> endPoints = new HashSet<>();

    /**
     * The products studied
     */
    @ApiModelProperty(value = "The products studied")
    @OneToMany(mappedBy = "study")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StudyProduct> studiedProducts = new HashSet<>();

    /**
     * The milestones of the study
     */
    @ApiModelProperty(value = "The milestones of the study")
    @OneToMany(mappedBy = "study")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StudyMilestone> mileStones = new HashSet<>();

    /**
     * The bio-analyses of this study
     */
    @ApiModelProperty(value = "The bio-analyses of this study")
    @OneToMany(mappedBy = "study")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BioAnalysis> bioAnalyses = new HashSet<>();

    /**
     * The data-analyses of this study
     */
    @ApiModelProperty(value = "The data-analyses of this study")
    @OneToMany(mappedBy = "study")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DataAnalysis> dataAnalyses = new HashSet<>();

    /**
     * The shipments from this study
     */
    @ApiModelProperty(value = "The shipments from this study")
    @OneToMany(mappedBy = "study")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Shipment> shipments = new HashSet<>();

    /**
     * The study samples from this study
     */
    @ApiModelProperty(value = "The study samples from this study")
    @OneToMany(mappedBy = "study")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StudySample> studySamples = new HashSet<>();

    /**
     * The investigators on the study
     */
    @ApiModelProperty(value = "The investigators on the study")
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "clinical_study_investigators",
               joinColumns = @JoinColumn(name = "clinical_study_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "investigators_id", referencedColumnName = "id"))
    private Set<PrincipalInvestigator> investigators = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudyIdentifier() {
        return studyIdentifier;
    }

    public ClinicalStudy studyIdentifier(String studyIdentifier) {
        this.studyIdentifier = studyIdentifier;
        return this;
    }

    public void setStudyIdentifier(String studyIdentifier) {
        this.studyIdentifier = studyIdentifier;
    }

    public String getPhase() {
        return phase;
    }

    public ClinicalStudy phase(String phase) {
        this.phase = phase;
        return this;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getStatus() {
        return status;
    }

    public ClinicalStudy status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSequence() {
        return sequence;
    }

    public ClinicalStudy sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getStudyYear() {
        return studyYear;
    }

    public ClinicalStudy studyYear(Integer studyYear) {
        this.studyYear = studyYear;
        return this;
    }

    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }

    public String getName() {
        return name;
    }

    public ClinicalStudy name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesign() {
        return design;
    }

    public ClinicalStudy design(String design) {
        this.design = design;
        return this;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public Integer getNumberOfCohorts() {
        return numberOfCohorts;
    }

    public ClinicalStudy numberOfCohorts(Integer numberOfCohorts) {
        this.numberOfCohorts = numberOfCohorts;
        return this;
    }

    public void setNumberOfCohorts(Integer numberOfCohorts) {
        this.numberOfCohorts = numberOfCohorts;
    }

    public Integer getIntendedSubjectsPerCohort() {
        return intendedSubjectsPerCohort;
    }

    public ClinicalStudy intendedSubjectsPerCohort(Integer intendedSubjectsPerCohort) {
        this.intendedSubjectsPerCohort = intendedSubjectsPerCohort;
        return this;
    }

    public void setIntendedSubjectsPerCohort(Integer intendedSubjectsPerCohort) {
        this.intendedSubjectsPerCohort = intendedSubjectsPerCohort;
    }

    public String getPopulationDiseaseState() {
        return populationDiseaseState;
    }

    public ClinicalStudy populationDiseaseState(String populationDiseaseState) {
        this.populationDiseaseState = populationDiseaseState;
        return this;
    }

    public void setPopulationDiseaseState(String populationDiseaseState) {
        this.populationDiseaseState = populationDiseaseState;
    }

    public Integer getMinimumAge() {
        return minimumAge;
    }

    public ClinicalStudy minimumAge(Integer minimumAge) {
        this.minimumAge = minimumAge;
        return this;
    }

    public void setMinimumAge(Integer minimumAge) {
        this.minimumAge = minimumAge;
    }

    public Integer getMaximumAge() {
        return maximumAge;
    }

    public ClinicalStudy maximumAge(Integer maximumAge) {
        this.maximumAge = maximumAge;
        return this;
    }

    public void setMaximumAge(Integer maximumAge) {
        this.maximumAge = maximumAge;
    }

    public Integer getSubjectsEnrolled() {
        return subjectsEnrolled;
    }

    public ClinicalStudy subjectsEnrolled(Integer subjectsEnrolled) {
        this.subjectsEnrolled = subjectsEnrolled;
        return this;
    }

    public void setSubjectsEnrolled(Integer subjectsEnrolled) {
        this.subjectsEnrolled = subjectsEnrolled;
    }

    public Boolean isFemalesEligible() {
        return femalesEligible;
    }

    public ClinicalStudy femalesEligible(Boolean femalesEligible) {
        this.femalesEligible = femalesEligible;
        return this;
    }

    public void setFemalesEligible(Boolean femalesEligible) {
        this.femalesEligible = femalesEligible;
    }

    public Boolean isMalesEligible() {
        return malesEligible;
    }

    public ClinicalStudy malesEligible(Boolean malesEligible) {
        this.malesEligible = malesEligible;
        return this;
    }

    public void setMalesEligible(Boolean malesEligible) {
        this.malesEligible = malesEligible;
    }

    public String getStudyShortName() {
        return studyShortName;
    }

    public ClinicalStudy studyShortName(String studyShortName) {
        this.studyShortName = studyShortName;
        return this;
    }

    public void setStudyShortName(String studyShortName) {
        this.studyShortName = studyShortName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public ClinicalStudy projectManager(String projectManager) {
        this.projectManager = projectManager;
        return this;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getPrincipalPhysician() {
        return principalPhysician;
    }

    public ClinicalStudy principalPhysician(String principalPhysician) {
        this.principalPhysician = principalPhysician;
        return this;
    }

    public void setPrincipalPhysician(String principalPhysician) {
        this.principalPhysician = principalPhysician;
    }

    public String getResearchRepresentative() {
        return researchRepresentative;
    }

    public ClinicalStudy researchRepresentative(String researchRepresentative) {
        this.researchRepresentative = researchRepresentative;
        return this;
    }

    public void setResearchRepresentative(String researchRepresentative) {
        this.researchRepresentative = researchRepresentative;
    }

    public String getAnalysisRepresentative() {
        return analysisRepresentative;
    }

    public ClinicalStudy analysisRepresentative(String analysisRepresentative) {
        this.analysisRepresentative = analysisRepresentative;
        return this;
    }

    public void setAnalysisRepresentative(String analysisRepresentative) {
        this.analysisRepresentative = analysisRepresentative;
    }

    public String getDataManager() {
        return dataManager;
    }

    public ClinicalStudy dataManager(String dataManager) {
        this.dataManager = dataManager;
        return this;
    }

    public void setDataManager(String dataManager) {
        this.dataManager = dataManager;
    }

    public TrialMasterFile getMasterFile() {
        return masterFile;
    }

    public ClinicalStudy masterFile(TrialMasterFile trialMasterFile) {
        this.masterFile = trialMasterFile;
        return this;
    }

    public void setMasterFile(TrialMasterFile trialMasterFile) {
        this.masterFile = trialMasterFile;
    }

    public Set<StudyEndPoint> getEndPoints() {
        return endPoints;
    }

    public ClinicalStudy endPoints(Set<StudyEndPoint> studyEndPoints) {
        this.endPoints = studyEndPoints;
        return this;
    }

    public ClinicalStudy addEndPoints(StudyEndPoint studyEndPoint) {
        this.endPoints.add(studyEndPoint);
        studyEndPoint.setStudy(this);
        return this;
    }

    public ClinicalStudy removeEndPoints(StudyEndPoint studyEndPoint) {
        this.endPoints.remove(studyEndPoint);
        studyEndPoint.setStudy(null);
        return this;
    }

    public void setEndPoints(Set<StudyEndPoint> studyEndPoints) {
        this.endPoints = studyEndPoints;
    }

    public Set<StudyProduct> getStudiedProducts() {
        return studiedProducts;
    }

    public ClinicalStudy studiedProducts(Set<StudyProduct> studyProducts) {
        this.studiedProducts = studyProducts;
        return this;
    }

    public ClinicalStudy addStudiedProducts(StudyProduct studyProduct) {
        this.studiedProducts.add(studyProduct);
        studyProduct.setStudy(this);
        return this;
    }

    public ClinicalStudy removeStudiedProducts(StudyProduct studyProduct) {
        this.studiedProducts.remove(studyProduct);
        studyProduct.setStudy(null);
        return this;
    }

    public void setStudiedProducts(Set<StudyProduct> studyProducts) {
        this.studiedProducts = studyProducts;
    }

    public Set<StudyMilestone> getMileStones() {
        return mileStones;
    }

    public ClinicalStudy mileStones(Set<StudyMilestone> studyMilestones) {
        this.mileStones = studyMilestones;
        return this;
    }

    public ClinicalStudy addMileStones(StudyMilestone studyMilestone) {
        this.mileStones.add(studyMilestone);
        studyMilestone.setStudy(this);
        return this;
    }

    public ClinicalStudy removeMileStones(StudyMilestone studyMilestone) {
        this.mileStones.remove(studyMilestone);
        studyMilestone.setStudy(null);
        return this;
    }

    public void setMileStones(Set<StudyMilestone> studyMilestones) {
        this.mileStones = studyMilestones;
    }

    public Set<BioAnalysis> getBioAnalyses() {
        return bioAnalyses;
    }

    public ClinicalStudy bioAnalyses(Set<BioAnalysis> bioAnalyses) {
        this.bioAnalyses = bioAnalyses;
        return this;
    }

    public ClinicalStudy addBioAnalyses(BioAnalysis bioAnalysis) {
        this.bioAnalyses.add(bioAnalysis);
        bioAnalysis.setStudy(this);
        return this;
    }

    public ClinicalStudy removeBioAnalyses(BioAnalysis bioAnalysis) {
        this.bioAnalyses.remove(bioAnalysis);
        bioAnalysis.setStudy(null);
        return this;
    }

    public void setBioAnalyses(Set<BioAnalysis> bioAnalyses) {
        this.bioAnalyses = bioAnalyses;
    }

    public Set<DataAnalysis> getDataAnalyses() {
        return dataAnalyses;
    }

    public ClinicalStudy dataAnalyses(Set<DataAnalysis> dataAnalyses) {
        this.dataAnalyses = dataAnalyses;
        return this;
    }

    public ClinicalStudy addDataAnalyses(DataAnalysis dataAnalysis) {
        this.dataAnalyses.add(dataAnalysis);
        dataAnalysis.setStudy(this);
        return this;
    }

    public ClinicalStudy removeDataAnalyses(DataAnalysis dataAnalysis) {
        this.dataAnalyses.remove(dataAnalysis);
        dataAnalysis.setStudy(null);
        return this;
    }

    public void setDataAnalyses(Set<DataAnalysis> dataAnalyses) {
        this.dataAnalyses = dataAnalyses;
    }

    public Set<Shipment> getShipments() {
        return shipments;
    }

    public ClinicalStudy shipments(Set<Shipment> shipments) {
        this.shipments = shipments;
        return this;
    }

    public ClinicalStudy addShipments(Shipment shipment) {
        this.shipments.add(shipment);
        shipment.setStudy(this);
        return this;
    }

    public ClinicalStudy removeShipments(Shipment shipment) {
        this.shipments.remove(shipment);
        shipment.setStudy(null);
        return this;
    }

    public void setShipments(Set<Shipment> shipments) {
        this.shipments = shipments;
    }

    public Set<StudySample> getStudySamples() {
        return studySamples;
    }

    public ClinicalStudy studySamples(Set<StudySample> studySamples) {
        this.studySamples = studySamples;
        return this;
    }

    public ClinicalStudy addStudySamples(StudySample studySample) {
        this.studySamples.add(studySample);
        studySample.setStudy(this);
        return this;
    }

    public ClinicalStudy removeStudySamples(StudySample studySample) {
        this.studySamples.remove(studySample);
        studySample.setStudy(null);
        return this;
    }

    public void setStudySamples(Set<StudySample> studySamples) {
        this.studySamples = studySamples;
    }

    public Set<PrincipalInvestigator> getInvestigators() {
        return investigators;
    }

    public ClinicalStudy investigators(Set<PrincipalInvestigator> principalInvestigators) {
        this.investigators = principalInvestigators;
        return this;
    }

    public ClinicalStudy addInvestigators(PrincipalInvestigator principalInvestigator) {
        this.investigators.add(principalInvestigator);
        principalInvestigator.getStudies().add(this);
        return this;
    }

    public ClinicalStudy removeInvestigators(PrincipalInvestigator principalInvestigator) {
        this.investigators.remove(principalInvestigator);
        principalInvestigator.getStudies().remove(this);
        return this;
    }

    public void setInvestigators(Set<PrincipalInvestigator> principalInvestigators) {
        this.investigators = principalInvestigators;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClinicalStudy)) {
            return false;
        }
        return id != null && id.equals(((ClinicalStudy) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClinicalStudy{" +
            "id=" + getId() +
            ", studyIdentifier='" + getStudyIdentifier() + "'" +
            ", phase='" + getPhase() + "'" +
            ", status='" + getStatus() + "'" +
            ", sequence=" + getSequence() +
            ", studyYear=" + getStudyYear() +
            ", name='" + getName() + "'" +
            ", design='" + getDesign() + "'" +
            ", numberOfCohorts=" + getNumberOfCohorts() +
            ", intendedSubjectsPerCohort=" + getIntendedSubjectsPerCohort() +
            ", populationDiseaseState='" + getPopulationDiseaseState() + "'" +
            ", minimumAge=" + getMinimumAge() +
            ", maximumAge=" + getMaximumAge() +
            ", subjectsEnrolled=" + getSubjectsEnrolled() +
            ", femalesEligible='" + isFemalesEligible() + "'" +
            ", malesEligible='" + isMalesEligible() + "'" +
            ", studyShortName='" + getStudyShortName() + "'" +
            ", projectManager='" + getProjectManager() + "'" +
            ", principalPhysician='" + getPrincipalPhysician() + "'" +
            ", researchRepresentative='" + getResearchRepresentative() + "'" +
            ", analysisRepresentative='" + getAnalysisRepresentative() + "'" +
            ", dataManager='" + getDataManager() + "'" +
            "}";
    }
}

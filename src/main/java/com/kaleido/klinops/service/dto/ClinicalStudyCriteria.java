/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.kaleido.klinops.domain.ClinicalStudy} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.ClinicalStudyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clinical-studies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClinicalStudyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter studyIdentifier;

    private StringFilter phase;

    private StringFilter status;

    private IntegerFilter sequence;

    private IntegerFilter studyYear;

    private StringFilter name;

    private StringFilter design;

    private IntegerFilter numberOfCohorts;

    private IntegerFilter intendedSubjectsPerCohort;

    private StringFilter populationDiseaseState;

    private IntegerFilter minimumAge;

    private IntegerFilter maximumAge;

    private IntegerFilter subjectsEnrolled;

    private BooleanFilter femalesEligible;

    private BooleanFilter malesEligible;

    private StringFilter studyShortName;

    private StringFilter projectManager;

    private StringFilter principalPhysician;

    private StringFilter researchRepresentative;

    private StringFilter analysisRepresentative;

    private StringFilter dataManager;

    private LongFilter masterFileId;

    private LongFilter endPointsId;

    private LongFilter studiedProductsId;

    private LongFilter mileStonesId;

    private LongFilter bioAnalysesId;

    private LongFilter dataAnalysesId;

    private LongFilter shipmentsId;

    private LongFilter studySamplesId;

    private LongFilter investigatorsId;

    public ClinicalStudyCriteria(){
    }

    public ClinicalStudyCriteria(ClinicalStudyCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.studyIdentifier = other.studyIdentifier == null ? null : other.studyIdentifier.copy();
        this.phase = other.phase == null ? null : other.phase.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.sequence = other.sequence == null ? null : other.sequence.copy();
        this.studyYear = other.studyYear == null ? null : other.studyYear.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.design = other.design == null ? null : other.design.copy();
        this.numberOfCohorts = other.numberOfCohorts == null ? null : other.numberOfCohorts.copy();
        this.intendedSubjectsPerCohort = other.intendedSubjectsPerCohort == null ? null : other.intendedSubjectsPerCohort.copy();
        this.populationDiseaseState = other.populationDiseaseState == null ? null : other.populationDiseaseState.copy();
        this.minimumAge = other.minimumAge == null ? null : other.minimumAge.copy();
        this.maximumAge = other.maximumAge == null ? null : other.maximumAge.copy();
        this.subjectsEnrolled = other.subjectsEnrolled == null ? null : other.subjectsEnrolled.copy();
        this.femalesEligible = other.femalesEligible == null ? null : other.femalesEligible.copy();
        this.malesEligible = other.malesEligible == null ? null : other.malesEligible.copy();
        this.studyShortName = other.studyShortName == null ? null : other.studyShortName.copy();
        this.projectManager = other.projectManager == null ? null : other.projectManager.copy();
        this.principalPhysician = other.principalPhysician == null ? null : other.principalPhysician.copy();
        this.researchRepresentative = other.researchRepresentative == null ? null : other.researchRepresentative.copy();
        this.analysisRepresentative = other.analysisRepresentative == null ? null : other.analysisRepresentative.copy();
        this.dataManager = other.dataManager == null ? null : other.dataManager.copy();
        this.masterFileId = other.masterFileId == null ? null : other.masterFileId.copy();
        this.endPointsId = other.endPointsId == null ? null : other.endPointsId.copy();
        this.studiedProductsId = other.studiedProductsId == null ? null : other.studiedProductsId.copy();
        this.mileStonesId = other.mileStonesId == null ? null : other.mileStonesId.copy();
        this.bioAnalysesId = other.bioAnalysesId == null ? null : other.bioAnalysesId.copy();
        this.dataAnalysesId = other.dataAnalysesId == null ? null : other.dataAnalysesId.copy();
        this.shipmentsId = other.shipmentsId == null ? null : other.shipmentsId.copy();
        this.studySamplesId = other.studySamplesId == null ? null : other.studySamplesId.copy();
        this.investigatorsId = other.investigatorsId == null ? null : other.investigatorsId.copy();
    }

    @Override
    public ClinicalStudyCriteria copy() {
        return new ClinicalStudyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStudyIdentifier() {
        return studyIdentifier;
    }

    public void setStudyIdentifier(StringFilter studyIdentifier) {
        this.studyIdentifier = studyIdentifier;
    }

    public StringFilter getPhase() {
        return phase;
    }

    public void setPhase(StringFilter phase) {
        this.phase = phase;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public IntegerFilter getSequence() {
        return sequence;
    }

    public void setSequence(IntegerFilter sequence) {
        this.sequence = sequence;
    }

    public IntegerFilter getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(IntegerFilter studyYear) {
        this.studyYear = studyYear;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDesign() {
        return design;
    }

    public void setDesign(StringFilter design) {
        this.design = design;
    }

    public IntegerFilter getNumberOfCohorts() {
        return numberOfCohorts;
    }

    public void setNumberOfCohorts(IntegerFilter numberOfCohorts) {
        this.numberOfCohorts = numberOfCohorts;
    }

    public IntegerFilter getIntendedSubjectsPerCohort() {
        return intendedSubjectsPerCohort;
    }

    public void setIntendedSubjectsPerCohort(IntegerFilter intendedSubjectsPerCohort) {
        this.intendedSubjectsPerCohort = intendedSubjectsPerCohort;
    }

    public StringFilter getPopulationDiseaseState() {
        return populationDiseaseState;
    }

    public void setPopulationDiseaseState(StringFilter populationDiseaseState) {
        this.populationDiseaseState = populationDiseaseState;
    }

    public IntegerFilter getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(IntegerFilter minimumAge) {
        this.minimumAge = minimumAge;
    }

    public IntegerFilter getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(IntegerFilter maximumAge) {
        this.maximumAge = maximumAge;
    }

    public IntegerFilter getSubjectsEnrolled() {
        return subjectsEnrolled;
    }

    public void setSubjectsEnrolled(IntegerFilter subjectsEnrolled) {
        this.subjectsEnrolled = subjectsEnrolled;
    }

    public BooleanFilter getFemalesEligible() {
        return femalesEligible;
    }

    public void setFemalesEligible(BooleanFilter femalesEligible) {
        this.femalesEligible = femalesEligible;
    }

    public BooleanFilter getMalesEligible() {
        return malesEligible;
    }

    public void setMalesEligible(BooleanFilter malesEligible) {
        this.malesEligible = malesEligible;
    }

    public StringFilter getStudyShortName() {
        return studyShortName;
    }

    public void setStudyShortName(StringFilter studyShortName) {
        this.studyShortName = studyShortName;
    }

    public StringFilter getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(StringFilter projectManager) {
        this.projectManager = projectManager;
    }

    public StringFilter getPrincipalPhysician() {
        return principalPhysician;
    }

    public void setPrincipalPhysician(StringFilter principalPhysician) {
        this.principalPhysician = principalPhysician;
    }

    public StringFilter getResearchRepresentative() {
        return researchRepresentative;
    }

    public void setResearchRepresentative(StringFilter researchRepresentative) {
        this.researchRepresentative = researchRepresentative;
    }

    public StringFilter getAnalysisRepresentative() {
        return analysisRepresentative;
    }

    public void setAnalysisRepresentative(StringFilter analysisRepresentative) {
        this.analysisRepresentative = analysisRepresentative;
    }

    public StringFilter getDataManager() {
        return dataManager;
    }

    public void setDataManager(StringFilter dataManager) {
        this.dataManager = dataManager;
    }

    public LongFilter getMasterFileId() {
        return masterFileId;
    }

    public void setMasterFileId(LongFilter masterFileId) {
        this.masterFileId = masterFileId;
    }

    public LongFilter getEndPointsId() {
        return endPointsId;
    }

    public void setEndPointsId(LongFilter endPointsId) {
        this.endPointsId = endPointsId;
    }

    public LongFilter getStudiedProductsId() {
        return studiedProductsId;
    }

    public void setStudiedProductsId(LongFilter studiedProductsId) {
        this.studiedProductsId = studiedProductsId;
    }

    public LongFilter getMileStonesId() {
        return mileStonesId;
    }

    public void setMileStonesId(LongFilter mileStonesId) {
        this.mileStonesId = mileStonesId;
    }

    public LongFilter getBioAnalysesId() {
        return bioAnalysesId;
    }

    public void setBioAnalysesId(LongFilter bioAnalysesId) {
        this.bioAnalysesId = bioAnalysesId;
    }

    public LongFilter getDataAnalysesId() {
        return dataAnalysesId;
    }

    public void setDataAnalysesId(LongFilter dataAnalysesId) {
        this.dataAnalysesId = dataAnalysesId;
    }

    public LongFilter getShipmentsId() {
        return shipmentsId;
    }

    public void setShipmentsId(LongFilter shipmentsId) {
        this.shipmentsId = shipmentsId;
    }

    public LongFilter getStudySamplesId() {
        return studySamplesId;
    }

    public void setStudySamplesId(LongFilter studySamplesId) {
        this.studySamplesId = studySamplesId;
    }

    public LongFilter getInvestigatorsId() {
        return investigatorsId;
    }

    public void setInvestigatorsId(LongFilter investigatorsId) {
        this.investigatorsId = investigatorsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClinicalStudyCriteria that = (ClinicalStudyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(studyIdentifier, that.studyIdentifier) &&
            Objects.equals(phase, that.phase) &&
            Objects.equals(status, that.status) &&
            Objects.equals(sequence, that.sequence) &&
            Objects.equals(studyYear, that.studyYear) &&
            Objects.equals(name, that.name) &&
            Objects.equals(design, that.design) &&
            Objects.equals(numberOfCohorts, that.numberOfCohorts) &&
            Objects.equals(intendedSubjectsPerCohort, that.intendedSubjectsPerCohort) &&
            Objects.equals(populationDiseaseState, that.populationDiseaseState) &&
            Objects.equals(minimumAge, that.minimumAge) &&
            Objects.equals(maximumAge, that.maximumAge) &&
            Objects.equals(subjectsEnrolled, that.subjectsEnrolled) &&
            Objects.equals(femalesEligible, that.femalesEligible) &&
            Objects.equals(malesEligible, that.malesEligible) &&
            Objects.equals(studyShortName, that.studyShortName) &&
            Objects.equals(projectManager, that.projectManager) &&
            Objects.equals(principalPhysician, that.principalPhysician) &&
            Objects.equals(researchRepresentative, that.researchRepresentative) &&
            Objects.equals(analysisRepresentative, that.analysisRepresentative) &&
            Objects.equals(dataManager, that.dataManager) &&
            Objects.equals(masterFileId, that.masterFileId) &&
            Objects.equals(endPointsId, that.endPointsId) &&
            Objects.equals(studiedProductsId, that.studiedProductsId) &&
            Objects.equals(mileStonesId, that.mileStonesId) &&
            Objects.equals(bioAnalysesId, that.bioAnalysesId) &&
            Objects.equals(dataAnalysesId, that.dataAnalysesId) &&
            Objects.equals(shipmentsId, that.shipmentsId) &&
            Objects.equals(studySamplesId, that.studySamplesId) &&
            Objects.equals(investigatorsId, that.investigatorsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        studyIdentifier,
        phase,
        status,
        sequence,
        studyYear,
        name,
        design,
        numberOfCohorts,
        intendedSubjectsPerCohort,
        populationDiseaseState,
        minimumAge,
        maximumAge,
        subjectsEnrolled,
        femalesEligible,
        malesEligible,
        studyShortName,
        projectManager,
        principalPhysician,
        researchRepresentative,
        analysisRepresentative,
        dataManager,
        masterFileId,
        endPointsId,
        studiedProductsId,
        mileStonesId,
        bioAnalysesId,
        dataAnalysesId,
        shipmentsId,
        studySamplesId,
        investigatorsId
        );
    }

    @Override
    public String toString() {
        return "ClinicalStudyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (studyIdentifier != null ? "studyIdentifier=" + studyIdentifier + ", " : "") +
                (phase != null ? "phase=" + phase + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (sequence != null ? "sequence=" + sequence + ", " : "") +
                (studyYear != null ? "studyYear=" + studyYear + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (design != null ? "design=" + design + ", " : "") +
                (numberOfCohorts != null ? "numberOfCohorts=" + numberOfCohorts + ", " : "") +
                (intendedSubjectsPerCohort != null ? "intendedSubjectsPerCohort=" + intendedSubjectsPerCohort + ", " : "") +
                (populationDiseaseState != null ? "populationDiseaseState=" + populationDiseaseState + ", " : "") +
                (minimumAge != null ? "minimumAge=" + minimumAge + ", " : "") +
                (maximumAge != null ? "maximumAge=" + maximumAge + ", " : "") +
                (subjectsEnrolled != null ? "subjectsEnrolled=" + subjectsEnrolled + ", " : "") +
                (femalesEligible != null ? "femalesEligible=" + femalesEligible + ", " : "") +
                (malesEligible != null ? "malesEligible=" + malesEligible + ", " : "") +
                (studyShortName != null ? "studyShortName=" + studyShortName + ", " : "") +
                (projectManager != null ? "projectManager=" + projectManager + ", " : "") +
                (principalPhysician != null ? "principalPhysician=" + principalPhysician + ", " : "") +
                (researchRepresentative != null ? "researchRepresentative=" + researchRepresentative + ", " : "") +
                (analysisRepresentative != null ? "analysisRepresentative=" + analysisRepresentative + ", " : "") +
                (dataManager != null ? "dataManager=" + dataManager + ", " : "") +
                (masterFileId != null ? "masterFileId=" + masterFileId + ", " : "") +
                (endPointsId != null ? "endPointsId=" + endPointsId + ", " : "") +
                (studiedProductsId != null ? "studiedProductsId=" + studiedProductsId + ", " : "") +
                (mileStonesId != null ? "mileStonesId=" + mileStonesId + ", " : "") +
                (bioAnalysesId != null ? "bioAnalysesId=" + bioAnalysesId + ", " : "") +
                (dataAnalysesId != null ? "dataAnalysesId=" + dataAnalysesId + ", " : "") +
                (shipmentsId != null ? "shipmentsId=" + shipmentsId + ", " : "") +
                (studySamplesId != null ? "studySamplesId=" + studySamplesId + ", " : "") +
                (investigatorsId != null ? "investigatorsId=" + investigatorsId + ", " : "") +
            "}";
    }

}

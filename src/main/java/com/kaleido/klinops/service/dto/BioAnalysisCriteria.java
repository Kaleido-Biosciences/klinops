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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.kaleido.klinops.domain.BioAnalysis} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.BioAnalysisResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bio-analyses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BioAnalysisCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter analyte;

    private StringFilter sampleType;

    private StringFilter bioAnalysisType;

    private LocalDateFilter anticipatedLabWorkStartDate;

    private LocalDateFilter actualLabWorkStartDate;

    private LocalDateFilter anticipatedLabResultDeliveryDate;

    private LocalDateFilter actualLabResultDeliveryDate;

    private StringFilter dataLocation;

    private StringFilter contactName;

    private StringFilter contactEmail;

    private StringFilter comments;

    private LongFilter studyEndPointId;

    private LongFilter laboratoriesId;

    private LongFilter studyId;

    private LongFilter dataAnalysesId;

    public BioAnalysisCriteria(){
    }

    public BioAnalysisCriteria(BioAnalysisCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.analyte = other.analyte == null ? null : other.analyte.copy();
        this.sampleType = other.sampleType == null ? null : other.sampleType.copy();
        this.bioAnalysisType = other.bioAnalysisType == null ? null : other.bioAnalysisType.copy();
        this.anticipatedLabWorkStartDate = other.anticipatedLabWorkStartDate == null ? null : other.anticipatedLabWorkStartDate.copy();
        this.actualLabWorkStartDate = other.actualLabWorkStartDate == null ? null : other.actualLabWorkStartDate.copy();
        this.anticipatedLabResultDeliveryDate = other.anticipatedLabResultDeliveryDate == null ? null : other.anticipatedLabResultDeliveryDate.copy();
        this.actualLabResultDeliveryDate = other.actualLabResultDeliveryDate == null ? null : other.actualLabResultDeliveryDate.copy();
        this.dataLocation = other.dataLocation == null ? null : other.dataLocation.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.contactEmail = other.contactEmail == null ? null : other.contactEmail.copy();
        this.comments = other.comments == null ? null : other.comments.copy();
        this.studyEndPointId = other.studyEndPointId == null ? null : other.studyEndPointId.copy();
        this.laboratoriesId = other.laboratoriesId == null ? null : other.laboratoriesId.copy();
        this.studyId = other.studyId == null ? null : other.studyId.copy();
        this.dataAnalysesId = other.dataAnalysesId == null ? null : other.dataAnalysesId.copy();
    }

    @Override
    public BioAnalysisCriteria copy() {
        return new BioAnalysisCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAnalyte() {
        return analyte;
    }

    public void setAnalyte(StringFilter analyte) {
        this.analyte = analyte;
    }

    public StringFilter getSampleType() {
        return sampleType;
    }

    public void setSampleType(StringFilter sampleType) {
        this.sampleType = sampleType;
    }

    public StringFilter getBioAnalysisType() {
        return bioAnalysisType;
    }

    public void setBioAnalysisType(StringFilter bioAnalysisType) {
        this.bioAnalysisType = bioAnalysisType;
    }

    public LocalDateFilter getAnticipatedLabWorkStartDate() {
        return anticipatedLabWorkStartDate;
    }

    public void setAnticipatedLabWorkStartDate(LocalDateFilter anticipatedLabWorkStartDate) {
        this.anticipatedLabWorkStartDate = anticipatedLabWorkStartDate;
    }

    public LocalDateFilter getActualLabWorkStartDate() {
        return actualLabWorkStartDate;
    }

    public void setActualLabWorkStartDate(LocalDateFilter actualLabWorkStartDate) {
        this.actualLabWorkStartDate = actualLabWorkStartDate;
    }

    public LocalDateFilter getAnticipatedLabResultDeliveryDate() {
        return anticipatedLabResultDeliveryDate;
    }

    public void setAnticipatedLabResultDeliveryDate(LocalDateFilter anticipatedLabResultDeliveryDate) {
        this.anticipatedLabResultDeliveryDate = anticipatedLabResultDeliveryDate;
    }

    public LocalDateFilter getActualLabResultDeliveryDate() {
        return actualLabResultDeliveryDate;
    }

    public void setActualLabResultDeliveryDate(LocalDateFilter actualLabResultDeliveryDate) {
        this.actualLabResultDeliveryDate = actualLabResultDeliveryDate;
    }

    public StringFilter getDataLocation() {
        return dataLocation;
    }

    public void setDataLocation(StringFilter dataLocation) {
        this.dataLocation = dataLocation;
    }

    public StringFilter getContactName() {
        return contactName;
    }

    public void setContactName(StringFilter contactName) {
        this.contactName = contactName;
    }

    public StringFilter getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(StringFilter contactEmail) {
        this.contactEmail = contactEmail;
    }

    public StringFilter getComments() {
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public LongFilter getStudyEndPointId() {
        return studyEndPointId;
    }

    public void setStudyEndPointId(LongFilter studyEndPointId) {
        this.studyEndPointId = studyEndPointId;
    }

    public LongFilter getLaboratoriesId() {
        return laboratoriesId;
    }

    public void setLaboratoriesId(LongFilter laboratoriesId) {
        this.laboratoriesId = laboratoriesId;
    }

    public LongFilter getStudyId() {
        return studyId;
    }

    public void setStudyId(LongFilter studyId) {
        this.studyId = studyId;
    }

    public LongFilter getDataAnalysesId() {
        return dataAnalysesId;
    }

    public void setDataAnalysesId(LongFilter dataAnalysesId) {
        this.dataAnalysesId = dataAnalysesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BioAnalysisCriteria that = (BioAnalysisCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(analyte, that.analyte) &&
            Objects.equals(sampleType, that.sampleType) &&
            Objects.equals(bioAnalysisType, that.bioAnalysisType) &&
            Objects.equals(anticipatedLabWorkStartDate, that.anticipatedLabWorkStartDate) &&
            Objects.equals(actualLabWorkStartDate, that.actualLabWorkStartDate) &&
            Objects.equals(anticipatedLabResultDeliveryDate, that.anticipatedLabResultDeliveryDate) &&
            Objects.equals(actualLabResultDeliveryDate, that.actualLabResultDeliveryDate) &&
            Objects.equals(dataLocation, that.dataLocation) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(contactEmail, that.contactEmail) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(studyEndPointId, that.studyEndPointId) &&
            Objects.equals(laboratoriesId, that.laboratoriesId) &&
            Objects.equals(studyId, that.studyId) &&
            Objects.equals(dataAnalysesId, that.dataAnalysesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        analyte,
        sampleType,
        bioAnalysisType,
        anticipatedLabWorkStartDate,
        actualLabWorkStartDate,
        anticipatedLabResultDeliveryDate,
        actualLabResultDeliveryDate,
        dataLocation,
        contactName,
        contactEmail,
        comments,
        studyEndPointId,
        laboratoriesId,
        studyId,
        dataAnalysesId
        );
    }

    @Override
    public String toString() {
        return "BioAnalysisCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (analyte != null ? "analyte=" + analyte + ", " : "") +
                (sampleType != null ? "sampleType=" + sampleType + ", " : "") +
                (bioAnalysisType != null ? "bioAnalysisType=" + bioAnalysisType + ", " : "") +
                (anticipatedLabWorkStartDate != null ? "anticipatedLabWorkStartDate=" + anticipatedLabWorkStartDate + ", " : "") +
                (actualLabWorkStartDate != null ? "actualLabWorkStartDate=" + actualLabWorkStartDate + ", " : "") +
                (anticipatedLabResultDeliveryDate != null ? "anticipatedLabResultDeliveryDate=" + anticipatedLabResultDeliveryDate + ", " : "") +
                (actualLabResultDeliveryDate != null ? "actualLabResultDeliveryDate=" + actualLabResultDeliveryDate + ", " : "") +
                (dataLocation != null ? "dataLocation=" + dataLocation + ", " : "") +
                (contactName != null ? "contactName=" + contactName + ", " : "") +
                (contactEmail != null ? "contactEmail=" + contactEmail + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (studyEndPointId != null ? "studyEndPointId=" + studyEndPointId + ", " : "") +
                (laboratoriesId != null ? "laboratoriesId=" + laboratoriesId + ", " : "") +
                (studyId != null ? "studyId=" + studyId + ", " : "") +
                (dataAnalysesId != null ? "dataAnalysesId=" + dataAnalysesId + ", " : "") +
            "}";
    }

}

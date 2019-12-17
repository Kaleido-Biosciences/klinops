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
 * Criteria class for the {@link com.kaleido.klinops.domain.DataAnalysis} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.DataAnalysisResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /data-analyses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DataAnalysisCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dataAnalysesType;

    private StringFilter contactName;

    private StringFilter contactEmail;

    private LocalDateFilter anticipatedAnalysisDeliveryDate;

    private LocalDateFilter actualAnalysisDeliveryDate;

    private StringFilter dataLocation;

    private LongFilter bioAnalysesId;

    private LongFilter studyId;

    public DataAnalysisCriteria(){
    }

    public DataAnalysisCriteria(DataAnalysisCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.dataAnalysesType = other.dataAnalysesType == null ? null : other.dataAnalysesType.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.contactEmail = other.contactEmail == null ? null : other.contactEmail.copy();
        this.anticipatedAnalysisDeliveryDate = other.anticipatedAnalysisDeliveryDate == null ? null : other.anticipatedAnalysisDeliveryDate.copy();
        this.actualAnalysisDeliveryDate = other.actualAnalysisDeliveryDate == null ? null : other.actualAnalysisDeliveryDate.copy();
        this.dataLocation = other.dataLocation == null ? null : other.dataLocation.copy();
        this.bioAnalysesId = other.bioAnalysesId == null ? null : other.bioAnalysesId.copy();
        this.studyId = other.studyId == null ? null : other.studyId.copy();
    }

    @Override
    public DataAnalysisCriteria copy() {
        return new DataAnalysisCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDataAnalysesType() {
        return dataAnalysesType;
    }

    public void setDataAnalysesType(StringFilter dataAnalysesType) {
        this.dataAnalysesType = dataAnalysesType;
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

    public LocalDateFilter getAnticipatedAnalysisDeliveryDate() {
        return anticipatedAnalysisDeliveryDate;
    }

    public void setAnticipatedAnalysisDeliveryDate(LocalDateFilter anticipatedAnalysisDeliveryDate) {
        this.anticipatedAnalysisDeliveryDate = anticipatedAnalysisDeliveryDate;
    }

    public LocalDateFilter getActualAnalysisDeliveryDate() {
        return actualAnalysisDeliveryDate;
    }

    public void setActualAnalysisDeliveryDate(LocalDateFilter actualAnalysisDeliveryDate) {
        this.actualAnalysisDeliveryDate = actualAnalysisDeliveryDate;
    }

    public StringFilter getDataLocation() {
        return dataLocation;
    }

    public void setDataLocation(StringFilter dataLocation) {
        this.dataLocation = dataLocation;
    }

    public LongFilter getBioAnalysesId() {
        return bioAnalysesId;
    }

    public void setBioAnalysesId(LongFilter bioAnalysesId) {
        this.bioAnalysesId = bioAnalysesId;
    }

    public LongFilter getStudyId() {
        return studyId;
    }

    public void setStudyId(LongFilter studyId) {
        this.studyId = studyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DataAnalysisCriteria that = (DataAnalysisCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dataAnalysesType, that.dataAnalysesType) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(contactEmail, that.contactEmail) &&
            Objects.equals(anticipatedAnalysisDeliveryDate, that.anticipatedAnalysisDeliveryDate) &&
            Objects.equals(actualAnalysisDeliveryDate, that.actualAnalysisDeliveryDate) &&
            Objects.equals(dataLocation, that.dataLocation) &&
            Objects.equals(bioAnalysesId, that.bioAnalysesId) &&
            Objects.equals(studyId, that.studyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dataAnalysesType,
        contactName,
        contactEmail,
        anticipatedAnalysisDeliveryDate,
        actualAnalysisDeliveryDate,
        dataLocation,
        bioAnalysesId,
        studyId
        );
    }

    @Override
    public String toString() {
        return "DataAnalysisCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dataAnalysesType != null ? "dataAnalysesType=" + dataAnalysesType + ", " : "") +
                (contactName != null ? "contactName=" + contactName + ", " : "") +
                (contactEmail != null ? "contactEmail=" + contactEmail + ", " : "") +
                (anticipatedAnalysisDeliveryDate != null ? "anticipatedAnalysisDeliveryDate=" + anticipatedAnalysisDeliveryDate + ", " : "") +
                (actualAnalysisDeliveryDate != null ? "actualAnalysisDeliveryDate=" + actualAnalysisDeliveryDate + ", " : "") +
                (dataLocation != null ? "dataLocation=" + dataLocation + ", " : "") +
                (bioAnalysesId != null ? "bioAnalysesId=" + bioAnalysesId + ", " : "") +
                (studyId != null ? "studyId=" + studyId + ", " : "") +
            "}";
    }

}

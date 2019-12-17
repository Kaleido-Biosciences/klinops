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
 * Criteria class for the {@link com.kaleido.klinops.domain.StudyProduct} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.StudyProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /study-products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudyProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter productName;

    private StringFilter doseRange;

    private IntegerFilter daysOfExposure;

    private StringFilter formulation;

    private LongFilter studyId;

    public StudyProductCriteria(){
    }

    public StudyProductCriteria(StudyProductCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.productName = other.productName == null ? null : other.productName.copy();
        this.doseRange = other.doseRange == null ? null : other.doseRange.copy();
        this.daysOfExposure = other.daysOfExposure == null ? null : other.daysOfExposure.copy();
        this.formulation = other.formulation == null ? null : other.formulation.copy();
        this.studyId = other.studyId == null ? null : other.studyId.copy();
    }

    @Override
    public StudyProductCriteria copy() {
        return new StudyProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProductName() {
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public StringFilter getDoseRange() {
        return doseRange;
    }

    public void setDoseRange(StringFilter doseRange) {
        this.doseRange = doseRange;
    }

    public IntegerFilter getDaysOfExposure() {
        return daysOfExposure;
    }

    public void setDaysOfExposure(IntegerFilter daysOfExposure) {
        this.daysOfExposure = daysOfExposure;
    }

    public StringFilter getFormulation() {
        return formulation;
    }

    public void setFormulation(StringFilter formulation) {
        this.formulation = formulation;
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
        final StudyProductCriteria that = (StudyProductCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(doseRange, that.doseRange) &&
            Objects.equals(daysOfExposure, that.daysOfExposure) &&
            Objects.equals(formulation, that.formulation) &&
            Objects.equals(studyId, that.studyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        productName,
        doseRange,
        daysOfExposure,
        formulation,
        studyId
        );
    }

    @Override
    public String toString() {
        return "StudyProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (productName != null ? "productName=" + productName + ", " : "") +
                (doseRange != null ? "doseRange=" + doseRange + ", " : "") +
                (daysOfExposure != null ? "daysOfExposure=" + daysOfExposure + ", " : "") +
                (formulation != null ? "formulation=" + formulation + ", " : "") +
                (studyId != null ? "studyId=" + studyId + ", " : "") +
            "}";
    }

}

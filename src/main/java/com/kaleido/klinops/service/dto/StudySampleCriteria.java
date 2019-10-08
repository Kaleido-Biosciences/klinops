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
 * Criteria class for the {@link com.kaleido.klinops.domain.StudySample} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.StudySampleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /study-samples?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudySampleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sampleType;

    private IntegerFilter expectedNumberOfSamples;

    private LongFilter studyId;

    public StudySampleCriteria(){
    }

    public StudySampleCriteria(StudySampleCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.sampleType = other.sampleType == null ? null : other.sampleType.copy();
        this.expectedNumberOfSamples = other.expectedNumberOfSamples == null ? null : other.expectedNumberOfSamples.copy();
        this.studyId = other.studyId == null ? null : other.studyId.copy();
    }

    @Override
    public StudySampleCriteria copy() {
        return new StudySampleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSampleType() {
        return sampleType;
    }

    public void setSampleType(StringFilter sampleType) {
        this.sampleType = sampleType;
    }

    public IntegerFilter getExpectedNumberOfSamples() {
        return expectedNumberOfSamples;
    }

    public void setExpectedNumberOfSamples(IntegerFilter expectedNumberOfSamples) {
        this.expectedNumberOfSamples = expectedNumberOfSamples;
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
        final StudySampleCriteria that = (StudySampleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sampleType, that.sampleType) &&
            Objects.equals(expectedNumberOfSamples, that.expectedNumberOfSamples) &&
            Objects.equals(studyId, that.studyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sampleType,
        expectedNumberOfSamples,
        studyId
        );
    }

    @Override
    public String toString() {
        return "StudySampleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sampleType != null ? "sampleType=" + sampleType + ", " : "") +
                (expectedNumberOfSamples != null ? "expectedNumberOfSamples=" + expectedNumberOfSamples + ", " : "") +
                (studyId != null ? "studyId=" + studyId + ", " : "") +
            "}";
    }

}

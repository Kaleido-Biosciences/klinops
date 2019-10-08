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
 * Criteria class for the {@link com.kaleido.klinops.domain.TrialMasterFile} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.TrialMasterFileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trial-master-files?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TrialMasterFileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileName;

    private StringFilter location;

    private StringFilter status;

    private BooleanFilter electronic;

    private LongFilter clinicalStudyId;

    public TrialMasterFileCriteria(){
    }

    public TrialMasterFileCriteria(TrialMasterFileCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.electronic = other.electronic == null ? null : other.electronic.copy();
        this.clinicalStudyId = other.clinicalStudyId == null ? null : other.clinicalStudyId.copy();
    }

    @Override
    public TrialMasterFileCriteria copy() {
        return new TrialMasterFileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public BooleanFilter getElectronic() {
        return electronic;
    }

    public void setElectronic(BooleanFilter electronic) {
        this.electronic = electronic;
    }

    public LongFilter getClinicalStudyId() {
        return clinicalStudyId;
    }

    public void setClinicalStudyId(LongFilter clinicalStudyId) {
        this.clinicalStudyId = clinicalStudyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TrialMasterFileCriteria that = (TrialMasterFileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(location, that.location) &&
            Objects.equals(status, that.status) &&
            Objects.equals(electronic, that.electronic) &&
            Objects.equals(clinicalStudyId, that.clinicalStudyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fileName,
        location,
        status,
        electronic,
        clinicalStudyId
        );
    }

    @Override
    public String toString() {
        return "TrialMasterFileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fileName != null ? "fileName=" + fileName + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (electronic != null ? "electronic=" + electronic + ", " : "") +
                (clinicalStudyId != null ? "clinicalStudyId=" + clinicalStudyId + ", " : "") +
            "}";
    }

}

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
 * Criteria class for the {@link com.kaleido.klinops.domain.StudyEndPoint} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.StudyEndPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /study-end-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudyEndPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter objective;

    private StringFilter endPointType;

    private LongFilter studyId;

    public StudyEndPointCriteria(){
    }

    public StudyEndPointCriteria(StudyEndPointCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.objective = other.objective == null ? null : other.objective.copy();
        this.endPointType = other.endPointType == null ? null : other.endPointType.copy();
        this.studyId = other.studyId == null ? null : other.studyId.copy();
    }

    @Override
    public StudyEndPointCriteria copy() {
        return new StudyEndPointCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getObjective() {
        return objective;
    }

    public void setObjective(StringFilter objective) {
        this.objective = objective;
    }

    public StringFilter getEndPointType() {
        return endPointType;
    }

    public void setEndPointType(StringFilter endPointType) {
        this.endPointType = endPointType;
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
        final StudyEndPointCriteria that = (StudyEndPointCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(objective, that.objective) &&
            Objects.equals(endPointType, that.endPointType) &&
            Objects.equals(studyId, that.studyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        objective,
        endPointType,
        studyId
        );
    }

    @Override
    public String toString() {
        return "StudyEndPointCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (objective != null ? "objective=" + objective + ", " : "") +
                (endPointType != null ? "endPointType=" + endPointType + ", " : "") +
                (studyId != null ? "studyId=" + studyId + ", " : "") +
            "}";
    }

}

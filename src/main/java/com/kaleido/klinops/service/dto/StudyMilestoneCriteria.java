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
 * Criteria class for the {@link com.kaleido.klinops.domain.StudyMilestone} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.StudyMilestoneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /study-milestones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudyMilestoneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter mileStoneName;

    private StringFilter mileStoneType;

    private LocalDateFilter projectedCompletionDate;

    private LocalDateFilter actualCompletionDate;

    private LongFilter studyId;

    public StudyMilestoneCriteria(){
    }

    public StudyMilestoneCriteria(StudyMilestoneCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.mileStoneName = other.mileStoneName == null ? null : other.mileStoneName.copy();
        this.mileStoneType = other.mileStoneType == null ? null : other.mileStoneType.copy();
        this.projectedCompletionDate = other.projectedCompletionDate == null ? null : other.projectedCompletionDate.copy();
        this.actualCompletionDate = other.actualCompletionDate == null ? null : other.actualCompletionDate.copy();
        this.studyId = other.studyId == null ? null : other.studyId.copy();
    }

    @Override
    public StudyMilestoneCriteria copy() {
        return new StudyMilestoneCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMileStoneName() {
        return mileStoneName;
    }

    public void setMileStoneName(StringFilter mileStoneName) {
        this.mileStoneName = mileStoneName;
    }

    public StringFilter getMileStoneType() {
        return mileStoneType;
    }

    public void setMileStoneType(StringFilter mileStoneType) {
        this.mileStoneType = mileStoneType;
    }

    public LocalDateFilter getProjectedCompletionDate() {
        return projectedCompletionDate;
    }

    public void setProjectedCompletionDate(LocalDateFilter projectedCompletionDate) {
        this.projectedCompletionDate = projectedCompletionDate;
    }

    public LocalDateFilter getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(LocalDateFilter actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
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
        final StudyMilestoneCriteria that = (StudyMilestoneCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(mileStoneName, that.mileStoneName) &&
            Objects.equals(mileStoneType, that.mileStoneType) &&
            Objects.equals(projectedCompletionDate, that.projectedCompletionDate) &&
            Objects.equals(actualCompletionDate, that.actualCompletionDate) &&
            Objects.equals(studyId, that.studyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        mileStoneName,
        mileStoneType,
        projectedCompletionDate,
        actualCompletionDate,
        studyId
        );
    }

    @Override
    public String toString() {
        return "StudyMilestoneCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (mileStoneName != null ? "mileStoneName=" + mileStoneName + ", " : "") +
                (mileStoneType != null ? "mileStoneType=" + mileStoneType + ", " : "") +
                (projectedCompletionDate != null ? "projectedCompletionDate=" + projectedCompletionDate + ", " : "") +
                (actualCompletionDate != null ? "actualCompletionDate=" + actualCompletionDate + ", " : "") +
                (studyId != null ? "studyId=" + studyId + ", " : "") +
            "}";
    }

}

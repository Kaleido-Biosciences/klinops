/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A milestone of a study
 */
@ApiModel(description = "A milestone of a study")
@Entity
@Table(name = "study_milestone")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudyMilestone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * The name of the milestone
     */
    @NotNull
    @ApiModelProperty(value = "The name of the milestone", required = true)
    @Column(name = "mile_stone_name", nullable = false)
    private String mileStoneName;

    /**
     * The type or category of milestone
     */
    @NotNull
    @ApiModelProperty(value = "The type or category of milestone", required = true)
    @Column(name = "mile_stone_type", nullable = false)
    private String mileStoneType;

    /**
     * When the milestone should be complete
     */
    @ApiModelProperty(value = "When the milestone should be complete")
    @Column(name = "projected_completion_date")
    private LocalDate projectedCompletionDate;

    /**
     * When the milestone was completed
     */
    @ApiModelProperty(value = "When the milestone was completed")
    @Column(name = "actual_completion_date")
    private LocalDate actualCompletionDate;

    /**
     * The study for which this is a milestone
     */
    @ApiModelProperty(value = "The study for which this is a milestone")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("mileStones")
    private ClinicalStudy study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMileStoneName() {
        return mileStoneName;
    }

    public StudyMilestone mileStoneName(String mileStoneName) {
        this.mileStoneName = mileStoneName;
        return this;
    }

    public void setMileStoneName(String mileStoneName) {
        this.mileStoneName = mileStoneName;
    }

    public String getMileStoneType() {
        return mileStoneType;
    }

    public StudyMilestone mileStoneType(String mileStoneType) {
        this.mileStoneType = mileStoneType;
        return this;
    }

    public void setMileStoneType(String mileStoneType) {
        this.mileStoneType = mileStoneType;
    }

    public LocalDate getProjectedCompletionDate() {
        return projectedCompletionDate;
    }

    public StudyMilestone projectedCompletionDate(LocalDate projectedCompletionDate) {
        this.projectedCompletionDate = projectedCompletionDate;
        return this;
    }

    public void setProjectedCompletionDate(LocalDate projectedCompletionDate) {
        this.projectedCompletionDate = projectedCompletionDate;
    }

    public LocalDate getActualCompletionDate() {
        return actualCompletionDate;
    }

    public StudyMilestone actualCompletionDate(LocalDate actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
        return this;
    }

    public void setActualCompletionDate(LocalDate actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public ClinicalStudy getStudy() {
        return study;
    }

    public StudyMilestone study(ClinicalStudy clinicalStudy) {
        this.study = clinicalStudy;
        return this;
    }

    public void setStudy(ClinicalStudy clinicalStudy) {
        this.study = clinicalStudy;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudyMilestone)) {
            return false;
        }
        return id != null && id.equals(((StudyMilestone) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudyMilestone{" +
            "id=" + getId() +
            ", mileStoneName='" + getMileStoneName() + "'" +
            ", mileStoneType='" + getMileStoneType() + "'" +
            ", projectedCompletionDate='" + getProjectedCompletionDate() + "'" +
            ", actualCompletionDate='" + getActualCompletionDate() + "'" +
            "}";
    }
}

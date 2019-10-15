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

/**
 * A product administered in a study
 */
@ApiModel(description = "A product administered in a study")
@Entity
@Table(name = "study_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudyProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * The product name
     */
    @NotNull
    @ApiModelProperty(value = "The product name", required = true)
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * The range of doses of the product in the study e.g 10g to 100g
     */
    @ApiModelProperty(value = "The range of doses of the product in the study e.g 10g to 100g")
    @Column(name = "dose_range")
    private String doseRange;

    /**
     * The number of days of exposure to the product
     */
    @Min(value = 0)
    @ApiModelProperty(value = "The number of days of exposure to the product")
    @Column(name = "days_of_exposure")
    private Integer daysOfExposure;

    /**
     * The type of formulation of the product
     */
    @NotNull
    @ApiModelProperty(value = "The type of formulation of the product", required = true)
    @Column(name = "formulation", nullable = false)
    private String formulation;

    /**
     * The study for which this is a product
     */
    @ApiModelProperty(value = "The study for which this is a product")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("studiedProducts")
    private ClinicalStudy study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public StudyProduct productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDoseRange() {
        return doseRange;
    }

    public StudyProduct doseRange(String doseRange) {
        this.doseRange = doseRange;
        return this;
    }

    public void setDoseRange(String doseRange) {
        this.doseRange = doseRange;
    }

    public Integer getDaysOfExposure() {
        return daysOfExposure;
    }

    public StudyProduct daysOfExposure(Integer daysOfExposure) {
        this.daysOfExposure = daysOfExposure;
        return this;
    }

    public void setDaysOfExposure(Integer daysOfExposure) {
        this.daysOfExposure = daysOfExposure;
    }

    public String getFormulation() {
        return formulation;
    }

    public StudyProduct formulation(String formulation) {
        this.formulation = formulation;
        return this;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public ClinicalStudy getStudy() {
        return study;
    }

    public StudyProduct study(ClinicalStudy clinicalStudy) {
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
        if (!(o instanceof StudyProduct)) {
            return false;
        }
        return id != null && id.equals(((StudyProduct) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudyProduct{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", doseRange='" + getDoseRange() + "'" +
            ", daysOfExposure=" + getDaysOfExposure() +
            ", formulation='" + getFormulation() + "'" +
            "}";
    }
}

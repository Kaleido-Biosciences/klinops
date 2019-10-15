/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A BioAnalysis from a Study
 */
@ApiModel(description = "A BioAnalysis from a Study")
@Entity
@Table(name = "bio_analysis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BioAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * The analyte measure in this bioAnalyses
     */
    @NotNull
    @ApiModelProperty(value = "The analyte measure in this bioAnalyses", required = true)
    @Column(name = "analyte", nullable = false)
    private String analyte;

    /**
     * The type or category of biosample used in the analysis
     */
    @NotNull
    @ApiModelProperty(value = "The type or category of biosample used in the analysis", required = true)
    @Column(name = "sample_type", nullable = false)
    private String sampleType;

    /**
     * The type or category of bioanalysis
     */
    @NotNull
    @ApiModelProperty(value = "The type or category of bioanalysis", required = true)
    @Column(name = "bio_analysis_type", nullable = false)
    private String bioAnalysisType;

    /**
     * Scheduled start date of the analysis
     */
    @ApiModelProperty(value = "Scheduled start date of the analysis")
    @Column(name = "anticipated_lab_work_start_date")
    private LocalDate anticipatedLabWorkStartDate;

    /**
     * Actual start date of the analysis
     */
    @ApiModelProperty(value = "Actual start date of the analysis")
    @Column(name = "actual_lab_work_start_date")
    private LocalDate actualLabWorkStartDate;

    /**
     * Scheduled delivery date for the analysis results
     */
    @ApiModelProperty(value = "Scheduled delivery date for the analysis results")
    @Column(name = "anticipated_lab_result_delivery_date")
    private LocalDate anticipatedLabResultDeliveryDate;

    /**
     * Date results where delivered
     */
    @ApiModelProperty(value = "Date results where delivered")
    @Column(name = "actual_lab_result_delivery_date")
    private LocalDate actualLabResultDeliveryDate;

    /**
     * Location of the delivered data (e.g. URL)
     */
    @ApiModelProperty(value = "Location of the delivered data (e.g. URL)")
    @Column(name = "data_location")
    private String dataLocation;

    /**
     * Contact name for the bioanalysis
     */
    @ApiModelProperty(value = "Contact name for the bioanalysis")
    @Column(name = "contact_name")
    private String contactName;

    /**
     * Contact email for the bioanalysis
     */
    @Pattern(regexp = "^(.+)@(.+)|$")
    @ApiModelProperty(value = "Contact email for the bioanalysis")
    @Column(name = "contact_email")
    private String contactEmail;

    /**
     * Comments relevant to the bioanalysis
     */
    @ApiModelProperty(value = "Comments relevant to the bioanalysis")
    @Column(name = "comments")
    private String comments;

    /**
     * The Study End Point involved in the bioanalysis
     */
    @ApiModelProperty(value = "The Study End Point involved in the bioanalysis")
    @ManyToOne
    @JsonIgnoreProperties("bioAnalyses")
    private StudyEndPoint studyEndPoint;

    /**
     * The laboratories involved in the bioanalysis
     */
    @ApiModelProperty(value = "The laboratories involved in the bioanalysis")
    @ManyToOne
    @JsonIgnoreProperties("bioAnalyses")
    private Laboratory laboratories;

    /**
     * The study for which this analysis was done
     */
    @ApiModelProperty(value = "The study for which this analysis was done")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("bioAnalyses")
    private ClinicalStudy study;

    /**
     * The data analyses that use this bioanalysis
     */
    @ApiModelProperty(value = "The data analyses that use this bioanalysis")
    @ManyToMany(mappedBy = "bioAnalyses")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<DataAnalysis> dataAnalyses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnalyte() {
        return analyte;
    }

    public BioAnalysis analyte(String analyte) {
        this.analyte = analyte;
        return this;
    }

    public void setAnalyte(String analyte) {
        this.analyte = analyte;
    }

    public String getSampleType() {
        return sampleType;
    }

    public BioAnalysis sampleType(String sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getBioAnalysisType() {
        return bioAnalysisType;
    }

    public BioAnalysis bioAnalysisType(String bioAnalysisType) {
        this.bioAnalysisType = bioAnalysisType;
        return this;
    }

    public void setBioAnalysisType(String bioAnalysisType) {
        this.bioAnalysisType = bioAnalysisType;
    }

    public LocalDate getAnticipatedLabWorkStartDate() {
        return anticipatedLabWorkStartDate;
    }

    public BioAnalysis anticipatedLabWorkStartDate(LocalDate anticipatedLabWorkStartDate) {
        this.anticipatedLabWorkStartDate = anticipatedLabWorkStartDate;
        return this;
    }

    public void setAnticipatedLabWorkStartDate(LocalDate anticipatedLabWorkStartDate) {
        this.anticipatedLabWorkStartDate = anticipatedLabWorkStartDate;
    }

    public LocalDate getActualLabWorkStartDate() {
        return actualLabWorkStartDate;
    }

    public BioAnalysis actualLabWorkStartDate(LocalDate actualLabWorkStartDate) {
        this.actualLabWorkStartDate = actualLabWorkStartDate;
        return this;
    }

    public void setActualLabWorkStartDate(LocalDate actualLabWorkStartDate) {
        this.actualLabWorkStartDate = actualLabWorkStartDate;
    }

    public LocalDate getAnticipatedLabResultDeliveryDate() {
        return anticipatedLabResultDeliveryDate;
    }

    public BioAnalysis anticipatedLabResultDeliveryDate(LocalDate anticipatedLabResultDeliveryDate) {
        this.anticipatedLabResultDeliveryDate = anticipatedLabResultDeliveryDate;
        return this;
    }

    public void setAnticipatedLabResultDeliveryDate(LocalDate anticipatedLabResultDeliveryDate) {
        this.anticipatedLabResultDeliveryDate = anticipatedLabResultDeliveryDate;
    }

    public LocalDate getActualLabResultDeliveryDate() {
        return actualLabResultDeliveryDate;
    }

    public BioAnalysis actualLabResultDeliveryDate(LocalDate actualLabResultDeliveryDate) {
        this.actualLabResultDeliveryDate = actualLabResultDeliveryDate;
        return this;
    }

    public void setActualLabResultDeliveryDate(LocalDate actualLabResultDeliveryDate) {
        this.actualLabResultDeliveryDate = actualLabResultDeliveryDate;
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public BioAnalysis dataLocation(String dataLocation) {
        this.dataLocation = dataLocation;
        return this;
    }

    public void setDataLocation(String dataLocation) {
        this.dataLocation = dataLocation;
    }

    public String getContactName() {
        return contactName;
    }

    public BioAnalysis contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public BioAnalysis contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getComments() {
        return comments;
    }

    public BioAnalysis comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public StudyEndPoint getStudyEndPoint() {
        return studyEndPoint;
    }

    public BioAnalysis studyEndPoint(StudyEndPoint studyEndPoint) {
        this.studyEndPoint = studyEndPoint;
        return this;
    }

    public void setStudyEndPoint(StudyEndPoint studyEndPoint) {
        this.studyEndPoint = studyEndPoint;
    }

    public Laboratory getLaboratories() {
        return laboratories;
    }

    public BioAnalysis laboratories(Laboratory laboratory) {
        this.laboratories = laboratory;
        return this;
    }

    public void setLaboratories(Laboratory laboratory) {
        this.laboratories = laboratory;
    }

    public ClinicalStudy getStudy() {
        return study;
    }

    public BioAnalysis study(ClinicalStudy clinicalStudy) {
        this.study = clinicalStudy;
        return this;
    }

    public void setStudy(ClinicalStudy clinicalStudy) {
        this.study = clinicalStudy;
    }

    public Set<DataAnalysis> getDataAnalyses() {
        return dataAnalyses;
    }

    public BioAnalysis dataAnalyses(Set<DataAnalysis> dataAnalyses) {
        this.dataAnalyses = dataAnalyses;
        return this;
    }

    public BioAnalysis addDataAnalyses(DataAnalysis dataAnalysis) {
        this.dataAnalyses.add(dataAnalysis);
        dataAnalysis.getBioAnalyses().add(this);
        return this;
    }

    public BioAnalysis removeDataAnalyses(DataAnalysis dataAnalysis) {
        this.dataAnalyses.remove(dataAnalysis);
        dataAnalysis.getBioAnalyses().remove(this);
        return this;
    }

    public void setDataAnalyses(Set<DataAnalysis> dataAnalyses) {
        this.dataAnalyses = dataAnalyses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BioAnalysis)) {
            return false;
        }
        return id != null && id.equals(((BioAnalysis) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BioAnalysis{" +
            "id=" + getId() +
            ", analyte='" + getAnalyte() + "'" +
            ", sampleType='" + getSampleType() + "'" +
            ", bioAnalysisType='" + getBioAnalysisType() + "'" +
            ", anticipatedLabWorkStartDate='" + getAnticipatedLabWorkStartDate() + "'" +
            ", actualLabWorkStartDate='" + getActualLabWorkStartDate() + "'" +
            ", anticipatedLabResultDeliveryDate='" + getAnticipatedLabResultDeliveryDate() + "'" +
            ", actualLabResultDeliveryDate='" + getActualLabResultDeliveryDate() + "'" +
            ", dataLocation='" + getDataLocation() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}

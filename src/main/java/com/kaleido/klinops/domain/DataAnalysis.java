package com.kaleido.klinops.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * An analysis of data from a study
 */
@ApiModel(description = "An analysis of data from a study")
@Entity
@Table(name = "data_analysis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dataanalysis")
public class DataAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    /**
     * The type or category of BioAnalysis
     */
    @NotNull
    @ApiModelProperty(value = "The type or category of BioAnalysis", required = true)
    @Column(name = "data_analyses_type", nullable = false)
    private String dataAnalysesType;

    /**
     * Main contact for the analysis
     */
    @ApiModelProperty(value = "Main contact for the analysis")
    @Column(name = "contact_name")
    private String contactName;

    /**
     * Contact email for the analysis
     */
    @Pattern(regexp = "^(.+)@(.+)|$")
    @ApiModelProperty(value = "Contact email for the analysis")
    @Column(name = "contact_email")
    private String contactEmail;

    /**
     * Expected date for delivery of the analysis
     */
    @ApiModelProperty(value = "Expected date for delivery of the analysis")
    @Column(name = "anticipated_analysis_delivery_date")
    private LocalDate anticipatedAnalysisDeliveryDate;

    /**
     * Actual date of delivery of the analysis
     */
    @ApiModelProperty(value = "Actual date of delivery of the analysis")
    @Column(name = "actual_analysis_delivery_date")
    private LocalDate actualAnalysisDeliveryDate;

    /**
     * The location where the analysis is stored (e.g. URL)
     */
    @ApiModelProperty(value = "The location where the analysis is stored (e.g. URL)")
    @Column(name = "data_location")
    private String dataLocation;

    /**
     * The bioAnalyses involved in this data analysis
     */
    @ApiModelProperty(value = "The bioAnalyses involved in this data analysis")
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "data_analysis_bio_analyses",
               joinColumns = @JoinColumn(name = "data_analysis_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "bio_analyses_id", referencedColumnName = "id"))
    private Set<BioAnalysis> bioAnalyses = new HashSet<>();

    /**
     * The study for which this analysis was done
     */
    @ApiModelProperty(value = "The study for which this analysis was done")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("dataAnalyses")
    private ClinicalStudy study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataAnalysesType() {
        return dataAnalysesType;
    }

    public DataAnalysis dataAnalysesType(String dataAnalysesType) {
        this.dataAnalysesType = dataAnalysesType;
        return this;
    }

    public void setDataAnalysesType(String dataAnalysesType) {
        this.dataAnalysesType = dataAnalysesType;
    }

    public String getContactName() {
        return contactName;
    }

    public DataAnalysis contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public DataAnalysis contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public LocalDate getAnticipatedAnalysisDeliveryDate() {
        return anticipatedAnalysisDeliveryDate;
    }

    public DataAnalysis anticipatedAnalysisDeliveryDate(LocalDate anticipatedAnalysisDeliveryDate) {
        this.anticipatedAnalysisDeliveryDate = anticipatedAnalysisDeliveryDate;
        return this;
    }

    public void setAnticipatedAnalysisDeliveryDate(LocalDate anticipatedAnalysisDeliveryDate) {
        this.anticipatedAnalysisDeliveryDate = anticipatedAnalysisDeliveryDate;
    }

    public LocalDate getActualAnalysisDeliveryDate() {
        return actualAnalysisDeliveryDate;
    }

    public DataAnalysis actualAnalysisDeliveryDate(LocalDate actualAnalysisDeliveryDate) {
        this.actualAnalysisDeliveryDate = actualAnalysisDeliveryDate;
        return this;
    }

    public void setActualAnalysisDeliveryDate(LocalDate actualAnalysisDeliveryDate) {
        this.actualAnalysisDeliveryDate = actualAnalysisDeliveryDate;
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public DataAnalysis dataLocation(String dataLocation) {
        this.dataLocation = dataLocation;
        return this;
    }

    public void setDataLocation(String dataLocation) {
        this.dataLocation = dataLocation;
    }

    public Set<BioAnalysis> getBioAnalyses() {
        return bioAnalyses;
    }

    public DataAnalysis bioAnalyses(Set<BioAnalysis> bioAnalyses) {
        this.bioAnalyses = bioAnalyses;
        return this;
    }

    public DataAnalysis addBioAnalyses(BioAnalysis bioAnalysis) {
        this.bioAnalyses.add(bioAnalysis);
        bioAnalysis.getDataAnalyses().add(this);
        return this;
    }

    public DataAnalysis removeBioAnalyses(BioAnalysis bioAnalysis) {
        this.bioAnalyses.remove(bioAnalysis);
        bioAnalysis.getDataAnalyses().remove(this);
        return this;
    }

    public void setBioAnalyses(Set<BioAnalysis> bioAnalyses) {
        this.bioAnalyses = bioAnalyses;
    }

    public ClinicalStudy getStudy() {
        return study;
    }

    public DataAnalysis study(ClinicalStudy clinicalStudy) {
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
        if (!(o instanceof DataAnalysis)) {
            return false;
        }
        return id != null && id.equals(((DataAnalysis) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DataAnalysis{" +
            "id=" + getId() +
            ", dataAnalysesType='" + getDataAnalysesType() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", anticipatedAnalysisDeliveryDate='" + getAnticipatedAnalysisDeliveryDate() + "'" +
            ", actualAnalysisDeliveryDate='" + getActualAnalysisDeliveryDate() + "'" +
            ", dataLocation='" + getDataLocation() + "'" +
            "}";
    }
}

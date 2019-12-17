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

/**
 * Represents study patient sample
 */
@ApiModel(description = "Represents study patient sample")
@Entity
@Table(name = "study_sample")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "studysample")
public class StudySample implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    /**
     * The type or category of the study sample
     */
    @NotNull
    @ApiModelProperty(value = "The type or category of the study sample", required = true)
    @Column(name = "sample_type", nullable = false)
    private String sampleType;

    /**
     * Expected number of samples
     */
    @Min(value = 0)
    @ApiModelProperty(value = "Expected number of samples")
    @Column(name = "expected_number_of_samples")
    private Integer expectedNumberOfSamples;

    /**
     * The study to which this study sample belongs
     */
    @ApiModelProperty(value = "The study to which this study sample belongs")
    @ManyToOne
    @JsonIgnoreProperties("studySamples")
    private ClinicalStudy study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSampleType() {
        return sampleType;
    }

    public StudySample sampleType(String sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public Integer getExpectedNumberOfSamples() {
        return expectedNumberOfSamples;
    }

    public StudySample expectedNumberOfSamples(Integer expectedNumberOfSamples) {
        this.expectedNumberOfSamples = expectedNumberOfSamples;
        return this;
    }

    public void setExpectedNumberOfSamples(Integer expectedNumberOfSamples) {
        this.expectedNumberOfSamples = expectedNumberOfSamples;
    }

    public ClinicalStudy getStudy() {
        return study;
    }

    public StudySample study(ClinicalStudy clinicalStudy) {
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
        if (!(o instanceof StudySample)) {
            return false;
        }
        return id != null && id.equals(((StudySample) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudySample{" +
            "id=" + getId() +
            ", sampleType='" + getSampleType() + "'" +
            ", expectedNumberOfSamples=" + getExpectedNumberOfSamples() +
            "}";
    }
}

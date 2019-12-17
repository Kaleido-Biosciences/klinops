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
 * An endpoint in a Study\n@author Mark Schreiber
 */
@ApiModel(description = "An endpoint in a Study\n@author Mark Schreiber")
@Entity
@Table(name = "study_end_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "studyendpoint")
public class StudyEndPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    /**
     * A description of the endpoint
     */
    @NotNull
    @Size(max = 5000)
    @ApiModelProperty(value = "A description of the endpoint", required = true)
    @Column(name = "description", length = 5000, nullable = false)
    private String description;

    /**
     * The objective(s) of the endpoint
     */
    @NotNull
    @Size(max = 5000)
    @ApiModelProperty(value = "The objective(s) of the endpoint", required = true)
    @Column(name = "objective", length = 5000, nullable = false)
    private String objective;

    /**
     * The type of endpoint
     */
    @NotNull
    @ApiModelProperty(value = "The type of endpoint", required = true)
    @Column(name = "end_point_type", nullable = false)
    private String endPointType;

    /**
     * The study for which this is an Endpoint
     */
    @ApiModelProperty(value = "The study for which this is an Endpoint")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("endPoints")
    private ClinicalStudy study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public StudyEndPoint description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjective() {
        return objective;
    }

    public StudyEndPoint objective(String objective) {
        this.objective = objective;
        return this;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getEndPointType() {
        return endPointType;
    }

    public StudyEndPoint endPointType(String endPointType) {
        this.endPointType = endPointType;
        return this;
    }

    public void setEndPointType(String endPointType) {
        this.endPointType = endPointType;
    }

    public ClinicalStudy getStudy() {
        return study;
    }

    public StudyEndPoint study(ClinicalStudy clinicalStudy) {
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
        if (!(o instanceof StudyEndPoint)) {
            return false;
        }
        return id != null && id.equals(((StudyEndPoint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudyEndPoint{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", objective='" + getObjective() + "'" +
            ", endPointType='" + getEndPointType() + "'" +
            "}";
    }
}

/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Information about the Trial Master File
 */
@ApiModel(description = "Information about the Trial Master File")
@Entity
@Table(name = "trial_master_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TrialMasterFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * The unique id or name of the file
     */

    @ApiModelProperty(value = "The unique id or name of the file")
    @Column(name = "file_name", unique = true)
    private String fileName;

    /**
     * The physical or electronic location of the master file
     */
    @NotNull
    @ApiModelProperty(value = "The physical or electronic location of the master file", required = true)
    @Column(name = "location", nullable = false)
    private String location;

    /**
     * The status of the master file
     */
    @ApiModelProperty(value = "The status of the master file")
    @Column(name = "status")
    private String status;

    /**
     * Is the master file electronic
     */
    @NotNull
    @ApiModelProperty(value = "Is the master file electronic", required = true)
    @Column(name = "electronic", nullable = false)
    private Boolean electronic;

    @OneToOne(mappedBy = "masterFile")
    @JsonIgnore
    private ClinicalStudy clinicalStudy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public TrialMasterFile fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocation() {
        return location;
    }

    public TrialMasterFile location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public TrialMasterFile status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isElectronic() {
        return electronic;
    }

    public TrialMasterFile electronic(Boolean electronic) {
        this.electronic = electronic;
        return this;
    }

    public void setElectronic(Boolean electronic) {
        this.electronic = electronic;
    }

    public ClinicalStudy getClinicalStudy() {
        return clinicalStudy;
    }

    public TrialMasterFile clinicalStudy(ClinicalStudy clinicalStudy) {
        this.clinicalStudy = clinicalStudy;
        return this;
    }

    public void setClinicalStudy(ClinicalStudy clinicalStudy) {
        this.clinicalStudy = clinicalStudy;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrialMasterFile)) {
            return false;
        }
        return id != null && id.equals(((TrialMasterFile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TrialMasterFile{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", location='" + getLocation() + "'" +
            ", status='" + getStatus() + "'" +
            ", electronic='" + isElectronic() + "'" +
            "}";
    }
}

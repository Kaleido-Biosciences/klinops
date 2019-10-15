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
import java.util.HashSet;
import java.util.Set;

/**
 * An investigator on a study\n@author Mark Schreiber
 */
@ApiModel(description = "An investigator on a study\n@author Mark Schreiber")
@Entity
@Table(name = "principal_investigator")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrincipalInvestigator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Name of the PI
     */
    @NotNull
    @ApiModelProperty(value = "Name of the PI", required = true)
    @Column(name = "investigator_name", nullable = false)
    private String investigatorName;

    /**
     * Street Address of the PI
     */
    @ApiModelProperty(value = "Street Address of the PI")
    @Column(name = "street_address")
    private String streetAddress;

    /**
     * City of the PI
     */
    @ApiModelProperty(value = "City of the PI")
    @Column(name = "city")
    private String city;

    /**
     * State of the PI
     */
    @ApiModelProperty(value = "State of the PI")
    @Column(name = "state")
    private String state;

    /**
     * Zip Code of the PI
     */
    @ApiModelProperty(value = "Zip Code of the PI")
    @Column(name = "zip")
    private String zip;

    /**
     * Country of the PI
     */
    @ApiModelProperty(value = "Country of the PI")
    @Column(name = "country")
    private String country;

    /**
     * Email address for the PI
     */
    @Pattern(regexp = "^(.+)@(.+)|$")
    @ApiModelProperty(value = "Email address for the PI")
    @Column(name = "email")
    private String email;

    /**
     * Phone number of the PI
     */
    @ApiModelProperty(value = "Phone number of the PI")
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * the site that the investigator was at for the study
     */
    @ApiModelProperty(value = "the site that the investigator was at for the study")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("investigators")
    private Site site;

    /**
     * Studies that the PI has worked on
     */
    @ApiModelProperty(value = "Studies that the PI has worked on")
    @ManyToMany(mappedBy = "investigators")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ClinicalStudy> studies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvestigatorName() {
        return investigatorName;
    }

    public PrincipalInvestigator investigatorName(String investigatorName) {
        this.investigatorName = investigatorName;
        return this;
    }

    public void setInvestigatorName(String investigatorName) {
        this.investigatorName = investigatorName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public PrincipalInvestigator streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public PrincipalInvestigator city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public PrincipalInvestigator state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public PrincipalInvestigator zip(String zip) {
        this.zip = zip;
        return this;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public PrincipalInvestigator country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public PrincipalInvestigator email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PrincipalInvestigator phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Site getSite() {
        return site;
    }

    public PrincipalInvestigator site(Site site) {
        this.site = site;
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Set<ClinicalStudy> getStudies() {
        return studies;
    }

    public PrincipalInvestigator studies(Set<ClinicalStudy> clinicalStudies) {
        this.studies = clinicalStudies;
        return this;
    }

    public PrincipalInvestigator addStudies(ClinicalStudy clinicalStudy) {
        this.studies.add(clinicalStudy);
        clinicalStudy.getInvestigators().add(this);
        return this;
    }

    public PrincipalInvestigator removeStudies(ClinicalStudy clinicalStudy) {
        this.studies.remove(clinicalStudy);
        clinicalStudy.getInvestigators().remove(this);
        return this;
    }

    public void setStudies(Set<ClinicalStudy> clinicalStudies) {
        this.studies = clinicalStudies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrincipalInvestigator)) {
            return false;
        }
        return id != null && id.equals(((PrincipalInvestigator) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PrincipalInvestigator{" +
            "id=" + getId() +
            ", investigatorName='" + getInvestigatorName() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zip='" + getZip() + "'" +
            ", country='" + getCountry() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}

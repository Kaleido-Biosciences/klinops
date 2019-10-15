/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.domain;
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
 * A Site where a study is carried out\n@author Mark Schreiber
 */
@ApiModel(description = "A Site where a study is carried out\n@author Mark Schreiber")
@Entity
@Table(name = "site")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * A universally unique ID for this object
     */
    @NotNull
    @ApiModelProperty(value = "A universally unique ID for this object", required = true)
    @Column(name = "site_name", nullable = false, unique = true)
    private String siteName;

    /**
     * The institution name
     */
    @NotNull
    @ApiModelProperty(value = "The institution name", required = true)
    @Column(name = "institution", nullable = false)
    private String institution;

    /**
     * The street address of the site
     */
    @NotNull
    @ApiModelProperty(value = "The street address of the site", required = true)
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    /**
     * The city the site is in
     */
    @NotNull
    @ApiModelProperty(value = "The city the site is in", required = true)
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * The state that the site is in
     */
    @ApiModelProperty(value = "The state that the site is in")
    @Column(name = "state")
    private String state;

    /**
     * Zip Code the site is in
     */
    @NotNull
    @ApiModelProperty(value = "Zip Code the site is in", required = true)
    @Column(name = "zip", nullable = false)
    private String zip;

    /**
     * The country that the site is in
     */
    @NotNull
    @ApiModelProperty(value = "The country that the site is in", required = true)
    @Column(name = "country", nullable = false)
    private String country;

    /**
     * The investigators at this study site
     */
    @ApiModelProperty(value = "The investigators at this study site")
    @OneToMany(mappedBy = "site")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PrincipalInvestigator> investigators = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public Site siteName(String siteName) {
        this.siteName = siteName;
        return this;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getInstitution() {
        return institution;
    }

    public Site institution(String institution) {
        this.institution = institution;
        return this;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Site streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public Site city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public Site state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public Site zip(String zip) {
        this.zip = zip;
        return this;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public Site country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<PrincipalInvestigator> getInvestigators() {
        return investigators;
    }

    public Site investigators(Set<PrincipalInvestigator> principalInvestigators) {
        this.investigators = principalInvestigators;
        return this;
    }

    public Site addInvestigators(PrincipalInvestigator principalInvestigator) {
        this.investigators.add(principalInvestigator);
        principalInvestigator.setSite(this);
        return this;
    }

    public Site removeInvestigators(PrincipalInvestigator principalInvestigator) {
        this.investigators.remove(principalInvestigator);
        principalInvestigator.setSite(null);
        return this;
    }

    public void setInvestigators(Set<PrincipalInvestigator> principalInvestigators) {
        this.investigators = principalInvestigators;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return id != null && id.equals(((Site) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", siteName='" + getSiteName() + "'" +
            ", institution='" + getInstitution() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zip='" + getZip() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}

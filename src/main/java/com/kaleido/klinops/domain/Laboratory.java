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

/**
 * A Laboratory that processes samples
 */
@ApiModel(description = "A Laboratory that processes samples")
@Entity
@Table(name = "laboratory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Laboratory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * A unique lab name
     */
    @NotNull
    @ApiModelProperty(value = "A unique lab name", required = true)
    @Column(name = "lab_name", nullable = false)
    private String labName;

    /**
     * Street address for the lab
     */
    @NotNull
    @ApiModelProperty(value = "Street address for the lab", required = true)
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    /**
     * City the lab is in
     */
    @NotNull
    @ApiModelProperty(value = "City the lab is in", required = true)
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * State the lab is in
     */
    @ApiModelProperty(value = "State the lab is in")
    @Column(name = "state")
    private String state;

    /**
     * Zip Code the lab is in
     */
    @NotNull
    @ApiModelProperty(value = "Zip Code the lab is in", required = true)
    @Column(name = "zip", nullable = false)
    private String zip;

    /**
     * Country the lab is in
     */
    @NotNull
    @ApiModelProperty(value = "Country the lab is in", required = true)
    @Column(name = "country", nullable = false)
    private String country;

    /**
     * Email contact for the lab
     */
    @Pattern(regexp = "^(.+)@(.+)|$")
    @ApiModelProperty(value = "Email contact for the lab")
    @Column(name = "lab_contact_email")
    private String labContactEmail;

    /**
     * Name of the main contact person at the lab
     */
    @ApiModelProperty(value = "Name of the main contact person at the lab")
    @Column(name = "lab_contact_name")
    private String labContactName;

    /**
     * Contact phone number
     */
    @ApiModelProperty(value = "Contact phone number")
    @Column(name = "lab_contact_phone_number")
    private String labContactPhoneNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabName() {
        return labName;
    }

    public Laboratory labName(String labName) {
        this.labName = labName;
        return this;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Laboratory streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public Laboratory city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public Laboratory state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public Laboratory zip(String zip) {
        this.zip = zip;
        return this;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public Laboratory country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLabContactEmail() {
        return labContactEmail;
    }

    public Laboratory labContactEmail(String labContactEmail) {
        this.labContactEmail = labContactEmail;
        return this;
    }

    public void setLabContactEmail(String labContactEmail) {
        this.labContactEmail = labContactEmail;
    }

    public String getLabContactName() {
        return labContactName;
    }

    public Laboratory labContactName(String labContactName) {
        this.labContactName = labContactName;
        return this;
    }

    public void setLabContactName(String labContactName) {
        this.labContactName = labContactName;
    }

    public String getLabContactPhoneNumber() {
        return labContactPhoneNumber;
    }

    public Laboratory labContactPhoneNumber(String labContactPhoneNumber) {
        this.labContactPhoneNumber = labContactPhoneNumber;
        return this;
    }

    public void setLabContactPhoneNumber(String labContactPhoneNumber) {
        this.labContactPhoneNumber = labContactPhoneNumber;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Laboratory)) {
            return false;
        }
        return id != null && id.equals(((Laboratory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Laboratory{" +
            "id=" + getId() +
            ", labName='" + getLabName() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zip='" + getZip() + "'" +
            ", country='" + getCountry() + "'" +
            ", labContactEmail='" + getLabContactEmail() + "'" +
            ", labContactName='" + getLabContactName() + "'" +
            ", labContactPhoneNumber='" + getLabContactPhoneNumber() + "'" +
            "}";
    }
}

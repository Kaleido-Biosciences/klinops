/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.kaleido.klinops.domain.PrincipalInvestigator} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.PrincipalInvestigatorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /principal-investigators?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrincipalInvestigatorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter investigatorName;

    private StringFilter streetAddress;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zip;

    private StringFilter country;

    private StringFilter email;

    private StringFilter phoneNumber;

    private LongFilter siteId;

    private LongFilter studiesId;

    public PrincipalInvestigatorCriteria(){
    }

    public PrincipalInvestigatorCriteria(PrincipalInvestigatorCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.investigatorName = other.investigatorName == null ? null : other.investigatorName.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.zip = other.zip == null ? null : other.zip.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.siteId = other.siteId == null ? null : other.siteId.copy();
        this.studiesId = other.studiesId == null ? null : other.studiesId.copy();
    }

    @Override
    public PrincipalInvestigatorCriteria copy() {
        return new PrincipalInvestigatorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getInvestigatorName() {
        return investigatorName;
    }

    public void setInvestigatorName(StringFilter investigatorName) {
        this.investigatorName = investigatorName;
    }

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getState() {
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getZip() {
        return zip;
    }

    public void setZip(StringFilter zip) {
        this.zip = zip;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LongFilter getSiteId() {
        return siteId;
    }

    public void setSiteId(LongFilter siteId) {
        this.siteId = siteId;
    }

    public LongFilter getStudiesId() {
        return studiesId;
    }

    public void setStudiesId(LongFilter studiesId) {
        this.studiesId = studiesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PrincipalInvestigatorCriteria that = (PrincipalInvestigatorCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(investigatorName, that.investigatorName) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zip, that.zip) &&
            Objects.equals(country, that.country) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(siteId, that.siteId) &&
            Objects.equals(studiesId, that.studiesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        investigatorName,
        streetAddress,
        city,
        state,
        zip,
        country,
        email,
        phoneNumber,
        siteId,
        studiesId
        );
    }

    @Override
    public String toString() {
        return "PrincipalInvestigatorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (investigatorName != null ? "investigatorName=" + investigatorName + ", " : "") +
                (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (zip != null ? "zip=" + zip + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (siteId != null ? "siteId=" + siteId + ", " : "") +
                (studiesId != null ? "studiesId=" + studiesId + ", " : "") +
            "}";
    }

}

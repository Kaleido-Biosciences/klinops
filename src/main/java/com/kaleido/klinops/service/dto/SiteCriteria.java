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
 * Criteria class for the {@link com.kaleido.klinops.domain.Site} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.SiteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SiteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter siteName;

    private StringFilter institution;

    private StringFilter streetAddress;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zip;

    private StringFilter country;

    private LongFilter investigatorsId;

    public SiteCriteria(){
    }

    public SiteCriteria(SiteCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.siteName = other.siteName == null ? null : other.siteName.copy();
        this.institution = other.institution == null ? null : other.institution.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.zip = other.zip == null ? null : other.zip.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.investigatorsId = other.investigatorsId == null ? null : other.investigatorsId.copy();
    }

    @Override
    public SiteCriteria copy() {
        return new SiteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSiteName() {
        return siteName;
    }

    public void setSiteName(StringFilter siteName) {
        this.siteName = siteName;
    }

    public StringFilter getInstitution() {
        return institution;
    }

    public void setInstitution(StringFilter institution) {
        this.institution = institution;
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

    public LongFilter getInvestigatorsId() {
        return investigatorsId;
    }

    public void setInvestigatorsId(LongFilter investigatorsId) {
        this.investigatorsId = investigatorsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SiteCriteria that = (SiteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(siteName, that.siteName) &&
            Objects.equals(institution, that.institution) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zip, that.zip) &&
            Objects.equals(country, that.country) &&
            Objects.equals(investigatorsId, that.investigatorsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        siteName,
        institution,
        streetAddress,
        city,
        state,
        zip,
        country,
        investigatorsId
        );
    }

    @Override
    public String toString() {
        return "SiteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (siteName != null ? "siteName=" + siteName + ", " : "") +
                (institution != null ? "institution=" + institution + ", " : "") +
                (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (zip != null ? "zip=" + zip + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (investigatorsId != null ? "investigatorsId=" + investigatorsId + ", " : "") +
            "}";
    }

}

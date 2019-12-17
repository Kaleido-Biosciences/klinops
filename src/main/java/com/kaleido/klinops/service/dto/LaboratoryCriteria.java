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
 * Criteria class for the {@link com.kaleido.klinops.domain.Laboratory} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.LaboratoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /laboratories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LaboratoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter labName;

    private StringFilter streetAddress;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zip;

    private StringFilter country;

    private StringFilter labContactEmail;

    private StringFilter labContactName;

    private StringFilter labContactPhoneNumber;

    public LaboratoryCriteria(){
    }

    public LaboratoryCriteria(LaboratoryCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.labName = other.labName == null ? null : other.labName.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.zip = other.zip == null ? null : other.zip.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.labContactEmail = other.labContactEmail == null ? null : other.labContactEmail.copy();
        this.labContactName = other.labContactName == null ? null : other.labContactName.copy();
        this.labContactPhoneNumber = other.labContactPhoneNumber == null ? null : other.labContactPhoneNumber.copy();
    }

    @Override
    public LaboratoryCriteria copy() {
        return new LaboratoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLabName() {
        return labName;
    }

    public void setLabName(StringFilter labName) {
        this.labName = labName;
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

    public StringFilter getLabContactEmail() {
        return labContactEmail;
    }

    public void setLabContactEmail(StringFilter labContactEmail) {
        this.labContactEmail = labContactEmail;
    }

    public StringFilter getLabContactName() {
        return labContactName;
    }

    public void setLabContactName(StringFilter labContactName) {
        this.labContactName = labContactName;
    }

    public StringFilter getLabContactPhoneNumber() {
        return labContactPhoneNumber;
    }

    public void setLabContactPhoneNumber(StringFilter labContactPhoneNumber) {
        this.labContactPhoneNumber = labContactPhoneNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LaboratoryCriteria that = (LaboratoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(labName, that.labName) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zip, that.zip) &&
            Objects.equals(country, that.country) &&
            Objects.equals(labContactEmail, that.labContactEmail) &&
            Objects.equals(labContactName, that.labContactName) &&
            Objects.equals(labContactPhoneNumber, that.labContactPhoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        labName,
        streetAddress,
        city,
        state,
        zip,
        country,
        labContactEmail,
        labContactName,
        labContactPhoneNumber
        );
    }

    @Override
    public String toString() {
        return "LaboratoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (labName != null ? "labName=" + labName + ", " : "") +
                (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (zip != null ? "zip=" + zip + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (labContactEmail != null ? "labContactEmail=" + labContactEmail + ", " : "") +
                (labContactName != null ? "labContactName=" + labContactName + ", " : "") +
                (labContactPhoneNumber != null ? "labContactPhoneNumber=" + labContactPhoneNumber + ", " : "") +
            "}";
    }

}

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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.kaleido.klinops.domain.Shipment} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.ShipmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shipments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShipmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter shipmentCode;

    private LocalDateFilter dateShipped;

    private LocalDateFilter dateReceived;

    private LongFilter componentsId;

    private LongFilter destinationId;

    private LongFilter studyId;

    public ShipmentCriteria(){
    }

    public ShipmentCriteria(ShipmentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.shipmentCode = other.shipmentCode == null ? null : other.shipmentCode.copy();
        this.dateShipped = other.dateShipped == null ? null : other.dateShipped.copy();
        this.dateReceived = other.dateReceived == null ? null : other.dateReceived.copy();
        this.componentsId = other.componentsId == null ? null : other.componentsId.copy();
        this.destinationId = other.destinationId == null ? null : other.destinationId.copy();
        this.studyId = other.studyId == null ? null : other.studyId.copy();
    }

    @Override
    public ShipmentCriteria copy() {
        return new ShipmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(StringFilter shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public LocalDateFilter getDateShipped() {
        return dateShipped;
    }

    public void setDateShipped(LocalDateFilter dateShipped) {
        this.dateShipped = dateShipped;
    }

    public LocalDateFilter getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDateFilter dateReceived) {
        this.dateReceived = dateReceived;
    }

    public LongFilter getComponentsId() {
        return componentsId;
    }

    public void setComponentsId(LongFilter componentsId) {
        this.componentsId = componentsId;
    }

    public LongFilter getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(LongFilter destinationId) {
        this.destinationId = destinationId;
    }

    public LongFilter getStudyId() {
        return studyId;
    }

    public void setStudyId(LongFilter studyId) {
        this.studyId = studyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShipmentCriteria that = (ShipmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(shipmentCode, that.shipmentCode) &&
            Objects.equals(dateShipped, that.dateShipped) &&
            Objects.equals(dateReceived, that.dateReceived) &&
            Objects.equals(componentsId, that.componentsId) &&
            Objects.equals(destinationId, that.destinationId) &&
            Objects.equals(studyId, that.studyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        shipmentCode,
        dateShipped,
        dateReceived,
        componentsId,
        destinationId,
        studyId
        );
    }

    @Override
    public String toString() {
        return "ShipmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (shipmentCode != null ? "shipmentCode=" + shipmentCode + ", " : "") +
                (dateShipped != null ? "dateShipped=" + dateShipped + ", " : "") +
                (dateReceived != null ? "dateReceived=" + dateReceived + ", " : "") +
                (componentsId != null ? "componentsId=" + componentsId + ", " : "") +
                (destinationId != null ? "destinationId=" + destinationId + ", " : "") +
                (studyId != null ? "studyId=" + studyId + ", " : "") +
            "}";
    }

}

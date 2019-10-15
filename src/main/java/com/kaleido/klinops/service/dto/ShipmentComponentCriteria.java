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
 * Criteria class for the {@link com.kaleido.klinops.domain.ShipmentComponent} entity. This class is used
 * in {@link com.kaleido.klinops.web.rest.ShipmentComponentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shipment-components?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShipmentComponentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sampleType;

    private IntegerFilter sampleCount;

    private LongFilter shipmentId;

    public ShipmentComponentCriteria(){
    }

    public ShipmentComponentCriteria(ShipmentComponentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.sampleType = other.sampleType == null ? null : other.sampleType.copy();
        this.sampleCount = other.sampleCount == null ? null : other.sampleCount.copy();
        this.shipmentId = other.shipmentId == null ? null : other.shipmentId.copy();
    }

    @Override
    public ShipmentComponentCriteria copy() {
        return new ShipmentComponentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSampleType() {
        return sampleType;
    }

    public void setSampleType(StringFilter sampleType) {
        this.sampleType = sampleType;
    }

    public IntegerFilter getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(IntegerFilter sampleCount) {
        this.sampleCount = sampleCount;
    }

    public LongFilter getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(LongFilter shipmentId) {
        this.shipmentId = shipmentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShipmentComponentCriteria that = (ShipmentComponentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sampleType, that.sampleType) &&
            Objects.equals(sampleCount, that.sampleCount) &&
            Objects.equals(shipmentId, that.shipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sampleType,
        sampleCount,
        shipmentId
        );
    }

    @Override
    public String toString() {
        return "ShipmentComponentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sampleType != null ? "sampleType=" + sampleType + ", " : "") +
                (sampleCount != null ? "sampleCount=" + sampleCount + ", " : "") +
                (shipmentId != null ? "shipmentId=" + shipmentId + ", " : "") +
            "}";
    }

}

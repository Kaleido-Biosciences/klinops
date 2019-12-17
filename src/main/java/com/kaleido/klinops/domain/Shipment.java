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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A sample shipment from a Study
 */
@ApiModel(description = "A sample shipment from a Study")
@Entity
@Table(name = "shipment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shipment")
public class Shipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    /**
     * A unique shipment code or reference
     */
    @NotNull
    @ApiModelProperty(value = "A unique shipment code or reference", required = true)
    @Column(name = "shipment_code", nullable = false)
    private String shipmentCode;

    /**
     * The date the Shipment was dispatched
     */
    @NotNull
    @ApiModelProperty(value = "The date the Shipment was dispatched", required = true)
    @Column(name = "date_shipped", nullable = false)
    private LocalDate dateShipped;

    /**
     * The date the sample was received by the destination
     */
    @ApiModelProperty(value = "The date the sample was received by the destination")
    @Column(name = "date_received")
    private LocalDate dateReceived;

    /**
     * The components of this shipment
     */
    @ApiModelProperty(value = "The components of this shipment")
    @OneToMany(mappedBy = "shipment")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShipmentComponent> components = new HashSet<>();

    /**
     * The destination lab for the shipment
     */
    @ApiModelProperty(value = "The destination lab for the shipment")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("shipments")
    private Laboratory destination;

    /**
     * The study to which this shipment belongs
     */
    @ApiModelProperty(value = "The study to which this shipment belongs")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("shipments")
    private ClinicalStudy study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public Shipment shipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
        return this;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public LocalDate getDateShipped() {
        return dateShipped;
    }

    public Shipment dateShipped(LocalDate dateShipped) {
        this.dateShipped = dateShipped;
        return this;
    }

    public void setDateShipped(LocalDate dateShipped) {
        this.dateShipped = dateShipped;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public Shipment dateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
        return this;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Set<ShipmentComponent> getComponents() {
        return components;
    }

    public Shipment components(Set<ShipmentComponent> shipmentComponents) {
        this.components = shipmentComponents;
        return this;
    }

    public Shipment addComponents(ShipmentComponent shipmentComponent) {
        this.components.add(shipmentComponent);
        shipmentComponent.setShipment(this);
        return this;
    }

    public Shipment removeComponents(ShipmentComponent shipmentComponent) {
        this.components.remove(shipmentComponent);
        shipmentComponent.setShipment(null);
        return this;
    }

    public void setComponents(Set<ShipmentComponent> shipmentComponents) {
        this.components = shipmentComponents;
    }

    public Laboratory getDestination() {
        return destination;
    }

    public Shipment destination(Laboratory laboratory) {
        this.destination = laboratory;
        return this;
    }

    public void setDestination(Laboratory laboratory) {
        this.destination = laboratory;
    }

    public ClinicalStudy getStudy() {
        return study;
    }

    public Shipment study(ClinicalStudy clinicalStudy) {
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
        if (!(o instanceof Shipment)) {
            return false;
        }
        return id != null && id.equals(((Shipment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Shipment{" +
            "id=" + getId() +
            ", shipmentCode='" + getShipmentCode() + "'" +
            ", dateShipped='" + getDateShipped() + "'" +
            ", dateReceived='" + getDateReceived() + "'" +
            "}";
    }
}

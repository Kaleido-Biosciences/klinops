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

/**
 * A component of a shipment, the type and count of a sample
 */
@ApiModel(description = "A component of a shipment, the type and count of a sample")
@Entity
@Table(name = "shipment_component")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shipmentcomponent")
public class ShipmentComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    /**
     * The type or category of sample
     */
    @NotNull
    @ApiModelProperty(value = "The type or category of sample", required = true)
    @Column(name = "sample_type", nullable = false)
    private String sampleType;

    /**
     * Number of samples of this type in the shipment
     */
    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "Number of samples of this type in the shipment", required = true)
    @Column(name = "sample_count", nullable = false)
    private Integer sampleCount;

    /**
     * The shipment that this is a component of
     */
    @ApiModelProperty(value = "The shipment that this is a component of")
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("components")
    private Shipment shipment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSampleType() {
        return sampleType;
    }

    public ShipmentComponent sampleType(String sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public Integer getSampleCount() {
        return sampleCount;
    }

    public ShipmentComponent sampleCount(Integer sampleCount) {
        this.sampleCount = sampleCount;
        return this;
    }

    public void setSampleCount(Integer sampleCount) {
        this.sampleCount = sampleCount;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public ShipmentComponent shipment(Shipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentComponent)) {
            return false;
        }
        return id != null && id.equals(((ShipmentComponent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShipmentComponent{" +
            "id=" + getId() +
            ", sampleType='" + getSampleType() + "'" +
            ", sampleCount=" + getSampleCount() +
            "}";
    }
}

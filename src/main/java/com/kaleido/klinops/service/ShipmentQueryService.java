package com.kaleido.klinops.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.kaleido.klinops.domain.Shipment;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.ShipmentRepository;
import com.kaleido.klinops.service.dto.ShipmentCriteria;

/**
 * Service for executing complex queries for {@link Shipment} entities in the database.
 * The main input is a {@link ShipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Shipment} or a {@link Page} of {@link Shipment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentQueryService extends QueryService<Shipment> {

    private final Logger log = LoggerFactory.getLogger(ShipmentQueryService.class);

    private final ShipmentRepository shipmentRepository;

    public ShipmentQueryService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Return a {@link List} of {@link Shipment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Shipment> findByCriteria(ShipmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Shipment> specification = createSpecification(criteria);
        return shipmentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Shipment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Shipment> findByCriteria(ShipmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Shipment> specification = createSpecification(criteria);
        return shipmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Shipment> specification = createSpecification(criteria);
        return shipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Shipment> createSpecification(ShipmentCriteria criteria) {
        Specification<Shipment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Shipment_.id));
            }
            if (criteria.getShipmentCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShipmentCode(), Shipment_.shipmentCode));
            }
            if (criteria.getDateShipped() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateShipped(), Shipment_.dateShipped));
            }
            if (criteria.getDateReceived() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateReceived(), Shipment_.dateReceived));
            }
            if (criteria.getComponentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getComponentsId(),
                    root -> root.join(Shipment_.components, JoinType.LEFT).get(ShipmentComponent_.id)));
            }
            if (criteria.getDestinationId() != null) {
                specification = specification.and(buildSpecification(criteria.getDestinationId(),
                    root -> root.join(Shipment_.destination, JoinType.LEFT).get(Laboratory_.id)));
            }
            if (criteria.getStudyId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudyId(),
                    root -> root.join(Shipment_.study, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
        }
        return specification;
    }
}

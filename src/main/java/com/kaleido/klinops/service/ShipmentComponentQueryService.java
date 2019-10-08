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

import com.kaleido.klinops.domain.ShipmentComponent;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.ShipmentComponentRepository;
import com.kaleido.klinops.service.dto.ShipmentComponentCriteria;

/**
 * Service for executing complex queries for {@link ShipmentComponent} entities in the database.
 * The main input is a {@link ShipmentComponentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShipmentComponent} or a {@link Page} of {@link ShipmentComponent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentComponentQueryService extends QueryService<ShipmentComponent> {

    private final Logger log = LoggerFactory.getLogger(ShipmentComponentQueryService.class);

    private final ShipmentComponentRepository shipmentComponentRepository;

    public ShipmentComponentQueryService(ShipmentComponentRepository shipmentComponentRepository) {
        this.shipmentComponentRepository = shipmentComponentRepository;
    }

    /**
     * Return a {@link List} of {@link ShipmentComponent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShipmentComponent> findByCriteria(ShipmentComponentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShipmentComponent> specification = createSpecification(criteria);
        return shipmentComponentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ShipmentComponent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentComponent> findByCriteria(ShipmentComponentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentComponent> specification = createSpecification(criteria);
        return shipmentComponentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentComponentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShipmentComponent> specification = createSpecification(criteria);
        return shipmentComponentRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentComponentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentComponent> createSpecification(ShipmentComponentCriteria criteria) {
        Specification<ShipmentComponent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ShipmentComponent_.id));
            }
            if (criteria.getSampleType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSampleType(), ShipmentComponent_.sampleType));
            }
            if (criteria.getSampleCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSampleCount(), ShipmentComponent_.sampleCount));
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getShipmentId(),
                    root -> root.join(ShipmentComponent_.shipment, JoinType.LEFT).get(Shipment_.id)));
            }
        }
        return specification;
    }
}

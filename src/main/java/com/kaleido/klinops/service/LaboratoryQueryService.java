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

import com.kaleido.klinops.domain.Laboratory;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.LaboratoryRepository;
import com.kaleido.klinops.service.dto.LaboratoryCriteria;

/**
 * Service for executing complex queries for {@link Laboratory} entities in the database.
 * The main input is a {@link LaboratoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Laboratory} or a {@link Page} of {@link Laboratory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LaboratoryQueryService extends QueryService<Laboratory> {

    private final Logger log = LoggerFactory.getLogger(LaboratoryQueryService.class);

    private final LaboratoryRepository laboratoryRepository;

    public LaboratoryQueryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    /**
     * Return a {@link List} of {@link Laboratory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Laboratory> findByCriteria(LaboratoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Laboratory> specification = createSpecification(criteria);
        return laboratoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Laboratory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Laboratory> findByCriteria(LaboratoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Laboratory> specification = createSpecification(criteria);
        return laboratoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LaboratoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Laboratory> specification = createSpecification(criteria);
        return laboratoryRepository.count(specification);
    }

    /**
     * Function to convert {@link LaboratoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Laboratory> createSpecification(LaboratoryCriteria criteria) {
        Specification<Laboratory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Laboratory_.id));
            }
            if (criteria.getLabName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabName(), Laboratory_.labName));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Laboratory_.streetAddress));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Laboratory_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Laboratory_.state));
            }
            if (criteria.getZip() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZip(), Laboratory_.zip));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Laboratory_.country));
            }
            if (criteria.getLabContactEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabContactEmail(), Laboratory_.labContactEmail));
            }
            if (criteria.getLabContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabContactName(), Laboratory_.labContactName));
            }
            if (criteria.getLabContactPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabContactPhoneNumber(), Laboratory_.labContactPhoneNumber));
            }
        }
        return specification;
    }
}

/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

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

import com.kaleido.klinops.domain.StudyProduct;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.StudyProductRepository;
import com.kaleido.klinops.service.dto.StudyProductCriteria;

/**
 * Service for executing complex queries for {@link StudyProduct} entities in the database.
 * The main input is a {@link StudyProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudyProduct} or a {@link Page} of {@link StudyProduct} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudyProductQueryService extends QueryService<StudyProduct> {

    private final Logger log = LoggerFactory.getLogger(StudyProductQueryService.class);

    private final StudyProductRepository studyProductRepository;

    public StudyProductQueryService(StudyProductRepository studyProductRepository) {
        this.studyProductRepository = studyProductRepository;
    }

    /**
     * Return a {@link List} of {@link StudyProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudyProduct> findByCriteria(StudyProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudyProduct> specification = createSpecification(criteria);
        return studyProductRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StudyProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyProduct> findByCriteria(StudyProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudyProduct> specification = createSpecification(criteria);
        return studyProductRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudyProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudyProduct> specification = createSpecification(criteria);
        return studyProductRepository.count(specification);
    }

    /**
     * Function to convert {@link StudyProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudyProduct> createSpecification(StudyProductCriteria criteria) {
        Specification<StudyProduct> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StudyProduct_.id));
            }
            if (criteria.getProductName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductName(), StudyProduct_.productName));
            }
            if (criteria.getDoseRange() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDoseRange(), StudyProduct_.doseRange));
            }
            if (criteria.getDaysOfExposure() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDaysOfExposure(), StudyProduct_.daysOfExposure));
            }
            if (criteria.getFormulation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFormulation(), StudyProduct_.formulation));
            }
            if (criteria.getStudyId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudyId(),
                    root -> root.join(StudyProduct_.study, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
        }
        return specification;
    }
}

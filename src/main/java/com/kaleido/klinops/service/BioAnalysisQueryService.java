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

import com.kaleido.klinops.domain.BioAnalysis;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.BioAnalysisRepository;
import com.kaleido.klinops.service.dto.BioAnalysisCriteria;

/**
 * Service for executing complex queries for {@link BioAnalysis} entities in the database.
 * The main input is a {@link BioAnalysisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BioAnalysis} or a {@link Page} of {@link BioAnalysis} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BioAnalysisQueryService extends QueryService<BioAnalysis> {

    private final Logger log = LoggerFactory.getLogger(BioAnalysisQueryService.class);

    private final BioAnalysisRepository bioAnalysisRepository;

    public BioAnalysisQueryService(BioAnalysisRepository bioAnalysisRepository) {
        this.bioAnalysisRepository = bioAnalysisRepository;
    }

    /**
     * Return a {@link List} of {@link BioAnalysis} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BioAnalysis> findByCriteria(BioAnalysisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BioAnalysis> specification = createSpecification(criteria);
        return bioAnalysisRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BioAnalysis} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BioAnalysis> findByCriteria(BioAnalysisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BioAnalysis> specification = createSpecification(criteria);
        return bioAnalysisRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BioAnalysisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BioAnalysis> specification = createSpecification(criteria);
        return bioAnalysisRepository.count(specification);
    }

    /**
     * Function to convert {@link BioAnalysisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BioAnalysis> createSpecification(BioAnalysisCriteria criteria) {
        Specification<BioAnalysis> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BioAnalysis_.id));
            }
            if (criteria.getAnalyte() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnalyte(), BioAnalysis_.analyte));
            }
            if (criteria.getSampleType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSampleType(), BioAnalysis_.sampleType));
            }
            if (criteria.getBioAnalysisType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBioAnalysisType(), BioAnalysis_.bioAnalysisType));
            }
            if (criteria.getAnticipatedLabWorkStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnticipatedLabWorkStartDate(), BioAnalysis_.anticipatedLabWorkStartDate));
            }
            if (criteria.getActualLabWorkStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualLabWorkStartDate(), BioAnalysis_.actualLabWorkStartDate));
            }
            if (criteria.getAnticipatedLabResultDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnticipatedLabResultDeliveryDate(), BioAnalysis_.anticipatedLabResultDeliveryDate));
            }
            if (criteria.getActualLabResultDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualLabResultDeliveryDate(), BioAnalysis_.actualLabResultDeliveryDate));
            }
            if (criteria.getDataLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataLocation(), BioAnalysis_.dataLocation));
            }
            if (criteria.getContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactName(), BioAnalysis_.contactName));
            }
            if (criteria.getContactEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactEmail(), BioAnalysis_.contactEmail));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), BioAnalysis_.comments));
            }
            if (criteria.getStudyEndPointId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudyEndPointId(),
                    root -> root.join(BioAnalysis_.studyEndPoint, JoinType.LEFT).get(StudyEndPoint_.id)));
            }
            if (criteria.getLaboratoriesId() != null) {
                specification = specification.and(buildSpecification(criteria.getLaboratoriesId(),
                    root -> root.join(BioAnalysis_.laboratories, JoinType.LEFT).get(Laboratory_.id)));
            }
            if (criteria.getStudyId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudyId(),
                    root -> root.join(BioAnalysis_.study, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
            if (criteria.getDataAnalysesId() != null) {
                specification = specification.and(buildSpecification(criteria.getDataAnalysesId(),
                    root -> root.join(BioAnalysis_.dataAnalyses, JoinType.LEFT).get(DataAnalysis_.id)));
            }
        }
        return specification;
    }
}

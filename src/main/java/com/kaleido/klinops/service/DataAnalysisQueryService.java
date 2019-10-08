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

import com.kaleido.klinops.domain.DataAnalysis;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.DataAnalysisRepository;
import com.kaleido.klinops.service.dto.DataAnalysisCriteria;

/**
 * Service for executing complex queries for {@link DataAnalysis} entities in the database.
 * The main input is a {@link DataAnalysisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DataAnalysis} or a {@link Page} of {@link DataAnalysis} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DataAnalysisQueryService extends QueryService<DataAnalysis> {

    private final Logger log = LoggerFactory.getLogger(DataAnalysisQueryService.class);

    private final DataAnalysisRepository dataAnalysisRepository;

    public DataAnalysisQueryService(DataAnalysisRepository dataAnalysisRepository) {
        this.dataAnalysisRepository = dataAnalysisRepository;
    }

    /**
     * Return a {@link List} of {@link DataAnalysis} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataAnalysis> findByCriteria(DataAnalysisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DataAnalysis> specification = createSpecification(criteria);
        return dataAnalysisRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DataAnalysis} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataAnalysis> findByCriteria(DataAnalysisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DataAnalysis> specification = createSpecification(criteria);
        return dataAnalysisRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataAnalysisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DataAnalysis> specification = createSpecification(criteria);
        return dataAnalysisRepository.count(specification);
    }

    /**
     * Function to convert {@link DataAnalysisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DataAnalysis> createSpecification(DataAnalysisCriteria criteria) {
        Specification<DataAnalysis> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DataAnalysis_.id));
            }
            if (criteria.getDataAnalysesType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataAnalysesType(), DataAnalysis_.dataAnalysesType));
            }
            if (criteria.getContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactName(), DataAnalysis_.contactName));
            }
            if (criteria.getContactEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactEmail(), DataAnalysis_.contactEmail));
            }
            if (criteria.getAnticipatedAnalysisDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnticipatedAnalysisDeliveryDate(), DataAnalysis_.anticipatedAnalysisDeliveryDate));
            }
            if (criteria.getActualAnalysisDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualAnalysisDeliveryDate(), DataAnalysis_.actualAnalysisDeliveryDate));
            }
            if (criteria.getDataLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataLocation(), DataAnalysis_.dataLocation));
            }
            if (criteria.getBioAnalysesId() != null) {
                specification = specification.and(buildSpecification(criteria.getBioAnalysesId(),
                    root -> root.join(DataAnalysis_.bioAnalyses, JoinType.LEFT).get(BioAnalysis_.id)));
            }
            if (criteria.getStudyId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudyId(),
                    root -> root.join(DataAnalysis_.study, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
        }
        return specification;
    }
}

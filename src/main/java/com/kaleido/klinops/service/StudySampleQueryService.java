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

import com.kaleido.klinops.domain.StudySample;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.StudySampleRepository;
import com.kaleido.klinops.service.dto.StudySampleCriteria;

/**
 * Service for executing complex queries for {@link StudySample} entities in the database.
 * The main input is a {@link StudySampleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudySample} or a {@link Page} of {@link StudySample} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudySampleQueryService extends QueryService<StudySample> {

    private final Logger log = LoggerFactory.getLogger(StudySampleQueryService.class);

    private final StudySampleRepository studySampleRepository;

    public StudySampleQueryService(StudySampleRepository studySampleRepository) {
        this.studySampleRepository = studySampleRepository;
    }

    /**
     * Return a {@link List} of {@link StudySample} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudySample> findByCriteria(StudySampleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudySample> specification = createSpecification(criteria);
        return studySampleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StudySample} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudySample> findByCriteria(StudySampleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudySample> specification = createSpecification(criteria);
        return studySampleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudySampleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudySample> specification = createSpecification(criteria);
        return studySampleRepository.count(specification);
    }

    /**
     * Function to convert {@link StudySampleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudySample> createSpecification(StudySampleCriteria criteria) {
        Specification<StudySample> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StudySample_.id));
            }
            if (criteria.getSampleType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSampleType(), StudySample_.sampleType));
            }
            if (criteria.getExpectedNumberOfSamples() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpectedNumberOfSamples(), StudySample_.expectedNumberOfSamples));
            }
            if (criteria.getStudyId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudyId(),
                    root -> root.join(StudySample_.study, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
        }
        return specification;
    }
}

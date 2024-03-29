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

import com.kaleido.klinops.domain.StudyEndPoint;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.StudyEndPointRepository;
import com.kaleido.klinops.repository.search.StudyEndPointSearchRepository;
import com.kaleido.klinops.service.dto.StudyEndPointCriteria;

/**
 * Service for executing complex queries for {@link StudyEndPoint} entities in the database.
 * The main input is a {@link StudyEndPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudyEndPoint} or a {@link Page} of {@link StudyEndPoint} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudyEndPointQueryService extends QueryService<StudyEndPoint> {

    private final Logger log = LoggerFactory.getLogger(StudyEndPointQueryService.class);

    private final StudyEndPointRepository studyEndPointRepository;

    private final StudyEndPointSearchRepository studyEndPointSearchRepository;

    public StudyEndPointQueryService(StudyEndPointRepository studyEndPointRepository, StudyEndPointSearchRepository studyEndPointSearchRepository) {
        this.studyEndPointRepository = studyEndPointRepository;
        this.studyEndPointSearchRepository = studyEndPointSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StudyEndPoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudyEndPoint> findByCriteria(StudyEndPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudyEndPoint> specification = createSpecification(criteria);
        return studyEndPointRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StudyEndPoint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyEndPoint> findByCriteria(StudyEndPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudyEndPoint> specification = createSpecification(criteria);
        return studyEndPointRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudyEndPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudyEndPoint> specification = createSpecification(criteria);
        return studyEndPointRepository.count(specification);
    }

    /**
     * Function to convert {@link StudyEndPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudyEndPoint> createSpecification(StudyEndPointCriteria criteria) {
        Specification<StudyEndPoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StudyEndPoint_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), StudyEndPoint_.description));
            }
            if (criteria.getObjective() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObjective(), StudyEndPoint_.objective));
            }
            if (criteria.getEndPointType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndPointType(), StudyEndPoint_.endPointType));
            }
            if (criteria.getStudyId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudyId(),
                    root -> root.join(StudyEndPoint_.study, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
        }
        return specification;
    }
}

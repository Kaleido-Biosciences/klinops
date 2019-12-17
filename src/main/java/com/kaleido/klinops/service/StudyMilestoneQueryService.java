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

import com.kaleido.klinops.domain.StudyMilestone;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.StudyMilestoneRepository;
import com.kaleido.klinops.repository.search.StudyMilestoneSearchRepository;
import com.kaleido.klinops.service.dto.StudyMilestoneCriteria;

/**
 * Service for executing complex queries for {@link StudyMilestone} entities in the database.
 * The main input is a {@link StudyMilestoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudyMilestone} or a {@link Page} of {@link StudyMilestone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudyMilestoneQueryService extends QueryService<StudyMilestone> {

    private final Logger log = LoggerFactory.getLogger(StudyMilestoneQueryService.class);

    private final StudyMilestoneRepository studyMilestoneRepository;

    private final StudyMilestoneSearchRepository studyMilestoneSearchRepository;

    public StudyMilestoneQueryService(StudyMilestoneRepository studyMilestoneRepository, StudyMilestoneSearchRepository studyMilestoneSearchRepository) {
        this.studyMilestoneRepository = studyMilestoneRepository;
        this.studyMilestoneSearchRepository = studyMilestoneSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StudyMilestone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudyMilestone> findByCriteria(StudyMilestoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudyMilestone> specification = createSpecification(criteria);
        return studyMilestoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StudyMilestone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyMilestone> findByCriteria(StudyMilestoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudyMilestone> specification = createSpecification(criteria);
        return studyMilestoneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudyMilestoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudyMilestone> specification = createSpecification(criteria);
        return studyMilestoneRepository.count(specification);
    }

    /**
     * Function to convert {@link StudyMilestoneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudyMilestone> createSpecification(StudyMilestoneCriteria criteria) {
        Specification<StudyMilestone> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StudyMilestone_.id));
            }
            if (criteria.getMileStoneName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMileStoneName(), StudyMilestone_.mileStoneName));
            }
            if (criteria.getMileStoneType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMileStoneType(), StudyMilestone_.mileStoneType));
            }
            if (criteria.getProjectedCompletionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProjectedCompletionDate(), StudyMilestone_.projectedCompletionDate));
            }
            if (criteria.getActualCompletionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualCompletionDate(), StudyMilestone_.actualCompletionDate));
            }
            if (criteria.getStudyId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudyId(),
                    root -> root.join(StudyMilestone_.study, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
        }
        return specification;
    }
}

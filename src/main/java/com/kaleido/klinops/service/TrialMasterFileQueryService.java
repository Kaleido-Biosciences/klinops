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

import com.kaleido.klinops.domain.TrialMasterFile;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.TrialMasterFileRepository;
import com.kaleido.klinops.service.dto.TrialMasterFileCriteria;

/**
 * Service for executing complex queries for {@link TrialMasterFile} entities in the database.
 * The main input is a {@link TrialMasterFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TrialMasterFile} or a {@link Page} of {@link TrialMasterFile} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrialMasterFileQueryService extends QueryService<TrialMasterFile> {

    private final Logger log = LoggerFactory.getLogger(TrialMasterFileQueryService.class);

    private final TrialMasterFileRepository trialMasterFileRepository;

    public TrialMasterFileQueryService(TrialMasterFileRepository trialMasterFileRepository) {
        this.trialMasterFileRepository = trialMasterFileRepository;
    }

    /**
     * Return a {@link List} of {@link TrialMasterFile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TrialMasterFile> findByCriteria(TrialMasterFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TrialMasterFile> specification = createSpecification(criteria);
        return trialMasterFileRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TrialMasterFile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrialMasterFile> findByCriteria(TrialMasterFileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TrialMasterFile> specification = createSpecification(criteria);
        return trialMasterFileRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrialMasterFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TrialMasterFile> specification = createSpecification(criteria);
        return trialMasterFileRepository.count(specification);
    }

    /**
     * Function to convert {@link TrialMasterFileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TrialMasterFile> createSpecification(TrialMasterFileCriteria criteria) {
        Specification<TrialMasterFile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TrialMasterFile_.id));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), TrialMasterFile_.fileName));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), TrialMasterFile_.location));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), TrialMasterFile_.status));
            }
            if (criteria.getElectronic() != null) {
                specification = specification.and(buildSpecification(criteria.getElectronic(), TrialMasterFile_.electronic));
            }
            if (criteria.getClinicalStudyId() != null) {
                specification = specification.and(buildSpecification(criteria.getClinicalStudyId(),
                    root -> root.join(TrialMasterFile_.clinicalStudy, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
        }
        return specification;
    }
}

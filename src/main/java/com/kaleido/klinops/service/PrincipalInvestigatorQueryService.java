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

import com.kaleido.klinops.domain.PrincipalInvestigator;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.PrincipalInvestigatorRepository;
import com.kaleido.klinops.repository.search.PrincipalInvestigatorSearchRepository;
import com.kaleido.klinops.service.dto.PrincipalInvestigatorCriteria;

/**
 * Service for executing complex queries for {@link PrincipalInvestigator} entities in the database.
 * The main input is a {@link PrincipalInvestigatorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrincipalInvestigator} or a {@link Page} of {@link PrincipalInvestigator} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrincipalInvestigatorQueryService extends QueryService<PrincipalInvestigator> {

    private final Logger log = LoggerFactory.getLogger(PrincipalInvestigatorQueryService.class);

    private final PrincipalInvestigatorRepository principalInvestigatorRepository;

    private final PrincipalInvestigatorSearchRepository principalInvestigatorSearchRepository;

    public PrincipalInvestigatorQueryService(PrincipalInvestigatorRepository principalInvestigatorRepository, PrincipalInvestigatorSearchRepository principalInvestigatorSearchRepository) {
        this.principalInvestigatorRepository = principalInvestigatorRepository;
        this.principalInvestigatorSearchRepository = principalInvestigatorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrincipalInvestigator} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrincipalInvestigator> findByCriteria(PrincipalInvestigatorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrincipalInvestigator> specification = createSpecification(criteria);
        return principalInvestigatorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PrincipalInvestigator} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrincipalInvestigator> findByCriteria(PrincipalInvestigatorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrincipalInvestigator> specification = createSpecification(criteria);
        return principalInvestigatorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrincipalInvestigatorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrincipalInvestigator> specification = createSpecification(criteria);
        return principalInvestigatorRepository.count(specification);
    }

    /**
     * Function to convert {@link PrincipalInvestigatorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrincipalInvestigator> createSpecification(PrincipalInvestigatorCriteria criteria) {
        Specification<PrincipalInvestigator> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PrincipalInvestigator_.id));
            }
            if (criteria.getInvestigatorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvestigatorName(), PrincipalInvestigator_.investigatorName));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), PrincipalInvestigator_.streetAddress));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), PrincipalInvestigator_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), PrincipalInvestigator_.state));
            }
            if (criteria.getZip() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZip(), PrincipalInvestigator_.zip));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), PrincipalInvestigator_.country));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), PrincipalInvestigator_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), PrincipalInvestigator_.phoneNumber));
            }
            if (criteria.getSiteId() != null) {
                specification = specification.and(buildSpecification(criteria.getSiteId(),
                    root -> root.join(PrincipalInvestigator_.site, JoinType.LEFT).get(Site_.id)));
            }
            if (criteria.getStudiesId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudiesId(),
                    root -> root.join(PrincipalInvestigator_.studies, JoinType.LEFT).get(ClinicalStudy_.id)));
            }
        }
        return specification;
    }
}

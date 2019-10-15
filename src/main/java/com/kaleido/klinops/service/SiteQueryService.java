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

import com.kaleido.klinops.domain.Site;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.SiteRepository;
import com.kaleido.klinops.service.dto.SiteCriteria;

/**
 * Service for executing complex queries for {@link Site} entities in the database.
 * The main input is a {@link SiteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Site} or a {@link Page} of {@link Site} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SiteQueryService extends QueryService<Site> {

    private final Logger log = LoggerFactory.getLogger(SiteQueryService.class);

    private final SiteRepository siteRepository;

    public SiteQueryService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    /**
     * Return a {@link List} of {@link Site} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Site> findByCriteria(SiteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Site> specification = createSpecification(criteria);
        return siteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Site} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Site> findByCriteria(SiteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Site> specification = createSpecification(criteria);
        return siteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SiteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Site> specification = createSpecification(criteria);
        return siteRepository.count(specification);
    }

    /**
     * Function to convert {@link SiteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Site> createSpecification(SiteCriteria criteria) {
        Specification<Site> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Site_.id));
            }
            if (criteria.getSiteName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSiteName(), Site_.siteName));
            }
            if (criteria.getInstitution() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInstitution(), Site_.institution));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Site_.streetAddress));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Site_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Site_.state));
            }
            if (criteria.getZip() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZip(), Site_.zip));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Site_.country));
            }
            if (criteria.getInvestigatorsId() != null) {
                specification = specification.and(buildSpecification(criteria.getInvestigatorsId(),
                    root -> root.join(Site_.investigators, JoinType.LEFT).get(PrincipalInvestigator_.id)));
            }
        }
        return specification;
    }
}

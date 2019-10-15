/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.PrincipalInvestigator;
import com.kaleido.klinops.repository.PrincipalInvestigatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PrincipalInvestigator}.
 */
@Service
@Transactional
public class PrincipalInvestigatorService {

    private final Logger log = LoggerFactory.getLogger(PrincipalInvestigatorService.class);

    private final PrincipalInvestigatorRepository principalInvestigatorRepository;

    public PrincipalInvestigatorService(PrincipalInvestigatorRepository principalInvestigatorRepository) {
        this.principalInvestigatorRepository = principalInvestigatorRepository;
    }

    /**
     * Save a principalInvestigator.
     *
     * @param principalInvestigator the entity to save.
     * @return the persisted entity.
     */
    public PrincipalInvestigator save(PrincipalInvestigator principalInvestigator) {
        log.debug("Request to save PrincipalInvestigator : {}", principalInvestigator);
        return principalInvestigatorRepository.save(principalInvestigator);
    }

    /**
     * Get all the principalInvestigators.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrincipalInvestigator> findAll(Pageable pageable) {
        log.debug("Request to get all PrincipalInvestigators");
        return principalInvestigatorRepository.findAll(pageable);
    }


    /**
     * Get one principalInvestigator by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrincipalInvestigator> findOne(Long id) {
        log.debug("Request to get PrincipalInvestigator : {}", id);
        return principalInvestigatorRepository.findById(id);
    }

    /**
     * Delete the principalInvestigator by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PrincipalInvestigator : {}", id);
        principalInvestigatorRepository.deleteById(id);
    }
}

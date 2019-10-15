/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.ClinicalStudyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ClinicalStudy}.
 */
@Service
@Transactional
public class ClinicalStudyService {

    private final Logger log = LoggerFactory.getLogger(ClinicalStudyService.class);

    private final ClinicalStudyRepository clinicalStudyRepository;

    public ClinicalStudyService(ClinicalStudyRepository clinicalStudyRepository) {
        this.clinicalStudyRepository = clinicalStudyRepository;
    }

    /**
     * Save a clinicalStudy.
     *
     * @param clinicalStudy the entity to save.
     * @return the persisted entity.
     */
    public ClinicalStudy save(ClinicalStudy clinicalStudy) {
        log.debug("Request to save ClinicalStudy : {}", clinicalStudy);
        return clinicalStudyRepository.save(clinicalStudy);
    }

    /**
     * Get all the clinicalStudies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClinicalStudy> findAll(Pageable pageable) {
        log.debug("Request to get all ClinicalStudies");
        return clinicalStudyRepository.findAll(pageable);
    }

    /**
     * Get all the clinicalStudies with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ClinicalStudy> findAllWithEagerRelationships(Pageable pageable) {
        return clinicalStudyRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one clinicalStudy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClinicalStudy> findOne(Long id) {
        log.debug("Request to get ClinicalStudy : {}", id);
        return clinicalStudyRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the clinicalStudy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ClinicalStudy : {}", id);
        clinicalStudyRepository.deleteById(id);
    }
}

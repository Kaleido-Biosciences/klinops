/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.BioAnalysis;
import com.kaleido.klinops.repository.BioAnalysisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BioAnalysis}.
 */
@Service
@Transactional
public class BioAnalysisService {

    private final Logger log = LoggerFactory.getLogger(BioAnalysisService.class);

    private final BioAnalysisRepository bioAnalysisRepository;

    public BioAnalysisService(BioAnalysisRepository bioAnalysisRepository) {
        this.bioAnalysisRepository = bioAnalysisRepository;
    }

    /**
     * Save a bioAnalysis.
     *
     * @param bioAnalysis the entity to save.
     * @return the persisted entity.
     */
    public BioAnalysis save(BioAnalysis bioAnalysis) {
        log.debug("Request to save BioAnalysis : {}", bioAnalysis);
        return bioAnalysisRepository.save(bioAnalysis);
    }

    /**
     * Get all the bioAnalyses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BioAnalysis> findAll(Pageable pageable) {
        log.debug("Request to get all BioAnalyses");
        return bioAnalysisRepository.findAll(pageable);
    }


    /**
     * Get one bioAnalysis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BioAnalysis> findOne(Long id) {
        log.debug("Request to get BioAnalysis : {}", id);
        return bioAnalysisRepository.findById(id);
    }

    /**
     * Delete the bioAnalysis by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BioAnalysis : {}", id);
        bioAnalysisRepository.deleteById(id);
    }
}

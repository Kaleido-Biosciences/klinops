/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.DataAnalysis;
import com.kaleido.klinops.repository.DataAnalysisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DataAnalysis}.
 */
@Service
@Transactional
public class DataAnalysisService {

    private final Logger log = LoggerFactory.getLogger(DataAnalysisService.class);

    private final DataAnalysisRepository dataAnalysisRepository;

    public DataAnalysisService(DataAnalysisRepository dataAnalysisRepository) {
        this.dataAnalysisRepository = dataAnalysisRepository;
    }

    /**
     * Save a dataAnalysis.
     *
     * @param dataAnalysis the entity to save.
     * @return the persisted entity.
     */
    public DataAnalysis save(DataAnalysis dataAnalysis) {
        log.debug("Request to save DataAnalysis : {}", dataAnalysis);
        return dataAnalysisRepository.save(dataAnalysis);
    }

    /**
     * Get all the dataAnalyses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DataAnalysis> findAll(Pageable pageable) {
        log.debug("Request to get all DataAnalyses");
        return dataAnalysisRepository.findAll(pageable);
    }

    /**
     * Get all the dataAnalyses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DataAnalysis> findAllWithEagerRelationships(Pageable pageable) {
        return dataAnalysisRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one dataAnalysis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataAnalysis> findOne(Long id) {
        log.debug("Request to get DataAnalysis : {}", id);
        return dataAnalysisRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the dataAnalysis by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DataAnalysis : {}", id);
        dataAnalysisRepository.deleteById(id);
    }
}

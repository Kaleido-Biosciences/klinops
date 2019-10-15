/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.Laboratory;
import com.kaleido.klinops.repository.LaboratoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Laboratory}.
 */
@Service
@Transactional
public class LaboratoryService {

    private final Logger log = LoggerFactory.getLogger(LaboratoryService.class);

    private final LaboratoryRepository laboratoryRepository;

    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    /**
     * Save a laboratory.
     *
     * @param laboratory the entity to save.
     * @return the persisted entity.
     */
    public Laboratory save(Laboratory laboratory) {
        log.debug("Request to save Laboratory : {}", laboratory);
        return laboratoryRepository.save(laboratory);
    }

    /**
     * Get all the laboratories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Laboratory> findAll(Pageable pageable) {
        log.debug("Request to get all Laboratories");
        return laboratoryRepository.findAll(pageable);
    }


    /**
     * Get one laboratory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Laboratory> findOne(Long id) {
        log.debug("Request to get Laboratory : {}", id);
        return laboratoryRepository.findById(id);
    }

    /**
     * Delete the laboratory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Laboratory : {}", id);
        laboratoryRepository.deleteById(id);
    }
}

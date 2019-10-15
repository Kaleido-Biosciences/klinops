/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.StudyMilestone;
import com.kaleido.klinops.repository.StudyMilestoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link StudyMilestone}.
 */
@Service
@Transactional
public class StudyMilestoneService {

    private final Logger log = LoggerFactory.getLogger(StudyMilestoneService.class);

    private final StudyMilestoneRepository studyMilestoneRepository;

    public StudyMilestoneService(StudyMilestoneRepository studyMilestoneRepository) {
        this.studyMilestoneRepository = studyMilestoneRepository;
    }

    /**
     * Save a studyMilestone.
     *
     * @param studyMilestone the entity to save.
     * @return the persisted entity.
     */
    public StudyMilestone save(StudyMilestone studyMilestone) {
        log.debug("Request to save StudyMilestone : {}", studyMilestone);
        return studyMilestoneRepository.save(studyMilestone);
    }

    /**
     * Get all the studyMilestones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyMilestone> findAll(Pageable pageable) {
        log.debug("Request to get all StudyMilestones");
        return studyMilestoneRepository.findAll(pageable);
    }


    /**
     * Get one studyMilestone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudyMilestone> findOne(Long id) {
        log.debug("Request to get StudyMilestone : {}", id);
        return studyMilestoneRepository.findById(id);
    }

    /**
     * Delete the studyMilestone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudyMilestone : {}", id);
        studyMilestoneRepository.deleteById(id);
    }
}

package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.TrialMasterFile;
import com.kaleido.klinops.repository.TrialMasterFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link TrialMasterFile}.
 */
@Service
@Transactional
public class TrialMasterFileService {

    private final Logger log = LoggerFactory.getLogger(TrialMasterFileService.class);

    private final TrialMasterFileRepository trialMasterFileRepository;

    public TrialMasterFileService(TrialMasterFileRepository trialMasterFileRepository) {
        this.trialMasterFileRepository = trialMasterFileRepository;
    }

    /**
     * Save a trialMasterFile.
     *
     * @param trialMasterFile the entity to save.
     * @return the persisted entity.
     */
    public TrialMasterFile save(TrialMasterFile trialMasterFile) {
        log.debug("Request to save TrialMasterFile : {}", trialMasterFile);
        return trialMasterFileRepository.save(trialMasterFile);
    }

    /**
     * Get all the trialMasterFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TrialMasterFile> findAll(Pageable pageable) {
        log.debug("Request to get all TrialMasterFiles");
        return trialMasterFileRepository.findAll(pageable);
    }



    /**
    *  Get all the trialMasterFiles where ClinicalStudy is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<TrialMasterFile> findAllWhereClinicalStudyIsNull() {
        log.debug("Request to get all trialMasterFiles where ClinicalStudy is null");
        return StreamSupport
            .stream(trialMasterFileRepository.findAll().spliterator(), false)
            .filter(trialMasterFile -> trialMasterFile.getClinicalStudy() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one trialMasterFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrialMasterFile> findOne(Long id) {
        log.debug("Request to get TrialMasterFile : {}", id);
        return trialMasterFileRepository.findById(id);
    }

    /**
     * Delete the trialMasterFile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TrialMasterFile : {}", id);
        trialMasterFileRepository.deleteById(id);
    }
}

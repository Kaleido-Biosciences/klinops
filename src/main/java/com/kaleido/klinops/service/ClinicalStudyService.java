package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.ClinicalStudyRepository;
import com.kaleido.klinops.repository.search.ClinicalStudySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ClinicalStudy}.
 */
@Service
@Transactional
public class ClinicalStudyService {

    private final Logger log = LoggerFactory.getLogger(ClinicalStudyService.class);

    private final ClinicalStudyRepository clinicalStudyRepository;

    private final ClinicalStudySearchRepository clinicalStudySearchRepository;

    public ClinicalStudyService(ClinicalStudyRepository clinicalStudyRepository, ClinicalStudySearchRepository clinicalStudySearchRepository) {
        this.clinicalStudyRepository = clinicalStudyRepository;
        this.clinicalStudySearchRepository = clinicalStudySearchRepository;
    }

    /**
     * Save a clinicalStudy.
     *
     * @param clinicalStudy the entity to save.
     * @return the persisted entity.
     */
    public ClinicalStudy save(ClinicalStudy clinicalStudy) {
        log.debug("Request to save ClinicalStudy : {}", clinicalStudy);
        ClinicalStudy result = clinicalStudyRepository.save(clinicalStudy);
        clinicalStudySearchRepository.save(result);
        return result;
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
        clinicalStudySearchRepository.deleteById(id);
    }

    /**
     * Search for the clinicalStudy corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClinicalStudy> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ClinicalStudies for query {}", query);
        return clinicalStudySearchRepository.search(queryStringQuery(query), pageable);    }
}

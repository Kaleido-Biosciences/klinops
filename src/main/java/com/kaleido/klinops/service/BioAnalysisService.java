package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.BioAnalysis;
import com.kaleido.klinops.repository.BioAnalysisRepository;
import com.kaleido.klinops.repository.search.BioAnalysisSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link BioAnalysis}.
 */
@Service
@Transactional
public class BioAnalysisService {

    private final Logger log = LoggerFactory.getLogger(BioAnalysisService.class);

    private final BioAnalysisRepository bioAnalysisRepository;

    private final BioAnalysisSearchRepository bioAnalysisSearchRepository;

    public BioAnalysisService(BioAnalysisRepository bioAnalysisRepository, BioAnalysisSearchRepository bioAnalysisSearchRepository) {
        this.bioAnalysisRepository = bioAnalysisRepository;
        this.bioAnalysisSearchRepository = bioAnalysisSearchRepository;
    }

    /**
     * Save a bioAnalysis.
     *
     * @param bioAnalysis the entity to save.
     * @return the persisted entity.
     */
    public BioAnalysis save(BioAnalysis bioAnalysis) {
        log.debug("Request to save BioAnalysis : {}", bioAnalysis);
        BioAnalysis result = bioAnalysisRepository.save(bioAnalysis);
        bioAnalysisSearchRepository.save(result);
        return result;
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
        bioAnalysisSearchRepository.deleteById(id);
    }

    /**
     * Search for the bioAnalysis corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BioAnalysis> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BioAnalyses for query {}", query);
        return bioAnalysisSearchRepository.search(queryStringQuery(query), pageable);    }
}

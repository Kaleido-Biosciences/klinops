package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.StudySample;
import com.kaleido.klinops.repository.StudySampleRepository;
import com.kaleido.klinops.repository.search.StudySampleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link StudySample}.
 */
@Service
@Transactional
public class StudySampleService {

    private final Logger log = LoggerFactory.getLogger(StudySampleService.class);

    private final StudySampleRepository studySampleRepository;

    private final StudySampleSearchRepository studySampleSearchRepository;

    public StudySampleService(StudySampleRepository studySampleRepository, StudySampleSearchRepository studySampleSearchRepository) {
        this.studySampleRepository = studySampleRepository;
        this.studySampleSearchRepository = studySampleSearchRepository;
    }

    /**
     * Save a studySample.
     *
     * @param studySample the entity to save.
     * @return the persisted entity.
     */
    public StudySample save(StudySample studySample) {
        log.debug("Request to save StudySample : {}", studySample);
        StudySample result = studySampleRepository.save(studySample);
        studySampleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the studySamples.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudySample> findAll(Pageable pageable) {
        log.debug("Request to get all StudySamples");
        return studySampleRepository.findAll(pageable);
    }


    /**
     * Get one studySample by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudySample> findOne(Long id) {
        log.debug("Request to get StudySample : {}", id);
        return studySampleRepository.findById(id);
    }

    /**
     * Delete the studySample by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudySample : {}", id);
        studySampleRepository.deleteById(id);
        studySampleSearchRepository.deleteById(id);
    }

    /**
     * Search for the studySample corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudySample> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudySamples for query {}", query);
        return studySampleSearchRepository.search(queryStringQuery(query), pageable);    }
}

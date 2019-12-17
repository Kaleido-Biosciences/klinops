package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.DataAnalysis;
import com.kaleido.klinops.repository.DataAnalysisRepository;
import com.kaleido.klinops.repository.search.DataAnalysisSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link DataAnalysis}.
 */
@Service
@Transactional
public class DataAnalysisService {

    private final Logger log = LoggerFactory.getLogger(DataAnalysisService.class);

    private final DataAnalysisRepository dataAnalysisRepository;

    private final DataAnalysisSearchRepository dataAnalysisSearchRepository;

    public DataAnalysisService(DataAnalysisRepository dataAnalysisRepository, DataAnalysisSearchRepository dataAnalysisSearchRepository) {
        this.dataAnalysisRepository = dataAnalysisRepository;
        this.dataAnalysisSearchRepository = dataAnalysisSearchRepository;
    }

    /**
     * Save a dataAnalysis.
     *
     * @param dataAnalysis the entity to save.
     * @return the persisted entity.
     */
    public DataAnalysis save(DataAnalysis dataAnalysis) {
        log.debug("Request to save DataAnalysis : {}", dataAnalysis);
        DataAnalysis result = dataAnalysisRepository.save(dataAnalysis);
        dataAnalysisSearchRepository.save(result);
        return result;
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
        dataAnalysisSearchRepository.deleteById(id);
    }

    /**
     * Search for the dataAnalysis corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DataAnalysis> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DataAnalyses for query {}", query);
        return dataAnalysisSearchRepository.search(queryStringQuery(query), pageable);    }
}

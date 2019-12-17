package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.StudyProduct;
import com.kaleido.klinops.repository.StudyProductRepository;
import com.kaleido.klinops.repository.search.StudyProductSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link StudyProduct}.
 */
@Service
@Transactional
public class StudyProductService {

    private final Logger log = LoggerFactory.getLogger(StudyProductService.class);

    private final StudyProductRepository studyProductRepository;

    private final StudyProductSearchRepository studyProductSearchRepository;

    public StudyProductService(StudyProductRepository studyProductRepository, StudyProductSearchRepository studyProductSearchRepository) {
        this.studyProductRepository = studyProductRepository;
        this.studyProductSearchRepository = studyProductSearchRepository;
    }

    /**
     * Save a studyProduct.
     *
     * @param studyProduct the entity to save.
     * @return the persisted entity.
     */
    public StudyProduct save(StudyProduct studyProduct) {
        log.debug("Request to save StudyProduct : {}", studyProduct);
        StudyProduct result = studyProductRepository.save(studyProduct);
        studyProductSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the studyProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyProduct> findAll(Pageable pageable) {
        log.debug("Request to get all StudyProducts");
        return studyProductRepository.findAll(pageable);
    }


    /**
     * Get one studyProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudyProduct> findOne(Long id) {
        log.debug("Request to get StudyProduct : {}", id);
        return studyProductRepository.findById(id);
    }

    /**
     * Delete the studyProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudyProduct : {}", id);
        studyProductRepository.deleteById(id);
        studyProductSearchRepository.deleteById(id);
    }

    /**
     * Search for the studyProduct corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyProduct> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudyProducts for query {}", query);
        return studyProductSearchRepository.search(queryStringQuery(query), pageable);    }
}

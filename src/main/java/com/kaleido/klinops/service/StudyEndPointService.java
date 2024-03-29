package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.StudyEndPoint;
import com.kaleido.klinops.repository.StudyEndPointRepository;
import com.kaleido.klinops.repository.search.StudyEndPointSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link StudyEndPoint}.
 */
@Service
@Transactional
public class StudyEndPointService {

    private final Logger log = LoggerFactory.getLogger(StudyEndPointService.class);

    private final StudyEndPointRepository studyEndPointRepository;

    private final StudyEndPointSearchRepository studyEndPointSearchRepository;

    public StudyEndPointService(StudyEndPointRepository studyEndPointRepository, StudyEndPointSearchRepository studyEndPointSearchRepository) {
        this.studyEndPointRepository = studyEndPointRepository;
        this.studyEndPointSearchRepository = studyEndPointSearchRepository;
    }

    /**
     * Save a studyEndPoint.
     *
     * @param studyEndPoint the entity to save.
     * @return the persisted entity.
     */
    public StudyEndPoint save(StudyEndPoint studyEndPoint) {
        log.debug("Request to save StudyEndPoint : {}", studyEndPoint);
        StudyEndPoint result = studyEndPointRepository.save(studyEndPoint);
        studyEndPointSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the studyEndPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyEndPoint> findAll(Pageable pageable) {
        log.debug("Request to get all StudyEndPoints");
        return studyEndPointRepository.findAll(pageable);
    }


    /**
     * Get one studyEndPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudyEndPoint> findOne(Long id) {
        log.debug("Request to get StudyEndPoint : {}", id);
        return studyEndPointRepository.findById(id);
    }

    /**
     * Delete the studyEndPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudyEndPoint : {}", id);
        studyEndPointRepository.deleteById(id);
        studyEndPointSearchRepository.deleteById(id);
    }

    /**
     * Search for the studyEndPoint corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyEndPoint> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudyEndPoints for query {}", query);
        return studyEndPointSearchRepository.search(queryStringQuery(query), pageable);    }
}

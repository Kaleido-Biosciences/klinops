package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.StudyMilestone;
import com.kaleido.klinops.repository.StudyMilestoneRepository;
import com.kaleido.klinops.repository.search.StudyMilestoneSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link StudyMilestone}.
 */
@Service
@Transactional
public class StudyMilestoneService {

    private final Logger log = LoggerFactory.getLogger(StudyMilestoneService.class);

    private final StudyMilestoneRepository studyMilestoneRepository;

    private final StudyMilestoneSearchRepository studyMilestoneSearchRepository;

    public StudyMilestoneService(StudyMilestoneRepository studyMilestoneRepository, StudyMilestoneSearchRepository studyMilestoneSearchRepository) {
        this.studyMilestoneRepository = studyMilestoneRepository;
        this.studyMilestoneSearchRepository = studyMilestoneSearchRepository;
    }

    /**
     * Save a studyMilestone.
     *
     * @param studyMilestone the entity to save.
     * @return the persisted entity.
     */
    public StudyMilestone save(StudyMilestone studyMilestone) {
        log.debug("Request to save StudyMilestone : {}", studyMilestone);
        StudyMilestone result = studyMilestoneRepository.save(studyMilestone);
        studyMilestoneSearchRepository.save(result);
        return result;
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
        studyMilestoneSearchRepository.deleteById(id);
    }

    /**
     * Search for the studyMilestone corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyMilestone> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudyMilestones for query {}", query);
        return studyMilestoneSearchRepository.search(queryStringQuery(query), pageable);    }
}

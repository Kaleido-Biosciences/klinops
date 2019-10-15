/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.StudyMilestone;
import com.kaleido.klinops.service.StudyMilestoneService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.StudyMilestoneCriteria;
import com.kaleido.klinops.service.StudyMilestoneQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.kaleido.klinops.domain.StudyMilestone}.
 */
@RestController
@RequestMapping("/api")
public class StudyMilestoneResource {

    private final Logger log = LoggerFactory.getLogger(StudyMilestoneResource.class);

    private static final String ENTITY_NAME = "studyMilestone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyMilestoneService studyMilestoneService;

    private final StudyMilestoneQueryService studyMilestoneQueryService;

    public StudyMilestoneResource(StudyMilestoneService studyMilestoneService, StudyMilestoneQueryService studyMilestoneQueryService) {
        this.studyMilestoneService = studyMilestoneService;
        this.studyMilestoneQueryService = studyMilestoneQueryService;
    }

    /**
     * {@code POST  /study-milestones} : Create a new studyMilestone.
     *
     * @param studyMilestone the studyMilestone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studyMilestone, or with status {@code 400 (Bad Request)} if the studyMilestone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/study-milestones")
    public ResponseEntity<StudyMilestone> createStudyMilestone(@Valid @RequestBody StudyMilestone studyMilestone) throws URISyntaxException {
        log.debug("REST request to save StudyMilestone : {}", studyMilestone);
        if (studyMilestone.getId() != null) {
            throw new BadRequestAlertException("A new studyMilestone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudyMilestone result = studyMilestoneService.save(studyMilestone);
        return ResponseEntity.created(new URI("/api/study-milestones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /study-milestones} : Updates an existing studyMilestone.
     *
     * @param studyMilestone the studyMilestone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyMilestone,
     * or with status {@code 400 (Bad Request)} if the studyMilestone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studyMilestone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/study-milestones")
    public ResponseEntity<StudyMilestone> updateStudyMilestone(@Valid @RequestBody StudyMilestone studyMilestone) throws URISyntaxException {
        log.debug("REST request to update StudyMilestone : {}", studyMilestone);
        if (studyMilestone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudyMilestone result = studyMilestoneService.save(studyMilestone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyMilestone.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /study-milestones} : get all the studyMilestones.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studyMilestones in body.
     */
    @GetMapping("/study-milestones")
    public ResponseEntity<List<StudyMilestone>> getAllStudyMilestones(StudyMilestoneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StudyMilestones by criteria: {}", criteria);
        Page<StudyMilestone> page = studyMilestoneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /study-milestones/count} : count all the studyMilestones.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/study-milestones/count")
    public ResponseEntity<Long> countStudyMilestones(StudyMilestoneCriteria criteria) {
        log.debug("REST request to count StudyMilestones by criteria: {}", criteria);
        return ResponseEntity.ok().body(studyMilestoneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /study-milestones/:id} : get the "id" studyMilestone.
     *
     * @param id the id of the studyMilestone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studyMilestone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/study-milestones/{id}")
    public ResponseEntity<StudyMilestone> getStudyMilestone(@PathVariable Long id) {
        log.debug("REST request to get StudyMilestone : {}", id);
        Optional<StudyMilestone> studyMilestone = studyMilestoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studyMilestone);
    }

    /**
     * {@code DELETE  /study-milestones/:id} : delete the "id" studyMilestone.
     *
     * @param id the id of the studyMilestone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/study-milestones/{id}")
    public ResponseEntity<Void> deleteStudyMilestone(@PathVariable Long id) {
        log.debug("REST request to delete StudyMilestone : {}", id);
        studyMilestoneService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

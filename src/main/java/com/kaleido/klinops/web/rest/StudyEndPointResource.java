/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.StudyEndPoint;
import com.kaleido.klinops.service.StudyEndPointService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.StudyEndPointCriteria;
import com.kaleido.klinops.service.StudyEndPointQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.StudyEndPoint}.
 */
@RestController
@RequestMapping("/api")
public class StudyEndPointResource {

    private final Logger log = LoggerFactory.getLogger(StudyEndPointResource.class);

    private static final String ENTITY_NAME = "studyEndPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyEndPointService studyEndPointService;

    private final StudyEndPointQueryService studyEndPointQueryService;

    public StudyEndPointResource(StudyEndPointService studyEndPointService, StudyEndPointQueryService studyEndPointQueryService) {
        this.studyEndPointService = studyEndPointService;
        this.studyEndPointQueryService = studyEndPointQueryService;
    }

    /**
     * {@code POST  /study-end-points} : Create a new studyEndPoint.
     *
     * @param studyEndPoint the studyEndPoint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studyEndPoint, or with status {@code 400 (Bad Request)} if the studyEndPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/study-end-points")
    public ResponseEntity<StudyEndPoint> createStudyEndPoint(@Valid @RequestBody StudyEndPoint studyEndPoint) throws URISyntaxException {
        log.debug("REST request to save StudyEndPoint : {}", studyEndPoint);
        if (studyEndPoint.getId() != null) {
            throw new BadRequestAlertException("A new studyEndPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudyEndPoint result = studyEndPointService.save(studyEndPoint);
        return ResponseEntity.created(new URI("/api/study-end-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /study-end-points} : Updates an existing studyEndPoint.
     *
     * @param studyEndPoint the studyEndPoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyEndPoint,
     * or with status {@code 400 (Bad Request)} if the studyEndPoint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studyEndPoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/study-end-points")
    public ResponseEntity<StudyEndPoint> updateStudyEndPoint(@Valid @RequestBody StudyEndPoint studyEndPoint) throws URISyntaxException {
        log.debug("REST request to update StudyEndPoint : {}", studyEndPoint);
        if (studyEndPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudyEndPoint result = studyEndPointService.save(studyEndPoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyEndPoint.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /study-end-points} : get all the studyEndPoints.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studyEndPoints in body.
     */
    @GetMapping("/study-end-points")
    public ResponseEntity<List<StudyEndPoint>> getAllStudyEndPoints(StudyEndPointCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StudyEndPoints by criteria: {}", criteria);
        Page<StudyEndPoint> page = studyEndPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /study-end-points/count} : count all the studyEndPoints.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/study-end-points/count")
    public ResponseEntity<Long> countStudyEndPoints(StudyEndPointCriteria criteria) {
        log.debug("REST request to count StudyEndPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(studyEndPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /study-end-points/:id} : get the "id" studyEndPoint.
     *
     * @param id the id of the studyEndPoint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studyEndPoint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/study-end-points/{id}")
    public ResponseEntity<StudyEndPoint> getStudyEndPoint(@PathVariable Long id) {
        log.debug("REST request to get StudyEndPoint : {}", id);
        Optional<StudyEndPoint> studyEndPoint = studyEndPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studyEndPoint);
    }

    /**
     * {@code DELETE  /study-end-points/:id} : delete the "id" studyEndPoint.
     *
     * @param id the id of the studyEndPoint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/study-end-points/{id}")
    public ResponseEntity<Void> deleteStudyEndPoint(@PathVariable Long id) {
        log.debug("REST request to delete StudyEndPoint : {}", id);
        studyEndPointService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

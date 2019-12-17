package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.StudySample;
import com.kaleido.klinops.service.StudySampleService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.StudySampleCriteria;
import com.kaleido.klinops.service.StudySampleQueryService;

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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.kaleido.klinops.domain.StudySample}.
 */
@RestController
@RequestMapping("/api")
public class StudySampleResource {

    private final Logger log = LoggerFactory.getLogger(StudySampleResource.class);

    private static final String ENTITY_NAME = "studySample";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudySampleService studySampleService;

    private final StudySampleQueryService studySampleQueryService;

    public StudySampleResource(StudySampleService studySampleService, StudySampleQueryService studySampleQueryService) {
        this.studySampleService = studySampleService;
        this.studySampleQueryService = studySampleQueryService;
    }

    /**
     * {@code POST  /study-samples} : Create a new studySample.
     *
     * @param studySample the studySample to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studySample, or with status {@code 400 (Bad Request)} if the studySample has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/study-samples")
    public ResponseEntity<StudySample> createStudySample(@Valid @RequestBody StudySample studySample) throws URISyntaxException {
        log.debug("REST request to save StudySample : {}", studySample);
        if (studySample.getId() != null) {
            throw new BadRequestAlertException("A new studySample cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudySample result = studySampleService.save(studySample);
        return ResponseEntity.created(new URI("/api/study-samples/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /study-samples} : Updates an existing studySample.
     *
     * @param studySample the studySample to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studySample,
     * or with status {@code 400 (Bad Request)} if the studySample is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studySample couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/study-samples")
    public ResponseEntity<StudySample> updateStudySample(@Valid @RequestBody StudySample studySample) throws URISyntaxException {
        log.debug("REST request to update StudySample : {}", studySample);
        if (studySample.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudySample result = studySampleService.save(studySample);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studySample.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /study-samples} : get all the studySamples.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studySamples in body.
     */
    @GetMapping("/study-samples")
    public ResponseEntity<List<StudySample>> getAllStudySamples(StudySampleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StudySamples by criteria: {}", criteria);
        Page<StudySample> page = studySampleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /study-samples/count} : count all the studySamples.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/study-samples/count")
    public ResponseEntity<Long> countStudySamples(StudySampleCriteria criteria) {
        log.debug("REST request to count StudySamples by criteria: {}", criteria);
        return ResponseEntity.ok().body(studySampleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /study-samples/:id} : get the "id" studySample.
     *
     * @param id the id of the studySample to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studySample, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/study-samples/{id}")
    public ResponseEntity<StudySample> getStudySample(@PathVariable Long id) {
        log.debug("REST request to get StudySample : {}", id);
        Optional<StudySample> studySample = studySampleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studySample);
    }

    /**
     * {@code DELETE  /study-samples/:id} : delete the "id" studySample.
     *
     * @param id the id of the studySample to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/study-samples/{id}")
    public ResponseEntity<Void> deleteStudySample(@PathVariable Long id) {
        log.debug("REST request to delete StudySample : {}", id);
        studySampleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/study-samples?query=:query} : search for the studySample corresponding
     * to the query.
     *
     * @param query the query of the studySample search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/study-samples")
    public ResponseEntity<List<StudySample>> searchStudySamples(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StudySamples for query {}", query);
        Page<StudySample> page = studySampleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}

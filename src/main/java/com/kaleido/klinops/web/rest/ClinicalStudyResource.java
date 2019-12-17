package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.service.ClinicalStudyService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.ClinicalStudyCriteria;
import com.kaleido.klinops.service.ClinicalStudyQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.ClinicalStudy}.
 */
@RestController
@RequestMapping("/api")
public class ClinicalStudyResource {

    private final Logger log = LoggerFactory.getLogger(ClinicalStudyResource.class);

    private static final String ENTITY_NAME = "clinicalStudy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClinicalStudyService clinicalStudyService;

    private final ClinicalStudyQueryService clinicalStudyQueryService;

    public ClinicalStudyResource(ClinicalStudyService clinicalStudyService, ClinicalStudyQueryService clinicalStudyQueryService) {
        this.clinicalStudyService = clinicalStudyService;
        this.clinicalStudyQueryService = clinicalStudyQueryService;
    }

    /**
     * {@code POST  /clinical-studies} : Create a new clinicalStudy.
     *
     * @param clinicalStudy the clinicalStudy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clinicalStudy, or with status {@code 400 (Bad Request)} if the clinicalStudy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clinical-studies")
    public ResponseEntity<ClinicalStudy> createClinicalStudy(@Valid @RequestBody ClinicalStudy clinicalStudy) throws URISyntaxException {
        log.debug("REST request to save ClinicalStudy : {}", clinicalStudy);
        if (clinicalStudy.getId() != null) {
            throw new BadRequestAlertException("A new clinicalStudy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClinicalStudy result = clinicalStudyService.save(clinicalStudy);
        return ResponseEntity.created(new URI("/api/clinical-studies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clinical-studies} : Updates an existing clinicalStudy.
     *
     * @param clinicalStudy the clinicalStudy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clinicalStudy,
     * or with status {@code 400 (Bad Request)} if the clinicalStudy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clinicalStudy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clinical-studies")
    public ResponseEntity<ClinicalStudy> updateClinicalStudy(@Valid @RequestBody ClinicalStudy clinicalStudy) throws URISyntaxException {
        log.debug("REST request to update ClinicalStudy : {}", clinicalStudy);
        if (clinicalStudy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClinicalStudy result = clinicalStudyService.save(clinicalStudy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clinicalStudy.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /clinical-studies} : get all the clinicalStudies.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clinicalStudies in body.
     */
    @GetMapping("/clinical-studies")
    public ResponseEntity<List<ClinicalStudy>> getAllClinicalStudies(ClinicalStudyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClinicalStudies by criteria: {}", criteria);
        Page<ClinicalStudy> page = clinicalStudyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /clinical-studies/count} : count all the clinicalStudies.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/clinical-studies/count")
    public ResponseEntity<Long> countClinicalStudies(ClinicalStudyCriteria criteria) {
        log.debug("REST request to count ClinicalStudies by criteria: {}", criteria);
        return ResponseEntity.ok().body(clinicalStudyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /clinical-studies/:id} : get the "id" clinicalStudy.
     *
     * @param id the id of the clinicalStudy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clinicalStudy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clinical-studies/{id}")
    public ResponseEntity<ClinicalStudy> getClinicalStudy(@PathVariable Long id) {
        log.debug("REST request to get ClinicalStudy : {}", id);
        Optional<ClinicalStudy> clinicalStudy = clinicalStudyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clinicalStudy);
    }

    /**
     * {@code DELETE  /clinical-studies/:id} : delete the "id" clinicalStudy.
     *
     * @param id the id of the clinicalStudy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clinical-studies/{id}")
    public ResponseEntity<Void> deleteClinicalStudy(@PathVariable Long id) {
        log.debug("REST request to delete ClinicalStudy : {}", id);
        clinicalStudyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/clinical-studies?query=:query} : search for the clinicalStudy corresponding
     * to the query.
     *
     * @param query the query of the clinicalStudy search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/clinical-studies")
    public ResponseEntity<List<ClinicalStudy>> searchClinicalStudies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ClinicalStudies for query {}", query);
        Page<ClinicalStudy> page = clinicalStudyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}

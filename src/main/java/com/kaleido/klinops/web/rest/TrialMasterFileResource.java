package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.TrialMasterFile;
import com.kaleido.klinops.service.TrialMasterFileService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.TrialMasterFileCriteria;
import com.kaleido.klinops.service.TrialMasterFileQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.TrialMasterFile}.
 */
@RestController
@RequestMapping("/api")
public class TrialMasterFileResource {

    private final Logger log = LoggerFactory.getLogger(TrialMasterFileResource.class);

    private static final String ENTITY_NAME = "trialMasterFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrialMasterFileService trialMasterFileService;

    private final TrialMasterFileQueryService trialMasterFileQueryService;

    public TrialMasterFileResource(TrialMasterFileService trialMasterFileService, TrialMasterFileQueryService trialMasterFileQueryService) {
        this.trialMasterFileService = trialMasterFileService;
        this.trialMasterFileQueryService = trialMasterFileQueryService;
    }

    /**
     * {@code POST  /trial-master-files} : Create a new trialMasterFile.
     *
     * @param trialMasterFile the trialMasterFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trialMasterFile, or with status {@code 400 (Bad Request)} if the trialMasterFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trial-master-files")
    public ResponseEntity<TrialMasterFile> createTrialMasterFile(@Valid @RequestBody TrialMasterFile trialMasterFile) throws URISyntaxException {
        log.debug("REST request to save TrialMasterFile : {}", trialMasterFile);
        if (trialMasterFile.getId() != null) {
            throw new BadRequestAlertException("A new trialMasterFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrialMasterFile result = trialMasterFileService.save(trialMasterFile);
        return ResponseEntity.created(new URI("/api/trial-master-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trial-master-files} : Updates an existing trialMasterFile.
     *
     * @param trialMasterFile the trialMasterFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trialMasterFile,
     * or with status {@code 400 (Bad Request)} if the trialMasterFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trialMasterFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trial-master-files")
    public ResponseEntity<TrialMasterFile> updateTrialMasterFile(@Valid @RequestBody TrialMasterFile trialMasterFile) throws URISyntaxException {
        log.debug("REST request to update TrialMasterFile : {}", trialMasterFile);
        if (trialMasterFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TrialMasterFile result = trialMasterFileService.save(trialMasterFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trialMasterFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /trial-master-files} : get all the trialMasterFiles.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trialMasterFiles in body.
     */
    @GetMapping("/trial-master-files")
    public ResponseEntity<List<TrialMasterFile>> getAllTrialMasterFiles(TrialMasterFileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TrialMasterFiles by criteria: {}", criteria);
        Page<TrialMasterFile> page = trialMasterFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /trial-master-files/count} : count all the trialMasterFiles.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/trial-master-files/count")
    public ResponseEntity<Long> countTrialMasterFiles(TrialMasterFileCriteria criteria) {
        log.debug("REST request to count TrialMasterFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(trialMasterFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trial-master-files/:id} : get the "id" trialMasterFile.
     *
     * @param id the id of the trialMasterFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trialMasterFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trial-master-files/{id}")
    public ResponseEntity<TrialMasterFile> getTrialMasterFile(@PathVariable Long id) {
        log.debug("REST request to get TrialMasterFile : {}", id);
        Optional<TrialMasterFile> trialMasterFile = trialMasterFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trialMasterFile);
    }

    /**
     * {@code DELETE  /trial-master-files/:id} : delete the "id" trialMasterFile.
     *
     * @param id the id of the trialMasterFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trial-master-files/{id}")
    public ResponseEntity<Void> deleteTrialMasterFile(@PathVariable Long id) {
        log.debug("REST request to delete TrialMasterFile : {}", id);
        trialMasterFileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/trial-master-files?query=:query} : search for the trialMasterFile corresponding
     * to the query.
     *
     * @param query the query of the trialMasterFile search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/trial-master-files")
    public ResponseEntity<List<TrialMasterFile>> searchTrialMasterFiles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TrialMasterFiles for query {}", query);
        Page<TrialMasterFile> page = trialMasterFileService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}

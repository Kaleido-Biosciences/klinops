package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.BioAnalysis;
import com.kaleido.klinops.service.BioAnalysisService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.BioAnalysisCriteria;
import com.kaleido.klinops.service.BioAnalysisQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.BioAnalysis}.
 */
@RestController
@RequestMapping("/api")
public class BioAnalysisResource {

    private final Logger log = LoggerFactory.getLogger(BioAnalysisResource.class);

    private static final String ENTITY_NAME = "bioAnalysis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BioAnalysisService bioAnalysisService;

    private final BioAnalysisQueryService bioAnalysisQueryService;

    public BioAnalysisResource(BioAnalysisService bioAnalysisService, BioAnalysisQueryService bioAnalysisQueryService) {
        this.bioAnalysisService = bioAnalysisService;
        this.bioAnalysisQueryService = bioAnalysisQueryService;
    }

    /**
     * {@code POST  /bio-analyses} : Create a new bioAnalysis.
     *
     * @param bioAnalysis the bioAnalysis to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bioAnalysis, or with status {@code 400 (Bad Request)} if the bioAnalysis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bio-analyses")
    public ResponseEntity<BioAnalysis> createBioAnalysis(@Valid @RequestBody BioAnalysis bioAnalysis) throws URISyntaxException {
        log.debug("REST request to save BioAnalysis : {}", bioAnalysis);
        if (bioAnalysis.getId() != null) {
            throw new BadRequestAlertException("A new bioAnalysis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BioAnalysis result = bioAnalysisService.save(bioAnalysis);
        return ResponseEntity.created(new URI("/api/bio-analyses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bio-analyses} : Updates an existing bioAnalysis.
     *
     * @param bioAnalysis the bioAnalysis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bioAnalysis,
     * or with status {@code 400 (Bad Request)} if the bioAnalysis is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bioAnalysis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bio-analyses")
    public ResponseEntity<BioAnalysis> updateBioAnalysis(@Valid @RequestBody BioAnalysis bioAnalysis) throws URISyntaxException {
        log.debug("REST request to update BioAnalysis : {}", bioAnalysis);
        if (bioAnalysis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BioAnalysis result = bioAnalysisService.save(bioAnalysis);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bioAnalysis.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bio-analyses} : get all the bioAnalyses.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bioAnalyses in body.
     */
    @GetMapping("/bio-analyses")
    public ResponseEntity<List<BioAnalysis>> getAllBioAnalyses(BioAnalysisCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BioAnalyses by criteria: {}", criteria);
        Page<BioAnalysis> page = bioAnalysisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /bio-analyses/count} : count all the bioAnalyses.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/bio-analyses/count")
    public ResponseEntity<Long> countBioAnalyses(BioAnalysisCriteria criteria) {
        log.debug("REST request to count BioAnalyses by criteria: {}", criteria);
        return ResponseEntity.ok().body(bioAnalysisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bio-analyses/:id} : get the "id" bioAnalysis.
     *
     * @param id the id of the bioAnalysis to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bioAnalysis, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bio-analyses/{id}")
    public ResponseEntity<BioAnalysis> getBioAnalysis(@PathVariable Long id) {
        log.debug("REST request to get BioAnalysis : {}", id);
        Optional<BioAnalysis> bioAnalysis = bioAnalysisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bioAnalysis);
    }

    /**
     * {@code DELETE  /bio-analyses/:id} : delete the "id" bioAnalysis.
     *
     * @param id the id of the bioAnalysis to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bio-analyses/{id}")
    public ResponseEntity<Void> deleteBioAnalysis(@PathVariable Long id) {
        log.debug("REST request to delete BioAnalysis : {}", id);
        bioAnalysisService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/bio-analyses?query=:query} : search for the bioAnalysis corresponding
     * to the query.
     *
     * @param query the query of the bioAnalysis search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/bio-analyses")
    public ResponseEntity<List<BioAnalysis>> searchBioAnalyses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BioAnalyses for query {}", query);
        Page<BioAnalysis> page = bioAnalysisService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}

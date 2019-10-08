package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.DataAnalysis;
import com.kaleido.klinops.service.DataAnalysisService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.DataAnalysisCriteria;
import com.kaleido.klinops.service.DataAnalysisQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.DataAnalysis}.
 */
@RestController
@RequestMapping("/api")
public class DataAnalysisResource {

    private final Logger log = LoggerFactory.getLogger(DataAnalysisResource.class);

    private static final String ENTITY_NAME = "dataAnalysis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataAnalysisService dataAnalysisService;

    private final DataAnalysisQueryService dataAnalysisQueryService;

    public DataAnalysisResource(DataAnalysisService dataAnalysisService, DataAnalysisQueryService dataAnalysisQueryService) {
        this.dataAnalysisService = dataAnalysisService;
        this.dataAnalysisQueryService = dataAnalysisQueryService;
    }

    /**
     * {@code POST  /data-analyses} : Create a new dataAnalysis.
     *
     * @param dataAnalysis the dataAnalysis to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataAnalysis, or with status {@code 400 (Bad Request)} if the dataAnalysis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-analyses")
    public ResponseEntity<DataAnalysis> createDataAnalysis(@Valid @RequestBody DataAnalysis dataAnalysis) throws URISyntaxException {
        log.debug("REST request to save DataAnalysis : {}", dataAnalysis);
        if (dataAnalysis.getId() != null) {
            throw new BadRequestAlertException("A new dataAnalysis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataAnalysis result = dataAnalysisService.save(dataAnalysis);
        return ResponseEntity.created(new URI("/api/data-analyses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-analyses} : Updates an existing dataAnalysis.
     *
     * @param dataAnalysis the dataAnalysis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataAnalysis,
     * or with status {@code 400 (Bad Request)} if the dataAnalysis is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataAnalysis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-analyses")
    public ResponseEntity<DataAnalysis> updateDataAnalysis(@Valid @RequestBody DataAnalysis dataAnalysis) throws URISyntaxException {
        log.debug("REST request to update DataAnalysis : {}", dataAnalysis);
        if (dataAnalysis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataAnalysis result = dataAnalysisService.save(dataAnalysis);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataAnalysis.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /data-analyses} : get all the dataAnalyses.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataAnalyses in body.
     */
    @GetMapping("/data-analyses")
    public ResponseEntity<List<DataAnalysis>> getAllDataAnalyses(DataAnalysisCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DataAnalyses by criteria: {}", criteria);
        Page<DataAnalysis> page = dataAnalysisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /data-analyses/count} : count all the dataAnalyses.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/data-analyses/count")
    public ResponseEntity<Long> countDataAnalyses(DataAnalysisCriteria criteria) {
        log.debug("REST request to count DataAnalyses by criteria: {}", criteria);
        return ResponseEntity.ok().body(dataAnalysisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /data-analyses/:id} : get the "id" dataAnalysis.
     *
     * @param id the id of the dataAnalysis to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataAnalysis, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-analyses/{id}")
    public ResponseEntity<DataAnalysis> getDataAnalysis(@PathVariable Long id) {
        log.debug("REST request to get DataAnalysis : {}", id);
        Optional<DataAnalysis> dataAnalysis = dataAnalysisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataAnalysis);
    }

    /**
     * {@code DELETE  /data-analyses/:id} : delete the "id" dataAnalysis.
     *
     * @param id the id of the dataAnalysis to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-analyses/{id}")
    public ResponseEntity<Void> deleteDataAnalysis(@PathVariable Long id) {
        log.debug("REST request to delete DataAnalysis : {}", id);
        dataAnalysisService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

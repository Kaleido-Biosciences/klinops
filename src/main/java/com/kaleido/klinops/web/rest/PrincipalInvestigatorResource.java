package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.PrincipalInvestigator;
import com.kaleido.klinops.service.PrincipalInvestigatorService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.PrincipalInvestigatorCriteria;
import com.kaleido.klinops.service.PrincipalInvestigatorQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.PrincipalInvestigator}.
 */
@RestController
@RequestMapping("/api")
public class PrincipalInvestigatorResource {

    private final Logger log = LoggerFactory.getLogger(PrincipalInvestigatorResource.class);

    private static final String ENTITY_NAME = "principalInvestigator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrincipalInvestigatorService principalInvestigatorService;

    private final PrincipalInvestigatorQueryService principalInvestigatorQueryService;

    public PrincipalInvestigatorResource(PrincipalInvestigatorService principalInvestigatorService, PrincipalInvestigatorQueryService principalInvestigatorQueryService) {
        this.principalInvestigatorService = principalInvestigatorService;
        this.principalInvestigatorQueryService = principalInvestigatorQueryService;
    }

    /**
     * {@code POST  /principal-investigators} : Create a new principalInvestigator.
     *
     * @param principalInvestigator the principalInvestigator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new principalInvestigator, or with status {@code 400 (Bad Request)} if the principalInvestigator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/principal-investigators")
    public ResponseEntity<PrincipalInvestigator> createPrincipalInvestigator(@Valid @RequestBody PrincipalInvestigator principalInvestigator) throws URISyntaxException {
        log.debug("REST request to save PrincipalInvestigator : {}", principalInvestigator);
        if (principalInvestigator.getId() != null) {
            throw new BadRequestAlertException("A new principalInvestigator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrincipalInvestigator result = principalInvestigatorService.save(principalInvestigator);
        return ResponseEntity.created(new URI("/api/principal-investigators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /principal-investigators} : Updates an existing principalInvestigator.
     *
     * @param principalInvestigator the principalInvestigator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated principalInvestigator,
     * or with status {@code 400 (Bad Request)} if the principalInvestigator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the principalInvestigator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/principal-investigators")
    public ResponseEntity<PrincipalInvestigator> updatePrincipalInvestigator(@Valid @RequestBody PrincipalInvestigator principalInvestigator) throws URISyntaxException {
        log.debug("REST request to update PrincipalInvestigator : {}", principalInvestigator);
        if (principalInvestigator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrincipalInvestigator result = principalInvestigatorService.save(principalInvestigator);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, principalInvestigator.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /principal-investigators} : get all the principalInvestigators.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of principalInvestigators in body.
     */
    @GetMapping("/principal-investigators")
    public ResponseEntity<List<PrincipalInvestigator>> getAllPrincipalInvestigators(PrincipalInvestigatorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PrincipalInvestigators by criteria: {}", criteria);
        Page<PrincipalInvestigator> page = principalInvestigatorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /principal-investigators/count} : count all the principalInvestigators.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/principal-investigators/count")
    public ResponseEntity<Long> countPrincipalInvestigators(PrincipalInvestigatorCriteria criteria) {
        log.debug("REST request to count PrincipalInvestigators by criteria: {}", criteria);
        return ResponseEntity.ok().body(principalInvestigatorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /principal-investigators/:id} : get the "id" principalInvestigator.
     *
     * @param id the id of the principalInvestigator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the principalInvestigator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/principal-investigators/{id}")
    public ResponseEntity<PrincipalInvestigator> getPrincipalInvestigator(@PathVariable Long id) {
        log.debug("REST request to get PrincipalInvestigator : {}", id);
        Optional<PrincipalInvestigator> principalInvestigator = principalInvestigatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(principalInvestigator);
    }

    /**
     * {@code DELETE  /principal-investigators/:id} : delete the "id" principalInvestigator.
     *
     * @param id the id of the principalInvestigator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/principal-investigators/{id}")
    public ResponseEntity<Void> deletePrincipalInvestigator(@PathVariable Long id) {
        log.debug("REST request to delete PrincipalInvestigator : {}", id);
        principalInvestigatorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/principal-investigators?query=:query} : search for the principalInvestigator corresponding
     * to the query.
     *
     * @param query the query of the principalInvestigator search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/principal-investigators")
    public ResponseEntity<List<PrincipalInvestigator>> searchPrincipalInvestigators(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PrincipalInvestigators for query {}", query);
        Page<PrincipalInvestigator> page = principalInvestigatorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}

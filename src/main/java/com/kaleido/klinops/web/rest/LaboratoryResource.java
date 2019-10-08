package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.Laboratory;
import com.kaleido.klinops.service.LaboratoryService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.LaboratoryCriteria;
import com.kaleido.klinops.service.LaboratoryQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.Laboratory}.
 */
@RestController
@RequestMapping("/api")
public class LaboratoryResource {

    private final Logger log = LoggerFactory.getLogger(LaboratoryResource.class);

    private static final String ENTITY_NAME = "laboratory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LaboratoryService laboratoryService;

    private final LaboratoryQueryService laboratoryQueryService;

    public LaboratoryResource(LaboratoryService laboratoryService, LaboratoryQueryService laboratoryQueryService) {
        this.laboratoryService = laboratoryService;
        this.laboratoryQueryService = laboratoryQueryService;
    }

    /**
     * {@code POST  /laboratories} : Create a new laboratory.
     *
     * @param laboratory the laboratory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new laboratory, or with status {@code 400 (Bad Request)} if the laboratory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/laboratories")
    public ResponseEntity<Laboratory> createLaboratory(@Valid @RequestBody Laboratory laboratory) throws URISyntaxException {
        log.debug("REST request to save Laboratory : {}", laboratory);
        if (laboratory.getId() != null) {
            throw new BadRequestAlertException("A new laboratory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Laboratory result = laboratoryService.save(laboratory);
        return ResponseEntity.created(new URI("/api/laboratories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /laboratories} : Updates an existing laboratory.
     *
     * @param laboratory the laboratory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratory,
     * or with status {@code 400 (Bad Request)} if the laboratory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the laboratory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/laboratories")
    public ResponseEntity<Laboratory> updateLaboratory(@Valid @RequestBody Laboratory laboratory) throws URISyntaxException {
        log.debug("REST request to update Laboratory : {}", laboratory);
        if (laboratory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Laboratory result = laboratoryService.save(laboratory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, laboratory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /laboratories} : get all the laboratories.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of laboratories in body.
     */
    @GetMapping("/laboratories")
    public ResponseEntity<List<Laboratory>> getAllLaboratories(LaboratoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Laboratories by criteria: {}", criteria);
        Page<Laboratory> page = laboratoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /laboratories/count} : count all the laboratories.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/laboratories/count")
    public ResponseEntity<Long> countLaboratories(LaboratoryCriteria criteria) {
        log.debug("REST request to count Laboratories by criteria: {}", criteria);
        return ResponseEntity.ok().body(laboratoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /laboratories/:id} : get the "id" laboratory.
     *
     * @param id the id of the laboratory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the laboratory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/laboratories/{id}")
    public ResponseEntity<Laboratory> getLaboratory(@PathVariable Long id) {
        log.debug("REST request to get Laboratory : {}", id);
        Optional<Laboratory> laboratory = laboratoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(laboratory);
    }

    /**
     * {@code DELETE  /laboratories/:id} : delete the "id" laboratory.
     *
     * @param id the id of the laboratory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/laboratories/{id}")
    public ResponseEntity<Void> deleteLaboratory(@PathVariable Long id) {
        log.debug("REST request to delete Laboratory : {}", id);
        laboratoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

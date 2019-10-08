package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.StudyProduct;
import com.kaleido.klinops.service.StudyProductService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.StudyProductCriteria;
import com.kaleido.klinops.service.StudyProductQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.StudyProduct}.
 */
@RestController
@RequestMapping("/api")
public class StudyProductResource {

    private final Logger log = LoggerFactory.getLogger(StudyProductResource.class);

    private static final String ENTITY_NAME = "studyProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyProductService studyProductService;

    private final StudyProductQueryService studyProductQueryService;

    public StudyProductResource(StudyProductService studyProductService, StudyProductQueryService studyProductQueryService) {
        this.studyProductService = studyProductService;
        this.studyProductQueryService = studyProductQueryService;
    }

    /**
     * {@code POST  /study-products} : Create a new studyProduct.
     *
     * @param studyProduct the studyProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studyProduct, or with status {@code 400 (Bad Request)} if the studyProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/study-products")
    public ResponseEntity<StudyProduct> createStudyProduct(@Valid @RequestBody StudyProduct studyProduct) throws URISyntaxException {
        log.debug("REST request to save StudyProduct : {}", studyProduct);
        if (studyProduct.getId() != null) {
            throw new BadRequestAlertException("A new studyProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudyProduct result = studyProductService.save(studyProduct);
        return ResponseEntity.created(new URI("/api/study-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /study-products} : Updates an existing studyProduct.
     *
     * @param studyProduct the studyProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyProduct,
     * or with status {@code 400 (Bad Request)} if the studyProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studyProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/study-products")
    public ResponseEntity<StudyProduct> updateStudyProduct(@Valid @RequestBody StudyProduct studyProduct) throws URISyntaxException {
        log.debug("REST request to update StudyProduct : {}", studyProduct);
        if (studyProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudyProduct result = studyProductService.save(studyProduct);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /study-products} : get all the studyProducts.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studyProducts in body.
     */
    @GetMapping("/study-products")
    public ResponseEntity<List<StudyProduct>> getAllStudyProducts(StudyProductCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StudyProducts by criteria: {}", criteria);
        Page<StudyProduct> page = studyProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /study-products/count} : count all the studyProducts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/study-products/count")
    public ResponseEntity<Long> countStudyProducts(StudyProductCriteria criteria) {
        log.debug("REST request to count StudyProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(studyProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /study-products/:id} : get the "id" studyProduct.
     *
     * @param id the id of the studyProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studyProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/study-products/{id}")
    public ResponseEntity<StudyProduct> getStudyProduct(@PathVariable Long id) {
        log.debug("REST request to get StudyProduct : {}", id);
        Optional<StudyProduct> studyProduct = studyProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studyProduct);
    }

    /**
     * {@code DELETE  /study-products/:id} : delete the "id" studyProduct.
     *
     * @param id the id of the studyProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/study-products/{id}")
    public ResponseEntity<Void> deleteStudyProduct(@PathVariable Long id) {
        log.debug("REST request to delete StudyProduct : {}", id);
        studyProductService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

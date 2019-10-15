/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.domain.ShipmentComponent;
import com.kaleido.klinops.service.ShipmentComponentService;
import com.kaleido.klinops.web.rest.errors.BadRequestAlertException;
import com.kaleido.klinops.service.dto.ShipmentComponentCriteria;
import com.kaleido.klinops.service.ShipmentComponentQueryService;

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
 * REST controller for managing {@link com.kaleido.klinops.domain.ShipmentComponent}.
 */
@RestController
@RequestMapping("/api")
public class ShipmentComponentResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentComponentResource.class);

    private static final String ENTITY_NAME = "shipmentComponent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentComponentService shipmentComponentService;

    private final ShipmentComponentQueryService shipmentComponentQueryService;

    public ShipmentComponentResource(ShipmentComponentService shipmentComponentService, ShipmentComponentQueryService shipmentComponentQueryService) {
        this.shipmentComponentService = shipmentComponentService;
        this.shipmentComponentQueryService = shipmentComponentQueryService;
    }

    /**
     * {@code POST  /shipment-components} : Create a new shipmentComponent.
     *
     * @param shipmentComponent the shipmentComponent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentComponent, or with status {@code 400 (Bad Request)} if the shipmentComponent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipment-components")
    public ResponseEntity<ShipmentComponent> createShipmentComponent(@Valid @RequestBody ShipmentComponent shipmentComponent) throws URISyntaxException {
        log.debug("REST request to save ShipmentComponent : {}", shipmentComponent);
        if (shipmentComponent.getId() != null) {
            throw new BadRequestAlertException("A new shipmentComponent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShipmentComponent result = shipmentComponentService.save(shipmentComponent);
        return ResponseEntity.created(new URI("/api/shipment-components/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipment-components} : Updates an existing shipmentComponent.
     *
     * @param shipmentComponent the shipmentComponent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentComponent,
     * or with status {@code 400 (Bad Request)} if the shipmentComponent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentComponent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipment-components")
    public ResponseEntity<ShipmentComponent> updateShipmentComponent(@Valid @RequestBody ShipmentComponent shipmentComponent) throws URISyntaxException {
        log.debug("REST request to update ShipmentComponent : {}", shipmentComponent);
        if (shipmentComponent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShipmentComponent result = shipmentComponentService.save(shipmentComponent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentComponent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shipment-components} : get all the shipmentComponents.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentComponents in body.
     */
    @GetMapping("/shipment-components")
    public ResponseEntity<List<ShipmentComponent>> getAllShipmentComponents(ShipmentComponentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShipmentComponents by criteria: {}", criteria);
        Page<ShipmentComponent> page = shipmentComponentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /shipment-components/count} : count all the shipmentComponents.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/shipment-components/count")
    public ResponseEntity<Long> countShipmentComponents(ShipmentComponentCriteria criteria) {
        log.debug("REST request to count ShipmentComponents by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentComponentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-components/:id} : get the "id" shipmentComponent.
     *
     * @param id the id of the shipmentComponent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentComponent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipment-components/{id}")
    public ResponseEntity<ShipmentComponent> getShipmentComponent(@PathVariable Long id) {
        log.debug("REST request to get ShipmentComponent : {}", id);
        Optional<ShipmentComponent> shipmentComponent = shipmentComponentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentComponent);
    }

    /**
     * {@code DELETE  /shipment-components/:id} : delete the "id" shipmentComponent.
     *
     * @param id the id of the shipmentComponent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipment-components/{id}")
    public ResponseEntity<Void> deleteShipmentComponent(@PathVariable Long id) {
        log.debug("REST request to delete ShipmentComponent : {}", id);
        shipmentComponentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

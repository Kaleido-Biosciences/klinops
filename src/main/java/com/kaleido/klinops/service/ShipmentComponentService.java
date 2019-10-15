/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.service;

import com.kaleido.klinops.domain.ShipmentComponent;
import com.kaleido.klinops.repository.ShipmentComponentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ShipmentComponent}.
 */
@Service
@Transactional
public class ShipmentComponentService {

    private final Logger log = LoggerFactory.getLogger(ShipmentComponentService.class);

    private final ShipmentComponentRepository shipmentComponentRepository;

    public ShipmentComponentService(ShipmentComponentRepository shipmentComponentRepository) {
        this.shipmentComponentRepository = shipmentComponentRepository;
    }

    /**
     * Save a shipmentComponent.
     *
     * @param shipmentComponent the entity to save.
     * @return the persisted entity.
     */
    public ShipmentComponent save(ShipmentComponent shipmentComponent) {
        log.debug("Request to save ShipmentComponent : {}", shipmentComponent);
        return shipmentComponentRepository.save(shipmentComponent);
    }

    /**
     * Get all the shipmentComponents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentComponent> findAll(Pageable pageable) {
        log.debug("Request to get all ShipmentComponents");
        return shipmentComponentRepository.findAll(pageable);
    }


    /**
     * Get one shipmentComponent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentComponent> findOne(Long id) {
        log.debug("Request to get ShipmentComponent : {}", id);
        return shipmentComponentRepository.findById(id);
    }

    /**
     * Delete the shipmentComponent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShipmentComponent : {}", id);
        shipmentComponentRepository.deleteById(id);
    }
}

/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.ShipmentComponent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShipmentComponent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentComponentRepository extends JpaRepository<ShipmentComponent, Long>, JpaSpecificationExecutor<ShipmentComponent> {

}

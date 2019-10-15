/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.Laboratory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Laboratory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, Long>, JpaSpecificationExecutor<Laboratory> {

}

/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.ClinicalStudy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ClinicalStudy entity.
 */
@Repository
public interface ClinicalStudyRepository extends JpaRepository<ClinicalStudy, Long>, JpaSpecificationExecutor<ClinicalStudy> {

    @Query(value = "select distinct clinicalStudy from ClinicalStudy clinicalStudy left join fetch clinicalStudy.investigators",
        countQuery = "select count(distinct clinicalStudy) from ClinicalStudy clinicalStudy")
    Page<ClinicalStudy> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct clinicalStudy from ClinicalStudy clinicalStudy left join fetch clinicalStudy.investigators")
    List<ClinicalStudy> findAllWithEagerRelationships();

    @Query("select clinicalStudy from ClinicalStudy clinicalStudy left join fetch clinicalStudy.investigators where clinicalStudy.id =:id")
    Optional<ClinicalStudy> findOneWithEagerRelationships(@Param("id") Long id);

}

/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.PrincipalInvestigator;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PrincipalInvestigator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrincipalInvestigatorRepository extends JpaRepository<PrincipalInvestigator, Long>, JpaSpecificationExecutor<PrincipalInvestigator> {

}

/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.StudyEndPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StudyEndPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyEndPointRepository extends JpaRepository<StudyEndPoint, Long>, JpaSpecificationExecutor<StudyEndPoint> {

}

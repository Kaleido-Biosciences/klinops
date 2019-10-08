package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.DataAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the DataAnalysis entity.
 */
@Repository
public interface DataAnalysisRepository extends JpaRepository<DataAnalysis, Long>, JpaSpecificationExecutor<DataAnalysis> {

    @Query(value = "select distinct dataAnalysis from DataAnalysis dataAnalysis left join fetch dataAnalysis.bioAnalyses",
        countQuery = "select count(distinct dataAnalysis) from DataAnalysis dataAnalysis")
    Page<DataAnalysis> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct dataAnalysis from DataAnalysis dataAnalysis left join fetch dataAnalysis.bioAnalyses")
    List<DataAnalysis> findAllWithEagerRelationships();

    @Query("select dataAnalysis from DataAnalysis dataAnalysis left join fetch dataAnalysis.bioAnalyses where dataAnalysis.id =:id")
    Optional<DataAnalysis> findOneWithEagerRelationships(@Param("id") Long id);

}

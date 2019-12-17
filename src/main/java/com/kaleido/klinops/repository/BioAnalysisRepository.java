package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.BioAnalysis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BioAnalysis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BioAnalysisRepository extends JpaRepository<BioAnalysis, Long>, JpaSpecificationExecutor<BioAnalysis> {

}

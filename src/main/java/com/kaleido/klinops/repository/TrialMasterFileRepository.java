package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.TrialMasterFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TrialMasterFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrialMasterFileRepository extends JpaRepository<TrialMasterFile, Long>, JpaSpecificationExecutor<TrialMasterFile> {

}

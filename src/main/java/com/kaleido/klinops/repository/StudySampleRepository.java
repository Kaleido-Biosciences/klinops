package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.StudySample;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StudySample entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudySampleRepository extends JpaRepository<StudySample, Long>, JpaSpecificationExecutor<StudySample> {

}

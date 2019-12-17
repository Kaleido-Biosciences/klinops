package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.StudyProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StudyProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyProductRepository extends JpaRepository<StudyProduct, Long>, JpaSpecificationExecutor<StudyProduct> {

}

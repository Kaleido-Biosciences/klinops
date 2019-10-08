package com.kaleido.klinops.repository;
import com.kaleido.klinops.domain.StudyMilestone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StudyMilestone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyMilestoneRepository extends JpaRepository<StudyMilestone, Long>, JpaSpecificationExecutor<StudyMilestone> {

}

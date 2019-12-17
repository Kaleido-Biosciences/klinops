package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.StudyMilestone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link StudyMilestone} entity.
 */
public interface StudyMilestoneSearchRepository extends ElasticsearchRepository<StudyMilestone, Long> {
}

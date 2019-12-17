package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.StudyEndPoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link StudyEndPoint} entity.
 */
public interface StudyEndPointSearchRepository extends ElasticsearchRepository<StudyEndPoint, Long> {
}

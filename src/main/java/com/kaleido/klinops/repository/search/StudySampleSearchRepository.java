package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.StudySample;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link StudySample} entity.
 */
public interface StudySampleSearchRepository extends ElasticsearchRepository<StudySample, Long> {
}

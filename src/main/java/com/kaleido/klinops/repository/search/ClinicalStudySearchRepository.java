package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.ClinicalStudy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ClinicalStudy} entity.
 */
public interface ClinicalStudySearchRepository extends ElasticsearchRepository<ClinicalStudy, Long> {
}

package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.DataAnalysis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DataAnalysis} entity.
 */
public interface DataAnalysisSearchRepository extends ElasticsearchRepository<DataAnalysis, Long> {
}

package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.BioAnalysis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link BioAnalysis} entity.
 */
public interface BioAnalysisSearchRepository extends ElasticsearchRepository<BioAnalysis, Long> {
}

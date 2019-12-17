package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.TrialMasterFile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TrialMasterFile} entity.
 */
public interface TrialMasterFileSearchRepository extends ElasticsearchRepository<TrialMasterFile, Long> {
}

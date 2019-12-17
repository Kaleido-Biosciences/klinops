package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.Laboratory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Laboratory} entity.
 */
public interface LaboratorySearchRepository extends ElasticsearchRepository<Laboratory, Long> {
}

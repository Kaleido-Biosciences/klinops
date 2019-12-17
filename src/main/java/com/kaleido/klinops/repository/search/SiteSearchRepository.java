package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.Site;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Site} entity.
 */
public interface SiteSearchRepository extends ElasticsearchRepository<Site, Long> {
}

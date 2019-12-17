package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.PrincipalInvestigator;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PrincipalInvestigator} entity.
 */
public interface PrincipalInvestigatorSearchRepository extends ElasticsearchRepository<PrincipalInvestigator, Long> {
}

package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.StudyProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link StudyProduct} entity.
 */
public interface StudyProductSearchRepository extends ElasticsearchRepository<StudyProduct, Long> {
}

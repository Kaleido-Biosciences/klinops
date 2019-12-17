package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.ShipmentComponent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ShipmentComponent} entity.
 */
public interface ShipmentComponentSearchRepository extends ElasticsearchRepository<ShipmentComponent, Long> {
}

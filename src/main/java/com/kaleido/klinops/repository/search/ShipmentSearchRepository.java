package com.kaleido.klinops.repository.search;
import com.kaleido.klinops.domain.Shipment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Shipment} entity.
 */
public interface ShipmentSearchRepository extends ElasticsearchRepository<Shipment, Long> {
}

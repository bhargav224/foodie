package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.Measurement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Measurement entity.
 */
public interface MeasurementSearchRepository extends ElasticsearchRepository<Measurement, Long> {
}

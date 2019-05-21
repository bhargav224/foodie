package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.Cusine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cusine entity.
 */
public interface CusineSearchRepository extends ElasticsearchRepository<Cusine, Long> {
}

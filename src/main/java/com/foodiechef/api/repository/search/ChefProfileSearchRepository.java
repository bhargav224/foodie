package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.ChefProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ChefProfile entity.
 */
public interface ChefProfileSearchRepository extends ElasticsearchRepository<ChefProfile, Long> {
}

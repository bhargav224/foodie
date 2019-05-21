package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.RestaurantMenu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RestaurantMenu entity.
 */
public interface RestaurantMenuSearchRepository extends ElasticsearchRepository<RestaurantMenu, Long> {
}

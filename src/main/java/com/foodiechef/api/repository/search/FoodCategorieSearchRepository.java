package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.FoodCategorie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FoodCategorie entity.
 */
public interface FoodCategorieSearchRepository extends ElasticsearchRepository<FoodCategorie, Long> {
}

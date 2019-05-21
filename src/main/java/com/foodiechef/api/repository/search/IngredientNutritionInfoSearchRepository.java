package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.IngredientNutritionInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IngredientNutritionInfo entity.
 */
public interface IngredientNutritionInfoSearchRepository extends ElasticsearchRepository<IngredientNutritionInfo, Long> {
}

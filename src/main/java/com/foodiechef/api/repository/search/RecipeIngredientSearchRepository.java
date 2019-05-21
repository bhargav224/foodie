package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.RecipeIngredient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RecipeIngredient entity.
 */
public interface RecipeIngredientSearchRepository extends ElasticsearchRepository<RecipeIngredient, Long> {
}

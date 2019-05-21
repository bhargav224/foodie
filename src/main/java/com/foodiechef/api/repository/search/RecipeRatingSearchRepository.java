package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.RecipeRating;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RecipeRating entity.
 */
public interface RecipeRatingSearchRepository extends ElasticsearchRepository<RecipeRating, Long> {
}

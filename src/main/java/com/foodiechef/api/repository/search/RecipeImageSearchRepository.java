package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.RecipeImage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RecipeImage entity.
 */
public interface RecipeImageSearchRepository extends ElasticsearchRepository<RecipeImage, Long> {
}

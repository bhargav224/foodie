package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.RecipeStep;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RecipeStep entity.
 */
public interface RecipeStepSearchRepository extends ElasticsearchRepository<RecipeStep, Long> {
}

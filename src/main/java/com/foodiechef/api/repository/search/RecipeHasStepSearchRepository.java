package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.RecipeHasStep;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RecipeHasStep entity.
 */
public interface RecipeHasStepSearchRepository extends ElasticsearchRepository<RecipeHasStep, Long> {
}

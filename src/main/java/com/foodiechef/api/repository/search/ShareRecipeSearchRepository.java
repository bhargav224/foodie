package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.ShareRecipe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ShareRecipe entity.
 */
public interface ShareRecipeSearchRepository extends ElasticsearchRepository<ShareRecipe, Long> {
}

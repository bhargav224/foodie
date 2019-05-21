package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.MenuRecipe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MenuRecipe entity.
 */
public interface MenuRecipeSearchRepository extends ElasticsearchRepository<MenuRecipe, Long> {
}

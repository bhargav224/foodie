package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.RecipeComment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RecipeComment entity.
 */
public interface RecipeCommentSearchRepository extends ElasticsearchRepository<RecipeComment, Long> {
}

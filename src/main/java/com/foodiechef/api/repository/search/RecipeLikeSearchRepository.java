package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.RecipeLike;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RecipeLike entity.
 */
public interface RecipeLikeSearchRepository extends ElasticsearchRepository<RecipeLike, Long> {
}

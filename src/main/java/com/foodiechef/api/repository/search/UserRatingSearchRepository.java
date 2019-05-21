package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.UserRating;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserRating entity.
 */
public interface UserRatingSearchRepository extends ElasticsearchRepository<UserRating, Long> {
}

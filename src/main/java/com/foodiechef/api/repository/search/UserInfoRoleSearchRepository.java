package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.UserInfoRole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserInfoRole entity.
 */
public interface UserInfoRoleSearchRepository extends ElasticsearchRepository<UserInfoRole, Long> {
}

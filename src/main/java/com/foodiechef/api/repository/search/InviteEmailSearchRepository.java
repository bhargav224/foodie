package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.InviteEmail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InviteEmail entity.
 */
public interface InviteEmailSearchRepository extends ElasticsearchRepository<InviteEmail, Long> {
}

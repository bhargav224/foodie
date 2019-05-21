package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.InviteContact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InviteContact entity.
 */
public interface InviteContactSearchRepository extends ElasticsearchRepository<InviteContact, Long> {
}

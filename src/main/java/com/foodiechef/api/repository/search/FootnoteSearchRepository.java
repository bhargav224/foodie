package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.Footnote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Footnote entity.
 */
public interface FootnoteSearchRepository extends ElasticsearchRepository<Footnote, Long> {
}

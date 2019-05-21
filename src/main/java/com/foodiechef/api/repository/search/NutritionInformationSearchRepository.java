package com.foodiechef.api.repository.search;

import com.foodiechef.api.domain.NutritionInformation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the NutritionInformation entity.
 */
public interface NutritionInformationSearchRepository extends ElasticsearchRepository<NutritionInformation, Long> {
}

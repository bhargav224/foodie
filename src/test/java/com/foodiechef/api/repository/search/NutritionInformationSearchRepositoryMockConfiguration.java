package com.foodiechef.api.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of NutritionInformationSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class NutritionInformationSearchRepositoryMockConfiguration {

    @MockBean
    private NutritionInformationSearchRepository mockNutritionInformationSearchRepository;

}

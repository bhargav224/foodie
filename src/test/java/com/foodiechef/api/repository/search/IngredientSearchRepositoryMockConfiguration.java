package com.foodiechef.api.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of IngredientSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class IngredientSearchRepositoryMockConfiguration {

    @MockBean
    private IngredientSearchRepository mockIngredientSearchRepository;

}

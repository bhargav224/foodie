package com.foodiechef.api.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RecipeHasStepSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RecipeHasStepSearchRepositoryMockConfiguration {

    @MockBean
    private RecipeHasStepSearchRepository mockRecipeHasStepSearchRepository;

}

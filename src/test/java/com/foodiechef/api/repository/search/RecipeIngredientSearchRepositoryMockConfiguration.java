package com.foodiechef.api.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RecipeIngredientSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RecipeIngredientSearchRepositoryMockConfiguration {

    @MockBean
    private RecipeIngredientSearchRepository mockRecipeIngredientSearchRepository;

}

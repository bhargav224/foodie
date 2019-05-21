package com.foodiechef.api.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RecipeCommentSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RecipeCommentSearchRepositoryMockConfiguration {

    @MockBean
    private RecipeCommentSearchRepository mockRecipeCommentSearchRepository;

}

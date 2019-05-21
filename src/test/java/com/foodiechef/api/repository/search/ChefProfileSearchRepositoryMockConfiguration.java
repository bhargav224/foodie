package com.foodiechef.api.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ChefProfileSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ChefProfileSearchRepositoryMockConfiguration {

    @MockBean
    private ChefProfileSearchRepository mockChefProfileSearchRepository;

}

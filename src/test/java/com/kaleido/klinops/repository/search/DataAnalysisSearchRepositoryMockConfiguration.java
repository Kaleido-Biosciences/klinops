package com.kaleido.klinops.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DataAnalysisSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DataAnalysisSearchRepositoryMockConfiguration {

    @MockBean
    private DataAnalysisSearchRepository mockDataAnalysisSearchRepository;

}

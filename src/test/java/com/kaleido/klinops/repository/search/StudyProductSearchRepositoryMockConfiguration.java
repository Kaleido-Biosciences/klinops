package com.kaleido.klinops.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link StudyProductSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class StudyProductSearchRepositoryMockConfiguration {

    @MockBean
    private StudyProductSearchRepository mockStudyProductSearchRepository;

}

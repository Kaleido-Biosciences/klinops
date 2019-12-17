package com.kaleido.klinops.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link StudyMilestoneSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class StudyMilestoneSearchRepositoryMockConfiguration {

    @MockBean
    private StudyMilestoneSearchRepository mockStudyMilestoneSearchRepository;

}

package com.kaleido.klinops.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ClinicalStudySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ClinicalStudySearchRepositoryMockConfiguration {

    @MockBean
    private ClinicalStudySearchRepository mockClinicalStudySearchRepository;

}

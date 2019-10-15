/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.domain.TrialMasterFile;
import com.kaleido.klinops.domain.StudyEndPoint;
import com.kaleido.klinops.domain.StudyProduct;
import com.kaleido.klinops.domain.StudyMilestone;
import com.kaleido.klinops.domain.BioAnalysis;
import com.kaleido.klinops.domain.DataAnalysis;
import com.kaleido.klinops.domain.Shipment;
import com.kaleido.klinops.domain.StudySample;
import com.kaleido.klinops.domain.PrincipalInvestigator;
import com.kaleido.klinops.repository.ClinicalStudyRepository;
import com.kaleido.klinops.service.ClinicalStudyService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.ClinicalStudyCriteria;
import com.kaleido.klinops.service.ClinicalStudyQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.kaleido.klinops.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ClinicalStudyResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class ClinicalStudyResourceIT {

    private static final String DEFAULT_STUDY_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_STUDY_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_PHASE = "AAAAAAAAAA";
    private static final String UPDATED_PHASE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE = 1;
    private static final Integer UPDATED_SEQUENCE = 2;
    private static final Integer SMALLER_SEQUENCE = 1 - 1;

    private static final Integer DEFAULT_STUDY_YEAR = 1900;
    private static final Integer UPDATED_STUDY_YEAR = 1901;
    private static final Integer SMALLER_STUDY_YEAR = 1900 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGN = "AAAAAAAAAA";
    private static final String UPDATED_DESIGN = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_COHORTS = 0;
    private static final Integer UPDATED_NUMBER_OF_COHORTS = 1;
    private static final Integer SMALLER_NUMBER_OF_COHORTS = 0 - 1;

    private static final Integer DEFAULT_INTENDED_SUBJECTS_PER_COHORT = 0;
    private static final Integer UPDATED_INTENDED_SUBJECTS_PER_COHORT = 1;
    private static final Integer SMALLER_INTENDED_SUBJECTS_PER_COHORT = 0 - 1;

    private static final String DEFAULT_POPULATION_DISEASE_STATE = "AAAAAAAAAA";
    private static final String UPDATED_POPULATION_DISEASE_STATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_MINIMUM_AGE = 0;
    private static final Integer UPDATED_MINIMUM_AGE = 1;
    private static final Integer SMALLER_MINIMUM_AGE = 0 - 1;

    private static final Integer DEFAULT_MAXIMUM_AGE = 0;
    private static final Integer UPDATED_MAXIMUM_AGE = 1;
    private static final Integer SMALLER_MAXIMUM_AGE = 0 - 1;

    private static final Integer DEFAULT_SUBJECTS_ENROLLED = 0;
    private static final Integer UPDATED_SUBJECTS_ENROLLED = 1;
    private static final Integer SMALLER_SUBJECTS_ENROLLED = 0 - 1;

    private static final Boolean DEFAULT_FEMALES_ELIGIBLE = false;
    private static final Boolean UPDATED_FEMALES_ELIGIBLE = true;

    private static final Boolean DEFAULT_MALES_ELIGIBLE = false;
    private static final Boolean UPDATED_MALES_ELIGIBLE = true;

    private static final String DEFAULT_STUDY_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STUDY_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_MANAGER = "BBBBBBBBBB";

    private static final String DEFAULT_PRINCIPAL_PHYSICIAN = "AAAAAAAAAA";
    private static final String UPDATED_PRINCIPAL_PHYSICIAN = "BBBBBBBBBB";

    private static final String DEFAULT_RESEARCH_REPRESENTATIVE = "AAAAAAAAAA";
    private static final String UPDATED_RESEARCH_REPRESENTATIVE = "BBBBBBBBBB";

    private static final String DEFAULT_ANALYSIS_REPRESENTATIVE = "AAAAAAAAAA";
    private static final String UPDATED_ANALYSIS_REPRESENTATIVE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_DATA_MANAGER = "BBBBBBBBBB";

    @Autowired
    private ClinicalStudyRepository clinicalStudyRepository;

    @Mock
    private ClinicalStudyRepository clinicalStudyRepositoryMock;

    @Mock
    private ClinicalStudyService clinicalStudyServiceMock;

    @Autowired
    private ClinicalStudyService clinicalStudyService;

    @Autowired
    private ClinicalStudyQueryService clinicalStudyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restClinicalStudyMockMvc;

    private ClinicalStudy clinicalStudy;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClinicalStudyResource clinicalStudyResource = new ClinicalStudyResource(clinicalStudyService, clinicalStudyQueryService);
        this.restClinicalStudyMockMvc = MockMvcBuilders.standaloneSetup(clinicalStudyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClinicalStudy createEntity(EntityManager em) {
        ClinicalStudy clinicalStudy = new ClinicalStudy()
            .studyIdentifier(DEFAULT_STUDY_IDENTIFIER)
            .phase(DEFAULT_PHASE)
            .status(DEFAULT_STATUS)
            .sequence(DEFAULT_SEQUENCE)
            .studyYear(DEFAULT_STUDY_YEAR)
            .name(DEFAULT_NAME)
            .design(DEFAULT_DESIGN)
            .numberOfCohorts(DEFAULT_NUMBER_OF_COHORTS)
            .intendedSubjectsPerCohort(DEFAULT_INTENDED_SUBJECTS_PER_COHORT)
            .populationDiseaseState(DEFAULT_POPULATION_DISEASE_STATE)
            .minimumAge(DEFAULT_MINIMUM_AGE)
            .maximumAge(DEFAULT_MAXIMUM_AGE)
            .subjectsEnrolled(DEFAULT_SUBJECTS_ENROLLED)
            .femalesEligible(DEFAULT_FEMALES_ELIGIBLE)
            .malesEligible(DEFAULT_MALES_ELIGIBLE)
            .studyShortName(DEFAULT_STUDY_SHORT_NAME)
            .projectManager(DEFAULT_PROJECT_MANAGER)
            .principalPhysician(DEFAULT_PRINCIPAL_PHYSICIAN)
            .researchRepresentative(DEFAULT_RESEARCH_REPRESENTATIVE)
            .analysisRepresentative(DEFAULT_ANALYSIS_REPRESENTATIVE)
            .dataManager(DEFAULT_DATA_MANAGER);
        return clinicalStudy;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClinicalStudy createUpdatedEntity(EntityManager em) {
        ClinicalStudy clinicalStudy = new ClinicalStudy()
            .studyIdentifier(UPDATED_STUDY_IDENTIFIER)
            .phase(UPDATED_PHASE)
            .status(UPDATED_STATUS)
            .sequence(UPDATED_SEQUENCE)
            .studyYear(UPDATED_STUDY_YEAR)
            .name(UPDATED_NAME)
            .design(UPDATED_DESIGN)
            .numberOfCohorts(UPDATED_NUMBER_OF_COHORTS)
            .intendedSubjectsPerCohort(UPDATED_INTENDED_SUBJECTS_PER_COHORT)
            .populationDiseaseState(UPDATED_POPULATION_DISEASE_STATE)
            .minimumAge(UPDATED_MINIMUM_AGE)
            .maximumAge(UPDATED_MAXIMUM_AGE)
            .subjectsEnrolled(UPDATED_SUBJECTS_ENROLLED)
            .femalesEligible(UPDATED_FEMALES_ELIGIBLE)
            .malesEligible(UPDATED_MALES_ELIGIBLE)
            .studyShortName(UPDATED_STUDY_SHORT_NAME)
            .projectManager(UPDATED_PROJECT_MANAGER)
            .principalPhysician(UPDATED_PRINCIPAL_PHYSICIAN)
            .researchRepresentative(UPDATED_RESEARCH_REPRESENTATIVE)
            .analysisRepresentative(UPDATED_ANALYSIS_REPRESENTATIVE)
            .dataManager(UPDATED_DATA_MANAGER);
        return clinicalStudy;
    }

    @BeforeEach
    public void initTest() {
        clinicalStudy = createEntity(em);
    }

    @Test
    @Transactional
    public void createClinicalStudy() throws Exception {
        int databaseSizeBeforeCreate = clinicalStudyRepository.findAll().size();

        // Create the ClinicalStudy
        restClinicalStudyMockMvc.perform(post("/api/clinical-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicalStudy)))
            .andExpect(status().isCreated());

        // Validate the ClinicalStudy in the database
        List<ClinicalStudy> clinicalStudyList = clinicalStudyRepository.findAll();
        assertThat(clinicalStudyList).hasSize(databaseSizeBeforeCreate + 1);
        ClinicalStudy testClinicalStudy = clinicalStudyList.get(clinicalStudyList.size() - 1);
        assertThat(testClinicalStudy.getStudyIdentifier()).isEqualTo(DEFAULT_STUDY_IDENTIFIER);
        assertThat(testClinicalStudy.getPhase()).isEqualTo(DEFAULT_PHASE);
        assertThat(testClinicalStudy.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testClinicalStudy.getSequence()).isEqualTo(DEFAULT_SEQUENCE);
        assertThat(testClinicalStudy.getStudyYear()).isEqualTo(DEFAULT_STUDY_YEAR);
        assertThat(testClinicalStudy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClinicalStudy.getDesign()).isEqualTo(DEFAULT_DESIGN);
        assertThat(testClinicalStudy.getNumberOfCohorts()).isEqualTo(DEFAULT_NUMBER_OF_COHORTS);
        assertThat(testClinicalStudy.getIntendedSubjectsPerCohort()).isEqualTo(DEFAULT_INTENDED_SUBJECTS_PER_COHORT);
        assertThat(testClinicalStudy.getPopulationDiseaseState()).isEqualTo(DEFAULT_POPULATION_DISEASE_STATE);
        assertThat(testClinicalStudy.getMinimumAge()).isEqualTo(DEFAULT_MINIMUM_AGE);
        assertThat(testClinicalStudy.getMaximumAge()).isEqualTo(DEFAULT_MAXIMUM_AGE);
        assertThat(testClinicalStudy.getSubjectsEnrolled()).isEqualTo(DEFAULT_SUBJECTS_ENROLLED);
        assertThat(testClinicalStudy.isFemalesEligible()).isEqualTo(DEFAULT_FEMALES_ELIGIBLE);
        assertThat(testClinicalStudy.isMalesEligible()).isEqualTo(DEFAULT_MALES_ELIGIBLE);
        assertThat(testClinicalStudy.getStudyShortName()).isEqualTo(DEFAULT_STUDY_SHORT_NAME);
        assertThat(testClinicalStudy.getProjectManager()).isEqualTo(DEFAULT_PROJECT_MANAGER);
        assertThat(testClinicalStudy.getPrincipalPhysician()).isEqualTo(DEFAULT_PRINCIPAL_PHYSICIAN);
        assertThat(testClinicalStudy.getResearchRepresentative()).isEqualTo(DEFAULT_RESEARCH_REPRESENTATIVE);
        assertThat(testClinicalStudy.getAnalysisRepresentative()).isEqualTo(DEFAULT_ANALYSIS_REPRESENTATIVE);
        assertThat(testClinicalStudy.getDataManager()).isEqualTo(DEFAULT_DATA_MANAGER);
    }

    @Test
    @Transactional
    public void createClinicalStudyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clinicalStudyRepository.findAll().size();

        // Create the ClinicalStudy with an existing ID
        clinicalStudy.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClinicalStudyMockMvc.perform(post("/api/clinical-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicalStudy)))
            .andExpect(status().isBadRequest());

        // Validate the ClinicalStudy in the database
        List<ClinicalStudy> clinicalStudyList = clinicalStudyRepository.findAll();
        assertThat(clinicalStudyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStudyShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicalStudyRepository.findAll().size();
        // set the field null
        clinicalStudy.setStudyShortName(null);

        // Create the ClinicalStudy, which fails.

        restClinicalStudyMockMvc.perform(post("/api/clinical-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicalStudy)))
            .andExpect(status().isBadRequest());

        List<ClinicalStudy> clinicalStudyList = clinicalStudyRepository.findAll();
        assertThat(clinicalStudyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectManagerIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicalStudyRepository.findAll().size();
        // set the field null
        clinicalStudy.setProjectManager(null);

        // Create the ClinicalStudy, which fails.

        restClinicalStudyMockMvc.perform(post("/api/clinical-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicalStudy)))
            .andExpect(status().isBadRequest());

        List<ClinicalStudy> clinicalStudyList = clinicalStudyRepository.findAll();
        assertThat(clinicalStudyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClinicalStudies() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList
        restClinicalStudyMockMvc.perform(get("/api/clinical-studies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicalStudy.getId().intValue())))
            .andExpect(jsonPath("$.[*].studyIdentifier").value(hasItem(DEFAULT_STUDY_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].phase").value(hasItem(DEFAULT_PHASE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)))
            .andExpect(jsonPath("$.[*].studyYear").value(hasItem(DEFAULT_STUDY_YEAR)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].design").value(hasItem(DEFAULT_DESIGN.toString())))
            .andExpect(jsonPath("$.[*].numberOfCohorts").value(hasItem(DEFAULT_NUMBER_OF_COHORTS)))
            .andExpect(jsonPath("$.[*].intendedSubjectsPerCohort").value(hasItem(DEFAULT_INTENDED_SUBJECTS_PER_COHORT)))
            .andExpect(jsonPath("$.[*].populationDiseaseState").value(hasItem(DEFAULT_POPULATION_DISEASE_STATE.toString())))
            .andExpect(jsonPath("$.[*].minimumAge").value(hasItem(DEFAULT_MINIMUM_AGE)))
            .andExpect(jsonPath("$.[*].maximumAge").value(hasItem(DEFAULT_MAXIMUM_AGE)))
            .andExpect(jsonPath("$.[*].subjectsEnrolled").value(hasItem(DEFAULT_SUBJECTS_ENROLLED)))
            .andExpect(jsonPath("$.[*].femalesEligible").value(hasItem(DEFAULT_FEMALES_ELIGIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].malesEligible").value(hasItem(DEFAULT_MALES_ELIGIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].studyShortName").value(hasItem(DEFAULT_STUDY_SHORT_NAME.toString())))
            .andExpect(jsonPath("$.[*].projectManager").value(hasItem(DEFAULT_PROJECT_MANAGER.toString())))
            .andExpect(jsonPath("$.[*].principalPhysician").value(hasItem(DEFAULT_PRINCIPAL_PHYSICIAN.toString())))
            .andExpect(jsonPath("$.[*].researchRepresentative").value(hasItem(DEFAULT_RESEARCH_REPRESENTATIVE.toString())))
            .andExpect(jsonPath("$.[*].analysisRepresentative").value(hasItem(DEFAULT_ANALYSIS_REPRESENTATIVE.toString())))
            .andExpect(jsonPath("$.[*].dataManager").value(hasItem(DEFAULT_DATA_MANAGER.toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllClinicalStudiesWithEagerRelationshipsIsEnabled() throws Exception {
        ClinicalStudyResource clinicalStudyResource = new ClinicalStudyResource(clinicalStudyServiceMock, clinicalStudyQueryService);
        when(clinicalStudyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restClinicalStudyMockMvc = MockMvcBuilders.standaloneSetup(clinicalStudyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restClinicalStudyMockMvc.perform(get("/api/clinical-studies?eagerload=true"))
        .andExpect(status().isOk());

        verify(clinicalStudyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllClinicalStudiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        ClinicalStudyResource clinicalStudyResource = new ClinicalStudyResource(clinicalStudyServiceMock, clinicalStudyQueryService);
            when(clinicalStudyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restClinicalStudyMockMvc = MockMvcBuilders.standaloneSetup(clinicalStudyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restClinicalStudyMockMvc.perform(get("/api/clinical-studies?eagerload=true"))
        .andExpect(status().isOk());

            verify(clinicalStudyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getClinicalStudy() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get the clinicalStudy
        restClinicalStudyMockMvc.perform(get("/api/clinical-studies/{id}", clinicalStudy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clinicalStudy.getId().intValue()))
            .andExpect(jsonPath("$.studyIdentifier").value(DEFAULT_STUDY_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.phase").value(DEFAULT_PHASE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE))
            .andExpect(jsonPath("$.studyYear").value(DEFAULT_STUDY_YEAR))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.design").value(DEFAULT_DESIGN.toString()))
            .andExpect(jsonPath("$.numberOfCohorts").value(DEFAULT_NUMBER_OF_COHORTS))
            .andExpect(jsonPath("$.intendedSubjectsPerCohort").value(DEFAULT_INTENDED_SUBJECTS_PER_COHORT))
            .andExpect(jsonPath("$.populationDiseaseState").value(DEFAULT_POPULATION_DISEASE_STATE.toString()))
            .andExpect(jsonPath("$.minimumAge").value(DEFAULT_MINIMUM_AGE))
            .andExpect(jsonPath("$.maximumAge").value(DEFAULT_MAXIMUM_AGE))
            .andExpect(jsonPath("$.subjectsEnrolled").value(DEFAULT_SUBJECTS_ENROLLED))
            .andExpect(jsonPath("$.femalesEligible").value(DEFAULT_FEMALES_ELIGIBLE.booleanValue()))
            .andExpect(jsonPath("$.malesEligible").value(DEFAULT_MALES_ELIGIBLE.booleanValue()))
            .andExpect(jsonPath("$.studyShortName").value(DEFAULT_STUDY_SHORT_NAME.toString()))
            .andExpect(jsonPath("$.projectManager").value(DEFAULT_PROJECT_MANAGER.toString()))
            .andExpect(jsonPath("$.principalPhysician").value(DEFAULT_PRINCIPAL_PHYSICIAN.toString()))
            .andExpect(jsonPath("$.researchRepresentative").value(DEFAULT_RESEARCH_REPRESENTATIVE.toString()))
            .andExpect(jsonPath("$.analysisRepresentative").value(DEFAULT_ANALYSIS_REPRESENTATIVE.toString()))
            .andExpect(jsonPath("$.dataManager").value(DEFAULT_DATA_MANAGER.toString()));
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyIdentifier equals to DEFAULT_STUDY_IDENTIFIER
        defaultClinicalStudyShouldBeFound("studyIdentifier.equals=" + DEFAULT_STUDY_IDENTIFIER);

        // Get all the clinicalStudyList where studyIdentifier equals to UPDATED_STUDY_IDENTIFIER
        defaultClinicalStudyShouldNotBeFound("studyIdentifier.equals=" + UPDATED_STUDY_IDENTIFIER);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyIdentifier in DEFAULT_STUDY_IDENTIFIER or UPDATED_STUDY_IDENTIFIER
        defaultClinicalStudyShouldBeFound("studyIdentifier.in=" + DEFAULT_STUDY_IDENTIFIER + "," + UPDATED_STUDY_IDENTIFIER);

        // Get all the clinicalStudyList where studyIdentifier equals to UPDATED_STUDY_IDENTIFIER
        defaultClinicalStudyShouldNotBeFound("studyIdentifier.in=" + UPDATED_STUDY_IDENTIFIER);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyIdentifier is not null
        defaultClinicalStudyShouldBeFound("studyIdentifier.specified=true");

        // Get all the clinicalStudyList where studyIdentifier is null
        defaultClinicalStudyShouldNotBeFound("studyIdentifier.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByPhaseIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where phase equals to DEFAULT_PHASE
        defaultClinicalStudyShouldBeFound("phase.equals=" + DEFAULT_PHASE);

        // Get all the clinicalStudyList where phase equals to UPDATED_PHASE
        defaultClinicalStudyShouldNotBeFound("phase.equals=" + UPDATED_PHASE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByPhaseIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where phase in DEFAULT_PHASE or UPDATED_PHASE
        defaultClinicalStudyShouldBeFound("phase.in=" + DEFAULT_PHASE + "," + UPDATED_PHASE);

        // Get all the clinicalStudyList where phase equals to UPDATED_PHASE
        defaultClinicalStudyShouldNotBeFound("phase.in=" + UPDATED_PHASE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByPhaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where phase is not null
        defaultClinicalStudyShouldBeFound("phase.specified=true");

        // Get all the clinicalStudyList where phase is null
        defaultClinicalStudyShouldNotBeFound("phase.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where status equals to DEFAULT_STATUS
        defaultClinicalStudyShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the clinicalStudyList where status equals to UPDATED_STATUS
        defaultClinicalStudyShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultClinicalStudyShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the clinicalStudyList where status equals to UPDATED_STATUS
        defaultClinicalStudyShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where status is not null
        defaultClinicalStudyShouldBeFound("status.specified=true");

        // Get all the clinicalStudyList where status is null
        defaultClinicalStudyShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySequenceIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where sequence equals to DEFAULT_SEQUENCE
        defaultClinicalStudyShouldBeFound("sequence.equals=" + DEFAULT_SEQUENCE);

        // Get all the clinicalStudyList where sequence equals to UPDATED_SEQUENCE
        defaultClinicalStudyShouldNotBeFound("sequence.equals=" + UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySequenceIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where sequence in DEFAULT_SEQUENCE or UPDATED_SEQUENCE
        defaultClinicalStudyShouldBeFound("sequence.in=" + DEFAULT_SEQUENCE + "," + UPDATED_SEQUENCE);

        // Get all the clinicalStudyList where sequence equals to UPDATED_SEQUENCE
        defaultClinicalStudyShouldNotBeFound("sequence.in=" + UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySequenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where sequence is not null
        defaultClinicalStudyShouldBeFound("sequence.specified=true");

        // Get all the clinicalStudyList where sequence is null
        defaultClinicalStudyShouldNotBeFound("sequence.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySequenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where sequence is greater than or equal to DEFAULT_SEQUENCE
        defaultClinicalStudyShouldBeFound("sequence.greaterThanOrEqual=" + DEFAULT_SEQUENCE);

        // Get all the clinicalStudyList where sequence is greater than or equal to UPDATED_SEQUENCE
        defaultClinicalStudyShouldNotBeFound("sequence.greaterThanOrEqual=" + UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySequenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where sequence is less than or equal to DEFAULT_SEQUENCE
        defaultClinicalStudyShouldBeFound("sequence.lessThanOrEqual=" + DEFAULT_SEQUENCE);

        // Get all the clinicalStudyList where sequence is less than or equal to SMALLER_SEQUENCE
        defaultClinicalStudyShouldNotBeFound("sequence.lessThanOrEqual=" + SMALLER_SEQUENCE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySequenceIsLessThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where sequence is less than DEFAULT_SEQUENCE
        defaultClinicalStudyShouldNotBeFound("sequence.lessThan=" + DEFAULT_SEQUENCE);

        // Get all the clinicalStudyList where sequence is less than UPDATED_SEQUENCE
        defaultClinicalStudyShouldBeFound("sequence.lessThan=" + UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySequenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where sequence is greater than DEFAULT_SEQUENCE
        defaultClinicalStudyShouldNotBeFound("sequence.greaterThan=" + DEFAULT_SEQUENCE);

        // Get all the clinicalStudyList where sequence is greater than SMALLER_SEQUENCE
        defaultClinicalStudyShouldBeFound("sequence.greaterThan=" + SMALLER_SEQUENCE);
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyYearIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyYear equals to DEFAULT_STUDY_YEAR
        defaultClinicalStudyShouldBeFound("studyYear.equals=" + DEFAULT_STUDY_YEAR);

        // Get all the clinicalStudyList where studyYear equals to UPDATED_STUDY_YEAR
        defaultClinicalStudyShouldNotBeFound("studyYear.equals=" + UPDATED_STUDY_YEAR);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyYearIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyYear in DEFAULT_STUDY_YEAR or UPDATED_STUDY_YEAR
        defaultClinicalStudyShouldBeFound("studyYear.in=" + DEFAULT_STUDY_YEAR + "," + UPDATED_STUDY_YEAR);

        // Get all the clinicalStudyList where studyYear equals to UPDATED_STUDY_YEAR
        defaultClinicalStudyShouldNotBeFound("studyYear.in=" + UPDATED_STUDY_YEAR);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyYear is not null
        defaultClinicalStudyShouldBeFound("studyYear.specified=true");

        // Get all the clinicalStudyList where studyYear is null
        defaultClinicalStudyShouldNotBeFound("studyYear.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyYear is greater than or equal to DEFAULT_STUDY_YEAR
        defaultClinicalStudyShouldBeFound("studyYear.greaterThanOrEqual=" + DEFAULT_STUDY_YEAR);

        // Get all the clinicalStudyList where studyYear is greater than or equal to UPDATED_STUDY_YEAR
        defaultClinicalStudyShouldNotBeFound("studyYear.greaterThanOrEqual=" + UPDATED_STUDY_YEAR);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyYear is less than or equal to DEFAULT_STUDY_YEAR
        defaultClinicalStudyShouldBeFound("studyYear.lessThanOrEqual=" + DEFAULT_STUDY_YEAR);

        // Get all the clinicalStudyList where studyYear is less than or equal to SMALLER_STUDY_YEAR
        defaultClinicalStudyShouldNotBeFound("studyYear.lessThanOrEqual=" + SMALLER_STUDY_YEAR);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyYearIsLessThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyYear is less than DEFAULT_STUDY_YEAR
        defaultClinicalStudyShouldNotBeFound("studyYear.lessThan=" + DEFAULT_STUDY_YEAR);

        // Get all the clinicalStudyList where studyYear is less than UPDATED_STUDY_YEAR
        defaultClinicalStudyShouldBeFound("studyYear.lessThan=" + UPDATED_STUDY_YEAR);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyYear is greater than DEFAULT_STUDY_YEAR
        defaultClinicalStudyShouldNotBeFound("studyYear.greaterThan=" + DEFAULT_STUDY_YEAR);

        // Get all the clinicalStudyList where studyYear is greater than SMALLER_STUDY_YEAR
        defaultClinicalStudyShouldBeFound("studyYear.greaterThan=" + SMALLER_STUDY_YEAR);
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where name equals to DEFAULT_NAME
        defaultClinicalStudyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the clinicalStudyList where name equals to UPDATED_NAME
        defaultClinicalStudyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultClinicalStudyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the clinicalStudyList where name equals to UPDATED_NAME
        defaultClinicalStudyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where name is not null
        defaultClinicalStudyShouldBeFound("name.specified=true");

        // Get all the clinicalStudyList where name is null
        defaultClinicalStudyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByDesignIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where design equals to DEFAULT_DESIGN
        defaultClinicalStudyShouldBeFound("design.equals=" + DEFAULT_DESIGN);

        // Get all the clinicalStudyList where design equals to UPDATED_DESIGN
        defaultClinicalStudyShouldNotBeFound("design.equals=" + UPDATED_DESIGN);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByDesignIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where design in DEFAULT_DESIGN or UPDATED_DESIGN
        defaultClinicalStudyShouldBeFound("design.in=" + DEFAULT_DESIGN + "," + UPDATED_DESIGN);

        // Get all the clinicalStudyList where design equals to UPDATED_DESIGN
        defaultClinicalStudyShouldNotBeFound("design.in=" + UPDATED_DESIGN);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByDesignIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where design is not null
        defaultClinicalStudyShouldBeFound("design.specified=true");

        // Get all the clinicalStudyList where design is null
        defaultClinicalStudyShouldNotBeFound("design.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNumberOfCohortsIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where numberOfCohorts equals to DEFAULT_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldBeFound("numberOfCohorts.equals=" + DEFAULT_NUMBER_OF_COHORTS);

        // Get all the clinicalStudyList where numberOfCohorts equals to UPDATED_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldNotBeFound("numberOfCohorts.equals=" + UPDATED_NUMBER_OF_COHORTS);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNumberOfCohortsIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where numberOfCohorts in DEFAULT_NUMBER_OF_COHORTS or UPDATED_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldBeFound("numberOfCohorts.in=" + DEFAULT_NUMBER_OF_COHORTS + "," + UPDATED_NUMBER_OF_COHORTS);

        // Get all the clinicalStudyList where numberOfCohorts equals to UPDATED_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldNotBeFound("numberOfCohorts.in=" + UPDATED_NUMBER_OF_COHORTS);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNumberOfCohortsIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where numberOfCohorts is not null
        defaultClinicalStudyShouldBeFound("numberOfCohorts.specified=true");

        // Get all the clinicalStudyList where numberOfCohorts is null
        defaultClinicalStudyShouldNotBeFound("numberOfCohorts.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNumberOfCohortsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where numberOfCohorts is greater than or equal to DEFAULT_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldBeFound("numberOfCohorts.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_COHORTS);

        // Get all the clinicalStudyList where numberOfCohorts is greater than or equal to UPDATED_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldNotBeFound("numberOfCohorts.greaterThanOrEqual=" + UPDATED_NUMBER_OF_COHORTS);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNumberOfCohortsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where numberOfCohorts is less than or equal to DEFAULT_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldBeFound("numberOfCohorts.lessThanOrEqual=" + DEFAULT_NUMBER_OF_COHORTS);

        // Get all the clinicalStudyList where numberOfCohorts is less than or equal to SMALLER_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldNotBeFound("numberOfCohorts.lessThanOrEqual=" + SMALLER_NUMBER_OF_COHORTS);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNumberOfCohortsIsLessThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where numberOfCohorts is less than DEFAULT_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldNotBeFound("numberOfCohorts.lessThan=" + DEFAULT_NUMBER_OF_COHORTS);

        // Get all the clinicalStudyList where numberOfCohorts is less than UPDATED_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldBeFound("numberOfCohorts.lessThan=" + UPDATED_NUMBER_OF_COHORTS);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByNumberOfCohortsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where numberOfCohorts is greater than DEFAULT_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldNotBeFound("numberOfCohorts.greaterThan=" + DEFAULT_NUMBER_OF_COHORTS);

        // Get all the clinicalStudyList where numberOfCohorts is greater than SMALLER_NUMBER_OF_COHORTS
        defaultClinicalStudyShouldBeFound("numberOfCohorts.greaterThan=" + SMALLER_NUMBER_OF_COHORTS);
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByIntendedSubjectsPerCohortIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort equals to DEFAULT_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldBeFound("intendedSubjectsPerCohort.equals=" + DEFAULT_INTENDED_SUBJECTS_PER_COHORT);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort equals to UPDATED_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldNotBeFound("intendedSubjectsPerCohort.equals=" + UPDATED_INTENDED_SUBJECTS_PER_COHORT);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByIntendedSubjectsPerCohortIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort in DEFAULT_INTENDED_SUBJECTS_PER_COHORT or UPDATED_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldBeFound("intendedSubjectsPerCohort.in=" + DEFAULT_INTENDED_SUBJECTS_PER_COHORT + "," + UPDATED_INTENDED_SUBJECTS_PER_COHORT);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort equals to UPDATED_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldNotBeFound("intendedSubjectsPerCohort.in=" + UPDATED_INTENDED_SUBJECTS_PER_COHORT);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByIntendedSubjectsPerCohortIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is not null
        defaultClinicalStudyShouldBeFound("intendedSubjectsPerCohort.specified=true");

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is null
        defaultClinicalStudyShouldNotBeFound("intendedSubjectsPerCohort.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByIntendedSubjectsPerCohortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is greater than or equal to DEFAULT_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldBeFound("intendedSubjectsPerCohort.greaterThanOrEqual=" + DEFAULT_INTENDED_SUBJECTS_PER_COHORT);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is greater than or equal to UPDATED_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldNotBeFound("intendedSubjectsPerCohort.greaterThanOrEqual=" + UPDATED_INTENDED_SUBJECTS_PER_COHORT);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByIntendedSubjectsPerCohortIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is less than or equal to DEFAULT_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldBeFound("intendedSubjectsPerCohort.lessThanOrEqual=" + DEFAULT_INTENDED_SUBJECTS_PER_COHORT);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is less than or equal to SMALLER_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldNotBeFound("intendedSubjectsPerCohort.lessThanOrEqual=" + SMALLER_INTENDED_SUBJECTS_PER_COHORT);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByIntendedSubjectsPerCohortIsLessThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is less than DEFAULT_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldNotBeFound("intendedSubjectsPerCohort.lessThan=" + DEFAULT_INTENDED_SUBJECTS_PER_COHORT);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is less than UPDATED_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldBeFound("intendedSubjectsPerCohort.lessThan=" + UPDATED_INTENDED_SUBJECTS_PER_COHORT);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByIntendedSubjectsPerCohortIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is greater than DEFAULT_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldNotBeFound("intendedSubjectsPerCohort.greaterThan=" + DEFAULT_INTENDED_SUBJECTS_PER_COHORT);

        // Get all the clinicalStudyList where intendedSubjectsPerCohort is greater than SMALLER_INTENDED_SUBJECTS_PER_COHORT
        defaultClinicalStudyShouldBeFound("intendedSubjectsPerCohort.greaterThan=" + SMALLER_INTENDED_SUBJECTS_PER_COHORT);
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByPopulationDiseaseStateIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where populationDiseaseState equals to DEFAULT_POPULATION_DISEASE_STATE
        defaultClinicalStudyShouldBeFound("populationDiseaseState.equals=" + DEFAULT_POPULATION_DISEASE_STATE);

        // Get all the clinicalStudyList where populationDiseaseState equals to UPDATED_POPULATION_DISEASE_STATE
        defaultClinicalStudyShouldNotBeFound("populationDiseaseState.equals=" + UPDATED_POPULATION_DISEASE_STATE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByPopulationDiseaseStateIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where populationDiseaseState in DEFAULT_POPULATION_DISEASE_STATE or UPDATED_POPULATION_DISEASE_STATE
        defaultClinicalStudyShouldBeFound("populationDiseaseState.in=" + DEFAULT_POPULATION_DISEASE_STATE + "," + UPDATED_POPULATION_DISEASE_STATE);

        // Get all the clinicalStudyList where populationDiseaseState equals to UPDATED_POPULATION_DISEASE_STATE
        defaultClinicalStudyShouldNotBeFound("populationDiseaseState.in=" + UPDATED_POPULATION_DISEASE_STATE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByPopulationDiseaseStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where populationDiseaseState is not null
        defaultClinicalStudyShouldBeFound("populationDiseaseState.specified=true");

        // Get all the clinicalStudyList where populationDiseaseState is null
        defaultClinicalStudyShouldNotBeFound("populationDiseaseState.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMinimumAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where minimumAge equals to DEFAULT_MINIMUM_AGE
        defaultClinicalStudyShouldBeFound("minimumAge.equals=" + DEFAULT_MINIMUM_AGE);

        // Get all the clinicalStudyList where minimumAge equals to UPDATED_MINIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("minimumAge.equals=" + UPDATED_MINIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMinimumAgeIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where minimumAge in DEFAULT_MINIMUM_AGE or UPDATED_MINIMUM_AGE
        defaultClinicalStudyShouldBeFound("minimumAge.in=" + DEFAULT_MINIMUM_AGE + "," + UPDATED_MINIMUM_AGE);

        // Get all the clinicalStudyList where minimumAge equals to UPDATED_MINIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("minimumAge.in=" + UPDATED_MINIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMinimumAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where minimumAge is not null
        defaultClinicalStudyShouldBeFound("minimumAge.specified=true");

        // Get all the clinicalStudyList where minimumAge is null
        defaultClinicalStudyShouldNotBeFound("minimumAge.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMinimumAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where minimumAge is greater than or equal to DEFAULT_MINIMUM_AGE
        defaultClinicalStudyShouldBeFound("minimumAge.greaterThanOrEqual=" + DEFAULT_MINIMUM_AGE);

        // Get all the clinicalStudyList where minimumAge is greater than or equal to UPDATED_MINIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("minimumAge.greaterThanOrEqual=" + UPDATED_MINIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMinimumAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where minimumAge is less than or equal to DEFAULT_MINIMUM_AGE
        defaultClinicalStudyShouldBeFound("minimumAge.lessThanOrEqual=" + DEFAULT_MINIMUM_AGE);

        // Get all the clinicalStudyList where minimumAge is less than or equal to SMALLER_MINIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("minimumAge.lessThanOrEqual=" + SMALLER_MINIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMinimumAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where minimumAge is less than DEFAULT_MINIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("minimumAge.lessThan=" + DEFAULT_MINIMUM_AGE);

        // Get all the clinicalStudyList where minimumAge is less than UPDATED_MINIMUM_AGE
        defaultClinicalStudyShouldBeFound("minimumAge.lessThan=" + UPDATED_MINIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMinimumAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where minimumAge is greater than DEFAULT_MINIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("minimumAge.greaterThan=" + DEFAULT_MINIMUM_AGE);

        // Get all the clinicalStudyList where minimumAge is greater than SMALLER_MINIMUM_AGE
        defaultClinicalStudyShouldBeFound("minimumAge.greaterThan=" + SMALLER_MINIMUM_AGE);
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByMaximumAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where maximumAge equals to DEFAULT_MAXIMUM_AGE
        defaultClinicalStudyShouldBeFound("maximumAge.equals=" + DEFAULT_MAXIMUM_AGE);

        // Get all the clinicalStudyList where maximumAge equals to UPDATED_MAXIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("maximumAge.equals=" + UPDATED_MAXIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMaximumAgeIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where maximumAge in DEFAULT_MAXIMUM_AGE or UPDATED_MAXIMUM_AGE
        defaultClinicalStudyShouldBeFound("maximumAge.in=" + DEFAULT_MAXIMUM_AGE + "," + UPDATED_MAXIMUM_AGE);

        // Get all the clinicalStudyList where maximumAge equals to UPDATED_MAXIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("maximumAge.in=" + UPDATED_MAXIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMaximumAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where maximumAge is not null
        defaultClinicalStudyShouldBeFound("maximumAge.specified=true");

        // Get all the clinicalStudyList where maximumAge is null
        defaultClinicalStudyShouldNotBeFound("maximumAge.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMaximumAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where maximumAge is greater than or equal to DEFAULT_MAXIMUM_AGE
        defaultClinicalStudyShouldBeFound("maximumAge.greaterThanOrEqual=" + DEFAULT_MAXIMUM_AGE);

        // Get all the clinicalStudyList where maximumAge is greater than or equal to UPDATED_MAXIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("maximumAge.greaterThanOrEqual=" + UPDATED_MAXIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMaximumAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where maximumAge is less than or equal to DEFAULT_MAXIMUM_AGE
        defaultClinicalStudyShouldBeFound("maximumAge.lessThanOrEqual=" + DEFAULT_MAXIMUM_AGE);

        // Get all the clinicalStudyList where maximumAge is less than or equal to SMALLER_MAXIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("maximumAge.lessThanOrEqual=" + SMALLER_MAXIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMaximumAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where maximumAge is less than DEFAULT_MAXIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("maximumAge.lessThan=" + DEFAULT_MAXIMUM_AGE);

        // Get all the clinicalStudyList where maximumAge is less than UPDATED_MAXIMUM_AGE
        defaultClinicalStudyShouldBeFound("maximumAge.lessThan=" + UPDATED_MAXIMUM_AGE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMaximumAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where maximumAge is greater than DEFAULT_MAXIMUM_AGE
        defaultClinicalStudyShouldNotBeFound("maximumAge.greaterThan=" + DEFAULT_MAXIMUM_AGE);

        // Get all the clinicalStudyList where maximumAge is greater than SMALLER_MAXIMUM_AGE
        defaultClinicalStudyShouldBeFound("maximumAge.greaterThan=" + SMALLER_MAXIMUM_AGE);
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesBySubjectsEnrolledIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where subjectsEnrolled equals to DEFAULT_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldBeFound("subjectsEnrolled.equals=" + DEFAULT_SUBJECTS_ENROLLED);

        // Get all the clinicalStudyList where subjectsEnrolled equals to UPDATED_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldNotBeFound("subjectsEnrolled.equals=" + UPDATED_SUBJECTS_ENROLLED);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySubjectsEnrolledIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where subjectsEnrolled in DEFAULT_SUBJECTS_ENROLLED or UPDATED_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldBeFound("subjectsEnrolled.in=" + DEFAULT_SUBJECTS_ENROLLED + "," + UPDATED_SUBJECTS_ENROLLED);

        // Get all the clinicalStudyList where subjectsEnrolled equals to UPDATED_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldNotBeFound("subjectsEnrolled.in=" + UPDATED_SUBJECTS_ENROLLED);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySubjectsEnrolledIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where subjectsEnrolled is not null
        defaultClinicalStudyShouldBeFound("subjectsEnrolled.specified=true");

        // Get all the clinicalStudyList where subjectsEnrolled is null
        defaultClinicalStudyShouldNotBeFound("subjectsEnrolled.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySubjectsEnrolledIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where subjectsEnrolled is greater than or equal to DEFAULT_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldBeFound("subjectsEnrolled.greaterThanOrEqual=" + DEFAULT_SUBJECTS_ENROLLED);

        // Get all the clinicalStudyList where subjectsEnrolled is greater than or equal to UPDATED_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldNotBeFound("subjectsEnrolled.greaterThanOrEqual=" + UPDATED_SUBJECTS_ENROLLED);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySubjectsEnrolledIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where subjectsEnrolled is less than or equal to DEFAULT_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldBeFound("subjectsEnrolled.lessThanOrEqual=" + DEFAULT_SUBJECTS_ENROLLED);

        // Get all the clinicalStudyList where subjectsEnrolled is less than or equal to SMALLER_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldNotBeFound("subjectsEnrolled.lessThanOrEqual=" + SMALLER_SUBJECTS_ENROLLED);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySubjectsEnrolledIsLessThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where subjectsEnrolled is less than DEFAULT_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldNotBeFound("subjectsEnrolled.lessThan=" + DEFAULT_SUBJECTS_ENROLLED);

        // Get all the clinicalStudyList where subjectsEnrolled is less than UPDATED_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldBeFound("subjectsEnrolled.lessThan=" + UPDATED_SUBJECTS_ENROLLED);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesBySubjectsEnrolledIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where subjectsEnrolled is greater than DEFAULT_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldNotBeFound("subjectsEnrolled.greaterThan=" + DEFAULT_SUBJECTS_ENROLLED);

        // Get all the clinicalStudyList where subjectsEnrolled is greater than SMALLER_SUBJECTS_ENROLLED
        defaultClinicalStudyShouldBeFound("subjectsEnrolled.greaterThan=" + SMALLER_SUBJECTS_ENROLLED);
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByFemalesEligibleIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where femalesEligible equals to DEFAULT_FEMALES_ELIGIBLE
        defaultClinicalStudyShouldBeFound("femalesEligible.equals=" + DEFAULT_FEMALES_ELIGIBLE);

        // Get all the clinicalStudyList where femalesEligible equals to UPDATED_FEMALES_ELIGIBLE
        defaultClinicalStudyShouldNotBeFound("femalesEligible.equals=" + UPDATED_FEMALES_ELIGIBLE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByFemalesEligibleIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where femalesEligible in DEFAULT_FEMALES_ELIGIBLE or UPDATED_FEMALES_ELIGIBLE
        defaultClinicalStudyShouldBeFound("femalesEligible.in=" + DEFAULT_FEMALES_ELIGIBLE + "," + UPDATED_FEMALES_ELIGIBLE);

        // Get all the clinicalStudyList where femalesEligible equals to UPDATED_FEMALES_ELIGIBLE
        defaultClinicalStudyShouldNotBeFound("femalesEligible.in=" + UPDATED_FEMALES_ELIGIBLE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByFemalesEligibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where femalesEligible is not null
        defaultClinicalStudyShouldBeFound("femalesEligible.specified=true");

        // Get all the clinicalStudyList where femalesEligible is null
        defaultClinicalStudyShouldNotBeFound("femalesEligible.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMalesEligibleIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where malesEligible equals to DEFAULT_MALES_ELIGIBLE
        defaultClinicalStudyShouldBeFound("malesEligible.equals=" + DEFAULT_MALES_ELIGIBLE);

        // Get all the clinicalStudyList where malesEligible equals to UPDATED_MALES_ELIGIBLE
        defaultClinicalStudyShouldNotBeFound("malesEligible.equals=" + UPDATED_MALES_ELIGIBLE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMalesEligibleIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where malesEligible in DEFAULT_MALES_ELIGIBLE or UPDATED_MALES_ELIGIBLE
        defaultClinicalStudyShouldBeFound("malesEligible.in=" + DEFAULT_MALES_ELIGIBLE + "," + UPDATED_MALES_ELIGIBLE);

        // Get all the clinicalStudyList where malesEligible equals to UPDATED_MALES_ELIGIBLE
        defaultClinicalStudyShouldNotBeFound("malesEligible.in=" + UPDATED_MALES_ELIGIBLE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMalesEligibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where malesEligible is not null
        defaultClinicalStudyShouldBeFound("malesEligible.specified=true");

        // Get all the clinicalStudyList where malesEligible is null
        defaultClinicalStudyShouldNotBeFound("malesEligible.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyShortName equals to DEFAULT_STUDY_SHORT_NAME
        defaultClinicalStudyShouldBeFound("studyShortName.equals=" + DEFAULT_STUDY_SHORT_NAME);

        // Get all the clinicalStudyList where studyShortName equals to UPDATED_STUDY_SHORT_NAME
        defaultClinicalStudyShouldNotBeFound("studyShortName.equals=" + UPDATED_STUDY_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyShortName in DEFAULT_STUDY_SHORT_NAME or UPDATED_STUDY_SHORT_NAME
        defaultClinicalStudyShouldBeFound("studyShortName.in=" + DEFAULT_STUDY_SHORT_NAME + "," + UPDATED_STUDY_SHORT_NAME);

        // Get all the clinicalStudyList where studyShortName equals to UPDATED_STUDY_SHORT_NAME
        defaultClinicalStudyShouldNotBeFound("studyShortName.in=" + UPDATED_STUDY_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByStudyShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where studyShortName is not null
        defaultClinicalStudyShouldBeFound("studyShortName.specified=true");

        // Get all the clinicalStudyList where studyShortName is null
        defaultClinicalStudyShouldNotBeFound("studyShortName.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByProjectManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where projectManager equals to DEFAULT_PROJECT_MANAGER
        defaultClinicalStudyShouldBeFound("projectManager.equals=" + DEFAULT_PROJECT_MANAGER);

        // Get all the clinicalStudyList where projectManager equals to UPDATED_PROJECT_MANAGER
        defaultClinicalStudyShouldNotBeFound("projectManager.equals=" + UPDATED_PROJECT_MANAGER);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByProjectManagerIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where projectManager in DEFAULT_PROJECT_MANAGER or UPDATED_PROJECT_MANAGER
        defaultClinicalStudyShouldBeFound("projectManager.in=" + DEFAULT_PROJECT_MANAGER + "," + UPDATED_PROJECT_MANAGER);

        // Get all the clinicalStudyList where projectManager equals to UPDATED_PROJECT_MANAGER
        defaultClinicalStudyShouldNotBeFound("projectManager.in=" + UPDATED_PROJECT_MANAGER);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByProjectManagerIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where projectManager is not null
        defaultClinicalStudyShouldBeFound("projectManager.specified=true");

        // Get all the clinicalStudyList where projectManager is null
        defaultClinicalStudyShouldNotBeFound("projectManager.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByPrincipalPhysicianIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where principalPhysician equals to DEFAULT_PRINCIPAL_PHYSICIAN
        defaultClinicalStudyShouldBeFound("principalPhysician.equals=" + DEFAULT_PRINCIPAL_PHYSICIAN);

        // Get all the clinicalStudyList where principalPhysician equals to UPDATED_PRINCIPAL_PHYSICIAN
        defaultClinicalStudyShouldNotBeFound("principalPhysician.equals=" + UPDATED_PRINCIPAL_PHYSICIAN);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByPrincipalPhysicianIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where principalPhysician in DEFAULT_PRINCIPAL_PHYSICIAN or UPDATED_PRINCIPAL_PHYSICIAN
        defaultClinicalStudyShouldBeFound("principalPhysician.in=" + DEFAULT_PRINCIPAL_PHYSICIAN + "," + UPDATED_PRINCIPAL_PHYSICIAN);

        // Get all the clinicalStudyList where principalPhysician equals to UPDATED_PRINCIPAL_PHYSICIAN
        defaultClinicalStudyShouldNotBeFound("principalPhysician.in=" + UPDATED_PRINCIPAL_PHYSICIAN);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByPrincipalPhysicianIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where principalPhysician is not null
        defaultClinicalStudyShouldBeFound("principalPhysician.specified=true");

        // Get all the clinicalStudyList where principalPhysician is null
        defaultClinicalStudyShouldNotBeFound("principalPhysician.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByResearchRepresentativeIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where researchRepresentative equals to DEFAULT_RESEARCH_REPRESENTATIVE
        defaultClinicalStudyShouldBeFound("researchRepresentative.equals=" + DEFAULT_RESEARCH_REPRESENTATIVE);

        // Get all the clinicalStudyList where researchRepresentative equals to UPDATED_RESEARCH_REPRESENTATIVE
        defaultClinicalStudyShouldNotBeFound("researchRepresentative.equals=" + UPDATED_RESEARCH_REPRESENTATIVE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByResearchRepresentativeIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where researchRepresentative in DEFAULT_RESEARCH_REPRESENTATIVE or UPDATED_RESEARCH_REPRESENTATIVE
        defaultClinicalStudyShouldBeFound("researchRepresentative.in=" + DEFAULT_RESEARCH_REPRESENTATIVE + "," + UPDATED_RESEARCH_REPRESENTATIVE);

        // Get all the clinicalStudyList where researchRepresentative equals to UPDATED_RESEARCH_REPRESENTATIVE
        defaultClinicalStudyShouldNotBeFound("researchRepresentative.in=" + UPDATED_RESEARCH_REPRESENTATIVE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByResearchRepresentativeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where researchRepresentative is not null
        defaultClinicalStudyShouldBeFound("researchRepresentative.specified=true");

        // Get all the clinicalStudyList where researchRepresentative is null
        defaultClinicalStudyShouldNotBeFound("researchRepresentative.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByAnalysisRepresentativeIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where analysisRepresentative equals to DEFAULT_ANALYSIS_REPRESENTATIVE
        defaultClinicalStudyShouldBeFound("analysisRepresentative.equals=" + DEFAULT_ANALYSIS_REPRESENTATIVE);

        // Get all the clinicalStudyList where analysisRepresentative equals to UPDATED_ANALYSIS_REPRESENTATIVE
        defaultClinicalStudyShouldNotBeFound("analysisRepresentative.equals=" + UPDATED_ANALYSIS_REPRESENTATIVE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByAnalysisRepresentativeIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where analysisRepresentative in DEFAULT_ANALYSIS_REPRESENTATIVE or UPDATED_ANALYSIS_REPRESENTATIVE
        defaultClinicalStudyShouldBeFound("analysisRepresentative.in=" + DEFAULT_ANALYSIS_REPRESENTATIVE + "," + UPDATED_ANALYSIS_REPRESENTATIVE);

        // Get all the clinicalStudyList where analysisRepresentative equals to UPDATED_ANALYSIS_REPRESENTATIVE
        defaultClinicalStudyShouldNotBeFound("analysisRepresentative.in=" + UPDATED_ANALYSIS_REPRESENTATIVE);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByAnalysisRepresentativeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where analysisRepresentative is not null
        defaultClinicalStudyShouldBeFound("analysisRepresentative.specified=true");

        // Get all the clinicalStudyList where analysisRepresentative is null
        defaultClinicalStudyShouldNotBeFound("analysisRepresentative.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByDataManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where dataManager equals to DEFAULT_DATA_MANAGER
        defaultClinicalStudyShouldBeFound("dataManager.equals=" + DEFAULT_DATA_MANAGER);

        // Get all the clinicalStudyList where dataManager equals to UPDATED_DATA_MANAGER
        defaultClinicalStudyShouldNotBeFound("dataManager.equals=" + UPDATED_DATA_MANAGER);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByDataManagerIsInShouldWork() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where dataManager in DEFAULT_DATA_MANAGER or UPDATED_DATA_MANAGER
        defaultClinicalStudyShouldBeFound("dataManager.in=" + DEFAULT_DATA_MANAGER + "," + UPDATED_DATA_MANAGER);

        // Get all the clinicalStudyList where dataManager equals to UPDATED_DATA_MANAGER
        defaultClinicalStudyShouldNotBeFound("dataManager.in=" + UPDATED_DATA_MANAGER);
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByDataManagerIsNullOrNotNull() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);

        // Get all the clinicalStudyList where dataManager is not null
        defaultClinicalStudyShouldBeFound("dataManager.specified=true");

        // Get all the clinicalStudyList where dataManager is null
        defaultClinicalStudyShouldNotBeFound("dataManager.specified=false");
    }

    @Test
    @Transactional
    public void getAllClinicalStudiesByMasterFileIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        TrialMasterFile masterFile = TrialMasterFileResourceIT.createEntity(em);
        em.persist(masterFile);
        em.flush();
        clinicalStudy.setMasterFile(masterFile);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long masterFileId = masterFile.getId();

        // Get all the clinicalStudyList where masterFile equals to masterFileId
        defaultClinicalStudyShouldBeFound("masterFileId.equals=" + masterFileId);

        // Get all the clinicalStudyList where masterFile equals to masterFileId + 1
        defaultClinicalStudyShouldNotBeFound("masterFileId.equals=" + (masterFileId + 1));
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByEndPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        StudyEndPoint endPoints = StudyEndPointResourceIT.createEntity(em);
        em.persist(endPoints);
        em.flush();
        clinicalStudy.addEndPoints(endPoints);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long endPointsId = endPoints.getId();

        // Get all the clinicalStudyList where endPoints equals to endPointsId
        defaultClinicalStudyShouldBeFound("endPointsId.equals=" + endPointsId);

        // Get all the clinicalStudyList where endPoints equals to endPointsId + 1
        defaultClinicalStudyShouldNotBeFound("endPointsId.equals=" + (endPointsId + 1));
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByStudiedProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        StudyProduct studiedProducts = StudyProductResourceIT.createEntity(em);
        em.persist(studiedProducts);
        em.flush();
        clinicalStudy.addStudiedProducts(studiedProducts);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long studiedProductsId = studiedProducts.getId();

        // Get all the clinicalStudyList where studiedProducts equals to studiedProductsId
        defaultClinicalStudyShouldBeFound("studiedProductsId.equals=" + studiedProductsId);

        // Get all the clinicalStudyList where studiedProducts equals to studiedProductsId + 1
        defaultClinicalStudyShouldNotBeFound("studiedProductsId.equals=" + (studiedProductsId + 1));
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByMileStonesIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        StudyMilestone mileStones = StudyMilestoneResourceIT.createEntity(em);
        em.persist(mileStones);
        em.flush();
        clinicalStudy.addMileStones(mileStones);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long mileStonesId = mileStones.getId();

        // Get all the clinicalStudyList where mileStones equals to mileStonesId
        defaultClinicalStudyShouldBeFound("mileStonesId.equals=" + mileStonesId);

        // Get all the clinicalStudyList where mileStones equals to mileStonesId + 1
        defaultClinicalStudyShouldNotBeFound("mileStonesId.equals=" + (mileStonesId + 1));
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByBioAnalysesIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        BioAnalysis bioAnalyses = BioAnalysisResourceIT.createEntity(em);
        em.persist(bioAnalyses);
        em.flush();
        clinicalStudy.addBioAnalyses(bioAnalyses);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long bioAnalysesId = bioAnalyses.getId();

        // Get all the clinicalStudyList where bioAnalyses equals to bioAnalysesId
        defaultClinicalStudyShouldBeFound("bioAnalysesId.equals=" + bioAnalysesId);

        // Get all the clinicalStudyList where bioAnalyses equals to bioAnalysesId + 1
        defaultClinicalStudyShouldNotBeFound("bioAnalysesId.equals=" + (bioAnalysesId + 1));
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByDataAnalysesIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        DataAnalysis dataAnalyses = DataAnalysisResourceIT.createEntity(em);
        em.persist(dataAnalyses);
        em.flush();
        clinicalStudy.addDataAnalyses(dataAnalyses);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long dataAnalysesId = dataAnalyses.getId();

        // Get all the clinicalStudyList where dataAnalyses equals to dataAnalysesId
        defaultClinicalStudyShouldBeFound("dataAnalysesId.equals=" + dataAnalysesId);

        // Get all the clinicalStudyList where dataAnalyses equals to dataAnalysesId + 1
        defaultClinicalStudyShouldNotBeFound("dataAnalysesId.equals=" + (dataAnalysesId + 1));
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByShipmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Shipment shipments = ShipmentResourceIT.createEntity(em);
        em.persist(shipments);
        em.flush();
        clinicalStudy.addShipments(shipments);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long shipmentsId = shipments.getId();

        // Get all the clinicalStudyList where shipments equals to shipmentsId
        defaultClinicalStudyShouldBeFound("shipmentsId.equals=" + shipmentsId);

        // Get all the clinicalStudyList where shipments equals to shipmentsId + 1
        defaultClinicalStudyShouldNotBeFound("shipmentsId.equals=" + (shipmentsId + 1));
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByStudySamplesIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        StudySample studySamples = StudySampleResourceIT.createEntity(em);
        em.persist(studySamples);
        em.flush();
        clinicalStudy.addStudySamples(studySamples);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long studySamplesId = studySamples.getId();

        // Get all the clinicalStudyList where studySamples equals to studySamplesId
        defaultClinicalStudyShouldBeFound("studySamplesId.equals=" + studySamplesId);

        // Get all the clinicalStudyList where studySamples equals to studySamplesId + 1
        defaultClinicalStudyShouldNotBeFound("studySamplesId.equals=" + (studySamplesId + 1));
    }


    @Test
    @Transactional
    public void getAllClinicalStudiesByInvestigatorsIsEqualToSomething() throws Exception {
        // Initialize the database
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        PrincipalInvestigator investigators = PrincipalInvestigatorResourceIT.createEntity(em);
        em.persist(investigators);
        em.flush();
        clinicalStudy.addInvestigators(investigators);
        clinicalStudyRepository.saveAndFlush(clinicalStudy);
        Long investigatorsId = investigators.getId();

        // Get all the clinicalStudyList where investigators equals to investigatorsId
        defaultClinicalStudyShouldBeFound("investigatorsId.equals=" + investigatorsId);

        // Get all the clinicalStudyList where investigators equals to investigatorsId + 1
        defaultClinicalStudyShouldNotBeFound("investigatorsId.equals=" + (investigatorsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClinicalStudyShouldBeFound(String filter) throws Exception {
        restClinicalStudyMockMvc.perform(get("/api/clinical-studies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicalStudy.getId().intValue())))
            .andExpect(jsonPath("$.[*].studyIdentifier").value(hasItem(DEFAULT_STUDY_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].phase").value(hasItem(DEFAULT_PHASE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)))
            .andExpect(jsonPath("$.[*].studyYear").value(hasItem(DEFAULT_STUDY_YEAR)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].design").value(hasItem(DEFAULT_DESIGN)))
            .andExpect(jsonPath("$.[*].numberOfCohorts").value(hasItem(DEFAULT_NUMBER_OF_COHORTS)))
            .andExpect(jsonPath("$.[*].intendedSubjectsPerCohort").value(hasItem(DEFAULT_INTENDED_SUBJECTS_PER_COHORT)))
            .andExpect(jsonPath("$.[*].populationDiseaseState").value(hasItem(DEFAULT_POPULATION_DISEASE_STATE)))
            .andExpect(jsonPath("$.[*].minimumAge").value(hasItem(DEFAULT_MINIMUM_AGE)))
            .andExpect(jsonPath("$.[*].maximumAge").value(hasItem(DEFAULT_MAXIMUM_AGE)))
            .andExpect(jsonPath("$.[*].subjectsEnrolled").value(hasItem(DEFAULT_SUBJECTS_ENROLLED)))
            .andExpect(jsonPath("$.[*].femalesEligible").value(hasItem(DEFAULT_FEMALES_ELIGIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].malesEligible").value(hasItem(DEFAULT_MALES_ELIGIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].studyShortName").value(hasItem(DEFAULT_STUDY_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].projectManager").value(hasItem(DEFAULT_PROJECT_MANAGER)))
            .andExpect(jsonPath("$.[*].principalPhysician").value(hasItem(DEFAULT_PRINCIPAL_PHYSICIAN)))
            .andExpect(jsonPath("$.[*].researchRepresentative").value(hasItem(DEFAULT_RESEARCH_REPRESENTATIVE)))
            .andExpect(jsonPath("$.[*].analysisRepresentative").value(hasItem(DEFAULT_ANALYSIS_REPRESENTATIVE)))
            .andExpect(jsonPath("$.[*].dataManager").value(hasItem(DEFAULT_DATA_MANAGER)));

        // Check, that the count call also returns 1
        restClinicalStudyMockMvc.perform(get("/api/clinical-studies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClinicalStudyShouldNotBeFound(String filter) throws Exception {
        restClinicalStudyMockMvc.perform(get("/api/clinical-studies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClinicalStudyMockMvc.perform(get("/api/clinical-studies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingClinicalStudy() throws Exception {
        // Get the clinicalStudy
        restClinicalStudyMockMvc.perform(get("/api/clinical-studies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClinicalStudy() throws Exception {
        // Initialize the database
        clinicalStudyService.save(clinicalStudy);

        int databaseSizeBeforeUpdate = clinicalStudyRepository.findAll().size();

        // Update the clinicalStudy
        ClinicalStudy updatedClinicalStudy = clinicalStudyRepository.findById(clinicalStudy.getId()).get();
        // Disconnect from session so that the updates on updatedClinicalStudy are not directly saved in db
        em.detach(updatedClinicalStudy);
        updatedClinicalStudy
            .studyIdentifier(UPDATED_STUDY_IDENTIFIER)
            .phase(UPDATED_PHASE)
            .status(UPDATED_STATUS)
            .sequence(UPDATED_SEQUENCE)
            .studyYear(UPDATED_STUDY_YEAR)
            .name(UPDATED_NAME)
            .design(UPDATED_DESIGN)
            .numberOfCohorts(UPDATED_NUMBER_OF_COHORTS)
            .intendedSubjectsPerCohort(UPDATED_INTENDED_SUBJECTS_PER_COHORT)
            .populationDiseaseState(UPDATED_POPULATION_DISEASE_STATE)
            .minimumAge(UPDATED_MINIMUM_AGE)
            .maximumAge(UPDATED_MAXIMUM_AGE)
            .subjectsEnrolled(UPDATED_SUBJECTS_ENROLLED)
            .femalesEligible(UPDATED_FEMALES_ELIGIBLE)
            .malesEligible(UPDATED_MALES_ELIGIBLE)
            .studyShortName(UPDATED_STUDY_SHORT_NAME)
            .projectManager(UPDATED_PROJECT_MANAGER)
            .principalPhysician(UPDATED_PRINCIPAL_PHYSICIAN)
            .researchRepresentative(UPDATED_RESEARCH_REPRESENTATIVE)
            .analysisRepresentative(UPDATED_ANALYSIS_REPRESENTATIVE)
            .dataManager(UPDATED_DATA_MANAGER);

        restClinicalStudyMockMvc.perform(put("/api/clinical-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClinicalStudy)))
            .andExpect(status().isOk());

        // Validate the ClinicalStudy in the database
        List<ClinicalStudy> clinicalStudyList = clinicalStudyRepository.findAll();
        assertThat(clinicalStudyList).hasSize(databaseSizeBeforeUpdate);
        ClinicalStudy testClinicalStudy = clinicalStudyList.get(clinicalStudyList.size() - 1);
        assertThat(testClinicalStudy.getStudyIdentifier()).isEqualTo(UPDATED_STUDY_IDENTIFIER);
        assertThat(testClinicalStudy.getPhase()).isEqualTo(UPDATED_PHASE);
        assertThat(testClinicalStudy.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testClinicalStudy.getSequence()).isEqualTo(UPDATED_SEQUENCE);
        assertThat(testClinicalStudy.getStudyYear()).isEqualTo(UPDATED_STUDY_YEAR);
        assertThat(testClinicalStudy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClinicalStudy.getDesign()).isEqualTo(UPDATED_DESIGN);
        assertThat(testClinicalStudy.getNumberOfCohorts()).isEqualTo(UPDATED_NUMBER_OF_COHORTS);
        assertThat(testClinicalStudy.getIntendedSubjectsPerCohort()).isEqualTo(UPDATED_INTENDED_SUBJECTS_PER_COHORT);
        assertThat(testClinicalStudy.getPopulationDiseaseState()).isEqualTo(UPDATED_POPULATION_DISEASE_STATE);
        assertThat(testClinicalStudy.getMinimumAge()).isEqualTo(UPDATED_MINIMUM_AGE);
        assertThat(testClinicalStudy.getMaximumAge()).isEqualTo(UPDATED_MAXIMUM_AGE);
        assertThat(testClinicalStudy.getSubjectsEnrolled()).isEqualTo(UPDATED_SUBJECTS_ENROLLED);
        assertThat(testClinicalStudy.isFemalesEligible()).isEqualTo(UPDATED_FEMALES_ELIGIBLE);
        assertThat(testClinicalStudy.isMalesEligible()).isEqualTo(UPDATED_MALES_ELIGIBLE);
        assertThat(testClinicalStudy.getStudyShortName()).isEqualTo(UPDATED_STUDY_SHORT_NAME);
        assertThat(testClinicalStudy.getProjectManager()).isEqualTo(UPDATED_PROJECT_MANAGER);
        assertThat(testClinicalStudy.getPrincipalPhysician()).isEqualTo(UPDATED_PRINCIPAL_PHYSICIAN);
        assertThat(testClinicalStudy.getResearchRepresentative()).isEqualTo(UPDATED_RESEARCH_REPRESENTATIVE);
        assertThat(testClinicalStudy.getAnalysisRepresentative()).isEqualTo(UPDATED_ANALYSIS_REPRESENTATIVE);
        assertThat(testClinicalStudy.getDataManager()).isEqualTo(UPDATED_DATA_MANAGER);
    }

    @Test
    @Transactional
    public void updateNonExistingClinicalStudy() throws Exception {
        int databaseSizeBeforeUpdate = clinicalStudyRepository.findAll().size();

        // Create the ClinicalStudy

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClinicalStudyMockMvc.perform(put("/api/clinical-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicalStudy)))
            .andExpect(status().isBadRequest());

        // Validate the ClinicalStudy in the database
        List<ClinicalStudy> clinicalStudyList = clinicalStudyRepository.findAll();
        assertThat(clinicalStudyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClinicalStudy() throws Exception {
        // Initialize the database
        clinicalStudyService.save(clinicalStudy);

        int databaseSizeBeforeDelete = clinicalStudyRepository.findAll().size();

        // Delete the clinicalStudy
        restClinicalStudyMockMvc.perform(delete("/api/clinical-studies/{id}", clinicalStudy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClinicalStudy> clinicalStudyList = clinicalStudyRepository.findAll();
        assertThat(clinicalStudyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClinicalStudy.class);
        ClinicalStudy clinicalStudy1 = new ClinicalStudy();
        clinicalStudy1.setId(1L);
        ClinicalStudy clinicalStudy2 = new ClinicalStudy();
        clinicalStudy2.setId(clinicalStudy1.getId());
        assertThat(clinicalStudy1).isEqualTo(clinicalStudy2);
        clinicalStudy2.setId(2L);
        assertThat(clinicalStudy1).isNotEqualTo(clinicalStudy2);
        clinicalStudy1.setId(null);
        assertThat(clinicalStudy1).isNotEqualTo(clinicalStudy2);
    }
}

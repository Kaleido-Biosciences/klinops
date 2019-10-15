/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.DataAnalysis;
import com.kaleido.klinops.domain.BioAnalysis;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.DataAnalysisRepository;
import com.kaleido.klinops.service.DataAnalysisService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.DataAnalysisCriteria;
import com.kaleido.klinops.service.DataAnalysisQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.kaleido.klinops.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DataAnalysisResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class DataAnalysisResourceIT {

    private static final String DEFAULT_DATA_ANALYSES_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_ANALYSES_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ANTICIPATED_ANALYSIS_DELIVERY_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTUAL_ANALYSIS_DELIVERY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DATA_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_DATA_LOCATION = "BBBBBBBBBB";

    @Autowired
    private DataAnalysisRepository dataAnalysisRepository;

    @Mock
    private DataAnalysisRepository dataAnalysisRepositoryMock;

    @Mock
    private DataAnalysisService dataAnalysisServiceMock;

    @Autowired
    private DataAnalysisService dataAnalysisService;

    @Autowired
    private DataAnalysisQueryService dataAnalysisQueryService;

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

    private MockMvc restDataAnalysisMockMvc;

    private DataAnalysis dataAnalysis;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataAnalysisResource dataAnalysisResource = new DataAnalysisResource(dataAnalysisService, dataAnalysisQueryService);
        this.restDataAnalysisMockMvc = MockMvcBuilders.standaloneSetup(dataAnalysisResource)
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
    public static DataAnalysis createEntity(EntityManager em) {
        DataAnalysis dataAnalysis = new DataAnalysis()
            .dataAnalysesType(DEFAULT_DATA_ANALYSES_TYPE)
            .contactName(DEFAULT_CONTACT_NAME)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .anticipatedAnalysisDeliveryDate(DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE)
            .actualAnalysisDeliveryDate(DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE)
            .dataLocation(DEFAULT_DATA_LOCATION);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        dataAnalysis.setStudy(clinicalStudy);
        return dataAnalysis;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataAnalysis createUpdatedEntity(EntityManager em) {
        DataAnalysis dataAnalysis = new DataAnalysis()
            .dataAnalysesType(UPDATED_DATA_ANALYSES_TYPE)
            .contactName(UPDATED_CONTACT_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .anticipatedAnalysisDeliveryDate(UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE)
            .actualAnalysisDeliveryDate(UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE)
            .dataLocation(UPDATED_DATA_LOCATION);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createUpdatedEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        dataAnalysis.setStudy(clinicalStudy);
        return dataAnalysis;
    }

    @BeforeEach
    public void initTest() {
        dataAnalysis = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataAnalysis() throws Exception {
        int databaseSizeBeforeCreate = dataAnalysisRepository.findAll().size();

        // Create the DataAnalysis
        restDataAnalysisMockMvc.perform(post("/api/data-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataAnalysis)))
            .andExpect(status().isCreated());

        // Validate the DataAnalysis in the database
        List<DataAnalysis> dataAnalysisList = dataAnalysisRepository.findAll();
        assertThat(dataAnalysisList).hasSize(databaseSizeBeforeCreate + 1);
        DataAnalysis testDataAnalysis = dataAnalysisList.get(dataAnalysisList.size() - 1);
        assertThat(testDataAnalysis.getDataAnalysesType()).isEqualTo(DEFAULT_DATA_ANALYSES_TYPE);
        assertThat(testDataAnalysis.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDataAnalysis.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testDataAnalysis.getAnticipatedAnalysisDeliveryDate()).isEqualTo(DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE);
        assertThat(testDataAnalysis.getActualAnalysisDeliveryDate()).isEqualTo(DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE);
        assertThat(testDataAnalysis.getDataLocation()).isEqualTo(DEFAULT_DATA_LOCATION);
    }

    @Test
    @Transactional
    public void createDataAnalysisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataAnalysisRepository.findAll().size();

        // Create the DataAnalysis with an existing ID
        dataAnalysis.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataAnalysisMockMvc.perform(post("/api/data-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataAnalysis)))
            .andExpect(status().isBadRequest());

        // Validate the DataAnalysis in the database
        List<DataAnalysis> dataAnalysisList = dataAnalysisRepository.findAll();
        assertThat(dataAnalysisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDataAnalysesTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataAnalysisRepository.findAll().size();
        // set the field null
        dataAnalysis.setDataAnalysesType(null);

        // Create the DataAnalysis, which fails.

        restDataAnalysisMockMvc.perform(post("/api/data-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataAnalysis)))
            .andExpect(status().isBadRequest());

        List<DataAnalysis> dataAnalysisList = dataAnalysisRepository.findAll();
        assertThat(dataAnalysisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDataAnalyses() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList
        restDataAnalysisMockMvc.perform(get("/api/data-analyses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataAnalysesType").value(hasItem(DEFAULT_DATA_ANALYSES_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].anticipatedAnalysisDeliveryDate").value(hasItem(DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualAnalysisDeliveryDate").value(hasItem(DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].dataLocation").value(hasItem(DEFAULT_DATA_LOCATION.toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDataAnalysesWithEagerRelationshipsIsEnabled() throws Exception {
        DataAnalysisResource dataAnalysisResource = new DataAnalysisResource(dataAnalysisServiceMock, dataAnalysisQueryService);
        when(dataAnalysisServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDataAnalysisMockMvc = MockMvcBuilders.standaloneSetup(dataAnalysisResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDataAnalysisMockMvc.perform(get("/api/data-analyses?eagerload=true"))
        .andExpect(status().isOk());

        verify(dataAnalysisServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDataAnalysesWithEagerRelationshipsIsNotEnabled() throws Exception {
        DataAnalysisResource dataAnalysisResource = new DataAnalysisResource(dataAnalysisServiceMock, dataAnalysisQueryService);
            when(dataAnalysisServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDataAnalysisMockMvc = MockMvcBuilders.standaloneSetup(dataAnalysisResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDataAnalysisMockMvc.perform(get("/api/data-analyses?eagerload=true"))
        .andExpect(status().isOk());

            verify(dataAnalysisServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDataAnalysis() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get the dataAnalysis
        restDataAnalysisMockMvc.perform(get("/api/data-analyses/{id}", dataAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataAnalysis.getId().intValue()))
            .andExpect(jsonPath("$.dataAnalysesType").value(DEFAULT_DATA_ANALYSES_TYPE.toString()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL.toString()))
            .andExpect(jsonPath("$.anticipatedAnalysisDeliveryDate").value(DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.actualAnalysisDeliveryDate").value(DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.dataLocation").value(DEFAULT_DATA_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByDataAnalysesTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where dataAnalysesType equals to DEFAULT_DATA_ANALYSES_TYPE
        defaultDataAnalysisShouldBeFound("dataAnalysesType.equals=" + DEFAULT_DATA_ANALYSES_TYPE);

        // Get all the dataAnalysisList where dataAnalysesType equals to UPDATED_DATA_ANALYSES_TYPE
        defaultDataAnalysisShouldNotBeFound("dataAnalysesType.equals=" + UPDATED_DATA_ANALYSES_TYPE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByDataAnalysesTypeIsInShouldWork() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where dataAnalysesType in DEFAULT_DATA_ANALYSES_TYPE or UPDATED_DATA_ANALYSES_TYPE
        defaultDataAnalysisShouldBeFound("dataAnalysesType.in=" + DEFAULT_DATA_ANALYSES_TYPE + "," + UPDATED_DATA_ANALYSES_TYPE);

        // Get all the dataAnalysisList where dataAnalysesType equals to UPDATED_DATA_ANALYSES_TYPE
        defaultDataAnalysisShouldNotBeFound("dataAnalysesType.in=" + UPDATED_DATA_ANALYSES_TYPE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByDataAnalysesTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where dataAnalysesType is not null
        defaultDataAnalysisShouldBeFound("dataAnalysesType.specified=true");

        // Get all the dataAnalysisList where dataAnalysesType is null
        defaultDataAnalysisShouldNotBeFound("dataAnalysesType.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where contactName equals to DEFAULT_CONTACT_NAME
        defaultDataAnalysisShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the dataAnalysisList where contactName equals to UPDATED_CONTACT_NAME
        defaultDataAnalysisShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultDataAnalysisShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the dataAnalysisList where contactName equals to UPDATED_CONTACT_NAME
        defaultDataAnalysisShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where contactName is not null
        defaultDataAnalysisShouldBeFound("contactName.specified=true");

        // Get all the dataAnalysisList where contactName is null
        defaultDataAnalysisShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByContactEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where contactEmail equals to DEFAULT_CONTACT_EMAIL
        defaultDataAnalysisShouldBeFound("contactEmail.equals=" + DEFAULT_CONTACT_EMAIL);

        // Get all the dataAnalysisList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultDataAnalysisShouldNotBeFound("contactEmail.equals=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByContactEmailIsInShouldWork() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where contactEmail in DEFAULT_CONTACT_EMAIL or UPDATED_CONTACT_EMAIL
        defaultDataAnalysisShouldBeFound("contactEmail.in=" + DEFAULT_CONTACT_EMAIL + "," + UPDATED_CONTACT_EMAIL);

        // Get all the dataAnalysisList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultDataAnalysisShouldNotBeFound("contactEmail.in=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByContactEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where contactEmail is not null
        defaultDataAnalysisShouldBeFound("contactEmail.specified=true");

        // Get all the dataAnalysisList where contactEmail is null
        defaultDataAnalysisShouldNotBeFound("contactEmail.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByAnticipatedAnalysisDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate equals to DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("anticipatedAnalysisDeliveryDate.equals=" + DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate equals to UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("anticipatedAnalysisDeliveryDate.equals=" + UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByAnticipatedAnalysisDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate in DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE or UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("anticipatedAnalysisDeliveryDate.in=" + DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE + "," + UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate equals to UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("anticipatedAnalysisDeliveryDate.in=" + UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByAnticipatedAnalysisDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is not null
        defaultDataAnalysisShouldBeFound("anticipatedAnalysisDeliveryDate.specified=true");

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is null
        defaultDataAnalysisShouldNotBeFound("anticipatedAnalysisDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByAnticipatedAnalysisDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is greater than or equal to DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("anticipatedAnalysisDeliveryDate.greaterThanOrEqual=" + DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is greater than or equal to UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("anticipatedAnalysisDeliveryDate.greaterThanOrEqual=" + UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByAnticipatedAnalysisDeliveryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is less than or equal to DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("anticipatedAnalysisDeliveryDate.lessThanOrEqual=" + DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is less than or equal to SMALLER_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("anticipatedAnalysisDeliveryDate.lessThanOrEqual=" + SMALLER_ANTICIPATED_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByAnticipatedAnalysisDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is less than DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("anticipatedAnalysisDeliveryDate.lessThan=" + DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is less than UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("anticipatedAnalysisDeliveryDate.lessThan=" + UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByAnticipatedAnalysisDeliveryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is greater than DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("anticipatedAnalysisDeliveryDate.greaterThan=" + DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where anticipatedAnalysisDeliveryDate is greater than SMALLER_ANTICIPATED_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("anticipatedAnalysisDeliveryDate.greaterThan=" + SMALLER_ANTICIPATED_ANALYSIS_DELIVERY_DATE);
    }


    @Test
    @Transactional
    public void getAllDataAnalysesByActualAnalysisDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate equals to DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("actualAnalysisDeliveryDate.equals=" + DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate equals to UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("actualAnalysisDeliveryDate.equals=" + UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByActualAnalysisDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate in DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE or UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("actualAnalysisDeliveryDate.in=" + DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE + "," + UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate equals to UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("actualAnalysisDeliveryDate.in=" + UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByActualAnalysisDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is not null
        defaultDataAnalysisShouldBeFound("actualAnalysisDeliveryDate.specified=true");

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is null
        defaultDataAnalysisShouldNotBeFound("actualAnalysisDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByActualAnalysisDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is greater than or equal to DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("actualAnalysisDeliveryDate.greaterThanOrEqual=" + DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is greater than or equal to UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("actualAnalysisDeliveryDate.greaterThanOrEqual=" + UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByActualAnalysisDeliveryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is less than or equal to DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("actualAnalysisDeliveryDate.lessThanOrEqual=" + DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is less than or equal to SMALLER_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("actualAnalysisDeliveryDate.lessThanOrEqual=" + SMALLER_ACTUAL_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByActualAnalysisDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is less than DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("actualAnalysisDeliveryDate.lessThan=" + DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is less than UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("actualAnalysisDeliveryDate.lessThan=" + UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByActualAnalysisDeliveryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is greater than DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldNotBeFound("actualAnalysisDeliveryDate.greaterThan=" + DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE);

        // Get all the dataAnalysisList where actualAnalysisDeliveryDate is greater than SMALLER_ACTUAL_ANALYSIS_DELIVERY_DATE
        defaultDataAnalysisShouldBeFound("actualAnalysisDeliveryDate.greaterThan=" + SMALLER_ACTUAL_ANALYSIS_DELIVERY_DATE);
    }


    @Test
    @Transactional
    public void getAllDataAnalysesByDataLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where dataLocation equals to DEFAULT_DATA_LOCATION
        defaultDataAnalysisShouldBeFound("dataLocation.equals=" + DEFAULT_DATA_LOCATION);

        // Get all the dataAnalysisList where dataLocation equals to UPDATED_DATA_LOCATION
        defaultDataAnalysisShouldNotBeFound("dataLocation.equals=" + UPDATED_DATA_LOCATION);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByDataLocationIsInShouldWork() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where dataLocation in DEFAULT_DATA_LOCATION or UPDATED_DATA_LOCATION
        defaultDataAnalysisShouldBeFound("dataLocation.in=" + DEFAULT_DATA_LOCATION + "," + UPDATED_DATA_LOCATION);

        // Get all the dataAnalysisList where dataLocation equals to UPDATED_DATA_LOCATION
        defaultDataAnalysisShouldNotBeFound("dataLocation.in=" + UPDATED_DATA_LOCATION);
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByDataLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);

        // Get all the dataAnalysisList where dataLocation is not null
        defaultDataAnalysisShouldBeFound("dataLocation.specified=true");

        // Get all the dataAnalysisList where dataLocation is null
        defaultDataAnalysisShouldNotBeFound("dataLocation.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataAnalysesByBioAnalysesIsEqualToSomething() throws Exception {
        // Initialize the database
        dataAnalysisRepository.saveAndFlush(dataAnalysis);
        BioAnalysis bioAnalyses = BioAnalysisResourceIT.createEntity(em);
        em.persist(bioAnalyses);
        em.flush();
        dataAnalysis.addBioAnalyses(bioAnalyses);
        dataAnalysisRepository.saveAndFlush(dataAnalysis);
        Long bioAnalysesId = bioAnalyses.getId();

        // Get all the dataAnalysisList where bioAnalyses equals to bioAnalysesId
        defaultDataAnalysisShouldBeFound("bioAnalysesId.equals=" + bioAnalysesId);

        // Get all the dataAnalysisList where bioAnalyses equals to bioAnalysesId + 1
        defaultDataAnalysisShouldNotBeFound("bioAnalysesId.equals=" + (bioAnalysesId + 1));
    }


    @Test
    @Transactional
    public void getAllDataAnalysesByStudyIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClinicalStudy study = dataAnalysis.getStudy();
        dataAnalysisRepository.saveAndFlush(dataAnalysis);
        Long studyId = study.getId();

        // Get all the dataAnalysisList where study equals to studyId
        defaultDataAnalysisShouldBeFound("studyId.equals=" + studyId);

        // Get all the dataAnalysisList where study equals to studyId + 1
        defaultDataAnalysisShouldNotBeFound("studyId.equals=" + (studyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDataAnalysisShouldBeFound(String filter) throws Exception {
        restDataAnalysisMockMvc.perform(get("/api/data-analyses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataAnalysesType").value(hasItem(DEFAULT_DATA_ANALYSES_TYPE)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].anticipatedAnalysisDeliveryDate").value(hasItem(DEFAULT_ANTICIPATED_ANALYSIS_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualAnalysisDeliveryDate").value(hasItem(DEFAULT_ACTUAL_ANALYSIS_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].dataLocation").value(hasItem(DEFAULT_DATA_LOCATION)));

        // Check, that the count call also returns 1
        restDataAnalysisMockMvc.perform(get("/api/data-analyses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDataAnalysisShouldNotBeFound(String filter) throws Exception {
        restDataAnalysisMockMvc.perform(get("/api/data-analyses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDataAnalysisMockMvc.perform(get("/api/data-analyses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDataAnalysis() throws Exception {
        // Get the dataAnalysis
        restDataAnalysisMockMvc.perform(get("/api/data-analyses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataAnalysis() throws Exception {
        // Initialize the database
        dataAnalysisService.save(dataAnalysis);

        int databaseSizeBeforeUpdate = dataAnalysisRepository.findAll().size();

        // Update the dataAnalysis
        DataAnalysis updatedDataAnalysis = dataAnalysisRepository.findById(dataAnalysis.getId()).get();
        // Disconnect from session so that the updates on updatedDataAnalysis are not directly saved in db
        em.detach(updatedDataAnalysis);
        updatedDataAnalysis
            .dataAnalysesType(UPDATED_DATA_ANALYSES_TYPE)
            .contactName(UPDATED_CONTACT_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .anticipatedAnalysisDeliveryDate(UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE)
            .actualAnalysisDeliveryDate(UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE)
            .dataLocation(UPDATED_DATA_LOCATION);

        restDataAnalysisMockMvc.perform(put("/api/data-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataAnalysis)))
            .andExpect(status().isOk());

        // Validate the DataAnalysis in the database
        List<DataAnalysis> dataAnalysisList = dataAnalysisRepository.findAll();
        assertThat(dataAnalysisList).hasSize(databaseSizeBeforeUpdate);
        DataAnalysis testDataAnalysis = dataAnalysisList.get(dataAnalysisList.size() - 1);
        assertThat(testDataAnalysis.getDataAnalysesType()).isEqualTo(UPDATED_DATA_ANALYSES_TYPE);
        assertThat(testDataAnalysis.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDataAnalysis.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testDataAnalysis.getAnticipatedAnalysisDeliveryDate()).isEqualTo(UPDATED_ANTICIPATED_ANALYSIS_DELIVERY_DATE);
        assertThat(testDataAnalysis.getActualAnalysisDeliveryDate()).isEqualTo(UPDATED_ACTUAL_ANALYSIS_DELIVERY_DATE);
        assertThat(testDataAnalysis.getDataLocation()).isEqualTo(UPDATED_DATA_LOCATION);
    }

    @Test
    @Transactional
    public void updateNonExistingDataAnalysis() throws Exception {
        int databaseSizeBeforeUpdate = dataAnalysisRepository.findAll().size();

        // Create the DataAnalysis

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataAnalysisMockMvc.perform(put("/api/data-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataAnalysis)))
            .andExpect(status().isBadRequest());

        // Validate the DataAnalysis in the database
        List<DataAnalysis> dataAnalysisList = dataAnalysisRepository.findAll();
        assertThat(dataAnalysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDataAnalysis() throws Exception {
        // Initialize the database
        dataAnalysisService.save(dataAnalysis);

        int databaseSizeBeforeDelete = dataAnalysisRepository.findAll().size();

        // Delete the dataAnalysis
        restDataAnalysisMockMvc.perform(delete("/api/data-analyses/{id}", dataAnalysis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataAnalysis> dataAnalysisList = dataAnalysisRepository.findAll();
        assertThat(dataAnalysisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataAnalysis.class);
        DataAnalysis dataAnalysis1 = new DataAnalysis();
        dataAnalysis1.setId(1L);
        DataAnalysis dataAnalysis2 = new DataAnalysis();
        dataAnalysis2.setId(dataAnalysis1.getId());
        assertThat(dataAnalysis1).isEqualTo(dataAnalysis2);
        dataAnalysis2.setId(2L);
        assertThat(dataAnalysis1).isNotEqualTo(dataAnalysis2);
        dataAnalysis1.setId(null);
        assertThat(dataAnalysis1).isNotEqualTo(dataAnalysis2);
    }
}

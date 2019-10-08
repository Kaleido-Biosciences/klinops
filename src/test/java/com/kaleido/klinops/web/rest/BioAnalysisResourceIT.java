package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.BioAnalysis;
import com.kaleido.klinops.domain.StudyEndPoint;
import com.kaleido.klinops.domain.Laboratory;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.domain.DataAnalysis;
import com.kaleido.klinops.repository.BioAnalysisRepository;
import com.kaleido.klinops.service.BioAnalysisService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.BioAnalysisCriteria;
import com.kaleido.klinops.service.BioAnalysisQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.List;

import static com.kaleido.klinops.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BioAnalysisResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class BioAnalysisResourceIT {

    private static final String DEFAULT_ANALYTE = "AAAAAAAAAA";
    private static final String UPDATED_ANALYTE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BIO_ANALYSIS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BIO_ANALYSIS_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ANTICIPATED_LAB_WORK_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANTICIPATED_LAB_WORK_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ANTICIPATED_LAB_WORK_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ACTUAL_LAB_WORK_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_LAB_WORK_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTUAL_LAB_WORK_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ANTICIPATED_LAB_RESULT_DELIVERY_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTUAL_LAB_RESULT_DELIVERY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DATA_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_DATA_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private BioAnalysisRepository bioAnalysisRepository;

    @Autowired
    private BioAnalysisService bioAnalysisService;

    @Autowired
    private BioAnalysisQueryService bioAnalysisQueryService;

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

    private MockMvc restBioAnalysisMockMvc;

    private BioAnalysis bioAnalysis;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BioAnalysisResource bioAnalysisResource = new BioAnalysisResource(bioAnalysisService, bioAnalysisQueryService);
        this.restBioAnalysisMockMvc = MockMvcBuilders.standaloneSetup(bioAnalysisResource)
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
    public static BioAnalysis createEntity(EntityManager em) {
        BioAnalysis bioAnalysis = new BioAnalysis()
            .analyte(DEFAULT_ANALYTE)
            .sampleType(DEFAULT_SAMPLE_TYPE)
            .bioAnalysisType(DEFAULT_BIO_ANALYSIS_TYPE)
            .anticipatedLabWorkStartDate(DEFAULT_ANTICIPATED_LAB_WORK_START_DATE)
            .actualLabWorkStartDate(DEFAULT_ACTUAL_LAB_WORK_START_DATE)
            .anticipatedLabResultDeliveryDate(DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE)
            .actualLabResultDeliveryDate(DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE)
            .dataLocation(DEFAULT_DATA_LOCATION)
            .contactName(DEFAULT_CONTACT_NAME)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .comments(DEFAULT_COMMENTS);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        bioAnalysis.setStudy(clinicalStudy);
        return bioAnalysis;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BioAnalysis createUpdatedEntity(EntityManager em) {
        BioAnalysis bioAnalysis = new BioAnalysis()
            .analyte(UPDATED_ANALYTE)
            .sampleType(UPDATED_SAMPLE_TYPE)
            .bioAnalysisType(UPDATED_BIO_ANALYSIS_TYPE)
            .anticipatedLabWorkStartDate(UPDATED_ANTICIPATED_LAB_WORK_START_DATE)
            .actualLabWorkStartDate(UPDATED_ACTUAL_LAB_WORK_START_DATE)
            .anticipatedLabResultDeliveryDate(UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE)
            .actualLabResultDeliveryDate(UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE)
            .dataLocation(UPDATED_DATA_LOCATION)
            .contactName(UPDATED_CONTACT_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .comments(UPDATED_COMMENTS);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createUpdatedEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        bioAnalysis.setStudy(clinicalStudy);
        return bioAnalysis;
    }

    @BeforeEach
    public void initTest() {
        bioAnalysis = createEntity(em);
    }

    @Test
    @Transactional
    public void createBioAnalysis() throws Exception {
        int databaseSizeBeforeCreate = bioAnalysisRepository.findAll().size();

        // Create the BioAnalysis
        restBioAnalysisMockMvc.perform(post("/api/bio-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bioAnalysis)))
            .andExpect(status().isCreated());

        // Validate the BioAnalysis in the database
        List<BioAnalysis> bioAnalysisList = bioAnalysisRepository.findAll();
        assertThat(bioAnalysisList).hasSize(databaseSizeBeforeCreate + 1);
        BioAnalysis testBioAnalysis = bioAnalysisList.get(bioAnalysisList.size() - 1);
        assertThat(testBioAnalysis.getAnalyte()).isEqualTo(DEFAULT_ANALYTE);
        assertThat(testBioAnalysis.getSampleType()).isEqualTo(DEFAULT_SAMPLE_TYPE);
        assertThat(testBioAnalysis.getBioAnalysisType()).isEqualTo(DEFAULT_BIO_ANALYSIS_TYPE);
        assertThat(testBioAnalysis.getAnticipatedLabWorkStartDate()).isEqualTo(DEFAULT_ANTICIPATED_LAB_WORK_START_DATE);
        assertThat(testBioAnalysis.getActualLabWorkStartDate()).isEqualTo(DEFAULT_ACTUAL_LAB_WORK_START_DATE);
        assertThat(testBioAnalysis.getAnticipatedLabResultDeliveryDate()).isEqualTo(DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);
        assertThat(testBioAnalysis.getActualLabResultDeliveryDate()).isEqualTo(DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE);
        assertThat(testBioAnalysis.getDataLocation()).isEqualTo(DEFAULT_DATA_LOCATION);
        assertThat(testBioAnalysis.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testBioAnalysis.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testBioAnalysis.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createBioAnalysisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bioAnalysisRepository.findAll().size();

        // Create the BioAnalysis with an existing ID
        bioAnalysis.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBioAnalysisMockMvc.perform(post("/api/bio-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bioAnalysis)))
            .andExpect(status().isBadRequest());

        // Validate the BioAnalysis in the database
        List<BioAnalysis> bioAnalysisList = bioAnalysisRepository.findAll();
        assertThat(bioAnalysisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAnalyteIsRequired() throws Exception {
        int databaseSizeBeforeTest = bioAnalysisRepository.findAll().size();
        // set the field null
        bioAnalysis.setAnalyte(null);

        // Create the BioAnalysis, which fails.

        restBioAnalysisMockMvc.perform(post("/api/bio-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bioAnalysis)))
            .andExpect(status().isBadRequest());

        List<BioAnalysis> bioAnalysisList = bioAnalysisRepository.findAll();
        assertThat(bioAnalysisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bioAnalysisRepository.findAll().size();
        // set the field null
        bioAnalysis.setSampleType(null);

        // Create the BioAnalysis, which fails.

        restBioAnalysisMockMvc.perform(post("/api/bio-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bioAnalysis)))
            .andExpect(status().isBadRequest());

        List<BioAnalysis> bioAnalysisList = bioAnalysisRepository.findAll();
        assertThat(bioAnalysisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBioAnalysisTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bioAnalysisRepository.findAll().size();
        // set the field null
        bioAnalysis.setBioAnalysisType(null);

        // Create the BioAnalysis, which fails.

        restBioAnalysisMockMvc.perform(post("/api/bio-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bioAnalysis)))
            .andExpect(status().isBadRequest());

        List<BioAnalysis> bioAnalysisList = bioAnalysisRepository.findAll();
        assertThat(bioAnalysisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBioAnalyses() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList
        restBioAnalysisMockMvc.perform(get("/api/bio-analyses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bioAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].analyte").value(hasItem(DEFAULT_ANALYTE.toString())))
            .andExpect(jsonPath("$.[*].sampleType").value(hasItem(DEFAULT_SAMPLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].bioAnalysisType").value(hasItem(DEFAULT_BIO_ANALYSIS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].anticipatedLabWorkStartDate").value(hasItem(DEFAULT_ANTICIPATED_LAB_WORK_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualLabWorkStartDate").value(hasItem(DEFAULT_ACTUAL_LAB_WORK_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].anticipatedLabResultDeliveryDate").value(hasItem(DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualLabResultDeliveryDate").value(hasItem(DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].dataLocation").value(hasItem(DEFAULT_DATA_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }
    
    @Test
    @Transactional
    public void getBioAnalysis() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get the bioAnalysis
        restBioAnalysisMockMvc.perform(get("/api/bio-analyses/{id}", bioAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bioAnalysis.getId().intValue()))
            .andExpect(jsonPath("$.analyte").value(DEFAULT_ANALYTE.toString()))
            .andExpect(jsonPath("$.sampleType").value(DEFAULT_SAMPLE_TYPE.toString()))
            .andExpect(jsonPath("$.bioAnalysisType").value(DEFAULT_BIO_ANALYSIS_TYPE.toString()))
            .andExpect(jsonPath("$.anticipatedLabWorkStartDate").value(DEFAULT_ANTICIPATED_LAB_WORK_START_DATE.toString()))
            .andExpect(jsonPath("$.actualLabWorkStartDate").value(DEFAULT_ACTUAL_LAB_WORK_START_DATE.toString()))
            .andExpect(jsonPath("$.anticipatedLabResultDeliveryDate").value(DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.actualLabResultDeliveryDate").value(DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.dataLocation").value(DEFAULT_DATA_LOCATION.toString()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnalyteIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where analyte equals to DEFAULT_ANALYTE
        defaultBioAnalysisShouldBeFound("analyte.equals=" + DEFAULT_ANALYTE);

        // Get all the bioAnalysisList where analyte equals to UPDATED_ANALYTE
        defaultBioAnalysisShouldNotBeFound("analyte.equals=" + UPDATED_ANALYTE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnalyteIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where analyte in DEFAULT_ANALYTE or UPDATED_ANALYTE
        defaultBioAnalysisShouldBeFound("analyte.in=" + DEFAULT_ANALYTE + "," + UPDATED_ANALYTE);

        // Get all the bioAnalysisList where analyte equals to UPDATED_ANALYTE
        defaultBioAnalysisShouldNotBeFound("analyte.in=" + UPDATED_ANALYTE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnalyteIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where analyte is not null
        defaultBioAnalysisShouldBeFound("analyte.specified=true");

        // Get all the bioAnalysisList where analyte is null
        defaultBioAnalysisShouldNotBeFound("analyte.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesBySampleTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where sampleType equals to DEFAULT_SAMPLE_TYPE
        defaultBioAnalysisShouldBeFound("sampleType.equals=" + DEFAULT_SAMPLE_TYPE);

        // Get all the bioAnalysisList where sampleType equals to UPDATED_SAMPLE_TYPE
        defaultBioAnalysisShouldNotBeFound("sampleType.equals=" + UPDATED_SAMPLE_TYPE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesBySampleTypeIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where sampleType in DEFAULT_SAMPLE_TYPE or UPDATED_SAMPLE_TYPE
        defaultBioAnalysisShouldBeFound("sampleType.in=" + DEFAULT_SAMPLE_TYPE + "," + UPDATED_SAMPLE_TYPE);

        // Get all the bioAnalysisList where sampleType equals to UPDATED_SAMPLE_TYPE
        defaultBioAnalysisShouldNotBeFound("sampleType.in=" + UPDATED_SAMPLE_TYPE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesBySampleTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where sampleType is not null
        defaultBioAnalysisShouldBeFound("sampleType.specified=true");

        // Get all the bioAnalysisList where sampleType is null
        defaultBioAnalysisShouldNotBeFound("sampleType.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByBioAnalysisTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where bioAnalysisType equals to DEFAULT_BIO_ANALYSIS_TYPE
        defaultBioAnalysisShouldBeFound("bioAnalysisType.equals=" + DEFAULT_BIO_ANALYSIS_TYPE);

        // Get all the bioAnalysisList where bioAnalysisType equals to UPDATED_BIO_ANALYSIS_TYPE
        defaultBioAnalysisShouldNotBeFound("bioAnalysisType.equals=" + UPDATED_BIO_ANALYSIS_TYPE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByBioAnalysisTypeIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where bioAnalysisType in DEFAULT_BIO_ANALYSIS_TYPE or UPDATED_BIO_ANALYSIS_TYPE
        defaultBioAnalysisShouldBeFound("bioAnalysisType.in=" + DEFAULT_BIO_ANALYSIS_TYPE + "," + UPDATED_BIO_ANALYSIS_TYPE);

        // Get all the bioAnalysisList where bioAnalysisType equals to UPDATED_BIO_ANALYSIS_TYPE
        defaultBioAnalysisShouldNotBeFound("bioAnalysisType.in=" + UPDATED_BIO_ANALYSIS_TYPE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByBioAnalysisTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where bioAnalysisType is not null
        defaultBioAnalysisShouldBeFound("bioAnalysisType.specified=true");

        // Get all the bioAnalysisList where bioAnalysisType is null
        defaultBioAnalysisShouldNotBeFound("bioAnalysisType.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabWorkStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate equals to DEFAULT_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabWorkStartDate.equals=" + DEFAULT_ANTICIPATED_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate equals to UPDATED_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabWorkStartDate.equals=" + UPDATED_ANTICIPATED_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabWorkStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate in DEFAULT_ANTICIPATED_LAB_WORK_START_DATE or UPDATED_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabWorkStartDate.in=" + DEFAULT_ANTICIPATED_LAB_WORK_START_DATE + "," + UPDATED_ANTICIPATED_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate equals to UPDATED_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabWorkStartDate.in=" + UPDATED_ANTICIPATED_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabWorkStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is not null
        defaultBioAnalysisShouldBeFound("anticipatedLabWorkStartDate.specified=true");

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is null
        defaultBioAnalysisShouldNotBeFound("anticipatedLabWorkStartDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabWorkStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is greater than or equal to DEFAULT_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabWorkStartDate.greaterThanOrEqual=" + DEFAULT_ANTICIPATED_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is greater than or equal to UPDATED_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabWorkStartDate.greaterThanOrEqual=" + UPDATED_ANTICIPATED_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabWorkStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is less than or equal to DEFAULT_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabWorkStartDate.lessThanOrEqual=" + DEFAULT_ANTICIPATED_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is less than or equal to SMALLER_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabWorkStartDate.lessThanOrEqual=" + SMALLER_ANTICIPATED_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabWorkStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is less than DEFAULT_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabWorkStartDate.lessThan=" + DEFAULT_ANTICIPATED_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is less than UPDATED_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabWorkStartDate.lessThan=" + UPDATED_ANTICIPATED_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabWorkStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is greater than DEFAULT_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabWorkStartDate.greaterThan=" + DEFAULT_ANTICIPATED_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where anticipatedLabWorkStartDate is greater than SMALLER_ANTICIPATED_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabWorkStartDate.greaterThan=" + SMALLER_ANTICIPATED_LAB_WORK_START_DATE);
    }


    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabWorkStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabWorkStartDate equals to DEFAULT_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("actualLabWorkStartDate.equals=" + DEFAULT_ACTUAL_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where actualLabWorkStartDate equals to UPDATED_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabWorkStartDate.equals=" + UPDATED_ACTUAL_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabWorkStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabWorkStartDate in DEFAULT_ACTUAL_LAB_WORK_START_DATE or UPDATED_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("actualLabWorkStartDate.in=" + DEFAULT_ACTUAL_LAB_WORK_START_DATE + "," + UPDATED_ACTUAL_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where actualLabWorkStartDate equals to UPDATED_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabWorkStartDate.in=" + UPDATED_ACTUAL_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabWorkStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabWorkStartDate is not null
        defaultBioAnalysisShouldBeFound("actualLabWorkStartDate.specified=true");

        // Get all the bioAnalysisList where actualLabWorkStartDate is null
        defaultBioAnalysisShouldNotBeFound("actualLabWorkStartDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabWorkStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabWorkStartDate is greater than or equal to DEFAULT_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("actualLabWorkStartDate.greaterThanOrEqual=" + DEFAULT_ACTUAL_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where actualLabWorkStartDate is greater than or equal to UPDATED_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabWorkStartDate.greaterThanOrEqual=" + UPDATED_ACTUAL_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabWorkStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabWorkStartDate is less than or equal to DEFAULT_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("actualLabWorkStartDate.lessThanOrEqual=" + DEFAULT_ACTUAL_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where actualLabWorkStartDate is less than or equal to SMALLER_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabWorkStartDate.lessThanOrEqual=" + SMALLER_ACTUAL_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabWorkStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabWorkStartDate is less than DEFAULT_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabWorkStartDate.lessThan=" + DEFAULT_ACTUAL_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where actualLabWorkStartDate is less than UPDATED_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("actualLabWorkStartDate.lessThan=" + UPDATED_ACTUAL_LAB_WORK_START_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabWorkStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabWorkStartDate is greater than DEFAULT_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabWorkStartDate.greaterThan=" + DEFAULT_ACTUAL_LAB_WORK_START_DATE);

        // Get all the bioAnalysisList where actualLabWorkStartDate is greater than SMALLER_ACTUAL_LAB_WORK_START_DATE
        defaultBioAnalysisShouldBeFound("actualLabWorkStartDate.greaterThan=" + SMALLER_ACTUAL_LAB_WORK_START_DATE);
    }


    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabResultDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate equals to DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabResultDeliveryDate.equals=" + DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate equals to UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabResultDeliveryDate.equals=" + UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabResultDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate in DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE or UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabResultDeliveryDate.in=" + DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE + "," + UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate equals to UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabResultDeliveryDate.in=" + UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabResultDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is not null
        defaultBioAnalysisShouldBeFound("anticipatedLabResultDeliveryDate.specified=true");

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is null
        defaultBioAnalysisShouldNotBeFound("anticipatedLabResultDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabResultDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is greater than or equal to DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabResultDeliveryDate.greaterThanOrEqual=" + DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is greater than or equal to UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabResultDeliveryDate.greaterThanOrEqual=" + UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabResultDeliveryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is less than or equal to DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabResultDeliveryDate.lessThanOrEqual=" + DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is less than or equal to SMALLER_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabResultDeliveryDate.lessThanOrEqual=" + SMALLER_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabResultDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is less than DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabResultDeliveryDate.lessThan=" + DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is less than UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabResultDeliveryDate.lessThan=" + UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByAnticipatedLabResultDeliveryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is greater than DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("anticipatedLabResultDeliveryDate.greaterThan=" + DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where anticipatedLabResultDeliveryDate is greater than SMALLER_ANTICIPATED_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("anticipatedLabResultDeliveryDate.greaterThan=" + SMALLER_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);
    }


    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabResultDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate equals to DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("actualLabResultDeliveryDate.equals=" + DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate equals to UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabResultDeliveryDate.equals=" + UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabResultDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate in DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE or UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("actualLabResultDeliveryDate.in=" + DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE + "," + UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate equals to UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabResultDeliveryDate.in=" + UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabResultDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is not null
        defaultBioAnalysisShouldBeFound("actualLabResultDeliveryDate.specified=true");

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is null
        defaultBioAnalysisShouldNotBeFound("actualLabResultDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabResultDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is greater than or equal to DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("actualLabResultDeliveryDate.greaterThanOrEqual=" + DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is greater than or equal to UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabResultDeliveryDate.greaterThanOrEqual=" + UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabResultDeliveryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is less than or equal to DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("actualLabResultDeliveryDate.lessThanOrEqual=" + DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is less than or equal to SMALLER_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabResultDeliveryDate.lessThanOrEqual=" + SMALLER_ACTUAL_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabResultDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is less than DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabResultDeliveryDate.lessThan=" + DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is less than UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("actualLabResultDeliveryDate.lessThan=" + UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByActualLabResultDeliveryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is greater than DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldNotBeFound("actualLabResultDeliveryDate.greaterThan=" + DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE);

        // Get all the bioAnalysisList where actualLabResultDeliveryDate is greater than SMALLER_ACTUAL_LAB_RESULT_DELIVERY_DATE
        defaultBioAnalysisShouldBeFound("actualLabResultDeliveryDate.greaterThan=" + SMALLER_ACTUAL_LAB_RESULT_DELIVERY_DATE);
    }


    @Test
    @Transactional
    public void getAllBioAnalysesByDataLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where dataLocation equals to DEFAULT_DATA_LOCATION
        defaultBioAnalysisShouldBeFound("dataLocation.equals=" + DEFAULT_DATA_LOCATION);

        // Get all the bioAnalysisList where dataLocation equals to UPDATED_DATA_LOCATION
        defaultBioAnalysisShouldNotBeFound("dataLocation.equals=" + UPDATED_DATA_LOCATION);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByDataLocationIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where dataLocation in DEFAULT_DATA_LOCATION or UPDATED_DATA_LOCATION
        defaultBioAnalysisShouldBeFound("dataLocation.in=" + DEFAULT_DATA_LOCATION + "," + UPDATED_DATA_LOCATION);

        // Get all the bioAnalysisList where dataLocation equals to UPDATED_DATA_LOCATION
        defaultBioAnalysisShouldNotBeFound("dataLocation.in=" + UPDATED_DATA_LOCATION);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByDataLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where dataLocation is not null
        defaultBioAnalysisShouldBeFound("dataLocation.specified=true");

        // Get all the bioAnalysisList where dataLocation is null
        defaultBioAnalysisShouldNotBeFound("dataLocation.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where contactName equals to DEFAULT_CONTACT_NAME
        defaultBioAnalysisShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the bioAnalysisList where contactName equals to UPDATED_CONTACT_NAME
        defaultBioAnalysisShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultBioAnalysisShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the bioAnalysisList where contactName equals to UPDATED_CONTACT_NAME
        defaultBioAnalysisShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where contactName is not null
        defaultBioAnalysisShouldBeFound("contactName.specified=true");

        // Get all the bioAnalysisList where contactName is null
        defaultBioAnalysisShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByContactEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where contactEmail equals to DEFAULT_CONTACT_EMAIL
        defaultBioAnalysisShouldBeFound("contactEmail.equals=" + DEFAULT_CONTACT_EMAIL);

        // Get all the bioAnalysisList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultBioAnalysisShouldNotBeFound("contactEmail.equals=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByContactEmailIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where contactEmail in DEFAULT_CONTACT_EMAIL or UPDATED_CONTACT_EMAIL
        defaultBioAnalysisShouldBeFound("contactEmail.in=" + DEFAULT_CONTACT_EMAIL + "," + UPDATED_CONTACT_EMAIL);

        // Get all the bioAnalysisList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultBioAnalysisShouldNotBeFound("contactEmail.in=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByContactEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where contactEmail is not null
        defaultBioAnalysisShouldBeFound("contactEmail.specified=true");

        // Get all the bioAnalysisList where contactEmail is null
        defaultBioAnalysisShouldNotBeFound("contactEmail.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where comments equals to DEFAULT_COMMENTS
        defaultBioAnalysisShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the bioAnalysisList where comments equals to UPDATED_COMMENTS
        defaultBioAnalysisShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultBioAnalysisShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the bioAnalysisList where comments equals to UPDATED_COMMENTS
        defaultBioAnalysisShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);

        // Get all the bioAnalysisList where comments is not null
        defaultBioAnalysisShouldBeFound("comments.specified=true");

        // Get all the bioAnalysisList where comments is null
        defaultBioAnalysisShouldNotBeFound("comments.specified=false");
    }

    @Test
    @Transactional
    public void getAllBioAnalysesByStudyEndPointIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);
        StudyEndPoint studyEndPoint = StudyEndPointResourceIT.createEntity(em);
        em.persist(studyEndPoint);
        em.flush();
        bioAnalysis.setStudyEndPoint(studyEndPoint);
        bioAnalysisRepository.saveAndFlush(bioAnalysis);
        Long studyEndPointId = studyEndPoint.getId();

        // Get all the bioAnalysisList where studyEndPoint equals to studyEndPointId
        defaultBioAnalysisShouldBeFound("studyEndPointId.equals=" + studyEndPointId);

        // Get all the bioAnalysisList where studyEndPoint equals to studyEndPointId + 1
        defaultBioAnalysisShouldNotBeFound("studyEndPointId.equals=" + (studyEndPointId + 1));
    }


    @Test
    @Transactional
    public void getAllBioAnalysesByLaboratoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);
        Laboratory laboratories = LaboratoryResourceIT.createEntity(em);
        em.persist(laboratories);
        em.flush();
        bioAnalysis.setLaboratories(laboratories);
        bioAnalysisRepository.saveAndFlush(bioAnalysis);
        Long laboratoriesId = laboratories.getId();

        // Get all the bioAnalysisList where laboratories equals to laboratoriesId
        defaultBioAnalysisShouldBeFound("laboratoriesId.equals=" + laboratoriesId);

        // Get all the bioAnalysisList where laboratories equals to laboratoriesId + 1
        defaultBioAnalysisShouldNotBeFound("laboratoriesId.equals=" + (laboratoriesId + 1));
    }


    @Test
    @Transactional
    public void getAllBioAnalysesByStudyIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClinicalStudy study = bioAnalysis.getStudy();
        bioAnalysisRepository.saveAndFlush(bioAnalysis);
        Long studyId = study.getId();

        // Get all the bioAnalysisList where study equals to studyId
        defaultBioAnalysisShouldBeFound("studyId.equals=" + studyId);

        // Get all the bioAnalysisList where study equals to studyId + 1
        defaultBioAnalysisShouldNotBeFound("studyId.equals=" + (studyId + 1));
    }


    @Test
    @Transactional
    public void getAllBioAnalysesByDataAnalysesIsEqualToSomething() throws Exception {
        // Initialize the database
        bioAnalysisRepository.saveAndFlush(bioAnalysis);
        DataAnalysis dataAnalyses = DataAnalysisResourceIT.createEntity(em);
        em.persist(dataAnalyses);
        em.flush();
        bioAnalysis.addDataAnalyses(dataAnalyses);
        bioAnalysisRepository.saveAndFlush(bioAnalysis);
        Long dataAnalysesId = dataAnalyses.getId();

        // Get all the bioAnalysisList where dataAnalyses equals to dataAnalysesId
        defaultBioAnalysisShouldBeFound("dataAnalysesId.equals=" + dataAnalysesId);

        // Get all the bioAnalysisList where dataAnalyses equals to dataAnalysesId + 1
        defaultBioAnalysisShouldNotBeFound("dataAnalysesId.equals=" + (dataAnalysesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBioAnalysisShouldBeFound(String filter) throws Exception {
        restBioAnalysisMockMvc.perform(get("/api/bio-analyses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bioAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].analyte").value(hasItem(DEFAULT_ANALYTE)))
            .andExpect(jsonPath("$.[*].sampleType").value(hasItem(DEFAULT_SAMPLE_TYPE)))
            .andExpect(jsonPath("$.[*].bioAnalysisType").value(hasItem(DEFAULT_BIO_ANALYSIS_TYPE)))
            .andExpect(jsonPath("$.[*].anticipatedLabWorkStartDate").value(hasItem(DEFAULT_ANTICIPATED_LAB_WORK_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualLabWorkStartDate").value(hasItem(DEFAULT_ACTUAL_LAB_WORK_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].anticipatedLabResultDeliveryDate").value(hasItem(DEFAULT_ANTICIPATED_LAB_RESULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualLabResultDeliveryDate").value(hasItem(DEFAULT_ACTUAL_LAB_RESULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].dataLocation").value(hasItem(DEFAULT_DATA_LOCATION)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restBioAnalysisMockMvc.perform(get("/api/bio-analyses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBioAnalysisShouldNotBeFound(String filter) throws Exception {
        restBioAnalysisMockMvc.perform(get("/api/bio-analyses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBioAnalysisMockMvc.perform(get("/api/bio-analyses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBioAnalysis() throws Exception {
        // Get the bioAnalysis
        restBioAnalysisMockMvc.perform(get("/api/bio-analyses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBioAnalysis() throws Exception {
        // Initialize the database
        bioAnalysisService.save(bioAnalysis);

        int databaseSizeBeforeUpdate = bioAnalysisRepository.findAll().size();

        // Update the bioAnalysis
        BioAnalysis updatedBioAnalysis = bioAnalysisRepository.findById(bioAnalysis.getId()).get();
        // Disconnect from session so that the updates on updatedBioAnalysis are not directly saved in db
        em.detach(updatedBioAnalysis);
        updatedBioAnalysis
            .analyte(UPDATED_ANALYTE)
            .sampleType(UPDATED_SAMPLE_TYPE)
            .bioAnalysisType(UPDATED_BIO_ANALYSIS_TYPE)
            .anticipatedLabWorkStartDate(UPDATED_ANTICIPATED_LAB_WORK_START_DATE)
            .actualLabWorkStartDate(UPDATED_ACTUAL_LAB_WORK_START_DATE)
            .anticipatedLabResultDeliveryDate(UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE)
            .actualLabResultDeliveryDate(UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE)
            .dataLocation(UPDATED_DATA_LOCATION)
            .contactName(UPDATED_CONTACT_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .comments(UPDATED_COMMENTS);

        restBioAnalysisMockMvc.perform(put("/api/bio-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBioAnalysis)))
            .andExpect(status().isOk());

        // Validate the BioAnalysis in the database
        List<BioAnalysis> bioAnalysisList = bioAnalysisRepository.findAll();
        assertThat(bioAnalysisList).hasSize(databaseSizeBeforeUpdate);
        BioAnalysis testBioAnalysis = bioAnalysisList.get(bioAnalysisList.size() - 1);
        assertThat(testBioAnalysis.getAnalyte()).isEqualTo(UPDATED_ANALYTE);
        assertThat(testBioAnalysis.getSampleType()).isEqualTo(UPDATED_SAMPLE_TYPE);
        assertThat(testBioAnalysis.getBioAnalysisType()).isEqualTo(UPDATED_BIO_ANALYSIS_TYPE);
        assertThat(testBioAnalysis.getAnticipatedLabWorkStartDate()).isEqualTo(UPDATED_ANTICIPATED_LAB_WORK_START_DATE);
        assertThat(testBioAnalysis.getActualLabWorkStartDate()).isEqualTo(UPDATED_ACTUAL_LAB_WORK_START_DATE);
        assertThat(testBioAnalysis.getAnticipatedLabResultDeliveryDate()).isEqualTo(UPDATED_ANTICIPATED_LAB_RESULT_DELIVERY_DATE);
        assertThat(testBioAnalysis.getActualLabResultDeliveryDate()).isEqualTo(UPDATED_ACTUAL_LAB_RESULT_DELIVERY_DATE);
        assertThat(testBioAnalysis.getDataLocation()).isEqualTo(UPDATED_DATA_LOCATION);
        assertThat(testBioAnalysis.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testBioAnalysis.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testBioAnalysis.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingBioAnalysis() throws Exception {
        int databaseSizeBeforeUpdate = bioAnalysisRepository.findAll().size();

        // Create the BioAnalysis

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBioAnalysisMockMvc.perform(put("/api/bio-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bioAnalysis)))
            .andExpect(status().isBadRequest());

        // Validate the BioAnalysis in the database
        List<BioAnalysis> bioAnalysisList = bioAnalysisRepository.findAll();
        assertThat(bioAnalysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBioAnalysis() throws Exception {
        // Initialize the database
        bioAnalysisService.save(bioAnalysis);

        int databaseSizeBeforeDelete = bioAnalysisRepository.findAll().size();

        // Delete the bioAnalysis
        restBioAnalysisMockMvc.perform(delete("/api/bio-analyses/{id}", bioAnalysis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BioAnalysis> bioAnalysisList = bioAnalysisRepository.findAll();
        assertThat(bioAnalysisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BioAnalysis.class);
        BioAnalysis bioAnalysis1 = new BioAnalysis();
        bioAnalysis1.setId(1L);
        BioAnalysis bioAnalysis2 = new BioAnalysis();
        bioAnalysis2.setId(bioAnalysis1.getId());
        assertThat(bioAnalysis1).isEqualTo(bioAnalysis2);
        bioAnalysis2.setId(2L);
        assertThat(bioAnalysis1).isNotEqualTo(bioAnalysis2);
        bioAnalysis1.setId(null);
        assertThat(bioAnalysis1).isNotEqualTo(bioAnalysis2);
    }
}

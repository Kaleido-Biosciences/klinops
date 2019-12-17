package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.StudyMilestone;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.StudyMilestoneRepository;
import com.kaleido.klinops.repository.search.StudyMilestoneSearchRepository;
import com.kaleido.klinops.service.StudyMilestoneService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.StudyMilestoneCriteria;
import com.kaleido.klinops.service.StudyMilestoneQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.Collections;
import java.util.List;

import static com.kaleido.klinops.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StudyMilestoneResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class StudyMilestoneResourceIT {

    private static final String DEFAULT_MILE_STONE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MILE_STONE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MILE_STONE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MILE_STONE_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PROJECTED_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROJECTED_COMPLETION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PROJECTED_COMPLETION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ACTUAL_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_COMPLETION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTUAL_COMPLETION_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private StudyMilestoneRepository studyMilestoneRepository;

    @Autowired
    private StudyMilestoneService studyMilestoneService;

    /**
     * This repository is mocked in the com.kaleido.klinops.repository.search test package.
     *
     * @see com.kaleido.klinops.repository.search.StudyMilestoneSearchRepositoryMockConfiguration
     */
    @Autowired
    private StudyMilestoneSearchRepository mockStudyMilestoneSearchRepository;

    @Autowired
    private StudyMilestoneQueryService studyMilestoneQueryService;

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

    private MockMvc restStudyMilestoneMockMvc;

    private StudyMilestone studyMilestone;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudyMilestoneResource studyMilestoneResource = new StudyMilestoneResource(studyMilestoneService, studyMilestoneQueryService);
        this.restStudyMilestoneMockMvc = MockMvcBuilders.standaloneSetup(studyMilestoneResource)
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
    public static StudyMilestone createEntity(EntityManager em) {
        StudyMilestone studyMilestone = new StudyMilestone()
            .mileStoneName(DEFAULT_MILE_STONE_NAME)
            .mileStoneType(DEFAULT_MILE_STONE_TYPE)
            .projectedCompletionDate(DEFAULT_PROJECTED_COMPLETION_DATE)
            .actualCompletionDate(DEFAULT_ACTUAL_COMPLETION_DATE);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        studyMilestone.setStudy(clinicalStudy);
        return studyMilestone;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyMilestone createUpdatedEntity(EntityManager em) {
        StudyMilestone studyMilestone = new StudyMilestone()
            .mileStoneName(UPDATED_MILE_STONE_NAME)
            .mileStoneType(UPDATED_MILE_STONE_TYPE)
            .projectedCompletionDate(UPDATED_PROJECTED_COMPLETION_DATE)
            .actualCompletionDate(UPDATED_ACTUAL_COMPLETION_DATE);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createUpdatedEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        studyMilestone.setStudy(clinicalStudy);
        return studyMilestone;
    }

    @BeforeEach
    public void initTest() {
        studyMilestone = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudyMilestone() throws Exception {
        int databaseSizeBeforeCreate = studyMilestoneRepository.findAll().size();

        // Create the StudyMilestone
        restStudyMilestoneMockMvc.perform(post("/api/study-milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyMilestone)))
            .andExpect(status().isCreated());

        // Validate the StudyMilestone in the database
        List<StudyMilestone> studyMilestoneList = studyMilestoneRepository.findAll();
        assertThat(studyMilestoneList).hasSize(databaseSizeBeforeCreate + 1);
        StudyMilestone testStudyMilestone = studyMilestoneList.get(studyMilestoneList.size() - 1);
        assertThat(testStudyMilestone.getMileStoneName()).isEqualTo(DEFAULT_MILE_STONE_NAME);
        assertThat(testStudyMilestone.getMileStoneType()).isEqualTo(DEFAULT_MILE_STONE_TYPE);
        assertThat(testStudyMilestone.getProjectedCompletionDate()).isEqualTo(DEFAULT_PROJECTED_COMPLETION_DATE);
        assertThat(testStudyMilestone.getActualCompletionDate()).isEqualTo(DEFAULT_ACTUAL_COMPLETION_DATE);

        // Validate the StudyMilestone in Elasticsearch
        verify(mockStudyMilestoneSearchRepository, times(1)).save(testStudyMilestone);
    }

    @Test
    @Transactional
    public void createStudyMilestoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studyMilestoneRepository.findAll().size();

        // Create the StudyMilestone with an existing ID
        studyMilestone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyMilestoneMockMvc.perform(post("/api/study-milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyMilestone)))
            .andExpect(status().isBadRequest());

        // Validate the StudyMilestone in the database
        List<StudyMilestone> studyMilestoneList = studyMilestoneRepository.findAll();
        assertThat(studyMilestoneList).hasSize(databaseSizeBeforeCreate);

        // Validate the StudyMilestone in Elasticsearch
        verify(mockStudyMilestoneSearchRepository, times(0)).save(studyMilestone);
    }


    @Test
    @Transactional
    public void checkMileStoneNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyMilestoneRepository.findAll().size();
        // set the field null
        studyMilestone.setMileStoneName(null);

        // Create the StudyMilestone, which fails.

        restStudyMilestoneMockMvc.perform(post("/api/study-milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyMilestone)))
            .andExpect(status().isBadRequest());

        List<StudyMilestone> studyMilestoneList = studyMilestoneRepository.findAll();
        assertThat(studyMilestoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMileStoneTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyMilestoneRepository.findAll().size();
        // set the field null
        studyMilestone.setMileStoneType(null);

        // Create the StudyMilestone, which fails.

        restStudyMilestoneMockMvc.perform(post("/api/study-milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyMilestone)))
            .andExpect(status().isBadRequest());

        List<StudyMilestone> studyMilestoneList = studyMilestoneRepository.findAll();
        assertThat(studyMilestoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudyMilestones() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList
        restStudyMilestoneMockMvc.perform(get("/api/study-milestones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyMilestone.getId().intValue())))
            .andExpect(jsonPath("$.[*].mileStoneName").value(hasItem(DEFAULT_MILE_STONE_NAME.toString())))
            .andExpect(jsonPath("$.[*].mileStoneType").value(hasItem(DEFAULT_MILE_STONE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].projectedCompletionDate").value(hasItem(DEFAULT_PROJECTED_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualCompletionDate").value(hasItem(DEFAULT_ACTUAL_COMPLETION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getStudyMilestone() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get the studyMilestone
        restStudyMilestoneMockMvc.perform(get("/api/study-milestones/{id}", studyMilestone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studyMilestone.getId().intValue()))
            .andExpect(jsonPath("$.mileStoneName").value(DEFAULT_MILE_STONE_NAME.toString()))
            .andExpect(jsonPath("$.mileStoneType").value(DEFAULT_MILE_STONE_TYPE.toString()))
            .andExpect(jsonPath("$.projectedCompletionDate").value(DEFAULT_PROJECTED_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.actualCompletionDate").value(DEFAULT_ACTUAL_COMPLETION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByMileStoneNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where mileStoneName equals to DEFAULT_MILE_STONE_NAME
        defaultStudyMilestoneShouldBeFound("mileStoneName.equals=" + DEFAULT_MILE_STONE_NAME);

        // Get all the studyMilestoneList where mileStoneName equals to UPDATED_MILE_STONE_NAME
        defaultStudyMilestoneShouldNotBeFound("mileStoneName.equals=" + UPDATED_MILE_STONE_NAME);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByMileStoneNameIsInShouldWork() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where mileStoneName in DEFAULT_MILE_STONE_NAME or UPDATED_MILE_STONE_NAME
        defaultStudyMilestoneShouldBeFound("mileStoneName.in=" + DEFAULT_MILE_STONE_NAME + "," + UPDATED_MILE_STONE_NAME);

        // Get all the studyMilestoneList where mileStoneName equals to UPDATED_MILE_STONE_NAME
        defaultStudyMilestoneShouldNotBeFound("mileStoneName.in=" + UPDATED_MILE_STONE_NAME);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByMileStoneNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where mileStoneName is not null
        defaultStudyMilestoneShouldBeFound("mileStoneName.specified=true");

        // Get all the studyMilestoneList where mileStoneName is null
        defaultStudyMilestoneShouldNotBeFound("mileStoneName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByMileStoneTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where mileStoneType equals to DEFAULT_MILE_STONE_TYPE
        defaultStudyMilestoneShouldBeFound("mileStoneType.equals=" + DEFAULT_MILE_STONE_TYPE);

        // Get all the studyMilestoneList where mileStoneType equals to UPDATED_MILE_STONE_TYPE
        defaultStudyMilestoneShouldNotBeFound("mileStoneType.equals=" + UPDATED_MILE_STONE_TYPE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByMileStoneTypeIsInShouldWork() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where mileStoneType in DEFAULT_MILE_STONE_TYPE or UPDATED_MILE_STONE_TYPE
        defaultStudyMilestoneShouldBeFound("mileStoneType.in=" + DEFAULT_MILE_STONE_TYPE + "," + UPDATED_MILE_STONE_TYPE);

        // Get all the studyMilestoneList where mileStoneType equals to UPDATED_MILE_STONE_TYPE
        defaultStudyMilestoneShouldNotBeFound("mileStoneType.in=" + UPDATED_MILE_STONE_TYPE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByMileStoneTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where mileStoneType is not null
        defaultStudyMilestoneShouldBeFound("mileStoneType.specified=true");

        // Get all the studyMilestoneList where mileStoneType is null
        defaultStudyMilestoneShouldNotBeFound("mileStoneType.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByProjectedCompletionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where projectedCompletionDate equals to DEFAULT_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("projectedCompletionDate.equals=" + DEFAULT_PROJECTED_COMPLETION_DATE);

        // Get all the studyMilestoneList where projectedCompletionDate equals to UPDATED_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("projectedCompletionDate.equals=" + UPDATED_PROJECTED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByProjectedCompletionDateIsInShouldWork() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where projectedCompletionDate in DEFAULT_PROJECTED_COMPLETION_DATE or UPDATED_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("projectedCompletionDate.in=" + DEFAULT_PROJECTED_COMPLETION_DATE + "," + UPDATED_PROJECTED_COMPLETION_DATE);

        // Get all the studyMilestoneList where projectedCompletionDate equals to UPDATED_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("projectedCompletionDate.in=" + UPDATED_PROJECTED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByProjectedCompletionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where projectedCompletionDate is not null
        defaultStudyMilestoneShouldBeFound("projectedCompletionDate.specified=true");

        // Get all the studyMilestoneList where projectedCompletionDate is null
        defaultStudyMilestoneShouldNotBeFound("projectedCompletionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByProjectedCompletionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where projectedCompletionDate is greater than or equal to DEFAULT_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("projectedCompletionDate.greaterThanOrEqual=" + DEFAULT_PROJECTED_COMPLETION_DATE);

        // Get all the studyMilestoneList where projectedCompletionDate is greater than or equal to UPDATED_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("projectedCompletionDate.greaterThanOrEqual=" + UPDATED_PROJECTED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByProjectedCompletionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where projectedCompletionDate is less than or equal to DEFAULT_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("projectedCompletionDate.lessThanOrEqual=" + DEFAULT_PROJECTED_COMPLETION_DATE);

        // Get all the studyMilestoneList where projectedCompletionDate is less than or equal to SMALLER_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("projectedCompletionDate.lessThanOrEqual=" + SMALLER_PROJECTED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByProjectedCompletionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where projectedCompletionDate is less than DEFAULT_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("projectedCompletionDate.lessThan=" + DEFAULT_PROJECTED_COMPLETION_DATE);

        // Get all the studyMilestoneList where projectedCompletionDate is less than UPDATED_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("projectedCompletionDate.lessThan=" + UPDATED_PROJECTED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByProjectedCompletionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where projectedCompletionDate is greater than DEFAULT_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("projectedCompletionDate.greaterThan=" + DEFAULT_PROJECTED_COMPLETION_DATE);

        // Get all the studyMilestoneList where projectedCompletionDate is greater than SMALLER_PROJECTED_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("projectedCompletionDate.greaterThan=" + SMALLER_PROJECTED_COMPLETION_DATE);
    }


    @Test
    @Transactional
    public void getAllStudyMilestonesByActualCompletionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where actualCompletionDate equals to DEFAULT_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("actualCompletionDate.equals=" + DEFAULT_ACTUAL_COMPLETION_DATE);

        // Get all the studyMilestoneList where actualCompletionDate equals to UPDATED_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("actualCompletionDate.equals=" + UPDATED_ACTUAL_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByActualCompletionDateIsInShouldWork() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where actualCompletionDate in DEFAULT_ACTUAL_COMPLETION_DATE or UPDATED_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("actualCompletionDate.in=" + DEFAULT_ACTUAL_COMPLETION_DATE + "," + UPDATED_ACTUAL_COMPLETION_DATE);

        // Get all the studyMilestoneList where actualCompletionDate equals to UPDATED_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("actualCompletionDate.in=" + UPDATED_ACTUAL_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByActualCompletionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where actualCompletionDate is not null
        defaultStudyMilestoneShouldBeFound("actualCompletionDate.specified=true");

        // Get all the studyMilestoneList where actualCompletionDate is null
        defaultStudyMilestoneShouldNotBeFound("actualCompletionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByActualCompletionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where actualCompletionDate is greater than or equal to DEFAULT_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("actualCompletionDate.greaterThanOrEqual=" + DEFAULT_ACTUAL_COMPLETION_DATE);

        // Get all the studyMilestoneList where actualCompletionDate is greater than or equal to UPDATED_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("actualCompletionDate.greaterThanOrEqual=" + UPDATED_ACTUAL_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByActualCompletionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where actualCompletionDate is less than or equal to DEFAULT_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("actualCompletionDate.lessThanOrEqual=" + DEFAULT_ACTUAL_COMPLETION_DATE);

        // Get all the studyMilestoneList where actualCompletionDate is less than or equal to SMALLER_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("actualCompletionDate.lessThanOrEqual=" + SMALLER_ACTUAL_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByActualCompletionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where actualCompletionDate is less than DEFAULT_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("actualCompletionDate.lessThan=" + DEFAULT_ACTUAL_COMPLETION_DATE);

        // Get all the studyMilestoneList where actualCompletionDate is less than UPDATED_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("actualCompletionDate.lessThan=" + UPDATED_ACTUAL_COMPLETION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudyMilestonesByActualCompletionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studyMilestoneRepository.saveAndFlush(studyMilestone);

        // Get all the studyMilestoneList where actualCompletionDate is greater than DEFAULT_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldNotBeFound("actualCompletionDate.greaterThan=" + DEFAULT_ACTUAL_COMPLETION_DATE);

        // Get all the studyMilestoneList where actualCompletionDate is greater than SMALLER_ACTUAL_COMPLETION_DATE
        defaultStudyMilestoneShouldBeFound("actualCompletionDate.greaterThan=" + SMALLER_ACTUAL_COMPLETION_DATE);
    }


    @Test
    @Transactional
    public void getAllStudyMilestonesByStudyIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClinicalStudy study = studyMilestone.getStudy();
        studyMilestoneRepository.saveAndFlush(studyMilestone);
        Long studyId = study.getId();

        // Get all the studyMilestoneList where study equals to studyId
        defaultStudyMilestoneShouldBeFound("studyId.equals=" + studyId);

        // Get all the studyMilestoneList where study equals to studyId + 1
        defaultStudyMilestoneShouldNotBeFound("studyId.equals=" + (studyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudyMilestoneShouldBeFound(String filter) throws Exception {
        restStudyMilestoneMockMvc.perform(get("/api/study-milestones?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyMilestone.getId().intValue())))
            .andExpect(jsonPath("$.[*].mileStoneName").value(hasItem(DEFAULT_MILE_STONE_NAME)))
            .andExpect(jsonPath("$.[*].mileStoneType").value(hasItem(DEFAULT_MILE_STONE_TYPE)))
            .andExpect(jsonPath("$.[*].projectedCompletionDate").value(hasItem(DEFAULT_PROJECTED_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualCompletionDate").value(hasItem(DEFAULT_ACTUAL_COMPLETION_DATE.toString())));

        // Check, that the count call also returns 1
        restStudyMilestoneMockMvc.perform(get("/api/study-milestones/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudyMilestoneShouldNotBeFound(String filter) throws Exception {
        restStudyMilestoneMockMvc.perform(get("/api/study-milestones?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudyMilestoneMockMvc.perform(get("/api/study-milestones/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStudyMilestone() throws Exception {
        // Get the studyMilestone
        restStudyMilestoneMockMvc.perform(get("/api/study-milestones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudyMilestone() throws Exception {
        // Initialize the database
        studyMilestoneService.save(studyMilestone);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockStudyMilestoneSearchRepository);

        int databaseSizeBeforeUpdate = studyMilestoneRepository.findAll().size();

        // Update the studyMilestone
        StudyMilestone updatedStudyMilestone = studyMilestoneRepository.findById(studyMilestone.getId()).get();
        // Disconnect from session so that the updates on updatedStudyMilestone are not directly saved in db
        em.detach(updatedStudyMilestone);
        updatedStudyMilestone
            .mileStoneName(UPDATED_MILE_STONE_NAME)
            .mileStoneType(UPDATED_MILE_STONE_TYPE)
            .projectedCompletionDate(UPDATED_PROJECTED_COMPLETION_DATE)
            .actualCompletionDate(UPDATED_ACTUAL_COMPLETION_DATE);

        restStudyMilestoneMockMvc.perform(put("/api/study-milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudyMilestone)))
            .andExpect(status().isOk());

        // Validate the StudyMilestone in the database
        List<StudyMilestone> studyMilestoneList = studyMilestoneRepository.findAll();
        assertThat(studyMilestoneList).hasSize(databaseSizeBeforeUpdate);
        StudyMilestone testStudyMilestone = studyMilestoneList.get(studyMilestoneList.size() - 1);
        assertThat(testStudyMilestone.getMileStoneName()).isEqualTo(UPDATED_MILE_STONE_NAME);
        assertThat(testStudyMilestone.getMileStoneType()).isEqualTo(UPDATED_MILE_STONE_TYPE);
        assertThat(testStudyMilestone.getProjectedCompletionDate()).isEqualTo(UPDATED_PROJECTED_COMPLETION_DATE);
        assertThat(testStudyMilestone.getActualCompletionDate()).isEqualTo(UPDATED_ACTUAL_COMPLETION_DATE);

        // Validate the StudyMilestone in Elasticsearch
        verify(mockStudyMilestoneSearchRepository, times(1)).save(testStudyMilestone);
    }

    @Test
    @Transactional
    public void updateNonExistingStudyMilestone() throws Exception {
        int databaseSizeBeforeUpdate = studyMilestoneRepository.findAll().size();

        // Create the StudyMilestone

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyMilestoneMockMvc.perform(put("/api/study-milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyMilestone)))
            .andExpect(status().isBadRequest());

        // Validate the StudyMilestone in the database
        List<StudyMilestone> studyMilestoneList = studyMilestoneRepository.findAll();
        assertThat(studyMilestoneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StudyMilestone in Elasticsearch
        verify(mockStudyMilestoneSearchRepository, times(0)).save(studyMilestone);
    }

    @Test
    @Transactional
    public void deleteStudyMilestone() throws Exception {
        // Initialize the database
        studyMilestoneService.save(studyMilestone);

        int databaseSizeBeforeDelete = studyMilestoneRepository.findAll().size();

        // Delete the studyMilestone
        restStudyMilestoneMockMvc.perform(delete("/api/study-milestones/{id}", studyMilestone.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudyMilestone> studyMilestoneList = studyMilestoneRepository.findAll();
        assertThat(studyMilestoneList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StudyMilestone in Elasticsearch
        verify(mockStudyMilestoneSearchRepository, times(1)).deleteById(studyMilestone.getId());
    }

    @Test
    @Transactional
    public void searchStudyMilestone() throws Exception {
        // Initialize the database
        studyMilestoneService.save(studyMilestone);
        when(mockStudyMilestoneSearchRepository.search(queryStringQuery("id:" + studyMilestone.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(studyMilestone), PageRequest.of(0, 1), 1));
        // Search the studyMilestone
        restStudyMilestoneMockMvc.perform(get("/api/_search/study-milestones?query=id:" + studyMilestone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyMilestone.getId().intValue())))
            .andExpect(jsonPath("$.[*].mileStoneName").value(hasItem(DEFAULT_MILE_STONE_NAME)))
            .andExpect(jsonPath("$.[*].mileStoneType").value(hasItem(DEFAULT_MILE_STONE_TYPE)))
            .andExpect(jsonPath("$.[*].projectedCompletionDate").value(hasItem(DEFAULT_PROJECTED_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualCompletionDate").value(hasItem(DEFAULT_ACTUAL_COMPLETION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyMilestone.class);
        StudyMilestone studyMilestone1 = new StudyMilestone();
        studyMilestone1.setId(1L);
        StudyMilestone studyMilestone2 = new StudyMilestone();
        studyMilestone2.setId(studyMilestone1.getId());
        assertThat(studyMilestone1).isEqualTo(studyMilestone2);
        studyMilestone2.setId(2L);
        assertThat(studyMilestone1).isNotEqualTo(studyMilestone2);
        studyMilestone1.setId(null);
        assertThat(studyMilestone1).isNotEqualTo(studyMilestone2);
    }
}

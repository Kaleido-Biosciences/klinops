package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.StudyEndPoint;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.StudyEndPointRepository;
import com.kaleido.klinops.service.StudyEndPointService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.StudyEndPointCriteria;
import com.kaleido.klinops.service.StudyEndPointQueryService;

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
import java.util.List;

import static com.kaleido.klinops.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StudyEndPointResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class StudyEndPointResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIVE = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIVE = "BBBBBBBBBB";

    private static final String DEFAULT_END_POINT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_END_POINT_TYPE = "BBBBBBBBBB";

    @Autowired
    private StudyEndPointRepository studyEndPointRepository;

    @Autowired
    private StudyEndPointService studyEndPointService;

    @Autowired
    private StudyEndPointQueryService studyEndPointQueryService;

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

    private MockMvc restStudyEndPointMockMvc;

    private StudyEndPoint studyEndPoint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudyEndPointResource studyEndPointResource = new StudyEndPointResource(studyEndPointService, studyEndPointQueryService);
        this.restStudyEndPointMockMvc = MockMvcBuilders.standaloneSetup(studyEndPointResource)
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
    public static StudyEndPoint createEntity(EntityManager em) {
        StudyEndPoint studyEndPoint = new StudyEndPoint()
            .description(DEFAULT_DESCRIPTION)
            .objective(DEFAULT_OBJECTIVE)
            .endPointType(DEFAULT_END_POINT_TYPE);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        studyEndPoint.setStudy(clinicalStudy);
        return studyEndPoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyEndPoint createUpdatedEntity(EntityManager em) {
        StudyEndPoint studyEndPoint = new StudyEndPoint()
            .description(UPDATED_DESCRIPTION)
            .objective(UPDATED_OBJECTIVE)
            .endPointType(UPDATED_END_POINT_TYPE);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createUpdatedEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        studyEndPoint.setStudy(clinicalStudy);
        return studyEndPoint;
    }

    @BeforeEach
    public void initTest() {
        studyEndPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudyEndPoint() throws Exception {
        int databaseSizeBeforeCreate = studyEndPointRepository.findAll().size();

        // Create the StudyEndPoint
        restStudyEndPointMockMvc.perform(post("/api/study-end-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyEndPoint)))
            .andExpect(status().isCreated());

        // Validate the StudyEndPoint in the database
        List<StudyEndPoint> studyEndPointList = studyEndPointRepository.findAll();
        assertThat(studyEndPointList).hasSize(databaseSizeBeforeCreate + 1);
        StudyEndPoint testStudyEndPoint = studyEndPointList.get(studyEndPointList.size() - 1);
        assertThat(testStudyEndPoint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStudyEndPoint.getObjective()).isEqualTo(DEFAULT_OBJECTIVE);
        assertThat(testStudyEndPoint.getEndPointType()).isEqualTo(DEFAULT_END_POINT_TYPE);
    }

    @Test
    @Transactional
    public void createStudyEndPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studyEndPointRepository.findAll().size();

        // Create the StudyEndPoint with an existing ID
        studyEndPoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyEndPointMockMvc.perform(post("/api/study-end-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyEndPoint)))
            .andExpect(status().isBadRequest());

        // Validate the StudyEndPoint in the database
        List<StudyEndPoint> studyEndPointList = studyEndPointRepository.findAll();
        assertThat(studyEndPointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyEndPointRepository.findAll().size();
        // set the field null
        studyEndPoint.setDescription(null);

        // Create the StudyEndPoint, which fails.

        restStudyEndPointMockMvc.perform(post("/api/study-end-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyEndPoint)))
            .andExpect(status().isBadRequest());

        List<StudyEndPoint> studyEndPointList = studyEndPointRepository.findAll();
        assertThat(studyEndPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkObjectiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyEndPointRepository.findAll().size();
        // set the field null
        studyEndPoint.setObjective(null);

        // Create the StudyEndPoint, which fails.

        restStudyEndPointMockMvc.perform(post("/api/study-end-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyEndPoint)))
            .andExpect(status().isBadRequest());

        List<StudyEndPoint> studyEndPointList = studyEndPointRepository.findAll();
        assertThat(studyEndPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndPointTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyEndPointRepository.findAll().size();
        // set the field null
        studyEndPoint.setEndPointType(null);

        // Create the StudyEndPoint, which fails.

        restStudyEndPointMockMvc.perform(post("/api/study-end-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyEndPoint)))
            .andExpect(status().isBadRequest());

        List<StudyEndPoint> studyEndPointList = studyEndPointRepository.findAll();
        assertThat(studyEndPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudyEndPoints() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList
        restStudyEndPointMockMvc.perform(get("/api/study-end-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyEndPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].objective").value(hasItem(DEFAULT_OBJECTIVE.toString())))
            .andExpect(jsonPath("$.[*].endPointType").value(hasItem(DEFAULT_END_POINT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getStudyEndPoint() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get the studyEndPoint
        restStudyEndPointMockMvc.perform(get("/api/study-end-points/{id}", studyEndPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studyEndPoint.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.objective").value(DEFAULT_OBJECTIVE.toString()))
            .andExpect(jsonPath("$.endPointType").value(DEFAULT_END_POINT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where description equals to DEFAULT_DESCRIPTION
        defaultStudyEndPointShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the studyEndPointList where description equals to UPDATED_DESCRIPTION
        defaultStudyEndPointShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultStudyEndPointShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the studyEndPointList where description equals to UPDATED_DESCRIPTION
        defaultStudyEndPointShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where description is not null
        defaultStudyEndPointShouldBeFound("description.specified=true");

        // Get all the studyEndPointList where description is null
        defaultStudyEndPointShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByObjectiveIsEqualToSomething() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where objective equals to DEFAULT_OBJECTIVE
        defaultStudyEndPointShouldBeFound("objective.equals=" + DEFAULT_OBJECTIVE);

        // Get all the studyEndPointList where objective equals to UPDATED_OBJECTIVE
        defaultStudyEndPointShouldNotBeFound("objective.equals=" + UPDATED_OBJECTIVE);
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByObjectiveIsInShouldWork() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where objective in DEFAULT_OBJECTIVE or UPDATED_OBJECTIVE
        defaultStudyEndPointShouldBeFound("objective.in=" + DEFAULT_OBJECTIVE + "," + UPDATED_OBJECTIVE);

        // Get all the studyEndPointList where objective equals to UPDATED_OBJECTIVE
        defaultStudyEndPointShouldNotBeFound("objective.in=" + UPDATED_OBJECTIVE);
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByObjectiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where objective is not null
        defaultStudyEndPointShouldBeFound("objective.specified=true");

        // Get all the studyEndPointList where objective is null
        defaultStudyEndPointShouldNotBeFound("objective.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByEndPointTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where endPointType equals to DEFAULT_END_POINT_TYPE
        defaultStudyEndPointShouldBeFound("endPointType.equals=" + DEFAULT_END_POINT_TYPE);

        // Get all the studyEndPointList where endPointType equals to UPDATED_END_POINT_TYPE
        defaultStudyEndPointShouldNotBeFound("endPointType.equals=" + UPDATED_END_POINT_TYPE);
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByEndPointTypeIsInShouldWork() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where endPointType in DEFAULT_END_POINT_TYPE or UPDATED_END_POINT_TYPE
        defaultStudyEndPointShouldBeFound("endPointType.in=" + DEFAULT_END_POINT_TYPE + "," + UPDATED_END_POINT_TYPE);

        // Get all the studyEndPointList where endPointType equals to UPDATED_END_POINT_TYPE
        defaultStudyEndPointShouldNotBeFound("endPointType.in=" + UPDATED_END_POINT_TYPE);
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByEndPointTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyEndPointRepository.saveAndFlush(studyEndPoint);

        // Get all the studyEndPointList where endPointType is not null
        defaultStudyEndPointShouldBeFound("endPointType.specified=true");

        // Get all the studyEndPointList where endPointType is null
        defaultStudyEndPointShouldNotBeFound("endPointType.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyEndPointsByStudyIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClinicalStudy study = studyEndPoint.getStudy();
        studyEndPointRepository.saveAndFlush(studyEndPoint);
        Long studyId = study.getId();

        // Get all the studyEndPointList where study equals to studyId
        defaultStudyEndPointShouldBeFound("studyId.equals=" + studyId);

        // Get all the studyEndPointList where study equals to studyId + 1
        defaultStudyEndPointShouldNotBeFound("studyId.equals=" + (studyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudyEndPointShouldBeFound(String filter) throws Exception {
        restStudyEndPointMockMvc.perform(get("/api/study-end-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyEndPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].objective").value(hasItem(DEFAULT_OBJECTIVE)))
            .andExpect(jsonPath("$.[*].endPointType").value(hasItem(DEFAULT_END_POINT_TYPE)));

        // Check, that the count call also returns 1
        restStudyEndPointMockMvc.perform(get("/api/study-end-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudyEndPointShouldNotBeFound(String filter) throws Exception {
        restStudyEndPointMockMvc.perform(get("/api/study-end-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudyEndPointMockMvc.perform(get("/api/study-end-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStudyEndPoint() throws Exception {
        // Get the studyEndPoint
        restStudyEndPointMockMvc.perform(get("/api/study-end-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudyEndPoint() throws Exception {
        // Initialize the database
        studyEndPointService.save(studyEndPoint);

        int databaseSizeBeforeUpdate = studyEndPointRepository.findAll().size();

        // Update the studyEndPoint
        StudyEndPoint updatedStudyEndPoint = studyEndPointRepository.findById(studyEndPoint.getId()).get();
        // Disconnect from session so that the updates on updatedStudyEndPoint are not directly saved in db
        em.detach(updatedStudyEndPoint);
        updatedStudyEndPoint
            .description(UPDATED_DESCRIPTION)
            .objective(UPDATED_OBJECTIVE)
            .endPointType(UPDATED_END_POINT_TYPE);

        restStudyEndPointMockMvc.perform(put("/api/study-end-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudyEndPoint)))
            .andExpect(status().isOk());

        // Validate the StudyEndPoint in the database
        List<StudyEndPoint> studyEndPointList = studyEndPointRepository.findAll();
        assertThat(studyEndPointList).hasSize(databaseSizeBeforeUpdate);
        StudyEndPoint testStudyEndPoint = studyEndPointList.get(studyEndPointList.size() - 1);
        assertThat(testStudyEndPoint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStudyEndPoint.getObjective()).isEqualTo(UPDATED_OBJECTIVE);
        assertThat(testStudyEndPoint.getEndPointType()).isEqualTo(UPDATED_END_POINT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingStudyEndPoint() throws Exception {
        int databaseSizeBeforeUpdate = studyEndPointRepository.findAll().size();

        // Create the StudyEndPoint

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyEndPointMockMvc.perform(put("/api/study-end-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyEndPoint)))
            .andExpect(status().isBadRequest());

        // Validate the StudyEndPoint in the database
        List<StudyEndPoint> studyEndPointList = studyEndPointRepository.findAll();
        assertThat(studyEndPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudyEndPoint() throws Exception {
        // Initialize the database
        studyEndPointService.save(studyEndPoint);

        int databaseSizeBeforeDelete = studyEndPointRepository.findAll().size();

        // Delete the studyEndPoint
        restStudyEndPointMockMvc.perform(delete("/api/study-end-points/{id}", studyEndPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudyEndPoint> studyEndPointList = studyEndPointRepository.findAll();
        assertThat(studyEndPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyEndPoint.class);
        StudyEndPoint studyEndPoint1 = new StudyEndPoint();
        studyEndPoint1.setId(1L);
        StudyEndPoint studyEndPoint2 = new StudyEndPoint();
        studyEndPoint2.setId(studyEndPoint1.getId());
        assertThat(studyEndPoint1).isEqualTo(studyEndPoint2);
        studyEndPoint2.setId(2L);
        assertThat(studyEndPoint1).isNotEqualTo(studyEndPoint2);
        studyEndPoint1.setId(null);
        assertThat(studyEndPoint1).isNotEqualTo(studyEndPoint2);
    }
}

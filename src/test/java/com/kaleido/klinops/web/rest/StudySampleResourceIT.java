/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.StudySample;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.StudySampleRepository;
import com.kaleido.klinops.service.StudySampleService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.StudySampleCriteria;
import com.kaleido.klinops.service.StudySampleQueryService;

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
 * Integration tests for the {@link StudySampleResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class StudySampleResourceIT {

    private static final String DEFAULT_SAMPLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXPECTED_NUMBER_OF_SAMPLES = 0;
    private static final Integer UPDATED_EXPECTED_NUMBER_OF_SAMPLES = 1;
    private static final Integer SMALLER_EXPECTED_NUMBER_OF_SAMPLES = 0 - 1;

    @Autowired
    private StudySampleRepository studySampleRepository;

    @Autowired
    private StudySampleService studySampleService;

    @Autowired
    private StudySampleQueryService studySampleQueryService;

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

    private MockMvc restStudySampleMockMvc;

    private StudySample studySample;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudySampleResource studySampleResource = new StudySampleResource(studySampleService, studySampleQueryService);
        this.restStudySampleMockMvc = MockMvcBuilders.standaloneSetup(studySampleResource)
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
    public static StudySample createEntity(EntityManager em) {
        StudySample studySample = new StudySample()
            .sampleType(DEFAULT_SAMPLE_TYPE)
            .expectedNumberOfSamples(DEFAULT_EXPECTED_NUMBER_OF_SAMPLES);
        return studySample;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudySample createUpdatedEntity(EntityManager em) {
        StudySample studySample = new StudySample()
            .sampleType(UPDATED_SAMPLE_TYPE)
            .expectedNumberOfSamples(UPDATED_EXPECTED_NUMBER_OF_SAMPLES);
        return studySample;
    }

    @BeforeEach
    public void initTest() {
        studySample = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudySample() throws Exception {
        int databaseSizeBeforeCreate = studySampleRepository.findAll().size();

        // Create the StudySample
        restStudySampleMockMvc.perform(post("/api/study-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studySample)))
            .andExpect(status().isCreated());

        // Validate the StudySample in the database
        List<StudySample> studySampleList = studySampleRepository.findAll();
        assertThat(studySampleList).hasSize(databaseSizeBeforeCreate + 1);
        StudySample testStudySample = studySampleList.get(studySampleList.size() - 1);
        assertThat(testStudySample.getSampleType()).isEqualTo(DEFAULT_SAMPLE_TYPE);
        assertThat(testStudySample.getExpectedNumberOfSamples()).isEqualTo(DEFAULT_EXPECTED_NUMBER_OF_SAMPLES);
    }

    @Test
    @Transactional
    public void createStudySampleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studySampleRepository.findAll().size();

        // Create the StudySample with an existing ID
        studySample.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudySampleMockMvc.perform(post("/api/study-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studySample)))
            .andExpect(status().isBadRequest());

        // Validate the StudySample in the database
        List<StudySample> studySampleList = studySampleRepository.findAll();
        assertThat(studySampleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSampleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studySampleRepository.findAll().size();
        // set the field null
        studySample.setSampleType(null);

        // Create the StudySample, which fails.

        restStudySampleMockMvc.perform(post("/api/study-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studySample)))
            .andExpect(status().isBadRequest());

        List<StudySample> studySampleList = studySampleRepository.findAll();
        assertThat(studySampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudySamples() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList
        restStudySampleMockMvc.perform(get("/api/study-samples?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studySample.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleType").value(hasItem(DEFAULT_SAMPLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expectedNumberOfSamples").value(hasItem(DEFAULT_EXPECTED_NUMBER_OF_SAMPLES)));
    }

    @Test
    @Transactional
    public void getStudySample() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get the studySample
        restStudySampleMockMvc.perform(get("/api/study-samples/{id}", studySample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studySample.getId().intValue()))
            .andExpect(jsonPath("$.sampleType").value(DEFAULT_SAMPLE_TYPE.toString()))
            .andExpect(jsonPath("$.expectedNumberOfSamples").value(DEFAULT_EXPECTED_NUMBER_OF_SAMPLES));
    }

    @Test
    @Transactional
    public void getAllStudySamplesBySampleTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where sampleType equals to DEFAULT_SAMPLE_TYPE
        defaultStudySampleShouldBeFound("sampleType.equals=" + DEFAULT_SAMPLE_TYPE);

        // Get all the studySampleList where sampleType equals to UPDATED_SAMPLE_TYPE
        defaultStudySampleShouldNotBeFound("sampleType.equals=" + UPDATED_SAMPLE_TYPE);
    }

    @Test
    @Transactional
    public void getAllStudySamplesBySampleTypeIsInShouldWork() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where sampleType in DEFAULT_SAMPLE_TYPE or UPDATED_SAMPLE_TYPE
        defaultStudySampleShouldBeFound("sampleType.in=" + DEFAULT_SAMPLE_TYPE + "," + UPDATED_SAMPLE_TYPE);

        // Get all the studySampleList where sampleType equals to UPDATED_SAMPLE_TYPE
        defaultStudySampleShouldNotBeFound("sampleType.in=" + UPDATED_SAMPLE_TYPE);
    }

    @Test
    @Transactional
    public void getAllStudySamplesBySampleTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where sampleType is not null
        defaultStudySampleShouldBeFound("sampleType.specified=true");

        // Get all the studySampleList where sampleType is null
        defaultStudySampleShouldNotBeFound("sampleType.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudySamplesByExpectedNumberOfSamplesIsEqualToSomething() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where expectedNumberOfSamples equals to DEFAULT_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldBeFound("expectedNumberOfSamples.equals=" + DEFAULT_EXPECTED_NUMBER_OF_SAMPLES);

        // Get all the studySampleList where expectedNumberOfSamples equals to UPDATED_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldNotBeFound("expectedNumberOfSamples.equals=" + UPDATED_EXPECTED_NUMBER_OF_SAMPLES);
    }

    @Test
    @Transactional
    public void getAllStudySamplesByExpectedNumberOfSamplesIsInShouldWork() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where expectedNumberOfSamples in DEFAULT_EXPECTED_NUMBER_OF_SAMPLES or UPDATED_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldBeFound("expectedNumberOfSamples.in=" + DEFAULT_EXPECTED_NUMBER_OF_SAMPLES + "," + UPDATED_EXPECTED_NUMBER_OF_SAMPLES);

        // Get all the studySampleList where expectedNumberOfSamples equals to UPDATED_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldNotBeFound("expectedNumberOfSamples.in=" + UPDATED_EXPECTED_NUMBER_OF_SAMPLES);
    }

    @Test
    @Transactional
    public void getAllStudySamplesByExpectedNumberOfSamplesIsNullOrNotNull() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where expectedNumberOfSamples is not null
        defaultStudySampleShouldBeFound("expectedNumberOfSamples.specified=true");

        // Get all the studySampleList where expectedNumberOfSamples is null
        defaultStudySampleShouldNotBeFound("expectedNumberOfSamples.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudySamplesByExpectedNumberOfSamplesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where expectedNumberOfSamples is greater than or equal to DEFAULT_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldBeFound("expectedNumberOfSamples.greaterThanOrEqual=" + DEFAULT_EXPECTED_NUMBER_OF_SAMPLES);

        // Get all the studySampleList where expectedNumberOfSamples is greater than or equal to UPDATED_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldNotBeFound("expectedNumberOfSamples.greaterThanOrEqual=" + UPDATED_EXPECTED_NUMBER_OF_SAMPLES);
    }

    @Test
    @Transactional
    public void getAllStudySamplesByExpectedNumberOfSamplesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where expectedNumberOfSamples is less than or equal to DEFAULT_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldBeFound("expectedNumberOfSamples.lessThanOrEqual=" + DEFAULT_EXPECTED_NUMBER_OF_SAMPLES);

        // Get all the studySampleList where expectedNumberOfSamples is less than or equal to SMALLER_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldNotBeFound("expectedNumberOfSamples.lessThanOrEqual=" + SMALLER_EXPECTED_NUMBER_OF_SAMPLES);
    }

    @Test
    @Transactional
    public void getAllStudySamplesByExpectedNumberOfSamplesIsLessThanSomething() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where expectedNumberOfSamples is less than DEFAULT_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldNotBeFound("expectedNumberOfSamples.lessThan=" + DEFAULT_EXPECTED_NUMBER_OF_SAMPLES);

        // Get all the studySampleList where expectedNumberOfSamples is less than UPDATED_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldBeFound("expectedNumberOfSamples.lessThan=" + UPDATED_EXPECTED_NUMBER_OF_SAMPLES);
    }

    @Test
    @Transactional
    public void getAllStudySamplesByExpectedNumberOfSamplesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);

        // Get all the studySampleList where expectedNumberOfSamples is greater than DEFAULT_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldNotBeFound("expectedNumberOfSamples.greaterThan=" + DEFAULT_EXPECTED_NUMBER_OF_SAMPLES);

        // Get all the studySampleList where expectedNumberOfSamples is greater than SMALLER_EXPECTED_NUMBER_OF_SAMPLES
        defaultStudySampleShouldBeFound("expectedNumberOfSamples.greaterThan=" + SMALLER_EXPECTED_NUMBER_OF_SAMPLES);
    }


    @Test
    @Transactional
    public void getAllStudySamplesByStudyIsEqualToSomething() throws Exception {
        // Initialize the database
        studySampleRepository.saveAndFlush(studySample);
        ClinicalStudy study = ClinicalStudyResourceIT.createEntity(em);
        em.persist(study);
        em.flush();
        studySample.setStudy(study);
        studySampleRepository.saveAndFlush(studySample);
        Long studyId = study.getId();

        // Get all the studySampleList where study equals to studyId
        defaultStudySampleShouldBeFound("studyId.equals=" + studyId);

        // Get all the studySampleList where study equals to studyId + 1
        defaultStudySampleShouldNotBeFound("studyId.equals=" + (studyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudySampleShouldBeFound(String filter) throws Exception {
        restStudySampleMockMvc.perform(get("/api/study-samples?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studySample.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleType").value(hasItem(DEFAULT_SAMPLE_TYPE)))
            .andExpect(jsonPath("$.[*].expectedNumberOfSamples").value(hasItem(DEFAULT_EXPECTED_NUMBER_OF_SAMPLES)));

        // Check, that the count call also returns 1
        restStudySampleMockMvc.perform(get("/api/study-samples/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudySampleShouldNotBeFound(String filter) throws Exception {
        restStudySampleMockMvc.perform(get("/api/study-samples?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudySampleMockMvc.perform(get("/api/study-samples/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStudySample() throws Exception {
        // Get the studySample
        restStudySampleMockMvc.perform(get("/api/study-samples/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudySample() throws Exception {
        // Initialize the database
        studySampleService.save(studySample);

        int databaseSizeBeforeUpdate = studySampleRepository.findAll().size();

        // Update the studySample
        StudySample updatedStudySample = studySampleRepository.findById(studySample.getId()).get();
        // Disconnect from session so that the updates on updatedStudySample are not directly saved in db
        em.detach(updatedStudySample);
        updatedStudySample
            .sampleType(UPDATED_SAMPLE_TYPE)
            .expectedNumberOfSamples(UPDATED_EXPECTED_NUMBER_OF_SAMPLES);

        restStudySampleMockMvc.perform(put("/api/study-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudySample)))
            .andExpect(status().isOk());

        // Validate the StudySample in the database
        List<StudySample> studySampleList = studySampleRepository.findAll();
        assertThat(studySampleList).hasSize(databaseSizeBeforeUpdate);
        StudySample testStudySample = studySampleList.get(studySampleList.size() - 1);
        assertThat(testStudySample.getSampleType()).isEqualTo(UPDATED_SAMPLE_TYPE);
        assertThat(testStudySample.getExpectedNumberOfSamples()).isEqualTo(UPDATED_EXPECTED_NUMBER_OF_SAMPLES);
    }

    @Test
    @Transactional
    public void updateNonExistingStudySample() throws Exception {
        int databaseSizeBeforeUpdate = studySampleRepository.findAll().size();

        // Create the StudySample

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudySampleMockMvc.perform(put("/api/study-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studySample)))
            .andExpect(status().isBadRequest());

        // Validate the StudySample in the database
        List<StudySample> studySampleList = studySampleRepository.findAll();
        assertThat(studySampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudySample() throws Exception {
        // Initialize the database
        studySampleService.save(studySample);

        int databaseSizeBeforeDelete = studySampleRepository.findAll().size();

        // Delete the studySample
        restStudySampleMockMvc.perform(delete("/api/study-samples/{id}", studySample.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudySample> studySampleList = studySampleRepository.findAll();
        assertThat(studySampleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudySample.class);
        StudySample studySample1 = new StudySample();
        studySample1.setId(1L);
        StudySample studySample2 = new StudySample();
        studySample2.setId(studySample1.getId());
        assertThat(studySample1).isEqualTo(studySample2);
        studySample2.setId(2L);
        assertThat(studySample1).isNotEqualTo(studySample2);
        studySample1.setId(null);
        assertThat(studySample1).isNotEqualTo(studySample2);
    }
}

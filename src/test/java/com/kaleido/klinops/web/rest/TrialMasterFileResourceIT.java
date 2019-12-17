package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.TrialMasterFile;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.TrialMasterFileRepository;
import com.kaleido.klinops.repository.search.TrialMasterFileSearchRepository;
import com.kaleido.klinops.service.TrialMasterFileService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.TrialMasterFileCriteria;
import com.kaleido.klinops.service.TrialMasterFileQueryService;

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
 * Integration tests for the {@link TrialMasterFileResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class TrialMasterFileResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ELECTRONIC = false;
    private static final Boolean UPDATED_ELECTRONIC = true;

    @Autowired
    private TrialMasterFileRepository trialMasterFileRepository;

    @Autowired
    private TrialMasterFileService trialMasterFileService;

    /**
     * This repository is mocked in the com.kaleido.klinops.repository.search test package.
     *
     * @see com.kaleido.klinops.repository.search.TrialMasterFileSearchRepositoryMockConfiguration
     */
    @Autowired
    private TrialMasterFileSearchRepository mockTrialMasterFileSearchRepository;

    @Autowired
    private TrialMasterFileQueryService trialMasterFileQueryService;

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

    private MockMvc restTrialMasterFileMockMvc;

    private TrialMasterFile trialMasterFile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrialMasterFileResource trialMasterFileResource = new TrialMasterFileResource(trialMasterFileService, trialMasterFileQueryService);
        this.restTrialMasterFileMockMvc = MockMvcBuilders.standaloneSetup(trialMasterFileResource)
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
    public static TrialMasterFile createEntity(EntityManager em) {
        TrialMasterFile trialMasterFile = new TrialMasterFile()
            .fileName(DEFAULT_FILE_NAME)
            .location(DEFAULT_LOCATION)
            .status(DEFAULT_STATUS)
            .electronic(DEFAULT_ELECTRONIC);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        trialMasterFile.setClinicalStudy(clinicalStudy);
        return trialMasterFile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrialMasterFile createUpdatedEntity(EntityManager em) {
        TrialMasterFile trialMasterFile = new TrialMasterFile()
            .fileName(UPDATED_FILE_NAME)
            .location(UPDATED_LOCATION)
            .status(UPDATED_STATUS)
            .electronic(UPDATED_ELECTRONIC);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createUpdatedEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        trialMasterFile.setClinicalStudy(clinicalStudy);
        return trialMasterFile;
    }

    @BeforeEach
    public void initTest() {
        trialMasterFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrialMasterFile() throws Exception {
        int databaseSizeBeforeCreate = trialMasterFileRepository.findAll().size();

        // Create the TrialMasterFile
        restTrialMasterFileMockMvc.perform(post("/api/trial-master-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trialMasterFile)))
            .andExpect(status().isCreated());

        // Validate the TrialMasterFile in the database
        List<TrialMasterFile> trialMasterFileList = trialMasterFileRepository.findAll();
        assertThat(trialMasterFileList).hasSize(databaseSizeBeforeCreate + 1);
        TrialMasterFile testTrialMasterFile = trialMasterFileList.get(trialMasterFileList.size() - 1);
        assertThat(testTrialMasterFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testTrialMasterFile.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testTrialMasterFile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTrialMasterFile.isElectronic()).isEqualTo(DEFAULT_ELECTRONIC);

        // Validate the TrialMasterFile in Elasticsearch
        verify(mockTrialMasterFileSearchRepository, times(1)).save(testTrialMasterFile);
    }

    @Test
    @Transactional
    public void createTrialMasterFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trialMasterFileRepository.findAll().size();

        // Create the TrialMasterFile with an existing ID
        trialMasterFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrialMasterFileMockMvc.perform(post("/api/trial-master-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trialMasterFile)))
            .andExpect(status().isBadRequest());

        // Validate the TrialMasterFile in the database
        List<TrialMasterFile> trialMasterFileList = trialMasterFileRepository.findAll();
        assertThat(trialMasterFileList).hasSize(databaseSizeBeforeCreate);

        // Validate the TrialMasterFile in Elasticsearch
        verify(mockTrialMasterFileSearchRepository, times(0)).save(trialMasterFile);
    }


    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = trialMasterFileRepository.findAll().size();
        // set the field null
        trialMasterFile.setLocation(null);

        // Create the TrialMasterFile, which fails.

        restTrialMasterFileMockMvc.perform(post("/api/trial-master-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trialMasterFile)))
            .andExpect(status().isBadRequest());

        List<TrialMasterFile> trialMasterFileList = trialMasterFileRepository.findAll();
        assertThat(trialMasterFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkElectronicIsRequired() throws Exception {
        int databaseSizeBeforeTest = trialMasterFileRepository.findAll().size();
        // set the field null
        trialMasterFile.setElectronic(null);

        // Create the TrialMasterFile, which fails.

        restTrialMasterFileMockMvc.perform(post("/api/trial-master-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trialMasterFile)))
            .andExpect(status().isBadRequest());

        List<TrialMasterFile> trialMasterFileList = trialMasterFileRepository.findAll();
        assertThat(trialMasterFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFiles() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList
        restTrialMasterFileMockMvc.perform(get("/api/trial-master-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trialMasterFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].electronic").value(hasItem(DEFAULT_ELECTRONIC.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getTrialMasterFile() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get the trialMasterFile
        restTrialMasterFileMockMvc.perform(get("/api/trial-master-files/{id}", trialMasterFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trialMasterFile.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.electronic").value(DEFAULT_ELECTRONIC.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where fileName equals to DEFAULT_FILE_NAME
        defaultTrialMasterFileShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the trialMasterFileList where fileName equals to UPDATED_FILE_NAME
        defaultTrialMasterFileShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultTrialMasterFileShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the trialMasterFileList where fileName equals to UPDATED_FILE_NAME
        defaultTrialMasterFileShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where fileName is not null
        defaultTrialMasterFileShouldBeFound("fileName.specified=true");

        // Get all the trialMasterFileList where fileName is null
        defaultTrialMasterFileShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where location equals to DEFAULT_LOCATION
        defaultTrialMasterFileShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the trialMasterFileList where location equals to UPDATED_LOCATION
        defaultTrialMasterFileShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultTrialMasterFileShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the trialMasterFileList where location equals to UPDATED_LOCATION
        defaultTrialMasterFileShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where location is not null
        defaultTrialMasterFileShouldBeFound("location.specified=true");

        // Get all the trialMasterFileList where location is null
        defaultTrialMasterFileShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where status equals to DEFAULT_STATUS
        defaultTrialMasterFileShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the trialMasterFileList where status equals to UPDATED_STATUS
        defaultTrialMasterFileShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTrialMasterFileShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the trialMasterFileList where status equals to UPDATED_STATUS
        defaultTrialMasterFileShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where status is not null
        defaultTrialMasterFileShouldBeFound("status.specified=true");

        // Get all the trialMasterFileList where status is null
        defaultTrialMasterFileShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByElectronicIsEqualToSomething() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where electronic equals to DEFAULT_ELECTRONIC
        defaultTrialMasterFileShouldBeFound("electronic.equals=" + DEFAULT_ELECTRONIC);

        // Get all the trialMasterFileList where electronic equals to UPDATED_ELECTRONIC
        defaultTrialMasterFileShouldNotBeFound("electronic.equals=" + UPDATED_ELECTRONIC);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByElectronicIsInShouldWork() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where electronic in DEFAULT_ELECTRONIC or UPDATED_ELECTRONIC
        defaultTrialMasterFileShouldBeFound("electronic.in=" + DEFAULT_ELECTRONIC + "," + UPDATED_ELECTRONIC);

        // Get all the trialMasterFileList where electronic equals to UPDATED_ELECTRONIC
        defaultTrialMasterFileShouldNotBeFound("electronic.in=" + UPDATED_ELECTRONIC);
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByElectronicIsNullOrNotNull() throws Exception {
        // Initialize the database
        trialMasterFileRepository.saveAndFlush(trialMasterFile);

        // Get all the trialMasterFileList where electronic is not null
        defaultTrialMasterFileShouldBeFound("electronic.specified=true");

        // Get all the trialMasterFileList where electronic is null
        defaultTrialMasterFileShouldNotBeFound("electronic.specified=false");
    }

    @Test
    @Transactional
    public void getAllTrialMasterFilesByClinicalStudyIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClinicalStudy clinicalStudy = trialMasterFile.getClinicalStudy();
        trialMasterFileRepository.saveAndFlush(trialMasterFile);
        Long clinicalStudyId = clinicalStudy.getId();

        // Get all the trialMasterFileList where clinicalStudy equals to clinicalStudyId
        defaultTrialMasterFileShouldBeFound("clinicalStudyId.equals=" + clinicalStudyId);

        // Get all the trialMasterFileList where clinicalStudy equals to clinicalStudyId + 1
        defaultTrialMasterFileShouldNotBeFound("clinicalStudyId.equals=" + (clinicalStudyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrialMasterFileShouldBeFound(String filter) throws Exception {
        restTrialMasterFileMockMvc.perform(get("/api/trial-master-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trialMasterFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].electronic").value(hasItem(DEFAULT_ELECTRONIC.booleanValue())));

        // Check, that the count call also returns 1
        restTrialMasterFileMockMvc.perform(get("/api/trial-master-files/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrialMasterFileShouldNotBeFound(String filter) throws Exception {
        restTrialMasterFileMockMvc.perform(get("/api/trial-master-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrialMasterFileMockMvc.perform(get("/api/trial-master-files/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTrialMasterFile() throws Exception {
        // Get the trialMasterFile
        restTrialMasterFileMockMvc.perform(get("/api/trial-master-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrialMasterFile() throws Exception {
        // Initialize the database
        trialMasterFileService.save(trialMasterFile);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTrialMasterFileSearchRepository);

        int databaseSizeBeforeUpdate = trialMasterFileRepository.findAll().size();

        // Update the trialMasterFile
        TrialMasterFile updatedTrialMasterFile = trialMasterFileRepository.findById(trialMasterFile.getId()).get();
        // Disconnect from session so that the updates on updatedTrialMasterFile are not directly saved in db
        em.detach(updatedTrialMasterFile);
        updatedTrialMasterFile
            .fileName(UPDATED_FILE_NAME)
            .location(UPDATED_LOCATION)
            .status(UPDATED_STATUS)
            .electronic(UPDATED_ELECTRONIC);

        restTrialMasterFileMockMvc.perform(put("/api/trial-master-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrialMasterFile)))
            .andExpect(status().isOk());

        // Validate the TrialMasterFile in the database
        List<TrialMasterFile> trialMasterFileList = trialMasterFileRepository.findAll();
        assertThat(trialMasterFileList).hasSize(databaseSizeBeforeUpdate);
        TrialMasterFile testTrialMasterFile = trialMasterFileList.get(trialMasterFileList.size() - 1);
        assertThat(testTrialMasterFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testTrialMasterFile.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testTrialMasterFile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTrialMasterFile.isElectronic()).isEqualTo(UPDATED_ELECTRONIC);

        // Validate the TrialMasterFile in Elasticsearch
        verify(mockTrialMasterFileSearchRepository, times(1)).save(testTrialMasterFile);
    }

    @Test
    @Transactional
    public void updateNonExistingTrialMasterFile() throws Exception {
        int databaseSizeBeforeUpdate = trialMasterFileRepository.findAll().size();

        // Create the TrialMasterFile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrialMasterFileMockMvc.perform(put("/api/trial-master-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trialMasterFile)))
            .andExpect(status().isBadRequest());

        // Validate the TrialMasterFile in the database
        List<TrialMasterFile> trialMasterFileList = trialMasterFileRepository.findAll();
        assertThat(trialMasterFileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TrialMasterFile in Elasticsearch
        verify(mockTrialMasterFileSearchRepository, times(0)).save(trialMasterFile);
    }

    @Test
    @Transactional
    public void deleteTrialMasterFile() throws Exception {
        // Initialize the database
        trialMasterFileService.save(trialMasterFile);

        int databaseSizeBeforeDelete = trialMasterFileRepository.findAll().size();

        // Delete the trialMasterFile
        restTrialMasterFileMockMvc.perform(delete("/api/trial-master-files/{id}", trialMasterFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrialMasterFile> trialMasterFileList = trialMasterFileRepository.findAll();
        assertThat(trialMasterFileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TrialMasterFile in Elasticsearch
        verify(mockTrialMasterFileSearchRepository, times(1)).deleteById(trialMasterFile.getId());
    }

    @Test
    @Transactional
    public void searchTrialMasterFile() throws Exception {
        // Initialize the database
        trialMasterFileService.save(trialMasterFile);
        when(mockTrialMasterFileSearchRepository.search(queryStringQuery("id:" + trialMasterFile.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(trialMasterFile), PageRequest.of(0, 1), 1));
        // Search the trialMasterFile
        restTrialMasterFileMockMvc.perform(get("/api/_search/trial-master-files?query=id:" + trialMasterFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trialMasterFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].electronic").value(hasItem(DEFAULT_ELECTRONIC.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrialMasterFile.class);
        TrialMasterFile trialMasterFile1 = new TrialMasterFile();
        trialMasterFile1.setId(1L);
        TrialMasterFile trialMasterFile2 = new TrialMasterFile();
        trialMasterFile2.setId(trialMasterFile1.getId());
        assertThat(trialMasterFile1).isEqualTo(trialMasterFile2);
        trialMasterFile2.setId(2L);
        assertThat(trialMasterFile1).isNotEqualTo(trialMasterFile2);
        trialMasterFile1.setId(null);
        assertThat(trialMasterFile1).isNotEqualTo(trialMasterFile2);
    }
}

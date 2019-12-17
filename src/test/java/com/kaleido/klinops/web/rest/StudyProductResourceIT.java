package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.StudyProduct;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.StudyProductRepository;
import com.kaleido.klinops.repository.search.StudyProductSearchRepository;
import com.kaleido.klinops.service.StudyProductService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.StudyProductCriteria;
import com.kaleido.klinops.service.StudyProductQueryService;

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
 * Integration tests for the {@link StudyProductResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class StudyProductResourceIT {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOSE_RANGE = "AAAAAAAAAA";
    private static final String UPDATED_DOSE_RANGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DAYS_OF_EXPOSURE = 0;
    private static final Integer UPDATED_DAYS_OF_EXPOSURE = 1;
    private static final Integer SMALLER_DAYS_OF_EXPOSURE = 0 - 1;

    private static final String DEFAULT_FORMULATION = "AAAAAAAAAA";
    private static final String UPDATED_FORMULATION = "BBBBBBBBBB";

    @Autowired
    private StudyProductRepository studyProductRepository;

    @Autowired
    private StudyProductService studyProductService;

    /**
     * This repository is mocked in the com.kaleido.klinops.repository.search test package.
     *
     * @see com.kaleido.klinops.repository.search.StudyProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private StudyProductSearchRepository mockStudyProductSearchRepository;

    @Autowired
    private StudyProductQueryService studyProductQueryService;

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

    private MockMvc restStudyProductMockMvc;

    private StudyProduct studyProduct;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudyProductResource studyProductResource = new StudyProductResource(studyProductService, studyProductQueryService);
        this.restStudyProductMockMvc = MockMvcBuilders.standaloneSetup(studyProductResource)
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
    public static StudyProduct createEntity(EntityManager em) {
        StudyProduct studyProduct = new StudyProduct()
            .productName(DEFAULT_PRODUCT_NAME)
            .doseRange(DEFAULT_DOSE_RANGE)
            .daysOfExposure(DEFAULT_DAYS_OF_EXPOSURE)
            .formulation(DEFAULT_FORMULATION);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        studyProduct.setStudy(clinicalStudy);
        return studyProduct;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyProduct createUpdatedEntity(EntityManager em) {
        StudyProduct studyProduct = new StudyProduct()
            .productName(UPDATED_PRODUCT_NAME)
            .doseRange(UPDATED_DOSE_RANGE)
            .daysOfExposure(UPDATED_DAYS_OF_EXPOSURE)
            .formulation(UPDATED_FORMULATION);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createUpdatedEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        studyProduct.setStudy(clinicalStudy);
        return studyProduct;
    }

    @BeforeEach
    public void initTest() {
        studyProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudyProduct() throws Exception {
        int databaseSizeBeforeCreate = studyProductRepository.findAll().size();

        // Create the StudyProduct
        restStudyProductMockMvc.perform(post("/api/study-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyProduct)))
            .andExpect(status().isCreated());

        // Validate the StudyProduct in the database
        List<StudyProduct> studyProductList = studyProductRepository.findAll();
        assertThat(studyProductList).hasSize(databaseSizeBeforeCreate + 1);
        StudyProduct testStudyProduct = studyProductList.get(studyProductList.size() - 1);
        assertThat(testStudyProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testStudyProduct.getDoseRange()).isEqualTo(DEFAULT_DOSE_RANGE);
        assertThat(testStudyProduct.getDaysOfExposure()).isEqualTo(DEFAULT_DAYS_OF_EXPOSURE);
        assertThat(testStudyProduct.getFormulation()).isEqualTo(DEFAULT_FORMULATION);

        // Validate the StudyProduct in Elasticsearch
        verify(mockStudyProductSearchRepository, times(1)).save(testStudyProduct);
    }

    @Test
    @Transactional
    public void createStudyProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studyProductRepository.findAll().size();

        // Create the StudyProduct with an existing ID
        studyProduct.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyProductMockMvc.perform(post("/api/study-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyProduct)))
            .andExpect(status().isBadRequest());

        // Validate the StudyProduct in the database
        List<StudyProduct> studyProductList = studyProductRepository.findAll();
        assertThat(studyProductList).hasSize(databaseSizeBeforeCreate);

        // Validate the StudyProduct in Elasticsearch
        verify(mockStudyProductSearchRepository, times(0)).save(studyProduct);
    }


    @Test
    @Transactional
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyProductRepository.findAll().size();
        // set the field null
        studyProduct.setProductName(null);

        // Create the StudyProduct, which fails.

        restStudyProductMockMvc.perform(post("/api/study-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyProduct)))
            .andExpect(status().isBadRequest());

        List<StudyProduct> studyProductList = studyProductRepository.findAll();
        assertThat(studyProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFormulationIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyProductRepository.findAll().size();
        // set the field null
        studyProduct.setFormulation(null);

        // Create the StudyProduct, which fails.

        restStudyProductMockMvc.perform(post("/api/study-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyProduct)))
            .andExpect(status().isBadRequest());

        List<StudyProduct> studyProductList = studyProductRepository.findAll();
        assertThat(studyProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudyProducts() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList
        restStudyProductMockMvc.perform(get("/api/study-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].doseRange").value(hasItem(DEFAULT_DOSE_RANGE.toString())))
            .andExpect(jsonPath("$.[*].daysOfExposure").value(hasItem(DEFAULT_DAYS_OF_EXPOSURE)))
            .andExpect(jsonPath("$.[*].formulation").value(hasItem(DEFAULT_FORMULATION.toString())));
    }
    
    @Test
    @Transactional
    public void getStudyProduct() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get the studyProduct
        restStudyProductMockMvc.perform(get("/api/study-products/{id}", studyProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studyProduct.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.doseRange").value(DEFAULT_DOSE_RANGE.toString()))
            .andExpect(jsonPath("$.daysOfExposure").value(DEFAULT_DAYS_OF_EXPOSURE))
            .andExpect(jsonPath("$.formulation").value(DEFAULT_FORMULATION.toString()));
    }

    @Test
    @Transactional
    public void getAllStudyProductsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where productName equals to DEFAULT_PRODUCT_NAME
        defaultStudyProductShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the studyProductList where productName equals to UPDATED_PRODUCT_NAME
        defaultStudyProductShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultStudyProductShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the studyProductList where productName equals to UPDATED_PRODUCT_NAME
        defaultStudyProductShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where productName is not null
        defaultStudyProductShouldBeFound("productName.specified=true");

        // Get all the studyProductList where productName is null
        defaultStudyProductShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDoseRangeIsEqualToSomething() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where doseRange equals to DEFAULT_DOSE_RANGE
        defaultStudyProductShouldBeFound("doseRange.equals=" + DEFAULT_DOSE_RANGE);

        // Get all the studyProductList where doseRange equals to UPDATED_DOSE_RANGE
        defaultStudyProductShouldNotBeFound("doseRange.equals=" + UPDATED_DOSE_RANGE);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDoseRangeIsInShouldWork() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where doseRange in DEFAULT_DOSE_RANGE or UPDATED_DOSE_RANGE
        defaultStudyProductShouldBeFound("doseRange.in=" + DEFAULT_DOSE_RANGE + "," + UPDATED_DOSE_RANGE);

        // Get all the studyProductList where doseRange equals to UPDATED_DOSE_RANGE
        defaultStudyProductShouldNotBeFound("doseRange.in=" + UPDATED_DOSE_RANGE);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDoseRangeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where doseRange is not null
        defaultStudyProductShouldBeFound("doseRange.specified=true");

        // Get all the studyProductList where doseRange is null
        defaultStudyProductShouldNotBeFound("doseRange.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDaysOfExposureIsEqualToSomething() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where daysOfExposure equals to DEFAULT_DAYS_OF_EXPOSURE
        defaultStudyProductShouldBeFound("daysOfExposure.equals=" + DEFAULT_DAYS_OF_EXPOSURE);

        // Get all the studyProductList where daysOfExposure equals to UPDATED_DAYS_OF_EXPOSURE
        defaultStudyProductShouldNotBeFound("daysOfExposure.equals=" + UPDATED_DAYS_OF_EXPOSURE);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDaysOfExposureIsInShouldWork() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where daysOfExposure in DEFAULT_DAYS_OF_EXPOSURE or UPDATED_DAYS_OF_EXPOSURE
        defaultStudyProductShouldBeFound("daysOfExposure.in=" + DEFAULT_DAYS_OF_EXPOSURE + "," + UPDATED_DAYS_OF_EXPOSURE);

        // Get all the studyProductList where daysOfExposure equals to UPDATED_DAYS_OF_EXPOSURE
        defaultStudyProductShouldNotBeFound("daysOfExposure.in=" + UPDATED_DAYS_OF_EXPOSURE);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDaysOfExposureIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where daysOfExposure is not null
        defaultStudyProductShouldBeFound("daysOfExposure.specified=true");

        // Get all the studyProductList where daysOfExposure is null
        defaultStudyProductShouldNotBeFound("daysOfExposure.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDaysOfExposureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where daysOfExposure is greater than or equal to DEFAULT_DAYS_OF_EXPOSURE
        defaultStudyProductShouldBeFound("daysOfExposure.greaterThanOrEqual=" + DEFAULT_DAYS_OF_EXPOSURE);

        // Get all the studyProductList where daysOfExposure is greater than or equal to UPDATED_DAYS_OF_EXPOSURE
        defaultStudyProductShouldNotBeFound("daysOfExposure.greaterThanOrEqual=" + UPDATED_DAYS_OF_EXPOSURE);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDaysOfExposureIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where daysOfExposure is less than or equal to DEFAULT_DAYS_OF_EXPOSURE
        defaultStudyProductShouldBeFound("daysOfExposure.lessThanOrEqual=" + DEFAULT_DAYS_OF_EXPOSURE);

        // Get all the studyProductList where daysOfExposure is less than or equal to SMALLER_DAYS_OF_EXPOSURE
        defaultStudyProductShouldNotBeFound("daysOfExposure.lessThanOrEqual=" + SMALLER_DAYS_OF_EXPOSURE);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDaysOfExposureIsLessThanSomething() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where daysOfExposure is less than DEFAULT_DAYS_OF_EXPOSURE
        defaultStudyProductShouldNotBeFound("daysOfExposure.lessThan=" + DEFAULT_DAYS_OF_EXPOSURE);

        // Get all the studyProductList where daysOfExposure is less than UPDATED_DAYS_OF_EXPOSURE
        defaultStudyProductShouldBeFound("daysOfExposure.lessThan=" + UPDATED_DAYS_OF_EXPOSURE);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByDaysOfExposureIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where daysOfExposure is greater than DEFAULT_DAYS_OF_EXPOSURE
        defaultStudyProductShouldNotBeFound("daysOfExposure.greaterThan=" + DEFAULT_DAYS_OF_EXPOSURE);

        // Get all the studyProductList where daysOfExposure is greater than SMALLER_DAYS_OF_EXPOSURE
        defaultStudyProductShouldBeFound("daysOfExposure.greaterThan=" + SMALLER_DAYS_OF_EXPOSURE);
    }


    @Test
    @Transactional
    public void getAllStudyProductsByFormulationIsEqualToSomething() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where formulation equals to DEFAULT_FORMULATION
        defaultStudyProductShouldBeFound("formulation.equals=" + DEFAULT_FORMULATION);

        // Get all the studyProductList where formulation equals to UPDATED_FORMULATION
        defaultStudyProductShouldNotBeFound("formulation.equals=" + UPDATED_FORMULATION);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByFormulationIsInShouldWork() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where formulation in DEFAULT_FORMULATION or UPDATED_FORMULATION
        defaultStudyProductShouldBeFound("formulation.in=" + DEFAULT_FORMULATION + "," + UPDATED_FORMULATION);

        // Get all the studyProductList where formulation equals to UPDATED_FORMULATION
        defaultStudyProductShouldNotBeFound("formulation.in=" + UPDATED_FORMULATION);
    }

    @Test
    @Transactional
    public void getAllStudyProductsByFormulationIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyProductRepository.saveAndFlush(studyProduct);

        // Get all the studyProductList where formulation is not null
        defaultStudyProductShouldBeFound("formulation.specified=true");

        // Get all the studyProductList where formulation is null
        defaultStudyProductShouldNotBeFound("formulation.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudyProductsByStudyIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClinicalStudy study = studyProduct.getStudy();
        studyProductRepository.saveAndFlush(studyProduct);
        Long studyId = study.getId();

        // Get all the studyProductList where study equals to studyId
        defaultStudyProductShouldBeFound("studyId.equals=" + studyId);

        // Get all the studyProductList where study equals to studyId + 1
        defaultStudyProductShouldNotBeFound("studyId.equals=" + (studyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudyProductShouldBeFound(String filter) throws Exception {
        restStudyProductMockMvc.perform(get("/api/study-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].doseRange").value(hasItem(DEFAULT_DOSE_RANGE)))
            .andExpect(jsonPath("$.[*].daysOfExposure").value(hasItem(DEFAULT_DAYS_OF_EXPOSURE)))
            .andExpect(jsonPath("$.[*].formulation").value(hasItem(DEFAULT_FORMULATION)));

        // Check, that the count call also returns 1
        restStudyProductMockMvc.perform(get("/api/study-products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudyProductShouldNotBeFound(String filter) throws Exception {
        restStudyProductMockMvc.perform(get("/api/study-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudyProductMockMvc.perform(get("/api/study-products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStudyProduct() throws Exception {
        // Get the studyProduct
        restStudyProductMockMvc.perform(get("/api/study-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudyProduct() throws Exception {
        // Initialize the database
        studyProductService.save(studyProduct);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockStudyProductSearchRepository);

        int databaseSizeBeforeUpdate = studyProductRepository.findAll().size();

        // Update the studyProduct
        StudyProduct updatedStudyProduct = studyProductRepository.findById(studyProduct.getId()).get();
        // Disconnect from session so that the updates on updatedStudyProduct are not directly saved in db
        em.detach(updatedStudyProduct);
        updatedStudyProduct
            .productName(UPDATED_PRODUCT_NAME)
            .doseRange(UPDATED_DOSE_RANGE)
            .daysOfExposure(UPDATED_DAYS_OF_EXPOSURE)
            .formulation(UPDATED_FORMULATION);

        restStudyProductMockMvc.perform(put("/api/study-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudyProduct)))
            .andExpect(status().isOk());

        // Validate the StudyProduct in the database
        List<StudyProduct> studyProductList = studyProductRepository.findAll();
        assertThat(studyProductList).hasSize(databaseSizeBeforeUpdate);
        StudyProduct testStudyProduct = studyProductList.get(studyProductList.size() - 1);
        assertThat(testStudyProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testStudyProduct.getDoseRange()).isEqualTo(UPDATED_DOSE_RANGE);
        assertThat(testStudyProduct.getDaysOfExposure()).isEqualTo(UPDATED_DAYS_OF_EXPOSURE);
        assertThat(testStudyProduct.getFormulation()).isEqualTo(UPDATED_FORMULATION);

        // Validate the StudyProduct in Elasticsearch
        verify(mockStudyProductSearchRepository, times(1)).save(testStudyProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingStudyProduct() throws Exception {
        int databaseSizeBeforeUpdate = studyProductRepository.findAll().size();

        // Create the StudyProduct

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyProductMockMvc.perform(put("/api/study-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyProduct)))
            .andExpect(status().isBadRequest());

        // Validate the StudyProduct in the database
        List<StudyProduct> studyProductList = studyProductRepository.findAll();
        assertThat(studyProductList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StudyProduct in Elasticsearch
        verify(mockStudyProductSearchRepository, times(0)).save(studyProduct);
    }

    @Test
    @Transactional
    public void deleteStudyProduct() throws Exception {
        // Initialize the database
        studyProductService.save(studyProduct);

        int databaseSizeBeforeDelete = studyProductRepository.findAll().size();

        // Delete the studyProduct
        restStudyProductMockMvc.perform(delete("/api/study-products/{id}", studyProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudyProduct> studyProductList = studyProductRepository.findAll();
        assertThat(studyProductList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StudyProduct in Elasticsearch
        verify(mockStudyProductSearchRepository, times(1)).deleteById(studyProduct.getId());
    }

    @Test
    @Transactional
    public void searchStudyProduct() throws Exception {
        // Initialize the database
        studyProductService.save(studyProduct);
        when(mockStudyProductSearchRepository.search(queryStringQuery("id:" + studyProduct.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(studyProduct), PageRequest.of(0, 1), 1));
        // Search the studyProduct
        restStudyProductMockMvc.perform(get("/api/_search/study-products?query=id:" + studyProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].doseRange").value(hasItem(DEFAULT_DOSE_RANGE)))
            .andExpect(jsonPath("$.[*].daysOfExposure").value(hasItem(DEFAULT_DAYS_OF_EXPOSURE)))
            .andExpect(jsonPath("$.[*].formulation").value(hasItem(DEFAULT_FORMULATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyProduct.class);
        StudyProduct studyProduct1 = new StudyProduct();
        studyProduct1.setId(1L);
        StudyProduct studyProduct2 = new StudyProduct();
        studyProduct2.setId(studyProduct1.getId());
        assertThat(studyProduct1).isEqualTo(studyProduct2);
        studyProduct2.setId(2L);
        assertThat(studyProduct1).isNotEqualTo(studyProduct2);
        studyProduct1.setId(null);
        assertThat(studyProduct1).isNotEqualTo(studyProduct2);
    }
}

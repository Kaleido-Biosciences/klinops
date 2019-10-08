package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.Laboratory;
import com.kaleido.klinops.repository.LaboratoryRepository;
import com.kaleido.klinops.service.LaboratoryService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.LaboratoryCriteria;
import com.kaleido.klinops.service.LaboratoryQueryService;

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
 * Integration tests for the {@link LaboratoryResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class LaboratoryResourceIT {

    private static final String DEFAULT_LAB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP = "AAAAAAAAAA";
    private static final String UPDATED_ZIP = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_LAB_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_LAB_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LAB_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAB_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAB_CONTACT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LAB_CONTACT_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private LaboratoryQueryService laboratoryQueryService;

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

    private MockMvc restLaboratoryMockMvc;

    private Laboratory laboratory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LaboratoryResource laboratoryResource = new LaboratoryResource(laboratoryService, laboratoryQueryService);
        this.restLaboratoryMockMvc = MockMvcBuilders.standaloneSetup(laboratoryResource)
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
    public static Laboratory createEntity(EntityManager em) {
        Laboratory laboratory = new Laboratory()
            .labName(DEFAULT_LAB_NAME)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zip(DEFAULT_ZIP)
            .country(DEFAULT_COUNTRY)
            .labContactEmail(DEFAULT_LAB_CONTACT_EMAIL)
            .labContactName(DEFAULT_LAB_CONTACT_NAME)
            .labContactPhoneNumber(DEFAULT_LAB_CONTACT_PHONE_NUMBER);
        return laboratory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laboratory createUpdatedEntity(EntityManager em) {
        Laboratory laboratory = new Laboratory()
            .labName(UPDATED_LAB_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY)
            .labContactEmail(UPDATED_LAB_CONTACT_EMAIL)
            .labContactName(UPDATED_LAB_CONTACT_NAME)
            .labContactPhoneNumber(UPDATED_LAB_CONTACT_PHONE_NUMBER);
        return laboratory;
    }

    @BeforeEach
    public void initTest() {
        laboratory = createEntity(em);
    }

    @Test
    @Transactional
    public void createLaboratory() throws Exception {
        int databaseSizeBeforeCreate = laboratoryRepository.findAll().size();

        // Create the Laboratory
        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratory)))
            .andExpect(status().isCreated());

        // Validate the Laboratory in the database
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeCreate + 1);
        Laboratory testLaboratory = laboratoryList.get(laboratoryList.size() - 1);
        assertThat(testLaboratory.getLabName()).isEqualTo(DEFAULT_LAB_NAME);
        assertThat(testLaboratory.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testLaboratory.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testLaboratory.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testLaboratory.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testLaboratory.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testLaboratory.getLabContactEmail()).isEqualTo(DEFAULT_LAB_CONTACT_EMAIL);
        assertThat(testLaboratory.getLabContactName()).isEqualTo(DEFAULT_LAB_CONTACT_NAME);
        assertThat(testLaboratory.getLabContactPhoneNumber()).isEqualTo(DEFAULT_LAB_CONTACT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createLaboratoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = laboratoryRepository.findAll().size();

        // Create the Laboratory with an existing ID
        laboratory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratory)))
            .andExpect(status().isBadRequest());

        // Validate the Laboratory in the database
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratoryRepository.findAll().size();
        // set the field null
        laboratory.setLabName(null);

        // Create the Laboratory, which fails.

        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratory)))
            .andExpect(status().isBadRequest());

        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratoryRepository.findAll().size();
        // set the field null
        laboratory.setStreetAddress(null);

        // Create the Laboratory, which fails.

        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratory)))
            .andExpect(status().isBadRequest());

        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratoryRepository.findAll().size();
        // set the field null
        laboratory.setCity(null);

        // Create the Laboratory, which fails.

        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratory)))
            .andExpect(status().isBadRequest());

        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratoryRepository.findAll().size();
        // set the field null
        laboratory.setZip(null);

        // Create the Laboratory, which fails.

        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratory)))
            .andExpect(status().isBadRequest());

        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratoryRepository.findAll().size();
        // set the field null
        laboratory.setCountry(null);

        // Create the Laboratory, which fails.

        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratory)))
            .andExpect(status().isBadRequest());

        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLaboratories() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList
        restLaboratoryMockMvc.perform(get("/api/laboratories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laboratory.getId().intValue())))
            .andExpect(jsonPath("$.[*].labName").value(hasItem(DEFAULT_LAB_NAME.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].labContactEmail").value(hasItem(DEFAULT_LAB_CONTACT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].labContactName").value(hasItem(DEFAULT_LAB_CONTACT_NAME.toString())))
            .andExpect(jsonPath("$.[*].labContactPhoneNumber").value(hasItem(DEFAULT_LAB_CONTACT_PHONE_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getLaboratory() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get the laboratory
        restLaboratoryMockMvc.perform(get("/api/laboratories/{id}", laboratory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(laboratory.getId().intValue()))
            .andExpect(jsonPath("$.labName").value(DEFAULT_LAB_NAME.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.labContactEmail").value(DEFAULT_LAB_CONTACT_EMAIL.toString()))
            .andExpect(jsonPath("$.labContactName").value(DEFAULT_LAB_CONTACT_NAME.toString()))
            .andExpect(jsonPath("$.labContactPhoneNumber").value(DEFAULT_LAB_CONTACT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabNameIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labName equals to DEFAULT_LAB_NAME
        defaultLaboratoryShouldBeFound("labName.equals=" + DEFAULT_LAB_NAME);

        // Get all the laboratoryList where labName equals to UPDATED_LAB_NAME
        defaultLaboratoryShouldNotBeFound("labName.equals=" + UPDATED_LAB_NAME);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabNameIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labName in DEFAULT_LAB_NAME or UPDATED_LAB_NAME
        defaultLaboratoryShouldBeFound("labName.in=" + DEFAULT_LAB_NAME + "," + UPDATED_LAB_NAME);

        // Get all the laboratoryList where labName equals to UPDATED_LAB_NAME
        defaultLaboratoryShouldNotBeFound("labName.in=" + UPDATED_LAB_NAME);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labName is not null
        defaultLaboratoryShouldBeFound("labName.specified=true");

        // Get all the laboratoryList where labName is null
        defaultLaboratoryShouldNotBeFound("labName.specified=false");
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultLaboratoryShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the laboratoryList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultLaboratoryShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultLaboratoryShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the laboratoryList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultLaboratoryShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where streetAddress is not null
        defaultLaboratoryShouldBeFound("streetAddress.specified=true");

        // Get all the laboratoryList where streetAddress is null
        defaultLaboratoryShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where city equals to DEFAULT_CITY
        defaultLaboratoryShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the laboratoryList where city equals to UPDATED_CITY
        defaultLaboratoryShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where city in DEFAULT_CITY or UPDATED_CITY
        defaultLaboratoryShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the laboratoryList where city equals to UPDATED_CITY
        defaultLaboratoryShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where city is not null
        defaultLaboratoryShouldBeFound("city.specified=true");

        // Get all the laboratoryList where city is null
        defaultLaboratoryShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where state equals to DEFAULT_STATE
        defaultLaboratoryShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the laboratoryList where state equals to UPDATED_STATE
        defaultLaboratoryShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where state in DEFAULT_STATE or UPDATED_STATE
        defaultLaboratoryShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the laboratoryList where state equals to UPDATED_STATE
        defaultLaboratoryShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where state is not null
        defaultLaboratoryShouldBeFound("state.specified=true");

        // Get all the laboratoryList where state is null
        defaultLaboratoryShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByZipIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where zip equals to DEFAULT_ZIP
        defaultLaboratoryShouldBeFound("zip.equals=" + DEFAULT_ZIP);

        // Get all the laboratoryList where zip equals to UPDATED_ZIP
        defaultLaboratoryShouldNotBeFound("zip.equals=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByZipIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where zip in DEFAULT_ZIP or UPDATED_ZIP
        defaultLaboratoryShouldBeFound("zip.in=" + DEFAULT_ZIP + "," + UPDATED_ZIP);

        // Get all the laboratoryList where zip equals to UPDATED_ZIP
        defaultLaboratoryShouldNotBeFound("zip.in=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByZipIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where zip is not null
        defaultLaboratoryShouldBeFound("zip.specified=true");

        // Get all the laboratoryList where zip is null
        defaultLaboratoryShouldNotBeFound("zip.specified=false");
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where country equals to DEFAULT_COUNTRY
        defaultLaboratoryShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the laboratoryList where country equals to UPDATED_COUNTRY
        defaultLaboratoryShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultLaboratoryShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the laboratoryList where country equals to UPDATED_COUNTRY
        defaultLaboratoryShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where country is not null
        defaultLaboratoryShouldBeFound("country.specified=true");

        // Get all the laboratoryList where country is null
        defaultLaboratoryShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactEmail equals to DEFAULT_LAB_CONTACT_EMAIL
        defaultLaboratoryShouldBeFound("labContactEmail.equals=" + DEFAULT_LAB_CONTACT_EMAIL);

        // Get all the laboratoryList where labContactEmail equals to UPDATED_LAB_CONTACT_EMAIL
        defaultLaboratoryShouldNotBeFound("labContactEmail.equals=" + UPDATED_LAB_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactEmailIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactEmail in DEFAULT_LAB_CONTACT_EMAIL or UPDATED_LAB_CONTACT_EMAIL
        defaultLaboratoryShouldBeFound("labContactEmail.in=" + DEFAULT_LAB_CONTACT_EMAIL + "," + UPDATED_LAB_CONTACT_EMAIL);

        // Get all the laboratoryList where labContactEmail equals to UPDATED_LAB_CONTACT_EMAIL
        defaultLaboratoryShouldNotBeFound("labContactEmail.in=" + UPDATED_LAB_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactEmail is not null
        defaultLaboratoryShouldBeFound("labContactEmail.specified=true");

        // Get all the laboratoryList where labContactEmail is null
        defaultLaboratoryShouldNotBeFound("labContactEmail.specified=false");
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactName equals to DEFAULT_LAB_CONTACT_NAME
        defaultLaboratoryShouldBeFound("labContactName.equals=" + DEFAULT_LAB_CONTACT_NAME);

        // Get all the laboratoryList where labContactName equals to UPDATED_LAB_CONTACT_NAME
        defaultLaboratoryShouldNotBeFound("labContactName.equals=" + UPDATED_LAB_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactName in DEFAULT_LAB_CONTACT_NAME or UPDATED_LAB_CONTACT_NAME
        defaultLaboratoryShouldBeFound("labContactName.in=" + DEFAULT_LAB_CONTACT_NAME + "," + UPDATED_LAB_CONTACT_NAME);

        // Get all the laboratoryList where labContactName equals to UPDATED_LAB_CONTACT_NAME
        defaultLaboratoryShouldNotBeFound("labContactName.in=" + UPDATED_LAB_CONTACT_NAME);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactName is not null
        defaultLaboratoryShouldBeFound("labContactName.specified=true");

        // Get all the laboratoryList where labContactName is null
        defaultLaboratoryShouldNotBeFound("labContactName.specified=false");
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactPhoneNumber equals to DEFAULT_LAB_CONTACT_PHONE_NUMBER
        defaultLaboratoryShouldBeFound("labContactPhoneNumber.equals=" + DEFAULT_LAB_CONTACT_PHONE_NUMBER);

        // Get all the laboratoryList where labContactPhoneNumber equals to UPDATED_LAB_CONTACT_PHONE_NUMBER
        defaultLaboratoryShouldNotBeFound("labContactPhoneNumber.equals=" + UPDATED_LAB_CONTACT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactPhoneNumber in DEFAULT_LAB_CONTACT_PHONE_NUMBER or UPDATED_LAB_CONTACT_PHONE_NUMBER
        defaultLaboratoryShouldBeFound("labContactPhoneNumber.in=" + DEFAULT_LAB_CONTACT_PHONE_NUMBER + "," + UPDATED_LAB_CONTACT_PHONE_NUMBER);

        // Get all the laboratoryList where labContactPhoneNumber equals to UPDATED_LAB_CONTACT_PHONE_NUMBER
        defaultLaboratoryShouldNotBeFound("labContactPhoneNumber.in=" + UPDATED_LAB_CONTACT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLaboratoriesByLabContactPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        laboratoryRepository.saveAndFlush(laboratory);

        // Get all the laboratoryList where labContactPhoneNumber is not null
        defaultLaboratoryShouldBeFound("labContactPhoneNumber.specified=true");

        // Get all the laboratoryList where labContactPhoneNumber is null
        defaultLaboratoryShouldNotBeFound("labContactPhoneNumber.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLaboratoryShouldBeFound(String filter) throws Exception {
        restLaboratoryMockMvc.perform(get("/api/laboratories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laboratory.getId().intValue())))
            .andExpect(jsonPath("$.[*].labName").value(hasItem(DEFAULT_LAB_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].labContactEmail").value(hasItem(DEFAULT_LAB_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].labContactName").value(hasItem(DEFAULT_LAB_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].labContactPhoneNumber").value(hasItem(DEFAULT_LAB_CONTACT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restLaboratoryMockMvc.perform(get("/api/laboratories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLaboratoryShouldNotBeFound(String filter) throws Exception {
        restLaboratoryMockMvc.perform(get("/api/laboratories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLaboratoryMockMvc.perform(get("/api/laboratories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLaboratory() throws Exception {
        // Get the laboratory
        restLaboratoryMockMvc.perform(get("/api/laboratories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLaboratory() throws Exception {
        // Initialize the database
        laboratoryService.save(laboratory);

        int databaseSizeBeforeUpdate = laboratoryRepository.findAll().size();

        // Update the laboratory
        Laboratory updatedLaboratory = laboratoryRepository.findById(laboratory.getId()).get();
        // Disconnect from session so that the updates on updatedLaboratory are not directly saved in db
        em.detach(updatedLaboratory);
        updatedLaboratory
            .labName(UPDATED_LAB_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY)
            .labContactEmail(UPDATED_LAB_CONTACT_EMAIL)
            .labContactName(UPDATED_LAB_CONTACT_NAME)
            .labContactPhoneNumber(UPDATED_LAB_CONTACT_PHONE_NUMBER);

        restLaboratoryMockMvc.perform(put("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLaboratory)))
            .andExpect(status().isOk());

        // Validate the Laboratory in the database
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeUpdate);
        Laboratory testLaboratory = laboratoryList.get(laboratoryList.size() - 1);
        assertThat(testLaboratory.getLabName()).isEqualTo(UPDATED_LAB_NAME);
        assertThat(testLaboratory.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testLaboratory.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLaboratory.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testLaboratory.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testLaboratory.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testLaboratory.getLabContactEmail()).isEqualTo(UPDATED_LAB_CONTACT_EMAIL);
        assertThat(testLaboratory.getLabContactName()).isEqualTo(UPDATED_LAB_CONTACT_NAME);
        assertThat(testLaboratory.getLabContactPhoneNumber()).isEqualTo(UPDATED_LAB_CONTACT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingLaboratory() throws Exception {
        int databaseSizeBeforeUpdate = laboratoryRepository.findAll().size();

        // Create the Laboratory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratoryMockMvc.perform(put("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratory)))
            .andExpect(status().isBadRequest());

        // Validate the Laboratory in the database
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLaboratory() throws Exception {
        // Initialize the database
        laboratoryService.save(laboratory);

        int databaseSizeBeforeDelete = laboratoryRepository.findAll().size();

        // Delete the laboratory
        restLaboratoryMockMvc.perform(delete("/api/laboratories/{id}", laboratory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Laboratory.class);
        Laboratory laboratory1 = new Laboratory();
        laboratory1.setId(1L);
        Laboratory laboratory2 = new Laboratory();
        laboratory2.setId(laboratory1.getId());
        assertThat(laboratory1).isEqualTo(laboratory2);
        laboratory2.setId(2L);
        assertThat(laboratory1).isNotEqualTo(laboratory2);
        laboratory1.setId(null);
        assertThat(laboratory1).isNotEqualTo(laboratory2);
    }
}

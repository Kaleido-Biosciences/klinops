/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.PrincipalInvestigator;
import com.kaleido.klinops.domain.Site;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.PrincipalInvestigatorRepository;
import com.kaleido.klinops.service.PrincipalInvestigatorService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.PrincipalInvestigatorCriteria;
import com.kaleido.klinops.service.PrincipalInvestigatorQueryService;

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
 * Integration tests for the {@link PrincipalInvestigatorResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class PrincipalInvestigatorResourceIT {

    private static final String DEFAULT_INVESTIGATOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INVESTIGATOR_NAME = "BBBBBBBBBB";

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

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private PrincipalInvestigatorRepository principalInvestigatorRepository;

    @Autowired
    private PrincipalInvestigatorService principalInvestigatorService;

    @Autowired
    private PrincipalInvestigatorQueryService principalInvestigatorQueryService;

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

    private MockMvc restPrincipalInvestigatorMockMvc;

    private PrincipalInvestigator principalInvestigator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrincipalInvestigatorResource principalInvestigatorResource = new PrincipalInvestigatorResource(principalInvestigatorService, principalInvestigatorQueryService);
        this.restPrincipalInvestigatorMockMvc = MockMvcBuilders.standaloneSetup(principalInvestigatorResource)
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
    public static PrincipalInvestigator createEntity(EntityManager em) {
        PrincipalInvestigator principalInvestigator = new PrincipalInvestigator()
            .investigatorName(DEFAULT_INVESTIGATOR_NAME)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zip(DEFAULT_ZIP)
            .country(DEFAULT_COUNTRY)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        // Add required entity
        Site site;
        if (TestUtil.findAll(em, Site.class).isEmpty()) {
            site = SiteResourceIT.createEntity(em);
            em.persist(site);
            em.flush();
        } else {
            site = TestUtil.findAll(em, Site.class).get(0);
        }
        principalInvestigator.setSite(site);
        return principalInvestigator;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrincipalInvestigator createUpdatedEntity(EntityManager em) {
        PrincipalInvestigator principalInvestigator = new PrincipalInvestigator()
            .investigatorName(UPDATED_INVESTIGATOR_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        // Add required entity
        Site site;
        if (TestUtil.findAll(em, Site.class).isEmpty()) {
            site = SiteResourceIT.createUpdatedEntity(em);
            em.persist(site);
            em.flush();
        } else {
            site = TestUtil.findAll(em, Site.class).get(0);
        }
        principalInvestigator.setSite(site);
        return principalInvestigator;
    }

    @BeforeEach
    public void initTest() {
        principalInvestigator = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrincipalInvestigator() throws Exception {
        int databaseSizeBeforeCreate = principalInvestigatorRepository.findAll().size();

        // Create the PrincipalInvestigator
        restPrincipalInvestigatorMockMvc.perform(post("/api/principal-investigators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(principalInvestigator)))
            .andExpect(status().isCreated());

        // Validate the PrincipalInvestigator in the database
        List<PrincipalInvestigator> principalInvestigatorList = principalInvestigatorRepository.findAll();
        assertThat(principalInvestigatorList).hasSize(databaseSizeBeforeCreate + 1);
        PrincipalInvestigator testPrincipalInvestigator = principalInvestigatorList.get(principalInvestigatorList.size() - 1);
        assertThat(testPrincipalInvestigator.getInvestigatorName()).isEqualTo(DEFAULT_INVESTIGATOR_NAME);
        assertThat(testPrincipalInvestigator.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testPrincipalInvestigator.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPrincipalInvestigator.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testPrincipalInvestigator.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testPrincipalInvestigator.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testPrincipalInvestigator.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPrincipalInvestigator.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createPrincipalInvestigatorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = principalInvestigatorRepository.findAll().size();

        // Create the PrincipalInvestigator with an existing ID
        principalInvestigator.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrincipalInvestigatorMockMvc.perform(post("/api/principal-investigators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(principalInvestigator)))
            .andExpect(status().isBadRequest());

        // Validate the PrincipalInvestigator in the database
        List<PrincipalInvestigator> principalInvestigatorList = principalInvestigatorRepository.findAll();
        assertThat(principalInvestigatorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInvestigatorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = principalInvestigatorRepository.findAll().size();
        // set the field null
        principalInvestigator.setInvestigatorName(null);

        // Create the PrincipalInvestigator, which fails.

        restPrincipalInvestigatorMockMvc.perform(post("/api/principal-investigators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(principalInvestigator)))
            .andExpect(status().isBadRequest());

        List<PrincipalInvestigator> principalInvestigatorList = principalInvestigatorRepository.findAll();
        assertThat(principalInvestigatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigators() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList
        restPrincipalInvestigatorMockMvc.perform(get("/api/principal-investigators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(principalInvestigator.getId().intValue())))
            .andExpect(jsonPath("$.[*].investigatorName").value(hasItem(DEFAULT_INVESTIGATOR_NAME.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getPrincipalInvestigator() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get the principalInvestigator
        restPrincipalInvestigatorMockMvc.perform(get("/api/principal-investigators/{id}", principalInvestigator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(principalInvestigator.getId().intValue()))
            .andExpect(jsonPath("$.investigatorName").value(DEFAULT_INVESTIGATOR_NAME.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByInvestigatorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where investigatorName equals to DEFAULT_INVESTIGATOR_NAME
        defaultPrincipalInvestigatorShouldBeFound("investigatorName.equals=" + DEFAULT_INVESTIGATOR_NAME);

        // Get all the principalInvestigatorList where investigatorName equals to UPDATED_INVESTIGATOR_NAME
        defaultPrincipalInvestigatorShouldNotBeFound("investigatorName.equals=" + UPDATED_INVESTIGATOR_NAME);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByInvestigatorNameIsInShouldWork() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where investigatorName in DEFAULT_INVESTIGATOR_NAME or UPDATED_INVESTIGATOR_NAME
        defaultPrincipalInvestigatorShouldBeFound("investigatorName.in=" + DEFAULT_INVESTIGATOR_NAME + "," + UPDATED_INVESTIGATOR_NAME);

        // Get all the principalInvestigatorList where investigatorName equals to UPDATED_INVESTIGATOR_NAME
        defaultPrincipalInvestigatorShouldNotBeFound("investigatorName.in=" + UPDATED_INVESTIGATOR_NAME);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByInvestigatorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where investigatorName is not null
        defaultPrincipalInvestigatorShouldBeFound("investigatorName.specified=true");

        // Get all the principalInvestigatorList where investigatorName is null
        defaultPrincipalInvestigatorShouldNotBeFound("investigatorName.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultPrincipalInvestigatorShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the principalInvestigatorList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultPrincipalInvestigatorShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultPrincipalInvestigatorShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the principalInvestigatorList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultPrincipalInvestigatorShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where streetAddress is not null
        defaultPrincipalInvestigatorShouldBeFound("streetAddress.specified=true");

        // Get all the principalInvestigatorList where streetAddress is null
        defaultPrincipalInvestigatorShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where city equals to DEFAULT_CITY
        defaultPrincipalInvestigatorShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the principalInvestigatorList where city equals to UPDATED_CITY
        defaultPrincipalInvestigatorShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where city in DEFAULT_CITY or UPDATED_CITY
        defaultPrincipalInvestigatorShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the principalInvestigatorList where city equals to UPDATED_CITY
        defaultPrincipalInvestigatorShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where city is not null
        defaultPrincipalInvestigatorShouldBeFound("city.specified=true");

        // Get all the principalInvestigatorList where city is null
        defaultPrincipalInvestigatorShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where state equals to DEFAULT_STATE
        defaultPrincipalInvestigatorShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the principalInvestigatorList where state equals to UPDATED_STATE
        defaultPrincipalInvestigatorShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where state in DEFAULT_STATE or UPDATED_STATE
        defaultPrincipalInvestigatorShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the principalInvestigatorList where state equals to UPDATED_STATE
        defaultPrincipalInvestigatorShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where state is not null
        defaultPrincipalInvestigatorShouldBeFound("state.specified=true");

        // Get all the principalInvestigatorList where state is null
        defaultPrincipalInvestigatorShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByZipIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where zip equals to DEFAULT_ZIP
        defaultPrincipalInvestigatorShouldBeFound("zip.equals=" + DEFAULT_ZIP);

        // Get all the principalInvestigatorList where zip equals to UPDATED_ZIP
        defaultPrincipalInvestigatorShouldNotBeFound("zip.equals=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByZipIsInShouldWork() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where zip in DEFAULT_ZIP or UPDATED_ZIP
        defaultPrincipalInvestigatorShouldBeFound("zip.in=" + DEFAULT_ZIP + "," + UPDATED_ZIP);

        // Get all the principalInvestigatorList where zip equals to UPDATED_ZIP
        defaultPrincipalInvestigatorShouldNotBeFound("zip.in=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByZipIsNullOrNotNull() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where zip is not null
        defaultPrincipalInvestigatorShouldBeFound("zip.specified=true");

        // Get all the principalInvestigatorList where zip is null
        defaultPrincipalInvestigatorShouldNotBeFound("zip.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where country equals to DEFAULT_COUNTRY
        defaultPrincipalInvestigatorShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the principalInvestigatorList where country equals to UPDATED_COUNTRY
        defaultPrincipalInvestigatorShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultPrincipalInvestigatorShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the principalInvestigatorList where country equals to UPDATED_COUNTRY
        defaultPrincipalInvestigatorShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where country is not null
        defaultPrincipalInvestigatorShouldBeFound("country.specified=true");

        // Get all the principalInvestigatorList where country is null
        defaultPrincipalInvestigatorShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where email equals to DEFAULT_EMAIL
        defaultPrincipalInvestigatorShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the principalInvestigatorList where email equals to UPDATED_EMAIL
        defaultPrincipalInvestigatorShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPrincipalInvestigatorShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the principalInvestigatorList where email equals to UPDATED_EMAIL
        defaultPrincipalInvestigatorShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where email is not null
        defaultPrincipalInvestigatorShouldBeFound("email.specified=true");

        // Get all the principalInvestigatorList where email is null
        defaultPrincipalInvestigatorShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultPrincipalInvestigatorShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the principalInvestigatorList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPrincipalInvestigatorShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultPrincipalInvestigatorShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the principalInvestigatorList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPrincipalInvestigatorShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);

        // Get all the principalInvestigatorList where phoneNumber is not null
        defaultPrincipalInvestigatorShouldBeFound("phoneNumber.specified=true");

        // Get all the principalInvestigatorList where phoneNumber is null
        defaultPrincipalInvestigatorShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsBySiteIsEqualToSomething() throws Exception {
        // Get already existing entity
        Site site = principalInvestigator.getSite();
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);
        Long siteId = site.getId();

        // Get all the principalInvestigatorList where site equals to siteId
        defaultPrincipalInvestigatorShouldBeFound("siteId.equals=" + siteId);

        // Get all the principalInvestigatorList where site equals to siteId + 1
        defaultPrincipalInvestigatorShouldNotBeFound("siteId.equals=" + (siteId + 1));
    }


    @Test
    @Transactional
    public void getAllPrincipalInvestigatorsByStudiesIsEqualToSomething() throws Exception {
        // Initialize the database
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);
        ClinicalStudy studies = ClinicalStudyResourceIT.createEntity(em);
        em.persist(studies);
        em.flush();
        principalInvestigator.addStudies(studies);
        principalInvestigatorRepository.saveAndFlush(principalInvestigator);
        Long studiesId = studies.getId();

        // Get all the principalInvestigatorList where studies equals to studiesId
        defaultPrincipalInvestigatorShouldBeFound("studiesId.equals=" + studiesId);

        // Get all the principalInvestigatorList where studies equals to studiesId + 1
        defaultPrincipalInvestigatorShouldNotBeFound("studiesId.equals=" + (studiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrincipalInvestigatorShouldBeFound(String filter) throws Exception {
        restPrincipalInvestigatorMockMvc.perform(get("/api/principal-investigators?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(principalInvestigator.getId().intValue())))
            .andExpect(jsonPath("$.[*].investigatorName").value(hasItem(DEFAULT_INVESTIGATOR_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restPrincipalInvestigatorMockMvc.perform(get("/api/principal-investigators/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrincipalInvestigatorShouldNotBeFound(String filter) throws Exception {
        restPrincipalInvestigatorMockMvc.perform(get("/api/principal-investigators?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrincipalInvestigatorMockMvc.perform(get("/api/principal-investigators/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPrincipalInvestigator() throws Exception {
        // Get the principalInvestigator
        restPrincipalInvestigatorMockMvc.perform(get("/api/principal-investigators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrincipalInvestigator() throws Exception {
        // Initialize the database
        principalInvestigatorService.save(principalInvestigator);

        int databaseSizeBeforeUpdate = principalInvestigatorRepository.findAll().size();

        // Update the principalInvestigator
        PrincipalInvestigator updatedPrincipalInvestigator = principalInvestigatorRepository.findById(principalInvestigator.getId()).get();
        // Disconnect from session so that the updates on updatedPrincipalInvestigator are not directly saved in db
        em.detach(updatedPrincipalInvestigator);
        updatedPrincipalInvestigator
            .investigatorName(UPDATED_INVESTIGATOR_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restPrincipalInvestigatorMockMvc.perform(put("/api/principal-investigators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrincipalInvestigator)))
            .andExpect(status().isOk());

        // Validate the PrincipalInvestigator in the database
        List<PrincipalInvestigator> principalInvestigatorList = principalInvestigatorRepository.findAll();
        assertThat(principalInvestigatorList).hasSize(databaseSizeBeforeUpdate);
        PrincipalInvestigator testPrincipalInvestigator = principalInvestigatorList.get(principalInvestigatorList.size() - 1);
        assertThat(testPrincipalInvestigator.getInvestigatorName()).isEqualTo(UPDATED_INVESTIGATOR_NAME);
        assertThat(testPrincipalInvestigator.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testPrincipalInvestigator.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPrincipalInvestigator.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPrincipalInvestigator.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testPrincipalInvestigator.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPrincipalInvestigator.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPrincipalInvestigator.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingPrincipalInvestigator() throws Exception {
        int databaseSizeBeforeUpdate = principalInvestigatorRepository.findAll().size();

        // Create the PrincipalInvestigator

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrincipalInvestigatorMockMvc.perform(put("/api/principal-investigators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(principalInvestigator)))
            .andExpect(status().isBadRequest());

        // Validate the PrincipalInvestigator in the database
        List<PrincipalInvestigator> principalInvestigatorList = principalInvestigatorRepository.findAll();
        assertThat(principalInvestigatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePrincipalInvestigator() throws Exception {
        // Initialize the database
        principalInvestigatorService.save(principalInvestigator);

        int databaseSizeBeforeDelete = principalInvestigatorRepository.findAll().size();

        // Delete the principalInvestigator
        restPrincipalInvestigatorMockMvc.perform(delete("/api/principal-investigators/{id}", principalInvestigator.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrincipalInvestigator> principalInvestigatorList = principalInvestigatorRepository.findAll();
        assertThat(principalInvestigatorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrincipalInvestigator.class);
        PrincipalInvestigator principalInvestigator1 = new PrincipalInvestigator();
        principalInvestigator1.setId(1L);
        PrincipalInvestigator principalInvestigator2 = new PrincipalInvestigator();
        principalInvestigator2.setId(principalInvestigator1.getId());
        assertThat(principalInvestigator1).isEqualTo(principalInvestigator2);
        principalInvestigator2.setId(2L);
        assertThat(principalInvestigator1).isNotEqualTo(principalInvestigator2);
        principalInvestigator1.setId(null);
        assertThat(principalInvestigator1).isNotEqualTo(principalInvestigator2);
    }
}

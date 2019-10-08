package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.Site;
import com.kaleido.klinops.domain.PrincipalInvestigator;
import com.kaleido.klinops.repository.SiteRepository;
import com.kaleido.klinops.service.SiteService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.SiteCriteria;
import com.kaleido.klinops.service.SiteQueryService;

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
 * Integration tests for the {@link SiteResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class SiteResourceIT {

    private static final String DEFAULT_SITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SITE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION = "BBBBBBBBBB";

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

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteService siteService;

    @Autowired
    private SiteQueryService siteQueryService;

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

    private MockMvc restSiteMockMvc;

    private Site site;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SiteResource siteResource = new SiteResource(siteService, siteQueryService);
        this.restSiteMockMvc = MockMvcBuilders.standaloneSetup(siteResource)
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
    public static Site createEntity(EntityManager em) {
        Site site = new Site()
            .siteName(DEFAULT_SITE_NAME)
            .institution(DEFAULT_INSTITUTION)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zip(DEFAULT_ZIP)
            .country(DEFAULT_COUNTRY);
        return site;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity(EntityManager em) {
        Site site = new Site()
            .siteName(UPDATED_SITE_NAME)
            .institution(UPDATED_INSTITUTION)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY);
        return site;
    }

    @BeforeEach
    public void initTest() {
        site = createEntity(em);
    }

    @Test
    @Transactional
    public void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().size();

        // Create the Site
        restSiteMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isCreated());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getSiteName()).isEqualTo(DEFAULT_SITE_NAME);
        assertThat(testSite.getInstitution()).isEqualTo(DEFAULT_INSTITUTION);
        assertThat(testSite.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testSite.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testSite.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSite.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testSite.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void createSiteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().size();

        // Create the Site with an existing ID
        site.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSiteNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setSiteName(null);

        // Create the Site, which fails.

        restSiteMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInstitutionIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setInstitution(null);

        // Create the Site, which fails.

        restSiteMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setStreetAddress(null);

        // Create the Site, which fails.

        restSiteMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setCity(null);

        // Create the Site, which fails.

        restSiteMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setZip(null);

        // Create the Site, which fails.

        restSiteMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setCountry(null);

        // Create the Site, which fails.

        restSiteMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSites() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList
        restSiteMockMvc.perform(get("/api/sites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME.toString())))
            .andExpect(jsonPath("$.[*].institution").value(hasItem(DEFAULT_INSTITUTION.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }
    
    @Test
    @Transactional
    public void getSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get the site
        restSiteMockMvc.perform(get("/api/sites/{id}", site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(site.getId().intValue()))
            .andExpect(jsonPath("$.siteName").value(DEFAULT_SITE_NAME.toString()))
            .andExpect(jsonPath("$.institution").value(DEFAULT_INSTITUTION.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getAllSitesBySiteNameIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where siteName equals to DEFAULT_SITE_NAME
        defaultSiteShouldBeFound("siteName.equals=" + DEFAULT_SITE_NAME);

        // Get all the siteList where siteName equals to UPDATED_SITE_NAME
        defaultSiteShouldNotBeFound("siteName.equals=" + UPDATED_SITE_NAME);
    }

    @Test
    @Transactional
    public void getAllSitesBySiteNameIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where siteName in DEFAULT_SITE_NAME or UPDATED_SITE_NAME
        defaultSiteShouldBeFound("siteName.in=" + DEFAULT_SITE_NAME + "," + UPDATED_SITE_NAME);

        // Get all the siteList where siteName equals to UPDATED_SITE_NAME
        defaultSiteShouldNotBeFound("siteName.in=" + UPDATED_SITE_NAME);
    }

    @Test
    @Transactional
    public void getAllSitesBySiteNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where siteName is not null
        defaultSiteShouldBeFound("siteName.specified=true");

        // Get all the siteList where siteName is null
        defaultSiteShouldNotBeFound("siteName.specified=false");
    }

    @Test
    @Transactional
    public void getAllSitesByInstitutionIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where institution equals to DEFAULT_INSTITUTION
        defaultSiteShouldBeFound("institution.equals=" + DEFAULT_INSTITUTION);

        // Get all the siteList where institution equals to UPDATED_INSTITUTION
        defaultSiteShouldNotBeFound("institution.equals=" + UPDATED_INSTITUTION);
    }

    @Test
    @Transactional
    public void getAllSitesByInstitutionIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where institution in DEFAULT_INSTITUTION or UPDATED_INSTITUTION
        defaultSiteShouldBeFound("institution.in=" + DEFAULT_INSTITUTION + "," + UPDATED_INSTITUTION);

        // Get all the siteList where institution equals to UPDATED_INSTITUTION
        defaultSiteShouldNotBeFound("institution.in=" + UPDATED_INSTITUTION);
    }

    @Test
    @Transactional
    public void getAllSitesByInstitutionIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where institution is not null
        defaultSiteShouldBeFound("institution.specified=true");

        // Get all the siteList where institution is null
        defaultSiteShouldNotBeFound("institution.specified=false");
    }

    @Test
    @Transactional
    public void getAllSitesByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultSiteShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the siteList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultSiteShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSitesByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultSiteShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the siteList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultSiteShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSitesByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where streetAddress is not null
        defaultSiteShouldBeFound("streetAddress.specified=true");

        // Get all the siteList where streetAddress is null
        defaultSiteShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllSitesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where city equals to DEFAULT_CITY
        defaultSiteShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the siteList where city equals to UPDATED_CITY
        defaultSiteShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllSitesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where city in DEFAULT_CITY or UPDATED_CITY
        defaultSiteShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the siteList where city equals to UPDATED_CITY
        defaultSiteShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllSitesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where city is not null
        defaultSiteShouldBeFound("city.specified=true");

        // Get all the siteList where city is null
        defaultSiteShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllSitesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where state equals to DEFAULT_STATE
        defaultSiteShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the siteList where state equals to UPDATED_STATE
        defaultSiteShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllSitesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where state in DEFAULT_STATE or UPDATED_STATE
        defaultSiteShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the siteList where state equals to UPDATED_STATE
        defaultSiteShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllSitesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where state is not null
        defaultSiteShouldBeFound("state.specified=true");

        // Get all the siteList where state is null
        defaultSiteShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllSitesByZipIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where zip equals to DEFAULT_ZIP
        defaultSiteShouldBeFound("zip.equals=" + DEFAULT_ZIP);

        // Get all the siteList where zip equals to UPDATED_ZIP
        defaultSiteShouldNotBeFound("zip.equals=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllSitesByZipIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where zip in DEFAULT_ZIP or UPDATED_ZIP
        defaultSiteShouldBeFound("zip.in=" + DEFAULT_ZIP + "," + UPDATED_ZIP);

        // Get all the siteList where zip equals to UPDATED_ZIP
        defaultSiteShouldNotBeFound("zip.in=" + UPDATED_ZIP);
    }

    @Test
    @Transactional
    public void getAllSitesByZipIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where zip is not null
        defaultSiteShouldBeFound("zip.specified=true");

        // Get all the siteList where zip is null
        defaultSiteShouldNotBeFound("zip.specified=false");
    }

    @Test
    @Transactional
    public void getAllSitesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where country equals to DEFAULT_COUNTRY
        defaultSiteShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the siteList where country equals to UPDATED_COUNTRY
        defaultSiteShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllSitesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultSiteShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the siteList where country equals to UPDATED_COUNTRY
        defaultSiteShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllSitesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where country is not null
        defaultSiteShouldBeFound("country.specified=true");

        // Get all the siteList where country is null
        defaultSiteShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllSitesByInvestigatorsIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);
        PrincipalInvestigator investigators = PrincipalInvestigatorResourceIT.createEntity(em);
        em.persist(investigators);
        em.flush();
        site.addInvestigators(investigators);
        siteRepository.saveAndFlush(site);
        Long investigatorsId = investigators.getId();

        // Get all the siteList where investigators equals to investigatorsId
        defaultSiteShouldBeFound("investigatorsId.equals=" + investigatorsId);

        // Get all the siteList where investigators equals to investigatorsId + 1
        defaultSiteShouldNotBeFound("investigatorsId.equals=" + (investigatorsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSiteShouldBeFound(String filter) throws Exception {
        restSiteMockMvc.perform(get("/api/sites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME)))
            .andExpect(jsonPath("$.[*].institution").value(hasItem(DEFAULT_INSTITUTION)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));

        // Check, that the count call also returns 1
        restSiteMockMvc.perform(get("/api/sites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSiteShouldNotBeFound(String filter) throws Exception {
        restSiteMockMvc.perform(get("/api/sites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSiteMockMvc.perform(get("/api/sites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSite() throws Exception {
        // Get the site
        restSiteMockMvc.perform(get("/api/sites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSite() throws Exception {
        // Initialize the database
        siteService.save(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).get();
        // Disconnect from session so that the updates on updatedSite are not directly saved in db
        em.detach(updatedSite);
        updatedSite
            .siteName(UPDATED_SITE_NAME)
            .institution(UPDATED_INSTITUTION)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY);

        restSiteMockMvc.perform(put("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSite)))
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getSiteName()).isEqualTo(UPDATED_SITE_NAME);
        assertThat(testSite.getInstitution()).isEqualTo(UPDATED_INSTITUTION);
        assertThat(testSite.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testSite.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSite.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSite.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testSite.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void updateNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Create the Site

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc.perform(put("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSite() throws Exception {
        // Initialize the database
        siteService.save(site);

        int databaseSizeBeforeDelete = siteRepository.findAll().size();

        // Delete the site
        restSiteMockMvc.perform(delete("/api/sites/{id}", site.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Site.class);
        Site site1 = new Site();
        site1.setId(1L);
        Site site2 = new Site();
        site2.setId(site1.getId());
        assertThat(site1).isEqualTo(site2);
        site2.setId(2L);
        assertThat(site1).isNotEqualTo(site2);
        site1.setId(null);
        assertThat(site1).isNotEqualTo(site2);
    }
}

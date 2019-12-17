package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.ShipmentComponent;
import com.kaleido.klinops.domain.Shipment;
import com.kaleido.klinops.repository.ShipmentComponentRepository;
import com.kaleido.klinops.repository.search.ShipmentComponentSearchRepository;
import com.kaleido.klinops.service.ShipmentComponentService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.ShipmentComponentCriteria;
import com.kaleido.klinops.service.ShipmentComponentQueryService;

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
 * Integration tests for the {@link ShipmentComponentResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class ShipmentComponentResourceIT {

    private static final String DEFAULT_SAMPLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SAMPLE_COUNT = 1;
    private static final Integer UPDATED_SAMPLE_COUNT = 2;
    private static final Integer SMALLER_SAMPLE_COUNT = 1 - 1;

    @Autowired
    private ShipmentComponentRepository shipmentComponentRepository;

    @Autowired
    private ShipmentComponentService shipmentComponentService;

    /**
     * This repository is mocked in the com.kaleido.klinops.repository.search test package.
     *
     * @see com.kaleido.klinops.repository.search.ShipmentComponentSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShipmentComponentSearchRepository mockShipmentComponentSearchRepository;

    @Autowired
    private ShipmentComponentQueryService shipmentComponentQueryService;

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

    private MockMvc restShipmentComponentMockMvc;

    private ShipmentComponent shipmentComponent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShipmentComponentResource shipmentComponentResource = new ShipmentComponentResource(shipmentComponentService, shipmentComponentQueryService);
        this.restShipmentComponentMockMvc = MockMvcBuilders.standaloneSetup(shipmentComponentResource)
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
    public static ShipmentComponent createEntity(EntityManager em) {
        ShipmentComponent shipmentComponent = new ShipmentComponent()
            .sampleType(DEFAULT_SAMPLE_TYPE)
            .sampleCount(DEFAULT_SAMPLE_COUNT);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createEntity(em);
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).get(0);
        }
        shipmentComponent.setShipment(shipment);
        return shipmentComponent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentComponent createUpdatedEntity(EntityManager em) {
        ShipmentComponent shipmentComponent = new ShipmentComponent()
            .sampleType(UPDATED_SAMPLE_TYPE)
            .sampleCount(UPDATED_SAMPLE_COUNT);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createUpdatedEntity(em);
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).get(0);
        }
        shipmentComponent.setShipment(shipment);
        return shipmentComponent;
    }

    @BeforeEach
    public void initTest() {
        shipmentComponent = createEntity(em);
    }

    @Test
    @Transactional
    public void createShipmentComponent() throws Exception {
        int databaseSizeBeforeCreate = shipmentComponentRepository.findAll().size();

        // Create the ShipmentComponent
        restShipmentComponentMockMvc.perform(post("/api/shipment-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentComponent)))
            .andExpect(status().isCreated());

        // Validate the ShipmentComponent in the database
        List<ShipmentComponent> shipmentComponentList = shipmentComponentRepository.findAll();
        assertThat(shipmentComponentList).hasSize(databaseSizeBeforeCreate + 1);
        ShipmentComponent testShipmentComponent = shipmentComponentList.get(shipmentComponentList.size() - 1);
        assertThat(testShipmentComponent.getSampleType()).isEqualTo(DEFAULT_SAMPLE_TYPE);
        assertThat(testShipmentComponent.getSampleCount()).isEqualTo(DEFAULT_SAMPLE_COUNT);

        // Validate the ShipmentComponent in Elasticsearch
        verify(mockShipmentComponentSearchRepository, times(1)).save(testShipmentComponent);
    }

    @Test
    @Transactional
    public void createShipmentComponentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shipmentComponentRepository.findAll().size();

        // Create the ShipmentComponent with an existing ID
        shipmentComponent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentComponentMockMvc.perform(post("/api/shipment-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentComponent)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentComponent in the database
        List<ShipmentComponent> shipmentComponentList = shipmentComponentRepository.findAll();
        assertThat(shipmentComponentList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShipmentComponent in Elasticsearch
        verify(mockShipmentComponentSearchRepository, times(0)).save(shipmentComponent);
    }


    @Test
    @Transactional
    public void checkSampleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentComponentRepository.findAll().size();
        // set the field null
        shipmentComponent.setSampleType(null);

        // Create the ShipmentComponent, which fails.

        restShipmentComponentMockMvc.perform(post("/api/shipment-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentComponent)))
            .andExpect(status().isBadRequest());

        List<ShipmentComponent> shipmentComponentList = shipmentComponentRepository.findAll();
        assertThat(shipmentComponentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentComponentRepository.findAll().size();
        // set the field null
        shipmentComponent.setSampleCount(null);

        // Create the ShipmentComponent, which fails.

        restShipmentComponentMockMvc.perform(post("/api/shipment-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentComponent)))
            .andExpect(status().isBadRequest());

        List<ShipmentComponent> shipmentComponentList = shipmentComponentRepository.findAll();
        assertThat(shipmentComponentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShipmentComponents() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList
        restShipmentComponentMockMvc.perform(get("/api/shipment-components?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentComponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleType").value(hasItem(DEFAULT_SAMPLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sampleCount").value(hasItem(DEFAULT_SAMPLE_COUNT)));
    }
    
    @Test
    @Transactional
    public void getShipmentComponent() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get the shipmentComponent
        restShipmentComponentMockMvc.perform(get("/api/shipment-components/{id}", shipmentComponent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentComponent.getId().intValue()))
            .andExpect(jsonPath("$.sampleType").value(DEFAULT_SAMPLE_TYPE.toString()))
            .andExpect(jsonPath("$.sampleCount").value(DEFAULT_SAMPLE_COUNT));
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleType equals to DEFAULT_SAMPLE_TYPE
        defaultShipmentComponentShouldBeFound("sampleType.equals=" + DEFAULT_SAMPLE_TYPE);

        // Get all the shipmentComponentList where sampleType equals to UPDATED_SAMPLE_TYPE
        defaultShipmentComponentShouldNotBeFound("sampleType.equals=" + UPDATED_SAMPLE_TYPE);
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleTypeIsInShouldWork() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleType in DEFAULT_SAMPLE_TYPE or UPDATED_SAMPLE_TYPE
        defaultShipmentComponentShouldBeFound("sampleType.in=" + DEFAULT_SAMPLE_TYPE + "," + UPDATED_SAMPLE_TYPE);

        // Get all the shipmentComponentList where sampleType equals to UPDATED_SAMPLE_TYPE
        defaultShipmentComponentShouldNotBeFound("sampleType.in=" + UPDATED_SAMPLE_TYPE);
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleType is not null
        defaultShipmentComponentShouldBeFound("sampleType.specified=true");

        // Get all the shipmentComponentList where sampleType is null
        defaultShipmentComponentShouldNotBeFound("sampleType.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleCountIsEqualToSomething() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleCount equals to DEFAULT_SAMPLE_COUNT
        defaultShipmentComponentShouldBeFound("sampleCount.equals=" + DEFAULT_SAMPLE_COUNT);

        // Get all the shipmentComponentList where sampleCount equals to UPDATED_SAMPLE_COUNT
        defaultShipmentComponentShouldNotBeFound("sampleCount.equals=" + UPDATED_SAMPLE_COUNT);
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleCountIsInShouldWork() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleCount in DEFAULT_SAMPLE_COUNT or UPDATED_SAMPLE_COUNT
        defaultShipmentComponentShouldBeFound("sampleCount.in=" + DEFAULT_SAMPLE_COUNT + "," + UPDATED_SAMPLE_COUNT);

        // Get all the shipmentComponentList where sampleCount equals to UPDATED_SAMPLE_COUNT
        defaultShipmentComponentShouldNotBeFound("sampleCount.in=" + UPDATED_SAMPLE_COUNT);
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleCount is not null
        defaultShipmentComponentShouldBeFound("sampleCount.specified=true");

        // Get all the shipmentComponentList where sampleCount is null
        defaultShipmentComponentShouldNotBeFound("sampleCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleCount is greater than or equal to DEFAULT_SAMPLE_COUNT
        defaultShipmentComponentShouldBeFound("sampleCount.greaterThanOrEqual=" + DEFAULT_SAMPLE_COUNT);

        // Get all the shipmentComponentList where sampleCount is greater than or equal to UPDATED_SAMPLE_COUNT
        defaultShipmentComponentShouldNotBeFound("sampleCount.greaterThanOrEqual=" + UPDATED_SAMPLE_COUNT);
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleCount is less than or equal to DEFAULT_SAMPLE_COUNT
        defaultShipmentComponentShouldBeFound("sampleCount.lessThanOrEqual=" + DEFAULT_SAMPLE_COUNT);

        // Get all the shipmentComponentList where sampleCount is less than or equal to SMALLER_SAMPLE_COUNT
        defaultShipmentComponentShouldNotBeFound("sampleCount.lessThanOrEqual=" + SMALLER_SAMPLE_COUNT);
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleCountIsLessThanSomething() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleCount is less than DEFAULT_SAMPLE_COUNT
        defaultShipmentComponentShouldNotBeFound("sampleCount.lessThan=" + DEFAULT_SAMPLE_COUNT);

        // Get all the shipmentComponentList where sampleCount is less than UPDATED_SAMPLE_COUNT
        defaultShipmentComponentShouldBeFound("sampleCount.lessThan=" + UPDATED_SAMPLE_COUNT);
    }

    @Test
    @Transactional
    public void getAllShipmentComponentsBySampleCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shipmentComponentRepository.saveAndFlush(shipmentComponent);

        // Get all the shipmentComponentList where sampleCount is greater than DEFAULT_SAMPLE_COUNT
        defaultShipmentComponentShouldNotBeFound("sampleCount.greaterThan=" + DEFAULT_SAMPLE_COUNT);

        // Get all the shipmentComponentList where sampleCount is greater than SMALLER_SAMPLE_COUNT
        defaultShipmentComponentShouldBeFound("sampleCount.greaterThan=" + SMALLER_SAMPLE_COUNT);
    }


    @Test
    @Transactional
    public void getAllShipmentComponentsByShipmentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Shipment shipment = shipmentComponent.getShipment();
        shipmentComponentRepository.saveAndFlush(shipmentComponent);
        Long shipmentId = shipment.getId();

        // Get all the shipmentComponentList where shipment equals to shipmentId
        defaultShipmentComponentShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the shipmentComponentList where shipment equals to shipmentId + 1
        defaultShipmentComponentShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentComponentShouldBeFound(String filter) throws Exception {
        restShipmentComponentMockMvc.perform(get("/api/shipment-components?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentComponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleType").value(hasItem(DEFAULT_SAMPLE_TYPE)))
            .andExpect(jsonPath("$.[*].sampleCount").value(hasItem(DEFAULT_SAMPLE_COUNT)));

        // Check, that the count call also returns 1
        restShipmentComponentMockMvc.perform(get("/api/shipment-components/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentComponentShouldNotBeFound(String filter) throws Exception {
        restShipmentComponentMockMvc.perform(get("/api/shipment-components?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentComponentMockMvc.perform(get("/api/shipment-components/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShipmentComponent() throws Exception {
        // Get the shipmentComponent
        restShipmentComponentMockMvc.perform(get("/api/shipment-components/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShipmentComponent() throws Exception {
        // Initialize the database
        shipmentComponentService.save(shipmentComponent);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockShipmentComponentSearchRepository);

        int databaseSizeBeforeUpdate = shipmentComponentRepository.findAll().size();

        // Update the shipmentComponent
        ShipmentComponent updatedShipmentComponent = shipmentComponentRepository.findById(shipmentComponent.getId()).get();
        // Disconnect from session so that the updates on updatedShipmentComponent are not directly saved in db
        em.detach(updatedShipmentComponent);
        updatedShipmentComponent
            .sampleType(UPDATED_SAMPLE_TYPE)
            .sampleCount(UPDATED_SAMPLE_COUNT);

        restShipmentComponentMockMvc.perform(put("/api/shipment-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShipmentComponent)))
            .andExpect(status().isOk());

        // Validate the ShipmentComponent in the database
        List<ShipmentComponent> shipmentComponentList = shipmentComponentRepository.findAll();
        assertThat(shipmentComponentList).hasSize(databaseSizeBeforeUpdate);
        ShipmentComponent testShipmentComponent = shipmentComponentList.get(shipmentComponentList.size() - 1);
        assertThat(testShipmentComponent.getSampleType()).isEqualTo(UPDATED_SAMPLE_TYPE);
        assertThat(testShipmentComponent.getSampleCount()).isEqualTo(UPDATED_SAMPLE_COUNT);

        // Validate the ShipmentComponent in Elasticsearch
        verify(mockShipmentComponentSearchRepository, times(1)).save(testShipmentComponent);
    }

    @Test
    @Transactional
    public void updateNonExistingShipmentComponent() throws Exception {
        int databaseSizeBeforeUpdate = shipmentComponentRepository.findAll().size();

        // Create the ShipmentComponent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentComponentMockMvc.perform(put("/api/shipment-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentComponent)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentComponent in the database
        List<ShipmentComponent> shipmentComponentList = shipmentComponentRepository.findAll();
        assertThat(shipmentComponentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShipmentComponent in Elasticsearch
        verify(mockShipmentComponentSearchRepository, times(0)).save(shipmentComponent);
    }

    @Test
    @Transactional
    public void deleteShipmentComponent() throws Exception {
        // Initialize the database
        shipmentComponentService.save(shipmentComponent);

        int databaseSizeBeforeDelete = shipmentComponentRepository.findAll().size();

        // Delete the shipmentComponent
        restShipmentComponentMockMvc.perform(delete("/api/shipment-components/{id}", shipmentComponent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShipmentComponent> shipmentComponentList = shipmentComponentRepository.findAll();
        assertThat(shipmentComponentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShipmentComponent in Elasticsearch
        verify(mockShipmentComponentSearchRepository, times(1)).deleteById(shipmentComponent.getId());
    }

    @Test
    @Transactional
    public void searchShipmentComponent() throws Exception {
        // Initialize the database
        shipmentComponentService.save(shipmentComponent);
        when(mockShipmentComponentSearchRepository.search(queryStringQuery("id:" + shipmentComponent.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(shipmentComponent), PageRequest.of(0, 1), 1));
        // Search the shipmentComponent
        restShipmentComponentMockMvc.perform(get("/api/_search/shipment-components?query=id:" + shipmentComponent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentComponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleType").value(hasItem(DEFAULT_SAMPLE_TYPE)))
            .andExpect(jsonPath("$.[*].sampleCount").value(hasItem(DEFAULT_SAMPLE_COUNT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentComponent.class);
        ShipmentComponent shipmentComponent1 = new ShipmentComponent();
        shipmentComponent1.setId(1L);
        ShipmentComponent shipmentComponent2 = new ShipmentComponent();
        shipmentComponent2.setId(shipmentComponent1.getId());
        assertThat(shipmentComponent1).isEqualTo(shipmentComponent2);
        shipmentComponent2.setId(2L);
        assertThat(shipmentComponent1).isNotEqualTo(shipmentComponent2);
        shipmentComponent1.setId(null);
        assertThat(shipmentComponent1).isNotEqualTo(shipmentComponent2);
    }
}

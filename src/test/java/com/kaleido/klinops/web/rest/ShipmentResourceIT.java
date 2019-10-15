/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.web.rest;

import com.kaleido.klinops.KlinopsApp;
import com.kaleido.klinops.config.TestSecurityConfiguration;
import com.kaleido.klinops.domain.Shipment;
import com.kaleido.klinops.domain.ShipmentComponent;
import com.kaleido.klinops.domain.Laboratory;
import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.repository.ShipmentRepository;
import com.kaleido.klinops.service.ShipmentService;
import com.kaleido.klinops.web.rest.errors.ExceptionTranslator;
import com.kaleido.klinops.service.dto.ShipmentCriteria;
import com.kaleido.klinops.service.ShipmentQueryService;

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
 * Integration tests for the {@link ShipmentResource} REST controller.
 */
@SpringBootTest(classes = {KlinopsApp.class, TestSecurityConfiguration.class})
public class ShipmentResourceIT {

    private static final String DEFAULT_SHIPMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SHIPMENT_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_SHIPPED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SHIPPED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_SHIPPED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_RECEIVED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEIVED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_RECEIVED = LocalDate.ofEpochDay(-1L);

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShipmentQueryService shipmentQueryService;

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

    private MockMvc restShipmentMockMvc;

    private Shipment shipment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShipmentResource shipmentResource = new ShipmentResource(shipmentService, shipmentQueryService);
        this.restShipmentMockMvc = MockMvcBuilders.standaloneSetup(shipmentResource)
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
    public static Shipment createEntity(EntityManager em) {
        Shipment shipment = new Shipment()
            .shipmentCode(DEFAULT_SHIPMENT_CODE)
            .dateShipped(DEFAULT_DATE_SHIPPED)
            .dateReceived(DEFAULT_DATE_RECEIVED);
        // Add required entity
        Laboratory laboratory;
        if (TestUtil.findAll(em, Laboratory.class).isEmpty()) {
            laboratory = LaboratoryResourceIT.createEntity(em);
            em.persist(laboratory);
            em.flush();
        } else {
            laboratory = TestUtil.findAll(em, Laboratory.class).get(0);
        }
        shipment.setDestination(laboratory);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        shipment.setStudy(clinicalStudy);
        return shipment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipment createUpdatedEntity(EntityManager em) {
        Shipment shipment = new Shipment()
            .shipmentCode(UPDATED_SHIPMENT_CODE)
            .dateShipped(UPDATED_DATE_SHIPPED)
            .dateReceived(UPDATED_DATE_RECEIVED);
        // Add required entity
        Laboratory laboratory;
        if (TestUtil.findAll(em, Laboratory.class).isEmpty()) {
            laboratory = LaboratoryResourceIT.createUpdatedEntity(em);
            em.persist(laboratory);
            em.flush();
        } else {
            laboratory = TestUtil.findAll(em, Laboratory.class).get(0);
        }
        shipment.setDestination(laboratory);
        // Add required entity
        ClinicalStudy clinicalStudy;
        if (TestUtil.findAll(em, ClinicalStudy.class).isEmpty()) {
            clinicalStudy = ClinicalStudyResourceIT.createUpdatedEntity(em);
            em.persist(clinicalStudy);
            em.flush();
        } else {
            clinicalStudy = TestUtil.findAll(em, ClinicalStudy.class).get(0);
        }
        shipment.setStudy(clinicalStudy);
        return shipment;
    }

    @BeforeEach
    public void initTest() {
        shipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createShipment() throws Exception {
        int databaseSizeBeforeCreate = shipmentRepository.findAll().size();

        // Create the Shipment
        restShipmentMockMvc.perform(post("/api/shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isCreated());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Shipment testShipment = shipmentList.get(shipmentList.size() - 1);
        assertThat(testShipment.getShipmentCode()).isEqualTo(DEFAULT_SHIPMENT_CODE);
        assertThat(testShipment.getDateShipped()).isEqualTo(DEFAULT_DATE_SHIPPED);
        assertThat(testShipment.getDateReceived()).isEqualTo(DEFAULT_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void createShipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shipmentRepository.findAll().size();

        // Create the Shipment with an existing ID
        shipment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentMockMvc.perform(post("/api/shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShipmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentRepository.findAll().size();
        // set the field null
        shipment.setShipmentCode(null);

        // Create the Shipment, which fails.

        restShipmentMockMvc.perform(post("/api/shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isBadRequest());

        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateShippedIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentRepository.findAll().size();
        // set the field null
        shipment.setDateShipped(null);

        // Create the Shipment, which fails.

        restShipmentMockMvc.perform(post("/api/shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isBadRequest());

        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShipments() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList
        restShipmentMockMvc.perform(get("/api/shipments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipmentCode").value(hasItem(DEFAULT_SHIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].dateShipped").value(hasItem(DEFAULT_DATE_SHIPPED.toString())))
            .andExpect(jsonPath("$.[*].dateReceived").value(hasItem(DEFAULT_DATE_RECEIVED.toString())));
    }

    @Test
    @Transactional
    public void getShipment() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get the shipment
        restShipmentMockMvc.perform(get("/api/shipments/{id}", shipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shipment.getId().intValue()))
            .andExpect(jsonPath("$.shipmentCode").value(DEFAULT_SHIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.dateShipped").value(DEFAULT_DATE_SHIPPED.toString()))
            .andExpect(jsonPath("$.dateReceived").value(DEFAULT_DATE_RECEIVED.toString()));
    }

    @Test
    @Transactional
    public void getAllShipmentsByShipmentCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where shipmentCode equals to DEFAULT_SHIPMENT_CODE
        defaultShipmentShouldBeFound("shipmentCode.equals=" + DEFAULT_SHIPMENT_CODE);

        // Get all the shipmentList where shipmentCode equals to UPDATED_SHIPMENT_CODE
        defaultShipmentShouldNotBeFound("shipmentCode.equals=" + UPDATED_SHIPMENT_CODE);
    }

    @Test
    @Transactional
    public void getAllShipmentsByShipmentCodeIsInShouldWork() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where shipmentCode in DEFAULT_SHIPMENT_CODE or UPDATED_SHIPMENT_CODE
        defaultShipmentShouldBeFound("shipmentCode.in=" + DEFAULT_SHIPMENT_CODE + "," + UPDATED_SHIPMENT_CODE);

        // Get all the shipmentList where shipmentCode equals to UPDATED_SHIPMENT_CODE
        defaultShipmentShouldNotBeFound("shipmentCode.in=" + UPDATED_SHIPMENT_CODE);
    }

    @Test
    @Transactional
    public void getAllShipmentsByShipmentCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where shipmentCode is not null
        defaultShipmentShouldBeFound("shipmentCode.specified=true");

        // Get all the shipmentList where shipmentCode is null
        defaultShipmentShouldNotBeFound("shipmentCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateShippedIsEqualToSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateShipped equals to DEFAULT_DATE_SHIPPED
        defaultShipmentShouldBeFound("dateShipped.equals=" + DEFAULT_DATE_SHIPPED);

        // Get all the shipmentList where dateShipped equals to UPDATED_DATE_SHIPPED
        defaultShipmentShouldNotBeFound("dateShipped.equals=" + UPDATED_DATE_SHIPPED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateShippedIsInShouldWork() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateShipped in DEFAULT_DATE_SHIPPED or UPDATED_DATE_SHIPPED
        defaultShipmentShouldBeFound("dateShipped.in=" + DEFAULT_DATE_SHIPPED + "," + UPDATED_DATE_SHIPPED);

        // Get all the shipmentList where dateShipped equals to UPDATED_DATE_SHIPPED
        defaultShipmentShouldNotBeFound("dateShipped.in=" + UPDATED_DATE_SHIPPED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateShippedIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateShipped is not null
        defaultShipmentShouldBeFound("dateShipped.specified=true");

        // Get all the shipmentList where dateShipped is null
        defaultShipmentShouldNotBeFound("dateShipped.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateShippedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateShipped is greater than or equal to DEFAULT_DATE_SHIPPED
        defaultShipmentShouldBeFound("dateShipped.greaterThanOrEqual=" + DEFAULT_DATE_SHIPPED);

        // Get all the shipmentList where dateShipped is greater than or equal to UPDATED_DATE_SHIPPED
        defaultShipmentShouldNotBeFound("dateShipped.greaterThanOrEqual=" + UPDATED_DATE_SHIPPED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateShippedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateShipped is less than or equal to DEFAULT_DATE_SHIPPED
        defaultShipmentShouldBeFound("dateShipped.lessThanOrEqual=" + DEFAULT_DATE_SHIPPED);

        // Get all the shipmentList where dateShipped is less than or equal to SMALLER_DATE_SHIPPED
        defaultShipmentShouldNotBeFound("dateShipped.lessThanOrEqual=" + SMALLER_DATE_SHIPPED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateShippedIsLessThanSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateShipped is less than DEFAULT_DATE_SHIPPED
        defaultShipmentShouldNotBeFound("dateShipped.lessThan=" + DEFAULT_DATE_SHIPPED);

        // Get all the shipmentList where dateShipped is less than UPDATED_DATE_SHIPPED
        defaultShipmentShouldBeFound("dateShipped.lessThan=" + UPDATED_DATE_SHIPPED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateShippedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateShipped is greater than DEFAULT_DATE_SHIPPED
        defaultShipmentShouldNotBeFound("dateShipped.greaterThan=" + DEFAULT_DATE_SHIPPED);

        // Get all the shipmentList where dateShipped is greater than SMALLER_DATE_SHIPPED
        defaultShipmentShouldBeFound("dateShipped.greaterThan=" + SMALLER_DATE_SHIPPED);
    }


    @Test
    @Transactional
    public void getAllShipmentsByDateReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateReceived equals to DEFAULT_DATE_RECEIVED
        defaultShipmentShouldBeFound("dateReceived.equals=" + DEFAULT_DATE_RECEIVED);

        // Get all the shipmentList where dateReceived equals to UPDATED_DATE_RECEIVED
        defaultShipmentShouldNotBeFound("dateReceived.equals=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateReceived in DEFAULT_DATE_RECEIVED or UPDATED_DATE_RECEIVED
        defaultShipmentShouldBeFound("dateReceived.in=" + DEFAULT_DATE_RECEIVED + "," + UPDATED_DATE_RECEIVED);

        // Get all the shipmentList where dateReceived equals to UPDATED_DATE_RECEIVED
        defaultShipmentShouldNotBeFound("dateReceived.in=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateReceived is not null
        defaultShipmentShouldBeFound("dateReceived.specified=true");

        // Get all the shipmentList where dateReceived is null
        defaultShipmentShouldNotBeFound("dateReceived.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateReceivedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateReceived is greater than or equal to DEFAULT_DATE_RECEIVED
        defaultShipmentShouldBeFound("dateReceived.greaterThanOrEqual=" + DEFAULT_DATE_RECEIVED);

        // Get all the shipmentList where dateReceived is greater than or equal to UPDATED_DATE_RECEIVED
        defaultShipmentShouldNotBeFound("dateReceived.greaterThanOrEqual=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateReceivedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateReceived is less than or equal to DEFAULT_DATE_RECEIVED
        defaultShipmentShouldBeFound("dateReceived.lessThanOrEqual=" + DEFAULT_DATE_RECEIVED);

        // Get all the shipmentList where dateReceived is less than or equal to SMALLER_DATE_RECEIVED
        defaultShipmentShouldNotBeFound("dateReceived.lessThanOrEqual=" + SMALLER_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateReceivedIsLessThanSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateReceived is less than DEFAULT_DATE_RECEIVED
        defaultShipmentShouldNotBeFound("dateReceived.lessThan=" + DEFAULT_DATE_RECEIVED);

        // Get all the shipmentList where dateReceived is less than UPDATED_DATE_RECEIVED
        defaultShipmentShouldBeFound("dateReceived.lessThan=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllShipmentsByDateReceivedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where dateReceived is greater than DEFAULT_DATE_RECEIVED
        defaultShipmentShouldNotBeFound("dateReceived.greaterThan=" + DEFAULT_DATE_RECEIVED);

        // Get all the shipmentList where dateReceived is greater than SMALLER_DATE_RECEIVED
        defaultShipmentShouldBeFound("dateReceived.greaterThan=" + SMALLER_DATE_RECEIVED);
    }


    @Test
    @Transactional
    public void getAllShipmentsByComponentsIsEqualToSomething() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);
        ShipmentComponent components = ShipmentComponentResourceIT.createEntity(em);
        em.persist(components);
        em.flush();
        shipment.addComponents(components);
        shipmentRepository.saveAndFlush(shipment);
        Long componentsId = components.getId();

        // Get all the shipmentList where components equals to componentsId
        defaultShipmentShouldBeFound("componentsId.equals=" + componentsId);

        // Get all the shipmentList where components equals to componentsId + 1
        defaultShipmentShouldNotBeFound("componentsId.equals=" + (componentsId + 1));
    }


    @Test
    @Transactional
    public void getAllShipmentsByDestinationIsEqualToSomething() throws Exception {
        // Get already existing entity
        Laboratory destination = shipment.getDestination();
        shipmentRepository.saveAndFlush(shipment);
        Long destinationId = destination.getId();

        // Get all the shipmentList where destination equals to destinationId
        defaultShipmentShouldBeFound("destinationId.equals=" + destinationId);

        // Get all the shipmentList where destination equals to destinationId + 1
        defaultShipmentShouldNotBeFound("destinationId.equals=" + (destinationId + 1));
    }


    @Test
    @Transactional
    public void getAllShipmentsByStudyIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClinicalStudy study = shipment.getStudy();
        shipmentRepository.saveAndFlush(shipment);
        Long studyId = study.getId();

        // Get all the shipmentList where study equals to studyId
        defaultShipmentShouldBeFound("studyId.equals=" + studyId);

        // Get all the shipmentList where study equals to studyId + 1
        defaultShipmentShouldNotBeFound("studyId.equals=" + (studyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentShouldBeFound(String filter) throws Exception {
        restShipmentMockMvc.perform(get("/api/shipments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipmentCode").value(hasItem(DEFAULT_SHIPMENT_CODE)))
            .andExpect(jsonPath("$.[*].dateShipped").value(hasItem(DEFAULT_DATE_SHIPPED.toString())))
            .andExpect(jsonPath("$.[*].dateReceived").value(hasItem(DEFAULT_DATE_RECEIVED.toString())));

        // Check, that the count call also returns 1
        restShipmentMockMvc.perform(get("/api/shipments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentShouldNotBeFound(String filter) throws Exception {
        restShipmentMockMvc.perform(get("/api/shipments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentMockMvc.perform(get("/api/shipments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShipment() throws Exception {
        // Get the shipment
        restShipmentMockMvc.perform(get("/api/shipments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShipment() throws Exception {
        // Initialize the database
        shipmentService.save(shipment);

        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();

        // Update the shipment
        Shipment updatedShipment = shipmentRepository.findById(shipment.getId()).get();
        // Disconnect from session so that the updates on updatedShipment are not directly saved in db
        em.detach(updatedShipment);
        updatedShipment
            .shipmentCode(UPDATED_SHIPMENT_CODE)
            .dateShipped(UPDATED_DATE_SHIPPED)
            .dateReceived(UPDATED_DATE_RECEIVED);

        restShipmentMockMvc.perform(put("/api/shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShipment)))
            .andExpect(status().isOk());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
        Shipment testShipment = shipmentList.get(shipmentList.size() - 1);
        assertThat(testShipment.getShipmentCode()).isEqualTo(UPDATED_SHIPMENT_CODE);
        assertThat(testShipment.getDateShipped()).isEqualTo(UPDATED_DATE_SHIPPED);
        assertThat(testShipment.getDateReceived()).isEqualTo(UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void updateNonExistingShipment() throws Exception {
        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();

        // Create the Shipment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMockMvc.perform(put("/api/shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShipment() throws Exception {
        // Initialize the database
        shipmentService.save(shipment);

        int databaseSizeBeforeDelete = shipmentRepository.findAll().size();

        // Delete the shipment
        restShipmentMockMvc.perform(delete("/api/shipments/{id}", shipment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shipment.class);
        Shipment shipment1 = new Shipment();
        shipment1.setId(1L);
        Shipment shipment2 = new Shipment();
        shipment2.setId(shipment1.getId());
        assertThat(shipment1).isEqualTo(shipment2);
        shipment2.setId(2L);
        assertThat(shipment1).isNotEqualTo(shipment2);
        shipment1.setId(null);
        assertThat(shipment1).isNotEqualTo(shipment2);
    }
}

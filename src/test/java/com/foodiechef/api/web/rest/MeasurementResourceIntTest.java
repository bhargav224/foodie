package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.Measurement;
import com.foodiechef.api.domain.RecipeIngredient;
import com.foodiechef.api.domain.IngredientNutritionInfo;
import com.foodiechef.api.repository.MeasurementRepository;
import com.foodiechef.api.repository.search.MeasurementSearchRepository;
import com.foodiechef.api.service.MeasurementService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.MeasurementCriteria;
import com.foodiechef.api.service.MeasurementQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.foodiechef.api.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeasurementResource REST controller.
 *
 * @see MeasurementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class MeasurementResourceIntTest {

    private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MeasurementService measurementService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.MeasurementSearchRepositoryMockConfiguration
     */
    @Autowired
    private MeasurementSearchRepository mockMeasurementSearchRepository;

    @Autowired
    private MeasurementQueryService measurementQueryService;

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

    private MockMvc restMeasurementMockMvc;

    private Measurement measurement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MeasurementResource measurementResource = new MeasurementResource(measurementService, measurementQueryService);
        this.restMeasurementMockMvc = MockMvcBuilders.standaloneSetup(measurementResource)
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
    public static Measurement createEntity(EntityManager em) {
        Measurement measurement = new Measurement()
            .abbreviation(DEFAULT_ABBREVIATION)
            .active(DEFAULT_ACTIVE);
        return measurement;
    }

    @Before
    public void initTest() {
        measurement = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeasurement() throws Exception {
        int databaseSizeBeforeCreate = measurementRepository.findAll().size();

        // Create the Measurement
        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurement)))
            .andExpect(status().isCreated());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeCreate + 1);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
        assertThat(testMeasurement.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Measurement in Elasticsearch
        verify(mockMeasurementSearchRepository, times(1)).save(testMeasurement);
    }

    @Test
    @Transactional
    public void createMeasurementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = measurementRepository.findAll().size();

        // Create the Measurement with an existing ID
        measurement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurement)))
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Measurement in Elasticsearch
        verify(mockMeasurementSearchRepository, times(0)).save(measurement);
    }

    @Test
    @Transactional
    public void checkAbbreviationIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementRepository.findAll().size();
        // set the field null
        measurement.setAbbreviation(null);

        // Create the Measurement, which fails.

        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurement)))
            .andExpect(status().isBadRequest());

        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementRepository.findAll().size();
        // set the field null
        measurement.setActive(null);

        // Create the Measurement, which fails.

        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurement)))
            .andExpect(status().isBadRequest());

        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMeasurements() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList
        restMeasurementMockMvc.perform(get("/api/measurements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get the measurement
        restMeasurementMockMvc.perform(get("/api/measurements/{id}", measurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(measurement.getId().intValue()))
            .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMeasurementsByAbbreviationIsEqualToSomething() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where abbreviation equals to DEFAULT_ABBREVIATION
        defaultMeasurementShouldBeFound("abbreviation.equals=" + DEFAULT_ABBREVIATION);

        // Get all the measurementList where abbreviation equals to UPDATED_ABBREVIATION
        defaultMeasurementShouldNotBeFound("abbreviation.equals=" + UPDATED_ABBREVIATION);
    }

    @Test
    @Transactional
    public void getAllMeasurementsByAbbreviationIsInShouldWork() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where abbreviation in DEFAULT_ABBREVIATION or UPDATED_ABBREVIATION
        defaultMeasurementShouldBeFound("abbreviation.in=" + DEFAULT_ABBREVIATION + "," + UPDATED_ABBREVIATION);

        // Get all the measurementList where abbreviation equals to UPDATED_ABBREVIATION
        defaultMeasurementShouldNotBeFound("abbreviation.in=" + UPDATED_ABBREVIATION);
    }

    @Test
    @Transactional
    public void getAllMeasurementsByAbbreviationIsNullOrNotNull() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where abbreviation is not null
        defaultMeasurementShouldBeFound("abbreviation.specified=true");

        // Get all the measurementList where abbreviation is null
        defaultMeasurementShouldNotBeFound("abbreviation.specified=false");
    }

    @Test
    @Transactional
    public void getAllMeasurementsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where active equals to DEFAULT_ACTIVE
        defaultMeasurementShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the measurementList where active equals to UPDATED_ACTIVE
        defaultMeasurementShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMeasurementsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultMeasurementShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the measurementList where active equals to UPDATED_ACTIVE
        defaultMeasurementShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMeasurementsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where active is not null
        defaultMeasurementShouldBeFound("active.specified=true");

        // Get all the measurementList where active is null
        defaultMeasurementShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllMeasurementsByRecipeIngredientIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeIngredient recipeIngredient = RecipeIngredientResourceIntTest.createEntity(em);
        em.persist(recipeIngredient);
        em.flush();
        measurement.addRecipeIngredient(recipeIngredient);
        measurementRepository.saveAndFlush(measurement);
        Long recipeIngredientId = recipeIngredient.getId();

        // Get all the measurementList where recipeIngredient equals to recipeIngredientId
        defaultMeasurementShouldBeFound("recipeIngredientId.equals=" + recipeIngredientId);

        // Get all the measurementList where recipeIngredient equals to recipeIngredientId + 1
        defaultMeasurementShouldNotBeFound("recipeIngredientId.equals=" + (recipeIngredientId + 1));
    }


    @Test
    @Transactional
    public void getAllMeasurementsByForNutritionUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        IngredientNutritionInfo forNutritionUnit = IngredientNutritionInfoResourceIntTest.createEntity(em);
        em.persist(forNutritionUnit);
        em.flush();
        measurement.addForNutritionUnit(forNutritionUnit);
        measurementRepository.saveAndFlush(measurement);
        Long forNutritionUnitId = forNutritionUnit.getId();

        // Get all the measurementList where forNutritionUnit equals to forNutritionUnitId
        defaultMeasurementShouldBeFound("forNutritionUnitId.equals=" + forNutritionUnitId);

        // Get all the measurementList where forNutritionUnit equals to forNutritionUnitId + 1
        defaultMeasurementShouldNotBeFound("forNutritionUnitId.equals=" + (forNutritionUnitId + 1));
    }


    @Test
    @Transactional
    public void getAllMeasurementsByForIngredientUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        IngredientNutritionInfo forIngredientUnit = IngredientNutritionInfoResourceIntTest.createEntity(em);
        em.persist(forIngredientUnit);
        em.flush();
        measurement.addForIngredientUnit(forIngredientUnit);
        measurementRepository.saveAndFlush(measurement);
        Long forIngredientUnitId = forIngredientUnit.getId();

        // Get all the measurementList where forIngredientUnit equals to forIngredientUnitId
        defaultMeasurementShouldBeFound("forIngredientUnitId.equals=" + forIngredientUnitId);

        // Get all the measurementList where forIngredientUnit equals to forIngredientUnitId + 1
        defaultMeasurementShouldNotBeFound("forIngredientUnitId.equals=" + (forIngredientUnitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMeasurementShouldBeFound(String filter) throws Exception {
        restMeasurementMockMvc.perform(get("/api/measurements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restMeasurementMockMvc.perform(get("/api/measurements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMeasurementShouldNotBeFound(String filter) throws Exception {
        restMeasurementMockMvc.perform(get("/api/measurements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMeasurementMockMvc.perform(get("/api/measurements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMeasurement() throws Exception {
        // Get the measurement
        restMeasurementMockMvc.perform(get("/api/measurements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeasurement() throws Exception {
        // Initialize the database
        measurementService.save(measurement);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMeasurementSearchRepository);

        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Update the measurement
        Measurement updatedMeasurement = measurementRepository.findById(measurement.getId()).get();
        // Disconnect from session so that the updates on updatedMeasurement are not directly saved in db
        em.detach(updatedMeasurement);
        updatedMeasurement
            .abbreviation(UPDATED_ABBREVIATION)
            .active(UPDATED_ACTIVE);

        restMeasurementMockMvc.perform(put("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeasurement)))
            .andExpect(status().isOk());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
        assertThat(testMeasurement.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Measurement in Elasticsearch
        verify(mockMeasurementSearchRepository, times(1)).save(testMeasurement);
    }

    @Test
    @Transactional
    public void updateNonExistingMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Create the Measurement

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasurementMockMvc.perform(put("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurement)))
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Measurement in Elasticsearch
        verify(mockMeasurementSearchRepository, times(0)).save(measurement);
    }

    @Test
    @Transactional
    public void deleteMeasurement() throws Exception {
        // Initialize the database
        measurementService.save(measurement);

        int databaseSizeBeforeDelete = measurementRepository.findAll().size();

        // Delete the measurement
        restMeasurementMockMvc.perform(delete("/api/measurements/{id}", measurement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Measurement in Elasticsearch
        verify(mockMeasurementSearchRepository, times(1)).deleteById(measurement.getId());
    }

    @Test
    @Transactional
    public void searchMeasurement() throws Exception {
        // Initialize the database
        measurementService.save(measurement);
        when(mockMeasurementSearchRepository.search(queryStringQuery("id:" + measurement.getId())))
            .thenReturn(Collections.singletonList(measurement));
        // Search the measurement
        restMeasurementMockMvc.perform(get("/api/_search/measurements?query=id:" + measurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Measurement.class);
        Measurement measurement1 = new Measurement();
        measurement1.setId(1L);
        Measurement measurement2 = new Measurement();
        measurement2.setId(measurement1.getId());
        assertThat(measurement1).isEqualTo(measurement2);
        measurement2.setId(2L);
        assertThat(measurement1).isNotEqualTo(measurement2);
        measurement1.setId(null);
        assertThat(measurement1).isNotEqualTo(measurement2);
    }
}

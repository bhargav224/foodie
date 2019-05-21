package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.NutritionInformation;
import com.foodiechef.api.domain.IngredientNutritionInfo;
import com.foodiechef.api.repository.NutritionInformationRepository;
import com.foodiechef.api.repository.search.NutritionInformationSearchRepository;
import com.foodiechef.api.service.NutritionInformationService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.NutritionInformationCriteria;
import com.foodiechef.api.service.NutritionInformationQueryService;

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
 * Test class for the NutritionInformationResource REST controller.
 *
 * @see NutritionInformationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class NutritionInformationResourceIntTest {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_NUTRITION = "AAAAAAAAAA";
    private static final String UPDATED_NUTRITION = "BBBBBBBBBB";

    @Autowired
    private NutritionInformationRepository nutritionInformationRepository;

    @Autowired
    private NutritionInformationService nutritionInformationService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.NutritionInformationSearchRepositoryMockConfiguration
     */
    @Autowired
    private NutritionInformationSearchRepository mockNutritionInformationSearchRepository;

    @Autowired
    private NutritionInformationQueryService nutritionInformationQueryService;

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

    private MockMvc restNutritionInformationMockMvc;

    private NutritionInformation nutritionInformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NutritionInformationResource nutritionInformationResource = new NutritionInformationResource(nutritionInformationService, nutritionInformationQueryService);
        this.restNutritionInformationMockMvc = MockMvcBuilders.standaloneSetup(nutritionInformationResource)
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
    public static NutritionInformation createEntity(EntityManager em) {
        NutritionInformation nutritionInformation = new NutritionInformation()
            .active(DEFAULT_ACTIVE)
            .nutrition(DEFAULT_NUTRITION);
        return nutritionInformation;
    }

    @Before
    public void initTest() {
        nutritionInformation = createEntity(em);
    }

    @Test
    @Transactional
    public void createNutritionInformation() throws Exception {
        int databaseSizeBeforeCreate = nutritionInformationRepository.findAll().size();

        // Create the NutritionInformation
        restNutritionInformationMockMvc.perform(post("/api/nutrition-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nutritionInformation)))
            .andExpect(status().isCreated());

        // Validate the NutritionInformation in the database
        List<NutritionInformation> nutritionInformationList = nutritionInformationRepository.findAll();
        assertThat(nutritionInformationList).hasSize(databaseSizeBeforeCreate + 1);
        NutritionInformation testNutritionInformation = nutritionInformationList.get(nutritionInformationList.size() - 1);
        assertThat(testNutritionInformation.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testNutritionInformation.getNutrition()).isEqualTo(DEFAULT_NUTRITION);

        // Validate the NutritionInformation in Elasticsearch
        verify(mockNutritionInformationSearchRepository, times(1)).save(testNutritionInformation);
    }

    @Test
    @Transactional
    public void createNutritionInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nutritionInformationRepository.findAll().size();

        // Create the NutritionInformation with an existing ID
        nutritionInformation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNutritionInformationMockMvc.perform(post("/api/nutrition-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nutritionInformation)))
            .andExpect(status().isBadRequest());

        // Validate the NutritionInformation in the database
        List<NutritionInformation> nutritionInformationList = nutritionInformationRepository.findAll();
        assertThat(nutritionInformationList).hasSize(databaseSizeBeforeCreate);

        // Validate the NutritionInformation in Elasticsearch
        verify(mockNutritionInformationSearchRepository, times(0)).save(nutritionInformation);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = nutritionInformationRepository.findAll().size();
        // set the field null
        nutritionInformation.setActive(null);

        // Create the NutritionInformation, which fails.

        restNutritionInformationMockMvc.perform(post("/api/nutrition-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nutritionInformation)))
            .andExpect(status().isBadRequest());

        List<NutritionInformation> nutritionInformationList = nutritionInformationRepository.findAll();
        assertThat(nutritionInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNutritionIsRequired() throws Exception {
        int databaseSizeBeforeTest = nutritionInformationRepository.findAll().size();
        // set the field null
        nutritionInformation.setNutrition(null);

        // Create the NutritionInformation, which fails.

        restNutritionInformationMockMvc.perform(post("/api/nutrition-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nutritionInformation)))
            .andExpect(status().isBadRequest());

        List<NutritionInformation> nutritionInformationList = nutritionInformationRepository.findAll();
        assertThat(nutritionInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNutritionInformations() throws Exception {
        // Initialize the database
        nutritionInformationRepository.saveAndFlush(nutritionInformation);

        // Get all the nutritionInformationList
        restNutritionInformationMockMvc.perform(get("/api/nutrition-informations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nutritionInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].nutrition").value(hasItem(DEFAULT_NUTRITION.toString())));
    }
    
    @Test
    @Transactional
    public void getNutritionInformation() throws Exception {
        // Initialize the database
        nutritionInformationRepository.saveAndFlush(nutritionInformation);

        // Get the nutritionInformation
        restNutritionInformationMockMvc.perform(get("/api/nutrition-informations/{id}", nutritionInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nutritionInformation.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.nutrition").value(DEFAULT_NUTRITION.toString()));
    }

    @Test
    @Transactional
    public void getAllNutritionInformationsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        nutritionInformationRepository.saveAndFlush(nutritionInformation);

        // Get all the nutritionInformationList where active equals to DEFAULT_ACTIVE
        defaultNutritionInformationShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the nutritionInformationList where active equals to UPDATED_ACTIVE
        defaultNutritionInformationShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllNutritionInformationsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        nutritionInformationRepository.saveAndFlush(nutritionInformation);

        // Get all the nutritionInformationList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultNutritionInformationShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the nutritionInformationList where active equals to UPDATED_ACTIVE
        defaultNutritionInformationShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllNutritionInformationsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        nutritionInformationRepository.saveAndFlush(nutritionInformation);

        // Get all the nutritionInformationList where active is not null
        defaultNutritionInformationShouldBeFound("active.specified=true");

        // Get all the nutritionInformationList where active is null
        defaultNutritionInformationShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllNutritionInformationsByNutritionIsEqualToSomething() throws Exception {
        // Initialize the database
        nutritionInformationRepository.saveAndFlush(nutritionInformation);

        // Get all the nutritionInformationList where nutrition equals to DEFAULT_NUTRITION
        defaultNutritionInformationShouldBeFound("nutrition.equals=" + DEFAULT_NUTRITION);

        // Get all the nutritionInformationList where nutrition equals to UPDATED_NUTRITION
        defaultNutritionInformationShouldNotBeFound("nutrition.equals=" + UPDATED_NUTRITION);
    }

    @Test
    @Transactional
    public void getAllNutritionInformationsByNutritionIsInShouldWork() throws Exception {
        // Initialize the database
        nutritionInformationRepository.saveAndFlush(nutritionInformation);

        // Get all the nutritionInformationList where nutrition in DEFAULT_NUTRITION or UPDATED_NUTRITION
        defaultNutritionInformationShouldBeFound("nutrition.in=" + DEFAULT_NUTRITION + "," + UPDATED_NUTRITION);

        // Get all the nutritionInformationList where nutrition equals to UPDATED_NUTRITION
        defaultNutritionInformationShouldNotBeFound("nutrition.in=" + UPDATED_NUTRITION);
    }

    @Test
    @Transactional
    public void getAllNutritionInformationsByNutritionIsNullOrNotNull() throws Exception {
        // Initialize the database
        nutritionInformationRepository.saveAndFlush(nutritionInformation);

        // Get all the nutritionInformationList where nutrition is not null
        defaultNutritionInformationShouldBeFound("nutrition.specified=true");

        // Get all the nutritionInformationList where nutrition is null
        defaultNutritionInformationShouldNotBeFound("nutrition.specified=false");
    }

    @Test
    @Transactional
    public void getAllNutritionInformationsByIngredientNutritionInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        IngredientNutritionInfo ingredientNutritionInfo = IngredientNutritionInfoResourceIntTest.createEntity(em);
        em.persist(ingredientNutritionInfo);
        em.flush();
        nutritionInformation.addIngredientNutritionInfo(ingredientNutritionInfo);
        nutritionInformationRepository.saveAndFlush(nutritionInformation);
        Long ingredientNutritionInfoId = ingredientNutritionInfo.getId();

        // Get all the nutritionInformationList where ingredientNutritionInfo equals to ingredientNutritionInfoId
        defaultNutritionInformationShouldBeFound("ingredientNutritionInfoId.equals=" + ingredientNutritionInfoId);

        // Get all the nutritionInformationList where ingredientNutritionInfo equals to ingredientNutritionInfoId + 1
        defaultNutritionInformationShouldNotBeFound("ingredientNutritionInfoId.equals=" + (ingredientNutritionInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultNutritionInformationShouldBeFound(String filter) throws Exception {
        restNutritionInformationMockMvc.perform(get("/api/nutrition-informations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nutritionInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].nutrition").value(hasItem(DEFAULT_NUTRITION)));

        // Check, that the count call also returns 1
        restNutritionInformationMockMvc.perform(get("/api/nutrition-informations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultNutritionInformationShouldNotBeFound(String filter) throws Exception {
        restNutritionInformationMockMvc.perform(get("/api/nutrition-informations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNutritionInformationMockMvc.perform(get("/api/nutrition-informations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingNutritionInformation() throws Exception {
        // Get the nutritionInformation
        restNutritionInformationMockMvc.perform(get("/api/nutrition-informations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNutritionInformation() throws Exception {
        // Initialize the database
        nutritionInformationService.save(nutritionInformation);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockNutritionInformationSearchRepository);

        int databaseSizeBeforeUpdate = nutritionInformationRepository.findAll().size();

        // Update the nutritionInformation
        NutritionInformation updatedNutritionInformation = nutritionInformationRepository.findById(nutritionInformation.getId()).get();
        // Disconnect from session so that the updates on updatedNutritionInformation are not directly saved in db
        em.detach(updatedNutritionInformation);
        updatedNutritionInformation
            .active(UPDATED_ACTIVE)
            .nutrition(UPDATED_NUTRITION);

        restNutritionInformationMockMvc.perform(put("/api/nutrition-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNutritionInformation)))
            .andExpect(status().isOk());

        // Validate the NutritionInformation in the database
        List<NutritionInformation> nutritionInformationList = nutritionInformationRepository.findAll();
        assertThat(nutritionInformationList).hasSize(databaseSizeBeforeUpdate);
        NutritionInformation testNutritionInformation = nutritionInformationList.get(nutritionInformationList.size() - 1);
        assertThat(testNutritionInformation.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testNutritionInformation.getNutrition()).isEqualTo(UPDATED_NUTRITION);

        // Validate the NutritionInformation in Elasticsearch
        verify(mockNutritionInformationSearchRepository, times(1)).save(testNutritionInformation);
    }

    @Test
    @Transactional
    public void updateNonExistingNutritionInformation() throws Exception {
        int databaseSizeBeforeUpdate = nutritionInformationRepository.findAll().size();

        // Create the NutritionInformation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNutritionInformationMockMvc.perform(put("/api/nutrition-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nutritionInformation)))
            .andExpect(status().isBadRequest());

        // Validate the NutritionInformation in the database
        List<NutritionInformation> nutritionInformationList = nutritionInformationRepository.findAll();
        assertThat(nutritionInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NutritionInformation in Elasticsearch
        verify(mockNutritionInformationSearchRepository, times(0)).save(nutritionInformation);
    }

    @Test
    @Transactional
    public void deleteNutritionInformation() throws Exception {
        // Initialize the database
        nutritionInformationService.save(nutritionInformation);

        int databaseSizeBeforeDelete = nutritionInformationRepository.findAll().size();

        // Delete the nutritionInformation
        restNutritionInformationMockMvc.perform(delete("/api/nutrition-informations/{id}", nutritionInformation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NutritionInformation> nutritionInformationList = nutritionInformationRepository.findAll();
        assertThat(nutritionInformationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NutritionInformation in Elasticsearch
        verify(mockNutritionInformationSearchRepository, times(1)).deleteById(nutritionInformation.getId());
    }

    @Test
    @Transactional
    public void searchNutritionInformation() throws Exception {
        // Initialize the database
        nutritionInformationService.save(nutritionInformation);
        when(mockNutritionInformationSearchRepository.search(queryStringQuery("id:" + nutritionInformation.getId())))
            .thenReturn(Collections.singletonList(nutritionInformation));
        // Search the nutritionInformation
        restNutritionInformationMockMvc.perform(get("/api/_search/nutrition-informations?query=id:" + nutritionInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nutritionInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].nutrition").value(hasItem(DEFAULT_NUTRITION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NutritionInformation.class);
        NutritionInformation nutritionInformation1 = new NutritionInformation();
        nutritionInformation1.setId(1L);
        NutritionInformation nutritionInformation2 = new NutritionInformation();
        nutritionInformation2.setId(nutritionInformation1.getId());
        assertThat(nutritionInformation1).isEqualTo(nutritionInformation2);
        nutritionInformation2.setId(2L);
        assertThat(nutritionInformation1).isNotEqualTo(nutritionInformation2);
        nutritionInformation1.setId(null);
        assertThat(nutritionInformation1).isNotEqualTo(nutritionInformation2);
    }
}

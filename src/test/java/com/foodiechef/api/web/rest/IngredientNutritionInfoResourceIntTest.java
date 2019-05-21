package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.IngredientNutritionInfo;
import com.foodiechef.api.domain.NutritionInformation;
import com.foodiechef.api.domain.Ingredient;
import com.foodiechef.api.domain.Measurement;
import com.foodiechef.api.repository.IngredientNutritionInfoRepository;
import com.foodiechef.api.repository.search.IngredientNutritionInfoSearchRepository;
import com.foodiechef.api.service.IngredientNutritionInfoService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.IngredientNutritionInfoCriteria;
import com.foodiechef.api.service.IngredientNutritionInfoQueryService;

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
 * Test class for the IngredientNutritionInfoResource REST controller.
 *
 * @see IngredientNutritionInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class IngredientNutritionInfoResourceIntTest {

    private static final Double DEFAULT_INGREDIENT_AMOUNT = 1D;
    private static final Double UPDATED_INGREDIENT_AMOUNT = 2D;

    private static final Double DEFAULT_NUTRITION_AMOUNT = 1D;
    private static final Double UPDATED_NUTRITION_AMOUNT = 2D;

    @Autowired
    private IngredientNutritionInfoRepository ingredientNutritionInfoRepository;

    @Autowired
    private IngredientNutritionInfoService ingredientNutritionInfoService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.IngredientNutritionInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private IngredientNutritionInfoSearchRepository mockIngredientNutritionInfoSearchRepository;

    @Autowired
    private IngredientNutritionInfoQueryService ingredientNutritionInfoQueryService;

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

    private MockMvc restIngredientNutritionInfoMockMvc;

    private IngredientNutritionInfo ingredientNutritionInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IngredientNutritionInfoResource ingredientNutritionInfoResource = new IngredientNutritionInfoResource(ingredientNutritionInfoService, ingredientNutritionInfoQueryService);
        this.restIngredientNutritionInfoMockMvc = MockMvcBuilders.standaloneSetup(ingredientNutritionInfoResource)
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
    public static IngredientNutritionInfo createEntity(EntityManager em) {
        IngredientNutritionInfo ingredientNutritionInfo = new IngredientNutritionInfo()
            .ingredientAmount(DEFAULT_INGREDIENT_AMOUNT)
            .nutritionAmount(DEFAULT_NUTRITION_AMOUNT);
        return ingredientNutritionInfo;
    }

    @Before
    public void initTest() {
        ingredientNutritionInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createIngredientNutritionInfo() throws Exception {
        int databaseSizeBeforeCreate = ingredientNutritionInfoRepository.findAll().size();

        // Create the IngredientNutritionInfo
        restIngredientNutritionInfoMockMvc.perform(post("/api/ingredient-nutrition-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredientNutritionInfo)))
            .andExpect(status().isCreated());

        // Validate the IngredientNutritionInfo in the database
        List<IngredientNutritionInfo> ingredientNutritionInfoList = ingredientNutritionInfoRepository.findAll();
        assertThat(ingredientNutritionInfoList).hasSize(databaseSizeBeforeCreate + 1);
        IngredientNutritionInfo testIngredientNutritionInfo = ingredientNutritionInfoList.get(ingredientNutritionInfoList.size() - 1);
        assertThat(testIngredientNutritionInfo.getIngredientAmount()).isEqualTo(DEFAULT_INGREDIENT_AMOUNT);
        assertThat(testIngredientNutritionInfo.getNutritionAmount()).isEqualTo(DEFAULT_NUTRITION_AMOUNT);

        // Validate the IngredientNutritionInfo in Elasticsearch
        verify(mockIngredientNutritionInfoSearchRepository, times(1)).save(testIngredientNutritionInfo);
    }

    @Test
    @Transactional
    public void createIngredientNutritionInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ingredientNutritionInfoRepository.findAll().size();

        // Create the IngredientNutritionInfo with an existing ID
        ingredientNutritionInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredientNutritionInfoMockMvc.perform(post("/api/ingredient-nutrition-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredientNutritionInfo)))
            .andExpect(status().isBadRequest());

        // Validate the IngredientNutritionInfo in the database
        List<IngredientNutritionInfo> ingredientNutritionInfoList = ingredientNutritionInfoRepository.findAll();
        assertThat(ingredientNutritionInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the IngredientNutritionInfo in Elasticsearch
        verify(mockIngredientNutritionInfoSearchRepository, times(0)).save(ingredientNutritionInfo);
    }

    @Test
    @Transactional
    public void checkIngredientAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientNutritionInfoRepository.findAll().size();
        // set the field null
        ingredientNutritionInfo.setIngredientAmount(null);

        // Create the IngredientNutritionInfo, which fails.

        restIngredientNutritionInfoMockMvc.perform(post("/api/ingredient-nutrition-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredientNutritionInfo)))
            .andExpect(status().isBadRequest());

        List<IngredientNutritionInfo> ingredientNutritionInfoList = ingredientNutritionInfoRepository.findAll();
        assertThat(ingredientNutritionInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNutritionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientNutritionInfoRepository.findAll().size();
        // set the field null
        ingredientNutritionInfo.setNutritionAmount(null);

        // Create the IngredientNutritionInfo, which fails.

        restIngredientNutritionInfoMockMvc.perform(post("/api/ingredient-nutrition-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredientNutritionInfo)))
            .andExpect(status().isBadRequest());

        List<IngredientNutritionInfo> ingredientNutritionInfoList = ingredientNutritionInfoRepository.findAll();
        assertThat(ingredientNutritionInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIngredientNutritionInfos() throws Exception {
        // Initialize the database
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);

        // Get all the ingredientNutritionInfoList
        restIngredientNutritionInfoMockMvc.perform(get("/api/ingredient-nutrition-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredientNutritionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingredientAmount").value(hasItem(DEFAULT_INGREDIENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].nutritionAmount").value(hasItem(DEFAULT_NUTRITION_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getIngredientNutritionInfo() throws Exception {
        // Initialize the database
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);

        // Get the ingredientNutritionInfo
        restIngredientNutritionInfoMockMvc.perform(get("/api/ingredient-nutrition-infos/{id}", ingredientNutritionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ingredientNutritionInfo.getId().intValue()))
            .andExpect(jsonPath("$.ingredientAmount").value(DEFAULT_INGREDIENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.nutritionAmount").value(DEFAULT_NUTRITION_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByIngredientAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);

        // Get all the ingredientNutritionInfoList where ingredientAmount equals to DEFAULT_INGREDIENT_AMOUNT
        defaultIngredientNutritionInfoShouldBeFound("ingredientAmount.equals=" + DEFAULT_INGREDIENT_AMOUNT);

        // Get all the ingredientNutritionInfoList where ingredientAmount equals to UPDATED_INGREDIENT_AMOUNT
        defaultIngredientNutritionInfoShouldNotBeFound("ingredientAmount.equals=" + UPDATED_INGREDIENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByIngredientAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);

        // Get all the ingredientNutritionInfoList where ingredientAmount in DEFAULT_INGREDIENT_AMOUNT or UPDATED_INGREDIENT_AMOUNT
        defaultIngredientNutritionInfoShouldBeFound("ingredientAmount.in=" + DEFAULT_INGREDIENT_AMOUNT + "," + UPDATED_INGREDIENT_AMOUNT);

        // Get all the ingredientNutritionInfoList where ingredientAmount equals to UPDATED_INGREDIENT_AMOUNT
        defaultIngredientNutritionInfoShouldNotBeFound("ingredientAmount.in=" + UPDATED_INGREDIENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByIngredientAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);

        // Get all the ingredientNutritionInfoList where ingredientAmount is not null
        defaultIngredientNutritionInfoShouldBeFound("ingredientAmount.specified=true");

        // Get all the ingredientNutritionInfoList where ingredientAmount is null
        defaultIngredientNutritionInfoShouldNotBeFound("ingredientAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByNutritionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);

        // Get all the ingredientNutritionInfoList where nutritionAmount equals to DEFAULT_NUTRITION_AMOUNT
        defaultIngredientNutritionInfoShouldBeFound("nutritionAmount.equals=" + DEFAULT_NUTRITION_AMOUNT);

        // Get all the ingredientNutritionInfoList where nutritionAmount equals to UPDATED_NUTRITION_AMOUNT
        defaultIngredientNutritionInfoShouldNotBeFound("nutritionAmount.equals=" + UPDATED_NUTRITION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByNutritionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);

        // Get all the ingredientNutritionInfoList where nutritionAmount in DEFAULT_NUTRITION_AMOUNT or UPDATED_NUTRITION_AMOUNT
        defaultIngredientNutritionInfoShouldBeFound("nutritionAmount.in=" + DEFAULT_NUTRITION_AMOUNT + "," + UPDATED_NUTRITION_AMOUNT);

        // Get all the ingredientNutritionInfoList where nutritionAmount equals to UPDATED_NUTRITION_AMOUNT
        defaultIngredientNutritionInfoShouldNotBeFound("nutritionAmount.in=" + UPDATED_NUTRITION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByNutritionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);

        // Get all the ingredientNutritionInfoList where nutritionAmount is not null
        defaultIngredientNutritionInfoShouldBeFound("nutritionAmount.specified=true");

        // Get all the ingredientNutritionInfoList where nutritionAmount is null
        defaultIngredientNutritionInfoShouldNotBeFound("nutritionAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByNutritionInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        NutritionInformation nutritionInformation = NutritionInformationResourceIntTest.createEntity(em);
        em.persist(nutritionInformation);
        em.flush();
        ingredientNutritionInfo.setNutritionInformation(nutritionInformation);
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);
        Long nutritionInformationId = nutritionInformation.getId();

        // Get all the ingredientNutritionInfoList where nutritionInformation equals to nutritionInformationId
        defaultIngredientNutritionInfoShouldBeFound("nutritionInformationId.equals=" + nutritionInformationId);

        // Get all the ingredientNutritionInfoList where nutritionInformation equals to nutritionInformationId + 1
        defaultIngredientNutritionInfoShouldNotBeFound("nutritionInformationId.equals=" + (nutritionInformationId + 1));
    }


    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByIngredientIsEqualToSomething() throws Exception {
        // Initialize the database
        Ingredient ingredient = IngredientResourceIntTest.createEntity(em);
        em.persist(ingredient);
        em.flush();
        ingredientNutritionInfo.setIngredient(ingredient);
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);
        Long ingredientId = ingredient.getId();

        // Get all the ingredientNutritionInfoList where ingredient equals to ingredientId
        defaultIngredientNutritionInfoShouldBeFound("ingredientId.equals=" + ingredientId);

        // Get all the ingredientNutritionInfoList where ingredient equals to ingredientId + 1
        defaultIngredientNutritionInfoShouldNotBeFound("ingredientId.equals=" + (ingredientId + 1));
    }


    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByNutritionUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        Measurement nutritionUnit = MeasurementResourceIntTest.createEntity(em);
        em.persist(nutritionUnit);
        em.flush();
        ingredientNutritionInfo.setNutritionUnit(nutritionUnit);
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);
        Long nutritionUnitId = nutritionUnit.getId();

        // Get all the ingredientNutritionInfoList where nutritionUnit equals to nutritionUnitId
        defaultIngredientNutritionInfoShouldBeFound("nutritionUnitId.equals=" + nutritionUnitId);

        // Get all the ingredientNutritionInfoList where nutritionUnit equals to nutritionUnitId + 1
        defaultIngredientNutritionInfoShouldNotBeFound("nutritionUnitId.equals=" + (nutritionUnitId + 1));
    }


    @Test
    @Transactional
    public void getAllIngredientNutritionInfosByIngredientUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        Measurement ingredientUnit = MeasurementResourceIntTest.createEntity(em);
        em.persist(ingredientUnit);
        em.flush();
        ingredientNutritionInfo.setIngredientUnit(ingredientUnit);
        ingredientNutritionInfoRepository.saveAndFlush(ingredientNutritionInfo);
        Long ingredientUnitId = ingredientUnit.getId();

        // Get all the ingredientNutritionInfoList where ingredientUnit equals to ingredientUnitId
        defaultIngredientNutritionInfoShouldBeFound("ingredientUnitId.equals=" + ingredientUnitId);

        // Get all the ingredientNutritionInfoList where ingredientUnit equals to ingredientUnitId + 1
        defaultIngredientNutritionInfoShouldNotBeFound("ingredientUnitId.equals=" + (ingredientUnitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultIngredientNutritionInfoShouldBeFound(String filter) throws Exception {
        restIngredientNutritionInfoMockMvc.perform(get("/api/ingredient-nutrition-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredientNutritionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingredientAmount").value(hasItem(DEFAULT_INGREDIENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].nutritionAmount").value(hasItem(DEFAULT_NUTRITION_AMOUNT.doubleValue())));

        // Check, that the count call also returns 1
        restIngredientNutritionInfoMockMvc.perform(get("/api/ingredient-nutrition-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultIngredientNutritionInfoShouldNotBeFound(String filter) throws Exception {
        restIngredientNutritionInfoMockMvc.perform(get("/api/ingredient-nutrition-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIngredientNutritionInfoMockMvc.perform(get("/api/ingredient-nutrition-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingIngredientNutritionInfo() throws Exception {
        // Get the ingredientNutritionInfo
        restIngredientNutritionInfoMockMvc.perform(get("/api/ingredient-nutrition-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIngredientNutritionInfo() throws Exception {
        // Initialize the database
        ingredientNutritionInfoService.save(ingredientNutritionInfo);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockIngredientNutritionInfoSearchRepository);

        int databaseSizeBeforeUpdate = ingredientNutritionInfoRepository.findAll().size();

        // Update the ingredientNutritionInfo
        IngredientNutritionInfo updatedIngredientNutritionInfo = ingredientNutritionInfoRepository.findById(ingredientNutritionInfo.getId()).get();
        // Disconnect from session so that the updates on updatedIngredientNutritionInfo are not directly saved in db
        em.detach(updatedIngredientNutritionInfo);
        updatedIngredientNutritionInfo
            .ingredientAmount(UPDATED_INGREDIENT_AMOUNT)
            .nutritionAmount(UPDATED_NUTRITION_AMOUNT);

        restIngredientNutritionInfoMockMvc.perform(put("/api/ingredient-nutrition-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIngredientNutritionInfo)))
            .andExpect(status().isOk());

        // Validate the IngredientNutritionInfo in the database
        List<IngredientNutritionInfo> ingredientNutritionInfoList = ingredientNutritionInfoRepository.findAll();
        assertThat(ingredientNutritionInfoList).hasSize(databaseSizeBeforeUpdate);
        IngredientNutritionInfo testIngredientNutritionInfo = ingredientNutritionInfoList.get(ingredientNutritionInfoList.size() - 1);
        assertThat(testIngredientNutritionInfo.getIngredientAmount()).isEqualTo(UPDATED_INGREDIENT_AMOUNT);
        assertThat(testIngredientNutritionInfo.getNutritionAmount()).isEqualTo(UPDATED_NUTRITION_AMOUNT);

        // Validate the IngredientNutritionInfo in Elasticsearch
        verify(mockIngredientNutritionInfoSearchRepository, times(1)).save(testIngredientNutritionInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingIngredientNutritionInfo() throws Exception {
        int databaseSizeBeforeUpdate = ingredientNutritionInfoRepository.findAll().size();

        // Create the IngredientNutritionInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientNutritionInfoMockMvc.perform(put("/api/ingredient-nutrition-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredientNutritionInfo)))
            .andExpect(status().isBadRequest());

        // Validate the IngredientNutritionInfo in the database
        List<IngredientNutritionInfo> ingredientNutritionInfoList = ingredientNutritionInfoRepository.findAll();
        assertThat(ingredientNutritionInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IngredientNutritionInfo in Elasticsearch
        verify(mockIngredientNutritionInfoSearchRepository, times(0)).save(ingredientNutritionInfo);
    }

    @Test
    @Transactional
    public void deleteIngredientNutritionInfo() throws Exception {
        // Initialize the database
        ingredientNutritionInfoService.save(ingredientNutritionInfo);

        int databaseSizeBeforeDelete = ingredientNutritionInfoRepository.findAll().size();

        // Delete the ingredientNutritionInfo
        restIngredientNutritionInfoMockMvc.perform(delete("/api/ingredient-nutrition-infos/{id}", ingredientNutritionInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IngredientNutritionInfo> ingredientNutritionInfoList = ingredientNutritionInfoRepository.findAll();
        assertThat(ingredientNutritionInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IngredientNutritionInfo in Elasticsearch
        verify(mockIngredientNutritionInfoSearchRepository, times(1)).deleteById(ingredientNutritionInfo.getId());
    }

    @Test
    @Transactional
    public void searchIngredientNutritionInfo() throws Exception {
        // Initialize the database
        ingredientNutritionInfoService.save(ingredientNutritionInfo);
        when(mockIngredientNutritionInfoSearchRepository.search(queryStringQuery("id:" + ingredientNutritionInfo.getId())))
            .thenReturn(Collections.singletonList(ingredientNutritionInfo));
        // Search the ingredientNutritionInfo
        restIngredientNutritionInfoMockMvc.perform(get("/api/_search/ingredient-nutrition-infos?query=id:" + ingredientNutritionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredientNutritionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingredientAmount").value(hasItem(DEFAULT_INGREDIENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].nutritionAmount").value(hasItem(DEFAULT_NUTRITION_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngredientNutritionInfo.class);
        IngredientNutritionInfo ingredientNutritionInfo1 = new IngredientNutritionInfo();
        ingredientNutritionInfo1.setId(1L);
        IngredientNutritionInfo ingredientNutritionInfo2 = new IngredientNutritionInfo();
        ingredientNutritionInfo2.setId(ingredientNutritionInfo1.getId());
        assertThat(ingredientNutritionInfo1).isEqualTo(ingredientNutritionInfo2);
        ingredientNutritionInfo2.setId(2L);
        assertThat(ingredientNutritionInfo1).isNotEqualTo(ingredientNutritionInfo2);
        ingredientNutritionInfo1.setId(null);
        assertThat(ingredientNutritionInfo1).isNotEqualTo(ingredientNutritionInfo2);
    }
}

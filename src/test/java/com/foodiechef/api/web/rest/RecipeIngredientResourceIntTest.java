package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.RecipeIngredient;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.Measurement;
import com.foodiechef.api.domain.Ingredient;
import com.foodiechef.api.repository.RecipeIngredientRepository;
import com.foodiechef.api.repository.search.RecipeIngredientSearchRepository;
import com.foodiechef.api.service.RecipeIngredientService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RecipeIngredientCriteria;
import com.foodiechef.api.service.RecipeIngredientQueryService;

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
 * Test class for the RecipeIngredientResource REST controller.
 *
 * @see RecipeIngredientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RecipeIngredientResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_INGREDIENT_NUTRITION_VALUE = 1D;
    private static final Double UPDATED_INGREDIENT_NUTRITION_VALUE = 2D;

    private static final String DEFAULT_RESTRICTION = "AAAAAAAAAA";
    private static final String UPDATED_RESTRICTION = "BBBBBBBBBB";

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private RecipeIngredientService recipeIngredientService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RecipeIngredientSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeIngredientSearchRepository mockRecipeIngredientSearchRepository;

    @Autowired
    private RecipeIngredientQueryService recipeIngredientQueryService;

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

    private MockMvc restRecipeIngredientMockMvc;

    private RecipeIngredient recipeIngredient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecipeIngredientResource recipeIngredientResource = new RecipeIngredientResource(recipeIngredientService, recipeIngredientQueryService);
        this.restRecipeIngredientMockMvc = MockMvcBuilders.standaloneSetup(recipeIngredientResource)
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
    public static RecipeIngredient createEntity(EntityManager em) {
        RecipeIngredient recipeIngredient = new RecipeIngredient()
            .amount(DEFAULT_AMOUNT)
            .ingredientNutritionValue(DEFAULT_INGREDIENT_NUTRITION_VALUE)
            .restriction(DEFAULT_RESTRICTION);
        return recipeIngredient;
    }

    @Before
    public void initTest() {
        recipeIngredient = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecipeIngredient() throws Exception {
        int databaseSizeBeforeCreate = recipeIngredientRepository.findAll().size();

        // Create the RecipeIngredient
        restRecipeIngredientMockMvc.perform(post("/api/recipe-ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeIngredient)))
            .andExpect(status().isCreated());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeCreate + 1);
        RecipeIngredient testRecipeIngredient = recipeIngredientList.get(recipeIngredientList.size() - 1);
        assertThat(testRecipeIngredient.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRecipeIngredient.getIngredientNutritionValue()).isEqualTo(DEFAULT_INGREDIENT_NUTRITION_VALUE);
        assertThat(testRecipeIngredient.getRestriction()).isEqualTo(DEFAULT_RESTRICTION);

        // Validate the RecipeIngredient in Elasticsearch
        verify(mockRecipeIngredientSearchRepository, times(1)).save(testRecipeIngredient);
    }

    @Test
    @Transactional
    public void createRecipeIngredientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recipeIngredientRepository.findAll().size();

        // Create the RecipeIngredient with an existing ID
        recipeIngredient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeIngredientMockMvc.perform(post("/api/recipe-ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeIngredient)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeCreate);

        // Validate the RecipeIngredient in Elasticsearch
        verify(mockRecipeIngredientSearchRepository, times(0)).save(recipeIngredient);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeIngredientRepository.findAll().size();
        // set the field null
        recipeIngredient.setAmount(null);

        // Create the RecipeIngredient, which fails.

        restRecipeIngredientMockMvc.perform(post("/api/recipe-ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeIngredient)))
            .andExpect(status().isBadRequest());

        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIngredientNutritionValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeIngredientRepository.findAll().size();
        // set the field null
        recipeIngredient.setIngredientNutritionValue(null);

        // Create the RecipeIngredient, which fails.

        restRecipeIngredientMockMvc.perform(post("/api/recipe-ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeIngredient)))
            .andExpect(status().isBadRequest());

        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRestrictionIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeIngredientRepository.findAll().size();
        // set the field null
        recipeIngredient.setRestriction(null);

        // Create the RecipeIngredient, which fails.

        restRecipeIngredientMockMvc.perform(post("/api/recipe-ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeIngredient)))
            .andExpect(status().isBadRequest());

        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecipeIngredients() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList
        restRecipeIngredientMockMvc.perform(get("/api/recipe-ingredients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeIngredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].ingredientNutritionValue").value(hasItem(DEFAULT_INGREDIENT_NUTRITION_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].restriction").value(hasItem(DEFAULT_RESTRICTION.toString())));
    }
    
    @Test
    @Transactional
    public void getRecipeIngredient() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get the recipeIngredient
        restRecipeIngredientMockMvc.perform(get("/api/recipe-ingredients/{id}", recipeIngredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recipeIngredient.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.ingredientNutritionValue").value(DEFAULT_INGREDIENT_NUTRITION_VALUE.doubleValue()))
            .andExpect(jsonPath("$.restriction").value(DEFAULT_RESTRICTION.toString()));
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where amount equals to DEFAULT_AMOUNT
        defaultRecipeIngredientShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the recipeIngredientList where amount equals to UPDATED_AMOUNT
        defaultRecipeIngredientShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultRecipeIngredientShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the recipeIngredientList where amount equals to UPDATED_AMOUNT
        defaultRecipeIngredientShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where amount is not null
        defaultRecipeIngredientShouldBeFound("amount.specified=true");

        // Get all the recipeIngredientList where amount is null
        defaultRecipeIngredientShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByIngredientNutritionValueIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where ingredientNutritionValue equals to DEFAULT_INGREDIENT_NUTRITION_VALUE
        defaultRecipeIngredientShouldBeFound("ingredientNutritionValue.equals=" + DEFAULT_INGREDIENT_NUTRITION_VALUE);

        // Get all the recipeIngredientList where ingredientNutritionValue equals to UPDATED_INGREDIENT_NUTRITION_VALUE
        defaultRecipeIngredientShouldNotBeFound("ingredientNutritionValue.equals=" + UPDATED_INGREDIENT_NUTRITION_VALUE);
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByIngredientNutritionValueIsInShouldWork() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where ingredientNutritionValue in DEFAULT_INGREDIENT_NUTRITION_VALUE or UPDATED_INGREDIENT_NUTRITION_VALUE
        defaultRecipeIngredientShouldBeFound("ingredientNutritionValue.in=" + DEFAULT_INGREDIENT_NUTRITION_VALUE + "," + UPDATED_INGREDIENT_NUTRITION_VALUE);

        // Get all the recipeIngredientList where ingredientNutritionValue equals to UPDATED_INGREDIENT_NUTRITION_VALUE
        defaultRecipeIngredientShouldNotBeFound("ingredientNutritionValue.in=" + UPDATED_INGREDIENT_NUTRITION_VALUE);
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByIngredientNutritionValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where ingredientNutritionValue is not null
        defaultRecipeIngredientShouldBeFound("ingredientNutritionValue.specified=true");

        // Get all the recipeIngredientList where ingredientNutritionValue is null
        defaultRecipeIngredientShouldNotBeFound("ingredientNutritionValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByRestrictionIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where restriction equals to DEFAULT_RESTRICTION
        defaultRecipeIngredientShouldBeFound("restriction.equals=" + DEFAULT_RESTRICTION);

        // Get all the recipeIngredientList where restriction equals to UPDATED_RESTRICTION
        defaultRecipeIngredientShouldNotBeFound("restriction.equals=" + UPDATED_RESTRICTION);
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByRestrictionIsInShouldWork() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where restriction in DEFAULT_RESTRICTION or UPDATED_RESTRICTION
        defaultRecipeIngredientShouldBeFound("restriction.in=" + DEFAULT_RESTRICTION + "," + UPDATED_RESTRICTION);

        // Get all the recipeIngredientList where restriction equals to UPDATED_RESTRICTION
        defaultRecipeIngredientShouldNotBeFound("restriction.in=" + UPDATED_RESTRICTION);
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByRestrictionIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList where restriction is not null
        defaultRecipeIngredientShouldBeFound("restriction.specified=true");

        // Get all the recipeIngredientList where restriction is null
        defaultRecipeIngredientShouldNotBeFound("restriction.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeIngredientsByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        recipeIngredient.setRecipe(recipe);
        recipeIngredientRepository.saveAndFlush(recipeIngredient);
        Long recipeId = recipe.getId();

        // Get all the recipeIngredientList where recipe equals to recipeId
        defaultRecipeIngredientShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the recipeIngredientList where recipe equals to recipeId + 1
        defaultRecipeIngredientShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipeIngredientsByMeasurementIsEqualToSomething() throws Exception {
        // Initialize the database
        Measurement measurement = MeasurementResourceIntTest.createEntity(em);
        em.persist(measurement);
        em.flush();
        recipeIngredient.setMeasurement(measurement);
        recipeIngredientRepository.saveAndFlush(recipeIngredient);
        Long measurementId = measurement.getId();

        // Get all the recipeIngredientList where measurement equals to measurementId
        defaultRecipeIngredientShouldBeFound("measurementId.equals=" + measurementId);

        // Get all the recipeIngredientList where measurement equals to measurementId + 1
        defaultRecipeIngredientShouldNotBeFound("measurementId.equals=" + (measurementId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipeIngredientsByIngredientIsEqualToSomething() throws Exception {
        // Initialize the database
        Ingredient ingredient = IngredientResourceIntTest.createEntity(em);
        em.persist(ingredient);
        em.flush();
        recipeIngredient.setIngredient(ingredient);
        recipeIngredientRepository.saveAndFlush(recipeIngredient);
        Long ingredientId = ingredient.getId();

        // Get all the recipeIngredientList where ingredient equals to ingredientId
        defaultRecipeIngredientShouldBeFound("ingredientId.equals=" + ingredientId);

        // Get all the recipeIngredientList where ingredient equals to ingredientId + 1
        defaultRecipeIngredientShouldNotBeFound("ingredientId.equals=" + (ingredientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecipeIngredientShouldBeFound(String filter) throws Exception {
        restRecipeIngredientMockMvc.perform(get("/api/recipe-ingredients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeIngredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].ingredientNutritionValue").value(hasItem(DEFAULT_INGREDIENT_NUTRITION_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].restriction").value(hasItem(DEFAULT_RESTRICTION)));

        // Check, that the count call also returns 1
        restRecipeIngredientMockMvc.perform(get("/api/recipe-ingredients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecipeIngredientShouldNotBeFound(String filter) throws Exception {
        restRecipeIngredientMockMvc.perform(get("/api/recipe-ingredients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeIngredientMockMvc.perform(get("/api/recipe-ingredients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRecipeIngredient() throws Exception {
        // Get the recipeIngredient
        restRecipeIngredientMockMvc.perform(get("/api/recipe-ingredients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipeIngredient() throws Exception {
        // Initialize the database
        recipeIngredientService.save(recipeIngredient);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecipeIngredientSearchRepository);

        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();

        // Update the recipeIngredient
        RecipeIngredient updatedRecipeIngredient = recipeIngredientRepository.findById(recipeIngredient.getId()).get();
        // Disconnect from session so that the updates on updatedRecipeIngredient are not directly saved in db
        em.detach(updatedRecipeIngredient);
        updatedRecipeIngredient
            .amount(UPDATED_AMOUNT)
            .ingredientNutritionValue(UPDATED_INGREDIENT_NUTRITION_VALUE)
            .restriction(UPDATED_RESTRICTION);

        restRecipeIngredientMockMvc.perform(put("/api/recipe-ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecipeIngredient)))
            .andExpect(status().isOk());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
        RecipeIngredient testRecipeIngredient = recipeIngredientList.get(recipeIngredientList.size() - 1);
        assertThat(testRecipeIngredient.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRecipeIngredient.getIngredientNutritionValue()).isEqualTo(UPDATED_INGREDIENT_NUTRITION_VALUE);
        assertThat(testRecipeIngredient.getRestriction()).isEqualTo(UPDATED_RESTRICTION);

        // Validate the RecipeIngredient in Elasticsearch
        verify(mockRecipeIngredientSearchRepository, times(1)).save(testRecipeIngredient);
    }

    @Test
    @Transactional
    public void updateNonExistingRecipeIngredient() throws Exception {
        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();

        // Create the RecipeIngredient

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeIngredientMockMvc.perform(put("/api/recipe-ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeIngredient)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RecipeIngredient in Elasticsearch
        verify(mockRecipeIngredientSearchRepository, times(0)).save(recipeIngredient);
    }

    @Test
    @Transactional
    public void deleteRecipeIngredient() throws Exception {
        // Initialize the database
        recipeIngredientService.save(recipeIngredient);

        int databaseSizeBeforeDelete = recipeIngredientRepository.findAll().size();

        // Delete the recipeIngredient
        restRecipeIngredientMockMvc.perform(delete("/api/recipe-ingredients/{id}", recipeIngredient.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RecipeIngredient in Elasticsearch
        verify(mockRecipeIngredientSearchRepository, times(1)).deleteById(recipeIngredient.getId());
    }

    @Test
    @Transactional
    public void searchRecipeIngredient() throws Exception {
        // Initialize the database
        recipeIngredientService.save(recipeIngredient);
        when(mockRecipeIngredientSearchRepository.search(queryStringQuery("id:" + recipeIngredient.getId())))
            .thenReturn(Collections.singletonList(recipeIngredient));
        // Search the recipeIngredient
        restRecipeIngredientMockMvc.perform(get("/api/_search/recipe-ingredients?query=id:" + recipeIngredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeIngredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].ingredientNutritionValue").value(hasItem(DEFAULT_INGREDIENT_NUTRITION_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].restriction").value(hasItem(DEFAULT_RESTRICTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeIngredient.class);
        RecipeIngredient recipeIngredient1 = new RecipeIngredient();
        recipeIngredient1.setId(1L);
        RecipeIngredient recipeIngredient2 = new RecipeIngredient();
        recipeIngredient2.setId(recipeIngredient1.getId());
        assertThat(recipeIngredient1).isEqualTo(recipeIngredient2);
        recipeIngredient2.setId(2L);
        assertThat(recipeIngredient1).isNotEqualTo(recipeIngredient2);
        recipeIngredient1.setId(null);
        assertThat(recipeIngredient1).isNotEqualTo(recipeIngredient2);
    }
}

package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.Ingredient;
import com.foodiechef.api.domain.RecipeIngredient;
import com.foodiechef.api.domain.IngredientNutritionInfo;
import com.foodiechef.api.repository.IngredientRepository;
import com.foodiechef.api.repository.search.IngredientSearchRepository;
import com.foodiechef.api.service.IngredientService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.IngredientCriteria;
import com.foodiechef.api.service.IngredientQueryService;

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
 * Test class for the IngredientResource REST controller.
 *
 * @see IngredientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class IngredientResourceIntTest {

    private static final String DEFAULT_INGREDIENT = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientService ingredientService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.IngredientSearchRepositoryMockConfiguration
     */
    @Autowired
    private IngredientSearchRepository mockIngredientSearchRepository;

    @Autowired
    private IngredientQueryService ingredientQueryService;

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

    private MockMvc restIngredientMockMvc;

    private Ingredient ingredient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IngredientResource ingredientResource = new IngredientResource(ingredientService, ingredientQueryService);
        this.restIngredientMockMvc = MockMvcBuilders.standaloneSetup(ingredientResource)
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
    public static Ingredient createEntity(EntityManager em) {
        Ingredient ingredient = new Ingredient()
            .ingredient(DEFAULT_INGREDIENT)
            .active(DEFAULT_ACTIVE);
        return ingredient;
    }

    @Before
    public void initTest() {
        ingredient = createEntity(em);
    }

    @Test
    @Transactional
    public void createIngredient() throws Exception {
        int databaseSizeBeforeCreate = ingredientRepository.findAll().size();

        // Create the Ingredient
        restIngredientMockMvc.perform(post("/api/ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredient)))
            .andExpect(status().isCreated());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeCreate + 1);
        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
        assertThat(testIngredient.getIngredient()).isEqualTo(DEFAULT_INGREDIENT);
        assertThat(testIngredient.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Ingredient in Elasticsearch
        verify(mockIngredientSearchRepository, times(1)).save(testIngredient);
    }

    @Test
    @Transactional
    public void createIngredientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ingredientRepository.findAll().size();

        // Create the Ingredient with an existing ID
        ingredient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredientMockMvc.perform(post("/api/ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredient)))
            .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ingredient in Elasticsearch
        verify(mockIngredientSearchRepository, times(0)).save(ingredient);
    }

    @Test
    @Transactional
    public void checkIngredientIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll().size();
        // set the field null
        ingredient.setIngredient(null);

        // Create the Ingredient, which fails.

        restIngredientMockMvc.perform(post("/api/ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredient)))
            .andExpect(status().isBadRequest());

        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll().size();
        // set the field null
        ingredient.setActive(null);

        // Create the Ingredient, which fails.

        restIngredientMockMvc.perform(post("/api/ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredient)))
            .andExpect(status().isBadRequest());

        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIngredients() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredientList
        restIngredientMockMvc.perform(get("/api/ingredients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingredient").value(hasItem(DEFAULT_INGREDIENT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get the ingredient
        restIngredientMockMvc.perform(get("/api/ingredients/{id}", ingredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ingredient.getId().intValue()))
            .andExpect(jsonPath("$.ingredient").value(DEFAULT_INGREDIENT.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllIngredientsByIngredientIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredientList where ingredient equals to DEFAULT_INGREDIENT
        defaultIngredientShouldBeFound("ingredient.equals=" + DEFAULT_INGREDIENT);

        // Get all the ingredientList where ingredient equals to UPDATED_INGREDIENT
        defaultIngredientShouldNotBeFound("ingredient.equals=" + UPDATED_INGREDIENT);
    }

    @Test
    @Transactional
    public void getAllIngredientsByIngredientIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredientList where ingredient in DEFAULT_INGREDIENT or UPDATED_INGREDIENT
        defaultIngredientShouldBeFound("ingredient.in=" + DEFAULT_INGREDIENT + "," + UPDATED_INGREDIENT);

        // Get all the ingredientList where ingredient equals to UPDATED_INGREDIENT
        defaultIngredientShouldNotBeFound("ingredient.in=" + UPDATED_INGREDIENT);
    }

    @Test
    @Transactional
    public void getAllIngredientsByIngredientIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredientList where ingredient is not null
        defaultIngredientShouldBeFound("ingredient.specified=true");

        // Get all the ingredientList where ingredient is null
        defaultIngredientShouldNotBeFound("ingredient.specified=false");
    }

    @Test
    @Transactional
    public void getAllIngredientsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredientList where active equals to DEFAULT_ACTIVE
        defaultIngredientShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the ingredientList where active equals to UPDATED_ACTIVE
        defaultIngredientShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllIngredientsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredientList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultIngredientShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the ingredientList where active equals to UPDATED_ACTIVE
        defaultIngredientShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllIngredientsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredientList where active is not null
        defaultIngredientShouldBeFound("active.specified=true");

        // Get all the ingredientList where active is null
        defaultIngredientShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllIngredientsByRecipeIngredientIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeIngredient recipeIngredient = RecipeIngredientResourceIntTest.createEntity(em);
        em.persist(recipeIngredient);
        em.flush();
        ingredient.addRecipeIngredient(recipeIngredient);
        ingredientRepository.saveAndFlush(ingredient);
        Long recipeIngredientId = recipeIngredient.getId();

        // Get all the ingredientList where recipeIngredient equals to recipeIngredientId
        defaultIngredientShouldBeFound("recipeIngredientId.equals=" + recipeIngredientId);

        // Get all the ingredientList where recipeIngredient equals to recipeIngredientId + 1
        defaultIngredientShouldNotBeFound("recipeIngredientId.equals=" + (recipeIngredientId + 1));
    }


    @Test
    @Transactional
    public void getAllIngredientsByIngredientNutritionInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        IngredientNutritionInfo ingredientNutritionInfo = IngredientNutritionInfoResourceIntTest.createEntity(em);
        em.persist(ingredientNutritionInfo);
        em.flush();
        ingredient.addIngredientNutritionInfo(ingredientNutritionInfo);
        ingredientRepository.saveAndFlush(ingredient);
        Long ingredientNutritionInfoId = ingredientNutritionInfo.getId();

        // Get all the ingredientList where ingredientNutritionInfo equals to ingredientNutritionInfoId
        defaultIngredientShouldBeFound("ingredientNutritionInfoId.equals=" + ingredientNutritionInfoId);

        // Get all the ingredientList where ingredientNutritionInfo equals to ingredientNutritionInfoId + 1
        defaultIngredientShouldNotBeFound("ingredientNutritionInfoId.equals=" + (ingredientNutritionInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultIngredientShouldBeFound(String filter) throws Exception {
        restIngredientMockMvc.perform(get("/api/ingredients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingredient").value(hasItem(DEFAULT_INGREDIENT)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restIngredientMockMvc.perform(get("/api/ingredients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultIngredientShouldNotBeFound(String filter) throws Exception {
        restIngredientMockMvc.perform(get("/api/ingredients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIngredientMockMvc.perform(get("/api/ingredients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingIngredient() throws Exception {
        // Get the ingredient
        restIngredientMockMvc.perform(get("/api/ingredients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIngredient() throws Exception {
        // Initialize the database
        ingredientService.save(ingredient);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockIngredientSearchRepository);

        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();

        // Update the ingredient
        Ingredient updatedIngredient = ingredientRepository.findById(ingredient.getId()).get();
        // Disconnect from session so that the updates on updatedIngredient are not directly saved in db
        em.detach(updatedIngredient);
        updatedIngredient
            .ingredient(UPDATED_INGREDIENT)
            .active(UPDATED_ACTIVE);

        restIngredientMockMvc.perform(put("/api/ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIngredient)))
            .andExpect(status().isOk());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
        assertThat(testIngredient.getIngredient()).isEqualTo(UPDATED_INGREDIENT);
        assertThat(testIngredient.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Ingredient in Elasticsearch
        verify(mockIngredientSearchRepository, times(1)).save(testIngredient);
    }

    @Test
    @Transactional
    public void updateNonExistingIngredient() throws Exception {
        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();

        // Create the Ingredient

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientMockMvc.perform(put("/api/ingredients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ingredient)))
            .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ingredient in Elasticsearch
        verify(mockIngredientSearchRepository, times(0)).save(ingredient);
    }

    @Test
    @Transactional
    public void deleteIngredient() throws Exception {
        // Initialize the database
        ingredientService.save(ingredient);

        int databaseSizeBeforeDelete = ingredientRepository.findAll().size();

        // Delete the ingredient
        restIngredientMockMvc.perform(delete("/api/ingredients/{id}", ingredient.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ingredient in Elasticsearch
        verify(mockIngredientSearchRepository, times(1)).deleteById(ingredient.getId());
    }

    @Test
    @Transactional
    public void searchIngredient() throws Exception {
        // Initialize the database
        ingredientService.save(ingredient);
        when(mockIngredientSearchRepository.search(queryStringQuery("id:" + ingredient.getId())))
            .thenReturn(Collections.singletonList(ingredient));
        // Search the ingredient
        restIngredientMockMvc.perform(get("/api/_search/ingredients?query=id:" + ingredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingredient").value(hasItem(DEFAULT_INGREDIENT)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ingredient.class);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(ingredient1.getId());
        assertThat(ingredient1).isEqualTo(ingredient2);
        ingredient2.setId(2L);
        assertThat(ingredient1).isNotEqualTo(ingredient2);
        ingredient1.setId(null);
        assertThat(ingredient1).isNotEqualTo(ingredient2);
    }
}

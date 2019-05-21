package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.FoodCategorie;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.MenuItem;
import com.foodiechef.api.repository.FoodCategorieRepository;
import com.foodiechef.api.repository.search.FoodCategorieSearchRepository;
import com.foodiechef.api.service.FoodCategorieService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.FoodCategorieCriteria;
import com.foodiechef.api.service.FoodCategorieQueryService;

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
 * Test class for the FoodCategorieResource REST controller.
 *
 * @see FoodCategorieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class FoodCategorieResourceIntTest {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private FoodCategorieRepository foodCategorieRepository;

    @Autowired
    private FoodCategorieService foodCategorieService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.FoodCategorieSearchRepositoryMockConfiguration
     */
    @Autowired
    private FoodCategorieSearchRepository mockFoodCategorieSearchRepository;

    @Autowired
    private FoodCategorieQueryService foodCategorieQueryService;

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

    private MockMvc restFoodCategorieMockMvc;

    private FoodCategorie foodCategorie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FoodCategorieResource foodCategorieResource = new FoodCategorieResource(foodCategorieService, foodCategorieQueryService);
        this.restFoodCategorieMockMvc = MockMvcBuilders.standaloneSetup(foodCategorieResource)
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
    public static FoodCategorie createEntity(EntityManager em) {
        FoodCategorie foodCategorie = new FoodCategorie()
            .active(DEFAULT_ACTIVE)
            .category(DEFAULT_CATEGORY);
        return foodCategorie;
    }

    @Before
    public void initTest() {
        foodCategorie = createEntity(em);
    }

    @Test
    @Transactional
    public void createFoodCategorie() throws Exception {
        int databaseSizeBeforeCreate = foodCategorieRepository.findAll().size();

        // Create the FoodCategorie
        restFoodCategorieMockMvc.perform(post("/api/food-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodCategorie)))
            .andExpect(status().isCreated());

        // Validate the FoodCategorie in the database
        List<FoodCategorie> foodCategorieList = foodCategorieRepository.findAll();
        assertThat(foodCategorieList).hasSize(databaseSizeBeforeCreate + 1);
        FoodCategorie testFoodCategorie = foodCategorieList.get(foodCategorieList.size() - 1);
        assertThat(testFoodCategorie.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testFoodCategorie.getCategory()).isEqualTo(DEFAULT_CATEGORY);

        // Validate the FoodCategorie in Elasticsearch
        verify(mockFoodCategorieSearchRepository, times(1)).save(testFoodCategorie);
    }

    @Test
    @Transactional
    public void createFoodCategorieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = foodCategorieRepository.findAll().size();

        // Create the FoodCategorie with an existing ID
        foodCategorie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodCategorieMockMvc.perform(post("/api/food-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodCategorie)))
            .andExpect(status().isBadRequest());

        // Validate the FoodCategorie in the database
        List<FoodCategorie> foodCategorieList = foodCategorieRepository.findAll();
        assertThat(foodCategorieList).hasSize(databaseSizeBeforeCreate);

        // Validate the FoodCategorie in Elasticsearch
        verify(mockFoodCategorieSearchRepository, times(0)).save(foodCategorie);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = foodCategorieRepository.findAll().size();
        // set the field null
        foodCategorie.setActive(null);

        // Create the FoodCategorie, which fails.

        restFoodCategorieMockMvc.perform(post("/api/food-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodCategorie)))
            .andExpect(status().isBadRequest());

        List<FoodCategorie> foodCategorieList = foodCategorieRepository.findAll();
        assertThat(foodCategorieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = foodCategorieRepository.findAll().size();
        // set the field null
        foodCategorie.setCategory(null);

        // Create the FoodCategorie, which fails.

        restFoodCategorieMockMvc.perform(post("/api/food-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodCategorie)))
            .andExpect(status().isBadRequest());

        List<FoodCategorie> foodCategorieList = foodCategorieRepository.findAll();
        assertThat(foodCategorieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFoodCategories() throws Exception {
        // Initialize the database
        foodCategorieRepository.saveAndFlush(foodCategorie);

        // Get all the foodCategorieList
        restFoodCategorieMockMvc.perform(get("/api/food-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodCategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }
    
    @Test
    @Transactional
    public void getFoodCategorie() throws Exception {
        // Initialize the database
        foodCategorieRepository.saveAndFlush(foodCategorie);

        // Get the foodCategorie
        restFoodCategorieMockMvc.perform(get("/api/food-categories/{id}", foodCategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(foodCategorie.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getAllFoodCategoriesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        foodCategorieRepository.saveAndFlush(foodCategorie);

        // Get all the foodCategorieList where active equals to DEFAULT_ACTIVE
        defaultFoodCategorieShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the foodCategorieList where active equals to UPDATED_ACTIVE
        defaultFoodCategorieShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllFoodCategoriesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        foodCategorieRepository.saveAndFlush(foodCategorie);

        // Get all the foodCategorieList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultFoodCategorieShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the foodCategorieList where active equals to UPDATED_ACTIVE
        defaultFoodCategorieShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllFoodCategoriesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodCategorieRepository.saveAndFlush(foodCategorie);

        // Get all the foodCategorieList where active is not null
        defaultFoodCategorieShouldBeFound("active.specified=true");

        // Get all the foodCategorieList where active is null
        defaultFoodCategorieShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllFoodCategoriesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        foodCategorieRepository.saveAndFlush(foodCategorie);

        // Get all the foodCategorieList where category equals to DEFAULT_CATEGORY
        defaultFoodCategorieShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the foodCategorieList where category equals to UPDATED_CATEGORY
        defaultFoodCategorieShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFoodCategoriesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        foodCategorieRepository.saveAndFlush(foodCategorie);

        // Get all the foodCategorieList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultFoodCategorieShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the foodCategorieList where category equals to UPDATED_CATEGORY
        defaultFoodCategorieShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFoodCategoriesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodCategorieRepository.saveAndFlush(foodCategorie);

        // Get all the foodCategorieList where category is not null
        defaultFoodCategorieShouldBeFound("category.specified=true");

        // Get all the foodCategorieList where category is null
        defaultFoodCategorieShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    public void getAllFoodCategoriesByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        foodCategorie.addRecipe(recipe);
        foodCategorieRepository.saveAndFlush(foodCategorie);
        Long recipeId = recipe.getId();

        // Get all the foodCategorieList where recipe equals to recipeId
        defaultFoodCategorieShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the foodCategorieList where recipe equals to recipeId + 1
        defaultFoodCategorieShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllFoodCategoriesByMenuItemIsEqualToSomething() throws Exception {
        // Initialize the database
        MenuItem menuItem = MenuItemResourceIntTest.createEntity(em);
        em.persist(menuItem);
        em.flush();
        foodCategorie.addMenuItem(menuItem);
        foodCategorieRepository.saveAndFlush(foodCategorie);
        Long menuItemId = menuItem.getId();

        // Get all the foodCategorieList where menuItem equals to menuItemId
        defaultFoodCategorieShouldBeFound("menuItemId.equals=" + menuItemId);

        // Get all the foodCategorieList where menuItem equals to menuItemId + 1
        defaultFoodCategorieShouldNotBeFound("menuItemId.equals=" + (menuItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFoodCategorieShouldBeFound(String filter) throws Exception {
        restFoodCategorieMockMvc.perform(get("/api/food-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodCategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)));

        // Check, that the count call also returns 1
        restFoodCategorieMockMvc.perform(get("/api/food-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFoodCategorieShouldNotBeFound(String filter) throws Exception {
        restFoodCategorieMockMvc.perform(get("/api/food-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFoodCategorieMockMvc.perform(get("/api/food-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFoodCategorie() throws Exception {
        // Get the foodCategorie
        restFoodCategorieMockMvc.perform(get("/api/food-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFoodCategorie() throws Exception {
        // Initialize the database
        foodCategorieService.save(foodCategorie);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFoodCategorieSearchRepository);

        int databaseSizeBeforeUpdate = foodCategorieRepository.findAll().size();

        // Update the foodCategorie
        FoodCategorie updatedFoodCategorie = foodCategorieRepository.findById(foodCategorie.getId()).get();
        // Disconnect from session so that the updates on updatedFoodCategorie are not directly saved in db
        em.detach(updatedFoodCategorie);
        updatedFoodCategorie
            .active(UPDATED_ACTIVE)
            .category(UPDATED_CATEGORY);

        restFoodCategorieMockMvc.perform(put("/api/food-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFoodCategorie)))
            .andExpect(status().isOk());

        // Validate the FoodCategorie in the database
        List<FoodCategorie> foodCategorieList = foodCategorieRepository.findAll();
        assertThat(foodCategorieList).hasSize(databaseSizeBeforeUpdate);
        FoodCategorie testFoodCategorie = foodCategorieList.get(foodCategorieList.size() - 1);
        assertThat(testFoodCategorie.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testFoodCategorie.getCategory()).isEqualTo(UPDATED_CATEGORY);

        // Validate the FoodCategorie in Elasticsearch
        verify(mockFoodCategorieSearchRepository, times(1)).save(testFoodCategorie);
    }

    @Test
    @Transactional
    public void updateNonExistingFoodCategorie() throws Exception {
        int databaseSizeBeforeUpdate = foodCategorieRepository.findAll().size();

        // Create the FoodCategorie

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodCategorieMockMvc.perform(put("/api/food-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodCategorie)))
            .andExpect(status().isBadRequest());

        // Validate the FoodCategorie in the database
        List<FoodCategorie> foodCategorieList = foodCategorieRepository.findAll();
        assertThat(foodCategorieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FoodCategorie in Elasticsearch
        verify(mockFoodCategorieSearchRepository, times(0)).save(foodCategorie);
    }

    @Test
    @Transactional
    public void deleteFoodCategorie() throws Exception {
        // Initialize the database
        foodCategorieService.save(foodCategorie);

        int databaseSizeBeforeDelete = foodCategorieRepository.findAll().size();

        // Delete the foodCategorie
        restFoodCategorieMockMvc.perform(delete("/api/food-categories/{id}", foodCategorie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FoodCategorie> foodCategorieList = foodCategorieRepository.findAll();
        assertThat(foodCategorieList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FoodCategorie in Elasticsearch
        verify(mockFoodCategorieSearchRepository, times(1)).deleteById(foodCategorie.getId());
    }

    @Test
    @Transactional
    public void searchFoodCategorie() throws Exception {
        // Initialize the database
        foodCategorieService.save(foodCategorie);
        when(mockFoodCategorieSearchRepository.search(queryStringQuery("id:" + foodCategorie.getId())))
            .thenReturn(Collections.singletonList(foodCategorie));
        // Search the foodCategorie
        restFoodCategorieMockMvc.perform(get("/api/_search/food-categories?query=id:" + foodCategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodCategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodCategorie.class);
        FoodCategorie foodCategorie1 = new FoodCategorie();
        foodCategorie1.setId(1L);
        FoodCategorie foodCategorie2 = new FoodCategorie();
        foodCategorie2.setId(foodCategorie1.getId());
        assertThat(foodCategorie1).isEqualTo(foodCategorie2);
        foodCategorie2.setId(2L);
        assertThat(foodCategorie1).isNotEqualTo(foodCategorie2);
        foodCategorie1.setId(null);
        assertThat(foodCategorie1).isNotEqualTo(foodCategorie2);
    }
}

package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.RecipeHasStep;
import com.foodiechef.api.domain.RecipeImage;
import com.foodiechef.api.domain.MenuRecipe;
import com.foodiechef.api.domain.RecipeComment;
import com.foodiechef.api.domain.RecipeRating;
import com.foodiechef.api.domain.ShareRecipe;
import com.foodiechef.api.domain.RecipeLike;
import com.foodiechef.api.domain.Footnote;
import com.foodiechef.api.domain.RecipeIngredient;
import com.foodiechef.api.domain.Level;
import com.foodiechef.api.domain.FoodCategorie;
import com.foodiechef.api.domain.Cusine;
import com.foodiechef.api.domain.Course;
import com.foodiechef.api.repository.RecipeRepository;
import com.foodiechef.api.repository.search.RecipeSearchRepository;
import com.foodiechef.api.service.RecipeService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RecipeCriteria;
import com.foodiechef.api.service.RecipeQueryService;

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
 * Test class for the RecipeResource REST controller.
 *
 * @see RecipeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RecipeResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Integer DEFAULT_CALORIES_PER_SERVINGS = 1;
    private static final Integer UPDATED_CALORIES_PER_SERVINGS = 2;

    private static final Integer DEFAULT_COOK_TIME = 1;
    private static final Integer UPDATED_COOK_TIME = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREP_TIME = 1;
    private static final Integer UPDATED_PREP_TIME = 2;

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final Integer DEFAULT_READY_IN = 1;
    private static final Integer UPDATED_READY_IN = 2;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO = "BBBBBBBBBB";

    private static final Integer DEFAULT_YIELDS = 1;
    private static final Integer UPDATED_YIELDS = 2;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RecipeSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeSearchRepository mockRecipeSearchRepository;

    @Autowired
    private RecipeQueryService recipeQueryService;

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

    private MockMvc restRecipeMockMvc;

    private Recipe recipe;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecipeResource recipeResource = new RecipeResource(recipeService, recipeQueryService);
        this.restRecipeMockMvc = MockMvcBuilders.standaloneSetup(recipeResource)
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
    public static Recipe createEntity(EntityManager em) {
        Recipe recipe = new Recipe()
            .amount(DEFAULT_AMOUNT)
            .caloriesPerServings(DEFAULT_CALORIES_PER_SERVINGS)
            .cookTime(DEFAULT_COOK_TIME)
            .description(DEFAULT_DESCRIPTION)
            .prepTime(DEFAULT_PREP_TIME)
            .published(DEFAULT_PUBLISHED)
            .rating(DEFAULT_RATING)
            .readyIn(DEFAULT_READY_IN)
            .title(DEFAULT_TITLE)
            .video(DEFAULT_VIDEO)
            .yields(DEFAULT_YIELDS);
        return recipe;
    }

    @Before
    public void initTest() {
        recipe = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecipe() throws Exception {
        int databaseSizeBeforeCreate = recipeRepository.findAll().size();

        // Create the Recipe
        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isCreated());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate + 1);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRecipe.getCaloriesPerServings()).isEqualTo(DEFAULT_CALORIES_PER_SERVINGS);
        assertThat(testRecipe.getCookTime()).isEqualTo(DEFAULT_COOK_TIME);
        assertThat(testRecipe.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRecipe.getPrepTime()).isEqualTo(DEFAULT_PREP_TIME);
        assertThat(testRecipe.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testRecipe.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testRecipe.getReadyIn()).isEqualTo(DEFAULT_READY_IN);
        assertThat(testRecipe.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRecipe.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testRecipe.getYields()).isEqualTo(DEFAULT_YIELDS);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(1)).save(testRecipe);
    }

    @Test
    @Transactional
    public void createRecipeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recipeRepository.findAll().size();

        // Create the Recipe with an existing ID
        recipe.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(0)).save(recipe);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setAmount(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCaloriesPerServingsIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setCaloriesPerServings(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCookTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setCookTime(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setDescription(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrepTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setPrepTime(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setPublished(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setRating(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReadyInIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setReadyIn(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setTitle(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVideoIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setVideo(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYieldsIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setYields(null);

        // Create the Recipe, which fails.

        restRecipeMockMvc.perform(post("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecipes() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList
        restRecipeMockMvc.perform(get("/api/recipes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].caloriesPerServings").value(hasItem(DEFAULT_CALORIES_PER_SERVINGS)))
            .andExpect(jsonPath("$.[*].cookTime").value(hasItem(DEFAULT_COOK_TIME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].prepTime").value(hasItem(DEFAULT_PREP_TIME)))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].readyIn").value(hasItem(DEFAULT_READY_IN)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].video").value(hasItem(DEFAULT_VIDEO.toString())))
            .andExpect(jsonPath("$.[*].yields").value(hasItem(DEFAULT_YIELDS)));
    }
    
    @Test
    @Transactional
    public void getRecipe() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get the recipe
        restRecipeMockMvc.perform(get("/api/recipes/{id}", recipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recipe.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.caloriesPerServings").value(DEFAULT_CALORIES_PER_SERVINGS))
            .andExpect(jsonPath("$.cookTime").value(DEFAULT_COOK_TIME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.prepTime").value(DEFAULT_PREP_TIME))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.readyIn").value(DEFAULT_READY_IN))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.video").value(DEFAULT_VIDEO.toString()))
            .andExpect(jsonPath("$.yields").value(DEFAULT_YIELDS));
    }

    @Test
    @Transactional
    public void getAllRecipesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where amount equals to DEFAULT_AMOUNT
        defaultRecipeShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the recipeList where amount equals to UPDATED_AMOUNT
        defaultRecipeShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRecipesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultRecipeShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the recipeList where amount equals to UPDATED_AMOUNT
        defaultRecipeShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllRecipesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where amount is not null
        defaultRecipeShouldBeFound("amount.specified=true");

        // Get all the recipeList where amount is null
        defaultRecipeShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByCaloriesPerServingsIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where caloriesPerServings equals to DEFAULT_CALORIES_PER_SERVINGS
        defaultRecipeShouldBeFound("caloriesPerServings.equals=" + DEFAULT_CALORIES_PER_SERVINGS);

        // Get all the recipeList where caloriesPerServings equals to UPDATED_CALORIES_PER_SERVINGS
        defaultRecipeShouldNotBeFound("caloriesPerServings.equals=" + UPDATED_CALORIES_PER_SERVINGS);
    }

    @Test
    @Transactional
    public void getAllRecipesByCaloriesPerServingsIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where caloriesPerServings in DEFAULT_CALORIES_PER_SERVINGS or UPDATED_CALORIES_PER_SERVINGS
        defaultRecipeShouldBeFound("caloriesPerServings.in=" + DEFAULT_CALORIES_PER_SERVINGS + "," + UPDATED_CALORIES_PER_SERVINGS);

        // Get all the recipeList where caloriesPerServings equals to UPDATED_CALORIES_PER_SERVINGS
        defaultRecipeShouldNotBeFound("caloriesPerServings.in=" + UPDATED_CALORIES_PER_SERVINGS);
    }

    @Test
    @Transactional
    public void getAllRecipesByCaloriesPerServingsIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where caloriesPerServings is not null
        defaultRecipeShouldBeFound("caloriesPerServings.specified=true");

        // Get all the recipeList where caloriesPerServings is null
        defaultRecipeShouldNotBeFound("caloriesPerServings.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByCaloriesPerServingsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where caloriesPerServings greater than or equals to DEFAULT_CALORIES_PER_SERVINGS
        defaultRecipeShouldBeFound("caloriesPerServings.greaterOrEqualThan=" + DEFAULT_CALORIES_PER_SERVINGS);

        // Get all the recipeList where caloriesPerServings greater than or equals to UPDATED_CALORIES_PER_SERVINGS
        defaultRecipeShouldNotBeFound("caloriesPerServings.greaterOrEqualThan=" + UPDATED_CALORIES_PER_SERVINGS);
    }

    @Test
    @Transactional
    public void getAllRecipesByCaloriesPerServingsIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where caloriesPerServings less than or equals to DEFAULT_CALORIES_PER_SERVINGS
        defaultRecipeShouldNotBeFound("caloriesPerServings.lessThan=" + DEFAULT_CALORIES_PER_SERVINGS);

        // Get all the recipeList where caloriesPerServings less than or equals to UPDATED_CALORIES_PER_SERVINGS
        defaultRecipeShouldBeFound("caloriesPerServings.lessThan=" + UPDATED_CALORIES_PER_SERVINGS);
    }


    @Test
    @Transactional
    public void getAllRecipesByCookTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where cookTime equals to DEFAULT_COOK_TIME
        defaultRecipeShouldBeFound("cookTime.equals=" + DEFAULT_COOK_TIME);

        // Get all the recipeList where cookTime equals to UPDATED_COOK_TIME
        defaultRecipeShouldNotBeFound("cookTime.equals=" + UPDATED_COOK_TIME);
    }

    @Test
    @Transactional
    public void getAllRecipesByCookTimeIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where cookTime in DEFAULT_COOK_TIME or UPDATED_COOK_TIME
        defaultRecipeShouldBeFound("cookTime.in=" + DEFAULT_COOK_TIME + "," + UPDATED_COOK_TIME);

        // Get all the recipeList where cookTime equals to UPDATED_COOK_TIME
        defaultRecipeShouldNotBeFound("cookTime.in=" + UPDATED_COOK_TIME);
    }

    @Test
    @Transactional
    public void getAllRecipesByCookTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where cookTime is not null
        defaultRecipeShouldBeFound("cookTime.specified=true");

        // Get all the recipeList where cookTime is null
        defaultRecipeShouldNotBeFound("cookTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByCookTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where cookTime greater than or equals to DEFAULT_COOK_TIME
        defaultRecipeShouldBeFound("cookTime.greaterOrEqualThan=" + DEFAULT_COOK_TIME);

        // Get all the recipeList where cookTime greater than or equals to UPDATED_COOK_TIME
        defaultRecipeShouldNotBeFound("cookTime.greaterOrEqualThan=" + UPDATED_COOK_TIME);
    }

    @Test
    @Transactional
    public void getAllRecipesByCookTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where cookTime less than or equals to DEFAULT_COOK_TIME
        defaultRecipeShouldNotBeFound("cookTime.lessThan=" + DEFAULT_COOK_TIME);

        // Get all the recipeList where cookTime less than or equals to UPDATED_COOK_TIME
        defaultRecipeShouldBeFound("cookTime.lessThan=" + UPDATED_COOK_TIME);
    }


    @Test
    @Transactional
    public void getAllRecipesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where description equals to DEFAULT_DESCRIPTION
        defaultRecipeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the recipeList where description equals to UPDATED_DESCRIPTION
        defaultRecipeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRecipesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRecipeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the recipeList where description equals to UPDATED_DESCRIPTION
        defaultRecipeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRecipesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where description is not null
        defaultRecipeShouldBeFound("description.specified=true");

        // Get all the recipeList where description is null
        defaultRecipeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByPrepTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where prepTime equals to DEFAULT_PREP_TIME
        defaultRecipeShouldBeFound("prepTime.equals=" + DEFAULT_PREP_TIME);

        // Get all the recipeList where prepTime equals to UPDATED_PREP_TIME
        defaultRecipeShouldNotBeFound("prepTime.equals=" + UPDATED_PREP_TIME);
    }

    @Test
    @Transactional
    public void getAllRecipesByPrepTimeIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where prepTime in DEFAULT_PREP_TIME or UPDATED_PREP_TIME
        defaultRecipeShouldBeFound("prepTime.in=" + DEFAULT_PREP_TIME + "," + UPDATED_PREP_TIME);

        // Get all the recipeList where prepTime equals to UPDATED_PREP_TIME
        defaultRecipeShouldNotBeFound("prepTime.in=" + UPDATED_PREP_TIME);
    }

    @Test
    @Transactional
    public void getAllRecipesByPrepTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where prepTime is not null
        defaultRecipeShouldBeFound("prepTime.specified=true");

        // Get all the recipeList where prepTime is null
        defaultRecipeShouldNotBeFound("prepTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByPrepTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where prepTime greater than or equals to DEFAULT_PREP_TIME
        defaultRecipeShouldBeFound("prepTime.greaterOrEqualThan=" + DEFAULT_PREP_TIME);

        // Get all the recipeList where prepTime greater than or equals to UPDATED_PREP_TIME
        defaultRecipeShouldNotBeFound("prepTime.greaterOrEqualThan=" + UPDATED_PREP_TIME);
    }

    @Test
    @Transactional
    public void getAllRecipesByPrepTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where prepTime less than or equals to DEFAULT_PREP_TIME
        defaultRecipeShouldNotBeFound("prepTime.lessThan=" + DEFAULT_PREP_TIME);

        // Get all the recipeList where prepTime less than or equals to UPDATED_PREP_TIME
        defaultRecipeShouldBeFound("prepTime.lessThan=" + UPDATED_PREP_TIME);
    }


    @Test
    @Transactional
    public void getAllRecipesByPublishedIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where published equals to DEFAULT_PUBLISHED
        defaultRecipeShouldBeFound("published.equals=" + DEFAULT_PUBLISHED);

        // Get all the recipeList where published equals to UPDATED_PUBLISHED
        defaultRecipeShouldNotBeFound("published.equals=" + UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    public void getAllRecipesByPublishedIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where published in DEFAULT_PUBLISHED or UPDATED_PUBLISHED
        defaultRecipeShouldBeFound("published.in=" + DEFAULT_PUBLISHED + "," + UPDATED_PUBLISHED);

        // Get all the recipeList where published equals to UPDATED_PUBLISHED
        defaultRecipeShouldNotBeFound("published.in=" + UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    public void getAllRecipesByPublishedIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where published is not null
        defaultRecipeShouldBeFound("published.specified=true");

        // Get all the recipeList where published is null
        defaultRecipeShouldNotBeFound("published.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where rating equals to DEFAULT_RATING
        defaultRecipeShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the recipeList where rating equals to UPDATED_RATING
        defaultRecipeShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllRecipesByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultRecipeShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the recipeList where rating equals to UPDATED_RATING
        defaultRecipeShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllRecipesByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where rating is not null
        defaultRecipeShouldBeFound("rating.specified=true");

        // Get all the recipeList where rating is null
        defaultRecipeShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where rating greater than or equals to DEFAULT_RATING
        defaultRecipeShouldBeFound("rating.greaterOrEqualThan=" + DEFAULT_RATING);

        // Get all the recipeList where rating greater than or equals to UPDATED_RATING
        defaultRecipeShouldNotBeFound("rating.greaterOrEqualThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllRecipesByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where rating less than or equals to DEFAULT_RATING
        defaultRecipeShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the recipeList where rating less than or equals to UPDATED_RATING
        defaultRecipeShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }


    @Test
    @Transactional
    public void getAllRecipesByReadyInIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where readyIn equals to DEFAULT_READY_IN
        defaultRecipeShouldBeFound("readyIn.equals=" + DEFAULT_READY_IN);

        // Get all the recipeList where readyIn equals to UPDATED_READY_IN
        defaultRecipeShouldNotBeFound("readyIn.equals=" + UPDATED_READY_IN);
    }

    @Test
    @Transactional
    public void getAllRecipesByReadyInIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where readyIn in DEFAULT_READY_IN or UPDATED_READY_IN
        defaultRecipeShouldBeFound("readyIn.in=" + DEFAULT_READY_IN + "," + UPDATED_READY_IN);

        // Get all the recipeList where readyIn equals to UPDATED_READY_IN
        defaultRecipeShouldNotBeFound("readyIn.in=" + UPDATED_READY_IN);
    }

    @Test
    @Transactional
    public void getAllRecipesByReadyInIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where readyIn is not null
        defaultRecipeShouldBeFound("readyIn.specified=true");

        // Get all the recipeList where readyIn is null
        defaultRecipeShouldNotBeFound("readyIn.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByReadyInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where readyIn greater than or equals to DEFAULT_READY_IN
        defaultRecipeShouldBeFound("readyIn.greaterOrEqualThan=" + DEFAULT_READY_IN);

        // Get all the recipeList where readyIn greater than or equals to UPDATED_READY_IN
        defaultRecipeShouldNotBeFound("readyIn.greaterOrEqualThan=" + UPDATED_READY_IN);
    }

    @Test
    @Transactional
    public void getAllRecipesByReadyInIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where readyIn less than or equals to DEFAULT_READY_IN
        defaultRecipeShouldNotBeFound("readyIn.lessThan=" + DEFAULT_READY_IN);

        // Get all the recipeList where readyIn less than or equals to UPDATED_READY_IN
        defaultRecipeShouldBeFound("readyIn.lessThan=" + UPDATED_READY_IN);
    }


    @Test
    @Transactional
    public void getAllRecipesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where title equals to DEFAULT_TITLE
        defaultRecipeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the recipeList where title equals to UPDATED_TITLE
        defaultRecipeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRecipesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultRecipeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the recipeList where title equals to UPDATED_TITLE
        defaultRecipeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRecipesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where title is not null
        defaultRecipeShouldBeFound("title.specified=true");

        // Get all the recipeList where title is null
        defaultRecipeShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByVideoIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where video equals to DEFAULT_VIDEO
        defaultRecipeShouldBeFound("video.equals=" + DEFAULT_VIDEO);

        // Get all the recipeList where video equals to UPDATED_VIDEO
        defaultRecipeShouldNotBeFound("video.equals=" + UPDATED_VIDEO);
    }

    @Test
    @Transactional
    public void getAllRecipesByVideoIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where video in DEFAULT_VIDEO or UPDATED_VIDEO
        defaultRecipeShouldBeFound("video.in=" + DEFAULT_VIDEO + "," + UPDATED_VIDEO);

        // Get all the recipeList where video equals to UPDATED_VIDEO
        defaultRecipeShouldNotBeFound("video.in=" + UPDATED_VIDEO);
    }

    @Test
    @Transactional
    public void getAllRecipesByVideoIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where video is not null
        defaultRecipeShouldBeFound("video.specified=true");

        // Get all the recipeList where video is null
        defaultRecipeShouldNotBeFound("video.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByYieldsIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where yields equals to DEFAULT_YIELDS
        defaultRecipeShouldBeFound("yields.equals=" + DEFAULT_YIELDS);

        // Get all the recipeList where yields equals to UPDATED_YIELDS
        defaultRecipeShouldNotBeFound("yields.equals=" + UPDATED_YIELDS);
    }

    @Test
    @Transactional
    public void getAllRecipesByYieldsIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where yields in DEFAULT_YIELDS or UPDATED_YIELDS
        defaultRecipeShouldBeFound("yields.in=" + DEFAULT_YIELDS + "," + UPDATED_YIELDS);

        // Get all the recipeList where yields equals to UPDATED_YIELDS
        defaultRecipeShouldNotBeFound("yields.in=" + UPDATED_YIELDS);
    }

    @Test
    @Transactional
    public void getAllRecipesByYieldsIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where yields is not null
        defaultRecipeShouldBeFound("yields.specified=true");

        // Get all the recipeList where yields is null
        defaultRecipeShouldNotBeFound("yields.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipesByYieldsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where yields greater than or equals to DEFAULT_YIELDS
        defaultRecipeShouldBeFound("yields.greaterOrEqualThan=" + DEFAULT_YIELDS);

        // Get all the recipeList where yields greater than or equals to UPDATED_YIELDS
        defaultRecipeShouldNotBeFound("yields.greaterOrEqualThan=" + UPDATED_YIELDS);
    }

    @Test
    @Transactional
    public void getAllRecipesByYieldsIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where yields less than or equals to DEFAULT_YIELDS
        defaultRecipeShouldNotBeFound("yields.lessThan=" + DEFAULT_YIELDS);

        // Get all the recipeList where yields less than or equals to UPDATED_YIELDS
        defaultRecipeShouldBeFound("yields.lessThan=" + UPDATED_YIELDS);
    }


    @Test
    @Transactional
    public void getAllRecipesByRecipeHasStepIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeHasStep recipeHasStep = RecipeHasStepResourceIntTest.createEntity(em);
        em.persist(recipeHasStep);
        em.flush();
        recipe.addRecipeHasStep(recipeHasStep);
        recipeRepository.saveAndFlush(recipe);
        Long recipeHasStepId = recipeHasStep.getId();

        // Get all the recipeList where recipeHasStep equals to recipeHasStepId
        defaultRecipeShouldBeFound("recipeHasStepId.equals=" + recipeHasStepId);

        // Get all the recipeList where recipeHasStep equals to recipeHasStepId + 1
        defaultRecipeShouldNotBeFound("recipeHasStepId.equals=" + (recipeHasStepId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByRecipeImageIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeImage recipeImage = RecipeImageResourceIntTest.createEntity(em);
        em.persist(recipeImage);
        em.flush();
        recipe.addRecipeImage(recipeImage);
        recipeRepository.saveAndFlush(recipe);
        Long recipeImageId = recipeImage.getId();

        // Get all the recipeList where recipeImage equals to recipeImageId
        defaultRecipeShouldBeFound("recipeImageId.equals=" + recipeImageId);

        // Get all the recipeList where recipeImage equals to recipeImageId + 1
        defaultRecipeShouldNotBeFound("recipeImageId.equals=" + (recipeImageId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByMenuRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        MenuRecipe menuRecipe = MenuRecipeResourceIntTest.createEntity(em);
        em.persist(menuRecipe);
        em.flush();
        recipe.addMenuRecipe(menuRecipe);
        recipeRepository.saveAndFlush(recipe);
        Long menuRecipeId = menuRecipe.getId();

        // Get all the recipeList where menuRecipe equals to menuRecipeId
        defaultRecipeShouldBeFound("menuRecipeId.equals=" + menuRecipeId);

        // Get all the recipeList where menuRecipe equals to menuRecipeId + 1
        defaultRecipeShouldNotBeFound("menuRecipeId.equals=" + (menuRecipeId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByRecipeCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeComment recipeComment = RecipeCommentResourceIntTest.createEntity(em);
        em.persist(recipeComment);
        em.flush();
        recipe.addRecipeComment(recipeComment);
        recipeRepository.saveAndFlush(recipe);
        Long recipeCommentId = recipeComment.getId();

        // Get all the recipeList where recipeComment equals to recipeCommentId
        defaultRecipeShouldBeFound("recipeCommentId.equals=" + recipeCommentId);

        // Get all the recipeList where recipeComment equals to recipeCommentId + 1
        defaultRecipeShouldNotBeFound("recipeCommentId.equals=" + (recipeCommentId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByRecipeRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeRating recipeRating = RecipeRatingResourceIntTest.createEntity(em);
        em.persist(recipeRating);
        em.flush();
        recipe.addRecipeRating(recipeRating);
        recipeRepository.saveAndFlush(recipe);
        Long recipeRatingId = recipeRating.getId();

        // Get all the recipeList where recipeRating equals to recipeRatingId
        defaultRecipeShouldBeFound("recipeRatingId.equals=" + recipeRatingId);

        // Get all the recipeList where recipeRating equals to recipeRatingId + 1
        defaultRecipeShouldNotBeFound("recipeRatingId.equals=" + (recipeRatingId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByShareRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        ShareRecipe shareRecipe = ShareRecipeResourceIntTest.createEntity(em);
        em.persist(shareRecipe);
        em.flush();
        recipe.addShareRecipe(shareRecipe);
        recipeRepository.saveAndFlush(recipe);
        Long shareRecipeId = shareRecipe.getId();

        // Get all the recipeList where shareRecipe equals to shareRecipeId
        defaultRecipeShouldBeFound("shareRecipeId.equals=" + shareRecipeId);

        // Get all the recipeList where shareRecipe equals to shareRecipeId + 1
        defaultRecipeShouldNotBeFound("shareRecipeId.equals=" + (shareRecipeId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByRecipeLikeIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeLike recipeLike = RecipeLikeResourceIntTest.createEntity(em);
        em.persist(recipeLike);
        em.flush();
        recipe.addRecipeLike(recipeLike);
        recipeRepository.saveAndFlush(recipe);
        Long recipeLikeId = recipeLike.getId();

        // Get all the recipeList where recipeLike equals to recipeLikeId
        defaultRecipeShouldBeFound("recipeLikeId.equals=" + recipeLikeId);

        // Get all the recipeList where recipeLike equals to recipeLikeId + 1
        defaultRecipeShouldNotBeFound("recipeLikeId.equals=" + (recipeLikeId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByFootnoteIsEqualToSomething() throws Exception {
        // Initialize the database
        Footnote footnote = FootnoteResourceIntTest.createEntity(em);
        em.persist(footnote);
        em.flush();
        recipe.addFootnote(footnote);
        recipeRepository.saveAndFlush(recipe);
        Long footnoteId = footnote.getId();

        // Get all the recipeList where footnote equals to footnoteId
        defaultRecipeShouldBeFound("footnoteId.equals=" + footnoteId);

        // Get all the recipeList where footnote equals to footnoteId + 1
        defaultRecipeShouldNotBeFound("footnoteId.equals=" + (footnoteId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByRecipeIngredientIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeIngredient recipeIngredient = RecipeIngredientResourceIntTest.createEntity(em);
        em.persist(recipeIngredient);
        em.flush();
        recipe.addRecipeIngredient(recipeIngredient);
        recipeRepository.saveAndFlush(recipe);
        Long recipeIngredientId = recipeIngredient.getId();

        // Get all the recipeList where recipeIngredient equals to recipeIngredientId
        defaultRecipeShouldBeFound("recipeIngredientId.equals=" + recipeIngredientId);

        // Get all the recipeList where recipeIngredient equals to recipeIngredientId + 1
        defaultRecipeShouldNotBeFound("recipeIngredientId.equals=" + (recipeIngredientId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        Level level = LevelResourceIntTest.createEntity(em);
        em.persist(level);
        em.flush();
        recipe.setLevel(level);
        recipeRepository.saveAndFlush(recipe);
        Long levelId = level.getId();

        // Get all the recipeList where level equals to levelId
        defaultRecipeShouldBeFound("levelId.equals=" + levelId);

        // Get all the recipeList where level equals to levelId + 1
        defaultRecipeShouldNotBeFound("levelId.equals=" + (levelId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByFoodCategorieIsEqualToSomething() throws Exception {
        // Initialize the database
        FoodCategorie foodCategorie = FoodCategorieResourceIntTest.createEntity(em);
        em.persist(foodCategorie);
        em.flush();
        recipe.setFoodCategorie(foodCategorie);
        recipeRepository.saveAndFlush(recipe);
        Long foodCategorieId = foodCategorie.getId();

        // Get all the recipeList where foodCategorie equals to foodCategorieId
        defaultRecipeShouldBeFound("foodCategorieId.equals=" + foodCategorieId);

        // Get all the recipeList where foodCategorie equals to foodCategorieId + 1
        defaultRecipeShouldNotBeFound("foodCategorieId.equals=" + (foodCategorieId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByCusineIsEqualToSomething() throws Exception {
        // Initialize the database
        Cusine cusine = CusineResourceIntTest.createEntity(em);
        em.persist(cusine);
        em.flush();
        recipe.setCusine(cusine);
        recipeRepository.saveAndFlush(recipe);
        Long cusineId = cusine.getId();

        // Get all the recipeList where cusine equals to cusineId
        defaultRecipeShouldBeFound("cusineId.equals=" + cusineId);

        // Get all the recipeList where cusine equals to cusineId + 1
        defaultRecipeShouldNotBeFound("cusineId.equals=" + (cusineId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipesByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        Course course = CourseResourceIntTest.createEntity(em);
        em.persist(course);
        em.flush();
        recipe.setCourse(course);
        recipeRepository.saveAndFlush(recipe);
        Long courseId = course.getId();

        // Get all the recipeList where course equals to courseId
        defaultRecipeShouldBeFound("courseId.equals=" + courseId);

        // Get all the recipeList where course equals to courseId + 1
        defaultRecipeShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecipeShouldBeFound(String filter) throws Exception {
        restRecipeMockMvc.perform(get("/api/recipes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].caloriesPerServings").value(hasItem(DEFAULT_CALORIES_PER_SERVINGS)))
            .andExpect(jsonPath("$.[*].cookTime").value(hasItem(DEFAULT_COOK_TIME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].prepTime").value(hasItem(DEFAULT_PREP_TIME)))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].readyIn").value(hasItem(DEFAULT_READY_IN)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(DEFAULT_VIDEO)))
            .andExpect(jsonPath("$.[*].yields").value(hasItem(DEFAULT_YIELDS)));

        // Check, that the count call also returns 1
        restRecipeMockMvc.perform(get("/api/recipes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecipeShouldNotBeFound(String filter) throws Exception {
        restRecipeMockMvc.perform(get("/api/recipes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeMockMvc.perform(get("/api/recipes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRecipe() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get("/api/recipes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipe() throws Exception {
        // Initialize the database
        recipeService.save(recipe);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecipeSearchRepository);

        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Update the recipe
        Recipe updatedRecipe = recipeRepository.findById(recipe.getId()).get();
        // Disconnect from session so that the updates on updatedRecipe are not directly saved in db
        em.detach(updatedRecipe);
        updatedRecipe
            .amount(UPDATED_AMOUNT)
            .caloriesPerServings(UPDATED_CALORIES_PER_SERVINGS)
            .cookTime(UPDATED_COOK_TIME)
            .description(UPDATED_DESCRIPTION)
            .prepTime(UPDATED_PREP_TIME)
            .published(UPDATED_PUBLISHED)
            .rating(UPDATED_RATING)
            .readyIn(UPDATED_READY_IN)
            .title(UPDATED_TITLE)
            .video(UPDATED_VIDEO)
            .yields(UPDATED_YIELDS);

        restRecipeMockMvc.perform(put("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecipe)))
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRecipe.getCaloriesPerServings()).isEqualTo(UPDATED_CALORIES_PER_SERVINGS);
        assertThat(testRecipe.getCookTime()).isEqualTo(UPDATED_COOK_TIME);
        assertThat(testRecipe.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRecipe.getPrepTime()).isEqualTo(UPDATED_PREP_TIME);
        assertThat(testRecipe.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testRecipe.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testRecipe.getReadyIn()).isEqualTo(UPDATED_READY_IN);
        assertThat(testRecipe.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRecipe.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testRecipe.getYields()).isEqualTo(UPDATED_YIELDS);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(1)).save(testRecipe);
    }

    @Test
    @Transactional
    public void updateNonExistingRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Create the Recipe

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc.perform(put("/api/recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipe)))
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(0)).save(recipe);
    }

    @Test
    @Transactional
    public void deleteRecipe() throws Exception {
        // Initialize the database
        recipeService.save(recipe);

        int databaseSizeBeforeDelete = recipeRepository.findAll().size();

        // Delete the recipe
        restRecipeMockMvc.perform(delete("/api/recipes/{id}", recipe.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Recipe in Elasticsearch
        verify(mockRecipeSearchRepository, times(1)).deleteById(recipe.getId());
    }

    @Test
    @Transactional
    public void searchRecipe() throws Exception {
        // Initialize the database
        recipeService.save(recipe);
        when(mockRecipeSearchRepository.search(queryStringQuery("id:" + recipe.getId())))
            .thenReturn(Collections.singletonList(recipe));
        // Search the recipe
        restRecipeMockMvc.perform(get("/api/_search/recipes?query=id:" + recipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].caloriesPerServings").value(hasItem(DEFAULT_CALORIES_PER_SERVINGS)))
            .andExpect(jsonPath("$.[*].cookTime").value(hasItem(DEFAULT_COOK_TIME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].prepTime").value(hasItem(DEFAULT_PREP_TIME)))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].readyIn").value(hasItem(DEFAULT_READY_IN)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(DEFAULT_VIDEO)))
            .andExpect(jsonPath("$.[*].yields").value(hasItem(DEFAULT_YIELDS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recipe.class);
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        Recipe recipe2 = new Recipe();
        recipe2.setId(recipe1.getId());
        assertThat(recipe1).isEqualTo(recipe2);
        recipe2.setId(2L);
        assertThat(recipe1).isNotEqualTo(recipe2);
        recipe1.setId(null);
        assertThat(recipe1).isNotEqualTo(recipe2);
    }
}

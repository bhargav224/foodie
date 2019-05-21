package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.RecipeRating;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.RecipeRatingRepository;
import com.foodiechef.api.repository.search.RecipeRatingSearchRepository;
import com.foodiechef.api.service.RecipeRatingService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RecipeRatingCriteria;
import com.foodiechef.api.service.RecipeRatingQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Test class for the RecipeRatingResource REST controller.
 *
 * @see RecipeRatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RecipeRatingResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    @Autowired
    private RecipeRatingRepository recipeRatingRepository;

    @Autowired
    private RecipeRatingService recipeRatingService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RecipeRatingSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeRatingSearchRepository mockRecipeRatingSearchRepository;

    @Autowired
    private RecipeRatingQueryService recipeRatingQueryService;

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

    private MockMvc restRecipeRatingMockMvc;

    private RecipeRating recipeRating;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecipeRatingResource recipeRatingResource = new RecipeRatingResource(recipeRatingService, recipeRatingQueryService);
        this.restRecipeRatingMockMvc = MockMvcBuilders.standaloneSetup(recipeRatingResource)
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
    public static RecipeRating createEntity(EntityManager em) {
        RecipeRating recipeRating = new RecipeRating()
            .date(DEFAULT_DATE)
            .rating(DEFAULT_RATING);
        return recipeRating;
    }

    @Before
    public void initTest() {
        recipeRating = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecipeRating() throws Exception {
        int databaseSizeBeforeCreate = recipeRatingRepository.findAll().size();

        // Create the RecipeRating
        restRecipeRatingMockMvc.perform(post("/api/recipe-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeRating)))
            .andExpect(status().isCreated());

        // Validate the RecipeRating in the database
        List<RecipeRating> recipeRatingList = recipeRatingRepository.findAll();
        assertThat(recipeRatingList).hasSize(databaseSizeBeforeCreate + 1);
        RecipeRating testRecipeRating = recipeRatingList.get(recipeRatingList.size() - 1);
        assertThat(testRecipeRating.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRecipeRating.getRating()).isEqualTo(DEFAULT_RATING);

        // Validate the RecipeRating in Elasticsearch
        verify(mockRecipeRatingSearchRepository, times(1)).save(testRecipeRating);
    }

    @Test
    @Transactional
    public void createRecipeRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recipeRatingRepository.findAll().size();

        // Create the RecipeRating with an existing ID
        recipeRating.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeRatingMockMvc.perform(post("/api/recipe-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeRating)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeRating in the database
        List<RecipeRating> recipeRatingList = recipeRatingRepository.findAll();
        assertThat(recipeRatingList).hasSize(databaseSizeBeforeCreate);

        // Validate the RecipeRating in Elasticsearch
        verify(mockRecipeRatingSearchRepository, times(0)).save(recipeRating);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRatingRepository.findAll().size();
        // set the field null
        recipeRating.setDate(null);

        // Create the RecipeRating, which fails.

        restRecipeRatingMockMvc.perform(post("/api/recipe-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeRating)))
            .andExpect(status().isBadRequest());

        List<RecipeRating> recipeRatingList = recipeRatingRepository.findAll();
        assertThat(recipeRatingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRatingRepository.findAll().size();
        // set the field null
        recipeRating.setRating(null);

        // Create the RecipeRating, which fails.

        restRecipeRatingMockMvc.perform(post("/api/recipe-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeRating)))
            .andExpect(status().isBadRequest());

        List<RecipeRating> recipeRatingList = recipeRatingRepository.findAll();
        assertThat(recipeRatingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecipeRatings() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList
        restRecipeRatingMockMvc.perform(get("/api/recipe-ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }
    
    @Test
    @Transactional
    public void getRecipeRating() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get the recipeRating
        restRecipeRatingMockMvc.perform(get("/api/recipe-ratings/{id}", recipeRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recipeRating.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING));
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where date equals to DEFAULT_DATE
        defaultRecipeRatingShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the recipeRatingList where date equals to UPDATED_DATE
        defaultRecipeRatingShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where date in DEFAULT_DATE or UPDATED_DATE
        defaultRecipeRatingShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the recipeRatingList where date equals to UPDATED_DATE
        defaultRecipeRatingShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where date is not null
        defaultRecipeRatingShouldBeFound("date.specified=true");

        // Get all the recipeRatingList where date is null
        defaultRecipeRatingShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where date greater than or equals to DEFAULT_DATE
        defaultRecipeRatingShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the recipeRatingList where date greater than or equals to UPDATED_DATE
        defaultRecipeRatingShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where date less than or equals to DEFAULT_DATE
        defaultRecipeRatingShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the recipeRatingList where date less than or equals to UPDATED_DATE
        defaultRecipeRatingShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllRecipeRatingsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where rating equals to DEFAULT_RATING
        defaultRecipeRatingShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the recipeRatingList where rating equals to UPDATED_RATING
        defaultRecipeRatingShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultRecipeRatingShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the recipeRatingList where rating equals to UPDATED_RATING
        defaultRecipeRatingShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where rating is not null
        defaultRecipeRatingShouldBeFound("rating.specified=true");

        // Get all the recipeRatingList where rating is null
        defaultRecipeRatingShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where rating greater than or equals to DEFAULT_RATING
        defaultRecipeRatingShouldBeFound("rating.greaterOrEqualThan=" + DEFAULT_RATING);

        // Get all the recipeRatingList where rating greater than or equals to UPDATED_RATING
        defaultRecipeRatingShouldNotBeFound("rating.greaterOrEqualThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllRecipeRatingsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeRatingRepository.saveAndFlush(recipeRating);

        // Get all the recipeRatingList where rating less than or equals to DEFAULT_RATING
        defaultRecipeRatingShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the recipeRatingList where rating less than or equals to UPDATED_RATING
        defaultRecipeRatingShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }


    @Test
    @Transactional
    public void getAllRecipeRatingsByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        recipeRating.setRecipe(recipe);
        recipeRatingRepository.saveAndFlush(recipeRating);
        Long recipeId = recipe.getId();

        // Get all the recipeRatingList where recipe equals to recipeId
        defaultRecipeRatingShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the recipeRatingList where recipe equals to recipeId + 1
        defaultRecipeRatingShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipeRatingsByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo userInfo = UserInfoResourceIntTest.createEntity(em);
        em.persist(userInfo);
        em.flush();
        recipeRating.setUserInfo(userInfo);
        recipeRatingRepository.saveAndFlush(recipeRating);
        Long userInfoId = userInfo.getId();

        // Get all the recipeRatingList where userInfo equals to userInfoId
        defaultRecipeRatingShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the recipeRatingList where userInfo equals to userInfoId + 1
        defaultRecipeRatingShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecipeRatingShouldBeFound(String filter) throws Exception {
        restRecipeRatingMockMvc.perform(get("/api/recipe-ratings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));

        // Check, that the count call also returns 1
        restRecipeRatingMockMvc.perform(get("/api/recipe-ratings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecipeRatingShouldNotBeFound(String filter) throws Exception {
        restRecipeRatingMockMvc.perform(get("/api/recipe-ratings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeRatingMockMvc.perform(get("/api/recipe-ratings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRecipeRating() throws Exception {
        // Get the recipeRating
        restRecipeRatingMockMvc.perform(get("/api/recipe-ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipeRating() throws Exception {
        // Initialize the database
        recipeRatingService.save(recipeRating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecipeRatingSearchRepository);

        int databaseSizeBeforeUpdate = recipeRatingRepository.findAll().size();

        // Update the recipeRating
        RecipeRating updatedRecipeRating = recipeRatingRepository.findById(recipeRating.getId()).get();
        // Disconnect from session so that the updates on updatedRecipeRating are not directly saved in db
        em.detach(updatedRecipeRating);
        updatedRecipeRating
            .date(UPDATED_DATE)
            .rating(UPDATED_RATING);

        restRecipeRatingMockMvc.perform(put("/api/recipe-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecipeRating)))
            .andExpect(status().isOk());

        // Validate the RecipeRating in the database
        List<RecipeRating> recipeRatingList = recipeRatingRepository.findAll();
        assertThat(recipeRatingList).hasSize(databaseSizeBeforeUpdate);
        RecipeRating testRecipeRating = recipeRatingList.get(recipeRatingList.size() - 1);
        assertThat(testRecipeRating.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRecipeRating.getRating()).isEqualTo(UPDATED_RATING);

        // Validate the RecipeRating in Elasticsearch
        verify(mockRecipeRatingSearchRepository, times(1)).save(testRecipeRating);
    }

    @Test
    @Transactional
    public void updateNonExistingRecipeRating() throws Exception {
        int databaseSizeBeforeUpdate = recipeRatingRepository.findAll().size();

        // Create the RecipeRating

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeRatingMockMvc.perform(put("/api/recipe-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeRating)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeRating in the database
        List<RecipeRating> recipeRatingList = recipeRatingRepository.findAll();
        assertThat(recipeRatingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RecipeRating in Elasticsearch
        verify(mockRecipeRatingSearchRepository, times(0)).save(recipeRating);
    }

    @Test
    @Transactional
    public void deleteRecipeRating() throws Exception {
        // Initialize the database
        recipeRatingService.save(recipeRating);

        int databaseSizeBeforeDelete = recipeRatingRepository.findAll().size();

        // Delete the recipeRating
        restRecipeRatingMockMvc.perform(delete("/api/recipe-ratings/{id}", recipeRating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecipeRating> recipeRatingList = recipeRatingRepository.findAll();
        assertThat(recipeRatingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RecipeRating in Elasticsearch
        verify(mockRecipeRatingSearchRepository, times(1)).deleteById(recipeRating.getId());
    }

    @Test
    @Transactional
    public void searchRecipeRating() throws Exception {
        // Initialize the database
        recipeRatingService.save(recipeRating);
        when(mockRecipeRatingSearchRepository.search(queryStringQuery("id:" + recipeRating.getId())))
            .thenReturn(Collections.singletonList(recipeRating));
        // Search the recipeRating
        restRecipeRatingMockMvc.perform(get("/api/_search/recipe-ratings?query=id:" + recipeRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeRating.class);
        RecipeRating recipeRating1 = new RecipeRating();
        recipeRating1.setId(1L);
        RecipeRating recipeRating2 = new RecipeRating();
        recipeRating2.setId(recipeRating1.getId());
        assertThat(recipeRating1).isEqualTo(recipeRating2);
        recipeRating2.setId(2L);
        assertThat(recipeRating1).isNotEqualTo(recipeRating2);
        recipeRating1.setId(null);
        assertThat(recipeRating1).isNotEqualTo(recipeRating2);
    }
}

package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.RecipeLike;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.RecipeLikeRepository;
import com.foodiechef.api.repository.search.RecipeLikeSearchRepository;
import com.foodiechef.api.service.RecipeLikeService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RecipeLikeCriteria;
import com.foodiechef.api.service.RecipeLikeQueryService;

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
 * Test class for the RecipeLikeResource REST controller.
 *
 * @see RecipeLikeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RecipeLikeResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_LIKES = 1;
    private static final Integer UPDATED_LIKES = 2;

    @Autowired
    private RecipeLikeRepository recipeLikeRepository;

    @Autowired
    private RecipeLikeService recipeLikeService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RecipeLikeSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeLikeSearchRepository mockRecipeLikeSearchRepository;

    @Autowired
    private RecipeLikeQueryService recipeLikeQueryService;

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

    private MockMvc restRecipeLikeMockMvc;

    private RecipeLike recipeLike;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecipeLikeResource recipeLikeResource = new RecipeLikeResource(recipeLikeService, recipeLikeQueryService);
        this.restRecipeLikeMockMvc = MockMvcBuilders.standaloneSetup(recipeLikeResource)
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
    public static RecipeLike createEntity(EntityManager em) {
        RecipeLike recipeLike = new RecipeLike()
            .date(DEFAULT_DATE)
            .likes(DEFAULT_LIKES);
        return recipeLike;
    }

    @Before
    public void initTest() {
        recipeLike = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecipeLike() throws Exception {
        int databaseSizeBeforeCreate = recipeLikeRepository.findAll().size();

        // Create the RecipeLike
        restRecipeLikeMockMvc.perform(post("/api/recipe-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeLike)))
            .andExpect(status().isCreated());

        // Validate the RecipeLike in the database
        List<RecipeLike> recipeLikeList = recipeLikeRepository.findAll();
        assertThat(recipeLikeList).hasSize(databaseSizeBeforeCreate + 1);
        RecipeLike testRecipeLike = recipeLikeList.get(recipeLikeList.size() - 1);
        assertThat(testRecipeLike.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRecipeLike.getLikes()).isEqualTo(DEFAULT_LIKES);

        // Validate the RecipeLike in Elasticsearch
        verify(mockRecipeLikeSearchRepository, times(1)).save(testRecipeLike);
    }

    @Test
    @Transactional
    public void createRecipeLikeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recipeLikeRepository.findAll().size();

        // Create the RecipeLike with an existing ID
        recipeLike.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeLikeMockMvc.perform(post("/api/recipe-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeLike)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeLike in the database
        List<RecipeLike> recipeLikeList = recipeLikeRepository.findAll();
        assertThat(recipeLikeList).hasSize(databaseSizeBeforeCreate);

        // Validate the RecipeLike in Elasticsearch
        verify(mockRecipeLikeSearchRepository, times(0)).save(recipeLike);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeLikeRepository.findAll().size();
        // set the field null
        recipeLike.setDate(null);

        // Create the RecipeLike, which fails.

        restRecipeLikeMockMvc.perform(post("/api/recipe-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeLike)))
            .andExpect(status().isBadRequest());

        List<RecipeLike> recipeLikeList = recipeLikeRepository.findAll();
        assertThat(recipeLikeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeLikeRepository.findAll().size();
        // set the field null
        recipeLike.setLikes(null);

        // Create the RecipeLike, which fails.

        restRecipeLikeMockMvc.perform(post("/api/recipe-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeLike)))
            .andExpect(status().isBadRequest());

        List<RecipeLike> recipeLikeList = recipeLikeRepository.findAll();
        assertThat(recipeLikeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecipeLikes() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList
        restRecipeLikeMockMvc.perform(get("/api/recipe-likes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeLike.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)));
    }
    
    @Test
    @Transactional
    public void getRecipeLike() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get the recipeLike
        restRecipeLikeMockMvc.perform(get("/api/recipe-likes/{id}", recipeLike.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recipeLike.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES));
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where date equals to DEFAULT_DATE
        defaultRecipeLikeShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the recipeLikeList where date equals to UPDATED_DATE
        defaultRecipeLikeShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where date in DEFAULT_DATE or UPDATED_DATE
        defaultRecipeLikeShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the recipeLikeList where date equals to UPDATED_DATE
        defaultRecipeLikeShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where date is not null
        defaultRecipeLikeShouldBeFound("date.specified=true");

        // Get all the recipeLikeList where date is null
        defaultRecipeLikeShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where date greater than or equals to DEFAULT_DATE
        defaultRecipeLikeShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the recipeLikeList where date greater than or equals to UPDATED_DATE
        defaultRecipeLikeShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where date less than or equals to DEFAULT_DATE
        defaultRecipeLikeShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the recipeLikeList where date less than or equals to UPDATED_DATE
        defaultRecipeLikeShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllRecipeLikesByLikesIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where likes equals to DEFAULT_LIKES
        defaultRecipeLikeShouldBeFound("likes.equals=" + DEFAULT_LIKES);

        // Get all the recipeLikeList where likes equals to UPDATED_LIKES
        defaultRecipeLikeShouldNotBeFound("likes.equals=" + UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByLikesIsInShouldWork() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where likes in DEFAULT_LIKES or UPDATED_LIKES
        defaultRecipeLikeShouldBeFound("likes.in=" + DEFAULT_LIKES + "," + UPDATED_LIKES);

        // Get all the recipeLikeList where likes equals to UPDATED_LIKES
        defaultRecipeLikeShouldNotBeFound("likes.in=" + UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByLikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where likes is not null
        defaultRecipeLikeShouldBeFound("likes.specified=true");

        // Get all the recipeLikeList where likes is null
        defaultRecipeLikeShouldNotBeFound("likes.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByLikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where likes greater than or equals to DEFAULT_LIKES
        defaultRecipeLikeShouldBeFound("likes.greaterOrEqualThan=" + DEFAULT_LIKES);

        // Get all the recipeLikeList where likes greater than or equals to UPDATED_LIKES
        defaultRecipeLikeShouldNotBeFound("likes.greaterOrEqualThan=" + UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void getAllRecipeLikesByLikesIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeLikeRepository.saveAndFlush(recipeLike);

        // Get all the recipeLikeList where likes less than or equals to DEFAULT_LIKES
        defaultRecipeLikeShouldNotBeFound("likes.lessThan=" + DEFAULT_LIKES);

        // Get all the recipeLikeList where likes less than or equals to UPDATED_LIKES
        defaultRecipeLikeShouldBeFound("likes.lessThan=" + UPDATED_LIKES);
    }


    @Test
    @Transactional
    public void getAllRecipeLikesByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        recipeLike.setRecipe(recipe);
        recipeLikeRepository.saveAndFlush(recipeLike);
        Long recipeId = recipe.getId();

        // Get all the recipeLikeList where recipe equals to recipeId
        defaultRecipeLikeShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the recipeLikeList where recipe equals to recipeId + 1
        defaultRecipeLikeShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipeLikesByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo userInfo = UserInfoResourceIntTest.createEntity(em);
        em.persist(userInfo);
        em.flush();
        recipeLike.setUserInfo(userInfo);
        recipeLikeRepository.saveAndFlush(recipeLike);
        Long userInfoId = userInfo.getId();

        // Get all the recipeLikeList where userInfo equals to userInfoId
        defaultRecipeLikeShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the recipeLikeList where userInfo equals to userInfoId + 1
        defaultRecipeLikeShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecipeLikeShouldBeFound(String filter) throws Exception {
        restRecipeLikeMockMvc.perform(get("/api/recipe-likes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeLike.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)));

        // Check, that the count call also returns 1
        restRecipeLikeMockMvc.perform(get("/api/recipe-likes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecipeLikeShouldNotBeFound(String filter) throws Exception {
        restRecipeLikeMockMvc.perform(get("/api/recipe-likes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeLikeMockMvc.perform(get("/api/recipe-likes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRecipeLike() throws Exception {
        // Get the recipeLike
        restRecipeLikeMockMvc.perform(get("/api/recipe-likes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipeLike() throws Exception {
        // Initialize the database
        recipeLikeService.save(recipeLike);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecipeLikeSearchRepository);

        int databaseSizeBeforeUpdate = recipeLikeRepository.findAll().size();

        // Update the recipeLike
        RecipeLike updatedRecipeLike = recipeLikeRepository.findById(recipeLike.getId()).get();
        // Disconnect from session so that the updates on updatedRecipeLike are not directly saved in db
        em.detach(updatedRecipeLike);
        updatedRecipeLike
            .date(UPDATED_DATE)
            .likes(UPDATED_LIKES);

        restRecipeLikeMockMvc.perform(put("/api/recipe-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecipeLike)))
            .andExpect(status().isOk());

        // Validate the RecipeLike in the database
        List<RecipeLike> recipeLikeList = recipeLikeRepository.findAll();
        assertThat(recipeLikeList).hasSize(databaseSizeBeforeUpdate);
        RecipeLike testRecipeLike = recipeLikeList.get(recipeLikeList.size() - 1);
        assertThat(testRecipeLike.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRecipeLike.getLikes()).isEqualTo(UPDATED_LIKES);

        // Validate the RecipeLike in Elasticsearch
        verify(mockRecipeLikeSearchRepository, times(1)).save(testRecipeLike);
    }

    @Test
    @Transactional
    public void updateNonExistingRecipeLike() throws Exception {
        int databaseSizeBeforeUpdate = recipeLikeRepository.findAll().size();

        // Create the RecipeLike

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeLikeMockMvc.perform(put("/api/recipe-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeLike)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeLike in the database
        List<RecipeLike> recipeLikeList = recipeLikeRepository.findAll();
        assertThat(recipeLikeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RecipeLike in Elasticsearch
        verify(mockRecipeLikeSearchRepository, times(0)).save(recipeLike);
    }

    @Test
    @Transactional
    public void deleteRecipeLike() throws Exception {
        // Initialize the database
        recipeLikeService.save(recipeLike);

        int databaseSizeBeforeDelete = recipeLikeRepository.findAll().size();

        // Delete the recipeLike
        restRecipeLikeMockMvc.perform(delete("/api/recipe-likes/{id}", recipeLike.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecipeLike> recipeLikeList = recipeLikeRepository.findAll();
        assertThat(recipeLikeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RecipeLike in Elasticsearch
        verify(mockRecipeLikeSearchRepository, times(1)).deleteById(recipeLike.getId());
    }

    @Test
    @Transactional
    public void searchRecipeLike() throws Exception {
        // Initialize the database
        recipeLikeService.save(recipeLike);
        when(mockRecipeLikeSearchRepository.search(queryStringQuery("id:" + recipeLike.getId())))
            .thenReturn(Collections.singletonList(recipeLike));
        // Search the recipeLike
        restRecipeLikeMockMvc.perform(get("/api/_search/recipe-likes?query=id:" + recipeLike.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeLike.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeLike.class);
        RecipeLike recipeLike1 = new RecipeLike();
        recipeLike1.setId(1L);
        RecipeLike recipeLike2 = new RecipeLike();
        recipeLike2.setId(recipeLike1.getId());
        assertThat(recipeLike1).isEqualTo(recipeLike2);
        recipeLike2.setId(2L);
        assertThat(recipeLike1).isNotEqualTo(recipeLike2);
        recipeLike1.setId(null);
        assertThat(recipeLike1).isNotEqualTo(recipeLike2);
    }
}

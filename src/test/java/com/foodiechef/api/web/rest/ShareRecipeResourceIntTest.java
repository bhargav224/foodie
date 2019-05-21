package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.ShareRecipe;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.ShareRecipeRepository;
import com.foodiechef.api.repository.search.ShareRecipeSearchRepository;
import com.foodiechef.api.service.ShareRecipeService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.ShareRecipeCriteria;
import com.foodiechef.api.service.ShareRecipeQueryService;

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
 * Test class for the ShareRecipeResource REST controller.
 *
 * @see ShareRecipeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class ShareRecipeResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ShareRecipeRepository shareRecipeRepository;

    @Autowired
    private ShareRecipeService shareRecipeService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.ShareRecipeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShareRecipeSearchRepository mockShareRecipeSearchRepository;

    @Autowired
    private ShareRecipeQueryService shareRecipeQueryService;

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

    private MockMvc restShareRecipeMockMvc;

    private ShareRecipe shareRecipe;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShareRecipeResource shareRecipeResource = new ShareRecipeResource(shareRecipeService, shareRecipeQueryService);
        this.restShareRecipeMockMvc = MockMvcBuilders.standaloneSetup(shareRecipeResource)
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
    public static ShareRecipe createEntity(EntityManager em) {
        ShareRecipe shareRecipe = new ShareRecipe()
            .date(DEFAULT_DATE);
        return shareRecipe;
    }

    @Before
    public void initTest() {
        shareRecipe = createEntity(em);
    }

    @Test
    @Transactional
    public void createShareRecipe() throws Exception {
        int databaseSizeBeforeCreate = shareRecipeRepository.findAll().size();

        // Create the ShareRecipe
        restShareRecipeMockMvc.perform(post("/api/share-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shareRecipe)))
            .andExpect(status().isCreated());

        // Validate the ShareRecipe in the database
        List<ShareRecipe> shareRecipeList = shareRecipeRepository.findAll();
        assertThat(shareRecipeList).hasSize(databaseSizeBeforeCreate + 1);
        ShareRecipe testShareRecipe = shareRecipeList.get(shareRecipeList.size() - 1);
        assertThat(testShareRecipe.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the ShareRecipe in Elasticsearch
        verify(mockShareRecipeSearchRepository, times(1)).save(testShareRecipe);
    }

    @Test
    @Transactional
    public void createShareRecipeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shareRecipeRepository.findAll().size();

        // Create the ShareRecipe with an existing ID
        shareRecipe.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareRecipeMockMvc.perform(post("/api/share-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shareRecipe)))
            .andExpect(status().isBadRequest());

        // Validate the ShareRecipe in the database
        List<ShareRecipe> shareRecipeList = shareRecipeRepository.findAll();
        assertThat(shareRecipeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShareRecipe in Elasticsearch
        verify(mockShareRecipeSearchRepository, times(0)).save(shareRecipe);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = shareRecipeRepository.findAll().size();
        // set the field null
        shareRecipe.setDate(null);

        // Create the ShareRecipe, which fails.

        restShareRecipeMockMvc.perform(post("/api/share-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shareRecipe)))
            .andExpect(status().isBadRequest());

        List<ShareRecipe> shareRecipeList = shareRecipeRepository.findAll();
        assertThat(shareRecipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShareRecipes() throws Exception {
        // Initialize the database
        shareRecipeRepository.saveAndFlush(shareRecipe);

        // Get all the shareRecipeList
        restShareRecipeMockMvc.perform(get("/api/share-recipes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareRecipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getShareRecipe() throws Exception {
        // Initialize the database
        shareRecipeRepository.saveAndFlush(shareRecipe);

        // Get the shareRecipe
        restShareRecipeMockMvc.perform(get("/api/share-recipes/{id}", shareRecipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shareRecipe.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllShareRecipesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        shareRecipeRepository.saveAndFlush(shareRecipe);

        // Get all the shareRecipeList where date equals to DEFAULT_DATE
        defaultShareRecipeShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the shareRecipeList where date equals to UPDATED_DATE
        defaultShareRecipeShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllShareRecipesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        shareRecipeRepository.saveAndFlush(shareRecipe);

        // Get all the shareRecipeList where date in DEFAULT_DATE or UPDATED_DATE
        defaultShareRecipeShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the shareRecipeList where date equals to UPDATED_DATE
        defaultShareRecipeShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllShareRecipesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareRecipeRepository.saveAndFlush(shareRecipe);

        // Get all the shareRecipeList where date is not null
        defaultShareRecipeShouldBeFound("date.specified=true");

        // Get all the shareRecipeList where date is null
        defaultShareRecipeShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllShareRecipesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shareRecipeRepository.saveAndFlush(shareRecipe);

        // Get all the shareRecipeList where date greater than or equals to DEFAULT_DATE
        defaultShareRecipeShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the shareRecipeList where date greater than or equals to UPDATED_DATE
        defaultShareRecipeShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllShareRecipesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        shareRecipeRepository.saveAndFlush(shareRecipe);

        // Get all the shareRecipeList where date less than or equals to DEFAULT_DATE
        defaultShareRecipeShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the shareRecipeList where date less than or equals to UPDATED_DATE
        defaultShareRecipeShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllShareRecipesByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        shareRecipe.setRecipe(recipe);
        shareRecipeRepository.saveAndFlush(shareRecipe);
        Long recipeId = recipe.getId();

        // Get all the shareRecipeList where recipe equals to recipeId
        defaultShareRecipeShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the shareRecipeList where recipe equals to recipeId + 1
        defaultShareRecipeShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllShareRecipesBySharedByIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo sharedBy = UserInfoResourceIntTest.createEntity(em);
        em.persist(sharedBy);
        em.flush();
        shareRecipe.setSharedBy(sharedBy);
        shareRecipeRepository.saveAndFlush(shareRecipe);
        Long sharedById = sharedBy.getId();

        // Get all the shareRecipeList where sharedBy equals to sharedById
        defaultShareRecipeShouldBeFound("sharedById.equals=" + sharedById);

        // Get all the shareRecipeList where sharedBy equals to sharedById + 1
        defaultShareRecipeShouldNotBeFound("sharedById.equals=" + (sharedById + 1));
    }


    @Test
    @Transactional
    public void getAllShareRecipesBySharedToIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo sharedTo = UserInfoResourceIntTest.createEntity(em);
        em.persist(sharedTo);
        em.flush();
        shareRecipe.setSharedTo(sharedTo);
        shareRecipeRepository.saveAndFlush(shareRecipe);
        Long sharedToId = sharedTo.getId();

        // Get all the shareRecipeList where sharedTo equals to sharedToId
        defaultShareRecipeShouldBeFound("sharedToId.equals=" + sharedToId);

        // Get all the shareRecipeList where sharedTo equals to sharedToId + 1
        defaultShareRecipeShouldNotBeFound("sharedToId.equals=" + (sharedToId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultShareRecipeShouldBeFound(String filter) throws Exception {
        restShareRecipeMockMvc.perform(get("/api/share-recipes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareRecipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restShareRecipeMockMvc.perform(get("/api/share-recipes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultShareRecipeShouldNotBeFound(String filter) throws Exception {
        restShareRecipeMockMvc.perform(get("/api/share-recipes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShareRecipeMockMvc.perform(get("/api/share-recipes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShareRecipe() throws Exception {
        // Get the shareRecipe
        restShareRecipeMockMvc.perform(get("/api/share-recipes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShareRecipe() throws Exception {
        // Initialize the database
        shareRecipeService.save(shareRecipe);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockShareRecipeSearchRepository);

        int databaseSizeBeforeUpdate = shareRecipeRepository.findAll().size();

        // Update the shareRecipe
        ShareRecipe updatedShareRecipe = shareRecipeRepository.findById(shareRecipe.getId()).get();
        // Disconnect from session so that the updates on updatedShareRecipe are not directly saved in db
        em.detach(updatedShareRecipe);
        updatedShareRecipe
            .date(UPDATED_DATE);

        restShareRecipeMockMvc.perform(put("/api/share-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShareRecipe)))
            .andExpect(status().isOk());

        // Validate the ShareRecipe in the database
        List<ShareRecipe> shareRecipeList = shareRecipeRepository.findAll();
        assertThat(shareRecipeList).hasSize(databaseSizeBeforeUpdate);
        ShareRecipe testShareRecipe = shareRecipeList.get(shareRecipeList.size() - 1);
        assertThat(testShareRecipe.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the ShareRecipe in Elasticsearch
        verify(mockShareRecipeSearchRepository, times(1)).save(testShareRecipe);
    }

    @Test
    @Transactional
    public void updateNonExistingShareRecipe() throws Exception {
        int databaseSizeBeforeUpdate = shareRecipeRepository.findAll().size();

        // Create the ShareRecipe

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareRecipeMockMvc.perform(put("/api/share-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shareRecipe)))
            .andExpect(status().isBadRequest());

        // Validate the ShareRecipe in the database
        List<ShareRecipe> shareRecipeList = shareRecipeRepository.findAll();
        assertThat(shareRecipeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareRecipe in Elasticsearch
        verify(mockShareRecipeSearchRepository, times(0)).save(shareRecipe);
    }

    @Test
    @Transactional
    public void deleteShareRecipe() throws Exception {
        // Initialize the database
        shareRecipeService.save(shareRecipe);

        int databaseSizeBeforeDelete = shareRecipeRepository.findAll().size();

        // Delete the shareRecipe
        restShareRecipeMockMvc.perform(delete("/api/share-recipes/{id}", shareRecipe.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShareRecipe> shareRecipeList = shareRecipeRepository.findAll();
        assertThat(shareRecipeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShareRecipe in Elasticsearch
        verify(mockShareRecipeSearchRepository, times(1)).deleteById(shareRecipe.getId());
    }

    @Test
    @Transactional
    public void searchShareRecipe() throws Exception {
        // Initialize the database
        shareRecipeService.save(shareRecipe);
        when(mockShareRecipeSearchRepository.search(queryStringQuery("id:" + shareRecipe.getId())))
            .thenReturn(Collections.singletonList(shareRecipe));
        // Search the shareRecipe
        restShareRecipeMockMvc.perform(get("/api/_search/share-recipes?query=id:" + shareRecipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareRecipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShareRecipe.class);
        ShareRecipe shareRecipe1 = new ShareRecipe();
        shareRecipe1.setId(1L);
        ShareRecipe shareRecipe2 = new ShareRecipe();
        shareRecipe2.setId(shareRecipe1.getId());
        assertThat(shareRecipe1).isEqualTo(shareRecipe2);
        shareRecipe2.setId(2L);
        assertThat(shareRecipe1).isNotEqualTo(shareRecipe2);
        shareRecipe1.setId(null);
        assertThat(shareRecipe1).isNotEqualTo(shareRecipe2);
    }
}

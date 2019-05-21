package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.RecipeHasStep;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.RecipeStep;
import com.foodiechef.api.repository.RecipeHasStepRepository;
import com.foodiechef.api.repository.search.RecipeHasStepSearchRepository;
import com.foodiechef.api.service.RecipeHasStepService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RecipeHasStepCriteria;
import com.foodiechef.api.service.RecipeHasStepQueryService;

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
 * Test class for the RecipeHasStepResource REST controller.
 *
 * @see RecipeHasStepResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RecipeHasStepResourceIntTest {

    @Autowired
    private RecipeHasStepRepository recipeHasStepRepository;

    @Autowired
    private RecipeHasStepService recipeHasStepService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RecipeHasStepSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeHasStepSearchRepository mockRecipeHasStepSearchRepository;

    @Autowired
    private RecipeHasStepQueryService recipeHasStepQueryService;

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

    private MockMvc restRecipeHasStepMockMvc;

    private RecipeHasStep recipeHasStep;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecipeHasStepResource recipeHasStepResource = new RecipeHasStepResource(recipeHasStepService, recipeHasStepQueryService);
        this.restRecipeHasStepMockMvc = MockMvcBuilders.standaloneSetup(recipeHasStepResource)
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
    public static RecipeHasStep createEntity(EntityManager em) {
        RecipeHasStep recipeHasStep = new RecipeHasStep();
        return recipeHasStep;
    }

    @Before
    public void initTest() {
        recipeHasStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecipeHasStep() throws Exception {
        int databaseSizeBeforeCreate = recipeHasStepRepository.findAll().size();

        // Create the RecipeHasStep
        restRecipeHasStepMockMvc.perform(post("/api/recipe-has-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeHasStep)))
            .andExpect(status().isCreated());

        // Validate the RecipeHasStep in the database
        List<RecipeHasStep> recipeHasStepList = recipeHasStepRepository.findAll();
        assertThat(recipeHasStepList).hasSize(databaseSizeBeforeCreate + 1);
        RecipeHasStep testRecipeHasStep = recipeHasStepList.get(recipeHasStepList.size() - 1);

        // Validate the RecipeHasStep in Elasticsearch
        verify(mockRecipeHasStepSearchRepository, times(1)).save(testRecipeHasStep);
    }

    @Test
    @Transactional
    public void createRecipeHasStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recipeHasStepRepository.findAll().size();

        // Create the RecipeHasStep with an existing ID
        recipeHasStep.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeHasStepMockMvc.perform(post("/api/recipe-has-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeHasStep)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeHasStep in the database
        List<RecipeHasStep> recipeHasStepList = recipeHasStepRepository.findAll();
        assertThat(recipeHasStepList).hasSize(databaseSizeBeforeCreate);

        // Validate the RecipeHasStep in Elasticsearch
        verify(mockRecipeHasStepSearchRepository, times(0)).save(recipeHasStep);
    }

    @Test
    @Transactional
    public void getAllRecipeHasSteps() throws Exception {
        // Initialize the database
        recipeHasStepRepository.saveAndFlush(recipeHasStep);

        // Get all the recipeHasStepList
        restRecipeHasStepMockMvc.perform(get("/api/recipe-has-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeHasStep.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getRecipeHasStep() throws Exception {
        // Initialize the database
        recipeHasStepRepository.saveAndFlush(recipeHasStep);

        // Get the recipeHasStep
        restRecipeHasStepMockMvc.perform(get("/api/recipe-has-steps/{id}", recipeHasStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recipeHasStep.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllRecipeHasStepsByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        recipeHasStep.setRecipe(recipe);
        recipeHasStepRepository.saveAndFlush(recipeHasStep);
        Long recipeId = recipe.getId();

        // Get all the recipeHasStepList where recipe equals to recipeId
        defaultRecipeHasStepShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the recipeHasStepList where recipe equals to recipeId + 1
        defaultRecipeHasStepShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipeHasStepsByRecipeStepIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeStep recipeStep = RecipeStepResourceIntTest.createEntity(em);
        em.persist(recipeStep);
        em.flush();
        recipeHasStep.setRecipeStep(recipeStep);
        recipeHasStepRepository.saveAndFlush(recipeHasStep);
        Long recipeStepId = recipeStep.getId();

        // Get all the recipeHasStepList where recipeStep equals to recipeStepId
        defaultRecipeHasStepShouldBeFound("recipeStepId.equals=" + recipeStepId);

        // Get all the recipeHasStepList where recipeStep equals to recipeStepId + 1
        defaultRecipeHasStepShouldNotBeFound("recipeStepId.equals=" + (recipeStepId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecipeHasStepShouldBeFound(String filter) throws Exception {
        restRecipeHasStepMockMvc.perform(get("/api/recipe-has-steps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeHasStep.getId().intValue())));

        // Check, that the count call also returns 1
        restRecipeHasStepMockMvc.perform(get("/api/recipe-has-steps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecipeHasStepShouldNotBeFound(String filter) throws Exception {
        restRecipeHasStepMockMvc.perform(get("/api/recipe-has-steps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeHasStepMockMvc.perform(get("/api/recipe-has-steps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRecipeHasStep() throws Exception {
        // Get the recipeHasStep
        restRecipeHasStepMockMvc.perform(get("/api/recipe-has-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipeHasStep() throws Exception {
        // Initialize the database
        recipeHasStepService.save(recipeHasStep);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecipeHasStepSearchRepository);

        int databaseSizeBeforeUpdate = recipeHasStepRepository.findAll().size();

        // Update the recipeHasStep
        RecipeHasStep updatedRecipeHasStep = recipeHasStepRepository.findById(recipeHasStep.getId()).get();
        // Disconnect from session so that the updates on updatedRecipeHasStep are not directly saved in db
        em.detach(updatedRecipeHasStep);

        restRecipeHasStepMockMvc.perform(put("/api/recipe-has-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecipeHasStep)))
            .andExpect(status().isOk());

        // Validate the RecipeHasStep in the database
        List<RecipeHasStep> recipeHasStepList = recipeHasStepRepository.findAll();
        assertThat(recipeHasStepList).hasSize(databaseSizeBeforeUpdate);
        RecipeHasStep testRecipeHasStep = recipeHasStepList.get(recipeHasStepList.size() - 1);

        // Validate the RecipeHasStep in Elasticsearch
        verify(mockRecipeHasStepSearchRepository, times(1)).save(testRecipeHasStep);
    }

    @Test
    @Transactional
    public void updateNonExistingRecipeHasStep() throws Exception {
        int databaseSizeBeforeUpdate = recipeHasStepRepository.findAll().size();

        // Create the RecipeHasStep

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeHasStepMockMvc.perform(put("/api/recipe-has-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeHasStep)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeHasStep in the database
        List<RecipeHasStep> recipeHasStepList = recipeHasStepRepository.findAll();
        assertThat(recipeHasStepList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RecipeHasStep in Elasticsearch
        verify(mockRecipeHasStepSearchRepository, times(0)).save(recipeHasStep);
    }

    @Test
    @Transactional
    public void deleteRecipeHasStep() throws Exception {
        // Initialize the database
        recipeHasStepService.save(recipeHasStep);

        int databaseSizeBeforeDelete = recipeHasStepRepository.findAll().size();

        // Delete the recipeHasStep
        restRecipeHasStepMockMvc.perform(delete("/api/recipe-has-steps/{id}", recipeHasStep.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecipeHasStep> recipeHasStepList = recipeHasStepRepository.findAll();
        assertThat(recipeHasStepList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RecipeHasStep in Elasticsearch
        verify(mockRecipeHasStepSearchRepository, times(1)).deleteById(recipeHasStep.getId());
    }

    @Test
    @Transactional
    public void searchRecipeHasStep() throws Exception {
        // Initialize the database
        recipeHasStepService.save(recipeHasStep);
        when(mockRecipeHasStepSearchRepository.search(queryStringQuery("id:" + recipeHasStep.getId())))
            .thenReturn(Collections.singletonList(recipeHasStep));
        // Search the recipeHasStep
        restRecipeHasStepMockMvc.perform(get("/api/_search/recipe-has-steps?query=id:" + recipeHasStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeHasStep.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeHasStep.class);
        RecipeHasStep recipeHasStep1 = new RecipeHasStep();
        recipeHasStep1.setId(1L);
        RecipeHasStep recipeHasStep2 = new RecipeHasStep();
        recipeHasStep2.setId(recipeHasStep1.getId());
        assertThat(recipeHasStep1).isEqualTo(recipeHasStep2);
        recipeHasStep2.setId(2L);
        assertThat(recipeHasStep1).isNotEqualTo(recipeHasStep2);
        recipeHasStep1.setId(null);
        assertThat(recipeHasStep1).isNotEqualTo(recipeHasStep2);
    }
}

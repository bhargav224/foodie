package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.RecipeStep;
import com.foodiechef.api.domain.RecipeHasStep;
import com.foodiechef.api.repository.RecipeStepRepository;
import com.foodiechef.api.repository.search.RecipeStepSearchRepository;
import com.foodiechef.api.service.RecipeStepService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RecipeStepCriteria;
import com.foodiechef.api.service.RecipeStepQueryService;

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
 * Test class for the RecipeStepResource REST controller.
 *
 * @see RecipeStepResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RecipeStepResourceIntTest {

    private static final String DEFAULT_INSTRUCTION = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Autowired
    private RecipeStepService recipeStepService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RecipeStepSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeStepSearchRepository mockRecipeStepSearchRepository;

    @Autowired
    private RecipeStepQueryService recipeStepQueryService;

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

    private MockMvc restRecipeStepMockMvc;

    private RecipeStep recipeStep;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecipeStepResource recipeStepResource = new RecipeStepResource(recipeStepService, recipeStepQueryService);
        this.restRecipeStepMockMvc = MockMvcBuilders.standaloneSetup(recipeStepResource)
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
    public static RecipeStep createEntity(EntityManager em) {
        RecipeStep recipeStep = new RecipeStep()
            .instruction(DEFAULT_INSTRUCTION)
            .name(DEFAULT_NAME)
            .path(DEFAULT_PATH);
        return recipeStep;
    }

    @Before
    public void initTest() {
        recipeStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecipeStep() throws Exception {
        int databaseSizeBeforeCreate = recipeStepRepository.findAll().size();

        // Create the RecipeStep
        restRecipeStepMockMvc.perform(post("/api/recipe-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeStep)))
            .andExpect(status().isCreated());

        // Validate the RecipeStep in the database
        List<RecipeStep> recipeStepList = recipeStepRepository.findAll();
        assertThat(recipeStepList).hasSize(databaseSizeBeforeCreate + 1);
        RecipeStep testRecipeStep = recipeStepList.get(recipeStepList.size() - 1);
        assertThat(testRecipeStep.getInstruction()).isEqualTo(DEFAULT_INSTRUCTION);
        assertThat(testRecipeStep.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecipeStep.getPath()).isEqualTo(DEFAULT_PATH);

        // Validate the RecipeStep in Elasticsearch
        verify(mockRecipeStepSearchRepository, times(1)).save(testRecipeStep);
    }

    @Test
    @Transactional
    public void createRecipeStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recipeStepRepository.findAll().size();

        // Create the RecipeStep with an existing ID
        recipeStep.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeStepMockMvc.perform(post("/api/recipe-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeStep)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeStep in the database
        List<RecipeStep> recipeStepList = recipeStepRepository.findAll();
        assertThat(recipeStepList).hasSize(databaseSizeBeforeCreate);

        // Validate the RecipeStep in Elasticsearch
        verify(mockRecipeStepSearchRepository, times(0)).save(recipeStep);
    }

    @Test
    @Transactional
    public void checkInstructionIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeStepRepository.findAll().size();
        // set the field null
        recipeStep.setInstruction(null);

        // Create the RecipeStep, which fails.

        restRecipeStepMockMvc.perform(post("/api/recipe-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeStep)))
            .andExpect(status().isBadRequest());

        List<RecipeStep> recipeStepList = recipeStepRepository.findAll();
        assertThat(recipeStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeStepRepository.findAll().size();
        // set the field null
        recipeStep.setName(null);

        // Create the RecipeStep, which fails.

        restRecipeStepMockMvc.perform(post("/api/recipe-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeStep)))
            .andExpect(status().isBadRequest());

        List<RecipeStep> recipeStepList = recipeStepRepository.findAll();
        assertThat(recipeStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeStepRepository.findAll().size();
        // set the field null
        recipeStep.setPath(null);

        // Create the RecipeStep, which fails.

        restRecipeStepMockMvc.perform(post("/api/recipe-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeStep)))
            .andExpect(status().isBadRequest());

        List<RecipeStep> recipeStepList = recipeStepRepository.findAll();
        assertThat(recipeStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecipeSteps() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList
        restRecipeStepMockMvc.perform(get("/api/recipe-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].instruction").value(hasItem(DEFAULT_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }
    
    @Test
    @Transactional
    public void getRecipeStep() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get the recipeStep
        restRecipeStepMockMvc.perform(get("/api/recipe-steps/{id}", recipeStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recipeStep.getId().intValue()))
            .andExpect(jsonPath("$.instruction").value(DEFAULT_INSTRUCTION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()));
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByInstructionIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where instruction equals to DEFAULT_INSTRUCTION
        defaultRecipeStepShouldBeFound("instruction.equals=" + DEFAULT_INSTRUCTION);

        // Get all the recipeStepList where instruction equals to UPDATED_INSTRUCTION
        defaultRecipeStepShouldNotBeFound("instruction.equals=" + UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByInstructionIsInShouldWork() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where instruction in DEFAULT_INSTRUCTION or UPDATED_INSTRUCTION
        defaultRecipeStepShouldBeFound("instruction.in=" + DEFAULT_INSTRUCTION + "," + UPDATED_INSTRUCTION);

        // Get all the recipeStepList where instruction equals to UPDATED_INSTRUCTION
        defaultRecipeStepShouldNotBeFound("instruction.in=" + UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByInstructionIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where instruction is not null
        defaultRecipeStepShouldBeFound("instruction.specified=true");

        // Get all the recipeStepList where instruction is null
        defaultRecipeStepShouldNotBeFound("instruction.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where name equals to DEFAULT_NAME
        defaultRecipeStepShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the recipeStepList where name equals to UPDATED_NAME
        defaultRecipeStepShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRecipeStepShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the recipeStepList where name equals to UPDATED_NAME
        defaultRecipeStepShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where name is not null
        defaultRecipeStepShouldBeFound("name.specified=true");

        // Get all the recipeStepList where name is null
        defaultRecipeStepShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where path equals to DEFAULT_PATH
        defaultRecipeStepShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the recipeStepList where path equals to UPDATED_PATH
        defaultRecipeStepShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByPathIsInShouldWork() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where path in DEFAULT_PATH or UPDATED_PATH
        defaultRecipeStepShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the recipeStepList where path equals to UPDATED_PATH
        defaultRecipeStepShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeStepRepository.saveAndFlush(recipeStep);

        // Get all the recipeStepList where path is not null
        defaultRecipeStepShouldBeFound("path.specified=true");

        // Get all the recipeStepList where path is null
        defaultRecipeStepShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeStepsByRecipeHasStepIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeHasStep recipeHasStep = RecipeHasStepResourceIntTest.createEntity(em);
        em.persist(recipeHasStep);
        em.flush();
        recipeStep.addRecipeHasStep(recipeHasStep);
        recipeStepRepository.saveAndFlush(recipeStep);
        Long recipeHasStepId = recipeHasStep.getId();

        // Get all the recipeStepList where recipeHasStep equals to recipeHasStepId
        defaultRecipeStepShouldBeFound("recipeHasStepId.equals=" + recipeHasStepId);

        // Get all the recipeStepList where recipeHasStep equals to recipeHasStepId + 1
        defaultRecipeStepShouldNotBeFound("recipeHasStepId.equals=" + (recipeHasStepId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecipeStepShouldBeFound(String filter) throws Exception {
        restRecipeStepMockMvc.perform(get("/api/recipe-steps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].instruction").value(hasItem(DEFAULT_INSTRUCTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));

        // Check, that the count call also returns 1
        restRecipeStepMockMvc.perform(get("/api/recipe-steps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecipeStepShouldNotBeFound(String filter) throws Exception {
        restRecipeStepMockMvc.perform(get("/api/recipe-steps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeStepMockMvc.perform(get("/api/recipe-steps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRecipeStep() throws Exception {
        // Get the recipeStep
        restRecipeStepMockMvc.perform(get("/api/recipe-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipeStep() throws Exception {
        // Initialize the database
        recipeStepService.save(recipeStep);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecipeStepSearchRepository);

        int databaseSizeBeforeUpdate = recipeStepRepository.findAll().size();

        // Update the recipeStep
        RecipeStep updatedRecipeStep = recipeStepRepository.findById(recipeStep.getId()).get();
        // Disconnect from session so that the updates on updatedRecipeStep are not directly saved in db
        em.detach(updatedRecipeStep);
        updatedRecipeStep
            .instruction(UPDATED_INSTRUCTION)
            .name(UPDATED_NAME)
            .path(UPDATED_PATH);

        restRecipeStepMockMvc.perform(put("/api/recipe-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecipeStep)))
            .andExpect(status().isOk());

        // Validate the RecipeStep in the database
        List<RecipeStep> recipeStepList = recipeStepRepository.findAll();
        assertThat(recipeStepList).hasSize(databaseSizeBeforeUpdate);
        RecipeStep testRecipeStep = recipeStepList.get(recipeStepList.size() - 1);
        assertThat(testRecipeStep.getInstruction()).isEqualTo(UPDATED_INSTRUCTION);
        assertThat(testRecipeStep.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecipeStep.getPath()).isEqualTo(UPDATED_PATH);

        // Validate the RecipeStep in Elasticsearch
        verify(mockRecipeStepSearchRepository, times(1)).save(testRecipeStep);
    }

    @Test
    @Transactional
    public void updateNonExistingRecipeStep() throws Exception {
        int databaseSizeBeforeUpdate = recipeStepRepository.findAll().size();

        // Create the RecipeStep

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeStepMockMvc.perform(put("/api/recipe-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeStep)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeStep in the database
        List<RecipeStep> recipeStepList = recipeStepRepository.findAll();
        assertThat(recipeStepList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RecipeStep in Elasticsearch
        verify(mockRecipeStepSearchRepository, times(0)).save(recipeStep);
    }

    @Test
    @Transactional
    public void deleteRecipeStep() throws Exception {
        // Initialize the database
        recipeStepService.save(recipeStep);

        int databaseSizeBeforeDelete = recipeStepRepository.findAll().size();

        // Delete the recipeStep
        restRecipeStepMockMvc.perform(delete("/api/recipe-steps/{id}", recipeStep.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecipeStep> recipeStepList = recipeStepRepository.findAll();
        assertThat(recipeStepList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RecipeStep in Elasticsearch
        verify(mockRecipeStepSearchRepository, times(1)).deleteById(recipeStep.getId());
    }

    @Test
    @Transactional
    public void searchRecipeStep() throws Exception {
        // Initialize the database
        recipeStepService.save(recipeStep);
        when(mockRecipeStepSearchRepository.search(queryStringQuery("id:" + recipeStep.getId())))
            .thenReturn(Collections.singletonList(recipeStep));
        // Search the recipeStep
        restRecipeStepMockMvc.perform(get("/api/_search/recipe-steps?query=id:" + recipeStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].instruction").value(hasItem(DEFAULT_INSTRUCTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeStep.class);
        RecipeStep recipeStep1 = new RecipeStep();
        recipeStep1.setId(1L);
        RecipeStep recipeStep2 = new RecipeStep();
        recipeStep2.setId(recipeStep1.getId());
        assertThat(recipeStep1).isEqualTo(recipeStep2);
        recipeStep2.setId(2L);
        assertThat(recipeStep1).isNotEqualTo(recipeStep2);
        recipeStep1.setId(null);
        assertThat(recipeStep1).isNotEqualTo(recipeStep2);
    }
}

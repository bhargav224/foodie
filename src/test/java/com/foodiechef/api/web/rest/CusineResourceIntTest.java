package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.Cusine;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.MenuItem;
import com.foodiechef.api.repository.CusineRepository;
import com.foodiechef.api.repository.search.CusineSearchRepository;
import com.foodiechef.api.service.CusineService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.CusineCriteria;
import com.foodiechef.api.service.CusineQueryService;

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
 * Test class for the CusineResource REST controller.
 *
 * @see CusineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class CusineResourceIntTest {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CusineRepository cusineRepository;

    @Autowired
    private CusineService cusineService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.CusineSearchRepositoryMockConfiguration
     */
    @Autowired
    private CusineSearchRepository mockCusineSearchRepository;

    @Autowired
    private CusineQueryService cusineQueryService;

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

    private MockMvc restCusineMockMvc;

    private Cusine cusine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CusineResource cusineResource = new CusineResource(cusineService, cusineQueryService);
        this.restCusineMockMvc = MockMvcBuilders.standaloneSetup(cusineResource)
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
    public static Cusine createEntity(EntityManager em) {
        Cusine cusine = new Cusine()
            .active(DEFAULT_ACTIVE)
            .description(DEFAULT_DESCRIPTION)
            .name(DEFAULT_NAME);
        return cusine;
    }

    @Before
    public void initTest() {
        cusine = createEntity(em);
    }

    @Test
    @Transactional
    public void createCusine() throws Exception {
        int databaseSizeBeforeCreate = cusineRepository.findAll().size();

        // Create the Cusine
        restCusineMockMvc.perform(post("/api/cusines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cusine)))
            .andExpect(status().isCreated());

        // Validate the Cusine in the database
        List<Cusine> cusineList = cusineRepository.findAll();
        assertThat(cusineList).hasSize(databaseSizeBeforeCreate + 1);
        Cusine testCusine = cusineList.get(cusineList.size() - 1);
        assertThat(testCusine.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCusine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCusine.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Cusine in Elasticsearch
        verify(mockCusineSearchRepository, times(1)).save(testCusine);
    }

    @Test
    @Transactional
    public void createCusineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cusineRepository.findAll().size();

        // Create the Cusine with an existing ID
        cusine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCusineMockMvc.perform(post("/api/cusines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cusine)))
            .andExpect(status().isBadRequest());

        // Validate the Cusine in the database
        List<Cusine> cusineList = cusineRepository.findAll();
        assertThat(cusineList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cusine in Elasticsearch
        verify(mockCusineSearchRepository, times(0)).save(cusine);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = cusineRepository.findAll().size();
        // set the field null
        cusine.setActive(null);

        // Create the Cusine, which fails.

        restCusineMockMvc.perform(post("/api/cusines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cusine)))
            .andExpect(status().isBadRequest());

        List<Cusine> cusineList = cusineRepository.findAll();
        assertThat(cusineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cusineRepository.findAll().size();
        // set the field null
        cusine.setDescription(null);

        // Create the Cusine, which fails.

        restCusineMockMvc.perform(post("/api/cusines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cusine)))
            .andExpect(status().isBadRequest());

        List<Cusine> cusineList = cusineRepository.findAll();
        assertThat(cusineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cusineRepository.findAll().size();
        // set the field null
        cusine.setName(null);

        // Create the Cusine, which fails.

        restCusineMockMvc.perform(post("/api/cusines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cusine)))
            .andExpect(status().isBadRequest());

        List<Cusine> cusineList = cusineRepository.findAll();
        assertThat(cusineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCusines() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList
        restCusineMockMvc.perform(get("/api/cusines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cusine.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getCusine() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get the cusine
        restCusineMockMvc.perform(get("/api/cusines/{id}", cusine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cusine.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllCusinesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where active equals to DEFAULT_ACTIVE
        defaultCusineShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the cusineList where active equals to UPDATED_ACTIVE
        defaultCusineShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCusinesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultCusineShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the cusineList where active equals to UPDATED_ACTIVE
        defaultCusineShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCusinesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where active is not null
        defaultCusineShouldBeFound("active.specified=true");

        // Get all the cusineList where active is null
        defaultCusineShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllCusinesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where description equals to DEFAULT_DESCRIPTION
        defaultCusineShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the cusineList where description equals to UPDATED_DESCRIPTION
        defaultCusineShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCusinesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCusineShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the cusineList where description equals to UPDATED_DESCRIPTION
        defaultCusineShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCusinesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where description is not null
        defaultCusineShouldBeFound("description.specified=true");

        // Get all the cusineList where description is null
        defaultCusineShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCusinesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where name equals to DEFAULT_NAME
        defaultCusineShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cusineList where name equals to UPDATED_NAME
        defaultCusineShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCusinesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCusineShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cusineList where name equals to UPDATED_NAME
        defaultCusineShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCusinesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cusineRepository.saveAndFlush(cusine);

        // Get all the cusineList where name is not null
        defaultCusineShouldBeFound("name.specified=true");

        // Get all the cusineList where name is null
        defaultCusineShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCusinesByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        cusine.addRecipe(recipe);
        cusineRepository.saveAndFlush(cusine);
        Long recipeId = recipe.getId();

        // Get all the cusineList where recipe equals to recipeId
        defaultCusineShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the cusineList where recipe equals to recipeId + 1
        defaultCusineShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllCusinesByMenuItemIsEqualToSomething() throws Exception {
        // Initialize the database
        MenuItem menuItem = MenuItemResourceIntTest.createEntity(em);
        em.persist(menuItem);
        em.flush();
        cusine.addMenuItem(menuItem);
        cusineRepository.saveAndFlush(cusine);
        Long menuItemId = menuItem.getId();

        // Get all the cusineList where menuItem equals to menuItemId
        defaultCusineShouldBeFound("menuItemId.equals=" + menuItemId);

        // Get all the cusineList where menuItem equals to menuItemId + 1
        defaultCusineShouldNotBeFound("menuItemId.equals=" + (menuItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCusineShouldBeFound(String filter) throws Exception {
        restCusineMockMvc.perform(get("/api/cusines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cusine.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCusineMockMvc.perform(get("/api/cusines/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCusineShouldNotBeFound(String filter) throws Exception {
        restCusineMockMvc.perform(get("/api/cusines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCusineMockMvc.perform(get("/api/cusines/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCusine() throws Exception {
        // Get the cusine
        restCusineMockMvc.perform(get("/api/cusines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCusine() throws Exception {
        // Initialize the database
        cusineService.save(cusine);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCusineSearchRepository);

        int databaseSizeBeforeUpdate = cusineRepository.findAll().size();

        // Update the cusine
        Cusine updatedCusine = cusineRepository.findById(cusine.getId()).get();
        // Disconnect from session so that the updates on updatedCusine are not directly saved in db
        em.detach(updatedCusine);
        updatedCusine
            .active(UPDATED_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME);

        restCusineMockMvc.perform(put("/api/cusines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCusine)))
            .andExpect(status().isOk());

        // Validate the Cusine in the database
        List<Cusine> cusineList = cusineRepository.findAll();
        assertThat(cusineList).hasSize(databaseSizeBeforeUpdate);
        Cusine testCusine = cusineList.get(cusineList.size() - 1);
        assertThat(testCusine.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCusine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCusine.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Cusine in Elasticsearch
        verify(mockCusineSearchRepository, times(1)).save(testCusine);
    }

    @Test
    @Transactional
    public void updateNonExistingCusine() throws Exception {
        int databaseSizeBeforeUpdate = cusineRepository.findAll().size();

        // Create the Cusine

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCusineMockMvc.perform(put("/api/cusines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cusine)))
            .andExpect(status().isBadRequest());

        // Validate the Cusine in the database
        List<Cusine> cusineList = cusineRepository.findAll();
        assertThat(cusineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cusine in Elasticsearch
        verify(mockCusineSearchRepository, times(0)).save(cusine);
    }

    @Test
    @Transactional
    public void deleteCusine() throws Exception {
        // Initialize the database
        cusineService.save(cusine);

        int databaseSizeBeforeDelete = cusineRepository.findAll().size();

        // Delete the cusine
        restCusineMockMvc.perform(delete("/api/cusines/{id}", cusine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cusine> cusineList = cusineRepository.findAll();
        assertThat(cusineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cusine in Elasticsearch
        verify(mockCusineSearchRepository, times(1)).deleteById(cusine.getId());
    }

    @Test
    @Transactional
    public void searchCusine() throws Exception {
        // Initialize the database
        cusineService.save(cusine);
        when(mockCusineSearchRepository.search(queryStringQuery("id:" + cusine.getId())))
            .thenReturn(Collections.singletonList(cusine));
        // Search the cusine
        restCusineMockMvc.perform(get("/api/_search/cusines?query=id:" + cusine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cusine.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cusine.class);
        Cusine cusine1 = new Cusine();
        cusine1.setId(1L);
        Cusine cusine2 = new Cusine();
        cusine2.setId(cusine1.getId());
        assertThat(cusine1).isEqualTo(cusine2);
        cusine2.setId(2L);
        assertThat(cusine1).isNotEqualTo(cusine2);
        cusine1.setId(null);
        assertThat(cusine1).isNotEqualTo(cusine2);
    }
}

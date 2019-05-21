package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.MenuRecipe;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.MenuItem;
import com.foodiechef.api.repository.MenuRecipeRepository;
import com.foodiechef.api.repository.search.MenuRecipeSearchRepository;
import com.foodiechef.api.service.MenuRecipeService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.MenuRecipeCriteria;
import com.foodiechef.api.service.MenuRecipeQueryService;

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
 * Test class for the MenuRecipeResource REST controller.
 *
 * @see MenuRecipeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class MenuRecipeResourceIntTest {

    @Autowired
    private MenuRecipeRepository menuRecipeRepository;

    @Autowired
    private MenuRecipeService menuRecipeService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.MenuRecipeSearchRepositoryMockConfiguration
     */
    @Autowired
    private MenuRecipeSearchRepository mockMenuRecipeSearchRepository;

    @Autowired
    private MenuRecipeQueryService menuRecipeQueryService;

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

    private MockMvc restMenuRecipeMockMvc;

    private MenuRecipe menuRecipe;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MenuRecipeResource menuRecipeResource = new MenuRecipeResource(menuRecipeService, menuRecipeQueryService);
        this.restMenuRecipeMockMvc = MockMvcBuilders.standaloneSetup(menuRecipeResource)
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
    public static MenuRecipe createEntity(EntityManager em) {
        MenuRecipe menuRecipe = new MenuRecipe();
        return menuRecipe;
    }

    @Before
    public void initTest() {
        menuRecipe = createEntity(em);
    }

    @Test
    @Transactional
    public void createMenuRecipe() throws Exception {
        int databaseSizeBeforeCreate = menuRecipeRepository.findAll().size();

        // Create the MenuRecipe
        restMenuRecipeMockMvc.perform(post("/api/menu-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuRecipe)))
            .andExpect(status().isCreated());

        // Validate the MenuRecipe in the database
        List<MenuRecipe> menuRecipeList = menuRecipeRepository.findAll();
        assertThat(menuRecipeList).hasSize(databaseSizeBeforeCreate + 1);
        MenuRecipe testMenuRecipe = menuRecipeList.get(menuRecipeList.size() - 1);

        // Validate the MenuRecipe in Elasticsearch
        verify(mockMenuRecipeSearchRepository, times(1)).save(testMenuRecipe);
    }

    @Test
    @Transactional
    public void createMenuRecipeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = menuRecipeRepository.findAll().size();

        // Create the MenuRecipe with an existing ID
        menuRecipe.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuRecipeMockMvc.perform(post("/api/menu-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuRecipe)))
            .andExpect(status().isBadRequest());

        // Validate the MenuRecipe in the database
        List<MenuRecipe> menuRecipeList = menuRecipeRepository.findAll();
        assertThat(menuRecipeList).hasSize(databaseSizeBeforeCreate);

        // Validate the MenuRecipe in Elasticsearch
        verify(mockMenuRecipeSearchRepository, times(0)).save(menuRecipe);
    }

    @Test
    @Transactional
    public void getAllMenuRecipes() throws Exception {
        // Initialize the database
        menuRecipeRepository.saveAndFlush(menuRecipe);

        // Get all the menuRecipeList
        restMenuRecipeMockMvc.perform(get("/api/menu-recipes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuRecipe.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getMenuRecipe() throws Exception {
        // Initialize the database
        menuRecipeRepository.saveAndFlush(menuRecipe);

        // Get the menuRecipe
        restMenuRecipeMockMvc.perform(get("/api/menu-recipes/{id}", menuRecipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(menuRecipe.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllMenuRecipesByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        menuRecipe.setRecipe(recipe);
        menuRecipeRepository.saveAndFlush(menuRecipe);
        Long recipeId = recipe.getId();

        // Get all the menuRecipeList where recipe equals to recipeId
        defaultMenuRecipeShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the menuRecipeList where recipe equals to recipeId + 1
        defaultMenuRecipeShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllMenuRecipesByMenuItemIsEqualToSomething() throws Exception {
        // Initialize the database
        MenuItem menuItem = MenuItemResourceIntTest.createEntity(em);
        em.persist(menuItem);
        em.flush();
        menuRecipe.setMenuItem(menuItem);
        menuRecipeRepository.saveAndFlush(menuRecipe);
        Long menuItemId = menuItem.getId();

        // Get all the menuRecipeList where menuItem equals to menuItemId
        defaultMenuRecipeShouldBeFound("menuItemId.equals=" + menuItemId);

        // Get all the menuRecipeList where menuItem equals to menuItemId + 1
        defaultMenuRecipeShouldNotBeFound("menuItemId.equals=" + (menuItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMenuRecipeShouldBeFound(String filter) throws Exception {
        restMenuRecipeMockMvc.perform(get("/api/menu-recipes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuRecipe.getId().intValue())));

        // Check, that the count call also returns 1
        restMenuRecipeMockMvc.perform(get("/api/menu-recipes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMenuRecipeShouldNotBeFound(String filter) throws Exception {
        restMenuRecipeMockMvc.perform(get("/api/menu-recipes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuRecipeMockMvc.perform(get("/api/menu-recipes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMenuRecipe() throws Exception {
        // Get the menuRecipe
        restMenuRecipeMockMvc.perform(get("/api/menu-recipes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuRecipe() throws Exception {
        // Initialize the database
        menuRecipeService.save(menuRecipe);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMenuRecipeSearchRepository);

        int databaseSizeBeforeUpdate = menuRecipeRepository.findAll().size();

        // Update the menuRecipe
        MenuRecipe updatedMenuRecipe = menuRecipeRepository.findById(menuRecipe.getId()).get();
        // Disconnect from session so that the updates on updatedMenuRecipe are not directly saved in db
        em.detach(updatedMenuRecipe);

        restMenuRecipeMockMvc.perform(put("/api/menu-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMenuRecipe)))
            .andExpect(status().isOk());

        // Validate the MenuRecipe in the database
        List<MenuRecipe> menuRecipeList = menuRecipeRepository.findAll();
        assertThat(menuRecipeList).hasSize(databaseSizeBeforeUpdate);
        MenuRecipe testMenuRecipe = menuRecipeList.get(menuRecipeList.size() - 1);

        // Validate the MenuRecipe in Elasticsearch
        verify(mockMenuRecipeSearchRepository, times(1)).save(testMenuRecipe);
    }

    @Test
    @Transactional
    public void updateNonExistingMenuRecipe() throws Exception {
        int databaseSizeBeforeUpdate = menuRecipeRepository.findAll().size();

        // Create the MenuRecipe

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuRecipeMockMvc.perform(put("/api/menu-recipes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuRecipe)))
            .andExpect(status().isBadRequest());

        // Validate the MenuRecipe in the database
        List<MenuRecipe> menuRecipeList = menuRecipeRepository.findAll();
        assertThat(menuRecipeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MenuRecipe in Elasticsearch
        verify(mockMenuRecipeSearchRepository, times(0)).save(menuRecipe);
    }

    @Test
    @Transactional
    public void deleteMenuRecipe() throws Exception {
        // Initialize the database
        menuRecipeService.save(menuRecipe);

        int databaseSizeBeforeDelete = menuRecipeRepository.findAll().size();

        // Delete the menuRecipe
        restMenuRecipeMockMvc.perform(delete("/api/menu-recipes/{id}", menuRecipe.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MenuRecipe> menuRecipeList = menuRecipeRepository.findAll();
        assertThat(menuRecipeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MenuRecipe in Elasticsearch
        verify(mockMenuRecipeSearchRepository, times(1)).deleteById(menuRecipe.getId());
    }

    @Test
    @Transactional
    public void searchMenuRecipe() throws Exception {
        // Initialize the database
        menuRecipeService.save(menuRecipe);
        when(mockMenuRecipeSearchRepository.search(queryStringQuery("id:" + menuRecipe.getId())))
            .thenReturn(Collections.singletonList(menuRecipe));
        // Search the menuRecipe
        restMenuRecipeMockMvc.perform(get("/api/_search/menu-recipes?query=id:" + menuRecipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuRecipe.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuRecipe.class);
        MenuRecipe menuRecipe1 = new MenuRecipe();
        menuRecipe1.setId(1L);
        MenuRecipe menuRecipe2 = new MenuRecipe();
        menuRecipe2.setId(menuRecipe1.getId());
        assertThat(menuRecipe1).isEqualTo(menuRecipe2);
        menuRecipe2.setId(2L);
        assertThat(menuRecipe1).isNotEqualTo(menuRecipe2);
        menuRecipe1.setId(null);
        assertThat(menuRecipe1).isNotEqualTo(menuRecipe2);
    }
}

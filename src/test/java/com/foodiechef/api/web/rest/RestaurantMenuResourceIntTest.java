package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.RestaurantMenu;
import com.foodiechef.api.domain.MenuItem;
import com.foodiechef.api.domain.Restaurant;
import com.foodiechef.api.repository.RestaurantMenuRepository;
import com.foodiechef.api.repository.search.RestaurantMenuSearchRepository;
import com.foodiechef.api.service.RestaurantMenuService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RestaurantMenuCriteria;
import com.foodiechef.api.service.RestaurantMenuQueryService;

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
 * Test class for the RestaurantMenuResource REST controller.
 *
 * @see RestaurantMenuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RestaurantMenuResourceIntTest {

    @Autowired
    private RestaurantMenuRepository restaurantMenuRepository;

    @Autowired
    private RestaurantMenuService restaurantMenuService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RestaurantMenuSearchRepositoryMockConfiguration
     */
    @Autowired
    private RestaurantMenuSearchRepository mockRestaurantMenuSearchRepository;

    @Autowired
    private RestaurantMenuQueryService restaurantMenuQueryService;

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

    private MockMvc restRestaurantMenuMockMvc;

    private RestaurantMenu restaurantMenu;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RestaurantMenuResource restaurantMenuResource = new RestaurantMenuResource(restaurantMenuService, restaurantMenuQueryService);
        this.restRestaurantMenuMockMvc = MockMvcBuilders.standaloneSetup(restaurantMenuResource)
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
    public static RestaurantMenu createEntity(EntityManager em) {
        RestaurantMenu restaurantMenu = new RestaurantMenu();
        return restaurantMenu;
    }

    @Before
    public void initTest() {
        restaurantMenu = createEntity(em);
    }

    @Test
    @Transactional
    public void createRestaurantMenu() throws Exception {
        int databaseSizeBeforeCreate = restaurantMenuRepository.findAll().size();

        // Create the RestaurantMenu
        restRestaurantMenuMockMvc.perform(post("/api/restaurant-menus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurantMenu)))
            .andExpect(status().isCreated());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeCreate + 1);
        RestaurantMenu testRestaurantMenu = restaurantMenuList.get(restaurantMenuList.size() - 1);

        // Validate the RestaurantMenu in Elasticsearch
        verify(mockRestaurantMenuSearchRepository, times(1)).save(testRestaurantMenu);
    }

    @Test
    @Transactional
    public void createRestaurantMenuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = restaurantMenuRepository.findAll().size();

        // Create the RestaurantMenu with an existing ID
        restaurantMenu.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMenuMockMvc.perform(post("/api/restaurant-menus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurantMenu)))
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeCreate);

        // Validate the RestaurantMenu in Elasticsearch
        verify(mockRestaurantMenuSearchRepository, times(0)).save(restaurantMenu);
    }

    @Test
    @Transactional
    public void getAllRestaurantMenus() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        // Get all the restaurantMenuList
        restRestaurantMenuMockMvc.perform(get("/api/restaurant-menus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantMenu.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        // Get the restaurantMenu
        restRestaurantMenuMockMvc.perform(get("/api/restaurant-menus/{id}", restaurantMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantMenu.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllRestaurantMenusByMenuItemIsEqualToSomething() throws Exception {
        // Initialize the database
        MenuItem menuItem = MenuItemResourceIntTest.createEntity(em);
        em.persist(menuItem);
        em.flush();
        restaurantMenu.setMenuItem(menuItem);
        restaurantMenuRepository.saveAndFlush(restaurantMenu);
        Long menuItemId = menuItem.getId();

        // Get all the restaurantMenuList where menuItem equals to menuItemId
        defaultRestaurantMenuShouldBeFound("menuItemId.equals=" + menuItemId);

        // Get all the restaurantMenuList where menuItem equals to menuItemId + 1
        defaultRestaurantMenuShouldNotBeFound("menuItemId.equals=" + (menuItemId + 1));
    }


    @Test
    @Transactional
    public void getAllRestaurantMenusByRestaurantIsEqualToSomething() throws Exception {
        // Initialize the database
        Restaurant restaurant = RestaurantResourceIntTest.createEntity(em);
        em.persist(restaurant);
        em.flush();
        restaurantMenu.setRestaurant(restaurant);
        restaurantMenuRepository.saveAndFlush(restaurantMenu);
        Long restaurantId = restaurant.getId();

        // Get all the restaurantMenuList where restaurant equals to restaurantId
        defaultRestaurantMenuShouldBeFound("restaurantId.equals=" + restaurantId);

        // Get all the restaurantMenuList where restaurant equals to restaurantId + 1
        defaultRestaurantMenuShouldNotBeFound("restaurantId.equals=" + (restaurantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRestaurantMenuShouldBeFound(String filter) throws Exception {
        restRestaurantMenuMockMvc.perform(get("/api/restaurant-menus?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantMenu.getId().intValue())));

        // Check, that the count call also returns 1
        restRestaurantMenuMockMvc.perform(get("/api/restaurant-menus/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRestaurantMenuShouldNotBeFound(String filter) throws Exception {
        restRestaurantMenuMockMvc.perform(get("/api/restaurant-menus?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRestaurantMenuMockMvc.perform(get("/api/restaurant-menus/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRestaurantMenu() throws Exception {
        // Get the restaurantMenu
        restRestaurantMenuMockMvc.perform(get("/api/restaurant-menus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuService.save(restaurantMenu);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRestaurantMenuSearchRepository);

        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();

        // Update the restaurantMenu
        RestaurantMenu updatedRestaurantMenu = restaurantMenuRepository.findById(restaurantMenu.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurantMenu are not directly saved in db
        em.detach(updatedRestaurantMenu);

        restRestaurantMenuMockMvc.perform(put("/api/restaurant-menus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRestaurantMenu)))
            .andExpect(status().isOk());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
        RestaurantMenu testRestaurantMenu = restaurantMenuList.get(restaurantMenuList.size() - 1);

        // Validate the RestaurantMenu in Elasticsearch
        verify(mockRestaurantMenuSearchRepository, times(1)).save(testRestaurantMenu);
    }

    @Test
    @Transactional
    public void updateNonExistingRestaurantMenu() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();

        // Create the RestaurantMenu

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc.perform(put("/api/restaurant-menus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurantMenu)))
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantMenu in Elasticsearch
        verify(mockRestaurantMenuSearchRepository, times(0)).save(restaurantMenu);
    }

    @Test
    @Transactional
    public void deleteRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuService.save(restaurantMenu);

        int databaseSizeBeforeDelete = restaurantMenuRepository.findAll().size();

        // Delete the restaurantMenu
        restRestaurantMenuMockMvc.perform(delete("/api/restaurant-menus/{id}", restaurantMenu.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RestaurantMenu in Elasticsearch
        verify(mockRestaurantMenuSearchRepository, times(1)).deleteById(restaurantMenu.getId());
    }

    @Test
    @Transactional
    public void searchRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuService.save(restaurantMenu);
        when(mockRestaurantMenuSearchRepository.search(queryStringQuery("id:" + restaurantMenu.getId())))
            .thenReturn(Collections.singletonList(restaurantMenu));
        // Search the restaurantMenu
        restRestaurantMenuMockMvc.perform(get("/api/_search/restaurant-menus?query=id:" + restaurantMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantMenu.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestaurantMenu.class);
        RestaurantMenu restaurantMenu1 = new RestaurantMenu();
        restaurantMenu1.setId(1L);
        RestaurantMenu restaurantMenu2 = new RestaurantMenu();
        restaurantMenu2.setId(restaurantMenu1.getId());
        assertThat(restaurantMenu1).isEqualTo(restaurantMenu2);
        restaurantMenu2.setId(2L);
        assertThat(restaurantMenu1).isNotEqualTo(restaurantMenu2);
        restaurantMenu1.setId(null);
        assertThat(restaurantMenu1).isNotEqualTo(restaurantMenu2);
    }
}

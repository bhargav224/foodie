package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.MenuItem;
import com.foodiechef.api.domain.RestaurantMenu;
import com.foodiechef.api.domain.MenuRecipe;
import com.foodiechef.api.domain.FoodCategorie;
import com.foodiechef.api.domain.Course;
import com.foodiechef.api.domain.Cusine;
import com.foodiechef.api.repository.MenuItemRepository;
import com.foodiechef.api.repository.search.MenuItemSearchRepository;
import com.foodiechef.api.service.MenuItemService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.MenuItemCriteria;
import com.foodiechef.api.service.MenuItemQueryService;

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
 * Test class for the MenuItemResource REST controller.
 *
 * @see MenuItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class MenuItemResourceIntTest {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemService menuItemService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.MenuItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private MenuItemSearchRepository mockMenuItemSearchRepository;

    @Autowired
    private MenuItemQueryService menuItemQueryService;

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

    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MenuItemResource menuItemResource = new MenuItemResource(menuItemService, menuItemQueryService);
        this.restMenuItemMockMvc = MockMvcBuilders.standaloneSetup(menuItemResource)
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
    public static MenuItem createEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem()
            .active(DEFAULT_ACTIVE)
            .description(DEFAULT_DESCRIPTION)
            .imagePath(DEFAULT_IMAGE_PATH)
            .name(DEFAULT_NAME);
        return menuItem;
    }

    @Before
    public void initTest() {
        menuItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createMenuItem() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // Create the MenuItem
        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate + 1);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testMenuItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMenuItem.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testMenuItem.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the MenuItem in Elasticsearch
        verify(mockMenuItemSearchRepository, times(1)).save(testMenuItem);
    }

    @Test
    @Transactional
    public void createMenuItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // Create the MenuItem with an existing ID
        menuItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the MenuItem in Elasticsearch
        verify(mockMenuItemSearchRepository, times(0)).save(menuItem);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setActive(null);

        // Create the MenuItem, which fails.

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setDescription(null);

        // Create the MenuItem, which fails.

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImagePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setImagePath(null);

        // Create the MenuItem, which fails.

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setName(null);

        // Create the MenuItem, which fails.

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(menuItem.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imagePath").value(DEFAULT_IMAGE_PATH.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllMenuItemsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where active equals to DEFAULT_ACTIVE
        defaultMenuItemShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the menuItemList where active equals to UPDATED_ACTIVE
        defaultMenuItemShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultMenuItemShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the menuItemList where active equals to UPDATED_ACTIVE
        defaultMenuItemShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where active is not null
        defaultMenuItemShouldBeFound("active.specified=true");

        // Get all the menuItemList where active is null
        defaultMenuItemShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where description equals to DEFAULT_DESCRIPTION
        defaultMenuItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the menuItemList where description equals to UPDATED_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMenuItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the menuItemList where description equals to UPDATED_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where description is not null
        defaultMenuItemShouldBeFound("description.specified=true");

        // Get all the menuItemList where description is null
        defaultMenuItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByImagePathIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where imagePath equals to DEFAULT_IMAGE_PATH
        defaultMenuItemShouldBeFound("imagePath.equals=" + DEFAULT_IMAGE_PATH);

        // Get all the menuItemList where imagePath equals to UPDATED_IMAGE_PATH
        defaultMenuItemShouldNotBeFound("imagePath.equals=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByImagePathIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where imagePath in DEFAULT_IMAGE_PATH or UPDATED_IMAGE_PATH
        defaultMenuItemShouldBeFound("imagePath.in=" + DEFAULT_IMAGE_PATH + "," + UPDATED_IMAGE_PATH);

        // Get all the menuItemList where imagePath equals to UPDATED_IMAGE_PATH
        defaultMenuItemShouldNotBeFound("imagePath.in=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByImagePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where imagePath is not null
        defaultMenuItemShouldBeFound("imagePath.specified=true");

        // Get all the menuItemList where imagePath is null
        defaultMenuItemShouldNotBeFound("imagePath.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name equals to DEFAULT_NAME
        defaultMenuItemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the menuItemList where name equals to UPDATED_NAME
        defaultMenuItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMenuItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the menuItemList where name equals to UPDATED_NAME
        defaultMenuItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name is not null
        defaultMenuItemShouldBeFound("name.specified=true");

        // Get all the menuItemList where name is null
        defaultMenuItemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByRestaurantMenuIsEqualToSomething() throws Exception {
        // Initialize the database
        RestaurantMenu restaurantMenu = RestaurantMenuResourceIntTest.createEntity(em);
        em.persist(restaurantMenu);
        em.flush();
        menuItem.addRestaurantMenu(restaurantMenu);
        menuItemRepository.saveAndFlush(menuItem);
        Long restaurantMenuId = restaurantMenu.getId();

        // Get all the menuItemList where restaurantMenu equals to restaurantMenuId
        defaultMenuItemShouldBeFound("restaurantMenuId.equals=" + restaurantMenuId);

        // Get all the menuItemList where restaurantMenu equals to restaurantMenuId + 1
        defaultMenuItemShouldNotBeFound("restaurantMenuId.equals=" + (restaurantMenuId + 1));
    }


    @Test
    @Transactional
    public void getAllMenuItemsByMenuRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        MenuRecipe menuRecipe = MenuRecipeResourceIntTest.createEntity(em);
        em.persist(menuRecipe);
        em.flush();
        menuItem.addMenuRecipe(menuRecipe);
        menuItemRepository.saveAndFlush(menuItem);
        Long menuRecipeId = menuRecipe.getId();

        // Get all the menuItemList where menuRecipe equals to menuRecipeId
        defaultMenuItemShouldBeFound("menuRecipeId.equals=" + menuRecipeId);

        // Get all the menuItemList where menuRecipe equals to menuRecipeId + 1
        defaultMenuItemShouldNotBeFound("menuRecipeId.equals=" + (menuRecipeId + 1));
    }


    @Test
    @Transactional
    public void getAllMenuItemsByFoodCategorieIsEqualToSomething() throws Exception {
        // Initialize the database
        FoodCategorie foodCategorie = FoodCategorieResourceIntTest.createEntity(em);
        em.persist(foodCategorie);
        em.flush();
        menuItem.setFoodCategorie(foodCategorie);
        menuItemRepository.saveAndFlush(menuItem);
        Long foodCategorieId = foodCategorie.getId();

        // Get all the menuItemList where foodCategorie equals to foodCategorieId
        defaultMenuItemShouldBeFound("foodCategorieId.equals=" + foodCategorieId);

        // Get all the menuItemList where foodCategorie equals to foodCategorieId + 1
        defaultMenuItemShouldNotBeFound("foodCategorieId.equals=" + (foodCategorieId + 1));
    }


    @Test
    @Transactional
    public void getAllMenuItemsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        Course course = CourseResourceIntTest.createEntity(em);
        em.persist(course);
        em.flush();
        menuItem.setCourse(course);
        menuItemRepository.saveAndFlush(menuItem);
        Long courseId = course.getId();

        // Get all the menuItemList where course equals to courseId
        defaultMenuItemShouldBeFound("courseId.equals=" + courseId);

        // Get all the menuItemList where course equals to courseId + 1
        defaultMenuItemShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }


    @Test
    @Transactional
    public void getAllMenuItemsByCusineIsEqualToSomething() throws Exception {
        // Initialize the database
        Cusine cusine = CusineResourceIntTest.createEntity(em);
        em.persist(cusine);
        em.flush();
        menuItem.setCusine(cusine);
        menuItemRepository.saveAndFlush(menuItem);
        Long cusineId = cusine.getId();

        // Get all the menuItemList where cusine equals to cusineId
        defaultMenuItemShouldBeFound("cusineId.equals=" + cusineId);

        // Get all the menuItemList where cusine equals to cusineId + 1
        defaultMenuItemShouldNotBeFound("cusineId.equals=" + (cusineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMenuItemShouldBeFound(String filter) throws Exception {
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restMenuItemMockMvc.perform(get("/api/menu-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMenuItemShouldNotBeFound(String filter) throws Exception {
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuItemMockMvc.perform(get("/api/menu-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuItem() throws Exception {
        // Initialize the database
        menuItemService.save(menuItem);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMenuItemSearchRepository);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem
        MenuItem updatedMenuItem = menuItemRepository.findById(menuItem.getId()).get();
        // Disconnect from session so that the updates on updatedMenuItem are not directly saved in db
        em.detach(updatedMenuItem);
        updatedMenuItem
            .active(UPDATED_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .imagePath(UPDATED_IMAGE_PATH)
            .name(UPDATED_NAME);

        restMenuItemMockMvc.perform(put("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMenuItem)))
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMenuItem.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testMenuItem.getName()).isEqualTo(UPDATED_NAME);

        // Validate the MenuItem in Elasticsearch
        verify(mockMenuItemSearchRepository, times(1)).save(testMenuItem);
    }

    @Test
    @Transactional
    public void updateNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Create the MenuItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc.perform(put("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MenuItem in Elasticsearch
        verify(mockMenuItemSearchRepository, times(0)).save(menuItem);
    }

    @Test
    @Transactional
    public void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemService.save(menuItem);

        int databaseSizeBeforeDelete = menuItemRepository.findAll().size();

        // Delete the menuItem
        restMenuItemMockMvc.perform(delete("/api/menu-items/{id}", menuItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MenuItem in Elasticsearch
        verify(mockMenuItemSearchRepository, times(1)).deleteById(menuItem.getId());
    }

    @Test
    @Transactional
    public void searchMenuItem() throws Exception {
        // Initialize the database
        menuItemService.save(menuItem);
        when(mockMenuItemSearchRepository.search(queryStringQuery("id:" + menuItem.getId())))
            .thenReturn(Collections.singletonList(menuItem));
        // Search the menuItem
        restMenuItemMockMvc.perform(get("/api/_search/menu-items?query=id:" + menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuItem.class);
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setId(1L);
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(menuItem1.getId());
        assertThat(menuItem1).isEqualTo(menuItem2);
        menuItem2.setId(2L);
        assertThat(menuItem1).isNotEqualTo(menuItem2);
        menuItem1.setId(null);
        assertThat(menuItem1).isNotEqualTo(menuItem2);
    }
}

package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.RecipeImage;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.repository.RecipeImageRepository;
import com.foodiechef.api.repository.search.RecipeImageSearchRepository;
import com.foodiechef.api.service.RecipeImageService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RecipeImageCriteria;
import com.foodiechef.api.service.RecipeImageQueryService;

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
 * Test class for the RecipeImageResource REST controller.
 *
 * @see RecipeImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RecipeImageResourceIntTest {

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    @Autowired
    private RecipeImageRepository recipeImageRepository;

    @Autowired
    private RecipeImageService recipeImageService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RecipeImageSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeImageSearchRepository mockRecipeImageSearchRepository;

    @Autowired
    private RecipeImageQueryService recipeImageQueryService;

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

    private MockMvc restRecipeImageMockMvc;

    private RecipeImage recipeImage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecipeImageResource recipeImageResource = new RecipeImageResource(recipeImageService, recipeImageQueryService);
        this.restRecipeImageMockMvc = MockMvcBuilders.standaloneSetup(recipeImageResource)
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
    public static RecipeImage createEntity(EntityManager em) {
        RecipeImage recipeImage = new RecipeImage()
            .imagePath(DEFAULT_IMAGE_PATH);
        return recipeImage;
    }

    @Before
    public void initTest() {
        recipeImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecipeImage() throws Exception {
        int databaseSizeBeforeCreate = recipeImageRepository.findAll().size();

        // Create the RecipeImage
        restRecipeImageMockMvc.perform(post("/api/recipe-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeImage)))
            .andExpect(status().isCreated());

        // Validate the RecipeImage in the database
        List<RecipeImage> recipeImageList = recipeImageRepository.findAll();
        assertThat(recipeImageList).hasSize(databaseSizeBeforeCreate + 1);
        RecipeImage testRecipeImage = recipeImageList.get(recipeImageList.size() - 1);
        assertThat(testRecipeImage.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);

        // Validate the RecipeImage in Elasticsearch
        verify(mockRecipeImageSearchRepository, times(1)).save(testRecipeImage);
    }

    @Test
    @Transactional
    public void createRecipeImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recipeImageRepository.findAll().size();

        // Create the RecipeImage with an existing ID
        recipeImage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeImageMockMvc.perform(post("/api/recipe-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeImage)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeImage in the database
        List<RecipeImage> recipeImageList = recipeImageRepository.findAll();
        assertThat(recipeImageList).hasSize(databaseSizeBeforeCreate);

        // Validate the RecipeImage in Elasticsearch
        verify(mockRecipeImageSearchRepository, times(0)).save(recipeImage);
    }

    @Test
    @Transactional
    public void checkImagePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeImageRepository.findAll().size();
        // set the field null
        recipeImage.setImagePath(null);

        // Create the RecipeImage, which fails.

        restRecipeImageMockMvc.perform(post("/api/recipe-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeImage)))
            .andExpect(status().isBadRequest());

        List<RecipeImage> recipeImageList = recipeImageRepository.findAll();
        assertThat(recipeImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecipeImages() throws Exception {
        // Initialize the database
        recipeImageRepository.saveAndFlush(recipeImage);

        // Get all the recipeImageList
        restRecipeImageMockMvc.perform(get("/api/recipe-images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH.toString())));
    }
    
    @Test
    @Transactional
    public void getRecipeImage() throws Exception {
        // Initialize the database
        recipeImageRepository.saveAndFlush(recipeImage);

        // Get the recipeImage
        restRecipeImageMockMvc.perform(get("/api/recipe-images/{id}", recipeImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recipeImage.getId().intValue()))
            .andExpect(jsonPath("$.imagePath").value(DEFAULT_IMAGE_PATH.toString()));
    }

    @Test
    @Transactional
    public void getAllRecipeImagesByImagePathIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeImageRepository.saveAndFlush(recipeImage);

        // Get all the recipeImageList where imagePath equals to DEFAULT_IMAGE_PATH
        defaultRecipeImageShouldBeFound("imagePath.equals=" + DEFAULT_IMAGE_PATH);

        // Get all the recipeImageList where imagePath equals to UPDATED_IMAGE_PATH
        defaultRecipeImageShouldNotBeFound("imagePath.equals=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void getAllRecipeImagesByImagePathIsInShouldWork() throws Exception {
        // Initialize the database
        recipeImageRepository.saveAndFlush(recipeImage);

        // Get all the recipeImageList where imagePath in DEFAULT_IMAGE_PATH or UPDATED_IMAGE_PATH
        defaultRecipeImageShouldBeFound("imagePath.in=" + DEFAULT_IMAGE_PATH + "," + UPDATED_IMAGE_PATH);

        // Get all the recipeImageList where imagePath equals to UPDATED_IMAGE_PATH
        defaultRecipeImageShouldNotBeFound("imagePath.in=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void getAllRecipeImagesByImagePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeImageRepository.saveAndFlush(recipeImage);

        // Get all the recipeImageList where imagePath is not null
        defaultRecipeImageShouldBeFound("imagePath.specified=true");

        // Get all the recipeImageList where imagePath is null
        defaultRecipeImageShouldNotBeFound("imagePath.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeImagesByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        recipeImage.setRecipe(recipe);
        recipeImageRepository.saveAndFlush(recipeImage);
        Long recipeId = recipe.getId();

        // Get all the recipeImageList where recipe equals to recipeId
        defaultRecipeImageShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the recipeImageList where recipe equals to recipeId + 1
        defaultRecipeImageShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecipeImageShouldBeFound(String filter) throws Exception {
        restRecipeImageMockMvc.perform(get("/api/recipe-images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)));

        // Check, that the count call also returns 1
        restRecipeImageMockMvc.perform(get("/api/recipe-images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecipeImageShouldNotBeFound(String filter) throws Exception {
        restRecipeImageMockMvc.perform(get("/api/recipe-images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeImageMockMvc.perform(get("/api/recipe-images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRecipeImage() throws Exception {
        // Get the recipeImage
        restRecipeImageMockMvc.perform(get("/api/recipe-images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipeImage() throws Exception {
        // Initialize the database
        recipeImageService.save(recipeImage);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecipeImageSearchRepository);

        int databaseSizeBeforeUpdate = recipeImageRepository.findAll().size();

        // Update the recipeImage
        RecipeImage updatedRecipeImage = recipeImageRepository.findById(recipeImage.getId()).get();
        // Disconnect from session so that the updates on updatedRecipeImage are not directly saved in db
        em.detach(updatedRecipeImage);
        updatedRecipeImage
            .imagePath(UPDATED_IMAGE_PATH);

        restRecipeImageMockMvc.perform(put("/api/recipe-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecipeImage)))
            .andExpect(status().isOk());

        // Validate the RecipeImage in the database
        List<RecipeImage> recipeImageList = recipeImageRepository.findAll();
        assertThat(recipeImageList).hasSize(databaseSizeBeforeUpdate);
        RecipeImage testRecipeImage = recipeImageList.get(recipeImageList.size() - 1);
        assertThat(testRecipeImage.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);

        // Validate the RecipeImage in Elasticsearch
        verify(mockRecipeImageSearchRepository, times(1)).save(testRecipeImage);
    }

    @Test
    @Transactional
    public void updateNonExistingRecipeImage() throws Exception {
        int databaseSizeBeforeUpdate = recipeImageRepository.findAll().size();

        // Create the RecipeImage

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeImageMockMvc.perform(put("/api/recipe-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeImage)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeImage in the database
        List<RecipeImage> recipeImageList = recipeImageRepository.findAll();
        assertThat(recipeImageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RecipeImage in Elasticsearch
        verify(mockRecipeImageSearchRepository, times(0)).save(recipeImage);
    }

    @Test
    @Transactional
    public void deleteRecipeImage() throws Exception {
        // Initialize the database
        recipeImageService.save(recipeImage);

        int databaseSizeBeforeDelete = recipeImageRepository.findAll().size();

        // Delete the recipeImage
        restRecipeImageMockMvc.perform(delete("/api/recipe-images/{id}", recipeImage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecipeImage> recipeImageList = recipeImageRepository.findAll();
        assertThat(recipeImageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RecipeImage in Elasticsearch
        verify(mockRecipeImageSearchRepository, times(1)).deleteById(recipeImage.getId());
    }

    @Test
    @Transactional
    public void searchRecipeImage() throws Exception {
        // Initialize the database
        recipeImageService.save(recipeImage);
        when(mockRecipeImageSearchRepository.search(queryStringQuery("id:" + recipeImage.getId())))
            .thenReturn(Collections.singletonList(recipeImage));
        // Search the recipeImage
        restRecipeImageMockMvc.perform(get("/api/_search/recipe-images?query=id:" + recipeImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeImage.class);
        RecipeImage recipeImage1 = new RecipeImage();
        recipeImage1.setId(1L);
        RecipeImage recipeImage2 = new RecipeImage();
        recipeImage2.setId(recipeImage1.getId());
        assertThat(recipeImage1).isEqualTo(recipeImage2);
        recipeImage2.setId(2L);
        assertThat(recipeImage1).isNotEqualTo(recipeImage2);
        recipeImage1.setId(null);
        assertThat(recipeImage1).isNotEqualTo(recipeImage2);
    }
}

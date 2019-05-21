package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.RecipeComment;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.RecipeCommentRepository;
import com.foodiechef.api.repository.search.RecipeCommentSearchRepository;
import com.foodiechef.api.service.RecipeCommentService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.RecipeCommentCriteria;
import com.foodiechef.api.service.RecipeCommentQueryService;

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
 * Test class for the RecipeCommentResource REST controller.
 *
 * @see RecipeCommentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class RecipeCommentResourceIntTest {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RecipeCommentRepository recipeCommentRepository;

    @Autowired
    private RecipeCommentService recipeCommentService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.RecipeCommentSearchRepositoryMockConfiguration
     */
    @Autowired
    private RecipeCommentSearchRepository mockRecipeCommentSearchRepository;

    @Autowired
    private RecipeCommentQueryService recipeCommentQueryService;

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

    private MockMvc restRecipeCommentMockMvc;

    private RecipeComment recipeComment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecipeCommentResource recipeCommentResource = new RecipeCommentResource(recipeCommentService, recipeCommentQueryService);
        this.restRecipeCommentMockMvc = MockMvcBuilders.standaloneSetup(recipeCommentResource)
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
    public static RecipeComment createEntity(EntityManager em) {
        RecipeComment recipeComment = new RecipeComment()
            .comment(DEFAULT_COMMENT)
            .date(DEFAULT_DATE);
        return recipeComment;
    }

    @Before
    public void initTest() {
        recipeComment = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecipeComment() throws Exception {
        int databaseSizeBeforeCreate = recipeCommentRepository.findAll().size();

        // Create the RecipeComment
        restRecipeCommentMockMvc.perform(post("/api/recipe-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeComment)))
            .andExpect(status().isCreated());

        // Validate the RecipeComment in the database
        List<RecipeComment> recipeCommentList = recipeCommentRepository.findAll();
        assertThat(recipeCommentList).hasSize(databaseSizeBeforeCreate + 1);
        RecipeComment testRecipeComment = recipeCommentList.get(recipeCommentList.size() - 1);
        assertThat(testRecipeComment.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testRecipeComment.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the RecipeComment in Elasticsearch
        verify(mockRecipeCommentSearchRepository, times(1)).save(testRecipeComment);
    }

    @Test
    @Transactional
    public void createRecipeCommentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recipeCommentRepository.findAll().size();

        // Create the RecipeComment with an existing ID
        recipeComment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeCommentMockMvc.perform(post("/api/recipe-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeComment)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeComment in the database
        List<RecipeComment> recipeCommentList = recipeCommentRepository.findAll();
        assertThat(recipeCommentList).hasSize(databaseSizeBeforeCreate);

        // Validate the RecipeComment in Elasticsearch
        verify(mockRecipeCommentSearchRepository, times(0)).save(recipeComment);
    }

    @Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeCommentRepository.findAll().size();
        // set the field null
        recipeComment.setComment(null);

        // Create the RecipeComment, which fails.

        restRecipeCommentMockMvc.perform(post("/api/recipe-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeComment)))
            .andExpect(status().isBadRequest());

        List<RecipeComment> recipeCommentList = recipeCommentRepository.findAll();
        assertThat(recipeCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeCommentRepository.findAll().size();
        // set the field null
        recipeComment.setDate(null);

        // Create the RecipeComment, which fails.

        restRecipeCommentMockMvc.perform(post("/api/recipe-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeComment)))
            .andExpect(status().isBadRequest());

        List<RecipeComment> recipeCommentList = recipeCommentRepository.findAll();
        assertThat(recipeCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecipeComments() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList
        restRecipeCommentMockMvc.perform(get("/api/recipe-comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRecipeComment() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get the recipeComment
        restRecipeCommentMockMvc.perform(get("/api/recipe-comments/{id}", recipeComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recipeComment.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllRecipeCommentsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList where comment equals to DEFAULT_COMMENT
        defaultRecipeCommentShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the recipeCommentList where comment equals to UPDATED_COMMENT
        defaultRecipeCommentShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllRecipeCommentsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultRecipeCommentShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the recipeCommentList where comment equals to UPDATED_COMMENT
        defaultRecipeCommentShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllRecipeCommentsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList where comment is not null
        defaultRecipeCommentShouldBeFound("comment.specified=true");

        // Get all the recipeCommentList where comment is null
        defaultRecipeCommentShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeCommentsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList where date equals to DEFAULT_DATE
        defaultRecipeCommentShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the recipeCommentList where date equals to UPDATED_DATE
        defaultRecipeCommentShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeCommentsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList where date in DEFAULT_DATE or UPDATED_DATE
        defaultRecipeCommentShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the recipeCommentList where date equals to UPDATED_DATE
        defaultRecipeCommentShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeCommentsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList where date is not null
        defaultRecipeCommentShouldBeFound("date.specified=true");

        // Get all the recipeCommentList where date is null
        defaultRecipeCommentShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllRecipeCommentsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList where date greater than or equals to DEFAULT_DATE
        defaultRecipeCommentShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the recipeCommentList where date greater than or equals to UPDATED_DATE
        defaultRecipeCommentShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRecipeCommentsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        recipeCommentRepository.saveAndFlush(recipeComment);

        // Get all the recipeCommentList where date less than or equals to DEFAULT_DATE
        defaultRecipeCommentShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the recipeCommentList where date less than or equals to UPDATED_DATE
        defaultRecipeCommentShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllRecipeCommentsByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        recipeComment.setRecipe(recipe);
        recipeCommentRepository.saveAndFlush(recipeComment);
        Long recipeId = recipe.getId();

        // Get all the recipeCommentList where recipe equals to recipeId
        defaultRecipeCommentShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the recipeCommentList where recipe equals to recipeId + 1
        defaultRecipeCommentShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllRecipeCommentsByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo userInfo = UserInfoResourceIntTest.createEntity(em);
        em.persist(userInfo);
        em.flush();
        recipeComment.setUserInfo(userInfo);
        recipeCommentRepository.saveAndFlush(recipeComment);
        Long userInfoId = userInfo.getId();

        // Get all the recipeCommentList where userInfo equals to userInfoId
        defaultRecipeCommentShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the recipeCommentList where userInfo equals to userInfoId + 1
        defaultRecipeCommentShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRecipeCommentShouldBeFound(String filter) throws Exception {
        restRecipeCommentMockMvc.perform(get("/api/recipe-comments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restRecipeCommentMockMvc.perform(get("/api/recipe-comments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRecipeCommentShouldNotBeFound(String filter) throws Exception {
        restRecipeCommentMockMvc.perform(get("/api/recipe-comments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeCommentMockMvc.perform(get("/api/recipe-comments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRecipeComment() throws Exception {
        // Get the recipeComment
        restRecipeCommentMockMvc.perform(get("/api/recipe-comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipeComment() throws Exception {
        // Initialize the database
        recipeCommentService.save(recipeComment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRecipeCommentSearchRepository);

        int databaseSizeBeforeUpdate = recipeCommentRepository.findAll().size();

        // Update the recipeComment
        RecipeComment updatedRecipeComment = recipeCommentRepository.findById(recipeComment.getId()).get();
        // Disconnect from session so that the updates on updatedRecipeComment are not directly saved in db
        em.detach(updatedRecipeComment);
        updatedRecipeComment
            .comment(UPDATED_COMMENT)
            .date(UPDATED_DATE);

        restRecipeCommentMockMvc.perform(put("/api/recipe-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecipeComment)))
            .andExpect(status().isOk());

        // Validate the RecipeComment in the database
        List<RecipeComment> recipeCommentList = recipeCommentRepository.findAll();
        assertThat(recipeCommentList).hasSize(databaseSizeBeforeUpdate);
        RecipeComment testRecipeComment = recipeCommentList.get(recipeCommentList.size() - 1);
        assertThat(testRecipeComment.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRecipeComment.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the RecipeComment in Elasticsearch
        verify(mockRecipeCommentSearchRepository, times(1)).save(testRecipeComment);
    }

    @Test
    @Transactional
    public void updateNonExistingRecipeComment() throws Exception {
        int databaseSizeBeforeUpdate = recipeCommentRepository.findAll().size();

        // Create the RecipeComment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeCommentMockMvc.perform(put("/api/recipe-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recipeComment)))
            .andExpect(status().isBadRequest());

        // Validate the RecipeComment in the database
        List<RecipeComment> recipeCommentList = recipeCommentRepository.findAll();
        assertThat(recipeCommentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RecipeComment in Elasticsearch
        verify(mockRecipeCommentSearchRepository, times(0)).save(recipeComment);
    }

    @Test
    @Transactional
    public void deleteRecipeComment() throws Exception {
        // Initialize the database
        recipeCommentService.save(recipeComment);

        int databaseSizeBeforeDelete = recipeCommentRepository.findAll().size();

        // Delete the recipeComment
        restRecipeCommentMockMvc.perform(delete("/api/recipe-comments/{id}", recipeComment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecipeComment> recipeCommentList = recipeCommentRepository.findAll();
        assertThat(recipeCommentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RecipeComment in Elasticsearch
        verify(mockRecipeCommentSearchRepository, times(1)).deleteById(recipeComment.getId());
    }

    @Test
    @Transactional
    public void searchRecipeComment() throws Exception {
        // Initialize the database
        recipeCommentService.save(recipeComment);
        when(mockRecipeCommentSearchRepository.search(queryStringQuery("id:" + recipeComment.getId())))
            .thenReturn(Collections.singletonList(recipeComment));
        // Search the recipeComment
        restRecipeCommentMockMvc.perform(get("/api/_search/recipe-comments?query=id:" + recipeComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeComment.class);
        RecipeComment recipeComment1 = new RecipeComment();
        recipeComment1.setId(1L);
        RecipeComment recipeComment2 = new RecipeComment();
        recipeComment2.setId(recipeComment1.getId());
        assertThat(recipeComment1).isEqualTo(recipeComment2);
        recipeComment2.setId(2L);
        assertThat(recipeComment1).isNotEqualTo(recipeComment2);
        recipeComment1.setId(null);
        assertThat(recipeComment1).isNotEqualTo(recipeComment2);
    }
}

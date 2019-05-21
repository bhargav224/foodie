package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.Footnote;
import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.FootnoteRepository;
import com.foodiechef.api.repository.search.FootnoteSearchRepository;
import com.foodiechef.api.service.FootnoteService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.FootnoteCriteria;
import com.foodiechef.api.service.FootnoteQueryService;

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
 * Test class for the FootnoteResource REST controller.
 *
 * @see FootnoteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class FootnoteResourceIntTest {

    private static final String DEFAULT_FOOTNOTE = "AAAAAAAAAA";
    private static final String UPDATED_FOOTNOTE = "BBBBBBBBBB";

    @Autowired
    private FootnoteRepository footnoteRepository;

    @Autowired
    private FootnoteService footnoteService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.FootnoteSearchRepositoryMockConfiguration
     */
    @Autowired
    private FootnoteSearchRepository mockFootnoteSearchRepository;

    @Autowired
    private FootnoteQueryService footnoteQueryService;

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

    private MockMvc restFootnoteMockMvc;

    private Footnote footnote;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FootnoteResource footnoteResource = new FootnoteResource(footnoteService, footnoteQueryService);
        this.restFootnoteMockMvc = MockMvcBuilders.standaloneSetup(footnoteResource)
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
    public static Footnote createEntity(EntityManager em) {
        Footnote footnote = new Footnote()
            .footnote(DEFAULT_FOOTNOTE);
        return footnote;
    }

    @Before
    public void initTest() {
        footnote = createEntity(em);
    }

    @Test
    @Transactional
    public void createFootnote() throws Exception {
        int databaseSizeBeforeCreate = footnoteRepository.findAll().size();

        // Create the Footnote
        restFootnoteMockMvc.perform(post("/api/footnotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(footnote)))
            .andExpect(status().isCreated());

        // Validate the Footnote in the database
        List<Footnote> footnoteList = footnoteRepository.findAll();
        assertThat(footnoteList).hasSize(databaseSizeBeforeCreate + 1);
        Footnote testFootnote = footnoteList.get(footnoteList.size() - 1);
        assertThat(testFootnote.getFootnote()).isEqualTo(DEFAULT_FOOTNOTE);

        // Validate the Footnote in Elasticsearch
        verify(mockFootnoteSearchRepository, times(1)).save(testFootnote);
    }

    @Test
    @Transactional
    public void createFootnoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = footnoteRepository.findAll().size();

        // Create the Footnote with an existing ID
        footnote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFootnoteMockMvc.perform(post("/api/footnotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(footnote)))
            .andExpect(status().isBadRequest());

        // Validate the Footnote in the database
        List<Footnote> footnoteList = footnoteRepository.findAll();
        assertThat(footnoteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Footnote in Elasticsearch
        verify(mockFootnoteSearchRepository, times(0)).save(footnote);
    }

    @Test
    @Transactional
    public void checkFootnoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = footnoteRepository.findAll().size();
        // set the field null
        footnote.setFootnote(null);

        // Create the Footnote, which fails.

        restFootnoteMockMvc.perform(post("/api/footnotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(footnote)))
            .andExpect(status().isBadRequest());

        List<Footnote> footnoteList = footnoteRepository.findAll();
        assertThat(footnoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFootnotes() throws Exception {
        // Initialize the database
        footnoteRepository.saveAndFlush(footnote);

        // Get all the footnoteList
        restFootnoteMockMvc.perform(get("/api/footnotes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(footnote.getId().intValue())))
            .andExpect(jsonPath("$.[*].footnote").value(hasItem(DEFAULT_FOOTNOTE.toString())));
    }
    
    @Test
    @Transactional
    public void getFootnote() throws Exception {
        // Initialize the database
        footnoteRepository.saveAndFlush(footnote);

        // Get the footnote
        restFootnoteMockMvc.perform(get("/api/footnotes/{id}", footnote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(footnote.getId().intValue()))
            .andExpect(jsonPath("$.footnote").value(DEFAULT_FOOTNOTE.toString()));
    }

    @Test
    @Transactional
    public void getAllFootnotesByFootnoteIsEqualToSomething() throws Exception {
        // Initialize the database
        footnoteRepository.saveAndFlush(footnote);

        // Get all the footnoteList where footnote equals to DEFAULT_FOOTNOTE
        defaultFootnoteShouldBeFound("footnote.equals=" + DEFAULT_FOOTNOTE);

        // Get all the footnoteList where footnote equals to UPDATED_FOOTNOTE
        defaultFootnoteShouldNotBeFound("footnote.equals=" + UPDATED_FOOTNOTE);
    }

    @Test
    @Transactional
    public void getAllFootnotesByFootnoteIsInShouldWork() throws Exception {
        // Initialize the database
        footnoteRepository.saveAndFlush(footnote);

        // Get all the footnoteList where footnote in DEFAULT_FOOTNOTE or UPDATED_FOOTNOTE
        defaultFootnoteShouldBeFound("footnote.in=" + DEFAULT_FOOTNOTE + "," + UPDATED_FOOTNOTE);

        // Get all the footnoteList where footnote equals to UPDATED_FOOTNOTE
        defaultFootnoteShouldNotBeFound("footnote.in=" + UPDATED_FOOTNOTE);
    }

    @Test
    @Transactional
    public void getAllFootnotesByFootnoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        footnoteRepository.saveAndFlush(footnote);

        // Get all the footnoteList where footnote is not null
        defaultFootnoteShouldBeFound("footnote.specified=true");

        // Get all the footnoteList where footnote is null
        defaultFootnoteShouldNotBeFound("footnote.specified=false");
    }

    @Test
    @Transactional
    public void getAllFootnotesByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        Recipe recipe = RecipeResourceIntTest.createEntity(em);
        em.persist(recipe);
        em.flush();
        footnote.setRecipe(recipe);
        footnoteRepository.saveAndFlush(footnote);
        Long recipeId = recipe.getId();

        // Get all the footnoteList where recipe equals to recipeId
        defaultFootnoteShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the footnoteList where recipe equals to recipeId + 1
        defaultFootnoteShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }


    @Test
    @Transactional
    public void getAllFootnotesByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo userInfo = UserInfoResourceIntTest.createEntity(em);
        em.persist(userInfo);
        em.flush();
        footnote.setUserInfo(userInfo);
        footnoteRepository.saveAndFlush(footnote);
        Long userInfoId = userInfo.getId();

        // Get all the footnoteList where userInfo equals to userInfoId
        defaultFootnoteShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the footnoteList where userInfo equals to userInfoId + 1
        defaultFootnoteShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFootnoteShouldBeFound(String filter) throws Exception {
        restFootnoteMockMvc.perform(get("/api/footnotes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(footnote.getId().intValue())))
            .andExpect(jsonPath("$.[*].footnote").value(hasItem(DEFAULT_FOOTNOTE)));

        // Check, that the count call also returns 1
        restFootnoteMockMvc.perform(get("/api/footnotes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFootnoteShouldNotBeFound(String filter) throws Exception {
        restFootnoteMockMvc.perform(get("/api/footnotes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFootnoteMockMvc.perform(get("/api/footnotes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFootnote() throws Exception {
        // Get the footnote
        restFootnoteMockMvc.perform(get("/api/footnotes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFootnote() throws Exception {
        // Initialize the database
        footnoteService.save(footnote);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFootnoteSearchRepository);

        int databaseSizeBeforeUpdate = footnoteRepository.findAll().size();

        // Update the footnote
        Footnote updatedFootnote = footnoteRepository.findById(footnote.getId()).get();
        // Disconnect from session so that the updates on updatedFootnote are not directly saved in db
        em.detach(updatedFootnote);
        updatedFootnote
            .footnote(UPDATED_FOOTNOTE);

        restFootnoteMockMvc.perform(put("/api/footnotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFootnote)))
            .andExpect(status().isOk());

        // Validate the Footnote in the database
        List<Footnote> footnoteList = footnoteRepository.findAll();
        assertThat(footnoteList).hasSize(databaseSizeBeforeUpdate);
        Footnote testFootnote = footnoteList.get(footnoteList.size() - 1);
        assertThat(testFootnote.getFootnote()).isEqualTo(UPDATED_FOOTNOTE);

        // Validate the Footnote in Elasticsearch
        verify(mockFootnoteSearchRepository, times(1)).save(testFootnote);
    }

    @Test
    @Transactional
    public void updateNonExistingFootnote() throws Exception {
        int databaseSizeBeforeUpdate = footnoteRepository.findAll().size();

        // Create the Footnote

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFootnoteMockMvc.perform(put("/api/footnotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(footnote)))
            .andExpect(status().isBadRequest());

        // Validate the Footnote in the database
        List<Footnote> footnoteList = footnoteRepository.findAll();
        assertThat(footnoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Footnote in Elasticsearch
        verify(mockFootnoteSearchRepository, times(0)).save(footnote);
    }

    @Test
    @Transactional
    public void deleteFootnote() throws Exception {
        // Initialize the database
        footnoteService.save(footnote);

        int databaseSizeBeforeDelete = footnoteRepository.findAll().size();

        // Delete the footnote
        restFootnoteMockMvc.perform(delete("/api/footnotes/{id}", footnote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Footnote> footnoteList = footnoteRepository.findAll();
        assertThat(footnoteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Footnote in Elasticsearch
        verify(mockFootnoteSearchRepository, times(1)).deleteById(footnote.getId());
    }

    @Test
    @Transactional
    public void searchFootnote() throws Exception {
        // Initialize the database
        footnoteService.save(footnote);
        when(mockFootnoteSearchRepository.search(queryStringQuery("id:" + footnote.getId())))
            .thenReturn(Collections.singletonList(footnote));
        // Search the footnote
        restFootnoteMockMvc.perform(get("/api/_search/footnotes?query=id:" + footnote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(footnote.getId().intValue())))
            .andExpect(jsonPath("$.[*].footnote").value(hasItem(DEFAULT_FOOTNOTE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Footnote.class);
        Footnote footnote1 = new Footnote();
        footnote1.setId(1L);
        Footnote footnote2 = new Footnote();
        footnote2.setId(footnote1.getId());
        assertThat(footnote1).isEqualTo(footnote2);
        footnote2.setId(2L);
        assertThat(footnote1).isNotEqualTo(footnote2);
        footnote1.setId(null);
        assertThat(footnote1).isNotEqualTo(footnote2);
    }
}

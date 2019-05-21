package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.UserRating;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.UserRatingRepository;
import com.foodiechef.api.repository.search.UserRatingSearchRepository;
import com.foodiechef.api.service.UserRatingService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.UserRatingCriteria;
import com.foodiechef.api.service.UserRatingQueryService;

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
 * Test class for the UserRatingResource REST controller.
 *
 * @see UserRatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class UserRatingResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Autowired
    private UserRatingService userRatingService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.UserRatingSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserRatingSearchRepository mockUserRatingSearchRepository;

    @Autowired
    private UserRatingQueryService userRatingQueryService;

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

    private MockMvc restUserRatingMockMvc;

    private UserRating userRating;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserRatingResource userRatingResource = new UserRatingResource(userRatingService, userRatingQueryService);
        this.restUserRatingMockMvc = MockMvcBuilders.standaloneSetup(userRatingResource)
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
    public static UserRating createEntity(EntityManager em) {
        UserRating userRating = new UserRating()
            .date(DEFAULT_DATE)
            .rating(DEFAULT_RATING);
        return userRating;
    }

    @Before
    public void initTest() {
        userRating = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserRating() throws Exception {
        int databaseSizeBeforeCreate = userRatingRepository.findAll().size();

        // Create the UserRating
        restUserRatingMockMvc.perform(post("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRating)))
            .andExpect(status().isCreated());

        // Validate the UserRating in the database
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeCreate + 1);
        UserRating testUserRating = userRatingList.get(userRatingList.size() - 1);
        assertThat(testUserRating.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testUserRating.getRating()).isEqualTo(DEFAULT_RATING);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(1)).save(testUserRating);
    }

    @Test
    @Transactional
    public void createUserRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRatingRepository.findAll().size();

        // Create the UserRating with an existing ID
        userRating.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRatingMockMvc.perform(post("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRating)))
            .andExpect(status().isBadRequest());

        // Validate the UserRating in the database
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(0)).save(userRating);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRatingRepository.findAll().size();
        // set the field null
        userRating.setDate(null);

        // Create the UserRating, which fails.

        restUserRatingMockMvc.perform(post("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRating)))
            .andExpect(status().isBadRequest());

        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRatingRepository.findAll().size();
        // set the field null
        userRating.setRating(null);

        // Create the UserRating, which fails.

        restUserRatingMockMvc.perform(post("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRating)))
            .andExpect(status().isBadRequest());

        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserRatings() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList
        restUserRatingMockMvc.perform(get("/api/user-ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }
    
    @Test
    @Transactional
    public void getUserRating() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get the userRating
        restUserRatingMockMvc.perform(get("/api/user-ratings/{id}", userRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userRating.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING));
    }

    @Test
    @Transactional
    public void getAllUserRatingsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where date equals to DEFAULT_DATE
        defaultUserRatingShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the userRatingList where date equals to UPDATED_DATE
        defaultUserRatingShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserRatingsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where date in DEFAULT_DATE or UPDATED_DATE
        defaultUserRatingShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the userRatingList where date equals to UPDATED_DATE
        defaultUserRatingShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserRatingsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where date is not null
        defaultUserRatingShouldBeFound("date.specified=true");

        // Get all the userRatingList where date is null
        defaultUserRatingShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserRatingsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where date greater than or equals to DEFAULT_DATE
        defaultUserRatingShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the userRatingList where date greater than or equals to UPDATED_DATE
        defaultUserRatingShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserRatingsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where date less than or equals to DEFAULT_DATE
        defaultUserRatingShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the userRatingList where date less than or equals to UPDATED_DATE
        defaultUserRatingShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllUserRatingsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where rating equals to DEFAULT_RATING
        defaultUserRatingShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the userRatingList where rating equals to UPDATED_RATING
        defaultUserRatingShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllUserRatingsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultUserRatingShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the userRatingList where rating equals to UPDATED_RATING
        defaultUserRatingShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllUserRatingsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where rating is not null
        defaultUserRatingShouldBeFound("rating.specified=true");

        // Get all the userRatingList where rating is null
        defaultUserRatingShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserRatingsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where rating greater than or equals to DEFAULT_RATING
        defaultUserRatingShouldBeFound("rating.greaterOrEqualThan=" + DEFAULT_RATING);

        // Get all the userRatingList where rating greater than or equals to UPDATED_RATING
        defaultUserRatingShouldNotBeFound("rating.greaterOrEqualThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllUserRatingsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList where rating less than or equals to DEFAULT_RATING
        defaultUserRatingShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the userRatingList where rating less than or equals to UPDATED_RATING
        defaultUserRatingShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }


    @Test
    @Transactional
    public void getAllUserRatingsByRateByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo rateByUser = UserInfoResourceIntTest.createEntity(em);
        em.persist(rateByUser);
        em.flush();
        userRating.setRateByUser(rateByUser);
        userRatingRepository.saveAndFlush(userRating);
        Long rateByUserId = rateByUser.getId();

        // Get all the userRatingList where rateByUser equals to rateByUserId
        defaultUserRatingShouldBeFound("rateByUserId.equals=" + rateByUserId);

        // Get all the userRatingList where rateByUser equals to rateByUserId + 1
        defaultUserRatingShouldNotBeFound("rateByUserId.equals=" + (rateByUserId + 1));
    }


    @Test
    @Transactional
    public void getAllUserRatingsByRateToUserIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo rateToUser = UserInfoResourceIntTest.createEntity(em);
        em.persist(rateToUser);
        em.flush();
        userRating.setRateToUser(rateToUser);
        userRatingRepository.saveAndFlush(userRating);
        Long rateToUserId = rateToUser.getId();

        // Get all the userRatingList where rateToUser equals to rateToUserId
        defaultUserRatingShouldBeFound("rateToUserId.equals=" + rateToUserId);

        // Get all the userRatingList where rateToUser equals to rateToUserId + 1
        defaultUserRatingShouldNotBeFound("rateToUserId.equals=" + (rateToUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultUserRatingShouldBeFound(String filter) throws Exception {
        restUserRatingMockMvc.perform(get("/api/user-ratings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));

        // Check, that the count call also returns 1
        restUserRatingMockMvc.perform(get("/api/user-ratings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultUserRatingShouldNotBeFound(String filter) throws Exception {
        restUserRatingMockMvc.perform(get("/api/user-ratings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserRatingMockMvc.perform(get("/api/user-ratings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserRating() throws Exception {
        // Get the userRating
        restUserRatingMockMvc.perform(get("/api/user-ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserRating() throws Exception {
        // Initialize the database
        userRatingService.save(userRating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockUserRatingSearchRepository);

        int databaseSizeBeforeUpdate = userRatingRepository.findAll().size();

        // Update the userRating
        UserRating updatedUserRating = userRatingRepository.findById(userRating.getId()).get();
        // Disconnect from session so that the updates on updatedUserRating are not directly saved in db
        em.detach(updatedUserRating);
        updatedUserRating
            .date(UPDATED_DATE)
            .rating(UPDATED_RATING);

        restUserRatingMockMvc.perform(put("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserRating)))
            .andExpect(status().isOk());

        // Validate the UserRating in the database
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeUpdate);
        UserRating testUserRating = userRatingList.get(userRatingList.size() - 1);
        assertThat(testUserRating.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserRating.getRating()).isEqualTo(UPDATED_RATING);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(1)).save(testUserRating);
    }

    @Test
    @Transactional
    public void updateNonExistingUserRating() throws Exception {
        int databaseSizeBeforeUpdate = userRatingRepository.findAll().size();

        // Create the UserRating

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRatingMockMvc.perform(put("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRating)))
            .andExpect(status().isBadRequest());

        // Validate the UserRating in the database
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(0)).save(userRating);
    }

    @Test
    @Transactional
    public void deleteUserRating() throws Exception {
        // Initialize the database
        userRatingService.save(userRating);

        int databaseSizeBeforeDelete = userRatingRepository.findAll().size();

        // Delete the userRating
        restUserRatingMockMvc.perform(delete("/api/user-ratings/{id}", userRating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(1)).deleteById(userRating.getId());
    }

    @Test
    @Transactional
    public void searchUserRating() throws Exception {
        // Initialize the database
        userRatingService.save(userRating);
        when(mockUserRatingSearchRepository.search(queryStringQuery("id:" + userRating.getId())))
            .thenReturn(Collections.singletonList(userRating));
        // Search the userRating
        restUserRatingMockMvc.perform(get("/api/_search/user-ratings?query=id:" + userRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRating.class);
        UserRating userRating1 = new UserRating();
        userRating1.setId(1L);
        UserRating userRating2 = new UserRating();
        userRating2.setId(userRating1.getId());
        assertThat(userRating1).isEqualTo(userRating2);
        userRating2.setId(2L);
        assertThat(userRating1).isNotEqualTo(userRating2);
        userRating1.setId(null);
        assertThat(userRating1).isNotEqualTo(userRating2);
    }
}

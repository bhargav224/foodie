package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.domain.ChefProfile;
import com.foodiechef.api.domain.UserInfoRole;
import com.foodiechef.api.domain.RecipeComment;
import com.foodiechef.api.domain.RecipeRating;
import com.foodiechef.api.domain.RecipeLike;
import com.foodiechef.api.domain.Footnote;
import com.foodiechef.api.domain.ShareRecipe;
import com.foodiechef.api.domain.InviteEmail;
import com.foodiechef.api.domain.InviteContact;
import com.foodiechef.api.domain.UserRating;
import com.foodiechef.api.domain.Restaurant;
import com.foodiechef.api.repository.UserInfoRepository;
import com.foodiechef.api.repository.search.UserInfoSearchRepository;
import com.foodiechef.api.service.UserInfoService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.UserInfoCriteria;
import com.foodiechef.api.service.UserInfoQueryService;

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
 * Test class for the UserInfoResource REST controller.
 *
 * @see UserInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class UserInfoResourceIntTest {

    private static final Boolean DEFAULT_AUTHENTICATED = false;
    private static final Boolean UPDATED_AUTHENTICATED = true;

    private static final Long DEFAULT_CONTACT = 1L;
    private static final Long UPDATED_CONTACT = 2L;

    private static final LocalDate DEFAULT_CURRENT_LOGGED_IN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CURRENT_LOGGED_IN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EMAIL_CONFIRMATION_SENT_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMAIL_CONFIRMATION_SENT_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_EMAIL_CONFIRMED = false;
    private static final Boolean UPDATED_EMAIL_CONFIRMED = true;

    private static final LocalDate DEFAULT_EMAIL_CONFIRMED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMAIL_CONFIRMED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_LOGGED_IN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_LOGGED_IN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REGISTERED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTERED_ON = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.UserInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserInfoSearchRepository mockUserInfoSearchRepository;

    @Autowired
    private UserInfoQueryService userInfoQueryService;

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

    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserInfoResource userInfoResource = new UserInfoResource(userInfoService, userInfoQueryService);
        this.restUserInfoMockMvc = MockMvcBuilders.standaloneSetup(userInfoResource)
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
    public static UserInfo createEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .authenticated(DEFAULT_AUTHENTICATED)
            .contact(DEFAULT_CONTACT)
            .currentLoggedIn(DEFAULT_CURRENT_LOGGED_IN)
            .email(DEFAULT_EMAIL)
            .emailConfirmationSentOn(DEFAULT_EMAIL_CONFIRMATION_SENT_ON)
            .emailConfirmed(DEFAULT_EMAIL_CONFIRMED)
            .emailConfirmedOn(DEFAULT_EMAIL_CONFIRMED_ON)
            .lastLoggedIn(DEFAULT_LAST_LOGGED_IN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .password(DEFAULT_PASSWORD)
            .photo(DEFAULT_PHOTO)
            .registeredOn(DEFAULT_REGISTERED_ON);
        return userInfo;
    }

    @Before
    public void initTest() {
        userInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserInfo() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo
        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate + 1);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.isAuthenticated()).isEqualTo(DEFAULT_AUTHENTICATED);
        assertThat(testUserInfo.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testUserInfo.getCurrentLoggedIn()).isEqualTo(DEFAULT_CURRENT_LOGGED_IN);
        assertThat(testUserInfo.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserInfo.getEmailConfirmationSentOn()).isEqualTo(DEFAULT_EMAIL_CONFIRMATION_SENT_ON);
        assertThat(testUserInfo.isEmailConfirmed()).isEqualTo(DEFAULT_EMAIL_CONFIRMED);
        assertThat(testUserInfo.getEmailConfirmedOn()).isEqualTo(DEFAULT_EMAIL_CONFIRMED_ON);
        assertThat(testUserInfo.getLastLoggedIn()).isEqualTo(DEFAULT_LAST_LOGGED_IN);
        assertThat(testUserInfo.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserInfo.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserInfo.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testUserInfo.getRegisteredOn()).isEqualTo(DEFAULT_REGISTERED_ON);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(1)).save(testUserInfo);
    }

    @Test
    @Transactional
    public void createUserInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo with an existing ID
        userInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(0)).save(userInfo);
    }

    @Test
    @Transactional
    public void checkAuthenticatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setAuthenticated(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setContact(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentLoggedInIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setCurrentLoggedIn(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setEmail(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailConfirmationSentOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setEmailConfirmationSentOn(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailConfirmedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setEmailConfirmed(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailConfirmedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setEmailConfirmedOn(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastLoggedInIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setLastLoggedIn(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setFirstName(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setLastName(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setPassword(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhotoIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setPhoto(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegisteredOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setRegisteredOn(null);

        // Create the UserInfo, which fails.

        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserInfos() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].authenticated").value(hasItem(DEFAULT_AUTHENTICATED.booleanValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.intValue())))
            .andExpect(jsonPath("$.[*].currentLoggedIn").value(hasItem(DEFAULT_CURRENT_LOGGED_IN.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].emailConfirmationSentOn").value(hasItem(DEFAULT_EMAIL_CONFIRMATION_SENT_ON.toString())))
            .andExpect(jsonPath("$.[*].emailConfirmed").value(hasItem(DEFAULT_EMAIL_CONFIRMED.booleanValue())))
            .andExpect(jsonPath("$.[*].emailConfirmedOn").value(hasItem(DEFAULT_EMAIL_CONFIRMED_ON.toString())))
            .andExpect(jsonPath("$.[*].lastLoggedIn").value(hasItem(DEFAULT_LAST_LOGGED_IN.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.toString())))
            .andExpect(jsonPath("$.[*].registeredOn").value(hasItem(DEFAULT_REGISTERED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.authenticated").value(DEFAULT_AUTHENTICATED.booleanValue()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.intValue()))
            .andExpect(jsonPath("$.currentLoggedIn").value(DEFAULT_CURRENT_LOGGED_IN.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.emailConfirmationSentOn").value(DEFAULT_EMAIL_CONFIRMATION_SENT_ON.toString()))
            .andExpect(jsonPath("$.emailConfirmed").value(DEFAULT_EMAIL_CONFIRMED.booleanValue()))
            .andExpect(jsonPath("$.emailConfirmedOn").value(DEFAULT_EMAIL_CONFIRMED_ON.toString()))
            .andExpect(jsonPath("$.lastLoggedIn").value(DEFAULT_LAST_LOGGED_IN.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO.toString()))
            .andExpect(jsonPath("$.registeredOn").value(DEFAULT_REGISTERED_ON.toString()));
    }

    @Test
    @Transactional
    public void getAllUserInfosByAuthenticatedIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where authenticated equals to DEFAULT_AUTHENTICATED
        defaultUserInfoShouldBeFound("authenticated.equals=" + DEFAULT_AUTHENTICATED);

        // Get all the userInfoList where authenticated equals to UPDATED_AUTHENTICATED
        defaultUserInfoShouldNotBeFound("authenticated.equals=" + UPDATED_AUTHENTICATED);
    }

    @Test
    @Transactional
    public void getAllUserInfosByAuthenticatedIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where authenticated in DEFAULT_AUTHENTICATED or UPDATED_AUTHENTICATED
        defaultUserInfoShouldBeFound("authenticated.in=" + DEFAULT_AUTHENTICATED + "," + UPDATED_AUTHENTICATED);

        // Get all the userInfoList where authenticated equals to UPDATED_AUTHENTICATED
        defaultUserInfoShouldNotBeFound("authenticated.in=" + UPDATED_AUTHENTICATED);
    }

    @Test
    @Transactional
    public void getAllUserInfosByAuthenticatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where authenticated is not null
        defaultUserInfoShouldBeFound("authenticated.specified=true");

        // Get all the userInfoList where authenticated is null
        defaultUserInfoShouldNotBeFound("authenticated.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where contact equals to DEFAULT_CONTACT
        defaultUserInfoShouldBeFound("contact.equals=" + DEFAULT_CONTACT);

        // Get all the userInfoList where contact equals to UPDATED_CONTACT
        defaultUserInfoShouldNotBeFound("contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllUserInfosByContactIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where contact in DEFAULT_CONTACT or UPDATED_CONTACT
        defaultUserInfoShouldBeFound("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT);

        // Get all the userInfoList where contact equals to UPDATED_CONTACT
        defaultUserInfoShouldNotBeFound("contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllUserInfosByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where contact is not null
        defaultUserInfoShouldBeFound("contact.specified=true");

        // Get all the userInfoList where contact is null
        defaultUserInfoShouldNotBeFound("contact.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByContactIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where contact greater than or equals to DEFAULT_CONTACT
        defaultUserInfoShouldBeFound("contact.greaterOrEqualThan=" + DEFAULT_CONTACT);

        // Get all the userInfoList where contact greater than or equals to UPDATED_CONTACT
        defaultUserInfoShouldNotBeFound("contact.greaterOrEqualThan=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllUserInfosByContactIsLessThanSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where contact less than or equals to DEFAULT_CONTACT
        defaultUserInfoShouldNotBeFound("contact.lessThan=" + DEFAULT_CONTACT);

        // Get all the userInfoList where contact less than or equals to UPDATED_CONTACT
        defaultUserInfoShouldBeFound("contact.lessThan=" + UPDATED_CONTACT);
    }


    @Test
    @Transactional
    public void getAllUserInfosByCurrentLoggedInIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where currentLoggedIn equals to DEFAULT_CURRENT_LOGGED_IN
        defaultUserInfoShouldBeFound("currentLoggedIn.equals=" + DEFAULT_CURRENT_LOGGED_IN);

        // Get all the userInfoList where currentLoggedIn equals to UPDATED_CURRENT_LOGGED_IN
        defaultUserInfoShouldNotBeFound("currentLoggedIn.equals=" + UPDATED_CURRENT_LOGGED_IN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByCurrentLoggedInIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where currentLoggedIn in DEFAULT_CURRENT_LOGGED_IN or UPDATED_CURRENT_LOGGED_IN
        defaultUserInfoShouldBeFound("currentLoggedIn.in=" + DEFAULT_CURRENT_LOGGED_IN + "," + UPDATED_CURRENT_LOGGED_IN);

        // Get all the userInfoList where currentLoggedIn equals to UPDATED_CURRENT_LOGGED_IN
        defaultUserInfoShouldNotBeFound("currentLoggedIn.in=" + UPDATED_CURRENT_LOGGED_IN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByCurrentLoggedInIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where currentLoggedIn is not null
        defaultUserInfoShouldBeFound("currentLoggedIn.specified=true");

        // Get all the userInfoList where currentLoggedIn is null
        defaultUserInfoShouldNotBeFound("currentLoggedIn.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByCurrentLoggedInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where currentLoggedIn greater than or equals to DEFAULT_CURRENT_LOGGED_IN
        defaultUserInfoShouldBeFound("currentLoggedIn.greaterOrEqualThan=" + DEFAULT_CURRENT_LOGGED_IN);

        // Get all the userInfoList where currentLoggedIn greater than or equals to UPDATED_CURRENT_LOGGED_IN
        defaultUserInfoShouldNotBeFound("currentLoggedIn.greaterOrEqualThan=" + UPDATED_CURRENT_LOGGED_IN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByCurrentLoggedInIsLessThanSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where currentLoggedIn less than or equals to DEFAULT_CURRENT_LOGGED_IN
        defaultUserInfoShouldNotBeFound("currentLoggedIn.lessThan=" + DEFAULT_CURRENT_LOGGED_IN);

        // Get all the userInfoList where currentLoggedIn less than or equals to UPDATED_CURRENT_LOGGED_IN
        defaultUserInfoShouldBeFound("currentLoggedIn.lessThan=" + UPDATED_CURRENT_LOGGED_IN);
    }


    @Test
    @Transactional
    public void getAllUserInfosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where email equals to DEFAULT_EMAIL
        defaultUserInfoShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the userInfoList where email equals to UPDATED_EMAIL
        defaultUserInfoShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUserInfoShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the userInfoList where email equals to UPDATED_EMAIL
        defaultUserInfoShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where email is not null
        defaultUserInfoShouldBeFound("email.specified=true");

        // Get all the userInfoList where email is null
        defaultUserInfoShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmationSentOnIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmationSentOn equals to DEFAULT_EMAIL_CONFIRMATION_SENT_ON
        defaultUserInfoShouldBeFound("emailConfirmationSentOn.equals=" + DEFAULT_EMAIL_CONFIRMATION_SENT_ON);

        // Get all the userInfoList where emailConfirmationSentOn equals to UPDATED_EMAIL_CONFIRMATION_SENT_ON
        defaultUserInfoShouldNotBeFound("emailConfirmationSentOn.equals=" + UPDATED_EMAIL_CONFIRMATION_SENT_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmationSentOnIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmationSentOn in DEFAULT_EMAIL_CONFIRMATION_SENT_ON or UPDATED_EMAIL_CONFIRMATION_SENT_ON
        defaultUserInfoShouldBeFound("emailConfirmationSentOn.in=" + DEFAULT_EMAIL_CONFIRMATION_SENT_ON + "," + UPDATED_EMAIL_CONFIRMATION_SENT_ON);

        // Get all the userInfoList where emailConfirmationSentOn equals to UPDATED_EMAIL_CONFIRMATION_SENT_ON
        defaultUserInfoShouldNotBeFound("emailConfirmationSentOn.in=" + UPDATED_EMAIL_CONFIRMATION_SENT_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmationSentOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmationSentOn is not null
        defaultUserInfoShouldBeFound("emailConfirmationSentOn.specified=true");

        // Get all the userInfoList where emailConfirmationSentOn is null
        defaultUserInfoShouldNotBeFound("emailConfirmationSentOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmationSentOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmationSentOn greater than or equals to DEFAULT_EMAIL_CONFIRMATION_SENT_ON
        defaultUserInfoShouldBeFound("emailConfirmationSentOn.greaterOrEqualThan=" + DEFAULT_EMAIL_CONFIRMATION_SENT_ON);

        // Get all the userInfoList where emailConfirmationSentOn greater than or equals to UPDATED_EMAIL_CONFIRMATION_SENT_ON
        defaultUserInfoShouldNotBeFound("emailConfirmationSentOn.greaterOrEqualThan=" + UPDATED_EMAIL_CONFIRMATION_SENT_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmationSentOnIsLessThanSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmationSentOn less than or equals to DEFAULT_EMAIL_CONFIRMATION_SENT_ON
        defaultUserInfoShouldNotBeFound("emailConfirmationSentOn.lessThan=" + DEFAULT_EMAIL_CONFIRMATION_SENT_ON);

        // Get all the userInfoList where emailConfirmationSentOn less than or equals to UPDATED_EMAIL_CONFIRMATION_SENT_ON
        defaultUserInfoShouldBeFound("emailConfirmationSentOn.lessThan=" + UPDATED_EMAIL_CONFIRMATION_SENT_ON);
    }


    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmedIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmed equals to DEFAULT_EMAIL_CONFIRMED
        defaultUserInfoShouldBeFound("emailConfirmed.equals=" + DEFAULT_EMAIL_CONFIRMED);

        // Get all the userInfoList where emailConfirmed equals to UPDATED_EMAIL_CONFIRMED
        defaultUserInfoShouldNotBeFound("emailConfirmed.equals=" + UPDATED_EMAIL_CONFIRMED);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmedIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmed in DEFAULT_EMAIL_CONFIRMED or UPDATED_EMAIL_CONFIRMED
        defaultUserInfoShouldBeFound("emailConfirmed.in=" + DEFAULT_EMAIL_CONFIRMED + "," + UPDATED_EMAIL_CONFIRMED);

        // Get all the userInfoList where emailConfirmed equals to UPDATED_EMAIL_CONFIRMED
        defaultUserInfoShouldNotBeFound("emailConfirmed.in=" + UPDATED_EMAIL_CONFIRMED);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmedIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmed is not null
        defaultUserInfoShouldBeFound("emailConfirmed.specified=true");

        // Get all the userInfoList where emailConfirmed is null
        defaultUserInfoShouldNotBeFound("emailConfirmed.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmedOn equals to DEFAULT_EMAIL_CONFIRMED_ON
        defaultUserInfoShouldBeFound("emailConfirmedOn.equals=" + DEFAULT_EMAIL_CONFIRMED_ON);

        // Get all the userInfoList where emailConfirmedOn equals to UPDATED_EMAIL_CONFIRMED_ON
        defaultUserInfoShouldNotBeFound("emailConfirmedOn.equals=" + UPDATED_EMAIL_CONFIRMED_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmedOnIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmedOn in DEFAULT_EMAIL_CONFIRMED_ON or UPDATED_EMAIL_CONFIRMED_ON
        defaultUserInfoShouldBeFound("emailConfirmedOn.in=" + DEFAULT_EMAIL_CONFIRMED_ON + "," + UPDATED_EMAIL_CONFIRMED_ON);

        // Get all the userInfoList where emailConfirmedOn equals to UPDATED_EMAIL_CONFIRMED_ON
        defaultUserInfoShouldNotBeFound("emailConfirmedOn.in=" + UPDATED_EMAIL_CONFIRMED_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmedOn is not null
        defaultUserInfoShouldBeFound("emailConfirmedOn.specified=true");

        // Get all the userInfoList where emailConfirmedOn is null
        defaultUserInfoShouldNotBeFound("emailConfirmedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmedOn greater than or equals to DEFAULT_EMAIL_CONFIRMED_ON
        defaultUserInfoShouldBeFound("emailConfirmedOn.greaterOrEqualThan=" + DEFAULT_EMAIL_CONFIRMED_ON);

        // Get all the userInfoList where emailConfirmedOn greater than or equals to UPDATED_EMAIL_CONFIRMED_ON
        defaultUserInfoShouldNotBeFound("emailConfirmedOn.greaterOrEqualThan=" + UPDATED_EMAIL_CONFIRMED_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByEmailConfirmedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where emailConfirmedOn less than or equals to DEFAULT_EMAIL_CONFIRMED_ON
        defaultUserInfoShouldNotBeFound("emailConfirmedOn.lessThan=" + DEFAULT_EMAIL_CONFIRMED_ON);

        // Get all the userInfoList where emailConfirmedOn less than or equals to UPDATED_EMAIL_CONFIRMED_ON
        defaultUserInfoShouldBeFound("emailConfirmedOn.lessThan=" + UPDATED_EMAIL_CONFIRMED_ON);
    }


    @Test
    @Transactional
    public void getAllUserInfosByLastLoggedInIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where lastLoggedIn equals to DEFAULT_LAST_LOGGED_IN
        defaultUserInfoShouldBeFound("lastLoggedIn.equals=" + DEFAULT_LAST_LOGGED_IN);

        // Get all the userInfoList where lastLoggedIn equals to UPDATED_LAST_LOGGED_IN
        defaultUserInfoShouldNotBeFound("lastLoggedIn.equals=" + UPDATED_LAST_LOGGED_IN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByLastLoggedInIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where lastLoggedIn in DEFAULT_LAST_LOGGED_IN or UPDATED_LAST_LOGGED_IN
        defaultUserInfoShouldBeFound("lastLoggedIn.in=" + DEFAULT_LAST_LOGGED_IN + "," + UPDATED_LAST_LOGGED_IN);

        // Get all the userInfoList where lastLoggedIn equals to UPDATED_LAST_LOGGED_IN
        defaultUserInfoShouldNotBeFound("lastLoggedIn.in=" + UPDATED_LAST_LOGGED_IN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByLastLoggedInIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where lastLoggedIn is not null
        defaultUserInfoShouldBeFound("lastLoggedIn.specified=true");

        // Get all the userInfoList where lastLoggedIn is null
        defaultUserInfoShouldNotBeFound("lastLoggedIn.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByLastLoggedInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where lastLoggedIn greater than or equals to DEFAULT_LAST_LOGGED_IN
        defaultUserInfoShouldBeFound("lastLoggedIn.greaterOrEqualThan=" + DEFAULT_LAST_LOGGED_IN);

        // Get all the userInfoList where lastLoggedIn greater than or equals to UPDATED_LAST_LOGGED_IN
        defaultUserInfoShouldNotBeFound("lastLoggedIn.greaterOrEqualThan=" + UPDATED_LAST_LOGGED_IN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByLastLoggedInIsLessThanSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where lastLoggedIn less than or equals to DEFAULT_LAST_LOGGED_IN
        defaultUserInfoShouldNotBeFound("lastLoggedIn.lessThan=" + DEFAULT_LAST_LOGGED_IN);

        // Get all the userInfoList where lastLoggedIn less than or equals to UPDATED_LAST_LOGGED_IN
        defaultUserInfoShouldBeFound("lastLoggedIn.lessThan=" + UPDATED_LAST_LOGGED_IN);
    }


    @Test
    @Transactional
    public void getAllUserInfosByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where firstName equals to DEFAULT_FIRST_NAME
        defaultUserInfoShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the userInfoList where firstName equals to UPDATED_FIRST_NAME
        defaultUserInfoShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserInfosByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultUserInfoShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the userInfoList where firstName equals to UPDATED_FIRST_NAME
        defaultUserInfoShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserInfosByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where firstName is not null
        defaultUserInfoShouldBeFound("firstName.specified=true");

        // Get all the userInfoList where firstName is null
        defaultUserInfoShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where lastName equals to DEFAULT_LAST_NAME
        defaultUserInfoShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the userInfoList where lastName equals to UPDATED_LAST_NAME
        defaultUserInfoShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserInfosByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUserInfoShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the userInfoList where lastName equals to UPDATED_LAST_NAME
        defaultUserInfoShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserInfosByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where lastName is not null
        defaultUserInfoShouldBeFound("lastName.specified=true");

        // Get all the userInfoList where lastName is null
        defaultUserInfoShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where password equals to DEFAULT_PASSWORD
        defaultUserInfoShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the userInfoList where password equals to UPDATED_PASSWORD
        defaultUserInfoShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultUserInfoShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the userInfoList where password equals to UPDATED_PASSWORD
        defaultUserInfoShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where password is not null
        defaultUserInfoShouldBeFound("password.specified=true");

        // Get all the userInfoList where password is null
        defaultUserInfoShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where photo equals to DEFAULT_PHOTO
        defaultUserInfoShouldBeFound("photo.equals=" + DEFAULT_PHOTO);

        // Get all the userInfoList where photo equals to UPDATED_PHOTO
        defaultUserInfoShouldNotBeFound("photo.equals=" + UPDATED_PHOTO);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPhotoIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where photo in DEFAULT_PHOTO or UPDATED_PHOTO
        defaultUserInfoShouldBeFound("photo.in=" + DEFAULT_PHOTO + "," + UPDATED_PHOTO);

        // Get all the userInfoList where photo equals to UPDATED_PHOTO
        defaultUserInfoShouldNotBeFound("photo.in=" + UPDATED_PHOTO);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPhotoIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where photo is not null
        defaultUserInfoShouldBeFound("photo.specified=true");

        // Get all the userInfoList where photo is null
        defaultUserInfoShouldNotBeFound("photo.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByRegisteredOnIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where registeredOn equals to DEFAULT_REGISTERED_ON
        defaultUserInfoShouldBeFound("registeredOn.equals=" + DEFAULT_REGISTERED_ON);

        // Get all the userInfoList where registeredOn equals to UPDATED_REGISTERED_ON
        defaultUserInfoShouldNotBeFound("registeredOn.equals=" + UPDATED_REGISTERED_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByRegisteredOnIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where registeredOn in DEFAULT_REGISTERED_ON or UPDATED_REGISTERED_ON
        defaultUserInfoShouldBeFound("registeredOn.in=" + DEFAULT_REGISTERED_ON + "," + UPDATED_REGISTERED_ON);

        // Get all the userInfoList where registeredOn equals to UPDATED_REGISTERED_ON
        defaultUserInfoShouldNotBeFound("registeredOn.in=" + UPDATED_REGISTERED_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByRegisteredOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where registeredOn is not null
        defaultUserInfoShouldBeFound("registeredOn.specified=true");

        // Get all the userInfoList where registeredOn is null
        defaultUserInfoShouldNotBeFound("registeredOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByRegisteredOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where registeredOn greater than or equals to DEFAULT_REGISTERED_ON
        defaultUserInfoShouldBeFound("registeredOn.greaterOrEqualThan=" + DEFAULT_REGISTERED_ON);

        // Get all the userInfoList where registeredOn greater than or equals to UPDATED_REGISTERED_ON
        defaultUserInfoShouldNotBeFound("registeredOn.greaterOrEqualThan=" + UPDATED_REGISTERED_ON);
    }

    @Test
    @Transactional
    public void getAllUserInfosByRegisteredOnIsLessThanSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where registeredOn less than or equals to DEFAULT_REGISTERED_ON
        defaultUserInfoShouldNotBeFound("registeredOn.lessThan=" + DEFAULT_REGISTERED_ON);

        // Get all the userInfoList where registeredOn less than or equals to UPDATED_REGISTERED_ON
        defaultUserInfoShouldBeFound("registeredOn.lessThan=" + UPDATED_REGISTERED_ON);
    }


    @Test
    @Transactional
    public void getAllUserInfosByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo userInfo = UserInfoResourceIntTest.createEntity(em);
        em.persist(userInfo);
        em.flush();
        userInfo.setUserInfo(userInfo);
        userInfoRepository.saveAndFlush(userInfo);
        Long userInfoId = userInfo.getId();

        // Get all the userInfoList where userInfo equals to userInfoId
        defaultUserInfoShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the userInfoList where userInfo equals to userInfoId + 1
        defaultUserInfoShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByChefProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        ChefProfile chefProfile = ChefProfileResourceIntTest.createEntity(em);
        em.persist(chefProfile);
        em.flush();
        userInfo.setChefProfile(chefProfile);
        userInfoRepository.saveAndFlush(userInfo);
        Long chefProfileId = chefProfile.getId();

        // Get all the userInfoList where chefProfile equals to chefProfileId
        defaultUserInfoShouldBeFound("chefProfileId.equals=" + chefProfileId);

        // Get all the userInfoList where chefProfile equals to chefProfileId + 1
        defaultUserInfoShouldNotBeFound("chefProfileId.equals=" + (chefProfileId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByUserInfoRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfoRole userInfoRole = UserInfoRoleResourceIntTest.createEntity(em);
        em.persist(userInfoRole);
        em.flush();
        userInfo.addUserInfoRole(userInfoRole);
        userInfoRepository.saveAndFlush(userInfo);
        Long userInfoRoleId = userInfoRole.getId();

        // Get all the userInfoList where userInfoRole equals to userInfoRoleId
        defaultUserInfoShouldBeFound("userInfoRoleId.equals=" + userInfoRoleId);

        // Get all the userInfoList where userInfoRole equals to userInfoRoleId + 1
        defaultUserInfoShouldNotBeFound("userInfoRoleId.equals=" + (userInfoRoleId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByRecipeCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeComment recipeComment = RecipeCommentResourceIntTest.createEntity(em);
        em.persist(recipeComment);
        em.flush();
        userInfo.addRecipeComment(recipeComment);
        userInfoRepository.saveAndFlush(userInfo);
        Long recipeCommentId = recipeComment.getId();

        // Get all the userInfoList where recipeComment equals to recipeCommentId
        defaultUserInfoShouldBeFound("recipeCommentId.equals=" + recipeCommentId);

        // Get all the userInfoList where recipeComment equals to recipeCommentId + 1
        defaultUserInfoShouldNotBeFound("recipeCommentId.equals=" + (recipeCommentId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByRecipeRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeRating recipeRating = RecipeRatingResourceIntTest.createEntity(em);
        em.persist(recipeRating);
        em.flush();
        userInfo.addRecipeRating(recipeRating);
        userInfoRepository.saveAndFlush(userInfo);
        Long recipeRatingId = recipeRating.getId();

        // Get all the userInfoList where recipeRating equals to recipeRatingId
        defaultUserInfoShouldBeFound("recipeRatingId.equals=" + recipeRatingId);

        // Get all the userInfoList where recipeRating equals to recipeRatingId + 1
        defaultUserInfoShouldNotBeFound("recipeRatingId.equals=" + (recipeRatingId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByRecipeLikeIsEqualToSomething() throws Exception {
        // Initialize the database
        RecipeLike recipeLike = RecipeLikeResourceIntTest.createEntity(em);
        em.persist(recipeLike);
        em.flush();
        userInfo.addRecipeLike(recipeLike);
        userInfoRepository.saveAndFlush(userInfo);
        Long recipeLikeId = recipeLike.getId();

        // Get all the userInfoList where recipeLike equals to recipeLikeId
        defaultUserInfoShouldBeFound("recipeLikeId.equals=" + recipeLikeId);

        // Get all the userInfoList where recipeLike equals to recipeLikeId + 1
        defaultUserInfoShouldNotBeFound("recipeLikeId.equals=" + (recipeLikeId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByFootnoteIsEqualToSomething() throws Exception {
        // Initialize the database
        Footnote footnote = FootnoteResourceIntTest.createEntity(em);
        em.persist(footnote);
        em.flush();
        userInfo.addFootnote(footnote);
        userInfoRepository.saveAndFlush(userInfo);
        Long footnoteId = footnote.getId();

        // Get all the userInfoList where footnote equals to footnoteId
        defaultUserInfoShouldBeFound("footnoteId.equals=" + footnoteId);

        // Get all the userInfoList where footnote equals to footnoteId + 1
        defaultUserInfoShouldNotBeFound("footnoteId.equals=" + (footnoteId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByForSharedByIsEqualToSomething() throws Exception {
        // Initialize the database
        ShareRecipe forSharedBy = ShareRecipeResourceIntTest.createEntity(em);
        em.persist(forSharedBy);
        em.flush();
        userInfo.addForSharedBy(forSharedBy);
        userInfoRepository.saveAndFlush(userInfo);
        Long forSharedById = forSharedBy.getId();

        // Get all the userInfoList where forSharedBy equals to forSharedById
        defaultUserInfoShouldBeFound("forSharedById.equals=" + forSharedById);

        // Get all the userInfoList where forSharedBy equals to forSharedById + 1
        defaultUserInfoShouldNotBeFound("forSharedById.equals=" + (forSharedById + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByForSharedToIsEqualToSomething() throws Exception {
        // Initialize the database
        ShareRecipe forSharedTo = ShareRecipeResourceIntTest.createEntity(em);
        em.persist(forSharedTo);
        em.flush();
        userInfo.addForSharedTo(forSharedTo);
        userInfoRepository.saveAndFlush(userInfo);
        Long forSharedToId = forSharedTo.getId();

        // Get all the userInfoList where forSharedTo equals to forSharedToId
        defaultUserInfoShouldBeFound("forSharedToId.equals=" + forSharedToId);

        // Get all the userInfoList where forSharedTo equals to forSharedToId + 1
        defaultUserInfoShouldNotBeFound("forSharedToId.equals=" + (forSharedToId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByInviteEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        InviteEmail inviteEmail = InviteEmailResourceIntTest.createEntity(em);
        em.persist(inviteEmail);
        em.flush();
        userInfo.addInviteEmail(inviteEmail);
        userInfoRepository.saveAndFlush(userInfo);
        Long inviteEmailId = inviteEmail.getId();

        // Get all the userInfoList where inviteEmail equals to inviteEmailId
        defaultUserInfoShouldBeFound("inviteEmailId.equals=" + inviteEmailId);

        // Get all the userInfoList where inviteEmail equals to inviteEmailId + 1
        defaultUserInfoShouldNotBeFound("inviteEmailId.equals=" + (inviteEmailId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByInviteContactIsEqualToSomething() throws Exception {
        // Initialize the database
        InviteContact inviteContact = InviteContactResourceIntTest.createEntity(em);
        em.persist(inviteContact);
        em.flush();
        userInfo.addInviteContact(inviteContact);
        userInfoRepository.saveAndFlush(userInfo);
        Long inviteContactId = inviteContact.getId();

        // Get all the userInfoList where inviteContact equals to inviteContactId
        defaultUserInfoShouldBeFound("inviteContactId.equals=" + inviteContactId);

        // Get all the userInfoList where inviteContact equals to inviteContactId + 1
        defaultUserInfoShouldNotBeFound("inviteContactId.equals=" + (inviteContactId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByForRateByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        UserRating forRateByUser = UserRatingResourceIntTest.createEntity(em);
        em.persist(forRateByUser);
        em.flush();
        userInfo.addForRateByUser(forRateByUser);
        userInfoRepository.saveAndFlush(userInfo);
        Long forRateByUserId = forRateByUser.getId();

        // Get all the userInfoList where forRateByUser equals to forRateByUserId
        defaultUserInfoShouldBeFound("forRateByUserId.equals=" + forRateByUserId);

        // Get all the userInfoList where forRateByUser equals to forRateByUserId + 1
        defaultUserInfoShouldNotBeFound("forRateByUserId.equals=" + (forRateByUserId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByForRateToUserIsEqualToSomething() throws Exception {
        // Initialize the database
        UserRating forRateToUser = UserRatingResourceIntTest.createEntity(em);
        em.persist(forRateToUser);
        em.flush();
        userInfo.addForRateToUser(forRateToUser);
        userInfoRepository.saveAndFlush(userInfo);
        Long forRateToUserId = forRateToUser.getId();

        // Get all the userInfoList where forRateToUser equals to forRateToUserId
        defaultUserInfoShouldBeFound("forRateToUserId.equals=" + forRateToUserId);

        // Get all the userInfoList where forRateToUser equals to forRateToUserId + 1
        defaultUserInfoShouldNotBeFound("forRateToUserId.equals=" + (forRateToUserId + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByInvitedByIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo invitedBy = UserInfoResourceIntTest.createEntity(em);
        em.persist(invitedBy);
        em.flush();
        userInfo.setInvitedBy(invitedBy);
        invitedBy.setUserInfo(userInfo);
        userInfoRepository.saveAndFlush(userInfo);
        Long invitedById = invitedBy.getId();

        // Get all the userInfoList where invitedBy equals to invitedById
        defaultUserInfoShouldBeFound("invitedById.equals=" + invitedById);

        // Get all the userInfoList where invitedBy equals to invitedById + 1
        defaultUserInfoShouldNotBeFound("invitedById.equals=" + (invitedById + 1));
    }


    @Test
    @Transactional
    public void getAllUserInfosByRestaurantIsEqualToSomething() throws Exception {
        // Initialize the database
        Restaurant restaurant = RestaurantResourceIntTest.createEntity(em);
        em.persist(restaurant);
        em.flush();
        userInfo.setRestaurant(restaurant);
        userInfoRepository.saveAndFlush(userInfo);
        Long restaurantId = restaurant.getId();

        // Get all the userInfoList where restaurant equals to restaurantId
        defaultUserInfoShouldBeFound("restaurantId.equals=" + restaurantId);

        // Get all the userInfoList where restaurant equals to restaurantId + 1
        defaultUserInfoShouldNotBeFound("restaurantId.equals=" + (restaurantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultUserInfoShouldBeFound(String filter) throws Exception {
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].authenticated").value(hasItem(DEFAULT_AUTHENTICATED.booleanValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.intValue())))
            .andExpect(jsonPath("$.[*].currentLoggedIn").value(hasItem(DEFAULT_CURRENT_LOGGED_IN.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].emailConfirmationSentOn").value(hasItem(DEFAULT_EMAIL_CONFIRMATION_SENT_ON.toString())))
            .andExpect(jsonPath("$.[*].emailConfirmed").value(hasItem(DEFAULT_EMAIL_CONFIRMED.booleanValue())))
            .andExpect(jsonPath("$.[*].emailConfirmedOn").value(hasItem(DEFAULT_EMAIL_CONFIRMED_ON.toString())))
            .andExpect(jsonPath("$.[*].lastLoggedIn").value(hasItem(DEFAULT_LAST_LOGGED_IN.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].registeredOn").value(hasItem(DEFAULT_REGISTERED_ON.toString())));

        // Check, that the count call also returns 1
        restUserInfoMockMvc.perform(get("/api/user-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultUserInfoShouldNotBeFound(String filter) throws Exception {
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserInfoMockMvc.perform(get("/api/user-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInfo() throws Exception {
        // Initialize the database
        userInfoService.save(userInfo);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockUserInfoSearchRepository);

        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on updatedUserInfo are not directly saved in db
        em.detach(updatedUserInfo);
        updatedUserInfo
            .authenticated(UPDATED_AUTHENTICATED)
            .contact(UPDATED_CONTACT)
            .currentLoggedIn(UPDATED_CURRENT_LOGGED_IN)
            .email(UPDATED_EMAIL)
            .emailConfirmationSentOn(UPDATED_EMAIL_CONFIRMATION_SENT_ON)
            .emailConfirmed(UPDATED_EMAIL_CONFIRMED)
            .emailConfirmedOn(UPDATED_EMAIL_CONFIRMED_ON)
            .lastLoggedIn(UPDATED_LAST_LOGGED_IN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .password(UPDATED_PASSWORD)
            .photo(UPDATED_PHOTO)
            .registeredOn(UPDATED_REGISTERED_ON);

        restUserInfoMockMvc.perform(put("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserInfo)))
            .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.isAuthenticated()).isEqualTo(UPDATED_AUTHENTICATED);
        assertThat(testUserInfo.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testUserInfo.getCurrentLoggedIn()).isEqualTo(UPDATED_CURRENT_LOGGED_IN);
        assertThat(testUserInfo.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserInfo.getEmailConfirmationSentOn()).isEqualTo(UPDATED_EMAIL_CONFIRMATION_SENT_ON);
        assertThat(testUserInfo.isEmailConfirmed()).isEqualTo(UPDATED_EMAIL_CONFIRMED);
        assertThat(testUserInfo.getEmailConfirmedOn()).isEqualTo(UPDATED_EMAIL_CONFIRMED_ON);
        assertThat(testUserInfo.getLastLoggedIn()).isEqualTo(UPDATED_LAST_LOGGED_IN);
        assertThat(testUserInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserInfo.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserInfo.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testUserInfo.getRegisteredOn()).isEqualTo(UPDATED_REGISTERED_ON);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(1)).save(testUserInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Create the UserInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInfoMockMvc.perform(put("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(0)).save(userInfo);
    }

    @Test
    @Transactional
    public void deleteUserInfo() throws Exception {
        // Initialize the database
        userInfoService.save(userInfo);

        int databaseSizeBeforeDelete = userInfoRepository.findAll().size();

        // Delete the userInfo
        restUserInfoMockMvc.perform(delete("/api/user-infos/{id}", userInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(1)).deleteById(userInfo.getId());
    }

    @Test
    @Transactional
    public void searchUserInfo() throws Exception {
        // Initialize the database
        userInfoService.save(userInfo);
        when(mockUserInfoSearchRepository.search(queryStringQuery("id:" + userInfo.getId())))
            .thenReturn(Collections.singletonList(userInfo));
        // Search the userInfo
        restUserInfoMockMvc.perform(get("/api/_search/user-infos?query=id:" + userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].authenticated").value(hasItem(DEFAULT_AUTHENTICATED.booleanValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.intValue())))
            .andExpect(jsonPath("$.[*].currentLoggedIn").value(hasItem(DEFAULT_CURRENT_LOGGED_IN.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].emailConfirmationSentOn").value(hasItem(DEFAULT_EMAIL_CONFIRMATION_SENT_ON.toString())))
            .andExpect(jsonPath("$.[*].emailConfirmed").value(hasItem(DEFAULT_EMAIL_CONFIRMED.booleanValue())))
            .andExpect(jsonPath("$.[*].emailConfirmedOn").value(hasItem(DEFAULT_EMAIL_CONFIRMED_ON.toString())))
            .andExpect(jsonPath("$.[*].lastLoggedIn").value(hasItem(DEFAULT_LAST_LOGGED_IN.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].registeredOn").value(hasItem(DEFAULT_REGISTERED_ON.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInfo.class);
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setId(1L);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(userInfo1.getId());
        assertThat(userInfo1).isEqualTo(userInfo2);
        userInfo2.setId(2L);
        assertThat(userInfo1).isNotEqualTo(userInfo2);
        userInfo1.setId(null);
        assertThat(userInfo1).isNotEqualTo(userInfo2);
    }
}

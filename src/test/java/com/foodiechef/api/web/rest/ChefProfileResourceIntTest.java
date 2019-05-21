package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.ChefProfile;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.ChefProfileRepository;
import com.foodiechef.api.repository.search.ChefProfileSearchRepository;
import com.foodiechef.api.service.ChefProfileService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.ChefProfileCriteria;
import com.foodiechef.api.service.ChefProfileQueryService;

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
 * Test class for the ChefProfileResource REST controller.
 *
 * @see ChefProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class ChefProfileResourceIntTest {

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALITY = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    @Autowired
    private ChefProfileRepository chefProfileRepository;

    @Autowired
    private ChefProfileService chefProfileService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.ChefProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChefProfileSearchRepository mockChefProfileSearchRepository;

    @Autowired
    private ChefProfileQueryService chefProfileQueryService;

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

    private MockMvc restChefProfileMockMvc;

    private ChefProfile chefProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChefProfileResource chefProfileResource = new ChefProfileResource(chefProfileService, chefProfileQueryService);
        this.restChefProfileMockMvc = MockMvcBuilders.standaloneSetup(chefProfileResource)
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
    public static ChefProfile createEntity(EntityManager em) {
        ChefProfile chefProfile = new ChefProfile()
            .photo(DEFAULT_PHOTO)
            .speciality(DEFAULT_SPECIALITY)
            .type(DEFAULT_TYPE)
            .website(DEFAULT_WEBSITE);
        return chefProfile;
    }

    @Before
    public void initTest() {
        chefProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createChefProfile() throws Exception {
        int databaseSizeBeforeCreate = chefProfileRepository.findAll().size();

        // Create the ChefProfile
        restChefProfileMockMvc.perform(post("/api/chef-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chefProfile)))
            .andExpect(status().isCreated());

        // Validate the ChefProfile in the database
        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeCreate + 1);
        ChefProfile testChefProfile = chefProfileList.get(chefProfileList.size() - 1);
        assertThat(testChefProfile.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testChefProfile.getSpeciality()).isEqualTo(DEFAULT_SPECIALITY);
        assertThat(testChefProfile.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testChefProfile.getWebsite()).isEqualTo(DEFAULT_WEBSITE);

        // Validate the ChefProfile in Elasticsearch
        verify(mockChefProfileSearchRepository, times(1)).save(testChefProfile);
    }

    @Test
    @Transactional
    public void createChefProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chefProfileRepository.findAll().size();

        // Create the ChefProfile with an existing ID
        chefProfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChefProfileMockMvc.perform(post("/api/chef-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chefProfile)))
            .andExpect(status().isBadRequest());

        // Validate the ChefProfile in the database
        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeCreate);

        // Validate the ChefProfile in Elasticsearch
        verify(mockChefProfileSearchRepository, times(0)).save(chefProfile);
    }

    @Test
    @Transactional
    public void checkPhotoIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProfileRepository.findAll().size();
        // set the field null
        chefProfile.setPhoto(null);

        // Create the ChefProfile, which fails.

        restChefProfileMockMvc.perform(post("/api/chef-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chefProfile)))
            .andExpect(status().isBadRequest());

        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSpecialityIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProfileRepository.findAll().size();
        // set the field null
        chefProfile.setSpeciality(null);

        // Create the ChefProfile, which fails.

        restChefProfileMockMvc.perform(post("/api/chef-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chefProfile)))
            .andExpect(status().isBadRequest());

        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProfileRepository.findAll().size();
        // set the field null
        chefProfile.setType(null);

        // Create the ChefProfile, which fails.

        restChefProfileMockMvc.perform(post("/api/chef-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chefProfile)))
            .andExpect(status().isBadRequest());

        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWebsiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = chefProfileRepository.findAll().size();
        // set the field null
        chefProfile.setWebsite(null);

        // Create the ChefProfile, which fails.

        restChefProfileMockMvc.perform(post("/api/chef-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chefProfile)))
            .andExpect(status().isBadRequest());

        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChefProfiles() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList
        restChefProfileMockMvc.perform(get("/api/chef-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chefProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.toString())))
            .andExpect(jsonPath("$.[*].speciality").value(hasItem(DEFAULT_SPECIALITY.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())));
    }
    
    @Test
    @Transactional
    public void getChefProfile() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get the chefProfile
        restChefProfileMockMvc.perform(get("/api/chef-profiles/{id}", chefProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chefProfile.getId().intValue()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO.toString()))
            .andExpect(jsonPath("$.speciality").value(DEFAULT_SPECIALITY.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()));
    }

    @Test
    @Transactional
    public void getAllChefProfilesByPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where photo equals to DEFAULT_PHOTO
        defaultChefProfileShouldBeFound("photo.equals=" + DEFAULT_PHOTO);

        // Get all the chefProfileList where photo equals to UPDATED_PHOTO
        defaultChefProfileShouldNotBeFound("photo.equals=" + UPDATED_PHOTO);
    }

    @Test
    @Transactional
    public void getAllChefProfilesByPhotoIsInShouldWork() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where photo in DEFAULT_PHOTO or UPDATED_PHOTO
        defaultChefProfileShouldBeFound("photo.in=" + DEFAULT_PHOTO + "," + UPDATED_PHOTO);

        // Get all the chefProfileList where photo equals to UPDATED_PHOTO
        defaultChefProfileShouldNotBeFound("photo.in=" + UPDATED_PHOTO);
    }

    @Test
    @Transactional
    public void getAllChefProfilesByPhotoIsNullOrNotNull() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where photo is not null
        defaultChefProfileShouldBeFound("photo.specified=true");

        // Get all the chefProfileList where photo is null
        defaultChefProfileShouldNotBeFound("photo.specified=false");
    }

    @Test
    @Transactional
    public void getAllChefProfilesBySpecialityIsEqualToSomething() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where speciality equals to DEFAULT_SPECIALITY
        defaultChefProfileShouldBeFound("speciality.equals=" + DEFAULT_SPECIALITY);

        // Get all the chefProfileList where speciality equals to UPDATED_SPECIALITY
        defaultChefProfileShouldNotBeFound("speciality.equals=" + UPDATED_SPECIALITY);
    }

    @Test
    @Transactional
    public void getAllChefProfilesBySpecialityIsInShouldWork() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where speciality in DEFAULT_SPECIALITY or UPDATED_SPECIALITY
        defaultChefProfileShouldBeFound("speciality.in=" + DEFAULT_SPECIALITY + "," + UPDATED_SPECIALITY);

        // Get all the chefProfileList where speciality equals to UPDATED_SPECIALITY
        defaultChefProfileShouldNotBeFound("speciality.in=" + UPDATED_SPECIALITY);
    }

    @Test
    @Transactional
    public void getAllChefProfilesBySpecialityIsNullOrNotNull() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where speciality is not null
        defaultChefProfileShouldBeFound("speciality.specified=true");

        // Get all the chefProfileList where speciality is null
        defaultChefProfileShouldNotBeFound("speciality.specified=false");
    }

    @Test
    @Transactional
    public void getAllChefProfilesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where type equals to DEFAULT_TYPE
        defaultChefProfileShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the chefProfileList where type equals to UPDATED_TYPE
        defaultChefProfileShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllChefProfilesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultChefProfileShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the chefProfileList where type equals to UPDATED_TYPE
        defaultChefProfileShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllChefProfilesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where type is not null
        defaultChefProfileShouldBeFound("type.specified=true");

        // Get all the chefProfileList where type is null
        defaultChefProfileShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllChefProfilesByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where website equals to DEFAULT_WEBSITE
        defaultChefProfileShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the chefProfileList where website equals to UPDATED_WEBSITE
        defaultChefProfileShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    public void getAllChefProfilesByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultChefProfileShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the chefProfileList where website equals to UPDATED_WEBSITE
        defaultChefProfileShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    public void getAllChefProfilesByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        chefProfileRepository.saveAndFlush(chefProfile);

        // Get all the chefProfileList where website is not null
        defaultChefProfileShouldBeFound("website.specified=true");

        // Get all the chefProfileList where website is null
        defaultChefProfileShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    public void getAllChefProfilesByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo userInfo = UserInfoResourceIntTest.createEntity(em);
        em.persist(userInfo);
        em.flush();
        chefProfile.setUserInfo(userInfo);
        userInfo.setChefProfile(chefProfile);
        chefProfileRepository.saveAndFlush(chefProfile);
        Long userInfoId = userInfo.getId();

        // Get all the chefProfileList where userInfo equals to userInfoId
        defaultChefProfileShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the chefProfileList where userInfo equals to userInfoId + 1
        defaultChefProfileShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultChefProfileShouldBeFound(String filter) throws Exception {
        restChefProfileMockMvc.perform(get("/api/chef-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chefProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].speciality").value(hasItem(DEFAULT_SPECIALITY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));

        // Check, that the count call also returns 1
        restChefProfileMockMvc.perform(get("/api/chef-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultChefProfileShouldNotBeFound(String filter) throws Exception {
        restChefProfileMockMvc.perform(get("/api/chef-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChefProfileMockMvc.perform(get("/api/chef-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChefProfile() throws Exception {
        // Get the chefProfile
        restChefProfileMockMvc.perform(get("/api/chef-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChefProfile() throws Exception {
        // Initialize the database
        chefProfileService.save(chefProfile);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockChefProfileSearchRepository);

        int databaseSizeBeforeUpdate = chefProfileRepository.findAll().size();

        // Update the chefProfile
        ChefProfile updatedChefProfile = chefProfileRepository.findById(chefProfile.getId()).get();
        // Disconnect from session so that the updates on updatedChefProfile are not directly saved in db
        em.detach(updatedChefProfile);
        updatedChefProfile
            .photo(UPDATED_PHOTO)
            .speciality(UPDATED_SPECIALITY)
            .type(UPDATED_TYPE)
            .website(UPDATED_WEBSITE);

        restChefProfileMockMvc.perform(put("/api/chef-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChefProfile)))
            .andExpect(status().isOk());

        // Validate the ChefProfile in the database
        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeUpdate);
        ChefProfile testChefProfile = chefProfileList.get(chefProfileList.size() - 1);
        assertThat(testChefProfile.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testChefProfile.getSpeciality()).isEqualTo(UPDATED_SPECIALITY);
        assertThat(testChefProfile.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testChefProfile.getWebsite()).isEqualTo(UPDATED_WEBSITE);

        // Validate the ChefProfile in Elasticsearch
        verify(mockChefProfileSearchRepository, times(1)).save(testChefProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingChefProfile() throws Exception {
        int databaseSizeBeforeUpdate = chefProfileRepository.findAll().size();

        // Create the ChefProfile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChefProfileMockMvc.perform(put("/api/chef-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chefProfile)))
            .andExpect(status().isBadRequest());

        // Validate the ChefProfile in the database
        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChefProfile in Elasticsearch
        verify(mockChefProfileSearchRepository, times(0)).save(chefProfile);
    }

    @Test
    @Transactional
    public void deleteChefProfile() throws Exception {
        // Initialize the database
        chefProfileService.save(chefProfile);

        int databaseSizeBeforeDelete = chefProfileRepository.findAll().size();

        // Delete the chefProfile
        restChefProfileMockMvc.perform(delete("/api/chef-profiles/{id}", chefProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ChefProfile> chefProfileList = chefProfileRepository.findAll();
        assertThat(chefProfileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ChefProfile in Elasticsearch
        verify(mockChefProfileSearchRepository, times(1)).deleteById(chefProfile.getId());
    }

    @Test
    @Transactional
    public void searchChefProfile() throws Exception {
        // Initialize the database
        chefProfileService.save(chefProfile);
        when(mockChefProfileSearchRepository.search(queryStringQuery("id:" + chefProfile.getId())))
            .thenReturn(Collections.singletonList(chefProfile));
        // Search the chefProfile
        restChefProfileMockMvc.perform(get("/api/_search/chef-profiles?query=id:" + chefProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chefProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].speciality").value(hasItem(DEFAULT_SPECIALITY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChefProfile.class);
        ChefProfile chefProfile1 = new ChefProfile();
        chefProfile1.setId(1L);
        ChefProfile chefProfile2 = new ChefProfile();
        chefProfile2.setId(chefProfile1.getId());
        assertThat(chefProfile1).isEqualTo(chefProfile2);
        chefProfile2.setId(2L);
        assertThat(chefProfile1).isNotEqualTo(chefProfile2);
        chefProfile1.setId(null);
        assertThat(chefProfile1).isNotEqualTo(chefProfile2);
    }
}

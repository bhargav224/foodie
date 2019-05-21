package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.UserInfoRole;
import com.foodiechef.api.repository.UserInfoRoleRepository;
import com.foodiechef.api.repository.search.UserInfoRoleSearchRepository;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;

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
 * Test class for the UserInfoRoleResource REST controller.
 *
 * @see UserInfoRoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class UserInfoRoleResourceIntTest {

    @Autowired
    private UserInfoRoleRepository userInfoRoleRepository;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.UserInfoRoleSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserInfoRoleSearchRepository mockUserInfoRoleSearchRepository;

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

    private MockMvc restUserInfoRoleMockMvc;

    private UserInfoRole userInfoRole;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserInfoRoleResource userInfoRoleResource = new UserInfoRoleResource(userInfoRoleRepository, mockUserInfoRoleSearchRepository);
        this.restUserInfoRoleMockMvc = MockMvcBuilders.standaloneSetup(userInfoRoleResource)
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
    public static UserInfoRole createEntity(EntityManager em) {
        UserInfoRole userInfoRole = new UserInfoRole();
        return userInfoRole;
    }

    @Before
    public void initTest() {
        userInfoRole = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserInfoRole() throws Exception {
        int databaseSizeBeforeCreate = userInfoRoleRepository.findAll().size();

        // Create the UserInfoRole
        restUserInfoRoleMockMvc.perform(post("/api/user-info-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfoRole)))
            .andExpect(status().isCreated());

        // Validate the UserInfoRole in the database
        List<UserInfoRole> userInfoRoleList = userInfoRoleRepository.findAll();
        assertThat(userInfoRoleList).hasSize(databaseSizeBeforeCreate + 1);
        UserInfoRole testUserInfoRole = userInfoRoleList.get(userInfoRoleList.size() - 1);

        // Validate the UserInfoRole in Elasticsearch
        verify(mockUserInfoRoleSearchRepository, times(1)).save(testUserInfoRole);
    }

    @Test
    @Transactional
    public void createUserInfoRoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userInfoRoleRepository.findAll().size();

        // Create the UserInfoRole with an existing ID
        userInfoRole.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInfoRoleMockMvc.perform(post("/api/user-info-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfoRole)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfoRole in the database
        List<UserInfoRole> userInfoRoleList = userInfoRoleRepository.findAll();
        assertThat(userInfoRoleList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserInfoRole in Elasticsearch
        verify(mockUserInfoRoleSearchRepository, times(0)).save(userInfoRole);
    }

    @Test
    @Transactional
    public void getAllUserInfoRoles() throws Exception {
        // Initialize the database
        userInfoRoleRepository.saveAndFlush(userInfoRole);

        // Get all the userInfoRoleList
        restUserInfoRoleMockMvc.perform(get("/api/user-info-roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfoRole.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getUserInfoRole() throws Exception {
        // Initialize the database
        userInfoRoleRepository.saveAndFlush(userInfoRole);

        // Get the userInfoRole
        restUserInfoRoleMockMvc.perform(get("/api/user-info-roles/{id}", userInfoRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userInfoRole.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserInfoRole() throws Exception {
        // Get the userInfoRole
        restUserInfoRoleMockMvc.perform(get("/api/user-info-roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInfoRole() throws Exception {
        // Initialize the database
        userInfoRoleRepository.saveAndFlush(userInfoRole);

        int databaseSizeBeforeUpdate = userInfoRoleRepository.findAll().size();

        // Update the userInfoRole
        UserInfoRole updatedUserInfoRole = userInfoRoleRepository.findById(userInfoRole.getId()).get();
        // Disconnect from session so that the updates on updatedUserInfoRole are not directly saved in db
        em.detach(updatedUserInfoRole);

        restUserInfoRoleMockMvc.perform(put("/api/user-info-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserInfoRole)))
            .andExpect(status().isOk());

        // Validate the UserInfoRole in the database
        List<UserInfoRole> userInfoRoleList = userInfoRoleRepository.findAll();
        assertThat(userInfoRoleList).hasSize(databaseSizeBeforeUpdate);
        UserInfoRole testUserInfoRole = userInfoRoleList.get(userInfoRoleList.size() - 1);

        // Validate the UserInfoRole in Elasticsearch
        verify(mockUserInfoRoleSearchRepository, times(1)).save(testUserInfoRole);
    }

    @Test
    @Transactional
    public void updateNonExistingUserInfoRole() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRoleRepository.findAll().size();

        // Create the UserInfoRole

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInfoRoleMockMvc.perform(put("/api/user-info-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfoRole)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfoRole in the database
        List<UserInfoRole> userInfoRoleList = userInfoRoleRepository.findAll();
        assertThat(userInfoRoleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserInfoRole in Elasticsearch
        verify(mockUserInfoRoleSearchRepository, times(0)).save(userInfoRole);
    }

    @Test
    @Transactional
    public void deleteUserInfoRole() throws Exception {
        // Initialize the database
        userInfoRoleRepository.saveAndFlush(userInfoRole);

        int databaseSizeBeforeDelete = userInfoRoleRepository.findAll().size();

        // Delete the userInfoRole
        restUserInfoRoleMockMvc.perform(delete("/api/user-info-roles/{id}", userInfoRole.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserInfoRole> userInfoRoleList = userInfoRoleRepository.findAll();
        assertThat(userInfoRoleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserInfoRole in Elasticsearch
        verify(mockUserInfoRoleSearchRepository, times(1)).deleteById(userInfoRole.getId());
    }

    @Test
    @Transactional
    public void searchUserInfoRole() throws Exception {
        // Initialize the database
        userInfoRoleRepository.saveAndFlush(userInfoRole);
        when(mockUserInfoRoleSearchRepository.search(queryStringQuery("id:" + userInfoRole.getId())))
            .thenReturn(Collections.singletonList(userInfoRole));
        // Search the userInfoRole
        restUserInfoRoleMockMvc.perform(get("/api/_search/user-info-roles?query=id:" + userInfoRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfoRole.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInfoRole.class);
        UserInfoRole userInfoRole1 = new UserInfoRole();
        userInfoRole1.setId(1L);
        UserInfoRole userInfoRole2 = new UserInfoRole();
        userInfoRole2.setId(userInfoRole1.getId());
        assertThat(userInfoRole1).isEqualTo(userInfoRole2);
        userInfoRole2.setId(2L);
        assertThat(userInfoRole1).isNotEqualTo(userInfoRole2);
        userInfoRole1.setId(null);
        assertThat(userInfoRole1).isNotEqualTo(userInfoRole2);
    }
}

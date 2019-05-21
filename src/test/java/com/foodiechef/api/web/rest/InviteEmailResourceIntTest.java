package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.InviteEmail;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.InviteEmailRepository;
import com.foodiechef.api.repository.search.InviteEmailSearchRepository;
import com.foodiechef.api.service.InviteEmailService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.InviteEmailCriteria;
import com.foodiechef.api.service.InviteEmailQueryService;

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
 * Test class for the InviteEmailResource REST controller.
 *
 * @see InviteEmailResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class InviteEmailResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private InviteEmailRepository inviteEmailRepository;

    @Autowired
    private InviteEmailService inviteEmailService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.InviteEmailSearchRepositoryMockConfiguration
     */
    @Autowired
    private InviteEmailSearchRepository mockInviteEmailSearchRepository;

    @Autowired
    private InviteEmailQueryService inviteEmailQueryService;

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

    private MockMvc restInviteEmailMockMvc;

    private InviteEmail inviteEmail;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InviteEmailResource inviteEmailResource = new InviteEmailResource(inviteEmailService, inviteEmailQueryService);
        this.restInviteEmailMockMvc = MockMvcBuilders.standaloneSetup(inviteEmailResource)
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
    public static InviteEmail createEntity(EntityManager em) {
        InviteEmail inviteEmail = new InviteEmail()
            .email(DEFAULT_EMAIL);
        return inviteEmail;
    }

    @Before
    public void initTest() {
        inviteEmail = createEntity(em);
    }

    @Test
    @Transactional
    public void createInviteEmail() throws Exception {
        int databaseSizeBeforeCreate = inviteEmailRepository.findAll().size();

        // Create the InviteEmail
        restInviteEmailMockMvc.perform(post("/api/invite-emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inviteEmail)))
            .andExpect(status().isCreated());

        // Validate the InviteEmail in the database
        List<InviteEmail> inviteEmailList = inviteEmailRepository.findAll();
        assertThat(inviteEmailList).hasSize(databaseSizeBeforeCreate + 1);
        InviteEmail testInviteEmail = inviteEmailList.get(inviteEmailList.size() - 1);
        assertThat(testInviteEmail.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the InviteEmail in Elasticsearch
        verify(mockInviteEmailSearchRepository, times(1)).save(testInviteEmail);
    }

    @Test
    @Transactional
    public void createInviteEmailWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inviteEmailRepository.findAll().size();

        // Create the InviteEmail with an existing ID
        inviteEmail.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInviteEmailMockMvc.perform(post("/api/invite-emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inviteEmail)))
            .andExpect(status().isBadRequest());

        // Validate the InviteEmail in the database
        List<InviteEmail> inviteEmailList = inviteEmailRepository.findAll();
        assertThat(inviteEmailList).hasSize(databaseSizeBeforeCreate);

        // Validate the InviteEmail in Elasticsearch
        verify(mockInviteEmailSearchRepository, times(0)).save(inviteEmail);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = inviteEmailRepository.findAll().size();
        // set the field null
        inviteEmail.setEmail(null);

        // Create the InviteEmail, which fails.

        restInviteEmailMockMvc.perform(post("/api/invite-emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inviteEmail)))
            .andExpect(status().isBadRequest());

        List<InviteEmail> inviteEmailList = inviteEmailRepository.findAll();
        assertThat(inviteEmailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInviteEmails() throws Exception {
        // Initialize the database
        inviteEmailRepository.saveAndFlush(inviteEmail);

        // Get all the inviteEmailList
        restInviteEmailMockMvc.perform(get("/api/invite-emails?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inviteEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getInviteEmail() throws Exception {
        // Initialize the database
        inviteEmailRepository.saveAndFlush(inviteEmail);

        // Get the inviteEmail
        restInviteEmailMockMvc.perform(get("/api/invite-emails/{id}", inviteEmail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inviteEmail.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getAllInviteEmailsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteEmailRepository.saveAndFlush(inviteEmail);

        // Get all the inviteEmailList where email equals to DEFAULT_EMAIL
        defaultInviteEmailShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the inviteEmailList where email equals to UPDATED_EMAIL
        defaultInviteEmailShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllInviteEmailsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        inviteEmailRepository.saveAndFlush(inviteEmail);

        // Get all the inviteEmailList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultInviteEmailShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the inviteEmailList where email equals to UPDATED_EMAIL
        defaultInviteEmailShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllInviteEmailsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteEmailRepository.saveAndFlush(inviteEmail);

        // Get all the inviteEmailList where email is not null
        defaultInviteEmailShouldBeFound("email.specified=true");

        // Get all the inviteEmailList where email is null
        defaultInviteEmailShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllInviteEmailsByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo userInfo = UserInfoResourceIntTest.createEntity(em);
        em.persist(userInfo);
        em.flush();
        inviteEmail.setUserInfo(userInfo);
        inviteEmailRepository.saveAndFlush(inviteEmail);
        Long userInfoId = userInfo.getId();

        // Get all the inviteEmailList where userInfo equals to userInfoId
        defaultInviteEmailShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the inviteEmailList where userInfo equals to userInfoId + 1
        defaultInviteEmailShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInviteEmailShouldBeFound(String filter) throws Exception {
        restInviteEmailMockMvc.perform(get("/api/invite-emails?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inviteEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restInviteEmailMockMvc.perform(get("/api/invite-emails/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInviteEmailShouldNotBeFound(String filter) throws Exception {
        restInviteEmailMockMvc.perform(get("/api/invite-emails?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInviteEmailMockMvc.perform(get("/api/invite-emails/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInviteEmail() throws Exception {
        // Get the inviteEmail
        restInviteEmailMockMvc.perform(get("/api/invite-emails/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInviteEmail() throws Exception {
        // Initialize the database
        inviteEmailService.save(inviteEmail);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockInviteEmailSearchRepository);

        int databaseSizeBeforeUpdate = inviteEmailRepository.findAll().size();

        // Update the inviteEmail
        InviteEmail updatedInviteEmail = inviteEmailRepository.findById(inviteEmail.getId()).get();
        // Disconnect from session so that the updates on updatedInviteEmail are not directly saved in db
        em.detach(updatedInviteEmail);
        updatedInviteEmail
            .email(UPDATED_EMAIL);

        restInviteEmailMockMvc.perform(put("/api/invite-emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInviteEmail)))
            .andExpect(status().isOk());

        // Validate the InviteEmail in the database
        List<InviteEmail> inviteEmailList = inviteEmailRepository.findAll();
        assertThat(inviteEmailList).hasSize(databaseSizeBeforeUpdate);
        InviteEmail testInviteEmail = inviteEmailList.get(inviteEmailList.size() - 1);
        assertThat(testInviteEmail.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the InviteEmail in Elasticsearch
        verify(mockInviteEmailSearchRepository, times(1)).save(testInviteEmail);
    }

    @Test
    @Transactional
    public void updateNonExistingInviteEmail() throws Exception {
        int databaseSizeBeforeUpdate = inviteEmailRepository.findAll().size();

        // Create the InviteEmail

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInviteEmailMockMvc.perform(put("/api/invite-emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inviteEmail)))
            .andExpect(status().isBadRequest());

        // Validate the InviteEmail in the database
        List<InviteEmail> inviteEmailList = inviteEmailRepository.findAll();
        assertThat(inviteEmailList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InviteEmail in Elasticsearch
        verify(mockInviteEmailSearchRepository, times(0)).save(inviteEmail);
    }

    @Test
    @Transactional
    public void deleteInviteEmail() throws Exception {
        // Initialize the database
        inviteEmailService.save(inviteEmail);

        int databaseSizeBeforeDelete = inviteEmailRepository.findAll().size();

        // Delete the inviteEmail
        restInviteEmailMockMvc.perform(delete("/api/invite-emails/{id}", inviteEmail.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InviteEmail> inviteEmailList = inviteEmailRepository.findAll();
        assertThat(inviteEmailList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InviteEmail in Elasticsearch
        verify(mockInviteEmailSearchRepository, times(1)).deleteById(inviteEmail.getId());
    }

    @Test
    @Transactional
    public void searchInviteEmail() throws Exception {
        // Initialize the database
        inviteEmailService.save(inviteEmail);
        when(mockInviteEmailSearchRepository.search(queryStringQuery("id:" + inviteEmail.getId())))
            .thenReturn(Collections.singletonList(inviteEmail));
        // Search the inviteEmail
        restInviteEmailMockMvc.perform(get("/api/_search/invite-emails?query=id:" + inviteEmail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inviteEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InviteEmail.class);
        InviteEmail inviteEmail1 = new InviteEmail();
        inviteEmail1.setId(1L);
        InviteEmail inviteEmail2 = new InviteEmail();
        inviteEmail2.setId(inviteEmail1.getId());
        assertThat(inviteEmail1).isEqualTo(inviteEmail2);
        inviteEmail2.setId(2L);
        assertThat(inviteEmail1).isNotEqualTo(inviteEmail2);
        inviteEmail1.setId(null);
        assertThat(inviteEmail1).isNotEqualTo(inviteEmail2);
    }
}

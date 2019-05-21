package com.foodiechef.api.web.rest;

import com.foodiechef.api.FoodieChefApp;

import com.foodiechef.api.domain.InviteContact;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.InviteContactRepository;
import com.foodiechef.api.repository.search.InviteContactSearchRepository;
import com.foodiechef.api.service.InviteContactService;
import com.foodiechef.api.web.rest.errors.ExceptionTranslator;
import com.foodiechef.api.service.dto.InviteContactCriteria;
import com.foodiechef.api.service.InviteContactQueryService;

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
 * Test class for the InviteContactResource REST controller.
 *
 * @see InviteContactResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieChefApp.class)
public class InviteContactResourceIntTest {

    private static final Long DEFAULT_CONTACT = 1L;
    private static final Long UPDATED_CONTACT = 2L;

    @Autowired
    private InviteContactRepository inviteContactRepository;

    @Autowired
    private InviteContactService inviteContactService;

    /**
     * This repository is mocked in the com.foodiechef.api.repository.search test package.
     *
     * @see com.foodiechef.api.repository.search.InviteContactSearchRepositoryMockConfiguration
     */
    @Autowired
    private InviteContactSearchRepository mockInviteContactSearchRepository;

    @Autowired
    private InviteContactQueryService inviteContactQueryService;

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

    private MockMvc restInviteContactMockMvc;

    private InviteContact inviteContact;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InviteContactResource inviteContactResource = new InviteContactResource(inviteContactService, inviteContactQueryService);
        this.restInviteContactMockMvc = MockMvcBuilders.standaloneSetup(inviteContactResource)
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
    public static InviteContact createEntity(EntityManager em) {
        InviteContact inviteContact = new InviteContact()
            .contact(DEFAULT_CONTACT);
        return inviteContact;
    }

    @Before
    public void initTest() {
        inviteContact = createEntity(em);
    }

    @Test
    @Transactional
    public void createInviteContact() throws Exception {
        int databaseSizeBeforeCreate = inviteContactRepository.findAll().size();

        // Create the InviteContact
        restInviteContactMockMvc.perform(post("/api/invite-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inviteContact)))
            .andExpect(status().isCreated());

        // Validate the InviteContact in the database
        List<InviteContact> inviteContactList = inviteContactRepository.findAll();
        assertThat(inviteContactList).hasSize(databaseSizeBeforeCreate + 1);
        InviteContact testInviteContact = inviteContactList.get(inviteContactList.size() - 1);
        assertThat(testInviteContact.getContact()).isEqualTo(DEFAULT_CONTACT);

        // Validate the InviteContact in Elasticsearch
        verify(mockInviteContactSearchRepository, times(1)).save(testInviteContact);
    }

    @Test
    @Transactional
    public void createInviteContactWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inviteContactRepository.findAll().size();

        // Create the InviteContact with an existing ID
        inviteContact.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInviteContactMockMvc.perform(post("/api/invite-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inviteContact)))
            .andExpect(status().isBadRequest());

        // Validate the InviteContact in the database
        List<InviteContact> inviteContactList = inviteContactRepository.findAll();
        assertThat(inviteContactList).hasSize(databaseSizeBeforeCreate);

        // Validate the InviteContact in Elasticsearch
        verify(mockInviteContactSearchRepository, times(0)).save(inviteContact);
    }

    @Test
    @Transactional
    public void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = inviteContactRepository.findAll().size();
        // set the field null
        inviteContact.setContact(null);

        // Create the InviteContact, which fails.

        restInviteContactMockMvc.perform(post("/api/invite-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inviteContact)))
            .andExpect(status().isBadRequest());

        List<InviteContact> inviteContactList = inviteContactRepository.findAll();
        assertThat(inviteContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInviteContacts() throws Exception {
        // Initialize the database
        inviteContactRepository.saveAndFlush(inviteContact);

        // Get all the inviteContactList
        restInviteContactMockMvc.perform(get("/api/invite-contacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inviteContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.intValue())));
    }
    
    @Test
    @Transactional
    public void getInviteContact() throws Exception {
        // Initialize the database
        inviteContactRepository.saveAndFlush(inviteContact);

        // Get the inviteContact
        restInviteContactMockMvc.perform(get("/api/invite-contacts/{id}", inviteContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inviteContact.getId().intValue()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.intValue()));
    }

    @Test
    @Transactional
    public void getAllInviteContactsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteContactRepository.saveAndFlush(inviteContact);

        // Get all the inviteContactList where contact equals to DEFAULT_CONTACT
        defaultInviteContactShouldBeFound("contact.equals=" + DEFAULT_CONTACT);

        // Get all the inviteContactList where contact equals to UPDATED_CONTACT
        defaultInviteContactShouldNotBeFound("contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllInviteContactsByContactIsInShouldWork() throws Exception {
        // Initialize the database
        inviteContactRepository.saveAndFlush(inviteContact);

        // Get all the inviteContactList where contact in DEFAULT_CONTACT or UPDATED_CONTACT
        defaultInviteContactShouldBeFound("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT);

        // Get all the inviteContactList where contact equals to UPDATED_CONTACT
        defaultInviteContactShouldNotBeFound("contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllInviteContactsByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteContactRepository.saveAndFlush(inviteContact);

        // Get all the inviteContactList where contact is not null
        defaultInviteContactShouldBeFound("contact.specified=true");

        // Get all the inviteContactList where contact is null
        defaultInviteContactShouldNotBeFound("contact.specified=false");
    }

    @Test
    @Transactional
    public void getAllInviteContactsByContactIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inviteContactRepository.saveAndFlush(inviteContact);

        // Get all the inviteContactList where contact greater than or equals to DEFAULT_CONTACT
        defaultInviteContactShouldBeFound("contact.greaterOrEqualThan=" + DEFAULT_CONTACT);

        // Get all the inviteContactList where contact greater than or equals to UPDATED_CONTACT
        defaultInviteContactShouldNotBeFound("contact.greaterOrEqualThan=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllInviteContactsByContactIsLessThanSomething() throws Exception {
        // Initialize the database
        inviteContactRepository.saveAndFlush(inviteContact);

        // Get all the inviteContactList where contact less than or equals to DEFAULT_CONTACT
        defaultInviteContactShouldNotBeFound("contact.lessThan=" + DEFAULT_CONTACT);

        // Get all the inviteContactList where contact less than or equals to UPDATED_CONTACT
        defaultInviteContactShouldBeFound("contact.lessThan=" + UPDATED_CONTACT);
    }


    @Test
    @Transactional
    public void getAllInviteContactsByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        UserInfo userInfo = UserInfoResourceIntTest.createEntity(em);
        em.persist(userInfo);
        em.flush();
        inviteContact.setUserInfo(userInfo);
        inviteContactRepository.saveAndFlush(inviteContact);
        Long userInfoId = userInfo.getId();

        // Get all the inviteContactList where userInfo equals to userInfoId
        defaultInviteContactShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the inviteContactList where userInfo equals to userInfoId + 1
        defaultInviteContactShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInviteContactShouldBeFound(String filter) throws Exception {
        restInviteContactMockMvc.perform(get("/api/invite-contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inviteContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.intValue())));

        // Check, that the count call also returns 1
        restInviteContactMockMvc.perform(get("/api/invite-contacts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInviteContactShouldNotBeFound(String filter) throws Exception {
        restInviteContactMockMvc.perform(get("/api/invite-contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInviteContactMockMvc.perform(get("/api/invite-contacts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInviteContact() throws Exception {
        // Get the inviteContact
        restInviteContactMockMvc.perform(get("/api/invite-contacts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInviteContact() throws Exception {
        // Initialize the database
        inviteContactService.save(inviteContact);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockInviteContactSearchRepository);

        int databaseSizeBeforeUpdate = inviteContactRepository.findAll().size();

        // Update the inviteContact
        InviteContact updatedInviteContact = inviteContactRepository.findById(inviteContact.getId()).get();
        // Disconnect from session so that the updates on updatedInviteContact are not directly saved in db
        em.detach(updatedInviteContact);
        updatedInviteContact
            .contact(UPDATED_CONTACT);

        restInviteContactMockMvc.perform(put("/api/invite-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInviteContact)))
            .andExpect(status().isOk());

        // Validate the InviteContact in the database
        List<InviteContact> inviteContactList = inviteContactRepository.findAll();
        assertThat(inviteContactList).hasSize(databaseSizeBeforeUpdate);
        InviteContact testInviteContact = inviteContactList.get(inviteContactList.size() - 1);
        assertThat(testInviteContact.getContact()).isEqualTo(UPDATED_CONTACT);

        // Validate the InviteContact in Elasticsearch
        verify(mockInviteContactSearchRepository, times(1)).save(testInviteContact);
    }

    @Test
    @Transactional
    public void updateNonExistingInviteContact() throws Exception {
        int databaseSizeBeforeUpdate = inviteContactRepository.findAll().size();

        // Create the InviteContact

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInviteContactMockMvc.perform(put("/api/invite-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inviteContact)))
            .andExpect(status().isBadRequest());

        // Validate the InviteContact in the database
        List<InviteContact> inviteContactList = inviteContactRepository.findAll();
        assertThat(inviteContactList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InviteContact in Elasticsearch
        verify(mockInviteContactSearchRepository, times(0)).save(inviteContact);
    }

    @Test
    @Transactional
    public void deleteInviteContact() throws Exception {
        // Initialize the database
        inviteContactService.save(inviteContact);

        int databaseSizeBeforeDelete = inviteContactRepository.findAll().size();

        // Delete the inviteContact
        restInviteContactMockMvc.perform(delete("/api/invite-contacts/{id}", inviteContact.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InviteContact> inviteContactList = inviteContactRepository.findAll();
        assertThat(inviteContactList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InviteContact in Elasticsearch
        verify(mockInviteContactSearchRepository, times(1)).deleteById(inviteContact.getId());
    }

    @Test
    @Transactional
    public void searchInviteContact() throws Exception {
        // Initialize the database
        inviteContactService.save(inviteContact);
        when(mockInviteContactSearchRepository.search(queryStringQuery("id:" + inviteContact.getId())))
            .thenReturn(Collections.singletonList(inviteContact));
        // Search the inviteContact
        restInviteContactMockMvc.perform(get("/api/_search/invite-contacts?query=id:" + inviteContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inviteContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InviteContact.class);
        InviteContact inviteContact1 = new InviteContact();
        inviteContact1.setId(1L);
        InviteContact inviteContact2 = new InviteContact();
        inviteContact2.setId(inviteContact1.getId());
        assertThat(inviteContact1).isEqualTo(inviteContact2);
        inviteContact2.setId(2L);
        assertThat(inviteContact1).isNotEqualTo(inviteContact2);
        inviteContact1.setId(null);
        assertThat(inviteContact1).isNotEqualTo(inviteContact2);
    }
}

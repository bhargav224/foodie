package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.InviteContactService;
import com.foodiechef.api.domain.InviteContact;
import com.foodiechef.api.repository.InviteContactRepository;
import com.foodiechef.api.repository.search.InviteContactSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InviteContact.
 */
@Service
@Transactional
public class InviteContactServiceImpl implements InviteContactService {

    private final Logger log = LoggerFactory.getLogger(InviteContactServiceImpl.class);

    private final InviteContactRepository inviteContactRepository;

    private final InviteContactSearchRepository inviteContactSearchRepository;

    public InviteContactServiceImpl(InviteContactRepository inviteContactRepository, InviteContactSearchRepository inviteContactSearchRepository) {
        this.inviteContactRepository = inviteContactRepository;
        this.inviteContactSearchRepository = inviteContactSearchRepository;
    }

    /**
     * Save a inviteContact.
     *
     * @param inviteContact the entity to save
     * @return the persisted entity
     */
    @Override
    public InviteContact save(InviteContact inviteContact) {
        log.debug("Request to save InviteContact : {}", inviteContact);
        InviteContact result = inviteContactRepository.save(inviteContact);
        inviteContactSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the inviteContacts.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InviteContact> findAll() {
        log.debug("Request to get all InviteContacts");
        return inviteContactRepository.findAll();
    }


    /**
     * Get one inviteContact by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InviteContact> findOne(Long id) {
        log.debug("Request to get InviteContact : {}", id);
        return inviteContactRepository.findById(id);
    }

    /**
     * Delete the inviteContact by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InviteContact : {}", id);
        inviteContactRepository.deleteById(id);
        inviteContactSearchRepository.deleteById(id);
    }

    /**
     * Search for the inviteContact corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InviteContact> search(String query) {
        log.debug("Request to search InviteContacts for query {}", query);
        return StreamSupport
            .stream(inviteContactSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

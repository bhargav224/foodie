package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.InviteEmailService;
import com.foodiechef.api.domain.InviteEmail;
import com.foodiechef.api.repository.InviteEmailRepository;
import com.foodiechef.api.repository.search.InviteEmailSearchRepository;
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
 * Service Implementation for managing InviteEmail.
 */
@Service
@Transactional
public class InviteEmailServiceImpl implements InviteEmailService {

    private final Logger log = LoggerFactory.getLogger(InviteEmailServiceImpl.class);

    private final InviteEmailRepository inviteEmailRepository;

    private final InviteEmailSearchRepository inviteEmailSearchRepository;

    public InviteEmailServiceImpl(InviteEmailRepository inviteEmailRepository, InviteEmailSearchRepository inviteEmailSearchRepository) {
        this.inviteEmailRepository = inviteEmailRepository;
        this.inviteEmailSearchRepository = inviteEmailSearchRepository;
    }

    /**
     * Save a inviteEmail.
     *
     * @param inviteEmail the entity to save
     * @return the persisted entity
     */
    @Override
    public InviteEmail save(InviteEmail inviteEmail) {
        log.debug("Request to save InviteEmail : {}", inviteEmail);
        InviteEmail result = inviteEmailRepository.save(inviteEmail);
        inviteEmailSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the inviteEmails.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InviteEmail> findAll() {
        log.debug("Request to get all InviteEmails");
        return inviteEmailRepository.findAll();
    }


    /**
     * Get one inviteEmail by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InviteEmail> findOne(Long id) {
        log.debug("Request to get InviteEmail : {}", id);
        return inviteEmailRepository.findById(id);
    }

    /**
     * Delete the inviteEmail by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InviteEmail : {}", id);
        inviteEmailRepository.deleteById(id);
        inviteEmailSearchRepository.deleteById(id);
    }

    /**
     * Search for the inviteEmail corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InviteEmail> search(String query) {
        log.debug("Request to search InviteEmails for query {}", query);
        return StreamSupport
            .stream(inviteEmailSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

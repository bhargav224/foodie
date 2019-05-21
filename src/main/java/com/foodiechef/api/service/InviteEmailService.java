package com.foodiechef.api.service;

import com.foodiechef.api.domain.InviteEmail;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing InviteEmail.
 */
public interface InviteEmailService {

    /**
     * Save a inviteEmail.
     *
     * @param inviteEmail the entity to save
     * @return the persisted entity
     */
    InviteEmail save(InviteEmail inviteEmail);

    /**
     * Get all the inviteEmails.
     *
     * @return the list of entities
     */
    List<InviteEmail> findAll();


    /**
     * Get the "id" inviteEmail.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InviteEmail> findOne(Long id);

    /**
     * Delete the "id" inviteEmail.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the inviteEmail corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<InviteEmail> search(String query);
}

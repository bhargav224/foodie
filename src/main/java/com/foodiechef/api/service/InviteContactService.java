package com.foodiechef.api.service;

import com.foodiechef.api.domain.InviteContact;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing InviteContact.
 */
public interface InviteContactService {

    /**
     * Save a inviteContact.
     *
     * @param inviteContact the entity to save
     * @return the persisted entity
     */
    InviteContact save(InviteContact inviteContact);

    /**
     * Get all the inviteContacts.
     *
     * @return the list of entities
     */
    List<InviteContact> findAll();


    /**
     * Get the "id" inviteContact.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InviteContact> findOne(Long id);

    /**
     * Delete the "id" inviteContact.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the inviteContact corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<InviteContact> search(String query);
}

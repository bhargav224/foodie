package com.foodiechef.api.service;

import com.foodiechef.api.domain.ChefProfile;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ChefProfile.
 */
public interface ChefProfileService {

    /**
     * Save a chefProfile.
     *
     * @param chefProfile the entity to save
     * @return the persisted entity
     */
    ChefProfile save(ChefProfile chefProfile);

    /**
     * Get all the chefProfiles.
     *
     * @return the list of entities
     */
    List<ChefProfile> findAll();
    /**
     * Get all the ChefProfileDTO where UserInfo is null.
     *
     * @return the list of entities
     */
    List<ChefProfile> findAllWhereUserInfoIsNull();


    /**
     * Get the "id" chefProfile.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ChefProfile> findOne(Long id);

    /**
     * Delete the "id" chefProfile.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the chefProfile corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ChefProfile> search(String query);
}

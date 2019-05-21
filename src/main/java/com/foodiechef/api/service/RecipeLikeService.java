package com.foodiechef.api.service;

import com.foodiechef.api.domain.RecipeLike;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RecipeLike.
 */
public interface RecipeLikeService {

    /**
     * Save a recipeLike.
     *
     * @param recipeLike the entity to save
     * @return the persisted entity
     */
    RecipeLike save(RecipeLike recipeLike);

    /**
     * Get all the recipeLikes.
     *
     * @return the list of entities
     */
    List<RecipeLike> findAll();


    /**
     * Get the "id" recipeLike.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RecipeLike> findOne(Long id);

    /**
     * Delete the "id" recipeLike.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recipeLike corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RecipeLike> search(String query);
}

package com.foodiechef.api.service;

import com.foodiechef.api.domain.UserRating;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing UserRating.
 */
public interface UserRatingService {

    /**
     * Save a userRating.
     *
     * @param userRating the entity to save
     * @return the persisted entity
     */
    UserRating save(UserRating userRating);

    /**
     * Get all the userRatings.
     *
     * @return the list of entities
     */
    List<UserRating> findAll();


    /**
     * Get the "id" userRating.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserRating> findOne(Long id);

    /**
     * Delete the "id" userRating.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userRating corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<UserRating> search(String query);
}

package com.foodiechef.api.service;

import com.foodiechef.api.domain.RecipeRating;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RecipeRating.
 */
public interface RecipeRatingService {

    /**
     * Save a recipeRating.
     *
     * @param recipeRating the entity to save
     * @return the persisted entity
     */
    RecipeRating save(RecipeRating recipeRating);

    /**
     * Get all the recipeRatings.
     *
     * @return the list of entities
     */
    List<RecipeRating> findAll();


    /**
     * Get the "id" recipeRating.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RecipeRating> findOne(Long id);

    /**
     * Delete the "id" recipeRating.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recipeRating corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RecipeRating> search(String query);
}

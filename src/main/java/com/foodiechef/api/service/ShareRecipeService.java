package com.foodiechef.api.service;

import com.foodiechef.api.domain.ShareRecipe;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ShareRecipe.
 */
public interface ShareRecipeService {

    /**
     * Save a shareRecipe.
     *
     * @param shareRecipe the entity to save
     * @return the persisted entity
     */
    ShareRecipe save(ShareRecipe shareRecipe);

    /**
     * Get all the shareRecipes.
     *
     * @return the list of entities
     */
    List<ShareRecipe> findAll();


    /**
     * Get the "id" shareRecipe.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ShareRecipe> findOne(Long id);

    /**
     * Delete the "id" shareRecipe.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the shareRecipe corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ShareRecipe> search(String query);
}

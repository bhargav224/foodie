package com.foodiechef.api.service;

import com.foodiechef.api.domain.Recipe;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Recipe.
 */
public interface RecipeService {

    /**
     * Save a recipe.
     *
     * @param recipe the entity to save
     * @return the persisted entity
     */
    Recipe save(Recipe recipe);

    /**
     * Get all the recipes.
     *
     * @return the list of entities
     */
    List<Recipe> findAll();


    /**
     * Get the "id" recipe.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Recipe> findOne(Long id);

    /**
     * Delete the "id" recipe.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recipe corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Recipe> search(String query);
}

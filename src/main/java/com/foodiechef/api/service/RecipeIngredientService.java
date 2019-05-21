package com.foodiechef.api.service;

import com.foodiechef.api.domain.RecipeIngredient;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RecipeIngredient.
 */
public interface RecipeIngredientService {

    /**
     * Save a recipeIngredient.
     *
     * @param recipeIngredient the entity to save
     * @return the persisted entity
     */
    RecipeIngredient save(RecipeIngredient recipeIngredient);

    /**
     * Get all the recipeIngredients.
     *
     * @return the list of entities
     */
    List<RecipeIngredient> findAll();


    /**
     * Get the "id" recipeIngredient.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RecipeIngredient> findOne(Long id);

    /**
     * Delete the "id" recipeIngredient.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recipeIngredient corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RecipeIngredient> search(String query);
}

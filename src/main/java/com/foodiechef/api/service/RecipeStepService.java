package com.foodiechef.api.service;

import com.foodiechef.api.domain.RecipeStep;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RecipeStep.
 */
public interface RecipeStepService {

    /**
     * Save a recipeStep.
     *
     * @param recipeStep the entity to save
     * @return the persisted entity
     */
    RecipeStep save(RecipeStep recipeStep);

    /**
     * Get all the recipeSteps.
     *
     * @return the list of entities
     */
    List<RecipeStep> findAll();


    /**
     * Get the "id" recipeStep.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RecipeStep> findOne(Long id);

    /**
     * Delete the "id" recipeStep.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recipeStep corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RecipeStep> search(String query);
}

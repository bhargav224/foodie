package com.foodiechef.api.service;

import com.foodiechef.api.domain.RecipeHasStep;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RecipeHasStep.
 */
public interface RecipeHasStepService {

    /**
     * Save a recipeHasStep.
     *
     * @param recipeHasStep the entity to save
     * @return the persisted entity
     */
    RecipeHasStep save(RecipeHasStep recipeHasStep);

    /**
     * Get all the recipeHasSteps.
     *
     * @return the list of entities
     */
    List<RecipeHasStep> findAll();


    /**
     * Get the "id" recipeHasStep.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RecipeHasStep> findOne(Long id);

    /**
     * Delete the "id" recipeHasStep.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recipeHasStep corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RecipeHasStep> search(String query);
}

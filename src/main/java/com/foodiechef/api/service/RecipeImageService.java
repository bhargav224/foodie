package com.foodiechef.api.service;

import com.foodiechef.api.domain.RecipeImage;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RecipeImage.
 */
public interface RecipeImageService {

    /**
     * Save a recipeImage.
     *
     * @param recipeImage the entity to save
     * @return the persisted entity
     */
    RecipeImage save(RecipeImage recipeImage);

    /**
     * Get all the recipeImages.
     *
     * @return the list of entities
     */
    List<RecipeImage> findAll();


    /**
     * Get the "id" recipeImage.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RecipeImage> findOne(Long id);

    /**
     * Delete the "id" recipeImage.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recipeImage corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RecipeImage> search(String query);
}

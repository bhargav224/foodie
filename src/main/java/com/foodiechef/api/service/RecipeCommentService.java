package com.foodiechef.api.service;

import com.foodiechef.api.domain.RecipeComment;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RecipeComment.
 */
public interface RecipeCommentService {

    /**
     * Save a recipeComment.
     *
     * @param recipeComment the entity to save
     * @return the persisted entity
     */
    RecipeComment save(RecipeComment recipeComment);

    /**
     * Get all the recipeComments.
     *
     * @return the list of entities
     */
    List<RecipeComment> findAll();


    /**
     * Get the "id" recipeComment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RecipeComment> findOne(Long id);

    /**
     * Delete the "id" recipeComment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the recipeComment corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RecipeComment> search(String query);
}

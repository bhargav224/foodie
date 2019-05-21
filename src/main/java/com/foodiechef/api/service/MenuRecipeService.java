package com.foodiechef.api.service;

import com.foodiechef.api.domain.MenuRecipe;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing MenuRecipe.
 */
public interface MenuRecipeService {

    /**
     * Save a menuRecipe.
     *
     * @param menuRecipe the entity to save
     * @return the persisted entity
     */
    MenuRecipe save(MenuRecipe menuRecipe);

    /**
     * Get all the menuRecipes.
     *
     * @return the list of entities
     */
    List<MenuRecipe> findAll();


    /**
     * Get the "id" menuRecipe.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MenuRecipe> findOne(Long id);

    /**
     * Delete the "id" menuRecipe.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the menuRecipe corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<MenuRecipe> search(String query);
}

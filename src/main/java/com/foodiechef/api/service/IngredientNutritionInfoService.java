package com.foodiechef.api.service;

import com.foodiechef.api.domain.IngredientNutritionInfo;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing IngredientNutritionInfo.
 */
public interface IngredientNutritionInfoService {

    /**
     * Save a ingredientNutritionInfo.
     *
     * @param ingredientNutritionInfo the entity to save
     * @return the persisted entity
     */
    IngredientNutritionInfo save(IngredientNutritionInfo ingredientNutritionInfo);

    /**
     * Get all the ingredientNutritionInfos.
     *
     * @return the list of entities
     */
    List<IngredientNutritionInfo> findAll();


    /**
     * Get the "id" ingredientNutritionInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IngredientNutritionInfo> findOne(Long id);

    /**
     * Delete the "id" ingredientNutritionInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ingredientNutritionInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<IngredientNutritionInfo> search(String query);
}

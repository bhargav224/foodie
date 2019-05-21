package com.foodiechef.api.service;

import com.foodiechef.api.domain.FoodCategorie;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing FoodCategorie.
 */
public interface FoodCategorieService {

    /**
     * Save a foodCategorie.
     *
     * @param foodCategorie the entity to save
     * @return the persisted entity
     */
    FoodCategorie save(FoodCategorie foodCategorie);

    /**
     * Get all the foodCategories.
     *
     * @return the list of entities
     */
    List<FoodCategorie> findAll();


    /**
     * Get the "id" foodCategorie.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FoodCategorie> findOne(Long id);

    /**
     * Delete the "id" foodCategorie.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the foodCategorie corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<FoodCategorie> search(String query);
}

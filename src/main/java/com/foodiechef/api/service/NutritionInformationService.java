package com.foodiechef.api.service;

import com.foodiechef.api.domain.NutritionInformation;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing NutritionInformation.
 */
public interface NutritionInformationService {

    /**
     * Save a nutritionInformation.
     *
     * @param nutritionInformation the entity to save
     * @return the persisted entity
     */
    NutritionInformation save(NutritionInformation nutritionInformation);

    /**
     * Get all the nutritionInformations.
     *
     * @return the list of entities
     */
    List<NutritionInformation> findAll();


    /**
     * Get the "id" nutritionInformation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<NutritionInformation> findOne(Long id);

    /**
     * Delete the "id" nutritionInformation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the nutritionInformation corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<NutritionInformation> search(String query);
}

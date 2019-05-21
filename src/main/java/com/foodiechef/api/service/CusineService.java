package com.foodiechef.api.service;

import com.foodiechef.api.domain.Cusine;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Cusine.
 */
public interface CusineService {

    /**
     * Save a cusine.
     *
     * @param cusine the entity to save
     * @return the persisted entity
     */
    Cusine save(Cusine cusine);

    /**
     * Get all the cusines.
     *
     * @return the list of entities
     */
    List<Cusine> findAll();


    /**
     * Get the "id" cusine.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Cusine> findOne(Long id);

    /**
     * Delete the "id" cusine.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cusine corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Cusine> search(String query);
}

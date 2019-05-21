package com.foodiechef.api.service;

import com.foodiechef.api.domain.RestaurantMenu;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RestaurantMenu.
 */
public interface RestaurantMenuService {

    /**
     * Save a restaurantMenu.
     *
     * @param restaurantMenu the entity to save
     * @return the persisted entity
     */
    RestaurantMenu save(RestaurantMenu restaurantMenu);

    /**
     * Get all the restaurantMenus.
     *
     * @return the list of entities
     */
    List<RestaurantMenu> findAll();


    /**
     * Get the "id" restaurantMenu.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RestaurantMenu> findOne(Long id);

    /**
     * Delete the "id" restaurantMenu.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the restaurantMenu corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RestaurantMenu> search(String query);
}

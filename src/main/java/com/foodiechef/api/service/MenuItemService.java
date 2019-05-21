package com.foodiechef.api.service;

import com.foodiechef.api.domain.MenuItem;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing MenuItem.
 */
public interface MenuItemService {

    /**
     * Save a menuItem.
     *
     * @param menuItem the entity to save
     * @return the persisted entity
     */
    MenuItem save(MenuItem menuItem);

    /**
     * Get all the menuItems.
     *
     * @return the list of entities
     */
    List<MenuItem> findAll();


    /**
     * Get the "id" menuItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MenuItem> findOne(Long id);

    /**
     * Delete the "id" menuItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the menuItem corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<MenuItem> search(String query);
}

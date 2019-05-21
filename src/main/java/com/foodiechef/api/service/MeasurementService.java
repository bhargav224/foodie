package com.foodiechef.api.service;

import com.foodiechef.api.domain.Measurement;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Measurement.
 */
public interface MeasurementService {

    /**
     * Save a measurement.
     *
     * @param measurement the entity to save
     * @return the persisted entity
     */
    Measurement save(Measurement measurement);

    /**
     * Get all the measurements.
     *
     * @return the list of entities
     */
    List<Measurement> findAll();


    /**
     * Get the "id" measurement.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Measurement> findOne(Long id);

    /**
     * Delete the "id" measurement.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the measurement corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Measurement> search(String query);
}

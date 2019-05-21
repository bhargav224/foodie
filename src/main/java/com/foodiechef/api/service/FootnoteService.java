package com.foodiechef.api.service;

import com.foodiechef.api.domain.Footnote;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Footnote.
 */
public interface FootnoteService {

    /**
     * Save a footnote.
     *
     * @param footnote the entity to save
     * @return the persisted entity
     */
    Footnote save(Footnote footnote);

    /**
     * Get all the footnotes.
     *
     * @return the list of entities
     */
    List<Footnote> findAll();


    /**
     * Get the "id" footnote.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Footnote> findOne(Long id);

    /**
     * Delete the "id" footnote.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the footnote corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Footnote> search(String query);
}

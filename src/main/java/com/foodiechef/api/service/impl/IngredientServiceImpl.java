package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.IngredientService;
import com.foodiechef.api.domain.Ingredient;
import com.foodiechef.api.repository.IngredientRepository;
import com.foodiechef.api.repository.search.IngredientSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ingredient.
 */
@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private final IngredientRepository ingredientRepository;

    private final IngredientSearchRepository ingredientSearchRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientSearchRepository ingredientSearchRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientSearchRepository = ingredientSearchRepository;
    }

    /**
     * Save a ingredient.
     *
     * @param ingredient the entity to save
     * @return the persisted entity
     */
    @Override
    public Ingredient save(Ingredient ingredient) {
        log.debug("Request to save Ingredient : {}", ingredient);
        Ingredient result = ingredientRepository.save(ingredient);
        ingredientSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ingredients.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> findAll() {
        log.debug("Request to get all Ingredients");
        return ingredientRepository.findAll();
    }


    /**
     * Get one ingredient by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ingredient> findOne(Long id) {
        log.debug("Request to get Ingredient : {}", id);
        return ingredientRepository.findById(id);
    }

    /**
     * Delete the ingredient by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ingredient : {}", id);
        ingredientRepository.deleteById(id);
        ingredientSearchRepository.deleteById(id);
    }

    /**
     * Search for the ingredient corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> search(String query) {
        log.debug("Request to search Ingredients for query {}", query);
        return StreamSupport
            .stream(ingredientSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

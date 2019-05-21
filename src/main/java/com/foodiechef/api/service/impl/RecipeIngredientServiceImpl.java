package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.RecipeIngredientService;
import com.foodiechef.api.domain.RecipeIngredient;
import com.foodiechef.api.repository.RecipeIngredientRepository;
import com.foodiechef.api.repository.search.RecipeIngredientSearchRepository;
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
 * Service Implementation for managing RecipeIngredient.
 */
@Service
@Transactional
public class RecipeIngredientServiceImpl implements RecipeIngredientService {

    private final Logger log = LoggerFactory.getLogger(RecipeIngredientServiceImpl.class);

    private final RecipeIngredientRepository recipeIngredientRepository;

    private final RecipeIngredientSearchRepository recipeIngredientSearchRepository;

    public RecipeIngredientServiceImpl(RecipeIngredientRepository recipeIngredientRepository, RecipeIngredientSearchRepository recipeIngredientSearchRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeIngredientSearchRepository = recipeIngredientSearchRepository;
    }

    /**
     * Save a recipeIngredient.
     *
     * @param recipeIngredient the entity to save
     * @return the persisted entity
     */
    @Override
    public RecipeIngredient save(RecipeIngredient recipeIngredient) {
        log.debug("Request to save RecipeIngredient : {}", recipeIngredient);
        RecipeIngredient result = recipeIngredientRepository.save(recipeIngredient);
        recipeIngredientSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the recipeIngredients.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeIngredient> findAll() {
        log.debug("Request to get all RecipeIngredients");
        return recipeIngredientRepository.findAll();
    }


    /**
     * Get one recipeIngredient by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeIngredient> findOne(Long id) {
        log.debug("Request to get RecipeIngredient : {}", id);
        return recipeIngredientRepository.findById(id);
    }

    /**
     * Delete the recipeIngredient by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecipeIngredient : {}", id);
        recipeIngredientRepository.deleteById(id);
        recipeIngredientSearchRepository.deleteById(id);
    }

    /**
     * Search for the recipeIngredient corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeIngredient> search(String query) {
        log.debug("Request to search RecipeIngredients for query {}", query);
        return StreamSupport
            .stream(recipeIngredientSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.RecipeStepService;
import com.foodiechef.api.domain.RecipeStep;
import com.foodiechef.api.repository.RecipeStepRepository;
import com.foodiechef.api.repository.search.RecipeStepSearchRepository;
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
 * Service Implementation for managing RecipeStep.
 */
@Service
@Transactional
public class RecipeStepServiceImpl implements RecipeStepService {

    private final Logger log = LoggerFactory.getLogger(RecipeStepServiceImpl.class);

    private final RecipeStepRepository recipeStepRepository;

    private final RecipeStepSearchRepository recipeStepSearchRepository;

    public RecipeStepServiceImpl(RecipeStepRepository recipeStepRepository, RecipeStepSearchRepository recipeStepSearchRepository) {
        this.recipeStepRepository = recipeStepRepository;
        this.recipeStepSearchRepository = recipeStepSearchRepository;
    }

    /**
     * Save a recipeStep.
     *
     * @param recipeStep the entity to save
     * @return the persisted entity
     */
    @Override
    public RecipeStep save(RecipeStep recipeStep) {
        log.debug("Request to save RecipeStep : {}", recipeStep);
        RecipeStep result = recipeStepRepository.save(recipeStep);
        recipeStepSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the recipeSteps.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeStep> findAll() {
        log.debug("Request to get all RecipeSteps");
        return recipeStepRepository.findAll();
    }


    /**
     * Get one recipeStep by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeStep> findOne(Long id) {
        log.debug("Request to get RecipeStep : {}", id);
        return recipeStepRepository.findById(id);
    }

    /**
     * Delete the recipeStep by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecipeStep : {}", id);
        recipeStepRepository.deleteById(id);
        recipeStepSearchRepository.deleteById(id);
    }

    /**
     * Search for the recipeStep corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeStep> search(String query) {
        log.debug("Request to search RecipeSteps for query {}", query);
        return StreamSupport
            .stream(recipeStepSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.RecipeHasStepService;
import com.foodiechef.api.domain.RecipeHasStep;
import com.foodiechef.api.repository.RecipeHasStepRepository;
import com.foodiechef.api.repository.search.RecipeHasStepSearchRepository;
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
 * Service Implementation for managing RecipeHasStep.
 */
@Service
@Transactional
public class RecipeHasStepServiceImpl implements RecipeHasStepService {

    private final Logger log = LoggerFactory.getLogger(RecipeHasStepServiceImpl.class);

    private final RecipeHasStepRepository recipeHasStepRepository;

    private final RecipeHasStepSearchRepository recipeHasStepSearchRepository;

    public RecipeHasStepServiceImpl(RecipeHasStepRepository recipeHasStepRepository, RecipeHasStepSearchRepository recipeHasStepSearchRepository) {
        this.recipeHasStepRepository = recipeHasStepRepository;
        this.recipeHasStepSearchRepository = recipeHasStepSearchRepository;
    }

    /**
     * Save a recipeHasStep.
     *
     * @param recipeHasStep the entity to save
     * @return the persisted entity
     */
    @Override
    public RecipeHasStep save(RecipeHasStep recipeHasStep) {
        log.debug("Request to save RecipeHasStep : {}", recipeHasStep);
        RecipeHasStep result = recipeHasStepRepository.save(recipeHasStep);
        recipeHasStepSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the recipeHasSteps.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeHasStep> findAll() {
        log.debug("Request to get all RecipeHasSteps");
        return recipeHasStepRepository.findAll();
    }


    /**
     * Get one recipeHasStep by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeHasStep> findOne(Long id) {
        log.debug("Request to get RecipeHasStep : {}", id);
        return recipeHasStepRepository.findById(id);
    }

    /**
     * Delete the recipeHasStep by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecipeHasStep : {}", id);
        recipeHasStepRepository.deleteById(id);
        recipeHasStepSearchRepository.deleteById(id);
    }

    /**
     * Search for the recipeHasStep corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeHasStep> search(String query) {
        log.debug("Request to search RecipeHasSteps for query {}", query);
        return StreamSupport
            .stream(recipeHasStepSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

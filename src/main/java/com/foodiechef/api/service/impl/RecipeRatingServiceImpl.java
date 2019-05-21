package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.RecipeRatingService;
import com.foodiechef.api.domain.RecipeRating;
import com.foodiechef.api.repository.RecipeRatingRepository;
import com.foodiechef.api.repository.search.RecipeRatingSearchRepository;
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
 * Service Implementation for managing RecipeRating.
 */
@Service
@Transactional
public class RecipeRatingServiceImpl implements RecipeRatingService {

    private final Logger log = LoggerFactory.getLogger(RecipeRatingServiceImpl.class);

    private final RecipeRatingRepository recipeRatingRepository;

    private final RecipeRatingSearchRepository recipeRatingSearchRepository;

    public RecipeRatingServiceImpl(RecipeRatingRepository recipeRatingRepository, RecipeRatingSearchRepository recipeRatingSearchRepository) {
        this.recipeRatingRepository = recipeRatingRepository;
        this.recipeRatingSearchRepository = recipeRatingSearchRepository;
    }

    /**
     * Save a recipeRating.
     *
     * @param recipeRating the entity to save
     * @return the persisted entity
     */
    @Override
    public RecipeRating save(RecipeRating recipeRating) {
        log.debug("Request to save RecipeRating : {}", recipeRating);
        RecipeRating result = recipeRatingRepository.save(recipeRating);
        recipeRatingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the recipeRatings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeRating> findAll() {
        log.debug("Request to get all RecipeRatings");
        return recipeRatingRepository.findAll();
    }


    /**
     * Get one recipeRating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeRating> findOne(Long id) {
        log.debug("Request to get RecipeRating : {}", id);
        return recipeRatingRepository.findById(id);
    }

    /**
     * Delete the recipeRating by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecipeRating : {}", id);
        recipeRatingRepository.deleteById(id);
        recipeRatingSearchRepository.deleteById(id);
    }

    /**
     * Search for the recipeRating corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeRating> search(String query) {
        log.debug("Request to search RecipeRatings for query {}", query);
        return StreamSupport
            .stream(recipeRatingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

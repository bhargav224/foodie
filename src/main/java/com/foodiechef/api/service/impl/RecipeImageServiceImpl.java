package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.RecipeImageService;
import com.foodiechef.api.domain.RecipeImage;
import com.foodiechef.api.repository.RecipeImageRepository;
import com.foodiechef.api.repository.search.RecipeImageSearchRepository;
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
 * Service Implementation for managing RecipeImage.
 */
@Service
@Transactional
public class RecipeImageServiceImpl implements RecipeImageService {

    private final Logger log = LoggerFactory.getLogger(RecipeImageServiceImpl.class);

    private final RecipeImageRepository recipeImageRepository;

    private final RecipeImageSearchRepository recipeImageSearchRepository;

    public RecipeImageServiceImpl(RecipeImageRepository recipeImageRepository, RecipeImageSearchRepository recipeImageSearchRepository) {
        this.recipeImageRepository = recipeImageRepository;
        this.recipeImageSearchRepository = recipeImageSearchRepository;
    }

    /**
     * Save a recipeImage.
     *
     * @param recipeImage the entity to save
     * @return the persisted entity
     */
    @Override
    public RecipeImage save(RecipeImage recipeImage) {
        log.debug("Request to save RecipeImage : {}", recipeImage);
        RecipeImage result = recipeImageRepository.save(recipeImage);
        recipeImageSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the recipeImages.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeImage> findAll() {
        log.debug("Request to get all RecipeImages");
        return recipeImageRepository.findAll();
    }


    /**
     * Get one recipeImage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeImage> findOne(Long id) {
        log.debug("Request to get RecipeImage : {}", id);
        return recipeImageRepository.findById(id);
    }

    /**
     * Delete the recipeImage by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecipeImage : {}", id);
        recipeImageRepository.deleteById(id);
        recipeImageSearchRepository.deleteById(id);
    }

    /**
     * Search for the recipeImage corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeImage> search(String query) {
        log.debug("Request to search RecipeImages for query {}", query);
        return StreamSupport
            .stream(recipeImageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

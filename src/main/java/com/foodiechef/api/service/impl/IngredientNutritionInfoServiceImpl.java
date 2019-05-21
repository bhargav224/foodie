package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.IngredientNutritionInfoService;
import com.foodiechef.api.domain.IngredientNutritionInfo;
import com.foodiechef.api.repository.IngredientNutritionInfoRepository;
import com.foodiechef.api.repository.search.IngredientNutritionInfoSearchRepository;
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
 * Service Implementation for managing IngredientNutritionInfo.
 */
@Service
@Transactional
public class IngredientNutritionInfoServiceImpl implements IngredientNutritionInfoService {

    private final Logger log = LoggerFactory.getLogger(IngredientNutritionInfoServiceImpl.class);

    private final IngredientNutritionInfoRepository ingredientNutritionInfoRepository;

    private final IngredientNutritionInfoSearchRepository ingredientNutritionInfoSearchRepository;

    public IngredientNutritionInfoServiceImpl(IngredientNutritionInfoRepository ingredientNutritionInfoRepository, IngredientNutritionInfoSearchRepository ingredientNutritionInfoSearchRepository) {
        this.ingredientNutritionInfoRepository = ingredientNutritionInfoRepository;
        this.ingredientNutritionInfoSearchRepository = ingredientNutritionInfoSearchRepository;
    }

    /**
     * Save a ingredientNutritionInfo.
     *
     * @param ingredientNutritionInfo the entity to save
     * @return the persisted entity
     */
    @Override
    public IngredientNutritionInfo save(IngredientNutritionInfo ingredientNutritionInfo) {
        log.debug("Request to save IngredientNutritionInfo : {}", ingredientNutritionInfo);
        IngredientNutritionInfo result = ingredientNutritionInfoRepository.save(ingredientNutritionInfo);
        ingredientNutritionInfoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ingredientNutritionInfos.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IngredientNutritionInfo> findAll() {
        log.debug("Request to get all IngredientNutritionInfos");
        return ingredientNutritionInfoRepository.findAll();
    }


    /**
     * Get one ingredientNutritionInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IngredientNutritionInfo> findOne(Long id) {
        log.debug("Request to get IngredientNutritionInfo : {}", id);
        return ingredientNutritionInfoRepository.findById(id);
    }

    /**
     * Delete the ingredientNutritionInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IngredientNutritionInfo : {}", id);
        ingredientNutritionInfoRepository.deleteById(id);
        ingredientNutritionInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the ingredientNutritionInfo corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IngredientNutritionInfo> search(String query) {
        log.debug("Request to search IngredientNutritionInfos for query {}", query);
        return StreamSupport
            .stream(ingredientNutritionInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

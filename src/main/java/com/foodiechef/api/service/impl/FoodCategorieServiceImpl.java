package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.FoodCategorieService;
import com.foodiechef.api.domain.FoodCategorie;
import com.foodiechef.api.repository.FoodCategorieRepository;
import com.foodiechef.api.repository.search.FoodCategorieSearchRepository;
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
 * Service Implementation for managing FoodCategorie.
 */
@Service
@Transactional
public class FoodCategorieServiceImpl implements FoodCategorieService {

    private final Logger log = LoggerFactory.getLogger(FoodCategorieServiceImpl.class);

    private final FoodCategorieRepository foodCategorieRepository;

    private final FoodCategorieSearchRepository foodCategorieSearchRepository;

    public FoodCategorieServiceImpl(FoodCategorieRepository foodCategorieRepository, FoodCategorieSearchRepository foodCategorieSearchRepository) {
        this.foodCategorieRepository = foodCategorieRepository;
        this.foodCategorieSearchRepository = foodCategorieSearchRepository;
    }

    /**
     * Save a foodCategorie.
     *
     * @param foodCategorie the entity to save
     * @return the persisted entity
     */
    @Override
    public FoodCategorie save(FoodCategorie foodCategorie) {
        log.debug("Request to save FoodCategorie : {}", foodCategorie);
        FoodCategorie result = foodCategorieRepository.save(foodCategorie);
        foodCategorieSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the foodCategories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FoodCategorie> findAll() {
        log.debug("Request to get all FoodCategories");
        return foodCategorieRepository.findAll();
    }


    /**
     * Get one foodCategorie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FoodCategorie> findOne(Long id) {
        log.debug("Request to get FoodCategorie : {}", id);
        return foodCategorieRepository.findById(id);
    }

    /**
     * Delete the foodCategorie by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FoodCategorie : {}", id);
        foodCategorieRepository.deleteById(id);
        foodCategorieSearchRepository.deleteById(id);
    }

    /**
     * Search for the foodCategorie corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FoodCategorie> search(String query) {
        log.debug("Request to search FoodCategories for query {}", query);
        return StreamSupport
            .stream(foodCategorieSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

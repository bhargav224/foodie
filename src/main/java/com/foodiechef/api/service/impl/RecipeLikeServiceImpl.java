package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.RecipeLikeService;
import com.foodiechef.api.domain.RecipeLike;
import com.foodiechef.api.repository.RecipeLikeRepository;
import com.foodiechef.api.repository.search.RecipeLikeSearchRepository;
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
 * Service Implementation for managing RecipeLike.
 */
@Service
@Transactional
public class RecipeLikeServiceImpl implements RecipeLikeService {

    private final Logger log = LoggerFactory.getLogger(RecipeLikeServiceImpl.class);

    private final RecipeLikeRepository recipeLikeRepository;

    private final RecipeLikeSearchRepository recipeLikeSearchRepository;

    public RecipeLikeServiceImpl(RecipeLikeRepository recipeLikeRepository, RecipeLikeSearchRepository recipeLikeSearchRepository) {
        this.recipeLikeRepository = recipeLikeRepository;
        this.recipeLikeSearchRepository = recipeLikeSearchRepository;
    }

    /**
     * Save a recipeLike.
     *
     * @param recipeLike the entity to save
     * @return the persisted entity
     */
    @Override
    public RecipeLike save(RecipeLike recipeLike) {
        log.debug("Request to save RecipeLike : {}", recipeLike);
        RecipeLike result = recipeLikeRepository.save(recipeLike);
        recipeLikeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the recipeLikes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeLike> findAll() {
        log.debug("Request to get all RecipeLikes");
        return recipeLikeRepository.findAll();
    }


    /**
     * Get one recipeLike by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeLike> findOne(Long id) {
        log.debug("Request to get RecipeLike : {}", id);
        return recipeLikeRepository.findById(id);
    }

    /**
     * Delete the recipeLike by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecipeLike : {}", id);
        recipeLikeRepository.deleteById(id);
        recipeLikeSearchRepository.deleteById(id);
    }

    /**
     * Search for the recipeLike corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeLike> search(String query) {
        log.debug("Request to search RecipeLikes for query {}", query);
        return StreamSupport
            .stream(recipeLikeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

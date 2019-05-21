package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.RecipeCommentService;
import com.foodiechef.api.domain.RecipeComment;
import com.foodiechef.api.repository.RecipeCommentRepository;
import com.foodiechef.api.repository.search.RecipeCommentSearchRepository;
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
 * Service Implementation for managing RecipeComment.
 */
@Service
@Transactional
public class RecipeCommentServiceImpl implements RecipeCommentService {

    private final Logger log = LoggerFactory.getLogger(RecipeCommentServiceImpl.class);

    private final RecipeCommentRepository recipeCommentRepository;

    private final RecipeCommentSearchRepository recipeCommentSearchRepository;

    public RecipeCommentServiceImpl(RecipeCommentRepository recipeCommentRepository, RecipeCommentSearchRepository recipeCommentSearchRepository) {
        this.recipeCommentRepository = recipeCommentRepository;
        this.recipeCommentSearchRepository = recipeCommentSearchRepository;
    }

    /**
     * Save a recipeComment.
     *
     * @param recipeComment the entity to save
     * @return the persisted entity
     */
    @Override
    public RecipeComment save(RecipeComment recipeComment) {
        log.debug("Request to save RecipeComment : {}", recipeComment);
        RecipeComment result = recipeCommentRepository.save(recipeComment);
        recipeCommentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the recipeComments.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeComment> findAll() {
        log.debug("Request to get all RecipeComments");
        return recipeCommentRepository.findAll();
    }


    /**
     * Get one recipeComment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeComment> findOne(Long id) {
        log.debug("Request to get RecipeComment : {}", id);
        return recipeCommentRepository.findById(id);
    }

    /**
     * Delete the recipeComment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecipeComment : {}", id);
        recipeCommentRepository.deleteById(id);
        recipeCommentSearchRepository.deleteById(id);
    }

    /**
     * Search for the recipeComment corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeComment> search(String query) {
        log.debug("Request to search RecipeComments for query {}", query);
        return StreamSupport
            .stream(recipeCommentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

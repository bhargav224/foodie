package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.ShareRecipeService;
import com.foodiechef.api.domain.ShareRecipe;
import com.foodiechef.api.repository.ShareRecipeRepository;
import com.foodiechef.api.repository.search.ShareRecipeSearchRepository;
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
 * Service Implementation for managing ShareRecipe.
 */
@Service
@Transactional
public class ShareRecipeServiceImpl implements ShareRecipeService {

    private final Logger log = LoggerFactory.getLogger(ShareRecipeServiceImpl.class);

    private final ShareRecipeRepository shareRecipeRepository;

    private final ShareRecipeSearchRepository shareRecipeSearchRepository;

    public ShareRecipeServiceImpl(ShareRecipeRepository shareRecipeRepository, ShareRecipeSearchRepository shareRecipeSearchRepository) {
        this.shareRecipeRepository = shareRecipeRepository;
        this.shareRecipeSearchRepository = shareRecipeSearchRepository;
    }

    /**
     * Save a shareRecipe.
     *
     * @param shareRecipe the entity to save
     * @return the persisted entity
     */
    @Override
    public ShareRecipe save(ShareRecipe shareRecipe) {
        log.debug("Request to save ShareRecipe : {}", shareRecipe);
        ShareRecipe result = shareRecipeRepository.save(shareRecipe);
        shareRecipeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the shareRecipes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShareRecipe> findAll() {
        log.debug("Request to get all ShareRecipes");
        return shareRecipeRepository.findAll();
    }


    /**
     * Get one shareRecipe by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ShareRecipe> findOne(Long id) {
        log.debug("Request to get ShareRecipe : {}", id);
        return shareRecipeRepository.findById(id);
    }

    /**
     * Delete the shareRecipe by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShareRecipe : {}", id);
        shareRecipeRepository.deleteById(id);
        shareRecipeSearchRepository.deleteById(id);
    }

    /**
     * Search for the shareRecipe corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShareRecipe> search(String query) {
        log.debug("Request to search ShareRecipes for query {}", query);
        return StreamSupport
            .stream(shareRecipeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

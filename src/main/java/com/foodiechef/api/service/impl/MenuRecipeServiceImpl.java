package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.MenuRecipeService;
import com.foodiechef.api.domain.MenuRecipe;
import com.foodiechef.api.repository.MenuRecipeRepository;
import com.foodiechef.api.repository.search.MenuRecipeSearchRepository;
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
 * Service Implementation for managing MenuRecipe.
 */
@Service
@Transactional
public class MenuRecipeServiceImpl implements MenuRecipeService {

    private final Logger log = LoggerFactory.getLogger(MenuRecipeServiceImpl.class);

    private final MenuRecipeRepository menuRecipeRepository;

    private final MenuRecipeSearchRepository menuRecipeSearchRepository;

    public MenuRecipeServiceImpl(MenuRecipeRepository menuRecipeRepository, MenuRecipeSearchRepository menuRecipeSearchRepository) {
        this.menuRecipeRepository = menuRecipeRepository;
        this.menuRecipeSearchRepository = menuRecipeSearchRepository;
    }

    /**
     * Save a menuRecipe.
     *
     * @param menuRecipe the entity to save
     * @return the persisted entity
     */
    @Override
    public MenuRecipe save(MenuRecipe menuRecipe) {
        log.debug("Request to save MenuRecipe : {}", menuRecipe);
        MenuRecipe result = menuRecipeRepository.save(menuRecipe);
        menuRecipeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the menuRecipes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuRecipe> findAll() {
        log.debug("Request to get all MenuRecipes");
        return menuRecipeRepository.findAll();
    }


    /**
     * Get one menuRecipe by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MenuRecipe> findOne(Long id) {
        log.debug("Request to get MenuRecipe : {}", id);
        return menuRecipeRepository.findById(id);
    }

    /**
     * Delete the menuRecipe by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MenuRecipe : {}", id);
        menuRecipeRepository.deleteById(id);
        menuRecipeSearchRepository.deleteById(id);
    }

    /**
     * Search for the menuRecipe corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuRecipe> search(String query) {
        log.debug("Request to search MenuRecipes for query {}", query);
        return StreamSupport
            .stream(menuRecipeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

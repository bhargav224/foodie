package com.foodiechef.api.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.foodiechef.api.domain.MenuRecipe;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.MenuRecipeRepository;
import com.foodiechef.api.repository.search.MenuRecipeSearchRepository;
import com.foodiechef.api.service.dto.MenuRecipeCriteria;

/**
 * Service for executing complex queries for MenuRecipe entities in the database.
 * The main input is a {@link MenuRecipeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuRecipe} or a {@link Page} of {@link MenuRecipe} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuRecipeQueryService extends QueryService<MenuRecipe> {

    private final Logger log = LoggerFactory.getLogger(MenuRecipeQueryService.class);

    private final MenuRecipeRepository menuRecipeRepository;

    private final MenuRecipeSearchRepository menuRecipeSearchRepository;

    public MenuRecipeQueryService(MenuRecipeRepository menuRecipeRepository, MenuRecipeSearchRepository menuRecipeSearchRepository) {
        this.menuRecipeRepository = menuRecipeRepository;
        this.menuRecipeSearchRepository = menuRecipeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MenuRecipe} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuRecipe> findByCriteria(MenuRecipeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MenuRecipe> specification = createSpecification(criteria);
        return menuRecipeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MenuRecipe} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuRecipe> findByCriteria(MenuRecipeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MenuRecipe> specification = createSpecification(criteria);
        return menuRecipeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuRecipeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MenuRecipe> specification = createSpecification(criteria);
        return menuRecipeRepository.count(specification);
    }

    /**
     * Function to convert MenuRecipeCriteria to a {@link Specification}
     */
    private Specification<MenuRecipe> createSpecification(MenuRecipeCriteria criteria) {
        Specification<MenuRecipe> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MenuRecipe_.id));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(MenuRecipe_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getMenuItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuItemId(),
                    root -> root.join(MenuRecipe_.menuItem, JoinType.LEFT).get(MenuItem_.id)));
            }
        }
        return specification;
    }
}

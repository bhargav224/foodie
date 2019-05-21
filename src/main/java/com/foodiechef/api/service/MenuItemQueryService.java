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

import com.foodiechef.api.domain.MenuItem;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.MenuItemRepository;
import com.foodiechef.api.repository.search.MenuItemSearchRepository;
import com.foodiechef.api.service.dto.MenuItemCriteria;

/**
 * Service for executing complex queries for MenuItem entities in the database.
 * The main input is a {@link MenuItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuItem} or a {@link Page} of {@link MenuItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuItemQueryService extends QueryService<MenuItem> {

    private final Logger log = LoggerFactory.getLogger(MenuItemQueryService.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemSearchRepository menuItemSearchRepository;

    public MenuItemQueryService(MenuItemRepository menuItemRepository, MenuItemSearchRepository menuItemSearchRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemSearchRepository = menuItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MenuItem} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuItem> findByCriteria(MenuItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MenuItem} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuItem> findByCriteria(MenuItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemRepository.count(specification);
    }

    /**
     * Function to convert MenuItemCriteria to a {@link Specification}
     */
    private Specification<MenuItem> createSpecification(MenuItemCriteria criteria) {
        Specification<MenuItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MenuItem_.id));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), MenuItem_.active));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MenuItem_.description));
            }
            if (criteria.getImagePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagePath(), MenuItem_.imagePath));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MenuItem_.name));
            }
            if (criteria.getRestaurantMenuId() != null) {
                specification = specification.and(buildSpecification(criteria.getRestaurantMenuId(),
                    root -> root.join(MenuItem_.restaurantMenus, JoinType.LEFT).get(RestaurantMenu_.id)));
            }
            if (criteria.getMenuRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuRecipeId(),
                    root -> root.join(MenuItem_.menuRecipes, JoinType.LEFT).get(MenuRecipe_.id)));
            }
            if (criteria.getFoodCategorieId() != null) {
                specification = specification.and(buildSpecification(criteria.getFoodCategorieId(),
                    root -> root.join(MenuItem_.foodCategorie, JoinType.LEFT).get(FoodCategorie_.id)));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(MenuItem_.course, JoinType.LEFT).get(Course_.id)));
            }
            if (criteria.getCusineId() != null) {
                specification = specification.and(buildSpecification(criteria.getCusineId(),
                    root -> root.join(MenuItem_.cusine, JoinType.LEFT).get(Cusine_.id)));
            }
        }
        return specification;
    }
}

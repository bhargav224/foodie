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

import com.foodiechef.api.domain.FoodCategorie;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.FoodCategorieRepository;
import com.foodiechef.api.repository.search.FoodCategorieSearchRepository;
import com.foodiechef.api.service.dto.FoodCategorieCriteria;

/**
 * Service for executing complex queries for FoodCategorie entities in the database.
 * The main input is a {@link FoodCategorieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FoodCategorie} or a {@link Page} of {@link FoodCategorie} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FoodCategorieQueryService extends QueryService<FoodCategorie> {

    private final Logger log = LoggerFactory.getLogger(FoodCategorieQueryService.class);

    private final FoodCategorieRepository foodCategorieRepository;

    private final FoodCategorieSearchRepository foodCategorieSearchRepository;

    public FoodCategorieQueryService(FoodCategorieRepository foodCategorieRepository, FoodCategorieSearchRepository foodCategorieSearchRepository) {
        this.foodCategorieRepository = foodCategorieRepository;
        this.foodCategorieSearchRepository = foodCategorieSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FoodCategorie} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FoodCategorie> findByCriteria(FoodCategorieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FoodCategorie> specification = createSpecification(criteria);
        return foodCategorieRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FoodCategorie} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FoodCategorie> findByCriteria(FoodCategorieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FoodCategorie> specification = createSpecification(criteria);
        return foodCategorieRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FoodCategorieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FoodCategorie> specification = createSpecification(criteria);
        return foodCategorieRepository.count(specification);
    }

    /**
     * Function to convert FoodCategorieCriteria to a {@link Specification}
     */
    private Specification<FoodCategorie> createSpecification(FoodCategorieCriteria criteria) {
        Specification<FoodCategorie> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FoodCategorie_.id));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), FoodCategorie_.active));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), FoodCategorie_.category));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(FoodCategorie_.recipes, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getMenuItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuItemId(),
                    root -> root.join(FoodCategorie_.menuItems, JoinType.LEFT).get(MenuItem_.id)));
            }
        }
        return specification;
    }
}

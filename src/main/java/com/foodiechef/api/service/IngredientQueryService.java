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

import com.foodiechef.api.domain.Ingredient;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.IngredientRepository;
import com.foodiechef.api.repository.search.IngredientSearchRepository;
import com.foodiechef.api.service.dto.IngredientCriteria;

/**
 * Service for executing complex queries for Ingredient entities in the database.
 * The main input is a {@link IngredientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ingredient} or a {@link Page} of {@link Ingredient} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IngredientQueryService extends QueryService<Ingredient> {

    private final Logger log = LoggerFactory.getLogger(IngredientQueryService.class);

    private final IngredientRepository ingredientRepository;

    private final IngredientSearchRepository ingredientSearchRepository;

    public IngredientQueryService(IngredientRepository ingredientRepository, IngredientSearchRepository ingredientSearchRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientSearchRepository = ingredientSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Ingredient} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ingredient> findByCriteria(IngredientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ingredient> specification = createSpecification(criteria);
        return ingredientRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ingredient} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ingredient> findByCriteria(IngredientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ingredient> specification = createSpecification(criteria);
        return ingredientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IngredientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ingredient> specification = createSpecification(criteria);
        return ingredientRepository.count(specification);
    }

    /**
     * Function to convert IngredientCriteria to a {@link Specification}
     */
    private Specification<Ingredient> createSpecification(IngredientCriteria criteria) {
        Specification<Ingredient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Ingredient_.id));
            }
            if (criteria.getIngredient() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIngredient(), Ingredient_.ingredient));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Ingredient_.active));
            }
            if (criteria.getRecipeIngredientId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeIngredientId(),
                    root -> root.join(Ingredient_.recipeIngredients, JoinType.LEFT).get(RecipeIngredient_.id)));
            }
            if (criteria.getIngredientNutritionInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getIngredientNutritionInfoId(),
                    root -> root.join(Ingredient_.ingredientNutritionInfos, JoinType.LEFT).get(IngredientNutritionInfo_.id)));
            }
        }
        return specification;
    }
}

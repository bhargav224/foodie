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

import com.foodiechef.api.domain.RecipeIngredient;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RecipeIngredientRepository;
import com.foodiechef.api.repository.search.RecipeIngredientSearchRepository;
import com.foodiechef.api.service.dto.RecipeIngredientCriteria;

/**
 * Service for executing complex queries for RecipeIngredient entities in the database.
 * The main input is a {@link RecipeIngredientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RecipeIngredient} or a {@link Page} of {@link RecipeIngredient} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeIngredientQueryService extends QueryService<RecipeIngredient> {

    private final Logger log = LoggerFactory.getLogger(RecipeIngredientQueryService.class);

    private final RecipeIngredientRepository recipeIngredientRepository;

    private final RecipeIngredientSearchRepository recipeIngredientSearchRepository;

    public RecipeIngredientQueryService(RecipeIngredientRepository recipeIngredientRepository, RecipeIngredientSearchRepository recipeIngredientSearchRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeIngredientSearchRepository = recipeIngredientSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RecipeIngredient} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeIngredient> findByCriteria(RecipeIngredientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RecipeIngredient> specification = createSpecification(criteria);
        return recipeIngredientRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RecipeIngredient} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeIngredient> findByCriteria(RecipeIngredientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecipeIngredient> specification = createSpecification(criteria);
        return recipeIngredientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipeIngredientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RecipeIngredient> specification = createSpecification(criteria);
        return recipeIngredientRepository.count(specification);
    }

    /**
     * Function to convert RecipeIngredientCriteria to a {@link Specification}
     */
    private Specification<RecipeIngredient> createSpecification(RecipeIngredientCriteria criteria) {
        Specification<RecipeIngredient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RecipeIngredient_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), RecipeIngredient_.amount));
            }
            if (criteria.getIngredientNutritionValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIngredientNutritionValue(), RecipeIngredient_.ingredientNutritionValue));
            }
            if (criteria.getRestriction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRestriction(), RecipeIngredient_.restriction));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(RecipeIngredient_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getMeasurementId() != null) {
                specification = specification.and(buildSpecification(criteria.getMeasurementId(),
                    root -> root.join(RecipeIngredient_.measurement, JoinType.LEFT).get(Measurement_.id)));
            }
            if (criteria.getIngredientId() != null) {
                specification = specification.and(buildSpecification(criteria.getIngredientId(),
                    root -> root.join(RecipeIngredient_.ingredient, JoinType.LEFT).get(Ingredient_.id)));
            }
        }
        return specification;
    }
}

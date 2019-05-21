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

import com.foodiechef.api.domain.Recipe;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RecipeRepository;
import com.foodiechef.api.repository.search.RecipeSearchRepository;
import com.foodiechef.api.service.dto.RecipeCriteria;

/**
 * Service for executing complex queries for Recipe entities in the database.
 * The main input is a {@link RecipeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Recipe} or a {@link Page} of {@link Recipe} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeQueryService extends QueryService<Recipe> {

    private final Logger log = LoggerFactory.getLogger(RecipeQueryService.class);

    private final RecipeRepository recipeRepository;

    private final RecipeSearchRepository recipeSearchRepository;

    public RecipeQueryService(RecipeRepository recipeRepository, RecipeSearchRepository recipeSearchRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeSearchRepository = recipeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Recipe} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Recipe> findByCriteria(RecipeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Recipe> specification = createSpecification(criteria);
        return recipeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Recipe} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Recipe> findByCriteria(RecipeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Recipe> specification = createSpecification(criteria);
        return recipeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Recipe> specification = createSpecification(criteria);
        return recipeRepository.count(specification);
    }

    /**
     * Function to convert RecipeCriteria to a {@link Specification}
     */
    private Specification<Recipe> createSpecification(RecipeCriteria criteria) {
        Specification<Recipe> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Recipe_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Recipe_.amount));
            }
            if (criteria.getCaloriesPerServings() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCaloriesPerServings(), Recipe_.caloriesPerServings));
            }
            if (criteria.getCookTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCookTime(), Recipe_.cookTime));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Recipe_.description));
            }
            if (criteria.getPrepTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrepTime(), Recipe_.prepTime));
            }
            if (criteria.getPublished() != null) {
                specification = specification.and(buildSpecification(criteria.getPublished(), Recipe_.published));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Recipe_.rating));
            }
            if (criteria.getReadyIn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReadyIn(), Recipe_.readyIn));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Recipe_.title));
            }
            if (criteria.getVideo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideo(), Recipe_.video));
            }
            if (criteria.getYields() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYields(), Recipe_.yields));
            }
            if (criteria.getRecipeHasStepId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeHasStepId(),
                    root -> root.join(Recipe_.recipeHasSteps, JoinType.LEFT).get(RecipeHasStep_.id)));
            }
            if (criteria.getRecipeImageId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeImageId(),
                    root -> root.join(Recipe_.recipeImages, JoinType.LEFT).get(RecipeImage_.id)));
            }
            if (criteria.getMenuRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuRecipeId(),
                    root -> root.join(Recipe_.menuRecipes, JoinType.LEFT).get(MenuRecipe_.id)));
            }
            if (criteria.getRecipeCommentId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeCommentId(),
                    root -> root.join(Recipe_.recipeComments, JoinType.LEFT).get(RecipeComment_.id)));
            }
            if (criteria.getRecipeRatingId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeRatingId(),
                    root -> root.join(Recipe_.recipeRatings, JoinType.LEFT).get(RecipeRating_.id)));
            }
            if (criteria.getShareRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getShareRecipeId(),
                    root -> root.join(Recipe_.shareRecipes, JoinType.LEFT).get(ShareRecipe_.id)));
            }
            if (criteria.getRecipeLikeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeLikeId(),
                    root -> root.join(Recipe_.recipeLikes, JoinType.LEFT).get(RecipeLike_.id)));
            }
            if (criteria.getFootnoteId() != null) {
                specification = specification.and(buildSpecification(criteria.getFootnoteId(),
                    root -> root.join(Recipe_.footnotes, JoinType.LEFT).get(Footnote_.id)));
            }
            if (criteria.getRecipeIngredientId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeIngredientId(),
                    root -> root.join(Recipe_.recipeIngredients, JoinType.LEFT).get(RecipeIngredient_.id)));
            }
            if (criteria.getLevelId() != null) {
                specification = specification.and(buildSpecification(criteria.getLevelId(),
                    root -> root.join(Recipe_.level, JoinType.LEFT).get(Level_.id)));
            }
            if (criteria.getFoodCategorieId() != null) {
                specification = specification.and(buildSpecification(criteria.getFoodCategorieId(),
                    root -> root.join(Recipe_.foodCategorie, JoinType.LEFT).get(FoodCategorie_.id)));
            }
            if (criteria.getCusineId() != null) {
                specification = specification.and(buildSpecification(criteria.getCusineId(),
                    root -> root.join(Recipe_.cusine, JoinType.LEFT).get(Cusine_.id)));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(Recipe_.course, JoinType.LEFT).get(Course_.id)));
            }
        }
        return specification;
    }
}

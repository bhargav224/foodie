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

import com.foodiechef.api.domain.RecipeRating;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RecipeRatingRepository;
import com.foodiechef.api.repository.search.RecipeRatingSearchRepository;
import com.foodiechef.api.service.dto.RecipeRatingCriteria;

/**
 * Service for executing complex queries for RecipeRating entities in the database.
 * The main input is a {@link RecipeRatingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RecipeRating} or a {@link Page} of {@link RecipeRating} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeRatingQueryService extends QueryService<RecipeRating> {

    private final Logger log = LoggerFactory.getLogger(RecipeRatingQueryService.class);

    private final RecipeRatingRepository recipeRatingRepository;

    private final RecipeRatingSearchRepository recipeRatingSearchRepository;

    public RecipeRatingQueryService(RecipeRatingRepository recipeRatingRepository, RecipeRatingSearchRepository recipeRatingSearchRepository) {
        this.recipeRatingRepository = recipeRatingRepository;
        this.recipeRatingSearchRepository = recipeRatingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RecipeRating} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeRating> findByCriteria(RecipeRatingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RecipeRating> specification = createSpecification(criteria);
        return recipeRatingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RecipeRating} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeRating> findByCriteria(RecipeRatingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecipeRating> specification = createSpecification(criteria);
        return recipeRatingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipeRatingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RecipeRating> specification = createSpecification(criteria);
        return recipeRatingRepository.count(specification);
    }

    /**
     * Function to convert RecipeRatingCriteria to a {@link Specification}
     */
    private Specification<RecipeRating> createSpecification(RecipeRatingCriteria criteria) {
        Specification<RecipeRating> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RecipeRating_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), RecipeRating_.date));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), RecipeRating_.rating));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(RecipeRating_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getUserInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoId(),
                    root -> root.join(RecipeRating_.userInfo, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

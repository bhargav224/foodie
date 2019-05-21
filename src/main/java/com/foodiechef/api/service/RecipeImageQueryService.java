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

import com.foodiechef.api.domain.RecipeImage;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RecipeImageRepository;
import com.foodiechef.api.repository.search.RecipeImageSearchRepository;
import com.foodiechef.api.service.dto.RecipeImageCriteria;

/**
 * Service for executing complex queries for RecipeImage entities in the database.
 * The main input is a {@link RecipeImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RecipeImage} or a {@link Page} of {@link RecipeImage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeImageQueryService extends QueryService<RecipeImage> {

    private final Logger log = LoggerFactory.getLogger(RecipeImageQueryService.class);

    private final RecipeImageRepository recipeImageRepository;

    private final RecipeImageSearchRepository recipeImageSearchRepository;

    public RecipeImageQueryService(RecipeImageRepository recipeImageRepository, RecipeImageSearchRepository recipeImageSearchRepository) {
        this.recipeImageRepository = recipeImageRepository;
        this.recipeImageSearchRepository = recipeImageSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RecipeImage} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeImage> findByCriteria(RecipeImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RecipeImage> specification = createSpecification(criteria);
        return recipeImageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RecipeImage} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeImage> findByCriteria(RecipeImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecipeImage> specification = createSpecification(criteria);
        return recipeImageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipeImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RecipeImage> specification = createSpecification(criteria);
        return recipeImageRepository.count(specification);
    }

    /**
     * Function to convert RecipeImageCriteria to a {@link Specification}
     */
    private Specification<RecipeImage> createSpecification(RecipeImageCriteria criteria) {
        Specification<RecipeImage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RecipeImage_.id));
            }
            if (criteria.getImagePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagePath(), RecipeImage_.imagePath));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(RecipeImage_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
        }
        return specification;
    }
}

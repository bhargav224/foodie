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

import com.foodiechef.api.domain.RecipeHasStep;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RecipeHasStepRepository;
import com.foodiechef.api.repository.search.RecipeHasStepSearchRepository;
import com.foodiechef.api.service.dto.RecipeHasStepCriteria;

/**
 * Service for executing complex queries for RecipeHasStep entities in the database.
 * The main input is a {@link RecipeHasStepCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RecipeHasStep} or a {@link Page} of {@link RecipeHasStep} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeHasStepQueryService extends QueryService<RecipeHasStep> {

    private final Logger log = LoggerFactory.getLogger(RecipeHasStepQueryService.class);

    private final RecipeHasStepRepository recipeHasStepRepository;

    private final RecipeHasStepSearchRepository recipeHasStepSearchRepository;

    public RecipeHasStepQueryService(RecipeHasStepRepository recipeHasStepRepository, RecipeHasStepSearchRepository recipeHasStepSearchRepository) {
        this.recipeHasStepRepository = recipeHasStepRepository;
        this.recipeHasStepSearchRepository = recipeHasStepSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RecipeHasStep} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeHasStep> findByCriteria(RecipeHasStepCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RecipeHasStep> specification = createSpecification(criteria);
        return recipeHasStepRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RecipeHasStep} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeHasStep> findByCriteria(RecipeHasStepCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecipeHasStep> specification = createSpecification(criteria);
        return recipeHasStepRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipeHasStepCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RecipeHasStep> specification = createSpecification(criteria);
        return recipeHasStepRepository.count(specification);
    }

    /**
     * Function to convert RecipeHasStepCriteria to a {@link Specification}
     */
    private Specification<RecipeHasStep> createSpecification(RecipeHasStepCriteria criteria) {
        Specification<RecipeHasStep> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RecipeHasStep_.id));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(RecipeHasStep_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getRecipeStepId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeStepId(),
                    root -> root.join(RecipeHasStep_.recipeStep, JoinType.LEFT).get(RecipeStep_.id)));
            }
        }
        return specification;
    }
}

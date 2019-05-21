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

import com.foodiechef.api.domain.RecipeStep;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RecipeStepRepository;
import com.foodiechef.api.repository.search.RecipeStepSearchRepository;
import com.foodiechef.api.service.dto.RecipeStepCriteria;

/**
 * Service for executing complex queries for RecipeStep entities in the database.
 * The main input is a {@link RecipeStepCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RecipeStep} or a {@link Page} of {@link RecipeStep} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeStepQueryService extends QueryService<RecipeStep> {

    private final Logger log = LoggerFactory.getLogger(RecipeStepQueryService.class);

    private final RecipeStepRepository recipeStepRepository;

    private final RecipeStepSearchRepository recipeStepSearchRepository;

    public RecipeStepQueryService(RecipeStepRepository recipeStepRepository, RecipeStepSearchRepository recipeStepSearchRepository) {
        this.recipeStepRepository = recipeStepRepository;
        this.recipeStepSearchRepository = recipeStepSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RecipeStep} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeStep> findByCriteria(RecipeStepCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RecipeStep> specification = createSpecification(criteria);
        return recipeStepRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RecipeStep} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeStep> findByCriteria(RecipeStepCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecipeStep> specification = createSpecification(criteria);
        return recipeStepRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipeStepCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RecipeStep> specification = createSpecification(criteria);
        return recipeStepRepository.count(specification);
    }

    /**
     * Function to convert RecipeStepCriteria to a {@link Specification}
     */
    private Specification<RecipeStep> createSpecification(RecipeStepCriteria criteria) {
        Specification<RecipeStep> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RecipeStep_.id));
            }
            if (criteria.getInstruction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInstruction(), RecipeStep_.instruction));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RecipeStep_.name));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), RecipeStep_.path));
            }
            if (criteria.getRecipeHasStepId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeHasStepId(),
                    root -> root.join(RecipeStep_.recipeHasSteps, JoinType.LEFT).get(RecipeHasStep_.id)));
            }
        }
        return specification;
    }
}

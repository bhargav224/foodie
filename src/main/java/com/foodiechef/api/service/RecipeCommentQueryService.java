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

import com.foodiechef.api.domain.RecipeComment;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RecipeCommentRepository;
import com.foodiechef.api.repository.search.RecipeCommentSearchRepository;
import com.foodiechef.api.service.dto.RecipeCommentCriteria;

/**
 * Service for executing complex queries for RecipeComment entities in the database.
 * The main input is a {@link RecipeCommentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RecipeComment} or a {@link Page} of {@link RecipeComment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeCommentQueryService extends QueryService<RecipeComment> {

    private final Logger log = LoggerFactory.getLogger(RecipeCommentQueryService.class);

    private final RecipeCommentRepository recipeCommentRepository;

    private final RecipeCommentSearchRepository recipeCommentSearchRepository;

    public RecipeCommentQueryService(RecipeCommentRepository recipeCommentRepository, RecipeCommentSearchRepository recipeCommentSearchRepository) {
        this.recipeCommentRepository = recipeCommentRepository;
        this.recipeCommentSearchRepository = recipeCommentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RecipeComment} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeComment> findByCriteria(RecipeCommentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RecipeComment> specification = createSpecification(criteria);
        return recipeCommentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RecipeComment} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeComment> findByCriteria(RecipeCommentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecipeComment> specification = createSpecification(criteria);
        return recipeCommentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipeCommentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RecipeComment> specification = createSpecification(criteria);
        return recipeCommentRepository.count(specification);
    }

    /**
     * Function to convert RecipeCommentCriteria to a {@link Specification}
     */
    private Specification<RecipeComment> createSpecification(RecipeCommentCriteria criteria) {
        Specification<RecipeComment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RecipeComment_.id));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), RecipeComment_.comment));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), RecipeComment_.date));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(RecipeComment_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getUserInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoId(),
                    root -> root.join(RecipeComment_.userInfo, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

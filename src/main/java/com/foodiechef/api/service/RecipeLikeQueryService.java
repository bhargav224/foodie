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

import com.foodiechef.api.domain.RecipeLike;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RecipeLikeRepository;
import com.foodiechef.api.repository.search.RecipeLikeSearchRepository;
import com.foodiechef.api.service.dto.RecipeLikeCriteria;

/**
 * Service for executing complex queries for RecipeLike entities in the database.
 * The main input is a {@link RecipeLikeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RecipeLike} or a {@link Page} of {@link RecipeLike} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeLikeQueryService extends QueryService<RecipeLike> {

    private final Logger log = LoggerFactory.getLogger(RecipeLikeQueryService.class);

    private final RecipeLikeRepository recipeLikeRepository;

    private final RecipeLikeSearchRepository recipeLikeSearchRepository;

    public RecipeLikeQueryService(RecipeLikeRepository recipeLikeRepository, RecipeLikeSearchRepository recipeLikeSearchRepository) {
        this.recipeLikeRepository = recipeLikeRepository;
        this.recipeLikeSearchRepository = recipeLikeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RecipeLike} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RecipeLike> findByCriteria(RecipeLikeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RecipeLike> specification = createSpecification(criteria);
        return recipeLikeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RecipeLike} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipeLike> findByCriteria(RecipeLikeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RecipeLike> specification = createSpecification(criteria);
        return recipeLikeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipeLikeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RecipeLike> specification = createSpecification(criteria);
        return recipeLikeRepository.count(specification);
    }

    /**
     * Function to convert RecipeLikeCriteria to a {@link Specification}
     */
    private Specification<RecipeLike> createSpecification(RecipeLikeCriteria criteria) {
        Specification<RecipeLike> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RecipeLike_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), RecipeLike_.date));
            }
            if (criteria.getLikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLikes(), RecipeLike_.likes));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(RecipeLike_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getUserInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoId(),
                    root -> root.join(RecipeLike_.userInfo, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

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

import com.foodiechef.api.domain.ShareRecipe;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.ShareRecipeRepository;
import com.foodiechef.api.repository.search.ShareRecipeSearchRepository;
import com.foodiechef.api.service.dto.ShareRecipeCriteria;

/**
 * Service for executing complex queries for ShareRecipe entities in the database.
 * The main input is a {@link ShareRecipeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShareRecipe} or a {@link Page} of {@link ShareRecipe} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShareRecipeQueryService extends QueryService<ShareRecipe> {

    private final Logger log = LoggerFactory.getLogger(ShareRecipeQueryService.class);

    private final ShareRecipeRepository shareRecipeRepository;

    private final ShareRecipeSearchRepository shareRecipeSearchRepository;

    public ShareRecipeQueryService(ShareRecipeRepository shareRecipeRepository, ShareRecipeSearchRepository shareRecipeSearchRepository) {
        this.shareRecipeRepository = shareRecipeRepository;
        this.shareRecipeSearchRepository = shareRecipeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ShareRecipe} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShareRecipe> findByCriteria(ShareRecipeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShareRecipe> specification = createSpecification(criteria);
        return shareRecipeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ShareRecipe} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShareRecipe> findByCriteria(ShareRecipeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShareRecipe> specification = createSpecification(criteria);
        return shareRecipeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShareRecipeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShareRecipe> specification = createSpecification(criteria);
        return shareRecipeRepository.count(specification);
    }

    /**
     * Function to convert ShareRecipeCriteria to a {@link Specification}
     */
    private Specification<ShareRecipe> createSpecification(ShareRecipeCriteria criteria) {
        Specification<ShareRecipe> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ShareRecipe_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ShareRecipe_.date));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(ShareRecipe_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getSharedById() != null) {
                specification = specification.and(buildSpecification(criteria.getSharedById(),
                    root -> root.join(ShareRecipe_.sharedBy, JoinType.LEFT).get(UserInfo_.id)));
            }
            if (criteria.getSharedToId() != null) {
                specification = specification.and(buildSpecification(criteria.getSharedToId(),
                    root -> root.join(ShareRecipe_.sharedTo, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

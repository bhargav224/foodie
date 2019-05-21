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

import com.foodiechef.api.domain.Level;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.LevelRepository;
import com.foodiechef.api.repository.search.LevelSearchRepository;
import com.foodiechef.api.service.dto.LevelCriteria;

/**
 * Service for executing complex queries for Level entities in the database.
 * The main input is a {@link LevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Level} or a {@link Page} of {@link Level} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LevelQueryService extends QueryService<Level> {

    private final Logger log = LoggerFactory.getLogger(LevelQueryService.class);

    private final LevelRepository levelRepository;

    private final LevelSearchRepository levelSearchRepository;

    public LevelQueryService(LevelRepository levelRepository, LevelSearchRepository levelSearchRepository) {
        this.levelRepository = levelRepository;
        this.levelSearchRepository = levelSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Level} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Level> findByCriteria(LevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Level> specification = createSpecification(criteria);
        return levelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Level} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Level> findByCriteria(LevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Level> specification = createSpecification(criteria);
        return levelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LevelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Level> specification = createSpecification(criteria);
        return levelRepository.count(specification);
    }

    /**
     * Function to convert LevelCriteria to a {@link Specification}
     */
    private Specification<Level> createSpecification(LevelCriteria criteria) {
        Specification<Level> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Level_.id));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Level_.active));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Level_.description));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(Level_.recipes, JoinType.LEFT).get(Recipe_.id)));
            }
        }
        return specification;
    }
}

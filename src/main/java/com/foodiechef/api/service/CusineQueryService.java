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

import com.foodiechef.api.domain.Cusine;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.CusineRepository;
import com.foodiechef.api.repository.search.CusineSearchRepository;
import com.foodiechef.api.service.dto.CusineCriteria;

/**
 * Service for executing complex queries for Cusine entities in the database.
 * The main input is a {@link CusineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cusine} or a {@link Page} of {@link Cusine} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CusineQueryService extends QueryService<Cusine> {

    private final Logger log = LoggerFactory.getLogger(CusineQueryService.class);

    private final CusineRepository cusineRepository;

    private final CusineSearchRepository cusineSearchRepository;

    public CusineQueryService(CusineRepository cusineRepository, CusineSearchRepository cusineSearchRepository) {
        this.cusineRepository = cusineRepository;
        this.cusineSearchRepository = cusineSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Cusine} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cusine> findByCriteria(CusineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cusine> specification = createSpecification(criteria);
        return cusineRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cusine} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cusine> findByCriteria(CusineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cusine> specification = createSpecification(criteria);
        return cusineRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CusineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cusine> specification = createSpecification(criteria);
        return cusineRepository.count(specification);
    }

    /**
     * Function to convert CusineCriteria to a {@link Specification}
     */
    private Specification<Cusine> createSpecification(CusineCriteria criteria) {
        Specification<Cusine> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Cusine_.id));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Cusine_.active));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Cusine_.description));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Cusine_.name));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(Cusine_.recipes, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getMenuItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuItemId(),
                    root -> root.join(Cusine_.menuItems, JoinType.LEFT).get(MenuItem_.id)));
            }
        }
        return specification;
    }
}

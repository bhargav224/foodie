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

import com.foodiechef.api.domain.RestaurantMenu;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.RestaurantMenuRepository;
import com.foodiechef.api.repository.search.RestaurantMenuSearchRepository;
import com.foodiechef.api.service.dto.RestaurantMenuCriteria;

/**
 * Service for executing complex queries for RestaurantMenu entities in the database.
 * The main input is a {@link RestaurantMenuCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RestaurantMenu} or a {@link Page} of {@link RestaurantMenu} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RestaurantMenuQueryService extends QueryService<RestaurantMenu> {

    private final Logger log = LoggerFactory.getLogger(RestaurantMenuQueryService.class);

    private final RestaurantMenuRepository restaurantMenuRepository;

    private final RestaurantMenuSearchRepository restaurantMenuSearchRepository;

    public RestaurantMenuQueryService(RestaurantMenuRepository restaurantMenuRepository, RestaurantMenuSearchRepository restaurantMenuSearchRepository) {
        this.restaurantMenuRepository = restaurantMenuRepository;
        this.restaurantMenuSearchRepository = restaurantMenuSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RestaurantMenu} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantMenu> findByCriteria(RestaurantMenuCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RestaurantMenu> specification = createSpecification(criteria);
        return restaurantMenuRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RestaurantMenu} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RestaurantMenu> findByCriteria(RestaurantMenuCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RestaurantMenu> specification = createSpecification(criteria);
        return restaurantMenuRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RestaurantMenuCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RestaurantMenu> specification = createSpecification(criteria);
        return restaurantMenuRepository.count(specification);
    }

    /**
     * Function to convert RestaurantMenuCriteria to a {@link Specification}
     */
    private Specification<RestaurantMenu> createSpecification(RestaurantMenuCriteria criteria) {
        Specification<RestaurantMenu> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RestaurantMenu_.id));
            }
            if (criteria.getMenuItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuItemId(),
                    root -> root.join(RestaurantMenu_.menuItem, JoinType.LEFT).get(MenuItem_.id)));
            }
            if (criteria.getRestaurantId() != null) {
                specification = specification.and(buildSpecification(criteria.getRestaurantId(),
                    root -> root.join(RestaurantMenu_.restaurant, JoinType.LEFT).get(Restaurant_.id)));
            }
        }
        return specification;
    }
}

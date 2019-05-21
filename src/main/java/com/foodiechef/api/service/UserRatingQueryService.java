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

import com.foodiechef.api.domain.UserRating;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.UserRatingRepository;
import com.foodiechef.api.repository.search.UserRatingSearchRepository;
import com.foodiechef.api.service.dto.UserRatingCriteria;

/**
 * Service for executing complex queries for UserRating entities in the database.
 * The main input is a {@link UserRatingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserRating} or a {@link Page} of {@link UserRating} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserRatingQueryService extends QueryService<UserRating> {

    private final Logger log = LoggerFactory.getLogger(UserRatingQueryService.class);

    private final UserRatingRepository userRatingRepository;

    private final UserRatingSearchRepository userRatingSearchRepository;

    public UserRatingQueryService(UserRatingRepository userRatingRepository, UserRatingSearchRepository userRatingSearchRepository) {
        this.userRatingRepository = userRatingRepository;
        this.userRatingSearchRepository = userRatingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link UserRating} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserRating> findByCriteria(UserRatingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserRating> specification = createSpecification(criteria);
        return userRatingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserRating} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserRating> findByCriteria(UserRatingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserRating> specification = createSpecification(criteria);
        return userRatingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserRatingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserRating> specification = createSpecification(criteria);
        return userRatingRepository.count(specification);
    }

    /**
     * Function to convert UserRatingCriteria to a {@link Specification}
     */
    private Specification<UserRating> createSpecification(UserRatingCriteria criteria) {
        Specification<UserRating> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserRating_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), UserRating_.date));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), UserRating_.rating));
            }
            if (criteria.getRateByUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getRateByUserId(),
                    root -> root.join(UserRating_.rateByUser, JoinType.LEFT).get(UserInfo_.id)));
            }
            if (criteria.getRateToUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getRateToUserId(),
                    root -> root.join(UserRating_.rateToUser, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

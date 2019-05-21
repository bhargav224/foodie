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

import com.foodiechef.api.domain.ChefProfile;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.ChefProfileRepository;
import com.foodiechef.api.repository.search.ChefProfileSearchRepository;
import com.foodiechef.api.service.dto.ChefProfileCriteria;

/**
 * Service for executing complex queries for ChefProfile entities in the database.
 * The main input is a {@link ChefProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChefProfile} or a {@link Page} of {@link ChefProfile} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChefProfileQueryService extends QueryService<ChefProfile> {

    private final Logger log = LoggerFactory.getLogger(ChefProfileQueryService.class);

    private final ChefProfileRepository chefProfileRepository;

    private final ChefProfileSearchRepository chefProfileSearchRepository;

    public ChefProfileQueryService(ChefProfileRepository chefProfileRepository, ChefProfileSearchRepository chefProfileSearchRepository) {
        this.chefProfileRepository = chefProfileRepository;
        this.chefProfileSearchRepository = chefProfileSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ChefProfile} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChefProfile> findByCriteria(ChefProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChefProfile> specification = createSpecification(criteria);
        return chefProfileRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ChefProfile} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChefProfile> findByCriteria(ChefProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChefProfile> specification = createSpecification(criteria);
        return chefProfileRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChefProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChefProfile> specification = createSpecification(criteria);
        return chefProfileRepository.count(specification);
    }

    /**
     * Function to convert ChefProfileCriteria to a {@link Specification}
     */
    private Specification<ChefProfile> createSpecification(ChefProfileCriteria criteria) {
        Specification<ChefProfile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChefProfile_.id));
            }
            if (criteria.getPhoto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoto(), ChefProfile_.photo));
            }
            if (criteria.getSpeciality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpeciality(), ChefProfile_.speciality));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ChefProfile_.type));
            }
            if (criteria.getWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsite(), ChefProfile_.website));
            }
            if (criteria.getUserInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoId(),
                    root -> root.join(ChefProfile_.userInfo, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

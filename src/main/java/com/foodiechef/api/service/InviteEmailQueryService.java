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

import com.foodiechef.api.domain.InviteEmail;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.InviteEmailRepository;
import com.foodiechef.api.repository.search.InviteEmailSearchRepository;
import com.foodiechef.api.service.dto.InviteEmailCriteria;

/**
 * Service for executing complex queries for InviteEmail entities in the database.
 * The main input is a {@link InviteEmailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InviteEmail} or a {@link Page} of {@link InviteEmail} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InviteEmailQueryService extends QueryService<InviteEmail> {

    private final Logger log = LoggerFactory.getLogger(InviteEmailQueryService.class);

    private final InviteEmailRepository inviteEmailRepository;

    private final InviteEmailSearchRepository inviteEmailSearchRepository;

    public InviteEmailQueryService(InviteEmailRepository inviteEmailRepository, InviteEmailSearchRepository inviteEmailSearchRepository) {
        this.inviteEmailRepository = inviteEmailRepository;
        this.inviteEmailSearchRepository = inviteEmailSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InviteEmail} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InviteEmail> findByCriteria(InviteEmailCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InviteEmail> specification = createSpecification(criteria);
        return inviteEmailRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InviteEmail} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InviteEmail> findByCriteria(InviteEmailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InviteEmail> specification = createSpecification(criteria);
        return inviteEmailRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InviteEmailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InviteEmail> specification = createSpecification(criteria);
        return inviteEmailRepository.count(specification);
    }

    /**
     * Function to convert InviteEmailCriteria to a {@link Specification}
     */
    private Specification<InviteEmail> createSpecification(InviteEmailCriteria criteria) {
        Specification<InviteEmail> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InviteEmail_.id));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), InviteEmail_.email));
            }
            if (criteria.getUserInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoId(),
                    root -> root.join(InviteEmail_.userInfo, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

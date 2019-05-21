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

import com.foodiechef.api.domain.InviteContact;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.InviteContactRepository;
import com.foodiechef.api.repository.search.InviteContactSearchRepository;
import com.foodiechef.api.service.dto.InviteContactCriteria;

/**
 * Service for executing complex queries for InviteContact entities in the database.
 * The main input is a {@link InviteContactCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InviteContact} or a {@link Page} of {@link InviteContact} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InviteContactQueryService extends QueryService<InviteContact> {

    private final Logger log = LoggerFactory.getLogger(InviteContactQueryService.class);

    private final InviteContactRepository inviteContactRepository;

    private final InviteContactSearchRepository inviteContactSearchRepository;

    public InviteContactQueryService(InviteContactRepository inviteContactRepository, InviteContactSearchRepository inviteContactSearchRepository) {
        this.inviteContactRepository = inviteContactRepository;
        this.inviteContactSearchRepository = inviteContactSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InviteContact} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InviteContact> findByCriteria(InviteContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InviteContact> specification = createSpecification(criteria);
        return inviteContactRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InviteContact} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InviteContact> findByCriteria(InviteContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InviteContact> specification = createSpecification(criteria);
        return inviteContactRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InviteContactCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InviteContact> specification = createSpecification(criteria);
        return inviteContactRepository.count(specification);
    }

    /**
     * Function to convert InviteContactCriteria to a {@link Specification}
     */
    private Specification<InviteContact> createSpecification(InviteContactCriteria criteria) {
        Specification<InviteContact> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InviteContact_.id));
            }
            if (criteria.getContact() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContact(), InviteContact_.contact));
            }
            if (criteria.getUserInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoId(),
                    root -> root.join(InviteContact_.userInfo, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

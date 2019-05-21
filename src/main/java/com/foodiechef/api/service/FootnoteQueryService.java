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

import com.foodiechef.api.domain.Footnote;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.FootnoteRepository;
import com.foodiechef.api.repository.search.FootnoteSearchRepository;
import com.foodiechef.api.service.dto.FootnoteCriteria;

/**
 * Service for executing complex queries for Footnote entities in the database.
 * The main input is a {@link FootnoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Footnote} or a {@link Page} of {@link Footnote} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FootnoteQueryService extends QueryService<Footnote> {

    private final Logger log = LoggerFactory.getLogger(FootnoteQueryService.class);

    private final FootnoteRepository footnoteRepository;

    private final FootnoteSearchRepository footnoteSearchRepository;

    public FootnoteQueryService(FootnoteRepository footnoteRepository, FootnoteSearchRepository footnoteSearchRepository) {
        this.footnoteRepository = footnoteRepository;
        this.footnoteSearchRepository = footnoteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Footnote} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Footnote> findByCriteria(FootnoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Footnote> specification = createSpecification(criteria);
        return footnoteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Footnote} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Footnote> findByCriteria(FootnoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Footnote> specification = createSpecification(criteria);
        return footnoteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FootnoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Footnote> specification = createSpecification(criteria);
        return footnoteRepository.count(specification);
    }

    /**
     * Function to convert FootnoteCriteria to a {@link Specification}
     */
    private Specification<Footnote> createSpecification(FootnoteCriteria criteria) {
        Specification<Footnote> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Footnote_.id));
            }
            if (criteria.getFootnote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFootnote(), Footnote_.footnote));
            }
            if (criteria.getRecipeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeId(),
                    root -> root.join(Footnote_.recipe, JoinType.LEFT).get(Recipe_.id)));
            }
            if (criteria.getUserInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoId(),
                    root -> root.join(Footnote_.userInfo, JoinType.LEFT).get(UserInfo_.id)));
            }
        }
        return specification;
    }
}

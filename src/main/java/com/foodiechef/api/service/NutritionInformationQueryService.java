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

import com.foodiechef.api.domain.NutritionInformation;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.NutritionInformationRepository;
import com.foodiechef.api.repository.search.NutritionInformationSearchRepository;
import com.foodiechef.api.service.dto.NutritionInformationCriteria;

/**
 * Service for executing complex queries for NutritionInformation entities in the database.
 * The main input is a {@link NutritionInformationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NutritionInformation} or a {@link Page} of {@link NutritionInformation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NutritionInformationQueryService extends QueryService<NutritionInformation> {

    private final Logger log = LoggerFactory.getLogger(NutritionInformationQueryService.class);

    private final NutritionInformationRepository nutritionInformationRepository;

    private final NutritionInformationSearchRepository nutritionInformationSearchRepository;

    public NutritionInformationQueryService(NutritionInformationRepository nutritionInformationRepository, NutritionInformationSearchRepository nutritionInformationSearchRepository) {
        this.nutritionInformationRepository = nutritionInformationRepository;
        this.nutritionInformationSearchRepository = nutritionInformationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NutritionInformation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NutritionInformation> findByCriteria(NutritionInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NutritionInformation> specification = createSpecification(criteria);
        return nutritionInformationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link NutritionInformation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NutritionInformation> findByCriteria(NutritionInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NutritionInformation> specification = createSpecification(criteria);
        return nutritionInformationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NutritionInformationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NutritionInformation> specification = createSpecification(criteria);
        return nutritionInformationRepository.count(specification);
    }

    /**
     * Function to convert NutritionInformationCriteria to a {@link Specification}
     */
    private Specification<NutritionInformation> createSpecification(NutritionInformationCriteria criteria) {
        Specification<NutritionInformation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), NutritionInformation_.id));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), NutritionInformation_.active));
            }
            if (criteria.getNutrition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNutrition(), NutritionInformation_.nutrition));
            }
            if (criteria.getIngredientNutritionInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getIngredientNutritionInfoId(),
                    root -> root.join(NutritionInformation_.ingredientNutritionInfos, JoinType.LEFT).get(IngredientNutritionInfo_.id)));
            }
        }
        return specification;
    }
}

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

import com.foodiechef.api.domain.IngredientNutritionInfo;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.IngredientNutritionInfoRepository;
import com.foodiechef.api.repository.search.IngredientNutritionInfoSearchRepository;
import com.foodiechef.api.service.dto.IngredientNutritionInfoCriteria;

/**
 * Service for executing complex queries for IngredientNutritionInfo entities in the database.
 * The main input is a {@link IngredientNutritionInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IngredientNutritionInfo} or a {@link Page} of {@link IngredientNutritionInfo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IngredientNutritionInfoQueryService extends QueryService<IngredientNutritionInfo> {

    private final Logger log = LoggerFactory.getLogger(IngredientNutritionInfoQueryService.class);

    private final IngredientNutritionInfoRepository ingredientNutritionInfoRepository;

    private final IngredientNutritionInfoSearchRepository ingredientNutritionInfoSearchRepository;

    public IngredientNutritionInfoQueryService(IngredientNutritionInfoRepository ingredientNutritionInfoRepository, IngredientNutritionInfoSearchRepository ingredientNutritionInfoSearchRepository) {
        this.ingredientNutritionInfoRepository = ingredientNutritionInfoRepository;
        this.ingredientNutritionInfoSearchRepository = ingredientNutritionInfoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link IngredientNutritionInfo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IngredientNutritionInfo> findByCriteria(IngredientNutritionInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IngredientNutritionInfo> specification = createSpecification(criteria);
        return ingredientNutritionInfoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link IngredientNutritionInfo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IngredientNutritionInfo> findByCriteria(IngredientNutritionInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IngredientNutritionInfo> specification = createSpecification(criteria);
        return ingredientNutritionInfoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IngredientNutritionInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IngredientNutritionInfo> specification = createSpecification(criteria);
        return ingredientNutritionInfoRepository.count(specification);
    }

    /**
     * Function to convert IngredientNutritionInfoCriteria to a {@link Specification}
     */
    private Specification<IngredientNutritionInfo> createSpecification(IngredientNutritionInfoCriteria criteria) {
        Specification<IngredientNutritionInfo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), IngredientNutritionInfo_.id));
            }
            if (criteria.getIngredientAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIngredientAmount(), IngredientNutritionInfo_.ingredientAmount));
            }
            if (criteria.getNutritionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNutritionAmount(), IngredientNutritionInfo_.nutritionAmount));
            }
            if (criteria.getNutritionInformationId() != null) {
                specification = specification.and(buildSpecification(criteria.getNutritionInformationId(),
                    root -> root.join(IngredientNutritionInfo_.nutritionInformation, JoinType.LEFT).get(NutritionInformation_.id)));
            }
            if (criteria.getIngredientId() != null) {
                specification = specification.and(buildSpecification(criteria.getIngredientId(),
                    root -> root.join(IngredientNutritionInfo_.ingredient, JoinType.LEFT).get(Ingredient_.id)));
            }
            if (criteria.getNutritionUnitId() != null) {
                specification = specification.and(buildSpecification(criteria.getNutritionUnitId(),
                    root -> root.join(IngredientNutritionInfo_.nutritionUnit, JoinType.LEFT).get(Measurement_.id)));
            }
            if (criteria.getIngredientUnitId() != null) {
                specification = specification.and(buildSpecification(criteria.getIngredientUnitId(),
                    root -> root.join(IngredientNutritionInfo_.ingredientUnit, JoinType.LEFT).get(Measurement_.id)));
            }
        }
        return specification;
    }
}

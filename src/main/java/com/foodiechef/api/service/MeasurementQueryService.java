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

import com.foodiechef.api.domain.Measurement;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.MeasurementRepository;
import com.foodiechef.api.repository.search.MeasurementSearchRepository;
import com.foodiechef.api.service.dto.MeasurementCriteria;

/**
 * Service for executing complex queries for Measurement entities in the database.
 * The main input is a {@link MeasurementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Measurement} or a {@link Page} of {@link Measurement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeasurementQueryService extends QueryService<Measurement> {

    private final Logger log = LoggerFactory.getLogger(MeasurementQueryService.class);

    private final MeasurementRepository measurementRepository;

    private final MeasurementSearchRepository measurementSearchRepository;

    public MeasurementQueryService(MeasurementRepository measurementRepository, MeasurementSearchRepository measurementSearchRepository) {
        this.measurementRepository = measurementRepository;
        this.measurementSearchRepository = measurementSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Measurement} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Measurement> findByCriteria(MeasurementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Measurement> specification = createSpecification(criteria);
        return measurementRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Measurement} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Measurement> findByCriteria(MeasurementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Measurement> specification = createSpecification(criteria);
        return measurementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MeasurementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Measurement> specification = createSpecification(criteria);
        return measurementRepository.count(specification);
    }

    /**
     * Function to convert MeasurementCriteria to a {@link Specification}
     */
    private Specification<Measurement> createSpecification(MeasurementCriteria criteria) {
        Specification<Measurement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Measurement_.id));
            }
            if (criteria.getAbbreviation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAbbreviation(), Measurement_.abbreviation));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Measurement_.active));
            }
            if (criteria.getRecipeIngredientId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeIngredientId(),
                    root -> root.join(Measurement_.recipeIngredients, JoinType.LEFT).get(RecipeIngredient_.id)));
            }
            if (criteria.getForNutritionUnitId() != null) {
                specification = specification.and(buildSpecification(criteria.getForNutritionUnitId(),
                    root -> root.join(Measurement_.forNutritionUnits, JoinType.LEFT).get(IngredientNutritionInfo_.id)));
            }
            if (criteria.getForIngredientUnitId() != null) {
                specification = specification.and(buildSpecification(criteria.getForIngredientUnitId(),
                    root -> root.join(Measurement_.forIngredientUnits, JoinType.LEFT).get(IngredientNutritionInfo_.id)));
            }
        }
        return specification;
    }
}

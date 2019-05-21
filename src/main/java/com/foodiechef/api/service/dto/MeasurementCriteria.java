package com.foodiechef.api.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Measurement entity. This class is used in MeasurementResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /measurements?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MeasurementCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter abbreviation;

    private BooleanFilter active;

    private LongFilter recipeIngredientId;

    private LongFilter forNutritionUnitId;

    private LongFilter forIngredientUnitId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(StringFilter abbreviation) {
        this.abbreviation = abbreviation;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public void setRecipeIngredientId(LongFilter recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public LongFilter getForNutritionUnitId() {
        return forNutritionUnitId;
    }

    public void setForNutritionUnitId(LongFilter forNutritionUnitId) {
        this.forNutritionUnitId = forNutritionUnitId;
    }

    public LongFilter getForIngredientUnitId() {
        return forIngredientUnitId;
    }

    public void setForIngredientUnitId(LongFilter forIngredientUnitId) {
        this.forIngredientUnitId = forIngredientUnitId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MeasurementCriteria that = (MeasurementCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(abbreviation, that.abbreviation) &&
            Objects.equals(active, that.active) &&
            Objects.equals(recipeIngredientId, that.recipeIngredientId) &&
            Objects.equals(forNutritionUnitId, that.forNutritionUnitId) &&
            Objects.equals(forIngredientUnitId, that.forIngredientUnitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        abbreviation,
        active,
        recipeIngredientId,
        forNutritionUnitId,
        forIngredientUnitId
        );
    }

    @Override
    public String toString() {
        return "MeasurementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (abbreviation != null ? "abbreviation=" + abbreviation + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (recipeIngredientId != null ? "recipeIngredientId=" + recipeIngredientId + ", " : "") +
                (forNutritionUnitId != null ? "forNutritionUnitId=" + forNutritionUnitId + ", " : "") +
                (forIngredientUnitId != null ? "forIngredientUnitId=" + forIngredientUnitId + ", " : "") +
            "}";
    }

}

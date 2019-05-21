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
 * Criteria class for the IngredientNutritionInfo entity. This class is used in IngredientNutritionInfoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /ingredient-nutrition-infos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IngredientNutritionInfoCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter ingredientAmount;

    private DoubleFilter nutritionAmount;

    private LongFilter nutritionInformationId;

    private LongFilter ingredientId;

    private LongFilter nutritionUnitId;

    private LongFilter ingredientUnitId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getIngredientAmount() {
        return ingredientAmount;
    }

    public void setIngredientAmount(DoubleFilter ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    public DoubleFilter getNutritionAmount() {
        return nutritionAmount;
    }

    public void setNutritionAmount(DoubleFilter nutritionAmount) {
        this.nutritionAmount = nutritionAmount;
    }

    public LongFilter getNutritionInformationId() {
        return nutritionInformationId;
    }

    public void setNutritionInformationId(LongFilter nutritionInformationId) {
        this.nutritionInformationId = nutritionInformationId;
    }

    public LongFilter getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(LongFilter ingredientId) {
        this.ingredientId = ingredientId;
    }

    public LongFilter getNutritionUnitId() {
        return nutritionUnitId;
    }

    public void setNutritionUnitId(LongFilter nutritionUnitId) {
        this.nutritionUnitId = nutritionUnitId;
    }

    public LongFilter getIngredientUnitId() {
        return ingredientUnitId;
    }

    public void setIngredientUnitId(LongFilter ingredientUnitId) {
        this.ingredientUnitId = ingredientUnitId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IngredientNutritionInfoCriteria that = (IngredientNutritionInfoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ingredientAmount, that.ingredientAmount) &&
            Objects.equals(nutritionAmount, that.nutritionAmount) &&
            Objects.equals(nutritionInformationId, that.nutritionInformationId) &&
            Objects.equals(ingredientId, that.ingredientId) &&
            Objects.equals(nutritionUnitId, that.nutritionUnitId) &&
            Objects.equals(ingredientUnitId, that.ingredientUnitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ingredientAmount,
        nutritionAmount,
        nutritionInformationId,
        ingredientId,
        nutritionUnitId,
        ingredientUnitId
        );
    }

    @Override
    public String toString() {
        return "IngredientNutritionInfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ingredientAmount != null ? "ingredientAmount=" + ingredientAmount + ", " : "") +
                (nutritionAmount != null ? "nutritionAmount=" + nutritionAmount + ", " : "") +
                (nutritionInformationId != null ? "nutritionInformationId=" + nutritionInformationId + ", " : "") +
                (ingredientId != null ? "ingredientId=" + ingredientId + ", " : "") +
                (nutritionUnitId != null ? "nutritionUnitId=" + nutritionUnitId + ", " : "") +
                (ingredientUnitId != null ? "ingredientUnitId=" + ingredientUnitId + ", " : "") +
            "}";
    }

}

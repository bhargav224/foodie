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
 * Criteria class for the RecipeIngredient entity. This class is used in RecipeIngredientResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /recipe-ingredients?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RecipeIngredientCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter amount;

    private DoubleFilter ingredientNutritionValue;

    private StringFilter restriction;

    private LongFilter recipeId;

    private LongFilter measurementId;

    private LongFilter ingredientId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public DoubleFilter getIngredientNutritionValue() {
        return ingredientNutritionValue;
    }

    public void setIngredientNutritionValue(DoubleFilter ingredientNutritionValue) {
        this.ingredientNutritionValue = ingredientNutritionValue;
    }

    public StringFilter getRestriction() {
        return restriction;
    }

    public void setRestriction(StringFilter restriction) {
        this.restriction = restriction;
    }

    public LongFilter getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(LongFilter recipeId) {
        this.recipeId = recipeId;
    }

    public LongFilter getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(LongFilter measurementId) {
        this.measurementId = measurementId;
    }

    public LongFilter getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(LongFilter ingredientId) {
        this.ingredientId = ingredientId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecipeIngredientCriteria that = (RecipeIngredientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(ingredientNutritionValue, that.ingredientNutritionValue) &&
            Objects.equals(restriction, that.restriction) &&
            Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(measurementId, that.measurementId) &&
            Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        amount,
        ingredientNutritionValue,
        restriction,
        recipeId,
        measurementId,
        ingredientId
        );
    }

    @Override
    public String toString() {
        return "RecipeIngredientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (ingredientNutritionValue != null ? "ingredientNutritionValue=" + ingredientNutritionValue + ", " : "") +
                (restriction != null ? "restriction=" + restriction + ", " : "") +
                (recipeId != null ? "recipeId=" + recipeId + ", " : "") +
                (measurementId != null ? "measurementId=" + measurementId + ", " : "") +
                (ingredientId != null ? "ingredientId=" + ingredientId + ", " : "") +
            "}";
    }

}

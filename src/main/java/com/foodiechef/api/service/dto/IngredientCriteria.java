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
 * Criteria class for the Ingredient entity. This class is used in IngredientResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /ingredients?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IngredientCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ingredient;

    private BooleanFilter active;

    private LongFilter recipeIngredientId;

    private LongFilter ingredientNutritionInfoId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIngredient() {
        return ingredient;
    }

    public void setIngredient(StringFilter ingredient) {
        this.ingredient = ingredient;
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

    public LongFilter getIngredientNutritionInfoId() {
        return ingredientNutritionInfoId;
    }

    public void setIngredientNutritionInfoId(LongFilter ingredientNutritionInfoId) {
        this.ingredientNutritionInfoId = ingredientNutritionInfoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IngredientCriteria that = (IngredientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ingredient, that.ingredient) &&
            Objects.equals(active, that.active) &&
            Objects.equals(recipeIngredientId, that.recipeIngredientId) &&
            Objects.equals(ingredientNutritionInfoId, that.ingredientNutritionInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ingredient,
        active,
        recipeIngredientId,
        ingredientNutritionInfoId
        );
    }

    @Override
    public String toString() {
        return "IngredientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ingredient != null ? "ingredient=" + ingredient + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (recipeIngredientId != null ? "recipeIngredientId=" + recipeIngredientId + ", " : "") +
                (ingredientNutritionInfoId != null ? "ingredientNutritionInfoId=" + ingredientNutritionInfoId + ", " : "") +
            "}";
    }

}

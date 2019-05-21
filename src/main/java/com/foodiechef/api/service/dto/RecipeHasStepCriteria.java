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
 * Criteria class for the RecipeHasStep entity. This class is used in RecipeHasStepResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /recipe-has-steps?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RecipeHasStepCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter recipeId;

    private LongFilter recipeStepId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(LongFilter recipeId) {
        this.recipeId = recipeId;
    }

    public LongFilter getRecipeStepId() {
        return recipeStepId;
    }

    public void setRecipeStepId(LongFilter recipeStepId) {
        this.recipeStepId = recipeStepId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecipeHasStepCriteria that = (RecipeHasStepCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(recipeStepId, that.recipeStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        recipeId,
        recipeStepId
        );
    }

    @Override
    public String toString() {
        return "RecipeHasStepCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (recipeId != null ? "recipeId=" + recipeId + ", " : "") +
                (recipeStepId != null ? "recipeStepId=" + recipeStepId + ", " : "") +
            "}";
    }

}

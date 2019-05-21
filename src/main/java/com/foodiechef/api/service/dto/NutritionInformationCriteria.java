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
 * Criteria class for the NutritionInformation entity. This class is used in NutritionInformationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /nutrition-informations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NutritionInformationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter active;

    private StringFilter nutrition;

    private LongFilter ingredientNutritionInfoId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public StringFilter getNutrition() {
        return nutrition;
    }

    public void setNutrition(StringFilter nutrition) {
        this.nutrition = nutrition;
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
        final NutritionInformationCriteria that = (NutritionInformationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(active, that.active) &&
            Objects.equals(nutrition, that.nutrition) &&
            Objects.equals(ingredientNutritionInfoId, that.ingredientNutritionInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        active,
        nutrition,
        ingredientNutritionInfoId
        );
    }

    @Override
    public String toString() {
        return "NutritionInformationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (nutrition != null ? "nutrition=" + nutrition + ", " : "") +
                (ingredientNutritionInfoId != null ? "ingredientNutritionInfoId=" + ingredientNutritionInfoId + ", " : "") +
            "}";
    }

}

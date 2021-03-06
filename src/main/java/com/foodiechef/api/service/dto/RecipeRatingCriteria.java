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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the RecipeRating entity. This class is used in RecipeRatingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /recipe-ratings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RecipeRatingCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private IntegerFilter rating;

    private LongFilter recipeId;

    private LongFilter userInfoId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public IntegerFilter getRating() {
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public LongFilter getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(LongFilter recipeId) {
        this.recipeId = recipeId;
    }

    public LongFilter getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(LongFilter userInfoId) {
        this.userInfoId = userInfoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecipeRatingCriteria that = (RecipeRatingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(userInfoId, that.userInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        date,
        rating,
        recipeId,
        userInfoId
        );
    }

    @Override
    public String toString() {
        return "RecipeRatingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (recipeId != null ? "recipeId=" + recipeId + ", " : "") +
                (userInfoId != null ? "userInfoId=" + userInfoId + ", " : "") +
            "}";
    }

}

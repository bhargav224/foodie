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
 * Criteria class for the ShareRecipe entity. This class is used in ShareRecipeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /share-recipes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShareRecipeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private LongFilter recipeId;

    private LongFilter sharedById;

    private LongFilter sharedToId;

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

    public LongFilter getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(LongFilter recipeId) {
        this.recipeId = recipeId;
    }

    public LongFilter getSharedById() {
        return sharedById;
    }

    public void setSharedById(LongFilter sharedById) {
        this.sharedById = sharedById;
    }

    public LongFilter getSharedToId() {
        return sharedToId;
    }

    public void setSharedToId(LongFilter sharedToId) {
        this.sharedToId = sharedToId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShareRecipeCriteria that = (ShareRecipeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(sharedById, that.sharedById) &&
            Objects.equals(sharedToId, that.sharedToId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        date,
        recipeId,
        sharedById,
        sharedToId
        );
    }

    @Override
    public String toString() {
        return "ShareRecipeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (recipeId != null ? "recipeId=" + recipeId + ", " : "") +
                (sharedById != null ? "sharedById=" + sharedById + ", " : "") +
                (sharedToId != null ? "sharedToId=" + sharedToId + ", " : "") +
            "}";
    }

}

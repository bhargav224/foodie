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
 * Criteria class for the Footnote entity. This class is used in FootnoteResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /footnotes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FootnoteCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter footnote;

    private LongFilter recipeId;

    private LongFilter userInfoId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFootnote() {
        return footnote;
    }

    public void setFootnote(StringFilter footnote) {
        this.footnote = footnote;
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
        final FootnoteCriteria that = (FootnoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(footnote, that.footnote) &&
            Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(userInfoId, that.userInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        footnote,
        recipeId,
        userInfoId
        );
    }

    @Override
    public String toString() {
        return "FootnoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (footnote != null ? "footnote=" + footnote + ", " : "") +
                (recipeId != null ? "recipeId=" + recipeId + ", " : "") +
                (userInfoId != null ? "userInfoId=" + userInfoId + ", " : "") +
            "}";
    }

}

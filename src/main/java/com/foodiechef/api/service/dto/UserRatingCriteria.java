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
 * Criteria class for the UserRating entity. This class is used in UserRatingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /user-ratings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserRatingCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private IntegerFilter rating;

    private LongFilter rateByUserId;

    private LongFilter rateToUserId;

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

    public LongFilter getRateByUserId() {
        return rateByUserId;
    }

    public void setRateByUserId(LongFilter rateByUserId) {
        this.rateByUserId = rateByUserId;
    }

    public LongFilter getRateToUserId() {
        return rateToUserId;
    }

    public void setRateToUserId(LongFilter rateToUserId) {
        this.rateToUserId = rateToUserId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserRatingCriteria that = (UserRatingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(rateByUserId, that.rateByUserId) &&
            Objects.equals(rateToUserId, that.rateToUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        date,
        rating,
        rateByUserId,
        rateToUserId
        );
    }

    @Override
    public String toString() {
        return "UserRatingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (rateByUserId != null ? "rateByUserId=" + rateByUserId + ", " : "") +
                (rateToUserId != null ? "rateToUserId=" + rateToUserId + ", " : "") +
            "}";
    }

}

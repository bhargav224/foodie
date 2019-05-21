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
 * Criteria class for the RestaurantMenu entity. This class is used in RestaurantMenuResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /restaurant-menus?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RestaurantMenuCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter menuItemId;

    private LongFilter restaurantId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(LongFilter menuItemId) {
        this.menuItemId = menuItemId;
    }

    public LongFilter getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(LongFilter restaurantId) {
        this.restaurantId = restaurantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RestaurantMenuCriteria that = (RestaurantMenuCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(menuItemId, that.menuItemId) &&
            Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        menuItemId,
        restaurantId
        );
    }

    @Override
    public String toString() {
        return "RestaurantMenuCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (menuItemId != null ? "menuItemId=" + menuItemId + ", " : "") +
                (restaurantId != null ? "restaurantId=" + restaurantId + ", " : "") +
            "}";
    }

}

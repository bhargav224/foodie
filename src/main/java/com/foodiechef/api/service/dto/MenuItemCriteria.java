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
 * Criteria class for the MenuItem entity. This class is used in MenuItemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /menu-items?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MenuItemCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter active;

    private StringFilter description;

    private StringFilter imagePath;

    private StringFilter name;

    private LongFilter restaurantMenuId;

    private LongFilter menuRecipeId;

    private LongFilter foodCategorieId;

    private LongFilter courseId;

    private LongFilter cusineId;

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

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getImagePath() {
        return imagePath;
    }

    public void setImagePath(StringFilter imagePath) {
        this.imagePath = imagePath;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getRestaurantMenuId() {
        return restaurantMenuId;
    }

    public void setRestaurantMenuId(LongFilter restaurantMenuId) {
        this.restaurantMenuId = restaurantMenuId;
    }

    public LongFilter getMenuRecipeId() {
        return menuRecipeId;
    }

    public void setMenuRecipeId(LongFilter menuRecipeId) {
        this.menuRecipeId = menuRecipeId;
    }

    public LongFilter getFoodCategorieId() {
        return foodCategorieId;
    }

    public void setFoodCategorieId(LongFilter foodCategorieId) {
        this.foodCategorieId = foodCategorieId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getCusineId() {
        return cusineId;
    }

    public void setCusineId(LongFilter cusineId) {
        this.cusineId = cusineId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuItemCriteria that = (MenuItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(active, that.active) &&
            Objects.equals(description, that.description) &&
            Objects.equals(imagePath, that.imagePath) &&
            Objects.equals(name, that.name) &&
            Objects.equals(restaurantMenuId, that.restaurantMenuId) &&
            Objects.equals(menuRecipeId, that.menuRecipeId) &&
            Objects.equals(foodCategorieId, that.foodCategorieId) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(cusineId, that.cusineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        active,
        description,
        imagePath,
        name,
        restaurantMenuId,
        menuRecipeId,
        foodCategorieId,
        courseId,
        cusineId
        );
    }

    @Override
    public String toString() {
        return "MenuItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (imagePath != null ? "imagePath=" + imagePath + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (restaurantMenuId != null ? "restaurantMenuId=" + restaurantMenuId + ", " : "") +
                (menuRecipeId != null ? "menuRecipeId=" + menuRecipeId + ", " : "") +
                (foodCategorieId != null ? "foodCategorieId=" + foodCategorieId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
                (cusineId != null ? "cusineId=" + cusineId + ", " : "") +
            "}";
    }

}

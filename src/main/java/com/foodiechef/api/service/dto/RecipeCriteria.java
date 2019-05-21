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
 * Criteria class for the Recipe entity. This class is used in RecipeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /recipes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RecipeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter amount;

    private IntegerFilter caloriesPerServings;

    private IntegerFilter cookTime;

    private StringFilter description;

    private IntegerFilter prepTime;

    private BooleanFilter published;

    private IntegerFilter rating;

    private IntegerFilter readyIn;

    private StringFilter title;

    private StringFilter video;

    private IntegerFilter yields;

    private LongFilter recipeHasStepId;

    private LongFilter recipeImageId;

    private LongFilter menuRecipeId;

    private LongFilter recipeCommentId;

    private LongFilter recipeRatingId;

    private LongFilter shareRecipeId;

    private LongFilter recipeLikeId;

    private LongFilter footnoteId;

    private LongFilter recipeIngredientId;

    private LongFilter levelId;

    private LongFilter foodCategorieId;

    private LongFilter cusineId;

    private LongFilter courseId;

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

    public IntegerFilter getCaloriesPerServings() {
        return caloriesPerServings;
    }

    public void setCaloriesPerServings(IntegerFilter caloriesPerServings) {
        this.caloriesPerServings = caloriesPerServings;
    }

    public IntegerFilter getCookTime() {
        return cookTime;
    }

    public void setCookTime(IntegerFilter cookTime) {
        this.cookTime = cookTime;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(IntegerFilter prepTime) {
        this.prepTime = prepTime;
    }

    public BooleanFilter getPublished() {
        return published;
    }

    public void setPublished(BooleanFilter published) {
        this.published = published;
    }

    public IntegerFilter getRating() {
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public IntegerFilter getReadyIn() {
        return readyIn;
    }

    public void setReadyIn(IntegerFilter readyIn) {
        this.readyIn = readyIn;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getVideo() {
        return video;
    }

    public void setVideo(StringFilter video) {
        this.video = video;
    }

    public IntegerFilter getYields() {
        return yields;
    }

    public void setYields(IntegerFilter yields) {
        this.yields = yields;
    }

    public LongFilter getRecipeHasStepId() {
        return recipeHasStepId;
    }

    public void setRecipeHasStepId(LongFilter recipeHasStepId) {
        this.recipeHasStepId = recipeHasStepId;
    }

    public LongFilter getRecipeImageId() {
        return recipeImageId;
    }

    public void setRecipeImageId(LongFilter recipeImageId) {
        this.recipeImageId = recipeImageId;
    }

    public LongFilter getMenuRecipeId() {
        return menuRecipeId;
    }

    public void setMenuRecipeId(LongFilter menuRecipeId) {
        this.menuRecipeId = menuRecipeId;
    }

    public LongFilter getRecipeCommentId() {
        return recipeCommentId;
    }

    public void setRecipeCommentId(LongFilter recipeCommentId) {
        this.recipeCommentId = recipeCommentId;
    }

    public LongFilter getRecipeRatingId() {
        return recipeRatingId;
    }

    public void setRecipeRatingId(LongFilter recipeRatingId) {
        this.recipeRatingId = recipeRatingId;
    }

    public LongFilter getShareRecipeId() {
        return shareRecipeId;
    }

    public void setShareRecipeId(LongFilter shareRecipeId) {
        this.shareRecipeId = shareRecipeId;
    }

    public LongFilter getRecipeLikeId() {
        return recipeLikeId;
    }

    public void setRecipeLikeId(LongFilter recipeLikeId) {
        this.recipeLikeId = recipeLikeId;
    }

    public LongFilter getFootnoteId() {
        return footnoteId;
    }

    public void setFootnoteId(LongFilter footnoteId) {
        this.footnoteId = footnoteId;
    }

    public LongFilter getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public void setRecipeIngredientId(LongFilter recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public LongFilter getLevelId() {
        return levelId;
    }

    public void setLevelId(LongFilter levelId) {
        this.levelId = levelId;
    }

    public LongFilter getFoodCategorieId() {
        return foodCategorieId;
    }

    public void setFoodCategorieId(LongFilter foodCategorieId) {
        this.foodCategorieId = foodCategorieId;
    }

    public LongFilter getCusineId() {
        return cusineId;
    }

    public void setCusineId(LongFilter cusineId) {
        this.cusineId = cusineId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecipeCriteria that = (RecipeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(caloriesPerServings, that.caloriesPerServings) &&
            Objects.equals(cookTime, that.cookTime) &&
            Objects.equals(description, that.description) &&
            Objects.equals(prepTime, that.prepTime) &&
            Objects.equals(published, that.published) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(readyIn, that.readyIn) &&
            Objects.equals(title, that.title) &&
            Objects.equals(video, that.video) &&
            Objects.equals(yields, that.yields) &&
            Objects.equals(recipeHasStepId, that.recipeHasStepId) &&
            Objects.equals(recipeImageId, that.recipeImageId) &&
            Objects.equals(menuRecipeId, that.menuRecipeId) &&
            Objects.equals(recipeCommentId, that.recipeCommentId) &&
            Objects.equals(recipeRatingId, that.recipeRatingId) &&
            Objects.equals(shareRecipeId, that.shareRecipeId) &&
            Objects.equals(recipeLikeId, that.recipeLikeId) &&
            Objects.equals(footnoteId, that.footnoteId) &&
            Objects.equals(recipeIngredientId, that.recipeIngredientId) &&
            Objects.equals(levelId, that.levelId) &&
            Objects.equals(foodCategorieId, that.foodCategorieId) &&
            Objects.equals(cusineId, that.cusineId) &&
            Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        amount,
        caloriesPerServings,
        cookTime,
        description,
        prepTime,
        published,
        rating,
        readyIn,
        title,
        video,
        yields,
        recipeHasStepId,
        recipeImageId,
        menuRecipeId,
        recipeCommentId,
        recipeRatingId,
        shareRecipeId,
        recipeLikeId,
        footnoteId,
        recipeIngredientId,
        levelId,
        foodCategorieId,
        cusineId,
        courseId
        );
    }

    @Override
    public String toString() {
        return "RecipeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (caloriesPerServings != null ? "caloriesPerServings=" + caloriesPerServings + ", " : "") +
                (cookTime != null ? "cookTime=" + cookTime + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (prepTime != null ? "prepTime=" + prepTime + ", " : "") +
                (published != null ? "published=" + published + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (readyIn != null ? "readyIn=" + readyIn + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (video != null ? "video=" + video + ", " : "") +
                (yields != null ? "yields=" + yields + ", " : "") +
                (recipeHasStepId != null ? "recipeHasStepId=" + recipeHasStepId + ", " : "") +
                (recipeImageId != null ? "recipeImageId=" + recipeImageId + ", " : "") +
                (menuRecipeId != null ? "menuRecipeId=" + menuRecipeId + ", " : "") +
                (recipeCommentId != null ? "recipeCommentId=" + recipeCommentId + ", " : "") +
                (recipeRatingId != null ? "recipeRatingId=" + recipeRatingId + ", " : "") +
                (shareRecipeId != null ? "shareRecipeId=" + shareRecipeId + ", " : "") +
                (recipeLikeId != null ? "recipeLikeId=" + recipeLikeId + ", " : "") +
                (footnoteId != null ? "footnoteId=" + footnoteId + ", " : "") +
                (recipeIngredientId != null ? "recipeIngredientId=" + recipeIngredientId + ", " : "") +
                (levelId != null ? "levelId=" + levelId + ", " : "") +
                (foodCategorieId != null ? "foodCategorieId=" + foodCategorieId + ", " : "") +
                (cusineId != null ? "cusineId=" + cusineId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
            "}";
    }

}

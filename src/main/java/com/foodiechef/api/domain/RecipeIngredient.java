package com.foodiechef.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RecipeIngredient.
 */
@Entity
@Table(name = "recipe_ingredient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "recipeingredient")
public class RecipeIngredient implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "ingredient_nutrition_value", nullable = false)
    private Double ingredientNutritionValue;

    @NotNull
    @Column(name = "restriction", nullable = false)
    private String restriction;

    @ManyToOne
    @JsonIgnoreProperties("recipeIngredients")
    private Recipe recipe;

    @ManyToOne
    @JsonIgnoreProperties("recipeIngredients")
    private Measurement measurement;

    @ManyToOne
    @JsonIgnoreProperties("recipeIngredients")
    private Ingredient ingredient;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public RecipeIngredient amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getIngredientNutritionValue() {
        return ingredientNutritionValue;
    }

    public RecipeIngredient ingredientNutritionValue(Double ingredientNutritionValue) {
        this.ingredientNutritionValue = ingredientNutritionValue;
        return this;
    }

    public void setIngredientNutritionValue(Double ingredientNutritionValue) {
        this.ingredientNutritionValue = ingredientNutritionValue;
    }

    public String getRestriction() {
        return restriction;
    }

    public RecipeIngredient restriction(String restriction) {
        this.restriction = restriction;
        return this;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public RecipeIngredient recipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public RecipeIngredient measurement(Measurement measurement) {
        this.measurement = measurement;
        return this;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public RecipeIngredient ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeIngredient recipeIngredient = (RecipeIngredient) o;
        if (recipeIngredient.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recipeIngredient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", ingredientNutritionValue=" + getIngredientNutritionValue() +
            ", restriction='" + getRestriction() + "'" +
            "}";
    }
}

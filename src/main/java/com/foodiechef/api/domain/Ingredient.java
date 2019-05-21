package com.foodiechef.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ingredient.
 */
@Entity
@Table(name = "ingredient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ingredient")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ingredient", nullable = false)
    private String ingredient;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "ingredient")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();
    @OneToMany(mappedBy = "ingredient")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IngredientNutritionInfo> ingredientNutritionInfos = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public Ingredient ingredient(String ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Boolean isActive() {
        return active;
    }

    public Ingredient active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public Ingredient recipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
        return this;
    }

    public Ingredient addRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.add(recipeIngredient);
        recipeIngredient.setIngredient(this);
        return this;
    }

    public Ingredient removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.remove(recipeIngredient);
        recipeIngredient.setIngredient(null);
        return this;
    }

    public void setRecipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public Set<IngredientNutritionInfo> getIngredientNutritionInfos() {
        return ingredientNutritionInfos;
    }

    public Ingredient ingredientNutritionInfos(Set<IngredientNutritionInfo> ingredientNutritionInfos) {
        this.ingredientNutritionInfos = ingredientNutritionInfos;
        return this;
    }

    public Ingredient addIngredientNutritionInfo(IngredientNutritionInfo ingredientNutritionInfo) {
        this.ingredientNutritionInfos.add(ingredientNutritionInfo);
        ingredientNutritionInfo.setIngredient(this);
        return this;
    }

    public Ingredient removeIngredientNutritionInfo(IngredientNutritionInfo ingredientNutritionInfo) {
        this.ingredientNutritionInfos.remove(ingredientNutritionInfo);
        ingredientNutritionInfo.setIngredient(null);
        return this;
    }

    public void setIngredientNutritionInfos(Set<IngredientNutritionInfo> ingredientNutritionInfos) {
        this.ingredientNutritionInfos = ingredientNutritionInfos;
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
        Ingredient ingredient = (Ingredient) o;
        if (ingredient.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ingredient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "id=" + getId() +
            ", ingredient='" + getIngredient() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}

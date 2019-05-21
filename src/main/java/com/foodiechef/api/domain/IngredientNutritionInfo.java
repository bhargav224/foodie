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
 * A IngredientNutritionInfo.
 */
@Entity
@Table(name = "ingredient_nutrition_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ingredientnutritioninfo")
public class IngredientNutritionInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ingredient_amount", nullable = false)
    private Double ingredientAmount;

    @NotNull
    @Column(name = "nutrition_amount", nullable = false)
    private Double nutritionAmount;

    @ManyToOne
    @JsonIgnoreProperties("ingredientNutritionInfos")
    private NutritionInformation nutritionInformation;

    @ManyToOne
    @JsonIgnoreProperties("ingredientNutritionInfos")
    private Ingredient ingredient;

    @ManyToOne
    @JsonIgnoreProperties("forNutritionUnits")
    private Measurement nutritionUnit;

    @ManyToOne
    @JsonIgnoreProperties("forIngredientUnits")
    private Measurement ingredientUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getIngredientAmount() {
        return ingredientAmount;
    }

    public IngredientNutritionInfo ingredientAmount(Double ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
        return this;
    }

    public void setIngredientAmount(Double ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    public Double getNutritionAmount() {
        return nutritionAmount;
    }

    public IngredientNutritionInfo nutritionAmount(Double nutritionAmount) {
        this.nutritionAmount = nutritionAmount;
        return this;
    }

    public void setNutritionAmount(Double nutritionAmount) {
        this.nutritionAmount = nutritionAmount;
    }

    public NutritionInformation getNutritionInformation() {
        return nutritionInformation;
    }

    public IngredientNutritionInfo nutritionInformation(NutritionInformation nutritionInformation) {
        this.nutritionInformation = nutritionInformation;
        return this;
    }

    public void setNutritionInformation(NutritionInformation nutritionInformation) {
        this.nutritionInformation = nutritionInformation;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public IngredientNutritionInfo ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Measurement getNutritionUnit() {
        return nutritionUnit;
    }

    public IngredientNutritionInfo nutritionUnit(Measurement measurement) {
        this.nutritionUnit = measurement;
        return this;
    }

    public void setNutritionUnit(Measurement measurement) {
        this.nutritionUnit = measurement;
    }

    public Measurement getIngredientUnit() {
        return ingredientUnit;
    }

    public IngredientNutritionInfo ingredientUnit(Measurement measurement) {
        this.ingredientUnit = measurement;
        return this;
    }

    public void setIngredientUnit(Measurement measurement) {
        this.ingredientUnit = measurement;
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
        IngredientNutritionInfo ingredientNutritionInfo = (IngredientNutritionInfo) o;
        if (ingredientNutritionInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ingredientNutritionInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IngredientNutritionInfo{" +
            "id=" + getId() +
            ", ingredientAmount=" + getIngredientAmount() +
            ", nutritionAmount=" + getNutritionAmount() +
            "}";
    }
}

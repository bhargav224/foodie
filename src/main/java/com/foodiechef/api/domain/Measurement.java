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
 * A Measurement.
 */
@Entity
@Table(name = "measurement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "measurement")
public class Measurement implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "abbreviation", nullable = false)
    private String abbreviation;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "measurement")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();
    @OneToMany(mappedBy = "nutritionUnit")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IngredientNutritionInfo> forNutritionUnits = new HashSet<>();
    @OneToMany(mappedBy = "ingredientUnit")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IngredientNutritionInfo> forIngredientUnits = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Measurement abbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Boolean isActive() {
        return active;
    }

    public Measurement active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public Measurement recipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
        return this;
    }

    public Measurement addRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.add(recipeIngredient);
        recipeIngredient.setMeasurement(this);
        return this;
    }

    public Measurement removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.remove(recipeIngredient);
        recipeIngredient.setMeasurement(null);
        return this;
    }

    public void setRecipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public Set<IngredientNutritionInfo> getForNutritionUnits() {
        return forNutritionUnits;
    }

    public Measurement forNutritionUnits(Set<IngredientNutritionInfo> ingredientNutritionInfos) {
        this.forNutritionUnits = ingredientNutritionInfos;
        return this;
    }

    public Measurement addForNutritionUnit(IngredientNutritionInfo ingredientNutritionInfo) {
        this.forNutritionUnits.add(ingredientNutritionInfo);
        ingredientNutritionInfo.setNutritionUnit(this);
        return this;
    }

    public Measurement removeForNutritionUnit(IngredientNutritionInfo ingredientNutritionInfo) {
        this.forNutritionUnits.remove(ingredientNutritionInfo);
        ingredientNutritionInfo.setNutritionUnit(null);
        return this;
    }

    public void setForNutritionUnits(Set<IngredientNutritionInfo> ingredientNutritionInfos) {
        this.forNutritionUnits = ingredientNutritionInfos;
    }

    public Set<IngredientNutritionInfo> getForIngredientUnits() {
        return forIngredientUnits;
    }

    public Measurement forIngredientUnits(Set<IngredientNutritionInfo> ingredientNutritionInfos) {
        this.forIngredientUnits = ingredientNutritionInfos;
        return this;
    }

    public Measurement addForIngredientUnit(IngredientNutritionInfo ingredientNutritionInfo) {
        this.forIngredientUnits.add(ingredientNutritionInfo);
        ingredientNutritionInfo.setIngredientUnit(this);
        return this;
    }

    public Measurement removeForIngredientUnit(IngredientNutritionInfo ingredientNutritionInfo) {
        this.forIngredientUnits.remove(ingredientNutritionInfo);
        ingredientNutritionInfo.setIngredientUnit(null);
        return this;
    }

    public void setForIngredientUnits(Set<IngredientNutritionInfo> ingredientNutritionInfos) {
        this.forIngredientUnits = ingredientNutritionInfos;
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
        Measurement measurement = (Measurement) o;
        if (measurement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), measurement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Measurement{" +
            "id=" + getId() +
            ", abbreviation='" + getAbbreviation() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}

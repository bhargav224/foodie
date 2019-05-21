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
 * A NutritionInformation.
 */
@Entity
@Table(name = "nutrition_information")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "nutritioninformation")
public class NutritionInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "nutrition", nullable = false)
    private String nutrition;

    @OneToMany(mappedBy = "nutritionInformation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IngredientNutritionInfo> ingredientNutritionInfos = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActive() {
        return active;
    }

    public NutritionInformation active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getNutrition() {
        return nutrition;
    }

    public NutritionInformation nutrition(String nutrition) {
        this.nutrition = nutrition;
        return this;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public Set<IngredientNutritionInfo> getIngredientNutritionInfos() {
        return ingredientNutritionInfos;
    }

    public NutritionInformation ingredientNutritionInfos(Set<IngredientNutritionInfo> ingredientNutritionInfos) {
        this.ingredientNutritionInfos = ingredientNutritionInfos;
        return this;
    }

    public NutritionInformation addIngredientNutritionInfo(IngredientNutritionInfo ingredientNutritionInfo) {
        this.ingredientNutritionInfos.add(ingredientNutritionInfo);
        ingredientNutritionInfo.setNutritionInformation(this);
        return this;
    }

    public NutritionInformation removeIngredientNutritionInfo(IngredientNutritionInfo ingredientNutritionInfo) {
        this.ingredientNutritionInfos.remove(ingredientNutritionInfo);
        ingredientNutritionInfo.setNutritionInformation(null);
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
        NutritionInformation nutritionInformation = (NutritionInformation) o;
        if (nutritionInformation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), nutritionInformation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NutritionInformation{" +
            "id=" + getId() +
            ", active='" + isActive() + "'" +
            ", nutrition='" + getNutrition() + "'" +
            "}";
    }
}

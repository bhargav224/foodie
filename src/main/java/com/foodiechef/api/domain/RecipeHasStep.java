package com.foodiechef.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RecipeHasStep.
 */
@Entity
@Table(name = "recipe_has_step")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "recipehasstep")
public class RecipeHasStep implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("recipeHasSteps")
    private Recipe recipe;

    @ManyToOne
    @JsonIgnoreProperties("recipeHasSteps")
    private RecipeStep recipeStep;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public RecipeHasStep recipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public RecipeStep getRecipeStep() {
        return recipeStep;
    }

    public RecipeHasStep recipeStep(RecipeStep recipeStep) {
        this.recipeStep = recipeStep;
        return this;
    }

    public void setRecipeStep(RecipeStep recipeStep) {
        this.recipeStep = recipeStep;
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
        RecipeHasStep recipeHasStep = (RecipeHasStep) o;
        if (recipeHasStep.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recipeHasStep.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RecipeHasStep{" +
            "id=" + getId() +
            "}";
    }
}

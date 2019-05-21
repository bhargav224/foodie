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
 * A RecipeStep.
 */
@Entity
@Table(name = "recipe_step")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "recipestep")
public class RecipeStep implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "instruction", nullable = false)
    private String instruction;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "path", nullable = false)
    private String path;

    @OneToMany(mappedBy = "recipeStep")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeHasStep> recipeHasSteps = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstruction() {
        return instruction;
    }

    public RecipeStep instruction(String instruction) {
        this.instruction = instruction;
        return this;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getName() {
        return name;
    }

    public RecipeStep name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public RecipeStep path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<RecipeHasStep> getRecipeHasSteps() {
        return recipeHasSteps;
    }

    public RecipeStep recipeHasSteps(Set<RecipeHasStep> recipeHasSteps) {
        this.recipeHasSteps = recipeHasSteps;
        return this;
    }

    public RecipeStep addRecipeHasStep(RecipeHasStep recipeHasStep) {
        this.recipeHasSteps.add(recipeHasStep);
        recipeHasStep.setRecipeStep(this);
        return this;
    }

    public RecipeStep removeRecipeHasStep(RecipeHasStep recipeHasStep) {
        this.recipeHasSteps.remove(recipeHasStep);
        recipeHasStep.setRecipeStep(null);
        return this;
    }

    public void setRecipeHasSteps(Set<RecipeHasStep> recipeHasSteps) {
        this.recipeHasSteps = recipeHasSteps;
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
        RecipeStep recipeStep = (RecipeStep) o;
        if (recipeStep.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recipeStep.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RecipeStep{" +
            "id=" + getId() +
            ", instruction='" + getInstruction() + "'" +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            "}";
    }
}

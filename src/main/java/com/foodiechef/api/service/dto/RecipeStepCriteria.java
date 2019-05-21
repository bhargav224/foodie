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
 * Criteria class for the RecipeStep entity. This class is used in RecipeStepResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /recipe-steps?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RecipeStepCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter instruction;

    private StringFilter name;

    private StringFilter path;

    private LongFilter recipeHasStepId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getInstruction() {
        return instruction;
    }

    public void setInstruction(StringFilter instruction) {
        this.instruction = instruction;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getPath() {
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public LongFilter getRecipeHasStepId() {
        return recipeHasStepId;
    }

    public void setRecipeHasStepId(LongFilter recipeHasStepId) {
        this.recipeHasStepId = recipeHasStepId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecipeStepCriteria that = (RecipeStepCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(instruction, that.instruction) &&
            Objects.equals(name, that.name) &&
            Objects.equals(path, that.path) &&
            Objects.equals(recipeHasStepId, that.recipeHasStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        instruction,
        name,
        path,
        recipeHasStepId
        );
    }

    @Override
    public String toString() {
        return "RecipeStepCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (instruction != null ? "instruction=" + instruction + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (path != null ? "path=" + path + ", " : "") +
                (recipeHasStepId != null ? "recipeHasStepId=" + recipeHasStepId + ", " : "") +
            "}";
    }

}

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
 * A FoodCategorie.
 */
@Entity
@Table(name = "food_categorie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "foodcategorie")
public class FoodCategorie implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @OneToMany(mappedBy = "foodCategorie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Recipe> recipes = new HashSet<>();
    @OneToMany(mappedBy = "foodCategorie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MenuItem> menuItems = new HashSet<>();
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

    public FoodCategorie active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCategory() {
        return category;
    }

    public FoodCategorie category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public FoodCategorie recipes(Set<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }

    public FoodCategorie addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        recipe.setFoodCategorie(this);
        return this;
    }

    public FoodCategorie removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
        recipe.setFoodCategorie(null);
        return this;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }

    public FoodCategorie menuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
        return this;
    }

    public FoodCategorie addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        menuItem.setFoodCategorie(this);
        return this;
    }

    public FoodCategorie removeMenuItem(MenuItem menuItem) {
        this.menuItems.remove(menuItem);
        menuItem.setFoodCategorie(null);
        return this;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
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
        FoodCategorie foodCategorie = (FoodCategorie) o;
        if (foodCategorie.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), foodCategorie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FoodCategorie{" +
            "id=" + getId() +
            ", active='" + isActive() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}

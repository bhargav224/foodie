package com.foodiechef.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A MenuItem.
 */
@Entity
@Table(name = "menu_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "menuitem")
public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "menuItem")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RestaurantMenu> restaurantMenus = new HashSet<>();
    @OneToMany(mappedBy = "menuItem")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MenuRecipe> menuRecipes = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("menuItems")
    private FoodCategorie foodCategorie;

    @ManyToOne
    @JsonIgnoreProperties("menuItems")
    private Course course;

    @ManyToOne
    @JsonIgnoreProperties("menuItems")
    private Cusine cusine;

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

    public MenuItem active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public MenuItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public MenuItem imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public MenuItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RestaurantMenu> getRestaurantMenus() {
        return restaurantMenus;
    }

    public MenuItem restaurantMenus(Set<RestaurantMenu> restaurantMenus) {
        this.restaurantMenus = restaurantMenus;
        return this;
    }

    public MenuItem addRestaurantMenu(RestaurantMenu restaurantMenu) {
        this.restaurantMenus.add(restaurantMenu);
        restaurantMenu.setMenuItem(this);
        return this;
    }

    public MenuItem removeRestaurantMenu(RestaurantMenu restaurantMenu) {
        this.restaurantMenus.remove(restaurantMenu);
        restaurantMenu.setMenuItem(null);
        return this;
    }

    public void setRestaurantMenus(Set<RestaurantMenu> restaurantMenus) {
        this.restaurantMenus = restaurantMenus;
    }

    public Set<MenuRecipe> getMenuRecipes() {
        return menuRecipes;
    }

    public MenuItem menuRecipes(Set<MenuRecipe> menuRecipes) {
        this.menuRecipes = menuRecipes;
        return this;
    }

    public MenuItem addMenuRecipe(MenuRecipe menuRecipe) {
        this.menuRecipes.add(menuRecipe);
        menuRecipe.setMenuItem(this);
        return this;
    }

    public MenuItem removeMenuRecipe(MenuRecipe menuRecipe) {
        this.menuRecipes.remove(menuRecipe);
        menuRecipe.setMenuItem(null);
        return this;
    }

    public void setMenuRecipes(Set<MenuRecipe> menuRecipes) {
        this.menuRecipes = menuRecipes;
    }

    public FoodCategorie getFoodCategorie() {
        return foodCategorie;
    }

    public MenuItem foodCategorie(FoodCategorie foodCategorie) {
        this.foodCategorie = foodCategorie;
        return this;
    }

    public void setFoodCategorie(FoodCategorie foodCategorie) {
        this.foodCategorie = foodCategorie;
    }

    public Course getCourse() {
        return course;
    }

    public MenuItem course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Cusine getCusine() {
        return cusine;
    }

    public MenuItem cusine(Cusine cusine) {
        this.cusine = cusine;
        return this;
    }

    public void setCusine(Cusine cusine) {
        this.cusine = cusine;
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
        MenuItem menuItem = (MenuItem) o;
        if (menuItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), menuItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + getId() +
            ", active='" + isActive() + "'" +
            ", description='" + getDescription() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}

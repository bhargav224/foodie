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
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "recipe")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "calories_per_servings", nullable = false)
    private Integer caloriesPerServings;

    @NotNull
    @Column(name = "cook_time", nullable = false)
    private Integer cookTime;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "prep_time", nullable = false)
    private Integer prepTime;

    @NotNull
    @Column(name = "published", nullable = false)
    private Boolean published;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotNull
    @Column(name = "ready_in", nullable = false)
    private Integer readyIn;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "video", nullable = false)
    private String video;

    @NotNull
    @Column(name = "yields", nullable = false)
    private Integer yields;

    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeHasStep> recipeHasSteps = new HashSet<>();
    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeImage> recipeImages = new HashSet<>();
    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MenuRecipe> menuRecipes = new HashSet<>();
    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeComment> recipeComments = new HashSet<>();
    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeRating> recipeRatings = new HashSet<>();
    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShareRecipe> shareRecipes = new HashSet<>();
    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeLike> recipeLikes = new HashSet<>();
    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Footnote> footnotes = new HashSet<>();
    @OneToMany(mappedBy = "recipe")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("recipes")
    private Level level;

    @ManyToOne
    @JsonIgnoreProperties("recipes")
    private FoodCategorie foodCategorie;

    @ManyToOne
    @JsonIgnoreProperties("recipes")
    private Cusine cusine;

    @ManyToOne
    @JsonIgnoreProperties("recipes")
    private Course course;

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

    public Recipe amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCaloriesPerServings() {
        return caloriesPerServings;
    }

    public Recipe caloriesPerServings(Integer caloriesPerServings) {
        this.caloriesPerServings = caloriesPerServings;
        return this;
    }

    public void setCaloriesPerServings(Integer caloriesPerServings) {
        this.caloriesPerServings = caloriesPerServings;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public Recipe cookTime(Integer cookTime) {
        this.cookTime = cookTime;
        return this;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public String getDescription() {
        return description;
    }

    public Recipe description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public Recipe prepTime(Integer prepTime) {
        this.prepTime = prepTime;
        return this;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Boolean isPublished() {
        return published;
    }

    public Recipe published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Integer getRating() {
        return rating;
    }

    public Recipe rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getReadyIn() {
        return readyIn;
    }

    public Recipe readyIn(Integer readyIn) {
        this.readyIn = readyIn;
        return this;
    }

    public void setReadyIn(Integer readyIn) {
        this.readyIn = readyIn;
    }

    public String getTitle() {
        return title;
    }

    public Recipe title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public Recipe video(String video) {
        this.video = video;
        return this;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getYields() {
        return yields;
    }

    public Recipe yields(Integer yields) {
        this.yields = yields;
        return this;
    }

    public void setYields(Integer yields) {
        this.yields = yields;
    }

    public Set<RecipeHasStep> getRecipeHasSteps() {
        return recipeHasSteps;
    }

    public Recipe recipeHasSteps(Set<RecipeHasStep> recipeHasSteps) {
        this.recipeHasSteps = recipeHasSteps;
        return this;
    }

    public Recipe addRecipeHasStep(RecipeHasStep recipeHasStep) {
        this.recipeHasSteps.add(recipeHasStep);
        recipeHasStep.setRecipe(this);
        return this;
    }

    public Recipe removeRecipeHasStep(RecipeHasStep recipeHasStep) {
        this.recipeHasSteps.remove(recipeHasStep);
        recipeHasStep.setRecipe(null);
        return this;
    }

    public void setRecipeHasSteps(Set<RecipeHasStep> recipeHasSteps) {
        this.recipeHasSteps = recipeHasSteps;
    }

    public Set<RecipeImage> getRecipeImages() {
        return recipeImages;
    }

    public Recipe recipeImages(Set<RecipeImage> recipeImages) {
        this.recipeImages = recipeImages;
        return this;
    }

    public Recipe addRecipeImage(RecipeImage recipeImage) {
        this.recipeImages.add(recipeImage);
        recipeImage.setRecipe(this);
        return this;
    }

    public Recipe removeRecipeImage(RecipeImage recipeImage) {
        this.recipeImages.remove(recipeImage);
        recipeImage.setRecipe(null);
        return this;
    }

    public void setRecipeImages(Set<RecipeImage> recipeImages) {
        this.recipeImages = recipeImages;
    }

    public Set<MenuRecipe> getMenuRecipes() {
        return menuRecipes;
    }

    public Recipe menuRecipes(Set<MenuRecipe> menuRecipes) {
        this.menuRecipes = menuRecipes;
        return this;
    }

    public Recipe addMenuRecipe(MenuRecipe menuRecipe) {
        this.menuRecipes.add(menuRecipe);
        menuRecipe.setRecipe(this);
        return this;
    }

    public Recipe removeMenuRecipe(MenuRecipe menuRecipe) {
        this.menuRecipes.remove(menuRecipe);
        menuRecipe.setRecipe(null);
        return this;
    }

    public void setMenuRecipes(Set<MenuRecipe> menuRecipes) {
        this.menuRecipes = menuRecipes;
    }

    public Set<RecipeComment> getRecipeComments() {
        return recipeComments;
    }

    public Recipe recipeComments(Set<RecipeComment> recipeComments) {
        this.recipeComments = recipeComments;
        return this;
    }

    public Recipe addRecipeComment(RecipeComment recipeComment) {
        this.recipeComments.add(recipeComment);
        recipeComment.setRecipe(this);
        return this;
    }

    public Recipe removeRecipeComment(RecipeComment recipeComment) {
        this.recipeComments.remove(recipeComment);
        recipeComment.setRecipe(null);
        return this;
    }

    public void setRecipeComments(Set<RecipeComment> recipeComments) {
        this.recipeComments = recipeComments;
    }

    public Set<RecipeRating> getRecipeRatings() {
        return recipeRatings;
    }

    public Recipe recipeRatings(Set<RecipeRating> recipeRatings) {
        this.recipeRatings = recipeRatings;
        return this;
    }

    public Recipe addRecipeRating(RecipeRating recipeRating) {
        this.recipeRatings.add(recipeRating);
        recipeRating.setRecipe(this);
        return this;
    }

    public Recipe removeRecipeRating(RecipeRating recipeRating) {
        this.recipeRatings.remove(recipeRating);
        recipeRating.setRecipe(null);
        return this;
    }

    public void setRecipeRatings(Set<RecipeRating> recipeRatings) {
        this.recipeRatings = recipeRatings;
    }

    public Set<ShareRecipe> getShareRecipes() {
        return shareRecipes;
    }

    public Recipe shareRecipes(Set<ShareRecipe> shareRecipes) {
        this.shareRecipes = shareRecipes;
        return this;
    }

    public Recipe addShareRecipe(ShareRecipe shareRecipe) {
        this.shareRecipes.add(shareRecipe);
        shareRecipe.setRecipe(this);
        return this;
    }

    public Recipe removeShareRecipe(ShareRecipe shareRecipe) {
        this.shareRecipes.remove(shareRecipe);
        shareRecipe.setRecipe(null);
        return this;
    }

    public void setShareRecipes(Set<ShareRecipe> shareRecipes) {
        this.shareRecipes = shareRecipes;
    }

    public Set<RecipeLike> getRecipeLikes() {
        return recipeLikes;
    }

    public Recipe recipeLikes(Set<RecipeLike> recipeLikes) {
        this.recipeLikes = recipeLikes;
        return this;
    }

    public Recipe addRecipeLike(RecipeLike recipeLike) {
        this.recipeLikes.add(recipeLike);
        recipeLike.setRecipe(this);
        return this;
    }

    public Recipe removeRecipeLike(RecipeLike recipeLike) {
        this.recipeLikes.remove(recipeLike);
        recipeLike.setRecipe(null);
        return this;
    }

    public void setRecipeLikes(Set<RecipeLike> recipeLikes) {
        this.recipeLikes = recipeLikes;
    }

    public Set<Footnote> getFootnotes() {
        return footnotes;
    }

    public Recipe footnotes(Set<Footnote> footnotes) {
        this.footnotes = footnotes;
        return this;
    }

    public Recipe addFootnote(Footnote footnote) {
        this.footnotes.add(footnote);
        footnote.setRecipe(this);
        return this;
    }

    public Recipe removeFootnote(Footnote footnote) {
        this.footnotes.remove(footnote);
        footnote.setRecipe(null);
        return this;
    }

    public void setFootnotes(Set<Footnote> footnotes) {
        this.footnotes = footnotes;
    }

    public Set<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public Recipe recipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
        return this;
    }

    public Recipe addRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.add(recipeIngredient);
        recipeIngredient.setRecipe(this);
        return this;
    }

    public Recipe removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.remove(recipeIngredient);
        recipeIngredient.setRecipe(null);
        return this;
    }

    public void setRecipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public Level getLevel() {
        return level;
    }

    public Recipe level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public FoodCategorie getFoodCategorie() {
        return foodCategorie;
    }

    public Recipe foodCategorie(FoodCategorie foodCategorie) {
        this.foodCategorie = foodCategorie;
        return this;
    }

    public void setFoodCategorie(FoodCategorie foodCategorie) {
        this.foodCategorie = foodCategorie;
    }

    public Cusine getCusine() {
        return cusine;
    }

    public Recipe cusine(Cusine cusine) {
        this.cusine = cusine;
        return this;
    }

    public void setCusine(Cusine cusine) {
        this.cusine = cusine;
    }

    public Course getCourse() {
        return course;
    }

    public Recipe course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
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
        Recipe recipe = (Recipe) o;
        if (recipe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recipe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", caloriesPerServings=" + getCaloriesPerServings() +
            ", cookTime=" + getCookTime() +
            ", description='" + getDescription() + "'" +
            ", prepTime=" + getPrepTime() +
            ", published='" + isPublished() + "'" +
            ", rating=" + getRating() +
            ", readyIn=" + getReadyIn() +
            ", title='" + getTitle() + "'" +
            ", video='" + getVideo() + "'" +
            ", yields=" + getYields() +
            "}";
    }
}

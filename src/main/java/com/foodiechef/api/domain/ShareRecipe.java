package com.foodiechef.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ShareRecipe.
 */
@Entity
@Table(name = "share_recipe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sharerecipe")
public class ShareRecipe implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JsonIgnoreProperties("shareRecipes")
    private Recipe recipe;

    @ManyToOne
    @JsonIgnoreProperties("forSharedBies")
    private UserInfo sharedBy;

    @ManyToOne
    @JsonIgnoreProperties("forSharedTos")
    private UserInfo sharedTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ShareRecipe date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public ShareRecipe recipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public UserInfo getSharedBy() {
        return sharedBy;
    }

    public ShareRecipe sharedBy(UserInfo userInfo) {
        this.sharedBy = userInfo;
        return this;
    }

    public void setSharedBy(UserInfo userInfo) {
        this.sharedBy = userInfo;
    }

    public UserInfo getSharedTo() {
        return sharedTo;
    }

    public ShareRecipe sharedTo(UserInfo userInfo) {
        this.sharedTo = userInfo;
        return this;
    }

    public void setSharedTo(UserInfo userInfo) {
        this.sharedTo = userInfo;
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
        ShareRecipe shareRecipe = (ShareRecipe) o;
        if (shareRecipe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shareRecipe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShareRecipe{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}

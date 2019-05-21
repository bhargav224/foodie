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
 * A Footnote.
 */
@Entity
@Table(name = "footnote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "footnote")
public class Footnote implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "footnote", nullable = false)
    private String footnote;

    @ManyToOne
    @JsonIgnoreProperties("footnotes")
    private Recipe recipe;

    @ManyToOne
    @JsonIgnoreProperties("footnotes")
    private UserInfo userInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFootnote() {
        return footnote;
    }

    public Footnote footnote(String footnote) {
        this.footnote = footnote;
        return this;
    }

    public void setFootnote(String footnote) {
        this.footnote = footnote;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Footnote recipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public Footnote userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
        Footnote footnote = (Footnote) o;
        if (footnote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), footnote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Footnote{" +
            "id=" + getId() +
            ", footnote='" + getFootnote() + "'" +
            "}";
    }
}

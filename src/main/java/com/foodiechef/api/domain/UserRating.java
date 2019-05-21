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
 * A UserRating.
 */
@Entity
@Table(name = "user_rating")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userrating")
public class UserRating implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @ManyToOne
    @JsonIgnoreProperties("forRateByUsers")
    private UserInfo rateByUser;

    @ManyToOne
    @JsonIgnoreProperties("forRateToUsers")
    private UserInfo rateToUser;

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

    public UserRating date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getRating() {
        return rating;
    }

    public UserRating rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public UserInfo getRateByUser() {
        return rateByUser;
    }

    public UserRating rateByUser(UserInfo userInfo) {
        this.rateByUser = userInfo;
        return this;
    }

    public void setRateByUser(UserInfo userInfo) {
        this.rateByUser = userInfo;
    }

    public UserInfo getRateToUser() {
        return rateToUser;
    }

    public UserRating rateToUser(UserInfo userInfo) {
        this.rateToUser = userInfo;
        return this;
    }

    public void setRateToUser(UserInfo userInfo) {
        this.rateToUser = userInfo;
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
        UserRating userRating = (UserRating) o;
        if (userRating.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userRating.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserRating{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", rating=" + getRating() +
            "}";
    }
}

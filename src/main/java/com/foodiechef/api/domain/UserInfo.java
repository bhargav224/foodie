package com.foodiechef.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserInfo.
 */
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userinfo")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_authenticated", nullable = false)
    private Boolean authenticated;

    @NotNull
    @Column(name = "contact", nullable = false)
    private Long contact;

    @NotNull
    @Column(name = "current_logged_in", nullable = false)
    private LocalDate currentLoggedIn;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "email_confirmation_sent_on", nullable = false)
    private LocalDate emailConfirmationSentOn;

    @NotNull
    @Column(name = "email_confirmed", nullable = false)
    private Boolean emailConfirmed;

    @NotNull
    @Column(name = "email_confirmed_on", nullable = false)
    private LocalDate emailConfirmedOn;

    @NotNull
    @Column(name = "last_logged_in", nullable = false)
    private LocalDate lastLoggedIn;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "jhi_password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "photo", nullable = false)
    private String photo;

    @NotNull
    @Column(name = "registered_on", nullable = false)
    private LocalDate registeredOn;

    @OneToOne
    @JoinColumn(unique = true)
    private UserInfo userInfo;

    @OneToOne
    @JoinColumn(unique = true)
    private ChefProfile chefProfile;

    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserInfoRole> userInfoRoles = new HashSet<>();
    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeComment> recipeComments = new HashSet<>();
    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeRating> recipeRatings = new HashSet<>();
    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecipeLike> recipeLikes = new HashSet<>();
    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Footnote> footnotes = new HashSet<>();
    @OneToMany(mappedBy = "sharedBy")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShareRecipe> forSharedBies = new HashSet<>();
    @OneToMany(mappedBy = "sharedTo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShareRecipe> forSharedTos = new HashSet<>();
    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InviteEmail> inviteEmails = new HashSet<>();
    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InviteContact> inviteContacts = new HashSet<>();
    @OneToMany(mappedBy = "rateByUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserRating> forRateByUsers = new HashSet<>();
    @OneToMany(mappedBy = "rateToUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserRating> forRateToUsers = new HashSet<>();
    @OneToOne(mappedBy = "userInfo")
    @JsonIgnore
    private UserInfo invitedBy;

    @ManyToOne
    @JsonIgnoreProperties("userInfos")
    private Restaurant restaurant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAuthenticated() {
        return authenticated;
    }

    public UserInfo authenticated(Boolean authenticated) {
        this.authenticated = authenticated;
        return this;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Long getContact() {
        return contact;
    }

    public UserInfo contact(Long contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public LocalDate getCurrentLoggedIn() {
        return currentLoggedIn;
    }

    public UserInfo currentLoggedIn(LocalDate currentLoggedIn) {
        this.currentLoggedIn = currentLoggedIn;
        return this;
    }

    public void setCurrentLoggedIn(LocalDate currentLoggedIn) {
        this.currentLoggedIn = currentLoggedIn;
    }

    public String getEmail() {
        return email;
    }

    public UserInfo email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEmailConfirmationSentOn() {
        return emailConfirmationSentOn;
    }

    public UserInfo emailConfirmationSentOn(LocalDate emailConfirmationSentOn) {
        this.emailConfirmationSentOn = emailConfirmationSentOn;
        return this;
    }

    public void setEmailConfirmationSentOn(LocalDate emailConfirmationSentOn) {
        this.emailConfirmationSentOn = emailConfirmationSentOn;
    }

    public Boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public UserInfo emailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
        return this;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public LocalDate getEmailConfirmedOn() {
        return emailConfirmedOn;
    }

    public UserInfo emailConfirmedOn(LocalDate emailConfirmedOn) {
        this.emailConfirmedOn = emailConfirmedOn;
        return this;
    }

    public void setEmailConfirmedOn(LocalDate emailConfirmedOn) {
        this.emailConfirmedOn = emailConfirmedOn;
    }

    public LocalDate getLastLoggedIn() {
        return lastLoggedIn;
    }

    public UserInfo lastLoggedIn(LocalDate lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
        return this;
    }

    public void setLastLoggedIn(LocalDate lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserInfo firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserInfo lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public UserInfo password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public UserInfo photo(String photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public UserInfo registeredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
        return this;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public UserInfo userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ChefProfile getChefProfile() {
        return chefProfile;
    }

    public UserInfo chefProfile(ChefProfile chefProfile) {
        this.chefProfile = chefProfile;
        return this;
    }

    public void setChefProfile(ChefProfile chefProfile) {
        this.chefProfile = chefProfile;
    }

    public Set<UserInfoRole> getUserInfoRoles() {
        return userInfoRoles;
    }

    public UserInfo userInfoRoles(Set<UserInfoRole> userInfoRoles) {
        this.userInfoRoles = userInfoRoles;
        return this;
    }

    public UserInfo addUserInfoRole(UserInfoRole userInfoRole) {
        this.userInfoRoles.add(userInfoRole);
        userInfoRole.setUserInfo(this);
        return this;
    }

    public UserInfo removeUserInfoRole(UserInfoRole userInfoRole) {
        this.userInfoRoles.remove(userInfoRole);
        userInfoRole.setUserInfo(null);
        return this;
    }

    public void setUserInfoRoles(Set<UserInfoRole> userInfoRoles) {
        this.userInfoRoles = userInfoRoles;
    }

    public Set<RecipeComment> getRecipeComments() {
        return recipeComments;
    }

    public UserInfo recipeComments(Set<RecipeComment> recipeComments) {
        this.recipeComments = recipeComments;
        return this;
    }

    public UserInfo addRecipeComment(RecipeComment recipeComment) {
        this.recipeComments.add(recipeComment);
        recipeComment.setUserInfo(this);
        return this;
    }

    public UserInfo removeRecipeComment(RecipeComment recipeComment) {
        this.recipeComments.remove(recipeComment);
        recipeComment.setUserInfo(null);
        return this;
    }

    public void setRecipeComments(Set<RecipeComment> recipeComments) {
        this.recipeComments = recipeComments;
    }

    public Set<RecipeRating> getRecipeRatings() {
        return recipeRatings;
    }

    public UserInfo recipeRatings(Set<RecipeRating> recipeRatings) {
        this.recipeRatings = recipeRatings;
        return this;
    }

    public UserInfo addRecipeRating(RecipeRating recipeRating) {
        this.recipeRatings.add(recipeRating);
        recipeRating.setUserInfo(this);
        return this;
    }

    public UserInfo removeRecipeRating(RecipeRating recipeRating) {
        this.recipeRatings.remove(recipeRating);
        recipeRating.setUserInfo(null);
        return this;
    }

    public void setRecipeRatings(Set<RecipeRating> recipeRatings) {
        this.recipeRatings = recipeRatings;
    }

    public Set<RecipeLike> getRecipeLikes() {
        return recipeLikes;
    }

    public UserInfo recipeLikes(Set<RecipeLike> recipeLikes) {
        this.recipeLikes = recipeLikes;
        return this;
    }

    public UserInfo addRecipeLike(RecipeLike recipeLike) {
        this.recipeLikes.add(recipeLike);
        recipeLike.setUserInfo(this);
        return this;
    }

    public UserInfo removeRecipeLike(RecipeLike recipeLike) {
        this.recipeLikes.remove(recipeLike);
        recipeLike.setUserInfo(null);
        return this;
    }

    public void setRecipeLikes(Set<RecipeLike> recipeLikes) {
        this.recipeLikes = recipeLikes;
    }

    public Set<Footnote> getFootnotes() {
        return footnotes;
    }

    public UserInfo footnotes(Set<Footnote> footnotes) {
        this.footnotes = footnotes;
        return this;
    }

    public UserInfo addFootnote(Footnote footnote) {
        this.footnotes.add(footnote);
        footnote.setUserInfo(this);
        return this;
    }

    public UserInfo removeFootnote(Footnote footnote) {
        this.footnotes.remove(footnote);
        footnote.setUserInfo(null);
        return this;
    }

    public void setFootnotes(Set<Footnote> footnotes) {
        this.footnotes = footnotes;
    }

    public Set<ShareRecipe> getForSharedBies() {
        return forSharedBies;
    }

    public UserInfo forSharedBies(Set<ShareRecipe> shareRecipes) {
        this.forSharedBies = shareRecipes;
        return this;
    }

    public UserInfo addForSharedBy(ShareRecipe shareRecipe) {
        this.forSharedBies.add(shareRecipe);
        shareRecipe.setSharedBy(this);
        return this;
    }

    public UserInfo removeForSharedBy(ShareRecipe shareRecipe) {
        this.forSharedBies.remove(shareRecipe);
        shareRecipe.setSharedBy(null);
        return this;
    }

    public void setForSharedBies(Set<ShareRecipe> shareRecipes) {
        this.forSharedBies = shareRecipes;
    }

    public Set<ShareRecipe> getForSharedTos() {
        return forSharedTos;
    }

    public UserInfo forSharedTos(Set<ShareRecipe> shareRecipes) {
        this.forSharedTos = shareRecipes;
        return this;
    }

    public UserInfo addForSharedTo(ShareRecipe shareRecipe) {
        this.forSharedTos.add(shareRecipe);
        shareRecipe.setSharedTo(this);
        return this;
    }

    public UserInfo removeForSharedTo(ShareRecipe shareRecipe) {
        this.forSharedTos.remove(shareRecipe);
        shareRecipe.setSharedTo(null);
        return this;
    }

    public void setForSharedTos(Set<ShareRecipe> shareRecipes) {
        this.forSharedTos = shareRecipes;
    }

    public Set<InviteEmail> getInviteEmails() {
        return inviteEmails;
    }

    public UserInfo inviteEmails(Set<InviteEmail> inviteEmails) {
        this.inviteEmails = inviteEmails;
        return this;
    }

    public UserInfo addInviteEmail(InviteEmail inviteEmail) {
        this.inviteEmails.add(inviteEmail);
        inviteEmail.setUserInfo(this);
        return this;
    }

    public UserInfo removeInviteEmail(InviteEmail inviteEmail) {
        this.inviteEmails.remove(inviteEmail);
        inviteEmail.setUserInfo(null);
        return this;
    }

    public void setInviteEmails(Set<InviteEmail> inviteEmails) {
        this.inviteEmails = inviteEmails;
    }

    public Set<InviteContact> getInviteContacts() {
        return inviteContacts;
    }

    public UserInfo inviteContacts(Set<InviteContact> inviteContacts) {
        this.inviteContacts = inviteContacts;
        return this;
    }

    public UserInfo addInviteContact(InviteContact inviteContact) {
        this.inviteContacts.add(inviteContact);
        inviteContact.setUserInfo(this);
        return this;
    }

    public UserInfo removeInviteContact(InviteContact inviteContact) {
        this.inviteContacts.remove(inviteContact);
        inviteContact.setUserInfo(null);
        return this;
    }

    public void setInviteContacts(Set<InviteContact> inviteContacts) {
        this.inviteContacts = inviteContacts;
    }

    public Set<UserRating> getForRateByUsers() {
        return forRateByUsers;
    }

    public UserInfo forRateByUsers(Set<UserRating> userRatings) {
        this.forRateByUsers = userRatings;
        return this;
    }

    public UserInfo addForRateByUser(UserRating userRating) {
        this.forRateByUsers.add(userRating);
        userRating.setRateByUser(this);
        return this;
    }

    public UserInfo removeForRateByUser(UserRating userRating) {
        this.forRateByUsers.remove(userRating);
        userRating.setRateByUser(null);
        return this;
    }

    public void setForRateByUsers(Set<UserRating> userRatings) {
        this.forRateByUsers = userRatings;
    }

    public Set<UserRating> getForRateToUsers() {
        return forRateToUsers;
    }

    public UserInfo forRateToUsers(Set<UserRating> userRatings) {
        this.forRateToUsers = userRatings;
        return this;
    }

    public UserInfo addForRateToUser(UserRating userRating) {
        this.forRateToUsers.add(userRating);
        userRating.setRateToUser(this);
        return this;
    }

    public UserInfo removeForRateToUser(UserRating userRating) {
        this.forRateToUsers.remove(userRating);
        userRating.setRateToUser(null);
        return this;
    }

    public void setForRateToUsers(Set<UserRating> userRatings) {
        this.forRateToUsers = userRatings;
    }

    public UserInfo getInvitedBy() {
        return invitedBy;
    }

    public UserInfo invitedBy(UserInfo userInfo) {
        this.invitedBy = userInfo;
        return this;
    }

    public void setInvitedBy(UserInfo userInfo) {
        this.invitedBy = userInfo;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public UserInfo restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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
        UserInfo userInfo = (UserInfo) o;
        if (userInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", authenticated='" + isAuthenticated() + "'" +
            ", contact=" + getContact() +
            ", currentLoggedIn='" + getCurrentLoggedIn() + "'" +
            ", email='" + getEmail() + "'" +
            ", emailConfirmationSentOn='" + getEmailConfirmationSentOn() + "'" +
            ", emailConfirmed='" + isEmailConfirmed() + "'" +
            ", emailConfirmedOn='" + getEmailConfirmedOn() + "'" +
            ", lastLoggedIn='" + getLastLoggedIn() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", password='" + getPassword() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", registeredOn='" + getRegisteredOn() + "'" +
            "}";
    }
}

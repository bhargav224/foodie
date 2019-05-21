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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the UserInfo entity. This class is used in UserInfoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /user-infos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserInfoCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter authenticated;

    private LongFilter contact;

    private LocalDateFilter currentLoggedIn;

    private StringFilter email;

    private LocalDateFilter emailConfirmationSentOn;

    private BooleanFilter emailConfirmed;

    private LocalDateFilter emailConfirmedOn;

    private LocalDateFilter lastLoggedIn;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter password;

    private StringFilter photo;

    private LocalDateFilter registeredOn;

    private LongFilter userInfoId;

    private LongFilter chefProfileId;

    private LongFilter userInfoRoleId;

    private LongFilter recipeCommentId;

    private LongFilter recipeRatingId;

    private LongFilter recipeLikeId;

    private LongFilter footnoteId;

    private LongFilter forSharedById;

    private LongFilter forSharedToId;

    private LongFilter inviteEmailId;

    private LongFilter inviteContactId;

    private LongFilter forRateByUserId;

    private LongFilter forRateToUserId;

    private LongFilter invitedById;

    private LongFilter restaurantId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(BooleanFilter authenticated) {
        this.authenticated = authenticated;
    }

    public LongFilter getContact() {
        return contact;
    }

    public void setContact(LongFilter contact) {
        this.contact = contact;
    }

    public LocalDateFilter getCurrentLoggedIn() {
        return currentLoggedIn;
    }

    public void setCurrentLoggedIn(LocalDateFilter currentLoggedIn) {
        this.currentLoggedIn = currentLoggedIn;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getEmailConfirmationSentOn() {
        return emailConfirmationSentOn;
    }

    public void setEmailConfirmationSentOn(LocalDateFilter emailConfirmationSentOn) {
        this.emailConfirmationSentOn = emailConfirmationSentOn;
    }

    public BooleanFilter getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(BooleanFilter emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public LocalDateFilter getEmailConfirmedOn() {
        return emailConfirmedOn;
    }

    public void setEmailConfirmedOn(LocalDateFilter emailConfirmedOn) {
        this.emailConfirmedOn = emailConfirmedOn;
    }

    public LocalDateFilter getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(LocalDateFilter lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getPhoto() {
        return photo;
    }

    public void setPhoto(StringFilter photo) {
        this.photo = photo;
    }

    public LocalDateFilter getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDateFilter registeredOn) {
        this.registeredOn = registeredOn;
    }

    public LongFilter getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(LongFilter userInfoId) {
        this.userInfoId = userInfoId;
    }

    public LongFilter getChefProfileId() {
        return chefProfileId;
    }

    public void setChefProfileId(LongFilter chefProfileId) {
        this.chefProfileId = chefProfileId;
    }

    public LongFilter getUserInfoRoleId() {
        return userInfoRoleId;
    }

    public void setUserInfoRoleId(LongFilter userInfoRoleId) {
        this.userInfoRoleId = userInfoRoleId;
    }

    public LongFilter getRecipeCommentId() {
        return recipeCommentId;
    }

    public void setRecipeCommentId(LongFilter recipeCommentId) {
        this.recipeCommentId = recipeCommentId;
    }

    public LongFilter getRecipeRatingId() {
        return recipeRatingId;
    }

    public void setRecipeRatingId(LongFilter recipeRatingId) {
        this.recipeRatingId = recipeRatingId;
    }

    public LongFilter getRecipeLikeId() {
        return recipeLikeId;
    }

    public void setRecipeLikeId(LongFilter recipeLikeId) {
        this.recipeLikeId = recipeLikeId;
    }

    public LongFilter getFootnoteId() {
        return footnoteId;
    }

    public void setFootnoteId(LongFilter footnoteId) {
        this.footnoteId = footnoteId;
    }

    public LongFilter getForSharedById() {
        return forSharedById;
    }

    public void setForSharedById(LongFilter forSharedById) {
        this.forSharedById = forSharedById;
    }

    public LongFilter getForSharedToId() {
        return forSharedToId;
    }

    public void setForSharedToId(LongFilter forSharedToId) {
        this.forSharedToId = forSharedToId;
    }

    public LongFilter getInviteEmailId() {
        return inviteEmailId;
    }

    public void setInviteEmailId(LongFilter inviteEmailId) {
        this.inviteEmailId = inviteEmailId;
    }

    public LongFilter getInviteContactId() {
        return inviteContactId;
    }

    public void setInviteContactId(LongFilter inviteContactId) {
        this.inviteContactId = inviteContactId;
    }

    public LongFilter getForRateByUserId() {
        return forRateByUserId;
    }

    public void setForRateByUserId(LongFilter forRateByUserId) {
        this.forRateByUserId = forRateByUserId;
    }

    public LongFilter getForRateToUserId() {
        return forRateToUserId;
    }

    public void setForRateToUserId(LongFilter forRateToUserId) {
        this.forRateToUserId = forRateToUserId;
    }

    public LongFilter getInvitedById() {
        return invitedById;
    }

    public void setInvitedById(LongFilter invitedById) {
        this.invitedById = invitedById;
    }

    public LongFilter getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(LongFilter restaurantId) {
        this.restaurantId = restaurantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserInfoCriteria that = (UserInfoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(authenticated, that.authenticated) &&
            Objects.equals(contact, that.contact) &&
            Objects.equals(currentLoggedIn, that.currentLoggedIn) &&
            Objects.equals(email, that.email) &&
            Objects.equals(emailConfirmationSentOn, that.emailConfirmationSentOn) &&
            Objects.equals(emailConfirmed, that.emailConfirmed) &&
            Objects.equals(emailConfirmedOn, that.emailConfirmedOn) &&
            Objects.equals(lastLoggedIn, that.lastLoggedIn) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(password, that.password) &&
            Objects.equals(photo, that.photo) &&
            Objects.equals(registeredOn, that.registeredOn) &&
            Objects.equals(userInfoId, that.userInfoId) &&
            Objects.equals(chefProfileId, that.chefProfileId) &&
            Objects.equals(userInfoRoleId, that.userInfoRoleId) &&
            Objects.equals(recipeCommentId, that.recipeCommentId) &&
            Objects.equals(recipeRatingId, that.recipeRatingId) &&
            Objects.equals(recipeLikeId, that.recipeLikeId) &&
            Objects.equals(footnoteId, that.footnoteId) &&
            Objects.equals(forSharedById, that.forSharedById) &&
            Objects.equals(forSharedToId, that.forSharedToId) &&
            Objects.equals(inviteEmailId, that.inviteEmailId) &&
            Objects.equals(inviteContactId, that.inviteContactId) &&
            Objects.equals(forRateByUserId, that.forRateByUserId) &&
            Objects.equals(forRateToUserId, that.forRateToUserId) &&
            Objects.equals(invitedById, that.invitedById) &&
            Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        authenticated,
        contact,
        currentLoggedIn,
        email,
        emailConfirmationSentOn,
        emailConfirmed,
        emailConfirmedOn,
        lastLoggedIn,
        firstName,
        lastName,
        password,
        photo,
        registeredOn,
        userInfoId,
        chefProfileId,
        userInfoRoleId,
        recipeCommentId,
        recipeRatingId,
        recipeLikeId,
        footnoteId,
        forSharedById,
        forSharedToId,
        inviteEmailId,
        inviteContactId,
        forRateByUserId,
        forRateToUserId,
        invitedById,
        restaurantId
        );
    }

    @Override
    public String toString() {
        return "UserInfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (authenticated != null ? "authenticated=" + authenticated + ", " : "") +
                (contact != null ? "contact=" + contact + ", " : "") +
                (currentLoggedIn != null ? "currentLoggedIn=" + currentLoggedIn + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (emailConfirmationSentOn != null ? "emailConfirmationSentOn=" + emailConfirmationSentOn + ", " : "") +
                (emailConfirmed != null ? "emailConfirmed=" + emailConfirmed + ", " : "") +
                (emailConfirmedOn != null ? "emailConfirmedOn=" + emailConfirmedOn + ", " : "") +
                (lastLoggedIn != null ? "lastLoggedIn=" + lastLoggedIn + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (photo != null ? "photo=" + photo + ", " : "") +
                (registeredOn != null ? "registeredOn=" + registeredOn + ", " : "") +
                (userInfoId != null ? "userInfoId=" + userInfoId + ", " : "") +
                (chefProfileId != null ? "chefProfileId=" + chefProfileId + ", " : "") +
                (userInfoRoleId != null ? "userInfoRoleId=" + userInfoRoleId + ", " : "") +
                (recipeCommentId != null ? "recipeCommentId=" + recipeCommentId + ", " : "") +
                (recipeRatingId != null ? "recipeRatingId=" + recipeRatingId + ", " : "") +
                (recipeLikeId != null ? "recipeLikeId=" + recipeLikeId + ", " : "") +
                (footnoteId != null ? "footnoteId=" + footnoteId + ", " : "") +
                (forSharedById != null ? "forSharedById=" + forSharedById + ", " : "") +
                (forSharedToId != null ? "forSharedToId=" + forSharedToId + ", " : "") +
                (inviteEmailId != null ? "inviteEmailId=" + inviteEmailId + ", " : "") +
                (inviteContactId != null ? "inviteContactId=" + inviteContactId + ", " : "") +
                (forRateByUserId != null ? "forRateByUserId=" + forRateByUserId + ", " : "") +
                (forRateToUserId != null ? "forRateToUserId=" + forRateToUserId + ", " : "") +
                (invitedById != null ? "invitedById=" + invitedById + ", " : "") +
                (restaurantId != null ? "restaurantId=" + restaurantId + ", " : "") +
            "}";
    }

}

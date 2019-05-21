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
 * Criteria class for the ChefProfile entity. This class is used in ChefProfileResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /chef-profiles?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChefProfileCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter photo;

    private StringFilter speciality;

    private StringFilter type;

    private StringFilter website;

    private LongFilter userInfoId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPhoto() {
        return photo;
    }

    public void setPhoto(StringFilter photo) {
        this.photo = photo;
    }

    public StringFilter getSpeciality() {
        return speciality;
    }

    public void setSpeciality(StringFilter speciality) {
        this.speciality = speciality;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public LongFilter getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(LongFilter userInfoId) {
        this.userInfoId = userInfoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChefProfileCriteria that = (ChefProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(photo, that.photo) &&
            Objects.equals(speciality, that.speciality) &&
            Objects.equals(type, that.type) &&
            Objects.equals(website, that.website) &&
            Objects.equals(userInfoId, that.userInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        photo,
        speciality,
        type,
        website,
        userInfoId
        );
    }

    @Override
    public String toString() {
        return "ChefProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (photo != null ? "photo=" + photo + ", " : "") +
                (speciality != null ? "speciality=" + speciality + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (website != null ? "website=" + website + ", " : "") +
                (userInfoId != null ? "userInfoId=" + userInfoId + ", " : "") +
            "}";
    }

}

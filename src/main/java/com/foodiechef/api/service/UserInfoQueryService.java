package com.foodiechef.api.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.domain.*; // for static metamodels
import com.foodiechef.api.repository.UserInfoRepository;
import com.foodiechef.api.repository.search.UserInfoSearchRepository;
import com.foodiechef.api.service.dto.UserInfoCriteria;

/**
 * Service for executing complex queries for UserInfo entities in the database.
 * The main input is a {@link UserInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserInfo} or a {@link Page} of {@link UserInfo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserInfoQueryService extends QueryService<UserInfo> {

    private final Logger log = LoggerFactory.getLogger(UserInfoQueryService.class);

    private final UserInfoRepository userInfoRepository;

    private final UserInfoSearchRepository userInfoSearchRepository;

    public UserInfoQueryService(UserInfoRepository userInfoRepository, UserInfoSearchRepository userInfoSearchRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoSearchRepository = userInfoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link UserInfo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserInfo> findByCriteria(UserInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserInfo> specification = createSpecification(criteria);
        return userInfoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserInfo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserInfo> findByCriteria(UserInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserInfo> specification = createSpecification(criteria);
        return userInfoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserInfo> specification = createSpecification(criteria);
        return userInfoRepository.count(specification);
    }

    /**
     * Function to convert UserInfoCriteria to a {@link Specification}
     */
    private Specification<UserInfo> createSpecification(UserInfoCriteria criteria) {
        Specification<UserInfo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserInfo_.id));
            }
            if (criteria.getAuthenticated() != null) {
                specification = specification.and(buildSpecification(criteria.getAuthenticated(), UserInfo_.authenticated));
            }
            if (criteria.getContact() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContact(), UserInfo_.contact));
            }
            if (criteria.getCurrentLoggedIn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentLoggedIn(), UserInfo_.currentLoggedIn));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), UserInfo_.email));
            }
            if (criteria.getEmailConfirmationSentOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmailConfirmationSentOn(), UserInfo_.emailConfirmationSentOn));
            }
            if (criteria.getEmailConfirmed() != null) {
                specification = specification.and(buildSpecification(criteria.getEmailConfirmed(), UserInfo_.emailConfirmed));
            }
            if (criteria.getEmailConfirmedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmailConfirmedOn(), UserInfo_.emailConfirmedOn));
            }
            if (criteria.getLastLoggedIn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastLoggedIn(), UserInfo_.lastLoggedIn));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), UserInfo_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), UserInfo_.lastName));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), UserInfo_.password));
            }
            if (criteria.getPhoto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoto(), UserInfo_.photo));
            }
            if (criteria.getRegisteredOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegisteredOn(), UserInfo_.registeredOn));
            }
            if (criteria.getUserInfoId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoId(),
                    root -> root.join(UserInfo_.userInfo, JoinType.LEFT).get(UserInfo_.id)));
            }
            if (criteria.getChefProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getChefProfileId(),
                    root -> root.join(UserInfo_.chefProfile, JoinType.LEFT).get(ChefProfile_.id)));
            }
            if (criteria.getUserInfoRoleId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserInfoRoleId(),
                    root -> root.join(UserInfo_.userInfoRoles, JoinType.LEFT).get(UserInfoRole_.id)));
            }
            if (criteria.getRecipeCommentId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeCommentId(),
                    root -> root.join(UserInfo_.recipeComments, JoinType.LEFT).get(RecipeComment_.id)));
            }
            if (criteria.getRecipeRatingId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeRatingId(),
                    root -> root.join(UserInfo_.recipeRatings, JoinType.LEFT).get(RecipeRating_.id)));
            }
            if (criteria.getRecipeLikeId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipeLikeId(),
                    root -> root.join(UserInfo_.recipeLikes, JoinType.LEFT).get(RecipeLike_.id)));
            }
            if (criteria.getFootnoteId() != null) {
                specification = specification.and(buildSpecification(criteria.getFootnoteId(),
                    root -> root.join(UserInfo_.footnotes, JoinType.LEFT).get(Footnote_.id)));
            }
            if (criteria.getForSharedById() != null) {
                specification = specification.and(buildSpecification(criteria.getForSharedById(),
                    root -> root.join(UserInfo_.forSharedBies, JoinType.LEFT).get(ShareRecipe_.id)));
            }
            if (criteria.getForSharedToId() != null) {
                specification = specification.and(buildSpecification(criteria.getForSharedToId(),
                    root -> root.join(UserInfo_.forSharedTos, JoinType.LEFT).get(ShareRecipe_.id)));
            }
            if (criteria.getInviteEmailId() != null) {
                specification = specification.and(buildSpecification(criteria.getInviteEmailId(),
                    root -> root.join(UserInfo_.inviteEmails, JoinType.LEFT).get(InviteEmail_.id)));
            }
            if (criteria.getInviteContactId() != null) {
                specification = specification.and(buildSpecification(criteria.getInviteContactId(),
                    root -> root.join(UserInfo_.inviteContacts, JoinType.LEFT).get(InviteContact_.id)));
            }
            if (criteria.getForRateByUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getForRateByUserId(),
                    root -> root.join(UserInfo_.forRateByUsers, JoinType.LEFT).get(UserRating_.id)));
            }
            if (criteria.getForRateToUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getForRateToUserId(),
                    root -> root.join(UserInfo_.forRateToUsers, JoinType.LEFT).get(UserRating_.id)));
            }
            if (criteria.getInvitedById() != null) {
                specification = specification.and(buildSpecification(criteria.getInvitedById(),
                    root -> root.join(UserInfo_.invitedBy, JoinType.LEFT).get(UserInfo_.id)));
            }
            if (criteria.getRestaurantId() != null) {
                specification = specification.and(buildSpecification(criteria.getRestaurantId(),
                    root -> root.join(UserInfo_.restaurant, JoinType.LEFT).get(Restaurant_.id)));
            }
        }
        return specification;
    }
}

package com.foodiechef.api.service;

import com.foodiechef.api.domain.UserInfo;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing UserInfo.
 */
public interface UserInfoService {

    /**
     * Save a userInfo.
     *
     * @param userInfo the entity to save
     * @return the persisted entity
     */
    UserInfo save(UserInfo userInfo);

    /**
     * Get all the userInfos.
     *
     * @return the list of entities
     */
    List<UserInfo> findAll();
    /**
     * Get all the UserInfoDTO where InvitedBy is null.
     *
     * @return the list of entities
     */
    List<UserInfo> findAllWhereInvitedByIsNull();


    /**
     * Get the "id" userInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserInfo> findOne(Long id);

    /**
     * Delete the "id" userInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<UserInfo> search(String query);
}

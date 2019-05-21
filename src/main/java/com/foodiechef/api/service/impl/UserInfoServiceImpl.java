package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.UserInfoService;
import com.foodiechef.api.domain.UserInfo;
import com.foodiechef.api.repository.UserInfoRepository;
import com.foodiechef.api.repository.search.UserInfoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserInfo.
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    private final Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final UserInfoRepository userInfoRepository;

    private final UserInfoSearchRepository userInfoSearchRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserInfoSearchRepository userInfoSearchRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoSearchRepository = userInfoSearchRepository;
    }

    /**
     * Save a userInfo.
     *
     * @param userInfo the entity to save
     * @return the persisted entity
     */
    @Override
    public UserInfo save(UserInfo userInfo) {
        log.debug("Request to save UserInfo : {}", userInfo);
        UserInfo result = userInfoRepository.save(userInfo);
        userInfoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userInfos.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserInfo> findAll() {
        log.debug("Request to get all UserInfos");
        return userInfoRepository.findAll();
    }



    /**
     *  get all the userInfos where InvitedBy is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<UserInfo> findAllWhereInvitedByIsNull() {
        log.debug("Request to get all userInfos where InvitedBy is null");
        return StreamSupport
            .stream(userInfoRepository.findAll().spliterator(), false)
            .filter(userInfo -> userInfo.getInvitedBy() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one userInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserInfo> findOne(Long id) {
        log.debug("Request to get UserInfo : {}", id);
        return userInfoRepository.findById(id);
    }

    /**
     * Delete the userInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserInfo : {}", id);
        userInfoRepository.deleteById(id);
        userInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the userInfo corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserInfo> search(String query) {
        log.debug("Request to search UserInfos for query {}", query);
        return StreamSupport
            .stream(userInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.ChefProfileService;
import com.foodiechef.api.domain.ChefProfile;
import com.foodiechef.api.repository.ChefProfileRepository;
import com.foodiechef.api.repository.search.ChefProfileSearchRepository;
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
 * Service Implementation for managing ChefProfile.
 */
@Service
@Transactional
public class ChefProfileServiceImpl implements ChefProfileService {

    private final Logger log = LoggerFactory.getLogger(ChefProfileServiceImpl.class);

    private final ChefProfileRepository chefProfileRepository;

    private final ChefProfileSearchRepository chefProfileSearchRepository;

    public ChefProfileServiceImpl(ChefProfileRepository chefProfileRepository, ChefProfileSearchRepository chefProfileSearchRepository) {
        this.chefProfileRepository = chefProfileRepository;
        this.chefProfileSearchRepository = chefProfileSearchRepository;
    }

    /**
     * Save a chefProfile.
     *
     * @param chefProfile the entity to save
     * @return the persisted entity
     */
    @Override
    public ChefProfile save(ChefProfile chefProfile) {
        log.debug("Request to save ChefProfile : {}", chefProfile);
        ChefProfile result = chefProfileRepository.save(chefProfile);
        chefProfileSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the chefProfiles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChefProfile> findAll() {
        log.debug("Request to get all ChefProfiles");
        return chefProfileRepository.findAll();
    }



    /**
     *  get all the chefProfiles where UserInfo is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ChefProfile> findAllWhereUserInfoIsNull() {
        log.debug("Request to get all chefProfiles where UserInfo is null");
        return StreamSupport
            .stream(chefProfileRepository.findAll().spliterator(), false)
            .filter(chefProfile -> chefProfile.getUserInfo() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one chefProfile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChefProfile> findOne(Long id) {
        log.debug("Request to get ChefProfile : {}", id);
        return chefProfileRepository.findById(id);
    }

    /**
     * Delete the chefProfile by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChefProfile : {}", id);
        chefProfileRepository.deleteById(id);
        chefProfileSearchRepository.deleteById(id);
    }

    /**
     * Search for the chefProfile corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChefProfile> search(String query) {
        log.debug("Request to search ChefProfiles for query {}", query);
        return StreamSupport
            .stream(chefProfileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

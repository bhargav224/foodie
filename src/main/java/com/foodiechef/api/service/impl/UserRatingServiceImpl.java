package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.UserRatingService;
import com.foodiechef.api.domain.UserRating;
import com.foodiechef.api.repository.UserRatingRepository;
import com.foodiechef.api.repository.search.UserRatingSearchRepository;
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
 * Service Implementation for managing UserRating.
 */
@Service
@Transactional
public class UserRatingServiceImpl implements UserRatingService {

    private final Logger log = LoggerFactory.getLogger(UserRatingServiceImpl.class);

    private final UserRatingRepository userRatingRepository;

    private final UserRatingSearchRepository userRatingSearchRepository;

    public UserRatingServiceImpl(UserRatingRepository userRatingRepository, UserRatingSearchRepository userRatingSearchRepository) {
        this.userRatingRepository = userRatingRepository;
        this.userRatingSearchRepository = userRatingSearchRepository;
    }

    /**
     * Save a userRating.
     *
     * @param userRating the entity to save
     * @return the persisted entity
     */
    @Override
    public UserRating save(UserRating userRating) {
        log.debug("Request to save UserRating : {}", userRating);
        UserRating result = userRatingRepository.save(userRating);
        userRatingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userRatings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserRating> findAll() {
        log.debug("Request to get all UserRatings");
        return userRatingRepository.findAll();
    }


    /**
     * Get one userRating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserRating> findOne(Long id) {
        log.debug("Request to get UserRating : {}", id);
        return userRatingRepository.findById(id);
    }

    /**
     * Delete the userRating by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRating : {}", id);
        userRatingRepository.deleteById(id);
        userRatingSearchRepository.deleteById(id);
    }

    /**
     * Search for the userRating corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserRating> search(String query) {
        log.debug("Request to search UserRatings for query {}", query);
        return StreamSupport
            .stream(userRatingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

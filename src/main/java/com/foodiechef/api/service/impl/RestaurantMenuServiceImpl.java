package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.RestaurantMenuService;
import com.foodiechef.api.domain.RestaurantMenu;
import com.foodiechef.api.repository.RestaurantMenuRepository;
import com.foodiechef.api.repository.search.RestaurantMenuSearchRepository;
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
 * Service Implementation for managing RestaurantMenu.
 */
@Service
@Transactional
public class RestaurantMenuServiceImpl implements RestaurantMenuService {

    private final Logger log = LoggerFactory.getLogger(RestaurantMenuServiceImpl.class);

    private final RestaurantMenuRepository restaurantMenuRepository;

    private final RestaurantMenuSearchRepository restaurantMenuSearchRepository;

    public RestaurantMenuServiceImpl(RestaurantMenuRepository restaurantMenuRepository, RestaurantMenuSearchRepository restaurantMenuSearchRepository) {
        this.restaurantMenuRepository = restaurantMenuRepository;
        this.restaurantMenuSearchRepository = restaurantMenuSearchRepository;
    }

    /**
     * Save a restaurantMenu.
     *
     * @param restaurantMenu the entity to save
     * @return the persisted entity
     */
    @Override
    public RestaurantMenu save(RestaurantMenu restaurantMenu) {
        log.debug("Request to save RestaurantMenu : {}", restaurantMenu);
        RestaurantMenu result = restaurantMenuRepository.save(restaurantMenu);
        restaurantMenuSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the restaurantMenus.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantMenu> findAll() {
        log.debug("Request to get all RestaurantMenus");
        return restaurantMenuRepository.findAll();
    }


    /**
     * Get one restaurantMenu by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurantMenu> findOne(Long id) {
        log.debug("Request to get RestaurantMenu : {}", id);
        return restaurantMenuRepository.findById(id);
    }

    /**
     * Delete the restaurantMenu by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RestaurantMenu : {}", id);
        restaurantMenuRepository.deleteById(id);
        restaurantMenuSearchRepository.deleteById(id);
    }

    /**
     * Search for the restaurantMenu corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantMenu> search(String query) {
        log.debug("Request to search RestaurantMenus for query {}", query);
        return StreamSupport
            .stream(restaurantMenuSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

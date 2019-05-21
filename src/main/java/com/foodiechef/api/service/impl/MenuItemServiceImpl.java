package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.MenuItemService;
import com.foodiechef.api.domain.MenuItem;
import com.foodiechef.api.repository.MenuItemRepository;
import com.foodiechef.api.repository.search.MenuItemSearchRepository;
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
 * Service Implementation for managing MenuItem.
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final Logger log = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemSearchRepository menuItemSearchRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemSearchRepository menuItemSearchRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemSearchRepository = menuItemSearchRepository;
    }

    /**
     * Save a menuItem.
     *
     * @param menuItem the entity to save
     * @return the persisted entity
     */
    @Override
    public MenuItem save(MenuItem menuItem) {
        log.debug("Request to save MenuItem : {}", menuItem);
        MenuItem result = menuItemRepository.save(menuItem);
        menuItemSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the menuItems.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> findAll() {
        log.debug("Request to get all MenuItems");
        return menuItemRepository.findAll();
    }


    /**
     * Get one menuItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItem> findOne(Long id) {
        log.debug("Request to get MenuItem : {}", id);
        return menuItemRepository.findById(id);
    }

    /**
     * Delete the menuItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
        menuItemSearchRepository.deleteById(id);
    }

    /**
     * Search for the menuItem corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> search(String query) {
        log.debug("Request to search MenuItems for query {}", query);
        return StreamSupport
            .stream(menuItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

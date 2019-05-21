package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.MenuItem;
import com.foodiechef.api.service.MenuItemService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.MenuItemCriteria;
import com.foodiechef.api.service.MenuItemQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MenuItem.
 */
@RestController
@RequestMapping("/api")
public class MenuItemResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemResource.class);

    private static final String ENTITY_NAME = "menuItem";

    private final MenuItemService menuItemService;

    private final MenuItemQueryService menuItemQueryService;

    public MenuItemResource(MenuItemService menuItemService, MenuItemQueryService menuItemQueryService) {
        this.menuItemService = menuItemService;
        this.menuItemQueryService = menuItemQueryService;
    }

    /**
     * POST  /menu-items : Create a new menuItem.
     *
     * @param menuItem the menuItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new menuItem, or with status 400 (Bad Request) if the menuItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/menu-items")
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItem);
        if (menuItem.getId() != null) {
            throw new BadRequestAlertException("A new menuItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuItem result = menuItemService.save(menuItem);
        return ResponseEntity.created(new URI("/api/menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /menu-items : Updates an existing menuItem.
     *
     * @param menuItem the menuItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated menuItem,
     * or with status 400 (Bad Request) if the menuItem is not valid,
     * or with status 500 (Internal Server Error) if the menuItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/menu-items")
    public ResponseEntity<MenuItem> updateMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to update MenuItem : {}", menuItem);
        if (menuItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MenuItem result = menuItemService.save(menuItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, menuItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /menu-items : get all the menuItems.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of menuItems in body
     */
    @GetMapping("/menu-items")
    public ResponseEntity<List<MenuItem>> getAllMenuItems(MenuItemCriteria criteria) {
        log.debug("REST request to get MenuItems by criteria: {}", criteria);
        List<MenuItem> entityList = menuItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /menu-items/count : count all the menuItems.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/menu-items/count")
    public ResponseEntity<Long> countMenuItems(MenuItemCriteria criteria) {
        log.debug("REST request to count MenuItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(menuItemQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /menu-items/:id : get the "id" menuItem.
     *
     * @param id the id of the menuItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the menuItem, or with status 404 (Not Found)
     */
    @GetMapping("/menu-items/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        log.debug("REST request to get MenuItem : {}", id);
        Optional<MenuItem> menuItem = menuItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuItem);
    }

    /**
     * DELETE  /menu-items/:id : delete the "id" menuItem.
     *
     * @param id the id of the menuItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/menu-items?query=:query : search for the menuItem corresponding
     * to the query.
     *
     * @param query the query of the menuItem search
     * @return the result of the search
     */
    @GetMapping("/_search/menu-items")
    public List<MenuItem> searchMenuItems(@RequestParam String query) {
        log.debug("REST request to search MenuItems for query {}", query);
        return menuItemService.search(query);
    }

}

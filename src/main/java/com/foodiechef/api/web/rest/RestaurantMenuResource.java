package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.RestaurantMenu;
import com.foodiechef.api.service.RestaurantMenuService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.RestaurantMenuCriteria;
import com.foodiechef.api.service.RestaurantMenuQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RestaurantMenu.
 */
@RestController
@RequestMapping("/api")
public class RestaurantMenuResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantMenuResource.class);

    private static final String ENTITY_NAME = "restaurantMenu";

    private final RestaurantMenuService restaurantMenuService;

    private final RestaurantMenuQueryService restaurantMenuQueryService;

    public RestaurantMenuResource(RestaurantMenuService restaurantMenuService, RestaurantMenuQueryService restaurantMenuQueryService) {
        this.restaurantMenuService = restaurantMenuService;
        this.restaurantMenuQueryService = restaurantMenuQueryService;
    }

    /**
     * POST  /restaurant-menus : Create a new restaurantMenu.
     *
     * @param restaurantMenu the restaurantMenu to create
     * @return the ResponseEntity with status 201 (Created) and with body the new restaurantMenu, or with status 400 (Bad Request) if the restaurantMenu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/restaurant-menus")
    public ResponseEntity<RestaurantMenu> createRestaurantMenu(@RequestBody RestaurantMenu restaurantMenu) throws URISyntaxException {
        log.debug("REST request to save RestaurantMenu : {}", restaurantMenu);
        if (restaurantMenu.getId() != null) {
            throw new BadRequestAlertException("A new restaurantMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurantMenu result = restaurantMenuService.save(restaurantMenu);
        return ResponseEntity.created(new URI("/api/restaurant-menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /restaurant-menus : Updates an existing restaurantMenu.
     *
     * @param restaurantMenu the restaurantMenu to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated restaurantMenu,
     * or with status 400 (Bad Request) if the restaurantMenu is not valid,
     * or with status 500 (Internal Server Error) if the restaurantMenu couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/restaurant-menus")
    public ResponseEntity<RestaurantMenu> updateRestaurantMenu(@RequestBody RestaurantMenu restaurantMenu) throws URISyntaxException {
        log.debug("REST request to update RestaurantMenu : {}", restaurantMenu);
        if (restaurantMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RestaurantMenu result = restaurantMenuService.save(restaurantMenu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, restaurantMenu.getId().toString()))
            .body(result);
    }

    /**
     * GET  /restaurant-menus : get all the restaurantMenus.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of restaurantMenus in body
     */
    @GetMapping("/restaurant-menus")
    public ResponseEntity<List<RestaurantMenu>> getAllRestaurantMenus(RestaurantMenuCriteria criteria) {
        log.debug("REST request to get RestaurantMenus by criteria: {}", criteria);
        List<RestaurantMenu> entityList = restaurantMenuQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /restaurant-menus/count : count all the restaurantMenus.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/restaurant-menus/count")
    public ResponseEntity<Long> countRestaurantMenus(RestaurantMenuCriteria criteria) {
        log.debug("REST request to count RestaurantMenus by criteria: {}", criteria);
        return ResponseEntity.ok().body(restaurantMenuQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /restaurant-menus/:id : get the "id" restaurantMenu.
     *
     * @param id the id of the restaurantMenu to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the restaurantMenu, or with status 404 (Not Found)
     */
    @GetMapping("/restaurant-menus/{id}")
    public ResponseEntity<RestaurantMenu> getRestaurantMenu(@PathVariable Long id) {
        log.debug("REST request to get RestaurantMenu : {}", id);
        Optional<RestaurantMenu> restaurantMenu = restaurantMenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurantMenu);
    }

    /**
     * DELETE  /restaurant-menus/:id : delete the "id" restaurantMenu.
     *
     * @param id the id of the restaurantMenu to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/restaurant-menus/{id}")
    public ResponseEntity<Void> deleteRestaurantMenu(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantMenu : {}", id);
        restaurantMenuService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/restaurant-menus?query=:query : search for the restaurantMenu corresponding
     * to the query.
     *
     * @param query the query of the restaurantMenu search
     * @return the result of the search
     */
    @GetMapping("/_search/restaurant-menus")
    public List<RestaurantMenu> searchRestaurantMenus(@RequestParam String query) {
        log.debug("REST request to search RestaurantMenus for query {}", query);
        return restaurantMenuService.search(query);
    }

}

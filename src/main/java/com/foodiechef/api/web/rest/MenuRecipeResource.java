package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.MenuRecipe;
import com.foodiechef.api.service.MenuRecipeService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.MenuRecipeCriteria;
import com.foodiechef.api.service.MenuRecipeQueryService;
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
 * REST controller for managing MenuRecipe.
 */
@RestController
@RequestMapping("/api")
public class MenuRecipeResource {

    private final Logger log = LoggerFactory.getLogger(MenuRecipeResource.class);

    private static final String ENTITY_NAME = "menuRecipe";

    private final MenuRecipeService menuRecipeService;

    private final MenuRecipeQueryService menuRecipeQueryService;

    public MenuRecipeResource(MenuRecipeService menuRecipeService, MenuRecipeQueryService menuRecipeQueryService) {
        this.menuRecipeService = menuRecipeService;
        this.menuRecipeQueryService = menuRecipeQueryService;
    }

    /**
     * POST  /menu-recipes : Create a new menuRecipe.
     *
     * @param menuRecipe the menuRecipe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new menuRecipe, or with status 400 (Bad Request) if the menuRecipe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/menu-recipes")
    public ResponseEntity<MenuRecipe> createMenuRecipe(@RequestBody MenuRecipe menuRecipe) throws URISyntaxException {
        log.debug("REST request to save MenuRecipe : {}", menuRecipe);
        if (menuRecipe.getId() != null) {
            throw new BadRequestAlertException("A new menuRecipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuRecipe result = menuRecipeService.save(menuRecipe);
        return ResponseEntity.created(new URI("/api/menu-recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /menu-recipes : Updates an existing menuRecipe.
     *
     * @param menuRecipe the menuRecipe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated menuRecipe,
     * or with status 400 (Bad Request) if the menuRecipe is not valid,
     * or with status 500 (Internal Server Error) if the menuRecipe couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/menu-recipes")
    public ResponseEntity<MenuRecipe> updateMenuRecipe(@RequestBody MenuRecipe menuRecipe) throws URISyntaxException {
        log.debug("REST request to update MenuRecipe : {}", menuRecipe);
        if (menuRecipe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MenuRecipe result = menuRecipeService.save(menuRecipe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, menuRecipe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /menu-recipes : get all the menuRecipes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of menuRecipes in body
     */
    @GetMapping("/menu-recipes")
    public ResponseEntity<List<MenuRecipe>> getAllMenuRecipes(MenuRecipeCriteria criteria) {
        log.debug("REST request to get MenuRecipes by criteria: {}", criteria);
        List<MenuRecipe> entityList = menuRecipeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /menu-recipes/count : count all the menuRecipes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/menu-recipes/count")
    public ResponseEntity<Long> countMenuRecipes(MenuRecipeCriteria criteria) {
        log.debug("REST request to count MenuRecipes by criteria: {}", criteria);
        return ResponseEntity.ok().body(menuRecipeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /menu-recipes/:id : get the "id" menuRecipe.
     *
     * @param id the id of the menuRecipe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the menuRecipe, or with status 404 (Not Found)
     */
    @GetMapping("/menu-recipes/{id}")
    public ResponseEntity<MenuRecipe> getMenuRecipe(@PathVariable Long id) {
        log.debug("REST request to get MenuRecipe : {}", id);
        Optional<MenuRecipe> menuRecipe = menuRecipeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuRecipe);
    }

    /**
     * DELETE  /menu-recipes/:id : delete the "id" menuRecipe.
     *
     * @param id the id of the menuRecipe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/menu-recipes/{id}")
    public ResponseEntity<Void> deleteMenuRecipe(@PathVariable Long id) {
        log.debug("REST request to delete MenuRecipe : {}", id);
        menuRecipeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/menu-recipes?query=:query : search for the menuRecipe corresponding
     * to the query.
     *
     * @param query the query of the menuRecipe search
     * @return the result of the search
     */
    @GetMapping("/_search/menu-recipes")
    public List<MenuRecipe> searchMenuRecipes(@RequestParam String query) {
        log.debug("REST request to search MenuRecipes for query {}", query);
        return menuRecipeService.search(query);
    }

}

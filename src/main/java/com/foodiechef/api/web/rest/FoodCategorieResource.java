package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.FoodCategorie;
import com.foodiechef.api.service.FoodCategorieService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.FoodCategorieCriteria;
import com.foodiechef.api.service.FoodCategorieQueryService;
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
 * REST controller for managing FoodCategorie.
 */
@RestController
@RequestMapping("/api")
public class FoodCategorieResource {

    private final Logger log = LoggerFactory.getLogger(FoodCategorieResource.class);

    private static final String ENTITY_NAME = "foodCategorie";

    private final FoodCategorieService foodCategorieService;

    private final FoodCategorieQueryService foodCategorieQueryService;

    public FoodCategorieResource(FoodCategorieService foodCategorieService, FoodCategorieQueryService foodCategorieQueryService) {
        this.foodCategorieService = foodCategorieService;
        this.foodCategorieQueryService = foodCategorieQueryService;
    }

    /**
     * POST  /food-categories : Create a new foodCategorie.
     *
     * @param foodCategorie the foodCategorie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new foodCategorie, or with status 400 (Bad Request) if the foodCategorie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/food-categories")
    public ResponseEntity<FoodCategorie> createFoodCategorie(@Valid @RequestBody FoodCategorie foodCategorie) throws URISyntaxException {
        log.debug("REST request to save FoodCategorie : {}", foodCategorie);
        if (foodCategorie.getId() != null) {
            throw new BadRequestAlertException("A new foodCategorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoodCategorie result = foodCategorieService.save(foodCategorie);
        return ResponseEntity.created(new URI("/api/food-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /food-categories : Updates an existing foodCategorie.
     *
     * @param foodCategorie the foodCategorie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated foodCategorie,
     * or with status 400 (Bad Request) if the foodCategorie is not valid,
     * or with status 500 (Internal Server Error) if the foodCategorie couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/food-categories")
    public ResponseEntity<FoodCategorie> updateFoodCategorie(@Valid @RequestBody FoodCategorie foodCategorie) throws URISyntaxException {
        log.debug("REST request to update FoodCategorie : {}", foodCategorie);
        if (foodCategorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FoodCategorie result = foodCategorieService.save(foodCategorie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, foodCategorie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /food-categories : get all the foodCategories.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of foodCategories in body
     */
    @GetMapping("/food-categories")
    public ResponseEntity<List<FoodCategorie>> getAllFoodCategories(FoodCategorieCriteria criteria) {
        log.debug("REST request to get FoodCategories by criteria: {}", criteria);
        List<FoodCategorie> entityList = foodCategorieQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /food-categories/count : count all the foodCategories.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/food-categories/count")
    public ResponseEntity<Long> countFoodCategories(FoodCategorieCriteria criteria) {
        log.debug("REST request to count FoodCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(foodCategorieQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /food-categories/:id : get the "id" foodCategorie.
     *
     * @param id the id of the foodCategorie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the foodCategorie, or with status 404 (Not Found)
     */
    @GetMapping("/food-categories/{id}")
    public ResponseEntity<FoodCategorie> getFoodCategorie(@PathVariable Long id) {
        log.debug("REST request to get FoodCategorie : {}", id);
        Optional<FoodCategorie> foodCategorie = foodCategorieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodCategorie);
    }

    /**
     * DELETE  /food-categories/:id : delete the "id" foodCategorie.
     *
     * @param id the id of the foodCategorie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/food-categories/{id}")
    public ResponseEntity<Void> deleteFoodCategorie(@PathVariable Long id) {
        log.debug("REST request to delete FoodCategorie : {}", id);
        foodCategorieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/food-categories?query=:query : search for the foodCategorie corresponding
     * to the query.
     *
     * @param query the query of the foodCategorie search
     * @return the result of the search
     */
    @GetMapping("/_search/food-categories")
    public List<FoodCategorie> searchFoodCategories(@RequestParam String query) {
        log.debug("REST request to search FoodCategories for query {}", query);
        return foodCategorieService.search(query);
    }

}

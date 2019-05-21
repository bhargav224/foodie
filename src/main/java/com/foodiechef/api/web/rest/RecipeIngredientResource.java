package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.RecipeIngredient;
import com.foodiechef.api.service.RecipeIngredientService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.RecipeIngredientCriteria;
import com.foodiechef.api.service.RecipeIngredientQueryService;
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
 * REST controller for managing RecipeIngredient.
 */
@RestController
@RequestMapping("/api")
public class RecipeIngredientResource {

    private final Logger log = LoggerFactory.getLogger(RecipeIngredientResource.class);

    private static final String ENTITY_NAME = "recipeIngredient";

    private final RecipeIngredientService recipeIngredientService;

    private final RecipeIngredientQueryService recipeIngredientQueryService;

    public RecipeIngredientResource(RecipeIngredientService recipeIngredientService, RecipeIngredientQueryService recipeIngredientQueryService) {
        this.recipeIngredientService = recipeIngredientService;
        this.recipeIngredientQueryService = recipeIngredientQueryService;
    }

    /**
     * POST  /recipe-ingredients : Create a new recipeIngredient.
     *
     * @param recipeIngredient the recipeIngredient to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeIngredient, or with status 400 (Bad Request) if the recipeIngredient has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipe-ingredients")
    public ResponseEntity<RecipeIngredient> createRecipeIngredient(@Valid @RequestBody RecipeIngredient recipeIngredient) throws URISyntaxException {
        log.debug("REST request to save RecipeIngredient : {}", recipeIngredient);
        if (recipeIngredient.getId() != null) {
            throw new BadRequestAlertException("A new recipeIngredient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeIngredient result = recipeIngredientService.save(recipeIngredient);
        return ResponseEntity.created(new URI("/api/recipe-ingredients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipe-ingredients : Updates an existing recipeIngredient.
     *
     * @param recipeIngredient the recipeIngredient to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeIngredient,
     * or with status 400 (Bad Request) if the recipeIngredient is not valid,
     * or with status 500 (Internal Server Error) if the recipeIngredient couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipe-ingredients")
    public ResponseEntity<RecipeIngredient> updateRecipeIngredient(@Valid @RequestBody RecipeIngredient recipeIngredient) throws URISyntaxException {
        log.debug("REST request to update RecipeIngredient : {}", recipeIngredient);
        if (recipeIngredient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecipeIngredient result = recipeIngredientService.save(recipeIngredient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipeIngredient.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipe-ingredients : get all the recipeIngredients.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of recipeIngredients in body
     */
    @GetMapping("/recipe-ingredients")
    public ResponseEntity<List<RecipeIngredient>> getAllRecipeIngredients(RecipeIngredientCriteria criteria) {
        log.debug("REST request to get RecipeIngredients by criteria: {}", criteria);
        List<RecipeIngredient> entityList = recipeIngredientQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /recipe-ingredients/count : count all the recipeIngredients.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/recipe-ingredients/count")
    public ResponseEntity<Long> countRecipeIngredients(RecipeIngredientCriteria criteria) {
        log.debug("REST request to count RecipeIngredients by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipeIngredientQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /recipe-ingredients/:id : get the "id" recipeIngredient.
     *
     * @param id the id of the recipeIngredient to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeIngredient, or with status 404 (Not Found)
     */
    @GetMapping("/recipe-ingredients/{id}")
    public ResponseEntity<RecipeIngredient> getRecipeIngredient(@PathVariable Long id) {
        log.debug("REST request to get RecipeIngredient : {}", id);
        Optional<RecipeIngredient> recipeIngredient = recipeIngredientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeIngredient);
    }

    /**
     * DELETE  /recipe-ingredients/:id : delete the "id" recipeIngredient.
     *
     * @param id the id of the recipeIngredient to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipe-ingredients/{id}")
    public ResponseEntity<Void> deleteRecipeIngredient(@PathVariable Long id) {
        log.debug("REST request to delete RecipeIngredient : {}", id);
        recipeIngredientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipe-ingredients?query=:query : search for the recipeIngredient corresponding
     * to the query.
     *
     * @param query the query of the recipeIngredient search
     * @return the result of the search
     */
    @GetMapping("/_search/recipe-ingredients")
    public List<RecipeIngredient> searchRecipeIngredients(@RequestParam String query) {
        log.debug("REST request to search RecipeIngredients for query {}", query);
        return recipeIngredientService.search(query);
    }

}

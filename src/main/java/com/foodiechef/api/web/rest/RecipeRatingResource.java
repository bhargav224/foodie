package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.RecipeRating;
import com.foodiechef.api.service.RecipeRatingService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.RecipeRatingCriteria;
import com.foodiechef.api.service.RecipeRatingQueryService;
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
 * REST controller for managing RecipeRating.
 */
@RestController
@RequestMapping("/api")
public class RecipeRatingResource {

    private final Logger log = LoggerFactory.getLogger(RecipeRatingResource.class);

    private static final String ENTITY_NAME = "recipeRating";

    private final RecipeRatingService recipeRatingService;

    private final RecipeRatingQueryService recipeRatingQueryService;

    public RecipeRatingResource(RecipeRatingService recipeRatingService, RecipeRatingQueryService recipeRatingQueryService) {
        this.recipeRatingService = recipeRatingService;
        this.recipeRatingQueryService = recipeRatingQueryService;
    }

    /**
     * POST  /recipe-ratings : Create a new recipeRating.
     *
     * @param recipeRating the recipeRating to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeRating, or with status 400 (Bad Request) if the recipeRating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipe-ratings")
    public ResponseEntity<RecipeRating> createRecipeRating(@Valid @RequestBody RecipeRating recipeRating) throws URISyntaxException {
        log.debug("REST request to save RecipeRating : {}", recipeRating);
        if (recipeRating.getId() != null) {
            throw new BadRequestAlertException("A new recipeRating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeRating result = recipeRatingService.save(recipeRating);
        return ResponseEntity.created(new URI("/api/recipe-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipe-ratings : Updates an existing recipeRating.
     *
     * @param recipeRating the recipeRating to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeRating,
     * or with status 400 (Bad Request) if the recipeRating is not valid,
     * or with status 500 (Internal Server Error) if the recipeRating couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipe-ratings")
    public ResponseEntity<RecipeRating> updateRecipeRating(@Valid @RequestBody RecipeRating recipeRating) throws URISyntaxException {
        log.debug("REST request to update RecipeRating : {}", recipeRating);
        if (recipeRating.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecipeRating result = recipeRatingService.save(recipeRating);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipeRating.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipe-ratings : get all the recipeRatings.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of recipeRatings in body
     */
    @GetMapping("/recipe-ratings")
    public ResponseEntity<List<RecipeRating>> getAllRecipeRatings(RecipeRatingCriteria criteria) {
        log.debug("REST request to get RecipeRatings by criteria: {}", criteria);
        List<RecipeRating> entityList = recipeRatingQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /recipe-ratings/count : count all the recipeRatings.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/recipe-ratings/count")
    public ResponseEntity<Long> countRecipeRatings(RecipeRatingCriteria criteria) {
        log.debug("REST request to count RecipeRatings by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipeRatingQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /recipe-ratings/:id : get the "id" recipeRating.
     *
     * @param id the id of the recipeRating to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeRating, or with status 404 (Not Found)
     */
    @GetMapping("/recipe-ratings/{id}")
    public ResponseEntity<RecipeRating> getRecipeRating(@PathVariable Long id) {
        log.debug("REST request to get RecipeRating : {}", id);
        Optional<RecipeRating> recipeRating = recipeRatingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeRating);
    }

    /**
     * DELETE  /recipe-ratings/:id : delete the "id" recipeRating.
     *
     * @param id the id of the recipeRating to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipe-ratings/{id}")
    public ResponseEntity<Void> deleteRecipeRating(@PathVariable Long id) {
        log.debug("REST request to delete RecipeRating : {}", id);
        recipeRatingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipe-ratings?query=:query : search for the recipeRating corresponding
     * to the query.
     *
     * @param query the query of the recipeRating search
     * @return the result of the search
     */
    @GetMapping("/_search/recipe-ratings")
    public List<RecipeRating> searchRecipeRatings(@RequestParam String query) {
        log.debug("REST request to search RecipeRatings for query {}", query);
        return recipeRatingService.search(query);
    }

}

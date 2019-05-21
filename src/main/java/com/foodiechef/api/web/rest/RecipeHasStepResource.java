package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.RecipeHasStep;
import com.foodiechef.api.service.RecipeHasStepService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.RecipeHasStepCriteria;
import com.foodiechef.api.service.RecipeHasStepQueryService;
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
 * REST controller for managing RecipeHasStep.
 */
@RestController
@RequestMapping("/api")
public class RecipeHasStepResource {

    private final Logger log = LoggerFactory.getLogger(RecipeHasStepResource.class);

    private static final String ENTITY_NAME = "recipeHasStep";

    private final RecipeHasStepService recipeHasStepService;

    private final RecipeHasStepQueryService recipeHasStepQueryService;

    public RecipeHasStepResource(RecipeHasStepService recipeHasStepService, RecipeHasStepQueryService recipeHasStepQueryService) {
        this.recipeHasStepService = recipeHasStepService;
        this.recipeHasStepQueryService = recipeHasStepQueryService;
    }

    /**
     * POST  /recipe-has-steps : Create a new recipeHasStep.
     *
     * @param recipeHasStep the recipeHasStep to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeHasStep, or with status 400 (Bad Request) if the recipeHasStep has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipe-has-steps")
    public ResponseEntity<RecipeHasStep> createRecipeHasStep(@RequestBody RecipeHasStep recipeHasStep) throws URISyntaxException {
        log.debug("REST request to save RecipeHasStep : {}", recipeHasStep);
        if (recipeHasStep.getId() != null) {
            throw new BadRequestAlertException("A new recipeHasStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeHasStep result = recipeHasStepService.save(recipeHasStep);
        return ResponseEntity.created(new URI("/api/recipe-has-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipe-has-steps : Updates an existing recipeHasStep.
     *
     * @param recipeHasStep the recipeHasStep to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeHasStep,
     * or with status 400 (Bad Request) if the recipeHasStep is not valid,
     * or with status 500 (Internal Server Error) if the recipeHasStep couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipe-has-steps")
    public ResponseEntity<RecipeHasStep> updateRecipeHasStep(@RequestBody RecipeHasStep recipeHasStep) throws URISyntaxException {
        log.debug("REST request to update RecipeHasStep : {}", recipeHasStep);
        if (recipeHasStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecipeHasStep result = recipeHasStepService.save(recipeHasStep);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipeHasStep.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipe-has-steps : get all the recipeHasSteps.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of recipeHasSteps in body
     */
    @GetMapping("/recipe-has-steps")
    public ResponseEntity<List<RecipeHasStep>> getAllRecipeHasSteps(RecipeHasStepCriteria criteria) {
        log.debug("REST request to get RecipeHasSteps by criteria: {}", criteria);
        List<RecipeHasStep> entityList = recipeHasStepQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /recipe-has-steps/count : count all the recipeHasSteps.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/recipe-has-steps/count")
    public ResponseEntity<Long> countRecipeHasSteps(RecipeHasStepCriteria criteria) {
        log.debug("REST request to count RecipeHasSteps by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipeHasStepQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /recipe-has-steps/:id : get the "id" recipeHasStep.
     *
     * @param id the id of the recipeHasStep to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeHasStep, or with status 404 (Not Found)
     */
    @GetMapping("/recipe-has-steps/{id}")
    public ResponseEntity<RecipeHasStep> getRecipeHasStep(@PathVariable Long id) {
        log.debug("REST request to get RecipeHasStep : {}", id);
        Optional<RecipeHasStep> recipeHasStep = recipeHasStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeHasStep);
    }

    /**
     * DELETE  /recipe-has-steps/:id : delete the "id" recipeHasStep.
     *
     * @param id the id of the recipeHasStep to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipe-has-steps/{id}")
    public ResponseEntity<Void> deleteRecipeHasStep(@PathVariable Long id) {
        log.debug("REST request to delete RecipeHasStep : {}", id);
        recipeHasStepService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipe-has-steps?query=:query : search for the recipeHasStep corresponding
     * to the query.
     *
     * @param query the query of the recipeHasStep search
     * @return the result of the search
     */
    @GetMapping("/_search/recipe-has-steps")
    public List<RecipeHasStep> searchRecipeHasSteps(@RequestParam String query) {
        log.debug("REST request to search RecipeHasSteps for query {}", query);
        return recipeHasStepService.search(query);
    }

}

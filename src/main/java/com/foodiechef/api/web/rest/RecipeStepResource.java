package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.RecipeStep;
import com.foodiechef.api.service.RecipeStepService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.RecipeStepCriteria;
import com.foodiechef.api.service.RecipeStepQueryService;
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
 * REST controller for managing RecipeStep.
 */
@RestController
@RequestMapping("/api")
public class RecipeStepResource {

    private final Logger log = LoggerFactory.getLogger(RecipeStepResource.class);

    private static final String ENTITY_NAME = "recipeStep";

    private final RecipeStepService recipeStepService;

    private final RecipeStepQueryService recipeStepQueryService;

    public RecipeStepResource(RecipeStepService recipeStepService, RecipeStepQueryService recipeStepQueryService) {
        this.recipeStepService = recipeStepService;
        this.recipeStepQueryService = recipeStepQueryService;
    }

    /**
     * POST  /recipe-steps : Create a new recipeStep.
     *
     * @param recipeStep the recipeStep to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeStep, or with status 400 (Bad Request) if the recipeStep has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipe-steps")
    public ResponseEntity<RecipeStep> createRecipeStep(@Valid @RequestBody RecipeStep recipeStep) throws URISyntaxException {
        log.debug("REST request to save RecipeStep : {}", recipeStep);
        if (recipeStep.getId() != null) {
            throw new BadRequestAlertException("A new recipeStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeStep result = recipeStepService.save(recipeStep);
        return ResponseEntity.created(new URI("/api/recipe-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipe-steps : Updates an existing recipeStep.
     *
     * @param recipeStep the recipeStep to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeStep,
     * or with status 400 (Bad Request) if the recipeStep is not valid,
     * or with status 500 (Internal Server Error) if the recipeStep couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipe-steps")
    public ResponseEntity<RecipeStep> updateRecipeStep(@Valid @RequestBody RecipeStep recipeStep) throws URISyntaxException {
        log.debug("REST request to update RecipeStep : {}", recipeStep);
        if (recipeStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecipeStep result = recipeStepService.save(recipeStep);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipeStep.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipe-steps : get all the recipeSteps.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of recipeSteps in body
     */
    @GetMapping("/recipe-steps")
    public ResponseEntity<List<RecipeStep>> getAllRecipeSteps(RecipeStepCriteria criteria) {
        log.debug("REST request to get RecipeSteps by criteria: {}", criteria);
        List<RecipeStep> entityList = recipeStepQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /recipe-steps/count : count all the recipeSteps.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/recipe-steps/count")
    public ResponseEntity<Long> countRecipeSteps(RecipeStepCriteria criteria) {
        log.debug("REST request to count RecipeSteps by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipeStepQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /recipe-steps/:id : get the "id" recipeStep.
     *
     * @param id the id of the recipeStep to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeStep, or with status 404 (Not Found)
     */
    @GetMapping("/recipe-steps/{id}")
    public ResponseEntity<RecipeStep> getRecipeStep(@PathVariable Long id) {
        log.debug("REST request to get RecipeStep : {}", id);
        Optional<RecipeStep> recipeStep = recipeStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeStep);
    }

    /**
     * DELETE  /recipe-steps/:id : delete the "id" recipeStep.
     *
     * @param id the id of the recipeStep to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipe-steps/{id}")
    public ResponseEntity<Void> deleteRecipeStep(@PathVariable Long id) {
        log.debug("REST request to delete RecipeStep : {}", id);
        recipeStepService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipe-steps?query=:query : search for the recipeStep corresponding
     * to the query.
     *
     * @param query the query of the recipeStep search
     * @return the result of the search
     */
    @GetMapping("/_search/recipe-steps")
    public List<RecipeStep> searchRecipeSteps(@RequestParam String query) {
        log.debug("REST request to search RecipeSteps for query {}", query);
        return recipeStepService.search(query);
    }

}

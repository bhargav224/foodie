package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.IngredientNutritionInfo;
import com.foodiechef.api.service.IngredientNutritionInfoService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.IngredientNutritionInfoCriteria;
import com.foodiechef.api.service.IngredientNutritionInfoQueryService;
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
 * REST controller for managing IngredientNutritionInfo.
 */
@RestController
@RequestMapping("/api")
public class IngredientNutritionInfoResource {

    private final Logger log = LoggerFactory.getLogger(IngredientNutritionInfoResource.class);

    private static final String ENTITY_NAME = "ingredientNutritionInfo";

    private final IngredientNutritionInfoService ingredientNutritionInfoService;

    private final IngredientNutritionInfoQueryService ingredientNutritionInfoQueryService;

    public IngredientNutritionInfoResource(IngredientNutritionInfoService ingredientNutritionInfoService, IngredientNutritionInfoQueryService ingredientNutritionInfoQueryService) {
        this.ingredientNutritionInfoService = ingredientNutritionInfoService;
        this.ingredientNutritionInfoQueryService = ingredientNutritionInfoQueryService;
    }

    /**
     * POST  /ingredient-nutrition-infos : Create a new ingredientNutritionInfo.
     *
     * @param ingredientNutritionInfo the ingredientNutritionInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ingredientNutritionInfo, or with status 400 (Bad Request) if the ingredientNutritionInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ingredient-nutrition-infos")
    public ResponseEntity<IngredientNutritionInfo> createIngredientNutritionInfo(@Valid @RequestBody IngredientNutritionInfo ingredientNutritionInfo) throws URISyntaxException {
        log.debug("REST request to save IngredientNutritionInfo : {}", ingredientNutritionInfo);
        if (ingredientNutritionInfo.getId() != null) {
            throw new BadRequestAlertException("A new ingredientNutritionInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IngredientNutritionInfo result = ingredientNutritionInfoService.save(ingredientNutritionInfo);
        return ResponseEntity.created(new URI("/api/ingredient-nutrition-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ingredient-nutrition-infos : Updates an existing ingredientNutritionInfo.
     *
     * @param ingredientNutritionInfo the ingredientNutritionInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ingredientNutritionInfo,
     * or with status 400 (Bad Request) if the ingredientNutritionInfo is not valid,
     * or with status 500 (Internal Server Error) if the ingredientNutritionInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ingredient-nutrition-infos")
    public ResponseEntity<IngredientNutritionInfo> updateIngredientNutritionInfo(@Valid @RequestBody IngredientNutritionInfo ingredientNutritionInfo) throws URISyntaxException {
        log.debug("REST request to update IngredientNutritionInfo : {}", ingredientNutritionInfo);
        if (ingredientNutritionInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IngredientNutritionInfo result = ingredientNutritionInfoService.save(ingredientNutritionInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ingredientNutritionInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ingredient-nutrition-infos : get all the ingredientNutritionInfos.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of ingredientNutritionInfos in body
     */
    @GetMapping("/ingredient-nutrition-infos")
    public ResponseEntity<List<IngredientNutritionInfo>> getAllIngredientNutritionInfos(IngredientNutritionInfoCriteria criteria) {
        log.debug("REST request to get IngredientNutritionInfos by criteria: {}", criteria);
        List<IngredientNutritionInfo> entityList = ingredientNutritionInfoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /ingredient-nutrition-infos/count : count all the ingredientNutritionInfos.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/ingredient-nutrition-infos/count")
    public ResponseEntity<Long> countIngredientNutritionInfos(IngredientNutritionInfoCriteria criteria) {
        log.debug("REST request to count IngredientNutritionInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(ingredientNutritionInfoQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /ingredient-nutrition-infos/:id : get the "id" ingredientNutritionInfo.
     *
     * @param id the id of the ingredientNutritionInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ingredientNutritionInfo, or with status 404 (Not Found)
     */
    @GetMapping("/ingredient-nutrition-infos/{id}")
    public ResponseEntity<IngredientNutritionInfo> getIngredientNutritionInfo(@PathVariable Long id) {
        log.debug("REST request to get IngredientNutritionInfo : {}", id);
        Optional<IngredientNutritionInfo> ingredientNutritionInfo = ingredientNutritionInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingredientNutritionInfo);
    }

    /**
     * DELETE  /ingredient-nutrition-infos/:id : delete the "id" ingredientNutritionInfo.
     *
     * @param id the id of the ingredientNutritionInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ingredient-nutrition-infos/{id}")
    public ResponseEntity<Void> deleteIngredientNutritionInfo(@PathVariable Long id) {
        log.debug("REST request to delete IngredientNutritionInfo : {}", id);
        ingredientNutritionInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ingredient-nutrition-infos?query=:query : search for the ingredientNutritionInfo corresponding
     * to the query.
     *
     * @param query the query of the ingredientNutritionInfo search
     * @return the result of the search
     */
    @GetMapping("/_search/ingredient-nutrition-infos")
    public List<IngredientNutritionInfo> searchIngredientNutritionInfos(@RequestParam String query) {
        log.debug("REST request to search IngredientNutritionInfos for query {}", query);
        return ingredientNutritionInfoService.search(query);
    }

}

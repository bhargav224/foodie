package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.RecipeImage;
import com.foodiechef.api.service.RecipeImageService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.RecipeImageCriteria;
import com.foodiechef.api.service.RecipeImageQueryService;
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
 * REST controller for managing RecipeImage.
 */
@RestController
@RequestMapping("/api")
public class RecipeImageResource {

    private final Logger log = LoggerFactory.getLogger(RecipeImageResource.class);

    private static final String ENTITY_NAME = "recipeImage";

    private final RecipeImageService recipeImageService;

    private final RecipeImageQueryService recipeImageQueryService;

    public RecipeImageResource(RecipeImageService recipeImageService, RecipeImageQueryService recipeImageQueryService) {
        this.recipeImageService = recipeImageService;
        this.recipeImageQueryService = recipeImageQueryService;
    }

    /**
     * POST  /recipe-images : Create a new recipeImage.
     *
     * @param recipeImage the recipeImage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeImage, or with status 400 (Bad Request) if the recipeImage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipe-images")
    public ResponseEntity<RecipeImage> createRecipeImage(@Valid @RequestBody RecipeImage recipeImage) throws URISyntaxException {
        log.debug("REST request to save RecipeImage : {}", recipeImage);
        if (recipeImage.getId() != null) {
            throw new BadRequestAlertException("A new recipeImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeImage result = recipeImageService.save(recipeImage);
        return ResponseEntity.created(new URI("/api/recipe-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipe-images : Updates an existing recipeImage.
     *
     * @param recipeImage the recipeImage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeImage,
     * or with status 400 (Bad Request) if the recipeImage is not valid,
     * or with status 500 (Internal Server Error) if the recipeImage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipe-images")
    public ResponseEntity<RecipeImage> updateRecipeImage(@Valid @RequestBody RecipeImage recipeImage) throws URISyntaxException {
        log.debug("REST request to update RecipeImage : {}", recipeImage);
        if (recipeImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecipeImage result = recipeImageService.save(recipeImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipeImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipe-images : get all the recipeImages.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of recipeImages in body
     */
    @GetMapping("/recipe-images")
    public ResponseEntity<List<RecipeImage>> getAllRecipeImages(RecipeImageCriteria criteria) {
        log.debug("REST request to get RecipeImages by criteria: {}", criteria);
        List<RecipeImage> entityList = recipeImageQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /recipe-images/count : count all the recipeImages.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/recipe-images/count")
    public ResponseEntity<Long> countRecipeImages(RecipeImageCriteria criteria) {
        log.debug("REST request to count RecipeImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipeImageQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /recipe-images/:id : get the "id" recipeImage.
     *
     * @param id the id of the recipeImage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeImage, or with status 404 (Not Found)
     */
    @GetMapping("/recipe-images/{id}")
    public ResponseEntity<RecipeImage> getRecipeImage(@PathVariable Long id) {
        log.debug("REST request to get RecipeImage : {}", id);
        Optional<RecipeImage> recipeImage = recipeImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeImage);
    }

    /**
     * DELETE  /recipe-images/:id : delete the "id" recipeImage.
     *
     * @param id the id of the recipeImage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipe-images/{id}")
    public ResponseEntity<Void> deleteRecipeImage(@PathVariable Long id) {
        log.debug("REST request to delete RecipeImage : {}", id);
        recipeImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipe-images?query=:query : search for the recipeImage corresponding
     * to the query.
     *
     * @param query the query of the recipeImage search
     * @return the result of the search
     */
    @GetMapping("/_search/recipe-images")
    public List<RecipeImage> searchRecipeImages(@RequestParam String query) {
        log.debug("REST request to search RecipeImages for query {}", query);
        return recipeImageService.search(query);
    }

}

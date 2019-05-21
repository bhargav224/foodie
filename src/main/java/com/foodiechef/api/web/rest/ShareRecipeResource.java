package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.ShareRecipe;
import com.foodiechef.api.service.ShareRecipeService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.ShareRecipeCriteria;
import com.foodiechef.api.service.ShareRecipeQueryService;
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
 * REST controller for managing ShareRecipe.
 */
@RestController
@RequestMapping("/api")
public class ShareRecipeResource {

    private final Logger log = LoggerFactory.getLogger(ShareRecipeResource.class);

    private static final String ENTITY_NAME = "shareRecipe";

    private final ShareRecipeService shareRecipeService;

    private final ShareRecipeQueryService shareRecipeQueryService;

    public ShareRecipeResource(ShareRecipeService shareRecipeService, ShareRecipeQueryService shareRecipeQueryService) {
        this.shareRecipeService = shareRecipeService;
        this.shareRecipeQueryService = shareRecipeQueryService;
    }

    /**
     * POST  /share-recipes : Create a new shareRecipe.
     *
     * @param shareRecipe the shareRecipe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shareRecipe, or with status 400 (Bad Request) if the shareRecipe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/share-recipes")
    public ResponseEntity<ShareRecipe> createShareRecipe(@Valid @RequestBody ShareRecipe shareRecipe) throws URISyntaxException {
        log.debug("REST request to save ShareRecipe : {}", shareRecipe);
        if (shareRecipe.getId() != null) {
            throw new BadRequestAlertException("A new shareRecipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShareRecipe result = shareRecipeService.save(shareRecipe);
        return ResponseEntity.created(new URI("/api/share-recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /share-recipes : Updates an existing shareRecipe.
     *
     * @param shareRecipe the shareRecipe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shareRecipe,
     * or with status 400 (Bad Request) if the shareRecipe is not valid,
     * or with status 500 (Internal Server Error) if the shareRecipe couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/share-recipes")
    public ResponseEntity<ShareRecipe> updateShareRecipe(@Valid @RequestBody ShareRecipe shareRecipe) throws URISyntaxException {
        log.debug("REST request to update ShareRecipe : {}", shareRecipe);
        if (shareRecipe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShareRecipe result = shareRecipeService.save(shareRecipe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shareRecipe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /share-recipes : get all the shareRecipes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of shareRecipes in body
     */
    @GetMapping("/share-recipes")
    public ResponseEntity<List<ShareRecipe>> getAllShareRecipes(ShareRecipeCriteria criteria) {
        log.debug("REST request to get ShareRecipes by criteria: {}", criteria);
        List<ShareRecipe> entityList = shareRecipeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /share-recipes/count : count all the shareRecipes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/share-recipes/count")
    public ResponseEntity<Long> countShareRecipes(ShareRecipeCriteria criteria) {
        log.debug("REST request to count ShareRecipes by criteria: {}", criteria);
        return ResponseEntity.ok().body(shareRecipeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /share-recipes/:id : get the "id" shareRecipe.
     *
     * @param id the id of the shareRecipe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shareRecipe, or with status 404 (Not Found)
     */
    @GetMapping("/share-recipes/{id}")
    public ResponseEntity<ShareRecipe> getShareRecipe(@PathVariable Long id) {
        log.debug("REST request to get ShareRecipe : {}", id);
        Optional<ShareRecipe> shareRecipe = shareRecipeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shareRecipe);
    }

    /**
     * DELETE  /share-recipes/:id : delete the "id" shareRecipe.
     *
     * @param id the id of the shareRecipe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/share-recipes/{id}")
    public ResponseEntity<Void> deleteShareRecipe(@PathVariable Long id) {
        log.debug("REST request to delete ShareRecipe : {}", id);
        shareRecipeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/share-recipes?query=:query : search for the shareRecipe corresponding
     * to the query.
     *
     * @param query the query of the shareRecipe search
     * @return the result of the search
     */
    @GetMapping("/_search/share-recipes")
    public List<ShareRecipe> searchShareRecipes(@RequestParam String query) {
        log.debug("REST request to search ShareRecipes for query {}", query);
        return shareRecipeService.search(query);
    }

}

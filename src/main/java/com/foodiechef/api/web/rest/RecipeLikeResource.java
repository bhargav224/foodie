package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.RecipeLike;
import com.foodiechef.api.service.RecipeLikeService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.RecipeLikeCriteria;
import com.foodiechef.api.service.RecipeLikeQueryService;
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
 * REST controller for managing RecipeLike.
 */
@RestController
@RequestMapping("/api")
public class RecipeLikeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeLikeResource.class);

    private static final String ENTITY_NAME = "recipeLike";

    private final RecipeLikeService recipeLikeService;

    private final RecipeLikeQueryService recipeLikeQueryService;

    public RecipeLikeResource(RecipeLikeService recipeLikeService, RecipeLikeQueryService recipeLikeQueryService) {
        this.recipeLikeService = recipeLikeService;
        this.recipeLikeQueryService = recipeLikeQueryService;
    }

    /**
     * POST  /recipe-likes : Create a new recipeLike.
     *
     * @param recipeLike the recipeLike to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeLike, or with status 400 (Bad Request) if the recipeLike has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipe-likes")
    public ResponseEntity<RecipeLike> createRecipeLike(@Valid @RequestBody RecipeLike recipeLike) throws URISyntaxException {
        log.debug("REST request to save RecipeLike : {}", recipeLike);
        if (recipeLike.getId() != null) {
            throw new BadRequestAlertException("A new recipeLike cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeLike result = recipeLikeService.save(recipeLike);
        return ResponseEntity.created(new URI("/api/recipe-likes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipe-likes : Updates an existing recipeLike.
     *
     * @param recipeLike the recipeLike to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeLike,
     * or with status 400 (Bad Request) if the recipeLike is not valid,
     * or with status 500 (Internal Server Error) if the recipeLike couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipe-likes")
    public ResponseEntity<RecipeLike> updateRecipeLike(@Valid @RequestBody RecipeLike recipeLike) throws URISyntaxException {
        log.debug("REST request to update RecipeLike : {}", recipeLike);
        if (recipeLike.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecipeLike result = recipeLikeService.save(recipeLike);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipeLike.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipe-likes : get all the recipeLikes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of recipeLikes in body
     */
    @GetMapping("/recipe-likes")
    public ResponseEntity<List<RecipeLike>> getAllRecipeLikes(RecipeLikeCriteria criteria) {
        log.debug("REST request to get RecipeLikes by criteria: {}", criteria);
        List<RecipeLike> entityList = recipeLikeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /recipe-likes/count : count all the recipeLikes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/recipe-likes/count")
    public ResponseEntity<Long> countRecipeLikes(RecipeLikeCriteria criteria) {
        log.debug("REST request to count RecipeLikes by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipeLikeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /recipe-likes/:id : get the "id" recipeLike.
     *
     * @param id the id of the recipeLike to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeLike, or with status 404 (Not Found)
     */
    @GetMapping("/recipe-likes/{id}")
    public ResponseEntity<RecipeLike> getRecipeLike(@PathVariable Long id) {
        log.debug("REST request to get RecipeLike : {}", id);
        Optional<RecipeLike> recipeLike = recipeLikeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeLike);
    }

    /**
     * DELETE  /recipe-likes/:id : delete the "id" recipeLike.
     *
     * @param id the id of the recipeLike to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipe-likes/{id}")
    public ResponseEntity<Void> deleteRecipeLike(@PathVariable Long id) {
        log.debug("REST request to delete RecipeLike : {}", id);
        recipeLikeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipe-likes?query=:query : search for the recipeLike corresponding
     * to the query.
     *
     * @param query the query of the recipeLike search
     * @return the result of the search
     */
    @GetMapping("/_search/recipe-likes")
    public List<RecipeLike> searchRecipeLikes(@RequestParam String query) {
        log.debug("REST request to search RecipeLikes for query {}", query);
        return recipeLikeService.search(query);
    }

}

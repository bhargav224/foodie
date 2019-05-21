package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.RecipeComment;
import com.foodiechef.api.service.RecipeCommentService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.RecipeCommentCriteria;
import com.foodiechef.api.service.RecipeCommentQueryService;
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
 * REST controller for managing RecipeComment.
 */
@RestController
@RequestMapping("/api")
public class RecipeCommentResource {

    private final Logger log = LoggerFactory.getLogger(RecipeCommentResource.class);

    private static final String ENTITY_NAME = "recipeComment";

    private final RecipeCommentService recipeCommentService;

    private final RecipeCommentQueryService recipeCommentQueryService;

    public RecipeCommentResource(RecipeCommentService recipeCommentService, RecipeCommentQueryService recipeCommentQueryService) {
        this.recipeCommentService = recipeCommentService;
        this.recipeCommentQueryService = recipeCommentQueryService;
    }

    /**
     * POST  /recipe-comments : Create a new recipeComment.
     *
     * @param recipeComment the recipeComment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeComment, or with status 400 (Bad Request) if the recipeComment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipe-comments")
    public ResponseEntity<RecipeComment> createRecipeComment(@Valid @RequestBody RecipeComment recipeComment) throws URISyntaxException {
        log.debug("REST request to save RecipeComment : {}", recipeComment);
        if (recipeComment.getId() != null) {
            throw new BadRequestAlertException("A new recipeComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeComment result = recipeCommentService.save(recipeComment);
        return ResponseEntity.created(new URI("/api/recipe-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipe-comments : Updates an existing recipeComment.
     *
     * @param recipeComment the recipeComment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeComment,
     * or with status 400 (Bad Request) if the recipeComment is not valid,
     * or with status 500 (Internal Server Error) if the recipeComment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipe-comments")
    public ResponseEntity<RecipeComment> updateRecipeComment(@Valid @RequestBody RecipeComment recipeComment) throws URISyntaxException {
        log.debug("REST request to update RecipeComment : {}", recipeComment);
        if (recipeComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecipeComment result = recipeCommentService.save(recipeComment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipeComment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipe-comments : get all the recipeComments.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of recipeComments in body
     */
    @GetMapping("/recipe-comments")
    public ResponseEntity<List<RecipeComment>> getAllRecipeComments(RecipeCommentCriteria criteria) {
        log.debug("REST request to get RecipeComments by criteria: {}", criteria);
        List<RecipeComment> entityList = recipeCommentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /recipe-comments/count : count all the recipeComments.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/recipe-comments/count")
    public ResponseEntity<Long> countRecipeComments(RecipeCommentCriteria criteria) {
        log.debug("REST request to count RecipeComments by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipeCommentQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /recipe-comments/:id : get the "id" recipeComment.
     *
     * @param id the id of the recipeComment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeComment, or with status 404 (Not Found)
     */
    @GetMapping("/recipe-comments/{id}")
    public ResponseEntity<RecipeComment> getRecipeComment(@PathVariable Long id) {
        log.debug("REST request to get RecipeComment : {}", id);
        Optional<RecipeComment> recipeComment = recipeCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeComment);
    }

    /**
     * DELETE  /recipe-comments/:id : delete the "id" recipeComment.
     *
     * @param id the id of the recipeComment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipe-comments/{id}")
    public ResponseEntity<Void> deleteRecipeComment(@PathVariable Long id) {
        log.debug("REST request to delete RecipeComment : {}", id);
        recipeCommentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipe-comments?query=:query : search for the recipeComment corresponding
     * to the query.
     *
     * @param query the query of the recipeComment search
     * @return the result of the search
     */
    @GetMapping("/_search/recipe-comments")
    public List<RecipeComment> searchRecipeComments(@RequestParam String query) {
        log.debug("REST request to search RecipeComments for query {}", query);
        return recipeCommentService.search(query);
    }

}

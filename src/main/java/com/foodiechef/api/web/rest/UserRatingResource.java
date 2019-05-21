package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.UserRating;
import com.foodiechef.api.service.UserRatingService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.UserRatingCriteria;
import com.foodiechef.api.service.UserRatingQueryService;
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
 * REST controller for managing UserRating.
 */
@RestController
@RequestMapping("/api")
public class UserRatingResource {

    private final Logger log = LoggerFactory.getLogger(UserRatingResource.class);

    private static final String ENTITY_NAME = "userRating";

    private final UserRatingService userRatingService;

    private final UserRatingQueryService userRatingQueryService;

    public UserRatingResource(UserRatingService userRatingService, UserRatingQueryService userRatingQueryService) {
        this.userRatingService = userRatingService;
        this.userRatingQueryService = userRatingQueryService;
    }

    /**
     * POST  /user-ratings : Create a new userRating.
     *
     * @param userRating the userRating to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userRating, or with status 400 (Bad Request) if the userRating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-ratings")
    public ResponseEntity<UserRating> createUserRating(@Valid @RequestBody UserRating userRating) throws URISyntaxException {
        log.debug("REST request to save UserRating : {}", userRating);
        if (userRating.getId() != null) {
            throw new BadRequestAlertException("A new userRating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRating result = userRatingService.save(userRating);
        return ResponseEntity.created(new URI("/api/user-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-ratings : Updates an existing userRating.
     *
     * @param userRating the userRating to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userRating,
     * or with status 400 (Bad Request) if the userRating is not valid,
     * or with status 500 (Internal Server Error) if the userRating couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-ratings")
    public ResponseEntity<UserRating> updateUserRating(@Valid @RequestBody UserRating userRating) throws URISyntaxException {
        log.debug("REST request to update UserRating : {}", userRating);
        if (userRating.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserRating result = userRatingService.save(userRating);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userRating.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-ratings : get all the userRatings.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of userRatings in body
     */
    @GetMapping("/user-ratings")
    public ResponseEntity<List<UserRating>> getAllUserRatings(UserRatingCriteria criteria) {
        log.debug("REST request to get UserRatings by criteria: {}", criteria);
        List<UserRating> entityList = userRatingQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /user-ratings/count : count all the userRatings.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/user-ratings/count")
    public ResponseEntity<Long> countUserRatings(UserRatingCriteria criteria) {
        log.debug("REST request to count UserRatings by criteria: {}", criteria);
        return ResponseEntity.ok().body(userRatingQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /user-ratings/:id : get the "id" userRating.
     *
     * @param id the id of the userRating to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userRating, or with status 404 (Not Found)
     */
    @GetMapping("/user-ratings/{id}")
    public ResponseEntity<UserRating> getUserRating(@PathVariable Long id) {
        log.debug("REST request to get UserRating : {}", id);
        Optional<UserRating> userRating = userRatingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userRating);
    }

    /**
     * DELETE  /user-ratings/:id : delete the "id" userRating.
     *
     * @param id the id of the userRating to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-ratings/{id}")
    public ResponseEntity<Void> deleteUserRating(@PathVariable Long id) {
        log.debug("REST request to delete UserRating : {}", id);
        userRatingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-ratings?query=:query : search for the userRating corresponding
     * to the query.
     *
     * @param query the query of the userRating search
     * @return the result of the search
     */
    @GetMapping("/_search/user-ratings")
    public List<UserRating> searchUserRatings(@RequestParam String query) {
        log.debug("REST request to search UserRatings for query {}", query);
        return userRatingService.search(query);
    }

}

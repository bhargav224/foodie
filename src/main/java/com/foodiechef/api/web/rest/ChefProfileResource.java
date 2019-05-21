package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.ChefProfile;
import com.foodiechef.api.service.ChefProfileService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.ChefProfileCriteria;
import com.foodiechef.api.service.ChefProfileQueryService;
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
 * REST controller for managing ChefProfile.
 */
@RestController
@RequestMapping("/api")
public class ChefProfileResource {

    private final Logger log = LoggerFactory.getLogger(ChefProfileResource.class);

    private static final String ENTITY_NAME = "chefProfile";

    private final ChefProfileService chefProfileService;

    private final ChefProfileQueryService chefProfileQueryService;

    public ChefProfileResource(ChefProfileService chefProfileService, ChefProfileQueryService chefProfileQueryService) {
        this.chefProfileService = chefProfileService;
        this.chefProfileQueryService = chefProfileQueryService;
    }

    /**
     * POST  /chef-profiles : Create a new chefProfile.
     *
     * @param chefProfile the chefProfile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chefProfile, or with status 400 (Bad Request) if the chefProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chef-profiles")
    public ResponseEntity<ChefProfile> createChefProfile(@Valid @RequestBody ChefProfile chefProfile) throws URISyntaxException {
        log.debug("REST request to save ChefProfile : {}", chefProfile);
        if (chefProfile.getId() != null) {
            throw new BadRequestAlertException("A new chefProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChefProfile result = chefProfileService.save(chefProfile);
        return ResponseEntity.created(new URI("/api/chef-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chef-profiles : Updates an existing chefProfile.
     *
     * @param chefProfile the chefProfile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chefProfile,
     * or with status 400 (Bad Request) if the chefProfile is not valid,
     * or with status 500 (Internal Server Error) if the chefProfile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chef-profiles")
    public ResponseEntity<ChefProfile> updateChefProfile(@Valid @RequestBody ChefProfile chefProfile) throws URISyntaxException {
        log.debug("REST request to update ChefProfile : {}", chefProfile);
        if (chefProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChefProfile result = chefProfileService.save(chefProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chefProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chef-profiles : get all the chefProfiles.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of chefProfiles in body
     */
    @GetMapping("/chef-profiles")
    public ResponseEntity<List<ChefProfile>> getAllChefProfiles(ChefProfileCriteria criteria) {
        log.debug("REST request to get ChefProfiles by criteria: {}", criteria);
        List<ChefProfile> entityList = chefProfileQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /chef-profiles/count : count all the chefProfiles.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/chef-profiles/count")
    public ResponseEntity<Long> countChefProfiles(ChefProfileCriteria criteria) {
        log.debug("REST request to count ChefProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(chefProfileQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /chef-profiles/:id : get the "id" chefProfile.
     *
     * @param id the id of the chefProfile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chefProfile, or with status 404 (Not Found)
     */
    @GetMapping("/chef-profiles/{id}")
    public ResponseEntity<ChefProfile> getChefProfile(@PathVariable Long id) {
        log.debug("REST request to get ChefProfile : {}", id);
        Optional<ChefProfile> chefProfile = chefProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chefProfile);
    }

    /**
     * DELETE  /chef-profiles/:id : delete the "id" chefProfile.
     *
     * @param id the id of the chefProfile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chef-profiles/{id}")
    public ResponseEntity<Void> deleteChefProfile(@PathVariable Long id) {
        log.debug("REST request to delete ChefProfile : {}", id);
        chefProfileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/chef-profiles?query=:query : search for the chefProfile corresponding
     * to the query.
     *
     * @param query the query of the chefProfile search
     * @return the result of the search
     */
    @GetMapping("/_search/chef-profiles")
    public List<ChefProfile> searchChefProfiles(@RequestParam String query) {
        log.debug("REST request to search ChefProfiles for query {}", query);
        return chefProfileService.search(query);
    }

}

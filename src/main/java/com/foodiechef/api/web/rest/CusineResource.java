package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.Cusine;
import com.foodiechef.api.service.CusineService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.CusineCriteria;
import com.foodiechef.api.service.CusineQueryService;
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
 * REST controller for managing Cusine.
 */
@RestController
@RequestMapping("/api")
public class CusineResource {

    private final Logger log = LoggerFactory.getLogger(CusineResource.class);

    private static final String ENTITY_NAME = "cusine";

    private final CusineService cusineService;

    private final CusineQueryService cusineQueryService;

    public CusineResource(CusineService cusineService, CusineQueryService cusineQueryService) {
        this.cusineService = cusineService;
        this.cusineQueryService = cusineQueryService;
    }

    /**
     * POST  /cusines : Create a new cusine.
     *
     * @param cusine the cusine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cusine, or with status 400 (Bad Request) if the cusine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cusines")
    public ResponseEntity<Cusine> createCusine(@Valid @RequestBody Cusine cusine) throws URISyntaxException {
        log.debug("REST request to save Cusine : {}", cusine);
        if (cusine.getId() != null) {
            throw new BadRequestAlertException("A new cusine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cusine result = cusineService.save(cusine);
        return ResponseEntity.created(new URI("/api/cusines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cusines : Updates an existing cusine.
     *
     * @param cusine the cusine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cusine,
     * or with status 400 (Bad Request) if the cusine is not valid,
     * or with status 500 (Internal Server Error) if the cusine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cusines")
    public ResponseEntity<Cusine> updateCusine(@Valid @RequestBody Cusine cusine) throws URISyntaxException {
        log.debug("REST request to update Cusine : {}", cusine);
        if (cusine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cusine result = cusineService.save(cusine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cusine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cusines : get all the cusines.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of cusines in body
     */
    @GetMapping("/cusines")
    public ResponseEntity<List<Cusine>> getAllCusines(CusineCriteria criteria) {
        log.debug("REST request to get Cusines by criteria: {}", criteria);
        List<Cusine> entityList = cusineQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /cusines/count : count all the cusines.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/cusines/count")
    public ResponseEntity<Long> countCusines(CusineCriteria criteria) {
        log.debug("REST request to count Cusines by criteria: {}", criteria);
        return ResponseEntity.ok().body(cusineQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /cusines/:id : get the "id" cusine.
     *
     * @param id the id of the cusine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cusine, or with status 404 (Not Found)
     */
    @GetMapping("/cusines/{id}")
    public ResponseEntity<Cusine> getCusine(@PathVariable Long id) {
        log.debug("REST request to get Cusine : {}", id);
        Optional<Cusine> cusine = cusineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cusine);
    }

    /**
     * DELETE  /cusines/:id : delete the "id" cusine.
     *
     * @param id the id of the cusine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cusines/{id}")
    public ResponseEntity<Void> deleteCusine(@PathVariable Long id) {
        log.debug("REST request to delete Cusine : {}", id);
        cusineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cusines?query=:query : search for the cusine corresponding
     * to the query.
     *
     * @param query the query of the cusine search
     * @return the result of the search
     */
    @GetMapping("/_search/cusines")
    public List<Cusine> searchCusines(@RequestParam String query) {
        log.debug("REST request to search Cusines for query {}", query);
        return cusineService.search(query);
    }

}

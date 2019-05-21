package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.NutritionInformation;
import com.foodiechef.api.service.NutritionInformationService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.NutritionInformationCriteria;
import com.foodiechef.api.service.NutritionInformationQueryService;
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
 * REST controller for managing NutritionInformation.
 */
@RestController
@RequestMapping("/api")
public class NutritionInformationResource {

    private final Logger log = LoggerFactory.getLogger(NutritionInformationResource.class);

    private static final String ENTITY_NAME = "nutritionInformation";

    private final NutritionInformationService nutritionInformationService;

    private final NutritionInformationQueryService nutritionInformationQueryService;

    public NutritionInformationResource(NutritionInformationService nutritionInformationService, NutritionInformationQueryService nutritionInformationQueryService) {
        this.nutritionInformationService = nutritionInformationService;
        this.nutritionInformationQueryService = nutritionInformationQueryService;
    }

    /**
     * POST  /nutrition-informations : Create a new nutritionInformation.
     *
     * @param nutritionInformation the nutritionInformation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new nutritionInformation, or with status 400 (Bad Request) if the nutritionInformation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/nutrition-informations")
    public ResponseEntity<NutritionInformation> createNutritionInformation(@Valid @RequestBody NutritionInformation nutritionInformation) throws URISyntaxException {
        log.debug("REST request to save NutritionInformation : {}", nutritionInformation);
        if (nutritionInformation.getId() != null) {
            throw new BadRequestAlertException("A new nutritionInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NutritionInformation result = nutritionInformationService.save(nutritionInformation);
        return ResponseEntity.created(new URI("/api/nutrition-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /nutrition-informations : Updates an existing nutritionInformation.
     *
     * @param nutritionInformation the nutritionInformation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated nutritionInformation,
     * or with status 400 (Bad Request) if the nutritionInformation is not valid,
     * or with status 500 (Internal Server Error) if the nutritionInformation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/nutrition-informations")
    public ResponseEntity<NutritionInformation> updateNutritionInformation(@Valid @RequestBody NutritionInformation nutritionInformation) throws URISyntaxException {
        log.debug("REST request to update NutritionInformation : {}", nutritionInformation);
        if (nutritionInformation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NutritionInformation result = nutritionInformationService.save(nutritionInformation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nutritionInformation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /nutrition-informations : get all the nutritionInformations.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of nutritionInformations in body
     */
    @GetMapping("/nutrition-informations")
    public ResponseEntity<List<NutritionInformation>> getAllNutritionInformations(NutritionInformationCriteria criteria) {
        log.debug("REST request to get NutritionInformations by criteria: {}", criteria);
        List<NutritionInformation> entityList = nutritionInformationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /nutrition-informations/count : count all the nutritionInformations.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/nutrition-informations/count")
    public ResponseEntity<Long> countNutritionInformations(NutritionInformationCriteria criteria) {
        log.debug("REST request to count NutritionInformations by criteria: {}", criteria);
        return ResponseEntity.ok().body(nutritionInformationQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /nutrition-informations/:id : get the "id" nutritionInformation.
     *
     * @param id the id of the nutritionInformation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nutritionInformation, or with status 404 (Not Found)
     */
    @GetMapping("/nutrition-informations/{id}")
    public ResponseEntity<NutritionInformation> getNutritionInformation(@PathVariable Long id) {
        log.debug("REST request to get NutritionInformation : {}", id);
        Optional<NutritionInformation> nutritionInformation = nutritionInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nutritionInformation);
    }

    /**
     * DELETE  /nutrition-informations/:id : delete the "id" nutritionInformation.
     *
     * @param id the id of the nutritionInformation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/nutrition-informations/{id}")
    public ResponseEntity<Void> deleteNutritionInformation(@PathVariable Long id) {
        log.debug("REST request to delete NutritionInformation : {}", id);
        nutritionInformationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/nutrition-informations?query=:query : search for the nutritionInformation corresponding
     * to the query.
     *
     * @param query the query of the nutritionInformation search
     * @return the result of the search
     */
    @GetMapping("/_search/nutrition-informations")
    public List<NutritionInformation> searchNutritionInformations(@RequestParam String query) {
        log.debug("REST request to search NutritionInformations for query {}", query);
        return nutritionInformationService.search(query);
    }

}

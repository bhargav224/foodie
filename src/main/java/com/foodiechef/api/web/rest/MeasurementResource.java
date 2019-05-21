package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.Measurement;
import com.foodiechef.api.service.MeasurementService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.MeasurementCriteria;
import com.foodiechef.api.service.MeasurementQueryService;
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
 * REST controller for managing Measurement.
 */
@RestController
@RequestMapping("/api")
public class MeasurementResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementResource.class);

    private static final String ENTITY_NAME = "measurement";

    private final MeasurementService measurementService;

    private final MeasurementQueryService measurementQueryService;

    public MeasurementResource(MeasurementService measurementService, MeasurementQueryService measurementQueryService) {
        this.measurementService = measurementService;
        this.measurementQueryService = measurementQueryService;
    }

    /**
     * POST  /measurements : Create a new measurement.
     *
     * @param measurement the measurement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new measurement, or with status 400 (Bad Request) if the measurement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/measurements")
    public ResponseEntity<Measurement> createMeasurement(@Valid @RequestBody Measurement measurement) throws URISyntaxException {
        log.debug("REST request to save Measurement : {}", measurement);
        if (measurement.getId() != null) {
            throw new BadRequestAlertException("A new measurement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Measurement result = measurementService.save(measurement);
        return ResponseEntity.created(new URI("/api/measurements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /measurements : Updates an existing measurement.
     *
     * @param measurement the measurement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated measurement,
     * or with status 400 (Bad Request) if the measurement is not valid,
     * or with status 500 (Internal Server Error) if the measurement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/measurements")
    public ResponseEntity<Measurement> updateMeasurement(@Valid @RequestBody Measurement measurement) throws URISyntaxException {
        log.debug("REST request to update Measurement : {}", measurement);
        if (measurement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Measurement result = measurementService.save(measurement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, measurement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /measurements : get all the measurements.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of measurements in body
     */
    @GetMapping("/measurements")
    public ResponseEntity<List<Measurement>> getAllMeasurements(MeasurementCriteria criteria) {
        log.debug("REST request to get Measurements by criteria: {}", criteria);
        List<Measurement> entityList = measurementQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /measurements/count : count all the measurements.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/measurements/count")
    public ResponseEntity<Long> countMeasurements(MeasurementCriteria criteria) {
        log.debug("REST request to count Measurements by criteria: {}", criteria);
        return ResponseEntity.ok().body(measurementQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /measurements/:id : get the "id" measurement.
     *
     * @param id the id of the measurement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the measurement, or with status 404 (Not Found)
     */
    @GetMapping("/measurements/{id}")
    public ResponseEntity<Measurement> getMeasurement(@PathVariable Long id) {
        log.debug("REST request to get Measurement : {}", id);
        Optional<Measurement> measurement = measurementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(measurement);
    }

    /**
     * DELETE  /measurements/:id : delete the "id" measurement.
     *
     * @param id the id of the measurement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/measurements/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id) {
        log.debug("REST request to delete Measurement : {}", id);
        measurementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/measurements?query=:query : search for the measurement corresponding
     * to the query.
     *
     * @param query the query of the measurement search
     * @return the result of the search
     */
    @GetMapping("/_search/measurements")
    public List<Measurement> searchMeasurements(@RequestParam String query) {
        log.debug("REST request to search Measurements for query {}", query);
        return measurementService.search(query);
    }

}

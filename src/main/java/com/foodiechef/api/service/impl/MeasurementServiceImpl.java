package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.MeasurementService;
import com.foodiechef.api.domain.Measurement;
import com.foodiechef.api.repository.MeasurementRepository;
import com.foodiechef.api.repository.search.MeasurementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Measurement.
 */
@Service
@Transactional
public class MeasurementServiceImpl implements MeasurementService {

    private final Logger log = LoggerFactory.getLogger(MeasurementServiceImpl.class);

    private final MeasurementRepository measurementRepository;

    private final MeasurementSearchRepository measurementSearchRepository;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository, MeasurementSearchRepository measurementSearchRepository) {
        this.measurementRepository = measurementRepository;
        this.measurementSearchRepository = measurementSearchRepository;
    }

    /**
     * Save a measurement.
     *
     * @param measurement the entity to save
     * @return the persisted entity
     */
    @Override
    public Measurement save(Measurement measurement) {
        log.debug("Request to save Measurement : {}", measurement);
        Measurement result = measurementRepository.save(measurement);
        measurementSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the measurements.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Measurement> findAll() {
        log.debug("Request to get all Measurements");
        return measurementRepository.findAll();
    }


    /**
     * Get one measurement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Measurement> findOne(Long id) {
        log.debug("Request to get Measurement : {}", id);
        return measurementRepository.findById(id);
    }

    /**
     * Delete the measurement by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Measurement : {}", id);
        measurementRepository.deleteById(id);
        measurementSearchRepository.deleteById(id);
    }

    /**
     * Search for the measurement corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Measurement> search(String query) {
        log.debug("Request to search Measurements for query {}", query);
        return StreamSupport
            .stream(measurementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

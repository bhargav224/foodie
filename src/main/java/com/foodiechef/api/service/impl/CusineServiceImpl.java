package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.CusineService;
import com.foodiechef.api.domain.Cusine;
import com.foodiechef.api.repository.CusineRepository;
import com.foodiechef.api.repository.search.CusineSearchRepository;
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
 * Service Implementation for managing Cusine.
 */
@Service
@Transactional
public class CusineServiceImpl implements CusineService {

    private final Logger log = LoggerFactory.getLogger(CusineServiceImpl.class);

    private final CusineRepository cusineRepository;

    private final CusineSearchRepository cusineSearchRepository;

    public CusineServiceImpl(CusineRepository cusineRepository, CusineSearchRepository cusineSearchRepository) {
        this.cusineRepository = cusineRepository;
        this.cusineSearchRepository = cusineSearchRepository;
    }

    /**
     * Save a cusine.
     *
     * @param cusine the entity to save
     * @return the persisted entity
     */
    @Override
    public Cusine save(Cusine cusine) {
        log.debug("Request to save Cusine : {}", cusine);
        Cusine result = cusineRepository.save(cusine);
        cusineSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cusines.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cusine> findAll() {
        log.debug("Request to get all Cusines");
        return cusineRepository.findAll();
    }


    /**
     * Get one cusine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cusine> findOne(Long id) {
        log.debug("Request to get Cusine : {}", id);
        return cusineRepository.findById(id);
    }

    /**
     * Delete the cusine by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cusine : {}", id);
        cusineRepository.deleteById(id);
        cusineSearchRepository.deleteById(id);
    }

    /**
     * Search for the cusine corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cusine> search(String query) {
        log.debug("Request to search Cusines for query {}", query);
        return StreamSupport
            .stream(cusineSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

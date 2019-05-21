package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.NutritionInformationService;
import com.foodiechef.api.domain.NutritionInformation;
import com.foodiechef.api.repository.NutritionInformationRepository;
import com.foodiechef.api.repository.search.NutritionInformationSearchRepository;
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
 * Service Implementation for managing NutritionInformation.
 */
@Service
@Transactional
public class NutritionInformationServiceImpl implements NutritionInformationService {

    private final Logger log = LoggerFactory.getLogger(NutritionInformationServiceImpl.class);

    private final NutritionInformationRepository nutritionInformationRepository;

    private final NutritionInformationSearchRepository nutritionInformationSearchRepository;

    public NutritionInformationServiceImpl(NutritionInformationRepository nutritionInformationRepository, NutritionInformationSearchRepository nutritionInformationSearchRepository) {
        this.nutritionInformationRepository = nutritionInformationRepository;
        this.nutritionInformationSearchRepository = nutritionInformationSearchRepository;
    }

    /**
     * Save a nutritionInformation.
     *
     * @param nutritionInformation the entity to save
     * @return the persisted entity
     */
    @Override
    public NutritionInformation save(NutritionInformation nutritionInformation) {
        log.debug("Request to save NutritionInformation : {}", nutritionInformation);
        NutritionInformation result = nutritionInformationRepository.save(nutritionInformation);
        nutritionInformationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the nutritionInformations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NutritionInformation> findAll() {
        log.debug("Request to get all NutritionInformations");
        return nutritionInformationRepository.findAll();
    }


    /**
     * Get one nutritionInformation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NutritionInformation> findOne(Long id) {
        log.debug("Request to get NutritionInformation : {}", id);
        return nutritionInformationRepository.findById(id);
    }

    /**
     * Delete the nutritionInformation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NutritionInformation : {}", id);
        nutritionInformationRepository.deleteById(id);
        nutritionInformationSearchRepository.deleteById(id);
    }

    /**
     * Search for the nutritionInformation corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NutritionInformation> search(String query) {
        log.debug("Request to search NutritionInformations for query {}", query);
        return StreamSupport
            .stream(nutritionInformationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

package com.foodiechef.api.service.impl;

import com.foodiechef.api.service.FootnoteService;
import com.foodiechef.api.domain.Footnote;
import com.foodiechef.api.repository.FootnoteRepository;
import com.foodiechef.api.repository.search.FootnoteSearchRepository;
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
 * Service Implementation for managing Footnote.
 */
@Service
@Transactional
public class FootnoteServiceImpl implements FootnoteService {

    private final Logger log = LoggerFactory.getLogger(FootnoteServiceImpl.class);

    private final FootnoteRepository footnoteRepository;

    private final FootnoteSearchRepository footnoteSearchRepository;

    public FootnoteServiceImpl(FootnoteRepository footnoteRepository, FootnoteSearchRepository footnoteSearchRepository) {
        this.footnoteRepository = footnoteRepository;
        this.footnoteSearchRepository = footnoteSearchRepository;
    }

    /**
     * Save a footnote.
     *
     * @param footnote the entity to save
     * @return the persisted entity
     */
    @Override
    public Footnote save(Footnote footnote) {
        log.debug("Request to save Footnote : {}", footnote);
        Footnote result = footnoteRepository.save(footnote);
        footnoteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the footnotes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Footnote> findAll() {
        log.debug("Request to get all Footnotes");
        return footnoteRepository.findAll();
    }


    /**
     * Get one footnote by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Footnote> findOne(Long id) {
        log.debug("Request to get Footnote : {}", id);
        return footnoteRepository.findById(id);
    }

    /**
     * Delete the footnote by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Footnote : {}", id);
        footnoteRepository.deleteById(id);
        footnoteSearchRepository.deleteById(id);
    }

    /**
     * Search for the footnote corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Footnote> search(String query) {
        log.debug("Request to search Footnotes for query {}", query);
        return StreamSupport
            .stream(footnoteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

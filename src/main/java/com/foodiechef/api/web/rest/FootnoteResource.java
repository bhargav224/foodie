package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.Footnote;
import com.foodiechef.api.service.FootnoteService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.FootnoteCriteria;
import com.foodiechef.api.service.FootnoteQueryService;
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
 * REST controller for managing Footnote.
 */
@RestController
@RequestMapping("/api")
public class FootnoteResource {

    private final Logger log = LoggerFactory.getLogger(FootnoteResource.class);

    private static final String ENTITY_NAME = "footnote";

    private final FootnoteService footnoteService;

    private final FootnoteQueryService footnoteQueryService;

    public FootnoteResource(FootnoteService footnoteService, FootnoteQueryService footnoteQueryService) {
        this.footnoteService = footnoteService;
        this.footnoteQueryService = footnoteQueryService;
    }

    /**
     * POST  /footnotes : Create a new footnote.
     *
     * @param footnote the footnote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new footnote, or with status 400 (Bad Request) if the footnote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/footnotes")
    public ResponseEntity<Footnote> createFootnote(@Valid @RequestBody Footnote footnote) throws URISyntaxException {
        log.debug("REST request to save Footnote : {}", footnote);
        if (footnote.getId() != null) {
            throw new BadRequestAlertException("A new footnote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Footnote result = footnoteService.save(footnote);
        return ResponseEntity.created(new URI("/api/footnotes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /footnotes : Updates an existing footnote.
     *
     * @param footnote the footnote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated footnote,
     * or with status 400 (Bad Request) if the footnote is not valid,
     * or with status 500 (Internal Server Error) if the footnote couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/footnotes")
    public ResponseEntity<Footnote> updateFootnote(@Valid @RequestBody Footnote footnote) throws URISyntaxException {
        log.debug("REST request to update Footnote : {}", footnote);
        if (footnote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Footnote result = footnoteService.save(footnote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, footnote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /footnotes : get all the footnotes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of footnotes in body
     */
    @GetMapping("/footnotes")
    public ResponseEntity<List<Footnote>> getAllFootnotes(FootnoteCriteria criteria) {
        log.debug("REST request to get Footnotes by criteria: {}", criteria);
        List<Footnote> entityList = footnoteQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /footnotes/count : count all the footnotes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/footnotes/count")
    public ResponseEntity<Long> countFootnotes(FootnoteCriteria criteria) {
        log.debug("REST request to count Footnotes by criteria: {}", criteria);
        return ResponseEntity.ok().body(footnoteQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /footnotes/:id : get the "id" footnote.
     *
     * @param id the id of the footnote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the footnote, or with status 404 (Not Found)
     */
    @GetMapping("/footnotes/{id}")
    public ResponseEntity<Footnote> getFootnote(@PathVariable Long id) {
        log.debug("REST request to get Footnote : {}", id);
        Optional<Footnote> footnote = footnoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(footnote);
    }

    /**
     * DELETE  /footnotes/:id : delete the "id" footnote.
     *
     * @param id the id of the footnote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/footnotes/{id}")
    public ResponseEntity<Void> deleteFootnote(@PathVariable Long id) {
        log.debug("REST request to delete Footnote : {}", id);
        footnoteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/footnotes?query=:query : search for the footnote corresponding
     * to the query.
     *
     * @param query the query of the footnote search
     * @return the result of the search
     */
    @GetMapping("/_search/footnotes")
    public List<Footnote> searchFootnotes(@RequestParam String query) {
        log.debug("REST request to search Footnotes for query {}", query);
        return footnoteService.search(query);
    }

}

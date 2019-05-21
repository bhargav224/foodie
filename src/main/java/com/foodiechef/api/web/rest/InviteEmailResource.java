package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.InviteEmail;
import com.foodiechef.api.service.InviteEmailService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.InviteEmailCriteria;
import com.foodiechef.api.service.InviteEmailQueryService;
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
 * REST controller for managing InviteEmail.
 */
@RestController
@RequestMapping("/api")
public class InviteEmailResource {

    private final Logger log = LoggerFactory.getLogger(InviteEmailResource.class);

    private static final String ENTITY_NAME = "inviteEmail";

    private final InviteEmailService inviteEmailService;

    private final InviteEmailQueryService inviteEmailQueryService;

    public InviteEmailResource(InviteEmailService inviteEmailService, InviteEmailQueryService inviteEmailQueryService) {
        this.inviteEmailService = inviteEmailService;
        this.inviteEmailQueryService = inviteEmailQueryService;
    }

    /**
     * POST  /invite-emails : Create a new inviteEmail.
     *
     * @param inviteEmail the inviteEmail to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inviteEmail, or with status 400 (Bad Request) if the inviteEmail has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invite-emails")
    public ResponseEntity<InviteEmail> createInviteEmail(@Valid @RequestBody InviteEmail inviteEmail) throws URISyntaxException {
        log.debug("REST request to save InviteEmail : {}", inviteEmail);
        if (inviteEmail.getId() != null) {
            throw new BadRequestAlertException("A new inviteEmail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InviteEmail result = inviteEmailService.save(inviteEmail);
        return ResponseEntity.created(new URI("/api/invite-emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invite-emails : Updates an existing inviteEmail.
     *
     * @param inviteEmail the inviteEmail to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inviteEmail,
     * or with status 400 (Bad Request) if the inviteEmail is not valid,
     * or with status 500 (Internal Server Error) if the inviteEmail couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invite-emails")
    public ResponseEntity<InviteEmail> updateInviteEmail(@Valid @RequestBody InviteEmail inviteEmail) throws URISyntaxException {
        log.debug("REST request to update InviteEmail : {}", inviteEmail);
        if (inviteEmail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InviteEmail result = inviteEmailService.save(inviteEmail);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inviteEmail.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invite-emails : get all the inviteEmails.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of inviteEmails in body
     */
    @GetMapping("/invite-emails")
    public ResponseEntity<List<InviteEmail>> getAllInviteEmails(InviteEmailCriteria criteria) {
        log.debug("REST request to get InviteEmails by criteria: {}", criteria);
        List<InviteEmail> entityList = inviteEmailQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /invite-emails/count : count all the inviteEmails.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/invite-emails/count")
    public ResponseEntity<Long> countInviteEmails(InviteEmailCriteria criteria) {
        log.debug("REST request to count InviteEmails by criteria: {}", criteria);
        return ResponseEntity.ok().body(inviteEmailQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /invite-emails/:id : get the "id" inviteEmail.
     *
     * @param id the id of the inviteEmail to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inviteEmail, or with status 404 (Not Found)
     */
    @GetMapping("/invite-emails/{id}")
    public ResponseEntity<InviteEmail> getInviteEmail(@PathVariable Long id) {
        log.debug("REST request to get InviteEmail : {}", id);
        Optional<InviteEmail> inviteEmail = inviteEmailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inviteEmail);
    }

    /**
     * DELETE  /invite-emails/:id : delete the "id" inviteEmail.
     *
     * @param id the id of the inviteEmail to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invite-emails/{id}")
    public ResponseEntity<Void> deleteInviteEmail(@PathVariable Long id) {
        log.debug("REST request to delete InviteEmail : {}", id);
        inviteEmailService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/invite-emails?query=:query : search for the inviteEmail corresponding
     * to the query.
     *
     * @param query the query of the inviteEmail search
     * @return the result of the search
     */
    @GetMapping("/_search/invite-emails")
    public List<InviteEmail> searchInviteEmails(@RequestParam String query) {
        log.debug("REST request to search InviteEmails for query {}", query);
        return inviteEmailService.search(query);
    }

}

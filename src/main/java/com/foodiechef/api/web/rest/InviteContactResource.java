package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.InviteContact;
import com.foodiechef.api.service.InviteContactService;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import com.foodiechef.api.service.dto.InviteContactCriteria;
import com.foodiechef.api.service.InviteContactQueryService;
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
 * REST controller for managing InviteContact.
 */
@RestController
@RequestMapping("/api")
public class InviteContactResource {

    private final Logger log = LoggerFactory.getLogger(InviteContactResource.class);

    private static final String ENTITY_NAME = "inviteContact";

    private final InviteContactService inviteContactService;

    private final InviteContactQueryService inviteContactQueryService;

    public InviteContactResource(InviteContactService inviteContactService, InviteContactQueryService inviteContactQueryService) {
        this.inviteContactService = inviteContactService;
        this.inviteContactQueryService = inviteContactQueryService;
    }

    /**
     * POST  /invite-contacts : Create a new inviteContact.
     *
     * @param inviteContact the inviteContact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inviteContact, or with status 400 (Bad Request) if the inviteContact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invite-contacts")
    public ResponseEntity<InviteContact> createInviteContact(@Valid @RequestBody InviteContact inviteContact) throws URISyntaxException {
        log.debug("REST request to save InviteContact : {}", inviteContact);
        if (inviteContact.getId() != null) {
            throw new BadRequestAlertException("A new inviteContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InviteContact result = inviteContactService.save(inviteContact);
        return ResponseEntity.created(new URI("/api/invite-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invite-contacts : Updates an existing inviteContact.
     *
     * @param inviteContact the inviteContact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inviteContact,
     * or with status 400 (Bad Request) if the inviteContact is not valid,
     * or with status 500 (Internal Server Error) if the inviteContact couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invite-contacts")
    public ResponseEntity<InviteContact> updateInviteContact(@Valid @RequestBody InviteContact inviteContact) throws URISyntaxException {
        log.debug("REST request to update InviteContact : {}", inviteContact);
        if (inviteContact.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InviteContact result = inviteContactService.save(inviteContact);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inviteContact.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invite-contacts : get all the inviteContacts.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of inviteContacts in body
     */
    @GetMapping("/invite-contacts")
    public ResponseEntity<List<InviteContact>> getAllInviteContacts(InviteContactCriteria criteria) {
        log.debug("REST request to get InviteContacts by criteria: {}", criteria);
        List<InviteContact> entityList = inviteContactQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /invite-contacts/count : count all the inviteContacts.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/invite-contacts/count")
    public ResponseEntity<Long> countInviteContacts(InviteContactCriteria criteria) {
        log.debug("REST request to count InviteContacts by criteria: {}", criteria);
        return ResponseEntity.ok().body(inviteContactQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /invite-contacts/:id : get the "id" inviteContact.
     *
     * @param id the id of the inviteContact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inviteContact, or with status 404 (Not Found)
     */
    @GetMapping("/invite-contacts/{id}")
    public ResponseEntity<InviteContact> getInviteContact(@PathVariable Long id) {
        log.debug("REST request to get InviteContact : {}", id);
        Optional<InviteContact> inviteContact = inviteContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inviteContact);
    }

    /**
     * DELETE  /invite-contacts/:id : delete the "id" inviteContact.
     *
     * @param id the id of the inviteContact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invite-contacts/{id}")
    public ResponseEntity<Void> deleteInviteContact(@PathVariable Long id) {
        log.debug("REST request to delete InviteContact : {}", id);
        inviteContactService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/invite-contacts?query=:query : search for the inviteContact corresponding
     * to the query.
     *
     * @param query the query of the inviteContact search
     * @return the result of the search
     */
    @GetMapping("/_search/invite-contacts")
    public List<InviteContact> searchInviteContacts(@RequestParam String query) {
        log.debug("REST request to search InviteContacts for query {}", query);
        return inviteContactService.search(query);
    }

}

package com.foodiechef.api.web.rest;
import com.foodiechef.api.domain.UserInfoRole;
import com.foodiechef.api.repository.UserInfoRoleRepository;
import com.foodiechef.api.repository.search.UserInfoRoleSearchRepository;
import com.foodiechef.api.web.rest.errors.BadRequestAlertException;
import com.foodiechef.api.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing UserInfoRole.
 */
@RestController
@RequestMapping("/api")
public class UserInfoRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserInfoRoleResource.class);

    private static final String ENTITY_NAME = "userInfoRole";

    private final UserInfoRoleRepository userInfoRoleRepository;

    private final UserInfoRoleSearchRepository userInfoRoleSearchRepository;

    public UserInfoRoleResource(UserInfoRoleRepository userInfoRoleRepository, UserInfoRoleSearchRepository userInfoRoleSearchRepository) {
        this.userInfoRoleRepository = userInfoRoleRepository;
        this.userInfoRoleSearchRepository = userInfoRoleSearchRepository;
    }

    /**
     * POST  /user-info-roles : Create a new userInfoRole.
     *
     * @param userInfoRole the userInfoRole to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userInfoRole, or with status 400 (Bad Request) if the userInfoRole has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-info-roles")
    public ResponseEntity<UserInfoRole> createUserInfoRole(@RequestBody UserInfoRole userInfoRole) throws URISyntaxException {
        log.debug("REST request to save UserInfoRole : {}", userInfoRole);
        if (userInfoRole.getId() != null) {
            throw new BadRequestAlertException("A new userInfoRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserInfoRole result = userInfoRoleRepository.save(userInfoRole);
        userInfoRoleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/user-info-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-info-roles : Updates an existing userInfoRole.
     *
     * @param userInfoRole the userInfoRole to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userInfoRole,
     * or with status 400 (Bad Request) if the userInfoRole is not valid,
     * or with status 500 (Internal Server Error) if the userInfoRole couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-info-roles")
    public ResponseEntity<UserInfoRole> updateUserInfoRole(@RequestBody UserInfoRole userInfoRole) throws URISyntaxException {
        log.debug("REST request to update UserInfoRole : {}", userInfoRole);
        if (userInfoRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserInfoRole result = userInfoRoleRepository.save(userInfoRole);
        userInfoRoleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userInfoRole.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-info-roles : get all the userInfoRoles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userInfoRoles in body
     */
    @GetMapping("/user-info-roles")
    public List<UserInfoRole> getAllUserInfoRoles() {
        log.debug("REST request to get all UserInfoRoles");
        return userInfoRoleRepository.findAll();
    }

    /**
     * GET  /user-info-roles/:id : get the "id" userInfoRole.
     *
     * @param id the id of the userInfoRole to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userInfoRole, or with status 404 (Not Found)
     */
    @GetMapping("/user-info-roles/{id}")
    public ResponseEntity<UserInfoRole> getUserInfoRole(@PathVariable Long id) {
        log.debug("REST request to get UserInfoRole : {}", id);
        Optional<UserInfoRole> userInfoRole = userInfoRoleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userInfoRole);
    }

    /**
     * DELETE  /user-info-roles/:id : delete the "id" userInfoRole.
     *
     * @param id the id of the userInfoRole to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-info-roles/{id}")
    public ResponseEntity<Void> deleteUserInfoRole(@PathVariable Long id) {
        log.debug("REST request to delete UserInfoRole : {}", id);
        userInfoRoleRepository.deleteById(id);
        userInfoRoleSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-info-roles?query=:query : search for the userInfoRole corresponding
     * to the query.
     *
     * @param query the query of the userInfoRole search
     * @return the result of the search
     */
    @GetMapping("/_search/user-info-roles")
    public List<UserInfoRole> searchUserInfoRoles(@RequestParam String query) {
        log.debug("REST request to search UserInfoRoles for query {}", query);
        return StreamSupport
            .stream(userInfoRoleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

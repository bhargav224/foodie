package com.foodiechef.api.repository;

import com.foodiechef.api.domain.InviteEmail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InviteEmail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InviteEmailRepository extends JpaRepository<InviteEmail, Long>, JpaSpecificationExecutor<InviteEmail> {

}

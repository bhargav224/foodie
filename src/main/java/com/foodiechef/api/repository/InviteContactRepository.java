package com.foodiechef.api.repository;

import com.foodiechef.api.domain.InviteContact;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InviteContact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InviteContactRepository extends JpaRepository<InviteContact, Long>, JpaSpecificationExecutor<InviteContact> {

}

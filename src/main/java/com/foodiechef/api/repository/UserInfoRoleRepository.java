package com.foodiechef.api.repository;

import com.foodiechef.api.domain.UserInfoRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserInfoRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInfoRoleRepository extends JpaRepository<UserInfoRole, Long> {

}

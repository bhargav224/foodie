package com.foodiechef.api.repository;

import com.foodiechef.api.domain.ChefProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChefProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefProfileRepository extends JpaRepository<ChefProfile, Long>, JpaSpecificationExecutor<ChefProfile> {

}

package com.foodiechef.api.repository;

import com.foodiechef.api.domain.Cusine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Cusine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CusineRepository extends JpaRepository<Cusine, Long>, JpaSpecificationExecutor<Cusine> {

}

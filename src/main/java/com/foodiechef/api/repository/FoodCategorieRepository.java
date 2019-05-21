package com.foodiechef.api.repository;

import com.foodiechef.api.domain.FoodCategorie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FoodCategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodCategorieRepository extends JpaRepository<FoodCategorie, Long>, JpaSpecificationExecutor<FoodCategorie> {

}

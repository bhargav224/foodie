package com.foodiechef.api.repository;

import com.foodiechef.api.domain.RestaurantMenu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RestaurantMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long>, JpaSpecificationExecutor<RestaurantMenu> {

}

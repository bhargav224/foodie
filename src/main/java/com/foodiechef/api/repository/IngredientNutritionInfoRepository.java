package com.foodiechef.api.repository;

import com.foodiechef.api.domain.IngredientNutritionInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IngredientNutritionInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredientNutritionInfoRepository extends JpaRepository<IngredientNutritionInfo, Long>, JpaSpecificationExecutor<IngredientNutritionInfo> {

}

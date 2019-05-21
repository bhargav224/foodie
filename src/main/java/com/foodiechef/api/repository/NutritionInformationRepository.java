package com.foodiechef.api.repository;

import com.foodiechef.api.domain.NutritionInformation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NutritionInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NutritionInformationRepository extends JpaRepository<NutritionInformation, Long>, JpaSpecificationExecutor<NutritionInformation> {

}

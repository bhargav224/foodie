package com.foodiechef.api.repository;

import com.foodiechef.api.domain.RecipeImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecipeImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long>, JpaSpecificationExecutor<RecipeImage> {

}

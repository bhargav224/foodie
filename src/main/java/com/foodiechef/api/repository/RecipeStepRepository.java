package com.foodiechef.api.repository;

import com.foodiechef.api.domain.RecipeStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecipeStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long>, JpaSpecificationExecutor<RecipeStep> {

}

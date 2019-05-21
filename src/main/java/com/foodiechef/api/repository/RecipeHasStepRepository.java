package com.foodiechef.api.repository;

import com.foodiechef.api.domain.RecipeHasStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecipeHasStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeHasStepRepository extends JpaRepository<RecipeHasStep, Long>, JpaSpecificationExecutor<RecipeHasStep> {

}

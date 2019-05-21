package com.foodiechef.api.repository;

import com.foodiechef.api.domain.RecipeRating;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecipeRating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long>, JpaSpecificationExecutor<RecipeRating> {

}

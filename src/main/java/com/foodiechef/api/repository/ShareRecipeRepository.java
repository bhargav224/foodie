package com.foodiechef.api.repository;

import com.foodiechef.api.domain.ShareRecipe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShareRecipe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShareRecipeRepository extends JpaRepository<ShareRecipe, Long>, JpaSpecificationExecutor<ShareRecipe> {

}

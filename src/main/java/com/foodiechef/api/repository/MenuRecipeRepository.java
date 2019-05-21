package com.foodiechef.api.repository;

import com.foodiechef.api.domain.MenuRecipe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MenuRecipe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuRecipeRepository extends JpaRepository<MenuRecipe, Long>, JpaSpecificationExecutor<MenuRecipe> {

}

package com.foodiechef.api.repository;

import com.foodiechef.api.domain.RecipeComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecipeComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long>, JpaSpecificationExecutor<RecipeComment> {

}

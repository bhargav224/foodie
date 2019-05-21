package com.foodiechef.api.repository;

import com.foodiechef.api.domain.RecipeLike;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecipeLike entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long>, JpaSpecificationExecutor<RecipeLike> {

}

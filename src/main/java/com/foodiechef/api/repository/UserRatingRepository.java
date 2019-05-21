package com.foodiechef.api.repository;

import com.foodiechef.api.domain.UserRating;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserRating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long>, JpaSpecificationExecutor<UserRating> {

}

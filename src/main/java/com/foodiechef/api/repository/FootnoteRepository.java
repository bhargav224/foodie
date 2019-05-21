package com.foodiechef.api.repository;

import com.foodiechef.api.domain.Footnote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Footnote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FootnoteRepository extends JpaRepository<Footnote, Long>, JpaSpecificationExecutor<Footnote> {

}

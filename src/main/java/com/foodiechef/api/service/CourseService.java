package com.foodiechef.api.service;

import com.foodiechef.api.domain.Course;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Course.
 */
public interface CourseService {

    /**
     * Save a course.
     *
     * @param course the entity to save
     * @return the persisted entity
     */
    Course save(Course course);

    /**
     * Get all the courses.
     *
     * @return the list of entities
     */
    List<Course> findAll();


    /**
     * Get the "id" course.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Course> findOne(Long id);

    /**
     * Delete the "id" course.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the course corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Course> search(String query);
}

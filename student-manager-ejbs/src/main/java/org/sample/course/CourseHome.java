package org.sample.course;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Home interface for Course Entity Bean
 */
public interface CourseHome extends EJBLocalHome {
    
    /**
     * Create a new course
     */
    Course create(Long courseId, String courseName, String courseCode) throws CreateException;
    
    /**
     * Find course by primary key
     */
    Course findByPrimaryKey(Long courseId) throws FinderException;
    
    /**
     * Find all courses
     */
    java.util.Collection findAll() throws FinderException;
    
    /**
     * Find course by course code
     */
    Course findByCourseCode(String courseCode) throws FinderException;
}


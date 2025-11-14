package org.sample.student;

import org.sample.course.Course;
import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Local interface for Student Entity Bean
 */
public interface Student extends EJBLocalObject {
    
    /**
     * Get student ID
     */
    Long getStudentId();
    
    /**
     * Set student ID
     */
    void setStudentId(Long studentId);
    
    /**
     * Get student name
     */
    String getName();
    
    /**
     * Set student name
     */
    void setName(String name);
    
    /**
     * Get student email
     */
    String getEmail();
    
    /**
     * Set student email
     */
    void setEmail(String email);
    
    /**
     * Get collection of courses this student is enrolled in
     */
    Collection<Course> getCourses();
    
    /**
     * Set collection of courses
     */
    void setCourses(Collection<Course> courses);
    
    /**
     * Add a course to this student
     */
    void addCourse(Course course);
    
    /**
     * Get list of courses for this student
     */
    Collection<Course> getCourseList();
}


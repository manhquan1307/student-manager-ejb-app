package org.sample.course;

import org.sample.student.Student;
import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Local interface for Course Entity Bean
 */
public interface Course extends EJBLocalObject {
    
    /**
     * Get course ID
     */
    Long getCourseId();
    
    /**
     * Set course ID
     */
    void setCourseId(Long courseId);
    
    /**
     * Get course name
     */
    String getCourseName();
    
    /**
     * Set course name
     */
    void setCourseName(String courseName);
    
    /**
     * Get course code
     */
    String getCourseCode();
    
    /**
     * Set course code
     */
    void setCourseCode(String courseCode);
    
    /**
     * Get collection of students enrolled in this course
     */
    Collection<Student> getStudents();
    
    /**
     * Set collection of students
     */
    void setStudents(Collection<Student> students);
}


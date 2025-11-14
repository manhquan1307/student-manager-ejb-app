package org.sample.student;

import org.sample.course.Course;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.CreateException;
import java.util.Collection;

/**
 * CMP Entity Bean implementation for Student
 */
public abstract class StudentBean implements EntityBean {
    
    private EntityContext entityContext;
    
    // CMP fields - abstract getters and setters
    public abstract Long getStudentId();
    public abstract void setStudentId(Long studentId);
    
    public abstract String getName();
    public abstract void setName(String name);
    
    public abstract String getEmail();
    public abstract void setEmail(String email);
    
    // CMR fields - abstract getters and setters for many-to-many relationship
    public abstract Collection<Course> getCourses();
    public abstract void setCourses(Collection<Course> courses);
    
    // Business methods
    public Long ejbCreate(Long studentId, String name, String email) throws CreateException {
        setStudentId(studentId);
        setName(name);
        setEmail(email);
        return null;
    }
    
    public void ejbPostCreate(Long studentId, String name, String email) throws CreateException {
        // Post-create initialization if needed
    }
    
    /**
     * Add a course to this student
     */
    public void addCourse(Course course) {
        Collection<Course> courses = getCourses();
        if (courses == null) {
            courses = new java.util.ArrayList<Course>();
        }
        if (!courses.contains(course)) {
            courses.add(course);
            setCourses(courses);
        }
    }
    
    /**
     * Get list of courses for this student
     */
    public Collection<Course> getCourseList() {
        return getCourses();
    }
    
    // EntityBean interface methods
    @Override
    public void setEntityContext(EntityContext entityContext) {
        this.entityContext = entityContext;
    }
    
    @Override
    public void unsetEntityContext() {
        this.entityContext = null;
    }
    
    @Override
    public void ejbRemove() {
        // Cleanup if needed
    }
    
    @Override
    public void ejbActivate() {
        // Activation logic if needed
    }
    
    @Override
    public void ejbPassivate() {
        // Passivation logic if needed
    }
    
    @Override
    public void ejbLoad() {
        // Load logic if needed
    }
    
    @Override
    public void ejbStore() {
        // Store logic if needed
    }
}


package org.sample.course;

import org.sample.student.Student;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.CreateException;
import java.util.Collection;

/**
 * CMP Entity Bean implementation for Course
 */
public abstract class CourseBean implements EntityBean {
    
    private EntityContext entityContext;
    
    // CMP fields - abstract getters and setters
    public abstract Long getCourseId();
    public abstract void setCourseId(Long courseId);
    
    public abstract String getCourseName();
    public abstract void setCourseName(String courseName);
    
    public abstract String getCourseCode();
    public abstract void setCourseCode(String courseCode);
    
    // CMR fields - abstract getters and setters for many-to-many relationship
    public abstract Collection<Student> getStudents();
    public abstract void setStudents(Collection<Student> students);
    
    // Business methods
    public Long ejbCreate(Long courseId, String courseName, String courseCode) throws CreateException {
        setCourseId(courseId);
        setCourseName(courseName);
        setCourseCode(courseCode);
        return null;
    }
    
    public void ejbPostCreate(Long courseId, String courseName, String courseCode) throws CreateException {
        // Post-create initialization if needed
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


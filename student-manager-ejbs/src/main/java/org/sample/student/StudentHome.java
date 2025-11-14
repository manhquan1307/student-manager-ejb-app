package org.sample.student;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Home interface for Student Entity Bean
 */
public interface StudentHome extends EJBLocalHome {
    
    /**
     * Create a new student
     */
    Student create(Long studentId, String name, String email) throws CreateException;
    
    /**
     * Find student by primary key
     */
    Student findByPrimaryKey(Long studentId) throws FinderException;
    
    /**
     * Find all students
     */
    java.util.Collection findAll() throws FinderException;
    
    /**
     * Find student by email
     */
    Student findByEmail(String email) throws FinderException;
}


package org.sample.student;

import org.sample.course.Course;
import org.sample.course.CourseHome;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Collection;

/**
 * Facade for managing Students and Courses
 * Demonstrates the required functionality:
 * - Create new student and course
 * - Add a course to a student (addCourse)
 * - List courses for a student (getCourseList)
 */
@Stateless
@LocalBean
public class StudentManagerFacade {
    
    /**
     * Create a new student
     * @param studentId Student ID
     * @param name Student name
     * @param email Student email
     * @return Created Student
     */
    public Student createStudent(Long studentId, String name, String email) {
        try {
            InitialContext ctx = new InitialContext();
            StudentHome studentHome = (StudentHome) ctx.lookup("java:comp/env/ejb/StudentHome");
            return studentHome.create(studentId, name, email);
        } catch (NamingException | javax.ejb.CreateException e) {
            throw new RuntimeException("Error creating student", e);
        }
    }
    
    /**
     * Create a new course
     * @param courseId Course ID
     * @param courseName Course name
     * @param courseCode Course code
     * @return Created Course
     */
    public Course createCourse(Long courseId, String courseName, String courseCode) {
        try {
            InitialContext ctx = new InitialContext();
            CourseHome courseHome = (CourseHome) ctx.lookup("java:comp/env/ejb/CourseHome");
            return courseHome.create(courseId, courseName, courseCode);
        } catch (NamingException | javax.ejb.CreateException e) {
            throw new RuntimeException("Error creating course", e);
        }
    }
    
    /**
     * Add a course to a student (addCourse)
     * @param studentId Student ID
     * @param courseId Course ID
     */
    public void addCourseToStudent(Long studentId, Long courseId) {
        try {
            InitialContext ctx = new InitialContext();
            StudentHome studentHome = (StudentHome) ctx.lookup("java:comp/env/ejb/StudentHome");
            CourseHome courseHome = (CourseHome) ctx.lookup("java:comp/env/ejb/CourseHome");
            
            Student student = studentHome.findByPrimaryKey(studentId);
            Course course = courseHome.findByPrimaryKey(courseId);
            
            if (student != null && course != null) {
                student.addCourse(course);
            }
        } catch (NamingException | javax.ejb.FinderException e) {
            throw new RuntimeException("Error adding course to student", e);
        }
    }
    
    /**
     * List courses for a student (getCourseList)
     * @param studentId Student ID
     * @return Collection of courses
     */
    public Collection<Course> getCourseList(Long studentId) {
        try {
            InitialContext ctx = new InitialContext();
            StudentHome studentHome = (StudentHome) ctx.lookup("java:comp/env/ejb/StudentHome");
            Student student = studentHome.findByPrimaryKey(studentId);
            
            if (student != null) {
                return student.getCourseList();
            }
            return null;
        } catch (NamingException | javax.ejb.FinderException e) {
            throw new RuntimeException("Error getting course list", e);
        }
    }
    
    /**
     * Find student by primary key
     */
    public Student findStudent(Long studentId) {
        try {
            InitialContext ctx = new InitialContext();
            StudentHome studentHome = (StudentHome) ctx.lookup("java:comp/env/ejb/StudentHome");
            return studentHome.findByPrimaryKey(studentId);
        } catch (NamingException | javax.ejb.FinderException e) {
            throw new RuntimeException("Error finding student", e);
        }
    }
    
    /**
     * Find course by primary key
     */
    public Course findCourse(Long courseId) {
        try {
            InitialContext ctx = new InitialContext();
            CourseHome courseHome = (CourseHome) ctx.lookup("java:comp/env/ejb/CourseHome");
            return courseHome.findByPrimaryKey(courseId);
        } catch (NamingException | javax.ejb.FinderException e) {
            throw new RuntimeException("Error finding course", e);
        }
    }
}


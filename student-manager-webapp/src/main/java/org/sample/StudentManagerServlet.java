package org.sample;

import org.sample.course.Course;
import org.sample.course.CourseHome;
import org.sample.student.Student;
import org.sample.student.StudentHome;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class StudentManagerServlet extends HttpServlet {
    
    private StudentHome getStudentHome() throws NamingException {
        InitialContext ctx = new InitialContext();
        try {
            String[] jndiNames = {
                "java:comp/env/ejb/StudentHome",
                "java:module/StudentHome",
                "java:app/student-manager-ejbs/StudentHome",
                "java:app/student-manager-ejbs-0.0.1-SNAPSHOT/StudentHome",
                "java:global/student-manager-ear/student-manager-ejbs/StudentHome",
                "java:global/student-manager-ear/student-manager-ejbs-0.0.1-SNAPSHOT/StudentHome",
                "java:global/student-manager-ear-0.0.1-SNAPSHOT/student-manager-ejbs/StudentHome",
                "java:global/student-manager-ear-0.0.1-SNAPSHOT/student-manager-ejbs-0.0.1-SNAPSHOT/StudentHome"
            };
            
            for (String jndiName : jndiNames) {
                try {
                    Object obj = ctx.lookup(jndiName);
                    if (obj != null && obj instanceof StudentHome) {
                        return (StudentHome) obj;
                    }
                } catch (Exception e) {
                    // Continue
                }
            }
            StringBuilder tried = new StringBuilder();
            for (int i = 0; i < jndiNames.length; i++) {
                if (i > 0) tried.append(", ");
                tried.append(jndiNames[i]);
            }
            throw new NamingException("Cannot find StudentHome. Tried: " + tried.toString());
        } finally {
            ctx.close();
        }
    }
    
    private CourseHome getCourseHome() throws NamingException {
        InitialContext ctx = new InitialContext();
        try {
            String[] jndiNames = {
                "java:comp/env/ejb/CourseHome",
                "java:module/CourseHome",
                "java:app/student-manager-ejbs/CourseHome",
                "java:app/student-manager-ejbs-0.0.1-SNAPSHOT/CourseHome",
                "java:global/student-manager-ear/student-manager-ejbs/CourseHome",
                "java:global/student-manager-ear/student-manager-ejbs-0.0.1-SNAPSHOT/CourseHome",
                "java:global/student-manager-ear-0.0.1-SNAPSHOT/student-manager-ejbs/CourseHome",
                "java:global/student-manager-ear-0.0.1-SNAPSHOT/student-manager-ejbs-0.0.1-SNAPSHOT/CourseHome"
            };
            
            for (String jndiName : jndiNames) {
                try {
                    Object obj = ctx.lookup(jndiName);
                    if (obj != null && obj instanceof CourseHome) {
                        return (CourseHome) obj;
                    }
                } catch (Exception e) {
                    // Continue
                }
            }
            StringBuilder tried = new StringBuilder();
            for (int i = 0; i < jndiNames.length; i++) {
                if (i > 0) tried.append(", ");
                tried.append(jndiNames[i]);
            }
            throw new NamingException("Cannot find CourseHome. Tried: " + tried.toString());
        } finally {
            ctx.close();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            switch (action) {
                case "createStudent":
                    showCreateStudentForm(out);
                    break;
                case "editStudent":
                    showEditStudentForm(out, request);
                    break;
                case "createCourse":
                    showCreateCourseForm(out);
                    break;
                case "editCourse":
                    showEditCourseForm(out, request);
                    break;
                case "addCourse":
                    showAddCourseForm(out, request);
                    break;
                case "viewCourses":
                    showStudentCourses(out, request);
                    break;
                case "deleteStudent":
                    handleDeleteStudent(out, request);
                    break;
                case "deleteCourse":
                    handleDeleteCourse(out, request);
                    break;
                case "removeCourse":
                    handleRemoveCourse(out, request);
                    break;
                case "list":
                default:
                    showMainPage(out);
                    break;
            }
        } catch (Exception e) {
            showErrorPage(out, e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            if ("createStudent".equals(action)) {
                handleCreateStudent(out, request);
            } else if ("updateStudent".equals(action)) {
                handleUpdateStudent(out, request);
            } else if ("createCourse".equals(action)) {
                handleCreateCourse(out, request);
            } else if ("updateCourse".equals(action)) {
                handleUpdateCourse(out, request);
            } else if ("addCourse".equals(action)) {
                handleAddCourse(out, request);
            } else {
                showErrorPage(out, new Exception("Unknown action: " + action));
            }
        } catch (Exception e) {
            showErrorPage(out, e);
        }
    }
    
    // ========== CSS STYLES ==========
    private String getCommonStyles() {
        return "<style>" +
            "* { margin: 0; padding: 0; box-sizing: border-box; }" +
            "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }" +
            ".container { max-width: 1400px; margin: 0 auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); }" +
            "h1 { color: #333; margin-bottom: 10px; font-size: 2.5em; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }" +
            "h2 { color: #555; margin-top: 30px; margin-bottom: 20px; padding-bottom: 10px; border-bottom: 3px solid #667eea; font-size: 1.8em; }" +
            "h3 { color: #666; margin: 20px 0 15px 0; font-size: 1.3em; }" +
            ".menu { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; margin: 30px 0; }" +
            ".btn { display: inline-block; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: 600; transition: all 0.3s ease; text-align: center; border: none; cursor: pointer; font-size: 14px; }" +
            ".btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }" +
            ".btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4); }" +
            ".btn-success { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); color: white; }" +
            ".btn-success:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(17, 153, 142, 0.4); }" +
            ".btn-info { background: linear-gradient(135deg, #3494E6 0%, #EC6EAD 100%); color: white; }" +
            ".btn-info:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(52, 148, 230, 0.4); }" +
            ".btn-warning { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; }" +
            ".btn-warning:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(245, 87, 108, 0.4); }" +
            ".btn-danger { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); color: white; }" +
            ".btn-danger:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(250, 112, 154, 0.4); }" +
            ".btn-sm { padding: 8px 16px; font-size: 12px; }" +
            "table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
            "th { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px; text-align: left; font-weight: 600; }" +
            "td { padding: 12px 15px; border-bottom: 1px solid #eee; }" +
            "tr:hover { background-color: #f8f9ff; transition: background 0.2s; }" +
            "tr:last-child td { border-bottom: none; }" +
            ".form-group { margin-bottom: 20px; }" +
            "label { display: block; margin-bottom: 8px; color: #555; font-weight: 600; font-size: 14px; }" +
            "input[type='text'], input[type='number'], input[type='email'], select { width: 100%; padding: 12px; border: 2px solid #e0e0e0; border-radius: 8px; font-size: 14px; transition: border 0.3s; }" +
            "input:focus, select:focus { outline: none; border-color: #667eea; }" +
            ".alert { padding: 15px; border-radius: 8px; margin: 15px 0; }" +
            ".alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }" +
            ".alert-error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }" +
            ".alert-info { background: #d1ecf1; color: #0c5460; border: 1px solid #bee5eb; }" +
            ".empty-msg { text-align: center; padding: 40px; color: #999; font-style: italic; }" +
            ".action-buttons { display: flex; gap: 8px; }" +
            ".card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); margin-bottom: 20px; }" +
            ".stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin: 30px 0; }" +
            ".stat-card { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 25px; border-radius: 10px; text-align: center; }" +
            ".stat-card h3 { color: white; margin: 0 0 10px 0; font-size: 2em; }" +
            ".stat-card p { margin: 0; opacity: 0.9; }" +
            "</style>";
    }
    
    // ========== MAIN PAGE ==========
    private void showMainPage(PrintWriter out) throws Exception {
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Student Manager System</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Student Manager System</h1>");
        out.println("<p style='color: #666; margin-bottom: 30px; font-size: 1.1em;'>Complete CRUD Management for Students and Courses</p>");
        
        // Statistics
        try {
            StudentHome studentHome = getStudentHome();
            CourseHome courseHome = getCourseHome();
            Collection<Student> students = studentHome.findAll();
            Collection<Course> courses = courseHome.findAll();
            
            int studentCount = (students != null) ? students.size() : 0;
            int courseCount = (courses != null) ? courses.size() : 0;
            
            out.println("<div class='stats'>");
            out.println("<div class='stat-card'><h3>" + studentCount + "</h3><p>Total Students</p></div>");
            out.println("<div class='stat-card'><h3>" + courseCount + "</h3><p>Total Courses</p></div>");
            out.println("</div>");
        } catch (Exception e) {
            // Ignore stats error
        }
        
        // Menu
        out.println("<div class='menu'>");
        out.println("<a href='student-manager?action=createStudent' class='btn btn-success'>Create Student</a>");
        out.println("<a href='student-manager?action=createCourse' class='btn btn-success'>Create Course</a>");
        out.println("<a href='student-manager?action=addCourse' class='btn btn-info'>Add Course to Student</a>");
        out.println("<a href='student-manager?action=viewCourses' class='btn btn-info'>View Student Courses</a>");
        out.println("</div>");
        
        // Students List
        out.println("<h2>👥 Students</h2>");
        try {
            StudentHome studentHome = getStudentHome();
            Collection<Student> students = studentHome.findAll();
            
            if (students != null && !students.isEmpty()) {
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>");
                for (Student student : students) {
                    out.println("<tr>");
                    out.println("<td><strong>" + (student.getStudentId() != null ? student.getStudentId() : "N/A") + "</strong></td>");
                    out.println("<td>" + (student.getName() != null ? student.getName() : "N/A") + "</td>");
                    out.println("<td>" + (student.getEmail() != null ? student.getEmail() : "N/A") + "</td>");
                    out.println("<td><div class='action-buttons'>");
                    out.println("<a href='student-manager?action=editStudent&id=" + student.getStudentId() + "' class='btn btn-warning btn-sm'>Edit</a>");
                    out.println("<a href='student-manager?action=viewCourses&studentId=" + student.getStudentId() + "' class='btn btn-info btn-sm'>Courses</a>");
                    out.println("<a href='student-manager?action=deleteStudent&id=" + student.getStudentId() + "' class='btn btn-danger btn-sm' onclick='return confirm(\"Are you sure you want to delete this student?\")'>Delete</a>");
                    out.println("</div></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<div class='empty-msg'>No students found. <a href='student-manager?action=createStudent' class='btn btn-success btn-sm'>Create one now</a></div>");
            }
        } catch (Exception e) {
            out.println("<div class='alert alert-error'>Error loading students: " + e.getMessage() + "</div>");
        }
        
        // Courses List
        out.println("<h2>Courses</h2>");
        try {
            CourseHome courseHome = getCourseHome();
            Collection<Course> courses = courseHome.findAll();
            
            if (courses != null && !courses.isEmpty()) {
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Course Name</th><th>Course Code</th><th>Actions</th></tr>");
                for (Course course : courses) {
                    out.println("<tr>");
                    out.println("<td><strong>" + (course.getCourseId() != null ? course.getCourseId() : "N/A") + "</strong></td>");
                    out.println("<td>" + (course.getCourseName() != null ? course.getCourseName() : "N/A") + "</td>");
                    out.println("<td><code style='background: #f0f0f0; padding: 4px 8px; border-radius: 4px;'>" + (course.getCourseCode() != null ? course.getCourseCode() : "N/A") + "</code></td>");
                    out.println("<td><div class='action-buttons'>");
                    out.println("<a href='student-manager?action=editCourse&id=" + course.getCourseId() + "' class='btn btn-warning btn-sm'>Edit</a>");
                    out.println("<a href='student-manager?action=deleteCourse&id=" + course.getCourseId() + "' class='btn btn-danger btn-sm' onclick='return confirm(\"Are you sure you want to delete this course?\")'>Delete</a>");
                    out.println("</div></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<div class='empty-msg'>No courses found. <a href='student-manager?action=createCourse' class='btn btn-success btn-sm'>Create one now</a></div>");
            }
        } catch (Exception e) {
            out.println("<div class='alert alert-error'>Error loading courses: " + e.getMessage() + "</div>");
        }
        
        out.println("</div></body></html>");
    }
    
    // ========== CREATE STUDENT ==========
    private void showCreateStudentForm(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Create Student</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Create New Student</h1>");
        out.println("<div class='card'>");
        out.println("<form method='post' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='createStudent'>");
        out.println("<div class='form-group'>");
        out.println("<label>Student ID:</label>");
        out.println("<input type='number' name='studentId' required>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Student Name:</label>");
        out.println("<input type='text' name='name' required>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Email:</label>");
        out.println("<input type='email' name='email' required>");
        out.println("</div>");
        out.println("<button type='submit' class='btn btn-success'>Create Student</button>");
        out.println("<a href='student-manager' class='btn btn-primary' style='margin-left: 10px;'>Cancel</a>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div></body></html>");
    }
    
    private void handleCreateStudent(PrintWriter out, HttpServletRequest request) throws Exception {
        Long studentId = Long.parseLong(request.getParameter("studentId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        StudentHome studentHome = getStudentHome();
        studentHome.create(studentId, name, email);
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Success</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-success'>");
        out.println("<h2>Student Created Successfully!</h2>");
        out.println("<p><strong>Student ID:</strong> " + studentId + "</p>");
        out.println("<p><strong>Name:</strong> " + name + "</p>");
        out.println("<p><strong>Email:</strong> " + email + "</p>");
        out.println("</div>");
        out.println("<a href='student-manager' class='btn btn-primary'>Back to Main</a>");
        out.println("</div></body></html>");
    }
    
    // ========== EDIT STUDENT ==========
    private void showEditStudentForm(PrintWriter out, HttpServletRequest request) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        StudentHome studentHome = getStudentHome();
        Student student = studentHome.findByPrimaryKey(id);
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Edit Student</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Edit Student</h1>");
        out.println("<div class='card'>");
        out.println("<form method='post' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='updateStudent'>");
        out.println("<input type='hidden' name='studentId' value='" + id + "'>");
        out.println("<div class='form-group'>");
        out.println("<label>Student ID:</label>");
        out.println("<input type='number' name='studentId' value='" + student.getStudentId() + "' readonly style='background: #f5f5f5;'>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Student Name:</label>");
        out.println("<input type='text' name='name' value='" + (student.getName() != null ? student.getName() : "") + "' required>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Email:</label>");
        out.println("<input type='email' name='email' value='" + (student.getEmail() != null ? student.getEmail() : "") + "' required>");
        out.println("</div>");
        out.println("<button type='submit' class='btn btn-success'>Update Student</button>");
        out.println("<a href='student-manager' class='btn btn-primary' style='margin-left: 10px;'>Cancel</a>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div></body></html>");
    }
    
    private void handleUpdateStudent(PrintWriter out, HttpServletRequest request) throws Exception {
        Long studentId = Long.parseLong(request.getParameter("studentId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        StudentHome studentHome = getStudentHome();
        Student student = studentHome.findByPrimaryKey(studentId);
        student.setName(name);
        student.setEmail(email);
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Success</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-success'>");
        out.println("<h2>Student Updated Successfully!</h2>");
        out.println("<p><strong>Student ID:</strong> " + studentId + "</p>");
        out.println("<p><strong>Name:</strong> " + name + "</p>");
        out.println("<p><strong>Email:</strong> " + email + "</p>");
        out.println("</div>");
        out.println("<a href='student-manager' class='btn btn-primary'>Back to Main</a>");
        out.println("</div></body></html>");
    }
    
    // ========== DELETE STUDENT ==========
    private void handleDeleteStudent(PrintWriter out, HttpServletRequest request) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        StudentHome studentHome = getStudentHome();
        Student student = studentHome.findByPrimaryKey(id);
        student.remove();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Deleted</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-success'>");
        out.println("<h2>Student Deleted Successfully!</h2>");
        out.println("<p>Student with ID <strong>" + id + "</strong> has been removed from the database.</p>");
        out.println("</div>");
        out.println("<a href='student-manager' class='btn btn-primary'>Back to Main</a>");
        out.println("</div></body></html>");
    }
    
    // ========== CREATE COURSE ==========
    private void showCreateCourseForm(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Create Course</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Create New Course</h1>");
        out.println("<div class='card'>");
        out.println("<form method='post' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='createCourse'>");
        out.println("<div class='form-group'>");
        out.println("<label>Course ID:</label>");
        out.println("<input type='number' name='courseId' required>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Course Name:</label>");
        out.println("<input type='text' name='courseName' required>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Course Code:</label>");
        out.println("<input type='text' name='courseCode' required>");
        out.println("</div>");
        out.println("<button type='submit' class='btn btn-success'>Create Course</button>");
        out.println("<a href='student-manager' class='btn btn-primary' style='margin-left: 10px;'>Cancel</a>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div></body></html>");
    }
    
    private void handleCreateCourse(PrintWriter out, HttpServletRequest request) throws Exception {
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        String courseName = request.getParameter("courseName");
        String courseCode = request.getParameter("courseCode");
        
        CourseHome courseHome = getCourseHome();
        courseHome.create(courseId, courseName, courseCode);
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Success</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-success'>");
        out.println("<h2>Course Created Successfully!</h2>");
        out.println("<p><strong>Course ID:</strong> " + courseId + "</p>");
        out.println("<p><strong>Course Name:</strong> " + courseName + "</p>");
        out.println("<p><strong>Course Code:</strong> " + courseCode + "</p>");
        out.println("</div>");
        out.println("<a href='student-manager' class='btn btn-primary'>Back to Main</a>");
        out.println("</div></body></html>");
    }
    
    // ========== EDIT COURSE ==========
    private void showEditCourseForm(PrintWriter out, HttpServletRequest request) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        CourseHome courseHome = getCourseHome();
        Course course = courseHome.findByPrimaryKey(id);
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Edit Course</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Edit Course</h1>");
        out.println("<div class='card'>");
        out.println("<form method='post' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='updateCourse'>");
        out.println("<input type='hidden' name='courseId' value='" + id + "'>");
        out.println("<div class='form-group'>");
        out.println("<label>Course ID:</label>");
        out.println("<input type='number' name='courseId' value='" + course.getCourseId() + "' readonly style='background: #f5f5f5;'>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Course Name:</label>");
        out.println("<input type='text' name='courseName' value='" + (course.getCourseName() != null ? course.getCourseName() : "") + "' required>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Course Code:</label>");
        out.println("<input type='text' name='courseCode' value='" + (course.getCourseCode() != null ? course.getCourseCode() : "") + "' required>");
        out.println("</div>");
        out.println("<button type='submit' class='btn btn-success'>Update Course</button>");
        out.println("<a href='student-manager' class='btn btn-primary' style='margin-left: 10px;'>Cancel</a>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div></body></html>");
    }
    
    private void handleUpdateCourse(PrintWriter out, HttpServletRequest request) throws Exception {
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        String courseName = request.getParameter("courseName");
        String courseCode = request.getParameter("courseCode");
        
        CourseHome courseHome = getCourseHome();
        Course course = courseHome.findByPrimaryKey(courseId);
        course.setCourseName(courseName);
        course.setCourseCode(courseCode);
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Success</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-success'>");
        out.println("<h2>Course Updated Successfully!</h2>");
        out.println("<p><strong>Course ID:</strong> " + courseId + "</p>");
        out.println("<p><strong>Course Name:</strong> " + courseName + "</p>");
        out.println("<p><strong>Course Code:</strong> " + courseCode + "</p>");
        out.println("</div>");
        out.println("<a href='student-manager' class='btn btn-primary'>Back to Main</a>");
        out.println("</div></body></html>");
    }
    
    // ========== DELETE COURSE ==========
    private void handleDeleteCourse(PrintWriter out, HttpServletRequest request) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        CourseHome courseHome = getCourseHome();
        Course course = courseHome.findByPrimaryKey(id);
        course.remove();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Deleted</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-success'>");
        out.println("<h2>Course Deleted Successfully!</h2>");
        out.println("<p>Course with ID <strong>" + id + "</strong> has been removed from the database.</p>");
        out.println("</div>");
        out.println("<a href='student-manager' class='btn btn-primary'>Back to Main</a>");
        out.println("</div></body></html>");
    }
    
    // ========== ADD COURSE TO STUDENT ==========
    private void showAddCourseForm(PrintWriter out, HttpServletRequest request) throws Exception {
        StudentHome studentHome = getStudentHome();
        CourseHome courseHome = getCourseHome();
        Collection<Student> students = studentHome.findAll();
        Collection<Course> courses = courseHome.findAll();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Add Course to Student</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Add Course to Student</h1>");
        out.println("<div class='card'>");
        out.println("<form method='post' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='addCourse'>");
        out.println("<div class='form-group'>");
        out.println("<label>Select Student:</label>");
        if (students != null && !students.isEmpty()) {
            out.println("<select name='studentId' required>");
            out.println("<option value=''>-- Select a Student --</option>");
            for (Student student : students) {
                out.println("<option value='" + student.getStudentId() + "'>");
                out.println("ID: " + student.getStudentId() + " - " + student.getName() + " (" + student.getEmail() + ")");
                out.println("</option>");
            }
            out.println("</select>");
        } else {
            out.println("<select disabled><option>No students available</option></select>");
            out.println("<div class='alert alert-info'>No students found. <a href='student-manager?action=createStudent'>Create one first</a></div>");
        }
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label>Select Course:</label>");
        if (courses != null && !courses.isEmpty()) {
            out.println("<select name='courseId' required>");
            out.println("<option value=''>-- Select a Course --</option>");
            for (Course course : courses) {
                out.println("<option value='" + course.getCourseId() + "'>");
                out.println("ID: " + course.getCourseId() + " - " + course.getCourseName() + " (" + course.getCourseCode() + ")");
                out.println("</option>");
            }
            out.println("</select>");
        } else {
            out.println("<select disabled><option>No courses available</option></select>");
            out.println("<div class='alert alert-info'>No courses found. <a href='student-manager?action=createCourse'>Create one first</a></div>");
        }
        out.println("</div>");
        if ((students != null && !students.isEmpty()) && (courses != null && !courses.isEmpty())) {
            out.println("<button type='submit' class='btn btn-success'>Add Course</button>");
        }
        out.println("<a href='student-manager' class='btn btn-primary' style='margin-left: 10px;'>Cancel</a>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div></body></html>");
    }
    
    private void handleAddCourse(PrintWriter out, HttpServletRequest request) throws Exception {
        Long studentId = Long.parseLong(request.getParameter("studentId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        
        StudentHome studentHome = getStudentHome();
        CourseHome courseHome = getCourseHome();
        Student student = studentHome.findByPrimaryKey(studentId);
        Course course = courseHome.findByPrimaryKey(courseId);
        
        student.addCourse(course);
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Success</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-success'>");
        out.println("<h2>Course Added Successfully!</h2>");
        out.println("<p>Course has been added to the student.</p>");
        out.println("<p><strong>Student ID:</strong> " + studentId + "</p>");
        out.println("<p><strong>Course ID:</strong> " + courseId + "</p>");
        out.println("</div>");
        out.println("<a href='student-manager' class='btn btn-primary'>Back to Main</a>");
        out.println("</div></body></html>");
    }
    
    // ========== VIEW STUDENT COURSES ==========
    private void showStudentCourses(PrintWriter out, HttpServletRequest request) throws Exception {
        String studentIdParam = request.getParameter("studentId");
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Student Courses</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Student Courses</h1>");
        out.println("<div class='card'>");
        out.println("<form method='get' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='viewCourses'>");
        out.println("<div class='form-group'>");
        out.println("<label>Student ID:</label>");
        out.println("<input type='number' name='studentId' value='" + (studentIdParam != null ? studentIdParam : "") + "' required>");
        out.println("</div>");
        out.println("<button type='submit' class='btn btn-info'>View Courses</button>");
        out.println("<a href='student-manager' class='btn btn-primary' style='margin-left: 10px;'>Back</a>");
        out.println("</form>");
        out.println("</div>");
        
        if (studentIdParam != null && !studentIdParam.trim().isEmpty()) {
            try {
                Long studentId = Long.parseLong(studentIdParam);
                StudentHome studentHome = getStudentHome();
                Student student = studentHome.findByPrimaryKey(studentId);
                
                out.println("<h3>Student: " + (student.getName() != null ? student.getName() : "N/A") + " (ID: " + student.getStudentId() + ")</h3>");
                
                Collection<Course> courses = null;
                try {
                    courses = student.getCourseList();
                } catch (IllegalStateException e) {
                    courses = null;
                }
                
                java.util.List<Course> courseList = new java.util.ArrayList<Course>();
                if (courses != null) {
                    try {
                        for (Course course : courses) {
                            if (course != null) {
                                courseList.add(course);
                            }
                        }
                    } catch (IllegalStateException e) {
                        courseList.clear();
                    }
                }
                
                if (!courseList.isEmpty()) {
                    out.println("<table>");
                    out.println("<tr><th>Course ID</th><th>Course Name</th><th>Course Code</th><th>Actions</th></tr>");
                    for (Course course : courseList) {
                        out.println("<tr>");
                        out.println("<td>" + course.getCourseId() + "</td>");
                        out.println("<td>" + course.getCourseName() + "</td>");
                        out.println("<td><code style='background: #f0f0f0; padding: 4px 8px; border-radius: 4px;'>" + course.getCourseCode() + "</code></td>");
                        out.println("<td><a href='student-manager?action=removeCourse&studentId=" + studentId + "&courseId=" + course.getCourseId() + "' class='btn btn-danger btn-sm' onclick='return confirm(\"Remove this course from student?\")'>❌ Remove</a></td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                } else {
                    out.println("<div class='empty-msg'>This student has not enrolled in any courses yet.</div>");
                }
            } catch (Exception e) {
                out.println("<div class='alert alert-error'>Error: " + e.getMessage() + "</div>");
            }
        }
        
        out.println("</div></body></html>");
    }
    
    // ========== REMOVE COURSE FROM STUDENT ==========
    private void handleRemoveCourse(PrintWriter out, HttpServletRequest request) throws Exception {
        Long studentId = Long.parseLong(request.getParameter("studentId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        
        StudentHome studentHome = getStudentHome();
        CourseHome courseHome = getCourseHome();
        Student student = studentHome.findByPrimaryKey(studentId);
        Course course = courseHome.findByPrimaryKey(courseId);
        
        Collection<Course> courses = student.getCourses();
        if (courses != null) {
            courses.remove(course);
            student.setCourses(courses);
        }
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Removed</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-success'>");
        out.println("<h2>Course Removed Successfully!</h2>");
        out.println("<p>Course has been removed from the student.</p>");
        out.println("</div>");
        out.println("<a href='student-manager?action=viewCourses&studentId=" + studentId + "' class='btn btn-primary'>Back to Courses</a>");
        out.println("</div></body></html>");
    }
    
    // ========== ERROR PAGE ==========
    private void showErrorPage(PrintWriter out, Exception e) {
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Error</title>");
        out.println(getCommonStyles());
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<div class='alert alert-error'>");
        out.println("<h2>Error Occurred</h2>");
        out.println("<p><strong>" + (e.getMessage() != null ? e.getMessage() : "Unknown error") + "</strong></p>");
        out.println("</div>");
        out.println("<a href='student-manager' class='btn btn-primary'>Back to Main</a>");
        out.println("</div></body></html>");
    }
}

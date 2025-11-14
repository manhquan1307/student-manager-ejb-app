package org.sample;

import org.sample.course.Course;
import org.sample.student.StudentManagerFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class StudentManagerServlet extends HttpServlet {
    
    private StudentManagerFacade getFacade() {
        try {
            javax.naming.InitialContext ctx = new javax.naming.InitialContext();
            // Try different JNDI names
            try {
                return (StudentManagerFacade) ctx.lookup("java:comp/env/ejb/StudentManagerFacade");
            } catch (Exception e1) {
                try {
                    return (StudentManagerFacade) ctx.lookup("java:module/StudentManagerFacade");
                } catch (Exception e2) {
                    try {
                        return (StudentManagerFacade) ctx.lookup("java:app/student-manager-ejbs/StudentManagerFacade");
                    } catch (Exception e3) {
                        return (StudentManagerFacade) ctx.lookup("java:global/student-manager-ear/student-manager-ejbs/StudentManagerFacade");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot get StudentManagerFacade: " + e.getMessage(), e);
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
                case "createCourse":
                    showCreateCourseForm(out);
                    break;
                case "addCourse":
                    showAddCourseForm(out, request);
                    break;
                case "viewCourses":
                    showStudentCourses(out, request);
                    break;
                case "list":
                default:
                    showMainPage(out);
                    break;
            }
        } catch (Exception e) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Error</title>");
            out.println("<style>body { font-family: Arial; margin: 20px; } .error { color: red; }</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2 class='error'>Error occurred:</h2>");
            out.println("<p><strong>" + e.getMessage() + "</strong></p>");
            out.println("<h3>Stack Trace:</h3>");
            out.println("<pre style='background: #f5f5f5; padding: 10px; border: 1px solid #ddd;'>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("<p><a href='student-manager'>Try Again</a></p>");
            out.println("</body></html>");
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
                Long studentId = Long.parseLong(request.getParameter("studentId"));
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                
                getFacade().createStudent(studentId, name, email);
                out.println("<html><body>");
                out.println("<h2>Student created successfully!</h2>");
                out.println("<p>Student ID: " + studentId + "</p>");
                out.println("<p>Name: " + name + "</p>");
                out.println("<p>Email: " + email + "</p>");
                out.println("<a href='student-manager'>Back to Main</a>");
                out.println("</body></html>");
                
            } else if ("createCourse".equals(action)) {
                Long courseId = Long.parseLong(request.getParameter("courseId"));
                String courseName = request.getParameter("courseName");
                String courseCode = request.getParameter("courseCode");
                
                getFacade().createCourse(courseId, courseName, courseCode);
                out.println("<html><body>");
                out.println("<h2>Course created successfully!</h2>");
                out.println("<p>Course ID: " + courseId + "</p>");
                out.println("<p>Course Name: " + courseName + "</p>");
                out.println("<p>Course Code: " + courseCode + "</p>");
                out.println("<a href='student-manager'>Back to Main</a>");
                out.println("</body></html>");
                
            } else if ("addCourse".equals(action)) {
                Long studentId = Long.parseLong(request.getParameter("studentId"));
                Long courseId = Long.parseLong(request.getParameter("courseId"));
                
                getFacade().addCourseToStudent(studentId, courseId);
                out.println("<html><body>");
                out.println("<h2>Course added to student successfully!</h2>");
                out.println("<p>Student ID: " + studentId + "</p>");
                out.println("<p>Course ID: " + courseId + "</p>");
                out.println("<a href='student-manager'>Back to Main</a>");
                out.println("</body></html>");
            }
        } catch (Exception e) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Error</title>");
            out.println("<style>body { font-family: Arial; margin: 20px; } .error { color: red; }</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2 class='error'>Error occurred:</h2>");
            out.println("<p><strong>" + e.getMessage() + "</strong></p>");
            out.println("<h3>Stack Trace:</h3>");
            out.println("<pre style='background: #f5f5f5; padding: 10px; border: 1px solid #ddd;'>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("<p><a href='student-manager'>Back to Main</a></p>");
            out.println("</body></html>");
        }
    }
    
    private void showMainPage(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Student Manager</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println(".container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println("h1 { color: #333; }");
        out.println(".menu { display: grid; grid-template-columns: repeat(2, 1fr); gap: 15px; margin-top: 20px; }");
        out.println(".menu-item { padding: 20px; background: #4CAF50; color: white; text-decoration: none; border-radius: 5px; text-align: center; }");
        out.println(".menu-item:hover { background: #45a049; }");
        out.println(".menu-item.secondary { background: #2196F3; }");
        out.println(".menu-item.secondary:hover { background: #0b7dda; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Student Manager System</h1>");
        out.println("<p>Manage Students and Courses</p>");
        out.println("<div class='menu'>");
        out.println("<a href='student-manager?action=createStudent' class='menu-item'>Create New Student</a>");
        out.println("<a href='student-manager?action=createCourse' class='menu-item'>Create New Course</a>");
        out.println("<a href='student-manager?action=addCourse' class='menu-item secondary'>Add Course to Student</a>");
        out.println("<a href='student-manager?action=viewCourses' class='menu-item secondary'>View Student Courses</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void showCreateStudentForm(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Create Student</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println(".container { max-width: 500px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println("h1 { color: #333; }");
        out.println("form { margin-top: 20px; }");
        out.println("label { display: block; margin-top: 10px; font-weight: bold; }");
        out.println("input[type='text'], input[type='number'] { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
        out.println("button { background: #4CAF50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; margin-top: 15px; }");
        out.println("button:hover { background: #45a049; }");
        out.println("a { display: inline-block; margin-top: 10px; color: #2196F3; text-decoration: none; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Create New Student</h1>");
        out.println("<form method='post' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='createStudent'>");
        out.println("<label>Student ID:</label>");
        out.println("<input type='number' name='studentId' required>");
        out.println("<label>Student Name:</label>");
        out.println("<input type='text' name='name' required>");
        out.println("<label>Email:</label>");
        out.println("<input type='text' name='email' required>");
        out.println("<button type='submit'>Create Student</button>");
        out.println("</form>");
        out.println("<a href='student-manager'>Back</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void showCreateCourseForm(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Create Course</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println(".container { max-width: 500px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println("h1 { color: #333; }");
        out.println("form { margin-top: 20px; }");
        out.println("label { display: block; margin-top: 10px; font-weight: bold; }");
        out.println("input[type='text'], input[type='number'] { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
        out.println("button { background: #4CAF50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; margin-top: 15px; }");
        out.println("button:hover { background: #45a049; }");
        out.println("a { display: inline-block; margin-top: 10px; color: #2196F3; text-decoration: none; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Create New Course</h1>");
        out.println("<form method='post' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='createCourse'>");
        out.println("<label>Course ID:</label>");
        out.println("<input type='number' name='courseId' required>");
        out.println("<label>Course Name:</label>");
        out.println("<input type='text' name='courseName' required>");
        out.println("<label>Course Code:</label>");
        out.println("<input type='text' name='courseCode' required>");
        out.println("<button type='submit'>Create Course</button>");
        out.println("</form>");
        out.println("<a href='student-manager'>Back</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void showAddCourseForm(PrintWriter out, HttpServletRequest request) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Add Course to Student</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println(".container { max-width: 500px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println("h1 { color: #333; }");
        out.println("form { margin-top: 20px; }");
        out.println("label { display: block; margin-top: 10px; font-weight: bold; }");
        out.println("input[type='number'] { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
        out.println("button { background: #4CAF50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; margin-top: 15px; }");
        out.println("button:hover { background: #45a049; }");
        out.println("a { display: inline-block; margin-top: 10px; color: #2196F3; text-decoration: none; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Add Course to Student</h1>");
        out.println("<form method='post' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='addCourse'>");
        out.println("<label>Student ID:</label>");
        out.println("<input type='number' name='studentId' required>");
        out.println("<label>Course ID:</label>");
        out.println("<input type='number' name='courseId' required>");
        out.println("<button type='submit'>Add Course</button>");
        out.println("</form>");
        out.println("<a href='student-manager'>Back</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void showStudentCourses(PrintWriter out, HttpServletRequest request) {
        String studentIdParam = request.getParameter("studentId");
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Student Courses</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println(".container { max-width: 700px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println("h1 { color: #333; }");
        out.println("form { margin: 20px 0; }");
        out.println("label { display: block; margin-top: 10px; font-weight: bold; }");
        out.println("input[type='number'] { width: 200px; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; }");
        out.println("button { background: #2196F3; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; margin-top: 10px; }");
        out.println("button:hover { background: #0b7dda; }");
        out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        out.println("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("tr:hover { background-color: #f5f5f5; }");
        out.println(".no-courses { color: #666; font-style: italic; margin-top: 20px; }");
        out.println("a { display: inline-block; margin-top: 10px; color: #2196F3; text-decoration: none; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Student Courses List</h1>");
        out.println("<form method='get' action='student-manager'>");
        out.println("<input type='hidden' name='action' value='viewCourses'>");
        out.println("<label>Student ID:</label>");
        out.println("<input type='number' name='studentId' value='" + 
                    (studentIdParam != null ? studentIdParam : "") + "' required>");
        out.println("<button type='submit'>View Courses</button>");
        out.println("</form>");
        
        if (studentIdParam != null) {
            try {
                Long studentId = Long.parseLong(studentIdParam);
                Collection<Course> courses = getFacade().getCourseList(studentId);
                
                if (courses != null && !courses.isEmpty()) {
                    out.println("<table>");
                    out.println("<tr><th>Course ID</th><th>Course Name</th><th>Course Code</th></tr>");
                    for (Course course : courses) {
                        out.println("<tr>");
                        out.println("<td>" + course.getCourseId() + "</td>");
                        out.println("<td>" + course.getCourseName() + "</td>");
                        out.println("<td>" + course.getCourseCode() + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                } else {
                    out.println("<p class='no-courses'>This student has not enrolled in any courses yet.</p>");
                }
            } catch (Exception e) {
                out.println("<p style='color: red;'>Error: " + e.getMessage() + "</p>");
            }
        }
        
        out.println("<a href='student-manager'>Back</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}

# Student Manager System

A comprehensive Enterprise JavaBeans (EJB) application for managing students and courses with full CRUD operations. This application demonstrates EJB 2.x Container Managed Persistence (CMP) with many-to-many relationships between students and courses.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Environment Setup](#environment-setup)
- [Building the Project](#building-the-project)
- [Deployment](#deployment)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

## Overview

The Student Manager System is an enterprise-level application built using Java EE technologies. It provides a web-based interface for managing students and courses, including:

- Creating, reading, updating, and deleting students
- Creating, reading, updating, and deleting courses
- Managing many-to-many relationships between students and courses
- Viewing enrolled courses for each student

## Architecture

The application follows a multi-tier architecture:

- **Presentation Layer**: JSP/Servlet-based web interface
- **Business Layer**: EJB 2.x Entity Beans with Container Managed Persistence (CMP)
- **Data Layer**: MySQL database with JDBC connectivity

The project is structured as a Maven multi-module application:

- `student-manager-ejbs`: Contains EJB entity beans (StudentBean, CourseBean)
- `student-manager-webapp`: Contains web servlets and JSP pages
- `student-manager-ear`: Enterprise Application Archive packaging

## Technologies

- **Java**: JDK 8
- **Application Server**: GlassFish 4.1.2
- **Database**: MySQL 5.7
- **Build Tool**: Maven 3.x
- **Containerization**: Docker and Docker Compose
- **Enterprise Java**: EJB 2.x (CMP), JNDI, JPA/JDO

## Prerequisites

Before setting up the application, ensure you have the following installed:

- Docker (version 20.10 or later)
- Docker Compose (version 1.29 or later)
- Maven (version 3.6 or later)
- Java JDK 8 (for local development)

## Project Structure

```
student-manager-ejb-app/
├── infrastructure/
│   ├── docker-compose.yml          # Docker Compose configuration
│   ├── Dockerfile.glassfish        # GlassFish container image
│   ├── init.sql                    # Database initialization script
│   ├── drivers/                    # MySQL JDBC driver
│   └── deployments/                # Auto-deployment directory
├── student-manager-ejbs/           # EJB module
│   └── src/main/
│       ├── java/org/sample/
│       │   ├── student/            # Student entity beans
│       │   └── course/             # Course entity beans
│       └── resources/META-INF/
│           ├── ejb-jar.xml        # EJB deployment descriptor
│           ├── glassfish-ejb-jar.xml
│           └── sun-cmp-mapping.xml # CMP mapping configuration
├── student-manager-webapp/         # Web application module
│   └── src/main/
│       ├── java/org/sample/
│       │   └── StudentManagerServlet.java
│       └── webapp/
│           └── WEB-INF/
│               └── web.xml
├── student-manager-ear/            # EAR packaging module
│   └── src/main/application/
│       └── application.xml
└── pom.xml                         # Parent POM

```

## Environment Setup

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd student-manager-ejb-app
```

### Step 2: Prepare MySQL JDBC Driver

Download the MySQL JDBC driver (mysql-connector-j-8.0.33.jar or compatible version) and place it in the `infrastructure/drivers/` directory:

```bash
mkdir -p infrastructure/drivers
# Download mysql-connector-j-8.0.33.jar and place it in infrastructure/drivers/
```

### Step 3: Verify Docker Setup

Ensure Docker and Docker Compose are running:

```bash
docker --version
docker-compose --version
```

## Building the Project

### Build with Maven

From the project root directory, execute:

```bash
mvn clean package
```

This command will:

1. Compile all modules (EJBs, Web App, EAR)
2. Package the EJB module into a JAR file
3. Package the web application into a WAR file
4. Package everything into an EAR file

The output EAR file will be located at:
```
student-manager-ear/target/student-manager-ear-0.0.1-SNAPSHOT.ear
```

### Build Output

After successful build, you should see:

- `student-manager-ejbs/target/student-manager-ejbs-0.0.1-SNAPSHOT.jar`
- `student-manager-webapp/target/student-manager-webapp-0.0.1-SNAPSHOT.war`
- `student-manager-ear/target/student-manager-ear-0.0.1-SNAPSHOT.ear`

## Deployment

### Method 1: Docker Compose (Recommended)

This method automatically sets up MySQL, GlassFish, and deploys the application.

#### Step 1: Copy EAR File to Deployments Directory

```bash
cp student-manager-ear/target/student-manager-ear-0.0.1-SNAPSHOT.ear infrastructure/deployments/
```

#### Step 2: Start Docker Containers

Navigate to the infrastructure directory and start the services:

```bash
cd infrastructure
docker-compose up -d
```

This command will:

1. Build the GlassFish Docker image
2. Start MySQL container
3. Start GlassFish container
4. Initialize the database with schema from `init.sql`
5. Configure JDBC connection pool
6. Auto-deploy the EAR application

#### Step 3: Monitor Deployment

Check the GlassFish logs to verify deployment:

```bash
docker-compose logs -f glassfish4
```

Wait for messages indicating successful deployment. You should see:
- "Application deployed successfully"
- "Application student-manager-ear-0.0.1-SNAPSHOT is now available"

#### Step 4: Verify Services

- **Application**: http://localhost:8080/student-manager-webapp-0.0.1-SNAPSHOT/student-manager
- **GlassFish Admin Console**: http://localhost:4848 (admin/admin123)
- **MySQL**: localhost:3306 (user: ejbuser, password: ejbpass, database: ejbdb)

### Method 2: Manual Deployment to GlassFish

If you have GlassFish installed locally:

#### Step 1: Start GlassFish

```bash
asadmin start-domain
```

#### Step 2: Configure MySQL Connection Pool

```bash
asadmin create-jdbc-connection-pool \
  --datasourceclassname=com.mysql.cj.jdbc.MysqlDataSource \
  --restype=javax.sql.DataSource \
  --property=user=ejbuser:password=ejbpass:databaseName=ejbdb:serverName=localhost:portNumber=3306:useSSL=false:allowPublicKeyRetrieval=true:serverTimezone=UTC \
  MySQLPool

asadmin create-jdbc-resource \
  --connectionpoolid MySQLPool \
  jdbc/ejbDS
```

#### Step 3: Deploy EAR File

```bash
asadmin deploy student-manager-ear/target/student-manager-ear-0.0.1-SNAPSHOT.ear
```

## Usage

### Accessing the Application

Once deployed, access the application at:

```
http://localhost:8080/student-manager-webapp-0.0.1-SNAPSHOT/student-manager
```

### Main Features

#### Student Management

- **Create Student**: Click "Create Student" button, fill in Student ID, Name, and Email
- **View Students**: All students are listed on the main page
- **Edit Student**: Click "Edit" button next to a student to modify name and email
- **Delete Student**: Click "Delete" button (with confirmation) to remove a student

#### Course Management

- **Create Course**: Click "Create Course" button, fill in Course ID, Name, and Code
- **View Courses**: All courses are listed on the main page
- **Edit Course**: Click "Edit" button next to a course to modify name and code
- **Delete Course**: Click "Delete" button (with confirmation) to remove a course

#### Student-Course Relationships

- **Add Course to Student**: Click "Add Course to Student", select student and course from dropdowns
- **View Student Courses**: Click "Courses" button next to a student or use "View Student Courses" menu
- **Remove Course from Student**: In the student courses view, click "Remove" button next to a course

### User Interface

The application features a modern, responsive web interface with:

- Gradient-based color scheme
- Card-based layouts
- Interactive tables with hover effects
- Form validation
- Success and error notifications
- Confirmation dialogs for destructive operations

## Database Schema

The application uses the following database schema:

### Student Table

```sql
CREATE TABLE Student (
    studentId BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255)
);
```

### Course Table

```sql
CREATE TABLE Course (
    courseId BIGINT NOT NULL PRIMARY KEY,
    courseName VARCHAR(255),
    courseCode VARCHAR(255)
);
```

### Student_Course Table (Many-to-Many Relationship)

```sql
CREATE TABLE Student_Course (
    studentId BIGINT NOT NULL,
    courseId BIGINT NOT NULL,
    PRIMARY KEY (studentId, courseId),
    FOREIGN KEY (studentId) REFERENCES Student(studentId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Course(courseId) ON DELETE CASCADE
);
```

The database is automatically initialized when MySQL container starts using the `infrastructure/init.sql` script.

## Configuration

### Database Configuration

Database settings are configured in `infrastructure/docker-compose.yml`:

- **Database Name**: ejbdb
- **Username**: ejbuser
- **Password**: ejbpass
- **Root Password**: root
- **Port**: 3306

### GlassFish Configuration

GlassFish settings:

- **HTTP Port**: 8080
- **Admin Port**: 4848
- **IIOP Port**: 3700
- **Admin Username**: admin
- **Admin Password**: admin123

### JDBC Resource

The JDBC resource is configured with JNDI name: `jdbc/ejbDS`

### EJB Configuration

EJB configuration files:

- `ejb-jar.xml`: Defines entity beans, CMP fields, and relationships
- `glassfish-ejb-jar.xml`: GlassFish-specific EJB configuration
- `sun-cmp-mapping.xml`: Maps EJB beans to database tables

## Troubleshooting

### Common Issues

#### Issue: Application not accessible

**Solution**: 
- Verify containers are running: `docker-compose ps`
- Check GlassFish logs: `docker-compose logs glassfish4`
- Ensure port 8080 is not in use by another application

#### Issue: Database connection errors

**Solution**:
- Verify MySQL container is running: `docker-compose ps mysql`
- Check MySQL logs: `docker-compose logs mysql`
- Ensure JDBC driver is in `infrastructure/drivers/` directory
- Verify connection pool configuration in GlassFish admin console

#### Issue: Table not found errors

**Solution**:
- Check if `init.sql` was executed: `docker-compose exec mysql mysql -u ejbuser -pejbpass ejbdb -e "SHOW TABLES;"`
- Verify `sun-cmp-mapping.xml` has correct table name mappings
- Check database initialization logs

#### Issue: EJB lookup failures

**Solution**:
- Verify JNDI names in `StudentManagerServlet.java`
- Check EJB deployment status in GlassFish admin console
- Review `glassfish-ejb-jar.xml` configuration

#### Issue: CMR collection access errors

**Solution**:
- This is a known issue with EJB CMP collections
- The application handles `IllegalStateException` gracefully
- Collections are copied to regular lists before iteration

### Viewing Logs

#### GlassFish Logs

```bash
docker-compose logs -f glassfish4
```

#### MySQL Logs

```bash
docker-compose logs -f mysql
```

#### All Services

```bash
docker-compose logs -f
```

### Restarting Services

To restart all services:

```bash
docker-compose restart
```

To restart a specific service:

```bash
docker-compose restart glassfish4
docker-compose restart mysql
```

### Stopping Services

To stop all services:

```bash
docker-compose down
```

To stop and remove volumes (this will delete database data):

```bash
docker-compose down -v
```

### Rebuilding Containers

If you need to rebuild the GlassFish image:

```bash
docker-compose build glassfish4
docker-compose up -d
```

### Accessing MySQL Database

To access MySQL directly:

```bash
docker-compose exec mysql mysql -u ejbuser -pejbpass ejbdb
```

### Accessing GlassFish Admin Console

1. Navigate to http://localhost:4848
2. Login with username: `admin`, password: `admin123`
3. Navigate to Applications to see deployed applications
4. Navigate to Resources > JDBC > Connection Pools to verify MySQL pool

## Development

### Project Dependencies

The project uses the following key dependencies:

- Java EE API (provided by GlassFish)
- MySQL JDBC Driver (mysql-connector-j-8.0.33.jar)
- Servlet API 2.5
- EJB API 3.1

### Code Structure

- **Entity Beans**: Abstract classes implementing `EntityBean` interface
- **Local Interfaces**: Business interfaces for entity beans
- **Home Interfaces**: Factory interfaces for creating and finding entities
- **Servlet**: Handles HTTP requests and renders HTML responses

### Building for Development

For development with hot-reload:

```bash
mvn clean compile
```

For packaging without tests:

```bash
mvn clean package -DskipTests
```

## License

This project is provided as-is for educational and demonstration purposes.

## Support

For issues and questions, please refer to the troubleshooting section or check the application logs.


-- Create database if not exists (already created by MYSQL_DATABASE env var)
USE ejbdb;

-- Table Student (CMP Entity Bean)
CREATE TABLE IF NOT EXISTS Student (
    studentId BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Course (CMP Entity Bean)
CREATE TABLE IF NOT EXISTS Course (
    courseId BIGINT NOT NULL PRIMARY KEY,
    courseName VARCHAR(255),
    courseCode VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table for Many-to-Many relationship (Student-Course)
-- This table is typically managed by CMP container
CREATE TABLE IF NOT EXISTS Student_Course (
    studentId BIGINT NOT NULL,
    courseId BIGINT NOT NULL,
    PRIMARY KEY (studentId, courseId),
    FOREIGN KEY (studentId) REFERENCES Student(studentId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Course(courseId) ON DELETE CASCADE,
    INDEX idx_student (studentId),
    INDEX idx_course (courseId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


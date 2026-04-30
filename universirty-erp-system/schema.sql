-- ============================================================
--  University ERP System - Database Schema
--  Database: MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS university_erp;
USE university_erp;

-- ─────────────────────────────────────────────
--  DEPARTMENTS
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS departments (
    dept_id      INT AUTO_INCREMENT PRIMARY KEY,
    dept_name    VARCHAR(100) NOT NULL UNIQUE,
    dept_code    VARCHAR(10)  NOT NULL UNIQUE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ─────────────────────────────────────────────
--  FACULTY
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS faculty (
    faculty_id      INT AUTO_INCREMENT PRIMARY KEY,
    faculty_code    VARCHAR(20)  NOT NULL UNIQUE,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    phone           VARCHAR(15),
    designation     VARCHAR(50),
    dept_id         INT,
    join_date       DATE,
    salary          DECIMAL(10,2),
    status          ENUM('Active','Inactive') DEFAULT 'Active',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL
);

-- ─────────────────────────────────────────────
--  STUDENTS
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS students (
    student_id      INT AUTO_INCREMENT PRIMARY KEY,
    student_code    VARCHAR(20)  NOT NULL UNIQUE,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    phone           VARCHAR(15),
    dob             DATE,
    gender          ENUM('Male','Female','Other'),
    address         TEXT,
    dept_id         INT,
    semester        INT DEFAULT 1,
    admission_date  DATE,
    status          ENUM('Active','Inactive','Graduated') DEFAULT 'Active',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL
);

-- ─────────────────────────────────────────────
--  COURSES
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS courses (
    course_id       INT AUTO_INCREMENT PRIMARY KEY,
    course_code     VARCHAR(20)  NOT NULL UNIQUE,
    course_name     VARCHAR(150) NOT NULL,
    credits         INT DEFAULT 3,
    dept_id         INT,
    faculty_id      INT,
    semester        INT,
    max_students    INT DEFAULT 60,
    status          ENUM('Active','Inactive') DEFAULT 'Active',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id)    REFERENCES departments(dept_id) ON DELETE SET NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE SET NULL
);

-- ─────────────────────────────────────────────
--  COURSE ENROLLMENTS
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id   INT AUTO_INCREMENT PRIMARY KEY,
    student_id      INT NOT NULL,
    course_id       INT NOT NULL,
    enroll_date     DATE DEFAULT (CURDATE()),
    grade           VARCHAR(5),
    UNIQUE KEY uq_enroll (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES courses(course_id)  ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  ATTENDANCE
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id   INT AUTO_INCREMENT PRIMARY KEY,
    student_id      INT NOT NULL,
    course_id       INT NOT NULL,
    att_date        DATE NOT NULL,
    status          ENUM('Present','Absent','Late') DEFAULT 'Present',
    UNIQUE KEY uq_att (student_id, course_id, att_date),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES courses(course_id)  ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  EXAMINATION RESULTS
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS exam_results (
    result_id       INT AUTO_INCREMENT PRIMARY KEY,
    student_id      INT NOT NULL,
    course_id       INT NOT NULL,
    exam_type       ENUM('Midterm','Final','Quiz','Assignment') DEFAULT 'Final',
    marks_obtained  DECIMAL(5,2),
    total_marks     DECIMAL(5,2) DEFAULT 100,
    exam_date       DATE,
    remarks         VARCHAR(200),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES courses(course_id)  ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  FEE MANAGEMENT
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS fee_structure (
    fee_id          INT AUTO_INCREMENT PRIMARY KEY,
    dept_id         INT,
    semester        INT,
    fee_type        VARCHAR(50) NOT NULL,
    amount          DECIMAL(10,2) NOT NULL,
    academic_year   VARCHAR(10),
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS fee_payments (
    payment_id      INT AUTO_INCREMENT PRIMARY KEY,
    student_id      INT NOT NULL,
    fee_id          INT NOT NULL,
    amount_paid     DECIMAL(10,2) NOT NULL,
    payment_date    DATE DEFAULT (CURDATE()),
    payment_mode    ENUM('Cash','Online','Cheque','DD') DEFAULT 'Cash',
    receipt_no      VARCHAR(30) UNIQUE,
    status          ENUM('Paid','Partial','Pending') DEFAULT 'Pending',
    remarks         VARCHAR(200),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (fee_id)     REFERENCES fee_structure(fee_id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────
--  SEED DATA
-- ─────────────────────────────────────────────
INSERT IGNORE INTO departments (dept_name, dept_code) VALUES
('Computer Science',        'CS'),
('Electronics Engineering', 'EC'),
('Mechanical Engineering',  'ME'),
('Business Administration', 'BA'),
('Mathematics',             'MA');

INSERT IGNORE INTO faculty (faculty_code,first_name,last_name,email,phone,designation,dept_id,join_date,salary) VALUES
('FAC001','Anjali','Sharma','anjali.sharma@uni.edu','9876543210','Professor',1,'2015-07-01',85000),
('FAC002','Rajesh','Kumar','rajesh.kumar@uni.edu','9876543211','Associate Professor',1,'2018-01-15',72000),
('FAC003','Priya','Nair','priya.nair@uni.edu','9876543212','Assistant Professor',2,'2020-06-01',60000);

INSERT IGNORE INTO students (student_code,first_name,last_name,email,phone,dob,gender,dept_id,semester,admission_date) VALUES
('STU001','Amit','Verma','amit.verma@student.uni.edu','9123456789','2002-03-15','Male',1,3,'2022-08-01'),
('STU002','Sneha','Patel','sneha.patel@student.uni.edu','9123456790','2003-07-22','Female',1,1,'2023-08-01'),
('STU003','Rahul','Singh','rahul.singh@student.uni.edu','9123456791','2002-11-05','Male',2,3,'2022-08-01');

INSERT IGNORE INTO courses (course_code,course_name,credits,dept_id,faculty_id,semester) VALUES
('CS301','Data Structures & Algorithms',4,1,1,3),
('CS302','Database Management Systems',3,1,2,3),
('EC201','Digital Electronics',3,2,3,3),
('CS101','Introduction to Programming',3,1,2,1);

INSERT IGNORE INTO fee_structure (dept_id,semester,fee_type,amount,academic_year) VALUES
(1,3,'Tuition Fee',45000,'2024-25'),
(1,3,'Lab Fee',5000,'2024-25'),
(1,1,'Tuition Fee',45000,'2024-25'),
(2,3,'Tuition Fee',42000,'2024-25');

# University ERP System
**Java · MySQL · JDBC | Console-Based Menu-Driven Application**

---

## Project Structure

```
UniversityERP/
├── sql/
│   └── schema.sql              ← Database schema + seed data
├── lib/
│   └── mysql-connector-j-8.x.x.jar  ← (download separately)
└── src/
    ├── Main.java               ← Application entry point
    ├── util/
    │   ├── DBConnection.java   ← Singleton JDBC connection manager
    │   └── ConsoleUtil.java    ← Shared I/O helpers
    ├── model/
    │   ├── Student.java
    │   ├── Faculty.java
    │   └── Course.java
    ├── dao/
    │   ├── StudentDAO.java
    │   ├── FacultyDAO.java
    │   ├── CourseDAO.java
    │   ├── AttendanceDAO.java
    │   ├── ExamResultDAO.java
    │   └── FeeDAO.java
    └── menu/
        ├── StudentMenu.java
        ├── FacultyMenu.java
        ├── CourseMenu.java
        ├── AttendanceMenu.java
        ├── ExamMenu.java
        └── FeeMenu.java
```

---

## Prerequisites

| Requirement | Version |
|-------------|---------|
| Java JDK    | 17 or higher |
| MySQL       | 8.0 or higher |
| MySQL JDBC Driver | [mysql-connector-j-8.x.x.jar](https://dev.mysql.com/downloads/connector/j/) |

---

## Setup Steps

### Step 1 – Database

```bash
# Log in to MySQL and run the schema
mysql -u root -p < sql/schema.sql
```

This creates the `university_erp` database with all tables and seed data.

### Step 2 – JDBC Driver

Download **mysql-connector-j-8.x.x.jar** from  
https://dev.mysql.com/downloads/connector/j/  
and place it in the `lib/` folder.

### Step 3 – Configure DB Credentials

Open `src/util/DBConnection.java` and update:

```java
private static final String DB_URL  = "jdbc:mysql://localhost:3306/university_erp?...";
private static final String USER     = "root";
private static final String PASSWORD = "your_password";   // ← change this
```

### Step 4 – Compile

**Windows:**
```bat
javac -cp ".;lib/mysql-connector-j-8.x.x.jar" -d out src/Main.java src/util/*.java src/model/*.java src/dao/*.java src/menu/*.java
```

**Linux / macOS:**
```bash
javac -cp ".:lib/mysql-connector-j-8.x.x.jar" -d out src/Main.java src/util/*.java src/model/*.java src/dao/*.java src/menu/*.java
```

### Step 5 – Run

**Windows:**
```bat
java -cp "out;lib/mysql-connector-j-8.x.x.jar" Main
```

**Linux / macOS:**
```bash
java -cp "out:lib/mysql-connector-j-8.x.x.jar" Main
```

---

## Database Tables

| Table | Purpose |
|-------|---------|
| `departments` | University departments |
| `faculty` | Faculty members with designation & salary |
| `students` | Student profiles linked to departments |
| `courses` | Course catalogue with faculty & dept assignment |
| `enrollments` | Student–course enrollment (many-to-many) |
| `attendance` | Daily attendance per student per course |
| `exam_results` | Midterm / Final / Quiz / Assignment marks |
| `fee_structure` | Fee templates per dept/semester |
| `fee_payments` | Actual payment records with status |

---

## Features by Module

### 1. Student Management
- Add / View / Search / Update / Delete students
- Tracks: code, name, email, phone, DOB, gender, address, dept, semester, status

### 2. Faculty Management
- Full CRUD for faculty
- Tracks: code, designation, department, join date, salary, status

### 3. Course Management
- Full CRUD for courses
- Enroll students into courses
- View list of enrolled students per course

### 4. Attendance Management
- Mark attendance (Present / Absent / Late) per student per course per date
- View all attendance records for a course
- Student-wise attendance percentage summary

### 5. Examination Results
- Record marks for Midterm, Final, Quiz, Assignment
- View all results for a student (with % calculation)
- View ranked results for a course with auto-grading (A+/A/B/C/D/F)

### 6. Fee Management
- Define fee structures per dept/semester (Tuition, Lab, etc.)
- Record payments with mode (Cash/Online/Cheque/DD) and receipt number
- Automatic Paid / Partial / Pending status
- Student fee history report
- University-wide pending fees report

---

## Grading Scale (auto-calculated in Exam Results)

| Percentage | Grade |
|-----------|-------|
| ≥ 90% | A+ |
| ≥ 80% | A  |
| ≥ 70% | B  |
| ≥ 60% | C  |
| ≥ 50% | D  |
| < 50% | F  |

---

## Extending the System

- **IDE support**: Import the `src/` folder as a Java project in IntelliJ IDEA or Eclipse and add `lib/mysql-connector-j.jar` to the build path.
- **Add Department menu**: A `DepartmentDAO` + `DepartmentMenu` following the same pattern.
- **Reports**: Add a `ReportsDAO` with complex JOIN queries for consolidated dashboards.
- **Authentication**: Add a `users` table and a login screen before the main menu.

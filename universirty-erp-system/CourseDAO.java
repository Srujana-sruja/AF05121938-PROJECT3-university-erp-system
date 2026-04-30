package dao;

import model.Course;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    /* ── CREATE ─────────────────────────────────────────── */

    public boolean addCourse(Course c) {
        String sql = """
                INSERT INTO courses
                  (course_code, course_name, credits, dept_id,
                   faculty_id, semester, max_students, status)
                VALUES (?,?,?,?,?,?,?,?)
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getCourseCode());
            ps.setString(2, c.getCourseName());
            ps.setInt(3,    c.getCredits());
            ps.setInt(4,    c.getDeptId());
            ps.setInt(5,    c.getFacultyId());
            ps.setInt(6,    c.getSemester());
            ps.setInt(7,    c.getMaxStudents());
            ps.setString(8, c.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addCourse error: " + e.getMessage());
            return false;
        }
    }

    /* ── READ ALL ────────────────────────────────────────── */

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = """
                SELECT c.*, d.dept_name,
                       CONCAT(f.first_name,' ',f.last_name) AS faculty_name
                FROM courses c
                LEFT JOIN departments d ON c.dept_id   = d.dept_id
                LEFT JOIN faculty     f ON c.faculty_id = f.faculty_id
                ORDER BY c.course_id
                """;
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getAllCourses error: " + e.getMessage());
        }
        return list;
    }

    /* ── READ BY ID ──────────────────────────────────────── */

    public Course getCourseById(int id) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("getCourseById error: " + e.getMessage());
        }
        return null;
    }

    /* ── ENROLL STUDENT ──────────────────────────────────── */

    public boolean enrollStudent(int studentId, int courseId) {
        String sql = "INSERT IGNORE INTO enrollments (student_id, course_id, enroll_date) VALUES (?,?,CURDATE())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("enrollStudent error: " + e.getMessage());
            return false;
        }
    }

    /* ── ENROLLED STUDENTS PER COURSE ────────────────────── */

    public void printEnrollmentsForCourse(int courseId) {
        String sql = """
                SELECT s.student_code,
                       CONCAT(s.first_name,' ',s.last_name) AS name,
                       e.enroll_date, e.grade
                FROM enrollments e
                JOIN students s ON e.student_id = s.student_id
                WHERE e.course_id = ?
                ORDER BY s.first_name
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            System.out.printf("  %-12s %-28s %-14s %-6s%n",
                    "Code", "Name", "Enrolled On", "Grade");
            System.out.println("  " + "─".repeat(62));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("  %-12s %-28s %-14s %-6s%n",
                        rs.getString("student_code"),
                        rs.getString("name"),
                        rs.getString("enroll_date"),
                        rs.getString("grade") != null ? rs.getString("grade") : "-");
            }
            if (count == 0) System.out.println("  No students enrolled.");

        } catch (SQLException e) {
            System.err.println("printEnrollments error: " + e.getMessage());
        }
    }

    /* ── UPDATE ──────────────────────────────────────────── */

    public boolean updateCourse(Course c) {
        String sql = """
                UPDATE courses
                SET course_name=?, credits=?, dept_id=?,
                    faculty_id=?, semester=?, max_students=?, status=?
                WHERE course_id=?
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getCourseName());
            ps.setInt(2,    c.getCredits());
            ps.setInt(3,    c.getDeptId());
            ps.setInt(4,    c.getFacultyId());
            ps.setInt(5,    c.getSemester());
            ps.setInt(6,    c.getMaxStudents());
            ps.setString(7, c.getStatus());
            ps.setInt(8,    c.getCourseId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("updateCourse error: " + e.getMessage());
            return false;
        }
    }

    /* ── DELETE ──────────────────────────────────────────── */

    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("deleteCourse error: " + e.getMessage());
            return false;
        }
    }

    /* ── private helper ──────────────────────────────────── */

    private Course mapRow(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseId(rs.getInt("course_id"));
        c.setCourseCode(rs.getString("course_code"));
        c.setCourseName(rs.getString("course_name"));
        c.setCredits(rs.getInt("credits"));
        c.setDeptId(rs.getInt("dept_id"));
        c.setFacultyId(rs.getInt("faculty_id"));
        c.setSemester(rs.getInt("semester"));
        c.setMaxStudents(rs.getInt("max_students"));
        c.setStatus(rs.getString("status"));
        return c;
    }
}

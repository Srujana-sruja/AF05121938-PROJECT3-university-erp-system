package dao;

import model.Student;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    /* ── CREATE ─────────────────────────────────────────── */

    public boolean addStudent(Student s) {
        String sql = """
                INSERT INTO students
                  (student_code, first_name, last_name, email, phone, dob,
                   gender, address, dept_id, semester, admission_date, status)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,  s.getStudentCode());
            ps.setString(2,  s.getFirstName());
            ps.setString(3,  s.getLastName());
            ps.setString(4,  s.getEmail());
            ps.setString(5,  s.getPhone());
            ps.setString(6,  s.getDob());
            ps.setString(7,  s.getGender());
            ps.setString(8,  s.getAddress());
            ps.setInt(9,     s.getDeptId());
            ps.setInt(10,    s.getSemester());
            ps.setString(11, s.getAdmissionDate());
            ps.setString(12, s.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addStudent error: " + e.getMessage());
            return false;
        }
    }

    /* ── READ ALL ────────────────────────────────────────── */

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = """
                SELECT s.*, d.dept_name
                FROM students s
                LEFT JOIN departments d ON s.dept_id = d.dept_id
                ORDER BY s.student_id
                """;
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getAllStudents error: " + e.getMessage());
        }
        return list;
    }

    /* ── READ BY ID ──────────────────────────────────────── */

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("getStudentById error: " + e.getMessage());
        }
        return null;
    }

    /* ── SEARCH ──────────────────────────────────────────── */

    public List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = """
                SELECT * FROM students
                WHERE first_name LIKE ? OR last_name LIKE ?
                   OR email LIKE ? OR student_code LIKE ?
                ORDER BY first_name
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw);
            ps.setString(3, kw); ps.setString(4, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("searchStudents error: " + e.getMessage());
        }
        return list;
    }

    /* ── UPDATE ──────────────────────────────────────────── */

    public boolean updateStudent(Student s) {
        String sql = """
                UPDATE students
                SET first_name=?, last_name=?, email=?, phone=?,
                    dob=?, gender=?, address=?, dept_id=?,
                    semester=?, status=?
                WHERE student_id=?
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,  s.getFirstName());
            ps.setString(2,  s.getLastName());
            ps.setString(3,  s.getEmail());
            ps.setString(4,  s.getPhone());
            ps.setString(5,  s.getDob());
            ps.setString(6,  s.getGender());
            ps.setString(7,  s.getAddress());
            ps.setInt(8,     s.getDeptId());
            ps.setInt(9,     s.getSemester());
            ps.setString(10, s.getStatus());
            ps.setInt(11,    s.getStudentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("updateStudent error: " + e.getMessage());
            return false;
        }
    }

    /* ── DELETE ──────────────────────────────────────────── */

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("deleteStudent error: " + e.getMessage());
            return false;
        }
    }

    /* ── private helper ──────────────────────────────────── */

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setStudentCode(rs.getString("student_code"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setDob(rs.getString("dob"));
        s.setGender(rs.getString("gender"));
        s.setAddress(rs.getString("address"));
        s.setDeptId(rs.getInt("dept_id"));
        s.setSemester(rs.getInt("semester"));
        s.setAdmissionDate(rs.getString("admission_date"));
        s.setStatus(rs.getString("status"));
        return s;
    }
}

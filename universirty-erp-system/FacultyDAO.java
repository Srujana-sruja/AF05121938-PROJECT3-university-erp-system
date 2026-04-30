package dao;

import model.Faculty;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDAO {

    /* ── CREATE ─────────────────────────────────────────── */

    public boolean addFaculty(Faculty f) {
        String sql = """
                INSERT INTO faculty
                  (faculty_code, first_name, last_name, email, phone,
                   designation, dept_id, join_date, salary, status)
                VALUES (?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,  f.getFacultyCode());
            ps.setString(2,  f.getFirstName());
            ps.setString(3,  f.getLastName());
            ps.setString(4,  f.getEmail());
            ps.setString(5,  f.getPhone());
            ps.setString(6,  f.getDesignation());
            ps.setInt(7,     f.getDeptId());
            ps.setString(8,  f.getJoinDate());
            ps.setDouble(9,  f.getSalary());
            ps.setString(10, f.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addFaculty error: " + e.getMessage());
            return false;
        }
    }

    /* ── READ ALL ────────────────────────────────────────── */

    public List<Faculty> getAllFaculty() {
        List<Faculty> list = new ArrayList<>();
        String sql = """
                SELECT f.*, d.dept_name
                FROM faculty f
                LEFT JOIN departments d ON f.dept_id = d.dept_id
                ORDER BY f.faculty_id
                """;
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getAllFaculty error: " + e.getMessage());
        }
        return list;
    }

    /* ── READ BY ID ──────────────────────────────────────── */

    public Faculty getFacultyById(int id) {
        String sql = "SELECT * FROM faculty WHERE faculty_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("getFacultyById error: " + e.getMessage());
        }
        return null;
    }

    /* ── UPDATE ──────────────────────────────────────────── */

    public boolean updateFaculty(Faculty f) {
        String sql = """
                UPDATE faculty
                SET first_name=?, last_name=?, email=?, phone=?,
                    designation=?, dept_id=?, salary=?, status=?
                WHERE faculty_id=?
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, f.getFirstName());
            ps.setString(2, f.getLastName());
            ps.setString(3, f.getEmail());
            ps.setString(4, f.getPhone());
            ps.setString(5, f.getDesignation());
            ps.setInt(6,    f.getDeptId());
            ps.setDouble(7, f.getSalary());
            ps.setString(8, f.getStatus());
            ps.setInt(9,    f.getFacultyId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("updateFaculty error: " + e.getMessage());
            return false;
        }
    }

    /* ── DELETE ──────────────────────────────────────────── */

    public boolean deleteFaculty(int id) {
        String sql = "DELETE FROM faculty WHERE faculty_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("deleteFaculty error: " + e.getMessage());
            return false;
        }
    }

    /* ── private helper ──────────────────────────────────── */

    private Faculty mapRow(ResultSet rs) throws SQLException {
        Faculty f = new Faculty();
        f.setFacultyId(rs.getInt("faculty_id"));
        f.setFacultyCode(rs.getString("faculty_code"));
        f.setFirstName(rs.getString("first_name"));
        f.setLastName(rs.getString("last_name"));
        f.setEmail(rs.getString("email"));
        f.setPhone(rs.getString("phone"));
        f.setDesignation(rs.getString("designation"));
        f.setDeptId(rs.getInt("dept_id"));
        f.setJoinDate(rs.getString("join_date"));
        f.setSalary(rs.getDouble("salary"));
        f.setStatus(rs.getString("status"));
        return f;
    }
}

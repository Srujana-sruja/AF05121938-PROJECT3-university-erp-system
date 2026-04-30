package dao;

import util.DBConnection;

import java.sql.*;

public class AttendanceDAO {

    /* ── MARK ATTENDANCE ─────────────────────────────────── */

    public boolean markAttendance(int studentId, int courseId,
                                  String date, String status) {
        String sql = """
                INSERT INTO attendance (student_id, course_id, att_date, status)
                VALUES (?,?,?,?)
                ON DUPLICATE KEY UPDATE status = VALUES(status)
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1,    studentId);
            ps.setInt(2,    courseId);
            ps.setString(3, date);
            ps.setString(4, status);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("markAttendance error: " + e.getMessage());
            return false;
        }
    }

    /* ── VIEW ATTENDANCE FOR A COURSE ────────────────────── */

    public void printAttendanceByCourse(int courseId) {
        String sql = """
                SELECT s.student_code,
                       CONCAT(s.first_name,' ',s.last_name) AS name,
                       a.att_date, a.status
                FROM attendance a
                JOIN students s ON a.student_id = s.student_id
                WHERE a.course_id = ?
                ORDER BY a.att_date DESC, s.first_name
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            System.out.printf("  %-12s %-26s %-14s %-8s%n",
                    "Code", "Name", "Date", "Status");
            System.out.println("  " + "─".repeat(62));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("  %-12s %-26s %-14s %-8s%n",
                        rs.getString("student_code"),
                        rs.getString("name"),
                        rs.getString("att_date"),
                        rs.getString("status"));
            }
            if (count == 0) System.out.println("  No attendance records found.");

        } catch (SQLException e) {
            System.err.println("printAttendanceByCourse error: " + e.getMessage());
        }
    }

    /* ── ATTENDANCE SUMMARY FOR A STUDENT ────────────────── */

    public void printAttendanceSummary(int studentId) {
        String sql = """
                SELECT c.course_code, c.course_name,
                       COUNT(*) AS total,
                       SUM(CASE WHEN a.status='Present' THEN 1 ELSE 0 END) AS present,
                       ROUND(100.0 * SUM(CASE WHEN a.status='Present' THEN 1 ELSE 0 END) / COUNT(*), 1) AS pct
                FROM attendance a
                JOIN courses c ON a.course_id = c.course_id
                WHERE a.student_id = ?
                GROUP BY c.course_id
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            System.out.printf("  %-10s %-30s %6s %8s %8s%n",
                    "Code", "Course", "Total", "Present", "%");
            System.out.println("  " + "─".repeat(66));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("  %-10s %-30s %6d %8d %7.1f%%%n",
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getInt("total"),
                        rs.getInt("present"),
                        rs.getDouble("pct"));
            }
            if (count == 0) System.out.println("  No attendance records.");

        } catch (SQLException e) {
            System.err.println("printAttendanceSummary error: " + e.getMessage());
        }
    }
}

package dao;

import util.DBConnection;

import java.sql.*;

public class ExamResultDAO {

    /* ── ADD RESULT ──────────────────────────────────────── */

    public boolean addResult(int studentId, int courseId, String examType,
                             double marksObtained, double totalMarks,
                             String examDate, String remarks) {
        String sql = """
                INSERT INTO exam_results
                  (student_id, course_id, exam_type, marks_obtained,
                   total_marks, exam_date, remarks)
                VALUES (?,?,?,?,?,?,?)
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1,    studentId);
            ps.setInt(2,    courseId);
            ps.setString(3, examType);
            ps.setDouble(4, marksObtained);
            ps.setDouble(5, totalMarks);
            ps.setString(6, examDate);
            ps.setString(7, remarks);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addResult error: " + e.getMessage());
            return false;
        }
    }

    /* ── VIEW RESULTS BY STUDENT ─────────────────────────── */

    public void printResultsByStudent(int studentId) {
        String sql = """
                SELECT c.course_code, c.course_name,
                       r.exam_type, r.marks_obtained, r.total_marks,
                       ROUND(100.0 * r.marks_obtained / r.total_marks, 1) AS pct,
                       r.exam_date, r.remarks
                FROM exam_results r
                JOIN courses c ON r.course_id = c.course_id
                WHERE r.student_id = ?
                ORDER BY r.exam_date DESC
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            System.out.printf("  %-10s %-22s %-12s %8s %8s %7s %-12s%n",
                    "Code", "Course", "Exam Type", "Marks", "Total", "%", "Date");
            System.out.println("  " + "─".repeat(82));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("  %-10s %-22s %-12s %8.1f %8.1f %6.1f%% %-12s%n",
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("exam_type"),
                        rs.getDouble("marks_obtained"),
                        rs.getDouble("total_marks"),
                        rs.getDouble("pct"),
                        rs.getString("exam_date"));
            }
            if (count == 0) System.out.println("  No exam results found.");

        } catch (SQLException e) {
            System.err.println("printResultsByStudent error: " + e.getMessage());
        }
    }

    /* ── VIEW RESULTS BY COURSE ──────────────────────────── */

    public void printResultsByCourse(int courseId, String examType) {
        String sql = """
                SELECT s.student_code,
                       CONCAT(s.first_name,' ',s.last_name) AS name,
                       r.marks_obtained, r.total_marks,
                       ROUND(100.0 * r.marks_obtained / r.total_marks, 1) AS pct,
                       CASE
                         WHEN r.marks_obtained / r.total_marks >= 0.90 THEN 'A+'
                         WHEN r.marks_obtained / r.total_marks >= 0.80 THEN 'A'
                         WHEN r.marks_obtained / r.total_marks >= 0.70 THEN 'B'
                         WHEN r.marks_obtained / r.total_marks >= 0.60 THEN 'C'
                         WHEN r.marks_obtained / r.total_marks >= 0.50 THEN 'D'
                         ELSE 'F'
                       END AS grade
                FROM exam_results r
                JOIN students s ON r.student_id = s.student_id
                WHERE r.course_id = ? AND r.exam_type = ?
                ORDER BY r.marks_obtained DESC
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1,    courseId);
            ps.setString(2, examType);
            ResultSet rs = ps.executeQuery();
            System.out.printf("  %-12s %-26s %8s %8s %8s %6s%n",
                    "Code", "Name", "Marks", "Total", "%", "Grade");
            System.out.println("  " + "─".repeat(70));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("  %-12s %-26s %8.1f %8.1f %7.1f%% %6s%n",
                        rs.getString("student_code"),
                        rs.getString("name"),
                        rs.getDouble("marks_obtained"),
                        rs.getDouble("total_marks"),
                        rs.getDouble("pct"),
                        rs.getString("grade"));
            }
            if (count == 0) System.out.println("  No results for this exam type.");

        } catch (SQLException e) {
            System.err.println("printResultsByCourse error: " + e.getMessage());
        }
    }
}

package dao;

import util.DBConnection;

import java.sql.*;

public class FeeDAO {

    /* ── ADD FEE STRUCTURE ───────────────────────────────── */

    public boolean addFeeStructure(int deptId, int semester,
                                   String feeType, double amount,
                                   String academicYear) {
        String sql = """
                INSERT INTO fee_structure
                  (dept_id, semester, fee_type, amount, academic_year)
                VALUES (?,?,?,?,?)
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1,    deptId);
            ps.setInt(2,    semester);
            ps.setString(3, feeType);
            ps.setDouble(4, amount);
            ps.setString(5, academicYear);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addFeeStructure error: " + e.getMessage());
            return false;
        }
    }

    /* ── VIEW FEE STRUCTURES ─────────────────────────────── */

    public void printFeeStructures() {
        String sql = """
                SELECT fs.fee_id, d.dept_name, fs.semester,
                       fs.fee_type, fs.amount, fs.academic_year
                FROM fee_structure fs
                LEFT JOIN departments d ON fs.dept_id = d.dept_id
                ORDER BY d.dept_name, fs.semester
                """;
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("  %-4s %-22s %4s %-18s %12s %-10s%n",
                    "ID", "Department", "Sem", "Fee Type", "Amount (₹)", "Acad.Year");
            System.out.println("  " + "─".repeat(74));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("  %-4d %-22s %4d %-18s %,12.2f %-10s%n",
                        rs.getInt("fee_id"),
                        rs.getString("dept_name"),
                        rs.getInt("semester"),
                        rs.getString("fee_type"),
                        rs.getDouble("amount"),
                        rs.getString("academic_year"));
            }
            if (count == 0) System.out.println("  No fee structures defined.");

        } catch (SQLException e) {
            System.err.println("printFeeStructures error: " + e.getMessage());
        }
    }

    /* ── RECORD PAYMENT ──────────────────────────────────── */

    public boolean recordPayment(int studentId, int feeId,
                                 double amountPaid, String paymentMode,
                                 String receiptNo, String remarks) {
        // Determine status
        String statusSql = "SELECT amount FROM fee_structure WHERE fee_id = ?";
        String status = "Pending";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(statusSql)) {

            ps.setInt(1, feeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double required = rs.getDouble("amount");
                status = amountPaid >= required ? "Paid" : "Partial";
            }
        } catch (SQLException e) {
            System.err.println("recordPayment lookup error: " + e.getMessage());
            return false;
        }

        String sql = """
                INSERT INTO fee_payments
                  (student_id, fee_id, amount_paid, payment_date,
                   payment_mode, receipt_no, status, remarks)
                VALUES (?,?,?,CURDATE(),?,?,?,?)
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1,    studentId);
            ps.setInt(2,    feeId);
            ps.setDouble(3, amountPaid);
            ps.setString(4, paymentMode);
            ps.setString(5, receiptNo);
            ps.setString(6, status);
            ps.setString(7, remarks);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("recordPayment error: " + e.getMessage());
            return false;
        }
    }

    /* ── FEE STATUS FOR A STUDENT ────────────────────────── */

    public void printFeeStatus(int studentId) {
        String sql = """
                SELECT fp.payment_id, fs.fee_type, fs.amount AS required,
                       fp.amount_paid, fp.payment_date,
                       fp.payment_mode, fp.receipt_no, fp.status
                FROM fee_payments fp
                JOIN fee_structure fs ON fp.fee_id = fs.fee_id
                WHERE fp.student_id = ?
                ORDER BY fp.payment_date DESC
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            System.out.printf("  %-4s %-18s %12s %12s %-12s %-8s %-14s %-8s%n",
                    "ID", "Fee Type", "Required ₹", "Paid ₹",
                    "Date", "Mode", "Receipt", "Status");
            System.out.println("  " + "─".repeat(92));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("  %-4d %-18s %,12.2f %,12.2f %-12s %-8s %-14s %-8s%n",
                        rs.getInt("payment_id"),
                        rs.getString("fee_type"),
                        rs.getDouble("required"),
                        rs.getDouble("amount_paid"),
                        rs.getString("payment_date"),
                        rs.getString("payment_mode"),
                        rs.getString("receipt_no"),
                        rs.getString("status"));
            }
            if (count == 0) System.out.println("  No payment records found.");

        } catch (SQLException e) {
            System.err.println("printFeeStatus error: " + e.getMessage());
        }
    }

    /* ── PENDING FEES REPORT ─────────────────────────────── */

    public void printPendingFees() {
        String sql = """
                SELECT s.student_code,
                       CONCAT(s.first_name,' ',s.last_name) AS name,
                       fp.receipt_no, fs.fee_type,
                       fs.amount AS required, fp.amount_paid,
                       (fs.amount - fp.amount_paid) AS balance,
                       fp.status
                FROM fee_payments fp
                JOIN students      s  ON fp.student_id = s.student_id
                JOIN fee_structure fs ON fp.fee_id     = fs.fee_id
                WHERE fp.status IN ('Pending','Partial')
                ORDER BY balance DESC
                """;
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("  %-12s %-22s %-18s %12s %10s %-8s%n",
                    "Code", "Name", "Fee Type", "Balance ₹", "Paid ₹", "Status");
            System.out.println("  " + "─".repeat(84));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("  %-12s %-22s %-18s %,12.2f %,10.2f %-8s%n",
                        rs.getString("student_code"),
                        rs.getString("name"),
                        rs.getString("fee_type"),
                        rs.getDouble("balance"),
                        rs.getDouble("amount_paid"),
                        rs.getString("status"));
            }
            if (count == 0) System.out.println("  No pending fees — all clear!");

        } catch (SQLException e) {
            System.err.println("printPendingFees error: " + e.getMessage());
        }
    }
}

package menu;

import dao.FeeDAO;
import util.ConsoleUtil;

public class FeeMenu {

    private final FeeDAO dao = new FeeDAO();

    public void show() {
        while (true) {
            ConsoleUtil.printHeader("FEE MANAGEMENT");
            System.out.println("  1. View Fee Structures");
            System.out.println("  2. Add Fee Structure");
            System.out.println("  3. Record Fee Payment");
            System.out.println("  4. View Fee Status (by Student)");
            System.out.println("  5. Pending Fees Report");
            System.out.println("  0. Back to Main Menu");
            ConsoleUtil.printSectionLine();

            int choice = ConsoleUtil.readInt("  Choice: ");
            switch (choice) {
                case 1  -> viewStructures();
                case 2  -> addStructure();
                case 3  -> recordPayment();
                case 4  -> viewFeeStatus();
                case 5  -> pendingReport();
                case 0  -> { return; }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private void viewStructures() {
        ConsoleUtil.printHeader("FEE STRUCTURES");
        dao.printFeeStructures();
        ConsoleUtil.pause();
    }

    private void addStructure() {
        ConsoleUtil.printHeader("ADD FEE STRUCTURE");
        int    deptId       = ConsoleUtil.readInt("  Dept ID       : ");
        int    semester     = ConsoleUtil.readInt("  Semester      : ");
        String feeType      = ConsoleUtil.readLine("  Fee Type      : ");
        double amount       = ConsoleUtil.readDouble("  Amount (₹)    : ");
        String academicYear = ConsoleUtil.readLine("  Academic Year : ");

        if (dao.addFeeStructure(deptId, semester, feeType, amount, academicYear))
            ConsoleUtil.printSuccess("Fee structure added.");
        else
            ConsoleUtil.printError("Failed to add fee structure.");
        ConsoleUtil.pause();
    }

    private void recordPayment() {
        ConsoleUtil.printHeader("RECORD FEE PAYMENT");
        int    studentId   = ConsoleUtil.readInt("  Student ID    : ");
        int    feeId       = ConsoleUtil.readInt("  Fee ID        : ");
        double amountPaid  = ConsoleUtil.readDouble("  Amount Paid ₹ : ");
        System.out.println("  Payment Mode: Cash | Online | Cheque | DD");
        String payMode     = ConsoleUtil.readLine("  Payment Mode  : ");
        String receiptNo   = ConsoleUtil.readLine("  Receipt No    : ");
        String remarks     = ConsoleUtil.readLine("  Remarks       : ");

        if (dao.recordPayment(studentId, feeId, amountPaid, payMode, receiptNo, remarks))
            ConsoleUtil.printSuccess("Payment recorded successfully!");
        else
            ConsoleUtil.printError("Failed to record payment.");
        ConsoleUtil.pause();
    }

    private void viewFeeStatus() {
        ConsoleUtil.printHeader("STUDENT FEE STATUS");
        int studentId = ConsoleUtil.readInt("  Student ID: ");
        dao.printFeeStatus(studentId);
        ConsoleUtil.pause();
    }

    private void pendingReport() {
        ConsoleUtil.printHeader("PENDING FEES REPORT");
        dao.printPendingFees();
        ConsoleUtil.pause();
    }
}

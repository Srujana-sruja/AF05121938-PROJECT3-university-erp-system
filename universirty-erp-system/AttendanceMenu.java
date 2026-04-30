package menu;

import dao.AttendanceDAO;
import util.ConsoleUtil;

public class AttendanceMenu {

    private final AttendanceDAO dao = new AttendanceDAO();

    public void show() {
        while (true) {
            ConsoleUtil.printHeader("ATTENDANCE MANAGEMENT");
            System.out.println("  1. Mark Attendance");
            System.out.println("  2. View Attendance by Course");
            System.out.println("  3. Student Attendance Summary");
            System.out.println("  0. Back to Main Menu");
            ConsoleUtil.printSectionLine();

            int choice = ConsoleUtil.readInt("  Choice: ");
            switch (choice) {
                case 1  -> markAttendance();
                case 2  -> viewByCourse();
                case 3  -> studentSummary();
                case 0  -> { return; }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private void markAttendance() {
        ConsoleUtil.printHeader("MARK ATTENDANCE");
        int    studentId = ConsoleUtil.readInt("  Student ID             : ");
        int    courseId  = ConsoleUtil.readInt("  Course ID              : ");
        String date      = ConsoleUtil.readLine("  Date (YYYY-MM-DD)      : ");
        String status    = ConsoleUtil.readLine("  Status (Present/Absent/Late): ");

        if (dao.markAttendance(studentId, courseId, date, status))
            ConsoleUtil.printSuccess("Attendance marked.");
        else
            ConsoleUtil.printError("Failed to mark attendance.");
        ConsoleUtil.pause();
    }

    private void viewByCourse() {
        ConsoleUtil.printHeader("COURSE ATTENDANCE");
        int courseId = ConsoleUtil.readInt("  Course ID: ");
        dao.printAttendanceByCourse(courseId);
        ConsoleUtil.pause();
    }

    private void studentSummary() {
        ConsoleUtil.printHeader("STUDENT ATTENDANCE SUMMARY");
        int studentId = ConsoleUtil.readInt("  Student ID: ");
        dao.printAttendanceSummary(studentId);
        ConsoleUtil.pause();
    }
}

package menu;

import dao.ExamResultDAO;
import util.ConsoleUtil;

public class ExamMenu {

    private final ExamResultDAO dao = new ExamResultDAO();

    public void show() {
        while (true) {
            ConsoleUtil.printHeader("EXAMINATION RESULTS");
            System.out.println("  1. Enter Exam Result");
            System.out.println("  2. View Results by Student");
            System.out.println("  3. View Results by Course");
            System.out.println("  0. Back to Main Menu");
            ConsoleUtil.printSectionLine();

            int choice = ConsoleUtil.readInt("  Choice: ");
            switch (choice) {
                case 1  -> addResult();
                case 2  -> viewByStudent();
                case 3  -> viewByCourse();
                case 0  -> { return; }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private void addResult() {
        ConsoleUtil.printHeader("ENTER EXAM RESULT");
        int    studentId    = ConsoleUtil.readInt("  Student ID              : ");
        int    courseId     = ConsoleUtil.readInt("  Course ID               : ");
        System.out.println("  Exam Type options: Midterm | Final | Quiz | Assignment");
        String examType     = ConsoleUtil.readLine("  Exam Type               : ");
        double marksObt     = ConsoleUtil.readDouble("  Marks Obtained          : ");
        double totalMarks   = ConsoleUtil.readDouble("  Total Marks             : ");
        String examDate     = ConsoleUtil.readLine("  Exam Date (YYYY-MM-DD)  : ");
        String remarks      = ConsoleUtil.readLine("  Remarks (optional)      : ");

        if (dao.addResult(studentId, courseId, examType, marksObt, totalMarks, examDate, remarks))
            ConsoleUtil.printSuccess("Result recorded successfully!");
        else
            ConsoleUtil.printError("Failed to record result.");
        ConsoleUtil.pause();
    }

    private void viewByStudent() {
        ConsoleUtil.printHeader("RESULTS – BY STUDENT");
        int studentId = ConsoleUtil.readInt("  Student ID: ");
        dao.printResultsByStudent(studentId);
        ConsoleUtil.pause();
    }

    private void viewByCourse() {
        ConsoleUtil.printHeader("RESULTS – BY COURSE");
        int    courseId = ConsoleUtil.readInt("  Course ID : ");
        System.out.println("  Exam Type options: Midterm | Final | Quiz | Assignment");
        String examType = ConsoleUtil.readLine("  Exam Type : ");
        dao.printResultsByCourse(courseId, examType);
        ConsoleUtil.pause();
    }
}

import menu.*;
import util.ConsoleUtil;
import util.DBConnection;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 *  University ERP System  –  Main Entry Point
 *  Tech : Java 17+  |  MySQL 8+  |  JDBC (mysql-connector-j)
 * ╚══════════════════════════════════════════════════════════════╝
 *
 *  HOW TO RUN:
 *    1. Create DB:  mysql -u root -p < sql/schema.sql
 *    2. Edit src/util/DBConnection.java → set your MySQL password
 *    3. Compile:
 *         javac -cp ".;lib/mysql-connector-j-8.x.x.jar" -d out \
 *               src/**\/\*.java
 *    4. Run:
 *         java  -cp "out;lib/mysql-connector-j-8.x.x.jar" Main
 *
 *  (Use ':' instead of ';' on Linux/macOS)
 */
public class Main {

    public static void main(String[] args) {
        printBanner();

        // Verify DB connectivity on startup
        try {
            DBConnection.getConnection();
            ConsoleUtil.printSuccess("Connected to MySQL database.\n");
        } catch (Exception e) {
            ConsoleUtil.printError("Database connection failed: " + e.getMessage());
            ConsoleUtil.printError("Please check DBConnection.java settings and ensure MySQL is running.");
            return;
        }

        StudentMenu  studentMenu  = new StudentMenu();
        FacultyMenu  facultyMenu  = new FacultyMenu();
        CourseMenu   courseMenu   = new CourseMenu();
        AttendanceMenu attMenu    = new AttendanceMenu();
        ExamMenu     examMenu     = new ExamMenu();
        FeeMenu      feeMenu      = new FeeMenu();

        while (true) {
            ConsoleUtil.printHeader("UNIVERSITY ERP SYSTEM – MAIN MENU");
            System.out.println("  1.  Student Management");
            System.out.println("  2.  Faculty Management");
            System.out.println("  3.  Course Management");
            System.out.println("  4.  Attendance Management");
            System.out.println("  5.  Examination Results");
            System.out.println("  6.  Fee Management");
            System.out.println("  0.  Exit");
            ConsoleUtil.printSectionLine();

            int choice = ConsoleUtil.readInt("  Choice: ");
            switch (choice) {
                case 1  -> studentMenu.show();
                case 2  -> facultyMenu.show();
                case 3  -> courseMenu.show();
                case 4  -> attMenu.show();
                case 5  -> examMenu.show();
                case 6  -> feeMenu.show();
                case 0  -> {
                    DBConnection.closeConnection();
                    ConsoleUtil.printSuccess("Thank you for using University ERP. Goodbye!");
                    return;
                }
                default -> ConsoleUtil.printError("Invalid option. Choose 0-6.");
            }
        }
    }

    private static void printBanner() {
        System.out.println("""
                
                ╔══════════════════════════════════════════════════════════════╗
                ║                                                              ║
                ║          U N I V E R S I T Y   E R P   S Y S T E M          ║
                ║                                                              ║
                ║    Modules: Students · Faculty · Courses · Attendance        ║
                ║             Examination Results · Fee Management             ║
                ║                                                              ║
                ║    Tech Stack: Java 17+  |  MySQL 8+  |  JDBC               ║
                ╚══════════════════════════════════════════════════════════════╝
                """);
    }
}

package menu;

import dao.CourseDAO;
import model.Course;
import util.ConsoleUtil;

import java.util.List;

public class CourseMenu {

    private final CourseDAO dao = new CourseDAO();

    public void show() {
        while (true) {
            ConsoleUtil.printHeader("COURSE MANAGEMENT");
            System.out.println("  1. Add New Course");
            System.out.println("  2. View All Courses");
            System.out.println("  3. Enroll Student in Course");
            System.out.println("  4. View Enrolled Students");
            System.out.println("  5. Update Course");
            System.out.println("  6. Delete Course");
            System.out.println("  0. Back to Main Menu");
            ConsoleUtil.printSectionLine();

            int choice = ConsoleUtil.readInt("  Choice: ");
            switch (choice) {
                case 1  -> addCourse();
                case 2  -> viewAll();
                case 3  -> enrollStudent();
                case 4  -> viewEnrollments();
                case 5  -> update();
                case 6  -> delete();
                case 0  -> { return; }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    /* ── ADD ─────────────────────────────────────────────── */

    private void addCourse() {
        ConsoleUtil.printHeader("ADD COURSE");
        Course c = new Course();
        c.setCourseCode(ConsoleUtil.readLine("  Course Code : "));
        c.setCourseName(ConsoleUtil.readLine("  Course Name : "));
        c.setCredits(ConsoleUtil.readInt("  Credits     : "));
        c.setDeptId(ConsoleUtil.readInt("  Dept ID     : "));
        c.setFacultyId(ConsoleUtil.readInt("  Faculty ID  : "));
        c.setSemester(ConsoleUtil.readInt("  Semester    : "));
        c.setMaxStudents(ConsoleUtil.readInt("  Max Students: "));
        c.setStatus("Active");

        if (dao.addCourse(c))
            ConsoleUtil.printSuccess("Course added successfully!");
        else
            ConsoleUtil.printError("Failed to add course.");
        ConsoleUtil.pause();
    }

    /* ── VIEW ALL ────────────────────────────────────────── */

    private void viewAll() {
        ConsoleUtil.printHeader("ALL COURSES");
        List<Course> list = dao.getAllCourses();
        if (list.isEmpty()) {
            ConsoleUtil.printInfo("No courses found.");
        } else {
            System.out.printf("  %-4s %-10s %-30s %4s %4s %6s %-8s%n",
                    "ID", "Code", "Name", "Cred", "Sem", "MaxStu", "Status");
            ConsoleUtil.printSectionLine();
            for (Course c : list) {
                System.out.printf("  %-4d %-10s %-30s %4d %4d %6d %-8s%n",
                        c.getCourseId(), c.getCourseCode(), c.getCourseName(),
                        c.getCredits(), c.getSemester(), c.getMaxStudents(), c.getStatus());
            }
            ConsoleUtil.printInfo("Total: " + list.size() + " course(s).");
        }
        ConsoleUtil.pause();
    }

    /* ── ENROLL ──────────────────────────────────────────── */

    private void enrollStudent() {
        ConsoleUtil.printHeader("ENROLL STUDENT");
        int studentId = ConsoleUtil.readInt("  Student ID : ");
        int courseId  = ConsoleUtil.readInt("  Course ID  : ");

        if (dao.enrollStudent(studentId, courseId))
            ConsoleUtil.printSuccess("Student enrolled successfully!");
        else
            ConsoleUtil.printError("Enrollment failed (may already be enrolled).");
        ConsoleUtil.pause();
    }

    /* ── VIEW ENROLLMENTS ────────────────────────────────── */

    private void viewEnrollments() {
        ConsoleUtil.printHeader("ENROLLED STUDENTS");
        int courseId = ConsoleUtil.readInt("  Course ID: ");
        dao.printEnrollmentsForCourse(courseId);
        ConsoleUtil.pause();
    }

    /* ── UPDATE ──────────────────────────────────────────── */

    private void update() {
        ConsoleUtil.printHeader("UPDATE COURSE");
        int id = ConsoleUtil.readInt("  Enter Course ID to update: ");
        Course c = dao.getCourseById(id);
        if (c == null) {
            ConsoleUtil.printError("Course not found.");
            ConsoleUtil.pause();
            return;
        }
        System.out.println("  Current: " + c);

        String nm = ConsoleUtil.readLine("  Course Name [" + c.getCourseName() + "]: ");
        if (!nm.isEmpty()) c.setCourseName(nm);

        String cr = ConsoleUtil.readLine("  Credits [" + c.getCredits() + "]: ");
        if (!cr.isEmpty()) c.setCredits(Integer.parseInt(cr));

        String fi = ConsoleUtil.readLine("  Faculty ID [" + c.getFacultyId() + "]: ");
        if (!fi.isEmpty()) c.setFacultyId(Integer.parseInt(fi));

        String st = ConsoleUtil.readLine("  Status (Active/Inactive) [" + c.getStatus() + "]: ");
        if (!st.isEmpty()) c.setStatus(st);

        if (dao.updateCourse(c))
            ConsoleUtil.printSuccess("Course updated successfully!");
        else
            ConsoleUtil.printError("Update failed.");
        ConsoleUtil.pause();
    }

    /* ── DELETE ──────────────────────────────────────────── */

    private void delete() {
        ConsoleUtil.printHeader("DELETE COURSE");
        int id = ConsoleUtil.readInt("  Enter Course ID to delete: ");
        String confirm = ConsoleUtil.readLine("  Confirm delete? (yes/no): ");
        if ("yes".equalsIgnoreCase(confirm)) {
            if (dao.deleteCourse(id))
                ConsoleUtil.printSuccess("Course deleted.");
            else
                ConsoleUtil.printError("Deletion failed. Check ID.");
        } else {
            ConsoleUtil.printInfo("Deletion cancelled.");
        }
        ConsoleUtil.pause();
    }
}

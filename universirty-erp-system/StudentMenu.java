package menu;

import dao.StudentDAO;
import model.Student;
import util.ConsoleUtil;

import java.util.List;

public class StudentMenu {

    private final StudentDAO dao = new StudentDAO();

    public void show() {
        while (true) {
            ConsoleUtil.printHeader("STUDENT MANAGEMENT");
            System.out.println("  1. Add New Student");
            System.out.println("  2. View All Students");
            System.out.println("  3. Search Student");
            System.out.println("  4. Update Student");
            System.out.println("  5. Delete Student");
            System.out.println("  0. Back to Main Menu");
            ConsoleUtil.printSectionLine();

            int choice = ConsoleUtil.readInt("  Choice: ");
            switch (choice) {
                case 1  -> addStudent();
                case 2  -> viewAll();
                case 3  -> search();
                case 4  -> update();
                case 5  -> delete();
                case 0  -> { return; }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    /* ── ADD ─────────────────────────────────────────────── */

    private void addStudent() {
        ConsoleUtil.printHeader("ADD STUDENT");
        Student s = new Student();
        s.setStudentCode(ConsoleUtil.readLine("  Student Code    : "));
        s.setFirstName(ConsoleUtil.readLine("  First Name      : "));
        s.setLastName(ConsoleUtil.readLine("  Last Name       : "));
        s.setEmail(ConsoleUtil.readLine("  Email           : "));
        s.setPhone(ConsoleUtil.readLine("  Phone           : "));
        s.setDob(ConsoleUtil.readLine("  DOB (YYYY-MM-DD): "));
        s.setGender(ConsoleUtil.readLine("  Gender (Male/Female/Other): "));
        s.setAddress(ConsoleUtil.readLine("  Address         : "));
        s.setDeptId(ConsoleUtil.readInt("  Dept ID         : "));
        s.setSemester(ConsoleUtil.readInt("  Semester        : "));
        s.setAdmissionDate(ConsoleUtil.readLine("  Admission Date (YYYY-MM-DD): "));
        s.setStatus("Active");

        if (dao.addStudent(s))
            ConsoleUtil.printSuccess("Student added successfully!");
        else
            ConsoleUtil.printError("Failed to add student.");
        ConsoleUtil.pause();
    }

    /* ── VIEW ALL ────────────────────────────────────────── */

    private void viewAll() {
        ConsoleUtil.printHeader("ALL STUDENTS");
        List<Student> list = dao.getAllStudents();
        if (list.isEmpty()) {
            ConsoleUtil.printInfo("No students found.");
        } else {
            System.out.printf("  %-4s %-12s %-26s %-30s %4s %-10s%n",
                    "ID", "Code", "Name", "Email", "Sem", "Status");
            ConsoleUtil.printSectionLine();
            for (Student s : list) {
                System.out.printf("  %-4d %-12s %-26s %-30s %4d %-10s%n",
                        s.getStudentId(),
                        s.getStudentCode(),
                        s.getFirstName() + " " + s.getLastName(),
                        s.getEmail(),
                        s.getSemester(),
                        s.getStatus());
            }
            ConsoleUtil.printInfo("Total: " + list.size() + " student(s).");
        }
        ConsoleUtil.pause();
    }

    /* ── SEARCH ──────────────────────────────────────────── */

    private void search() {
        ConsoleUtil.printHeader("SEARCH STUDENTS");
        String kw = ConsoleUtil.readLine("  Enter keyword: ");
        List<Student> list = dao.searchStudents(kw);
        if (list.isEmpty()) {
            ConsoleUtil.printInfo("No matching students.");
        } else {
            for (Student s : list) System.out.println("  " + s);
        }
        ConsoleUtil.pause();
    }

    /* ── UPDATE ──────────────────────────────────────────── */

    private void update() {
        ConsoleUtil.printHeader("UPDATE STUDENT");
        int id = ConsoleUtil.readInt("  Enter Student ID to update: ");
        Student s = dao.getStudentById(id);
        if (s == null) {
            ConsoleUtil.printError("Student not found.");
            ConsoleUtil.pause();
            return;
        }
        System.out.println("  Current: " + s);
        System.out.println("  (Press ENTER to keep existing value)");

        String fn = ConsoleUtil.readLine("  First Name [" + s.getFirstName() + "]: ");
        if (!fn.isEmpty()) s.setFirstName(fn);

        String ln = ConsoleUtil.readLine("  Last Name [" + s.getLastName() + "]: ");
        if (!ln.isEmpty()) s.setLastName(ln);

        String em = ConsoleUtil.readLine("  Email [" + s.getEmail() + "]: ");
        if (!em.isEmpty()) s.setEmail(em);

        String ph = ConsoleUtil.readLine("  Phone [" + s.getPhone() + "]: ");
        if (!ph.isEmpty()) s.setPhone(ph);

        String semStr = ConsoleUtil.readLine("  Semester [" + s.getSemester() + "]: ");
        if (!semStr.isEmpty()) s.setSemester(Integer.parseInt(semStr));

        String st = ConsoleUtil.readLine("  Status (Active/Inactive/Graduated) [" + s.getStatus() + "]: ");
        if (!st.isEmpty()) s.setStatus(st);

        if (dao.updateStudent(s))
            ConsoleUtil.printSuccess("Student updated successfully!");
        else
            ConsoleUtil.printError("Update failed.");
        ConsoleUtil.pause();
    }

    /* ── DELETE ──────────────────────────────────────────── */

    private void delete() {
        ConsoleUtil.printHeader("DELETE STUDENT");
        int id = ConsoleUtil.readInt("  Enter Student ID to delete: ");
        String confirm = ConsoleUtil.readLine("  Confirm delete? This removes all related records. (yes/no): ");
        if ("yes".equalsIgnoreCase(confirm)) {
            if (dao.deleteStudent(id))
                ConsoleUtil.printSuccess("Student deleted.");
            else
                ConsoleUtil.printError("Deletion failed. Check ID.");
        } else {
            ConsoleUtil.printInfo("Deletion cancelled.");
        }
        ConsoleUtil.pause();
    }
}

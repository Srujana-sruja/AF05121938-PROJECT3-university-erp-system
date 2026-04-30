package menu;

import dao.FacultyDAO;
import model.Faculty;
import util.ConsoleUtil;

import java.util.List;

public class FacultyMenu {

    private final FacultyDAO dao = new FacultyDAO();

    public void show() {
        while (true) {
            ConsoleUtil.printHeader("FACULTY MANAGEMENT");
            System.out.println("  1. Add New Faculty");
            System.out.println("  2. View All Faculty");
            System.out.println("  3. Update Faculty");
            System.out.println("  4. Delete Faculty");
            System.out.println("  0. Back to Main Menu");
            ConsoleUtil.printSectionLine();

            int choice = ConsoleUtil.readInt("  Choice: ");
            switch (choice) {
                case 1  -> addFaculty();
                case 2  -> viewAll();
                case 3  -> update();
                case 4  -> delete();
                case 0  -> { return; }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    /* ── ADD ─────────────────────────────────────────────── */

    private void addFaculty() {
        ConsoleUtil.printHeader("ADD FACULTY");
        Faculty f = new Faculty();
        f.setFacultyCode(ConsoleUtil.readLine("  Faculty Code    : "));
        f.setFirstName(ConsoleUtil.readLine("  First Name      : "));
        f.setLastName(ConsoleUtil.readLine("  Last Name       : "));
        f.setEmail(ConsoleUtil.readLine("  Email           : "));
        f.setPhone(ConsoleUtil.readLine("  Phone           : "));
        f.setDesignation(ConsoleUtil.readLine("  Designation     : "));
        f.setDeptId(ConsoleUtil.readInt("  Dept ID         : "));
        f.setJoinDate(ConsoleUtil.readLine("  Join Date (YYYY-MM-DD): "));
        f.setSalary(ConsoleUtil.readDouble("  Salary          : "));
        f.setStatus("Active");

        if (dao.addFaculty(f))
            ConsoleUtil.printSuccess("Faculty added successfully!");
        else
            ConsoleUtil.printError("Failed to add faculty.");
        ConsoleUtil.pause();
    }

    /* ── VIEW ALL ────────────────────────────────────────── */

    private void viewAll() {
        ConsoleUtil.printHeader("ALL FACULTY");
        List<Faculty> list = dao.getAllFaculty();
        if (list.isEmpty()) {
            ConsoleUtil.printInfo("No faculty records found.");
        } else {
            System.out.printf("  %-4s %-12s %-26s %-30s %-22s %-10s%n",
                    "ID", "Code", "Name", "Email", "Designation", "Status");
            ConsoleUtil.printSectionLine();
            for (Faculty f : list) {
                System.out.printf("  %-4d %-12s %-26s %-30s %-22s %-10s%n",
                        f.getFacultyId(),
                        f.getFacultyCode(),
                        f.getFirstName() + " " + f.getLastName(),
                        f.getEmail(),
                        f.getDesignation(),
                        f.getStatus());
            }
            ConsoleUtil.printInfo("Total: " + list.size() + " faculty member(s).");
        }
        ConsoleUtil.pause();
    }

    /* ── UPDATE ──────────────────────────────────────────── */

    private void update() {
        ConsoleUtil.printHeader("UPDATE FACULTY");
        int id = ConsoleUtil.readInt("  Enter Faculty ID to update: ");
        Faculty f = dao.getFacultyById(id);
        if (f == null) {
            ConsoleUtil.printError("Faculty not found.");
            ConsoleUtil.pause();
            return;
        }
        System.out.println("  Current: " + f);
        System.out.println("  (Press ENTER to keep existing value)");

        String fn = ConsoleUtil.readLine("  First Name [" + f.getFirstName() + "]: ");
        if (!fn.isEmpty()) f.setFirstName(fn);

        String ln = ConsoleUtil.readLine("  Last Name [" + f.getLastName() + "]: ");
        if (!ln.isEmpty()) f.setLastName(ln);

        String em = ConsoleUtil.readLine("  Email [" + f.getEmail() + "]: ");
        if (!em.isEmpty()) f.setEmail(em);

        String ph = ConsoleUtil.readLine("  Phone [" + f.getPhone() + "]: ");
        if (!ph.isEmpty()) f.setPhone(ph);

        String des = ConsoleUtil.readLine("  Designation [" + f.getDesignation() + "]: ");
        if (!des.isEmpty()) f.setDesignation(des);

        String salStr = ConsoleUtil.readLine("  Salary [" + f.getSalary() + "]: ");
        if (!salStr.isEmpty()) f.setSalary(Double.parseDouble(salStr));

        String st = ConsoleUtil.readLine("  Status (Active/Inactive) [" + f.getStatus() + "]: ");
        if (!st.isEmpty()) f.setStatus(st);

        if (dao.updateFaculty(f))
            ConsoleUtil.printSuccess("Faculty updated successfully!");
        else
            ConsoleUtil.printError("Update failed.");
        ConsoleUtil.pause();
    }

    /* ── DELETE ──────────────────────────────────────────── */

    private void delete() {
        ConsoleUtil.printHeader("DELETE FACULTY");
        int id = ConsoleUtil.readInt("  Enter Faculty ID to delete: ");
        String confirm = ConsoleUtil.readLine("  Confirm delete? (yes/no): ");
        if ("yes".equalsIgnoreCase(confirm)) {
            if (dao.deleteFaculty(id))
                ConsoleUtil.printSuccess("Faculty member deleted.");
            else
                ConsoleUtil.printError("Deletion failed. Check ID.");
        } else {
            ConsoleUtil.printInfo("Deletion cancelled.");
        }
        ConsoleUtil.pause();
    }
}

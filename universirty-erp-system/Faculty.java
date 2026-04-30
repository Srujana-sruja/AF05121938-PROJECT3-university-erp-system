package model;

public class Faculty {
    private int    facultyId;
    private String facultyCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String designation;
    private int    deptId;
    private String joinDate;
    private double salary;
    private String status;

    public Faculty() {}

    public Faculty(String facultyCode, String firstName, String lastName,
                   String email, String phone, String designation,
                   int deptId, String joinDate, double salary, String status) {
        this.facultyCode  = facultyCode;
        this.firstName    = firstName;
        this.lastName     = lastName;
        this.email        = email;
        this.phone        = phone;
        this.designation  = designation;
        this.deptId       = deptId;
        this.joinDate     = joinDate;
        this.salary       = salary;
        this.status       = status;
    }

    /* ── getters / setters ────────────────────────── */

    public int    getFacultyId()      { return facultyId; }
    public void   setFacultyId(int v) { facultyId = v; }

    public String getFacultyCode()         { return facultyCode; }
    public void   setFacultyCode(String v) { facultyCode = v; }

    public String getFirstName()         { return firstName; }
    public void   setFirstName(String v) { firstName = v; }

    public String getLastName()         { return lastName; }
    public void   setLastName(String v) { lastName = v; }

    public String getEmail()         { return email; }
    public void   setEmail(String v) { email = v; }

    public String getPhone()         { return phone; }
    public void   setPhone(String v) { phone = v; }

    public String getDesignation()         { return designation; }
    public void   setDesignation(String v) { designation = v; }

    public int  getDeptId()      { return deptId; }
    public void setDeptId(int v) { deptId = v; }

    public String getJoinDate()         { return joinDate; }
    public void   setJoinDate(String v) { joinDate = v; }

    public double getSalary()         { return salary; }
    public void   setSalary(double v) { salary = v; }

    public String getStatus()         { return status; }
    public void   setStatus(String v) { status = v; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s  |  Code: %s  |  Designation: %s  |  Status: %s",
                facultyId, firstName, lastName, facultyCode, designation, status);
    }
}

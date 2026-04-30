package model;

public class Student {
    private int    studentId;
    private String studentCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dob;
    private String gender;
    private String address;
    private int    deptId;
    private int    semester;
    private String admissionDate;
    private String status;

    /* ── constructors ─────────────────────────────── */

    public Student() {}

    public Student(String studentCode, String firstName, String lastName,
                   String email, String phone, String dob, String gender,
                   String address, int deptId, int semester,
                   String admissionDate, String status) {
        this.studentCode   = studentCode;
        this.firstName     = firstName;
        this.lastName      = lastName;
        this.email         = email;
        this.phone         = phone;
        this.dob           = dob;
        this.gender        = gender;
        this.address       = address;
        this.deptId        = deptId;
        this.semester      = semester;
        this.admissionDate = admissionDate;
        this.status        = status;
    }

    /* ── getters / setters ────────────────────────── */

    public int    getStudentId()      { return studentId; }
    public void   setStudentId(int v) { studentId = v; }

    public String getStudentCode()         { return studentCode; }
    public void   setStudentCode(String v) { studentCode = v; }

    public String getFirstName()         { return firstName; }
    public void   setFirstName(String v) { firstName = v; }

    public String getLastName()         { return lastName; }
    public void   setLastName(String v) { lastName = v; }

    public String getEmail()         { return email; }
    public void   setEmail(String v) { email = v; }

    public String getPhone()         { return phone; }
    public void   setPhone(String v) { phone = v; }

    public String getDob()         { return dob; }
    public void   setDob(String v) { dob = v; }

    public String getGender()         { return gender; }
    public void   setGender(String v) { gender = v; }

    public String getAddress()         { return address; }
    public void   setAddress(String v) { address = v; }

    public int  getDeptId()      { return deptId; }
    public void setDeptId(int v) { deptId = v; }

    public int  getSemester()      { return semester; }
    public void setSemester(int v) { semester = v; }

    public String getAdmissionDate()         { return admissionDate; }
    public void   setAdmissionDate(String v) { admissionDate = v; }

    public String getStatus()         { return status; }
    public void   setStatus(String v) { status = v; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s  |  Code: %s  |  Email: %s  |  Sem: %d  |  Status: %s",
                studentId, firstName, lastName, studentCode, email, semester, status);
    }
}

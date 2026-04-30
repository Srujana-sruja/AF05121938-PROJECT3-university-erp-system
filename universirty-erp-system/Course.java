package model;

public class Course {
    private int    courseId;
    private String courseCode;
    private String courseName;
    private int    credits;
    private int    deptId;
    private int    facultyId;
    private int    semester;
    private int    maxStudents;
    private String status;

    public Course() {}

    public Course(String courseCode, String courseName, int credits,
                  int deptId, int facultyId, int semester, int maxStudents, String status) {
        this.courseCode  = courseCode;
        this.courseName  = courseName;
        this.credits     = credits;
        this.deptId      = deptId;
        this.facultyId   = facultyId;
        this.semester    = semester;
        this.maxStudents = maxStudents;
        this.status      = status;
    }

    /* ── getters / setters ────────────────────────── */

    public int    getCourseId()      { return courseId; }
    public void   setCourseId(int v) { courseId = v; }

    public String getCourseCode()         { return courseCode; }
    public void   setCourseCode(String v) { courseCode = v; }

    public String getCourseName()         { return courseName; }
    public void   setCourseName(String v) { courseName = v; }

    public int  getCredits()      { return credits; }
    public void setCredits(int v) { credits = v; }

    public int  getDeptId()      { return deptId; }
    public void setDeptId(int v) { deptId = v; }

    public int  getFacultyId()      { return facultyId; }
    public void setFacultyId(int v) { facultyId = v; }

    public int  getSemester()      { return semester; }
    public void setSemester(int v) { semester = v; }

    public int  getMaxStudents()      { return maxStudents; }
    public void setMaxStudents(int v) { maxStudents = v; }

    public String getStatus()         { return status; }
    public void   setStatus(String v) { status = v; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s  |  Credits: %d  |  Sem: %d  |  Status: %s",
                courseId, courseCode, courseName, credits, semester, status);
    }
}

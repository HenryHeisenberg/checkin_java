package cn.ssm.model;

import javax.persistence.*;

@Table(name = "student_class")
public class StudentClass {
    @Id
    @Column(name = "student_id")
    private String studentId;

    @Id
    @Column(name = "class_id")
    private String classId;

    /**
     * @return student_id
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * @return class_id
     */
    public String getClassId() {
        return classId;
    }

    /**
     * @param classId
     */
    public void setClassId(String classId) {
        this.classId = classId;
    }
}
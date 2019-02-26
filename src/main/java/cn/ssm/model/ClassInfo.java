package cn.ssm.model;

import javax.persistence.*;

@Table(name = "class_info")
public class ClassInfo {
    @Id
    private String id;

    private String name;

    @Column(name = "teacher_id")
    private String teacherId;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return teacher_id
     */
    public String getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId
     */
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
package cn.ssm.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "student_checkin")
public class StudentCheckin {
    @Id
    @Column(name = "student_id")
    private String studentId;

    @Id
    @Column(name = "checkin_id")
    private String checkinId;

    @Column(name = "create_time")
    private Date createTime;

    private String img;

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
     * @return checkin_id
     */
    public String getCheckinId() {
        return checkinId;
    }

    /**
     * @param checkinId
     */
    public void setCheckinId(String checkinId) {
        this.checkinId = checkinId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img
     */
    public void setImg(String img) {
        this.img = img;
    }
}
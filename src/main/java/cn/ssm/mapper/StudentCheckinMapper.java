package cn.ssm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import cn.ssm.model.StudentCheckin;
import tk.mybatis.mapper.common.Mapper;

public interface StudentCheckinMapper extends Mapper<StudentCheckin> {
    @Select("SELECT * FROM student_checkin WHERE student_checkin.student_id = #{studentId} AND student_checkin.checkin_id IN ( SELECT checkin_info.id FROM checkin_info WHERE checkin_info.class_id = #{classId} )")
    List<StudentCheckin> findCheckedOrNot(String studentId, String classId);
}
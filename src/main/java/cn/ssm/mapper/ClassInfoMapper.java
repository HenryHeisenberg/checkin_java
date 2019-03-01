package cn.ssm.mapper;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Select;


import cn.ssm.model.ClassInfo;
import tk.mybatis.mapper.common.Mapper;

public interface ClassInfoMapper extends Mapper<ClassInfo> {
    @Select("SELECT * FROM class_info WHERE id IN ( SELECT id FROM class_info WHERE id NOT IN ( SELECT class_id FROM student_class WHERE student_id = #{id} ) )")
    List<ClassInfo> findUnselected(String id);
}
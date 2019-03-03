package cn.ssm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import cn.ssm.model.CheckinInfo;
import tk.mybatis.mapper.common.Mapper;

public interface CheckinInfoMapper extends Mapper<CheckinInfo> {

    @Select("select * from checkin_info where class_id=#{classId}")
    List<CheckinInfo> findCheckInfo(String classId);


}
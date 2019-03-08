package cn.ssm.service;

import cn.ssm.base.impl.BaseServiceImpl;
import cn.ssm.mapper.StudentCheckinMapper;
import cn.ssm.model.StudentCheckInInfo;
import cn.ssm.model.StudentCheckin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentCheckinService extends BaseServiceImpl<StudentCheckin> {
    @Autowired
    StudentCheckinMapper checkinMapper;
    
    public List<StudentCheckin> findCheckedOrNot(String studentId,String classId){
        return checkinMapper.findCheckedOrNot(studentId, classId);
    }

    public List<StudentCheckInInfo> getPresentStudents(String id){
        return checkinMapper.getPresentStudents(id);
    }

    public List<StudentCheckInInfo> getAbsentStudents(String id){
        return checkinMapper.getAbsentStudents(id);
    }

}

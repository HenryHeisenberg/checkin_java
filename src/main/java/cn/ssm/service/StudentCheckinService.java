package cn.ssm.service;

import cn.ssm.base.impl.BaseServiceImpl;
import cn.ssm.mapper.StudentCheckinMapper;
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
}

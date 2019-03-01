package cn.ssm.service;


import cn.ssm.base.impl.BaseServiceImpl;
import cn.ssm.mapper.ClassInfoMapper;
import cn.ssm.model.ClassInfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassInfoService extends BaseServiceImpl<ClassInfo> {
    @Autowired
    ClassInfoMapper classInfoMapper;
    public List<ClassInfo> findUnselected(String id) {
        return classInfoMapper.findUnselected(id);
    }
}

package cn.ssm.service;

import cn.ssm.base.impl.BaseServiceImpl;
import cn.ssm.mapper.CheckinInfoMapper;
import cn.ssm.model.CheckinInfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckinInfoService extends BaseServiceImpl<CheckinInfo> {
    
    @Autowired
    CheckinInfoMapper checkinInfoMapper;
    
    public List<CheckinInfo> findListCheckin(String classId){
        return checkinInfoMapper.findCheckInfo(classId);
    }
}

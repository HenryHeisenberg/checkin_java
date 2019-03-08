package cn.ssm.web;

import cn.ssm.base.BaseResult;
import cn.ssm.model.CheckinInfo;
import cn.ssm.model.ClassInfo;
import cn.ssm.model.StudentCheckInInfo;
import cn.ssm.service.CheckinInfoService;
import cn.ssm.service.ClassInfoService;
import cn.ssm.service.StudentCheckinService;
import cn.ssm.utils.util.StringUtil;
import cn.ssm.utils.util.UUIDUtils;
import com.github.pagehelper.PageHelper;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    ClassInfoService classInfoService;

    @Autowired
    CheckinInfoService checkinInfoService;

    @Autowired
    StudentCheckinService studentCheckinService;

    @GetMapping("/getClassesByID")
    public Object getClassesByID(String id){
        if(StringUtils.isEmpty(id)){
            return BaseResult.fail("老师ID不能为空！");
        }
        ClassInfo classInfo=new ClassInfo();
        classInfo.setTeacherId(id);
        List<ClassInfo> select = classInfoService.select(classInfo);
        return BaseResult.success(select);
    }

    @PostMapping("/createClass")
    public Object createClass(ClassInfo classInfo){
        if(StringUtils.isEmpty(classInfo.getName())){
            return BaseResult.fail("课程名不能为空！");
        }
        List<ClassInfo> select = classInfoService.select(classInfo);
        if(select.size()>0){
            return BaseResult.fail("课程名不能重复！");
        }
        classInfo.setId(UUIDUtils.getUUID());
        classInfoService.insert(classInfo);
        ClassInfo classInfo1=new ClassInfo();
        classInfo1.setTeacherId(classInfo.getTeacherId());
        List<ClassInfo> classInfos = classInfoService.select(classInfo1);
        return BaseResult.success(classInfos);
    }

    @PostMapping("/setCheckin")
    public Object setCheckin(CheckinInfo checkinInfo){
        if(StringUtils.isEmpty(checkinInfo.getClassId())){
            return BaseResult.fail("未选择课程名");
        }
        if (StringUtils.isEmpty(checkinInfo.getStartTime())){
            return BaseResult.fail("开始时间不能为空！");
        }
        if (StringUtils.isEmpty(checkinInfo.getEndTime())){
            return BaseResult.fail("结束时间不能为空！");
        }
        if(StringUtils.isEmpty(checkinInfo.getCreateTime())){
            checkinInfo.setCreateTime(new Date());
        }
        checkinInfo.setId(UUIDUtils.getUUID());
        checkinInfoService.insert(checkinInfo);
        return BaseResult.success("设置签到成功");
    }

    @GetMapping("/getAllCheckin")
    public Object getAllCheckins(CheckinInfo checkinInfo){
        if(StringUtils.isEmpty(checkinInfo.getClassId())){
            return BaseResult.fail("课程不能为空");
        }
        PageHelper.orderBy("create_time");
        List<CheckinInfo> select = checkinInfoService.select(checkinInfo);
        return BaseResult.success(select);
    }

    @GetMapping("/getCheckinInfo")
    public Object getCheckinInfo (String id){
        if(StringUtils.isEmpty(id)){
            return BaseResult.fail("缺少参数");
        }
        Map<String ,Object> resultMap =new HashMap<>();
        List<StudentCheckInInfo> presentStudents = studentCheckinService.getPresentStudents(id);
        List<StudentCheckInInfo> absentStudents = studentCheckinService.getAbsentStudents(id);
        int totalNum=presentStudents.size()+absentStudents.size();
        resultMap.put("totalNum",totalNum);
        resultMap.put("presentStudents",presentStudents);
        resultMap.put("absentStudents",absentStudents);
        return BaseResult.success(resultMap);
    }


}

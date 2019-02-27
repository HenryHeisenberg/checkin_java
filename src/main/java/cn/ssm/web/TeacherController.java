package cn.ssm.web;

import cn.ssm.base.BaseResult;
import cn.ssm.model.CheckinInfo;
import cn.ssm.model.ClassInfo;
import cn.ssm.service.CheckinInfoService;
import cn.ssm.service.ClassInfoService;
import cn.ssm.utils.util.StringUtil;
import cn.ssm.utils.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    ClassInfoService classInfoService;

    @Autowired
    CheckinInfoService checkinInfoService;

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
        classInfo.setId(UUIDUtils.getUUID());
        classInfoService.insert(classInfo);
        return BaseResult.success("添加课程成功");
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


}

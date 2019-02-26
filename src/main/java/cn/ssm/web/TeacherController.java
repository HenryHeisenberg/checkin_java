package cn.ssm.web;

import cn.ssm.base.BaseResult;
import cn.ssm.model.ClassInfo;
import cn.ssm.service.ClassInfoService;
import cn.ssm.utils.util.StringUtil;
import cn.ssm.utils.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    ClassInfoService classInfoService;

    @PostMapping("/createClass")
    public Object createClass(ClassInfo classInfo){
        if(StringUtils.isEmpty(classInfo.getName())){
            return BaseResult.fail("课程名不能为空！");
        }
        classInfo.setId(UUIDUtils.getUUID());
        classInfoService.insert(classInfo);
        return BaseResult.success("添加课程成功");
    }


}

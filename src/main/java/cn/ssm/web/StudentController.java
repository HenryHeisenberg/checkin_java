package cn.ssm.web;

import cn.ssm.base.BaseResult;
import cn.ssm.model.CheckinInfo;
import cn.ssm.model.ClassInfo;
import cn.ssm.model.StudentCheckin;
import cn.ssm.model.StudentClass;
import cn.ssm.model.UserInfo;
import cn.ssm.service.CheckinInfoService;
import cn.ssm.service.ClassInfoService;
import cn.ssm.service.StudentCheckinService;
import cn.ssm.service.StudentClassService;
import cn.ssm.service.UserInfoService;
import cn.ssm.utils.util.StringUtil;
import cn.ssm.utils.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    ClassInfoService classInfoService;

    @Autowired
    StudentClassService studentClassService;

    @Autowired
    CheckinInfoService checkinInfoService;

    @Autowired
    StudentCheckinService studentCheckinService;
    
    @Autowired
    UserInfoService userInfoService;

    //查找学生信息
    @PostMapping("/getStudent")
    public Object getStudent(String id) {
        UserInfo selectByKey = userInfoService.selectByKey(id);
        return BaseResult.success(selectByKey);
    }
    
    // 查询未加入的课程
    @GetMapping("/getClasses")
    public Object getClasses(String id) {
        List<ClassInfo> findUnselected = classInfoService.findUnselected(id);
        return BaseResult.success(findUnselected);
    }

    // 通过学生id查询已选课程
    @PostMapping("/getClassesById")
    public Object getClassesById(String id) {
        if (StringUtils.isEmpty(id)) {
            return BaseResult.fail("学生ID不能为空！");
        }
        StudentClass studentClass = new StudentClass();
        studentClass.setStudentId(id);
        List<StudentClass> select = studentClassService.select(studentClass);
        if (select.isEmpty()) {
            return BaseResult.fail("该学生未选择任何课程");
        }
        List<ClassInfo> classList = new ArrayList<ClassInfo>();
        for (StudentClass studentClassInfo : select) {
            String classId = studentClassInfo.getClassId();
            ClassInfo classInfo = classInfoService.selectByKey(classId);
            classList.add(classInfo);
        }
        return BaseResult.success(classList);
    }

    // 根据课程id查询课程详细信息
    @GetMapping("/getOneClassByClassId")
    public Object getOneClassByClassId(String id) {
        if (StringUtils.isEmpty(id)) {
            return BaseResult.fail("课程ID不能为空！");
        }
        ClassInfo classInfo = new ClassInfo();
        ClassInfo selectByKey = classInfoService.selectByKey(id);
        return BaseResult.success(selectByKey);
    }

    // 查询是否缺席
    @PostMapping("selectCheckOrNotById")
    public Object selectCheckOrNot(String studentId, String classId) {
        if (StringUtils.isEmpty(studentId)) {
            return BaseResult.fail("学生ID不能为空！");
        }
        if (StringUtils.isEmpty(classId)) {
            return BaseResult.fail("课程ID不能为空！");
        }
        CheckinInfo selectCheckinInfoByKey = checkinInfoService.selectByKey(classId);
        String checkinInfoId = selectCheckinInfoByKey.getId();
        StudentCheckin studentCheckin = new StudentCheckin();
        studentCheckin.setCheckinId(checkinInfoId);
        studentCheckin.setStudentId(studentId);
        List<StudentCheckin> selectByExample = studentCheckinService.selectByExample(studentCheckin);
        if (selectByExample.isEmpty()) {
            return BaseResult.success("此课程缺席");
        } else {
            return BaseResult.success("此课程已出席");
        }
    }

    // 用于选课
    @PostMapping("setClass")
    public Object setClass(String studentId, String classId) {
        if (StringUtils.isEmpty(studentId)) {
            return BaseResult.fail("学生ID不能为空！");
        }
        if (StringUtils.isEmpty(classId)) {
            return BaseResult.fail("课程ID不能为空！");
        }
        StudentClass studentClass = new StudentClass();
        studentClass.setStudentId(studentId);
        studentClass.setClassId(classId);
        studentClassService.insert(studentClass);
        ClassInfo classInfo = classInfoService.selectByKey(classId);
        return BaseResult.success("选择" + classInfo.getName() + "课程成功");
    }

    @PostMapping("checkin")
    public Object checkin(int check, String id) {
        return null;
    }
}

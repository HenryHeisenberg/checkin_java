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
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
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

    // 查找学生信息
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

    // 查询已加入的课程
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

    @PostMapping("/checkIn")
    public Object checkIn(String studentId, String checkId, String path) {
        if (StringUtils.isEmpty(studentId)) {
            return BaseResult.fail("学生ID不能为空！");
        }
        if (StringUtils.isEmpty(checkId)) {
            return BaseResult.fail("签到ID不能为空！");
        }
        if (StringUtils.isEmpty(path)) {
            return BaseResult.fail("签到照片不能为空！");
        }
        CheckinInfo select = new CheckinInfo();
        select.setId(checkId);
        CheckinInfo checkinInfo = checkinInfoService.selectByKey(select);
        Date now = new Date();
        if (checkinInfo == null) {
            return BaseResult.fail("未查询到此次签到");
        }

        Date startTime = checkinInfo.getStartTime();
        Date endTime = checkinInfo.getEndTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String start = sdf.format(startTime);
        if (startTime.after(now)) {
            return BaseResult.fail("此次签到未开始,开始时间为:" + start);
        }
        if (endTime.before(now)) {
            return BaseResult.fail("此次签到已结束");
        }
        StudentCheckin studentCheckin = new StudentCheckin();
        studentCheckin.setCheckinId(checkId);
        studentCheckin.setStudentId(studentId);
        StudentCheckin re_student = studentCheckinService.selectOne(studentCheckin);
        if (re_student != null) {
            return BaseResult.fail("请勿重复签到");
        }
        studentCheckin.setCreateTime(now);
        studentCheckin.setImg(path);
        studentCheckinService.insert(studentCheckin);
        return BaseResult.success("签到成功");
    }

    // 根据课程ID查询所有签到表
    @PostMapping("/getCheckInByClassId")
    public Object getCheckInByClassId(String classId, String userId) {
        List<CheckinInfo> selectByExample = checkinInfoService.findListCheckin(classId);
        for (CheckinInfo c : selectByExample) {
            c.setIsSign(false);
            String id = c.getId();
            StudentCheckin studentCheckin = new StudentCheckin();
            studentCheckin.setCheckinId(id);
            studentCheckin.setStudentId(userId);
            List<StudentCheckin> select = studentCheckinService.select(studentCheckin);
            if (select.size() > 0) {
                c.setIsSign(true);
            }
            long startTime = c.getStartTime().getTime();
            long endTime = c.getEndTime().getTime();
            long now = new Date().getTime();
            if (now > endTime && c.isIsSign() == false) {
                c.setStatus("已超时而且缺席");
            } else if (now > startTime && c.isIsSign() == true) {
                c.setStatus("已签到");
            } else if (now < startTime) {
                c.setStatus("未到签到时间");
            } else if (now > startTime && now < endTime && c.isIsSign() == false) {
                c.setStatus("可进行签到");
            }
        }
        if (selectByExample.isEmpty()) {
            BaseResult.fail("此课程没有任何签到信息");
        }
        return BaseResult.success(selectByExample);
    }

    // 查询是否缺席
    @PostMapping("/findCheckedOrNot")
    public Object findCheckedOrNot(String studentId, String classId) {
        List<StudentCheckin> findCheckedOrNot = studentCheckinService.findCheckedOrNot(studentId, classId);
        if (findCheckedOrNot.isEmpty()) {
            return BaseResult.success("此课未缺席");
        }
        return BaseResult.success(findCheckedOrNot);
    }

}

package cn.ssm.web;

import cn.ssm.base.BaseResult;
import cn.ssm.model.CheckinInfo;
import cn.ssm.model.ClassInfo;
import cn.ssm.model.StudentCheckin;
import cn.ssm.model.StudentClass;
import cn.ssm.service.CheckinInfoService;
import cn.ssm.service.ClassInfoService;
import cn.ssm.service.StudentCheckinService;
import cn.ssm.service.StudentClassService;
import cn.ssm.utils.util.StringUtil;
import cn.ssm.utils.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
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


    @GetMapping("/getClasses")
    public Object getClasses() {
        ClassInfo classInfo = new ClassInfo();
        List<ClassInfo> selectAll = classInfoService.selectAll();
        return BaseResult.success(selectAll);
    }

    @GetMapping("/getClassesById")
    public Object getClassesById(String id) {
        if (StringUtils.isEmpty(id)) {
            return BaseResult.fail("学生ID不能为空！");
        }
        StudentClass studentClass = new StudentClass();
        studentClass.setStudentId(id);
        List<StudentClass> select = studentClassService.select(studentClass);
        return BaseResult.success(select);
    }

    @GetMapping("/getOneClassByClassId")
    public Object getOneClassByClassId(String id) {
        if (StringUtils.isEmpty(id)) {
            return BaseResult.fail("课程ID不能为空！");
        }
        ClassInfo classInfo = new ClassInfo();
        ClassInfo selectByKey = classInfoService.selectByKey(id);
        return BaseResult.success(selectByKey);
    }

    @GetMapping("/selectCheckOrNotById")
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

    @PostMapping("/checkIn")
    public Object checkIn(String studentId, String checkId ,String path) {
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
        StudentCheckin studentCheckin=new StudentCheckin();
        studentCheckin.setCheckinId(checkId);
        studentCheckin.setStudentId(studentId);
        StudentCheckin re_student = studentCheckinService.selectOne(studentCheckin);
        if(re_student!=null){
            return BaseResult.fail("请勿重复签到");
        }
        studentCheckin.setCreateTime(now);
        studentCheckin.setImg(path);
        studentCheckinService.insert(studentCheckin);
        return BaseResult.success("签到成功");
    }

}

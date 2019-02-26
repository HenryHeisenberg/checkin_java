package cn.ssm.web;

import cn.ssm.base.BaseResult;
import cn.ssm.model.UserInfo;
import cn.ssm.service.UserInfoService;
import cn.ssm.utils.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
    UserInfoService userInfoService;

	@PostMapping("/login")
	public Object login(UserInfo user) {
		String username = user.getUsername();
		String password = user.getPassword();
		if (StringUtils.isAnyBlank(username, password)) {
			return BaseResult.fail("账号或密码为空");
		}
        UserInfo selectOne = userInfoService.selectOne(user);
		if (selectOne != null) {
			return BaseResult.success(selectOne);
		}
		return BaseResult.fail("账号或密码错误");
	}

	@PostMapping("/update")
	public Object update(String id, String old, String newPassword) {
		if (id == null || StringUtils.isAnyBlank(old, newPassword)) {
			return BaseResult.fail("缺少参数");
		}
        UserInfo selectByKey = userInfoService.selectByKey(id);
		String password = selectByKey.getPassword();
		if (!password.equals(old)) {
			return BaseResult.fail("原密码错误");
		}
		selectByKey.setPassword(newPassword);
        userInfoService.updateByPrimaryKeySelective(selectByKey);
		return BaseResult.success(selectByKey);
	}

	@PostMapping("/updateInfo")
	public Object updateInfo(UserInfo user) {
		userInfoService.updateByPrimaryKeySelective(user);
		return BaseResult.success("更新成功");
	}

	@PostMapping("/register")
	public Object register(UserInfo user) {
		String username = user.getUsername();
		String password = user.getPassword();
		if (StringUtils.isAnyBlank(username, password)) {
			return BaseResult.fail("账号或密码为空");
		}
        UserInfo user2 = new UserInfo();
		user2.setUsername(username);
        UserInfo selectOne = userInfoService.selectOne(user2);
		if (selectOne != null) {
			return BaseResult.fail("该用户已存在");
		}
		user.setId(UUIDUtils.getUUID());
        userInfoService.insert(user);
        UserInfo selectOne2 = userInfoService.selectOne(user);
		return BaseResult.success(selectOne2);
	}

}

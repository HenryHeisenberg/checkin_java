package cn.ssm.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

//	@Autowired
//	UserService userService;
//
//	@PostMapping("/login")
//	public Object login(User user) {
//		String username = user.getUsername();
//		String password = user.getPassword();
//		if (StringUtils.isAnyBlank(username, password)) {
//			return BaseResult.fail("账号或密码为空");
//		}
//		User selectOne = userService.selectOne(user);
//		if (selectOne != null) {
//			return BaseResult.success(selectOne);
//		}
//		return BaseResult.fail("账号或密码错误");
//	}
//
//	@PostMapping("/update")
//	public Object update(String id, String old, String newPassword) {
//		if (id == null || StringUtils.isAnyBlank(old, newPassword)) {
//			return BaseResult.fail("缺少参数");
//		}
//		User selectByKey = userService.selectByKey(id);
//		String password = selectByKey.getPassword();
//		if (!password.equals(old)) {
//			return BaseResult.fail("原密码错误");
//		}
//		selectByKey.setPassword(newPassword);
//		userService.updateByPrimaryKeySelective(selectByKey);
//		return BaseResult.success(selectByKey);
//	}
//
//	@PostMapping("/register")
//	public Object register(User user) {
//		String username = user.getUsername();
//		String password = user.getPassword();
//		if (StringUtils.isAnyBlank(username, password)) {
//			return BaseResult.fail("账号或密码为空");
//		}
//		User user2 = new User();
//		user2.setUsername(username);
//		User selectOne = userService.selectOne(user2);
//		if (selectOne != null) {
//			return BaseResult.fail("该用户已存在");
//		}
//		userService.insert(user);
//		User selectOne2 = userService.selectOne(user);
//		return BaseResult.success(selectOne2);
//	}

}

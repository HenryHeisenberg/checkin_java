package cn.ssm.shiro;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import cn.ssm.Enum.ShiroEnum;
import cn.ssm.model.WxUser;

@RestController
// @CrossOrigin(origins = "*", maxAge = 3600)
public class ShiroController {

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;// SpringMVC在启动的时候将所有的有贴请求映射的@RequestMapping的方法收集起来
	@Autowired
	private DefaultWebSecurityManager securityManager;
	@Autowired
	private WxUserRealm wxUserRealm;

	/**
	 * 登出方法
	 * 
	 * @param User
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public Map<String, Object> logout() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		if (principal != null) {
			subject.logout();
			returnMap.put("status", "success");
			returnMap.put("data", "退出成功！");
			return returnMap;
		}
		returnMap.put("status", "fail");
		returnMap.put("data", "退出失败！");
		return returnMap;
	}

	/**
	 * 获取用户信息方法
	 * 
	 * @param User
	 * @return
	 */
	@GetMapping(value = "/getUserInfo")
	public Map<String, Object> getUserInfo() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		WxUser user = (WxUser) session.getAttribute(ShiroEnum.SESSION_USER_INFO.getCode());
		returnMap.put("status", "success");
		returnMap.put("data", user);
		return returnMap;
	}

	/**
	 * 认证方法
	 * 
	 * @param User
	 * @return
	 */
	@RequestMapping("/unauth")
	public Map<String, Object> unauth() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", "没有认证！");
		returnMap.put("status", "unauth");
		return returnMap;
	}

	/**
	 * 权限方法
	 * 
	 * @param User
	 * @return
	 */
	@RequestMapping("/unauthorized")
	public Map<String, Object> unauthorized() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", "没有权限！");
		returnMap.put("status", "unauthorized");
		return returnMap;
	}

	/**
	 * 清除所有用户的权限
	 * 
	 */
	@GetMapping("/clearAllAuthorizationInfo")
	@RequiresRoles("admin")
	public Object clearAllAuthorizationInfo() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		wxUserRealm.clearAllAuthz();
		return returnMap;
	}

}

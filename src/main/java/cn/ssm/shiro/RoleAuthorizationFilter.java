package cn.ssm.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import com.alibaba.fastjson.JSONObject;

/**
 * 使用注解的情况貌似不会进入这个过滤器，后续再看
 * 
 */
public class RoleAuthorizationFilter extends AuthorizationFilter {

	/**
	 * isAccessAllowed(定义判断是否有权限的规则),判断角色是否具备访问要求
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		Subject subject = getSubject(request, response);
		String[] rolesArray = (String[]) mappedValue;

		if (rolesArray == null || rolesArray.length == 0) {
			// no roles specified, so nothing to check - allow access.
			return true;
		}

		Set<String> roles = CollectionUtils.asSet(rolesArray);
		for (String role : roles) {
			if (subject.hasRole(role)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "unauthorization");
		jsonObject.put("returnMsg", "授权失败！");
		PrintWriter out = null;
		HttpServletResponse res = (HttpServletResponse) response;
		try {
			res.setCharacterEncoding("UTF-8");
			res.setContentType("application/json");
			out = response.getWriter();
			out.println(jsonObject);
		} catch (Exception e) {
		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}
		}
		return false;
	}

	/**
	 * 防止springBoot自动注册导致自己注册的失效，此处是使springBoot自动注册的失效
	 */
	/*
	 * @Bean public FilterRegistrationBean
	 * shiroRolesFilterRegistration(RoleAuthorizationFilter filter) {
	 * FilterRegistrationBean registration = new FilterRegistrationBean(filter);
	 * registration.setEnabled(false); return registration; }
	 */

}

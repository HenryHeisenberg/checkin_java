package cn.ssm.shiro;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * shiro表单认证过滤器，认证成功与失败都会进入这个过滤器，实际收到可以在这里实现登录的
 * 
 * @author 7-81
 *
 */
public class MyAuthenticationFilter extends FormAuthenticationFilter {

	private Logger logger = LoggerFactory.getLogger(MyAuthenticationFilter.class);

	/**
	 * 进入onAccessDenied（区分ajax和普通请求，控制跳转返回格式）就已经是拒绝了，这里做拒绝后的处理，或者首次登陆的处理
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		/************ 登录失败处理 ***************/
		logger.warn("=== login fail : unauthorization ===");// 登录失败
		/************ 登录失败处理 ***************/
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "unauth");
		jsonObject.put("returnMsg", "认证失败！");
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
		// 返回false意思是不用再做处理
		return false;
	}

	/**
	 * 登录成功之后（登录之后跳转到loginurl），做的事情（setsession）
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.onLoginSuccess(token, subject, request, response);
	}

	/**
	 * 处理登录失败后的处理（ajax请求返回json格式（里面带有自定义拒绝标志信息,和错误代码），普通请求返回拒绝页面）
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		// TODO Auto-generated method stub
		return super.onLoginFailure(token, e, request, response);
	}

	/**
	 * 防止springBoot自动注册导致自己注册的失效，此处是使springBoot自动注册的失效
	 */
	/*
	 * @Bean public FilterRegistrationBean
	 * shiroLoginFilteRegistration(MyAuthenticationFilter filter) {
	 * FilterRegistrationBean registration = new FilterRegistrationBean(filter);
	 * registration.setEnabled(false); return registration; }
	 */

}

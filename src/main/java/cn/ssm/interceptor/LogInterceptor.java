package cn.ssm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.ssm.utils.util.WebUtils;

/**
 * 日志拦截器
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

	// private static final String OPENID = "openId";
	private static final String REMOTEHOST = "RemoteHost";
	private static final String SERVLETPATH = "servletPath";

	private static final transient Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String servletPath = request.getServletPath();
		// 放OPENID
		MDC.put(REMOTEHOST, WebUtils.getRemoteHost(request));
		MDC.put(SERVLETPATH, servletPath);
		logger.info(servletPath);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// logger.info("--------------处理请求完成后视图渲染之前的处理操作---------------");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 其他逻辑代码
		// logger.info("---------------视图渲染之后的操作-------------------------");
		// 最后执行MDC删除
		MDC.remove(REMOTEHOST);
		MDC.remove(SERVLETPATH);
	}
}
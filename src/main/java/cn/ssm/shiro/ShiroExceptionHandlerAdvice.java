package cn.ssm.shiro;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常拦截器（这个类主要拦截shiro的异常）
 * 
 * @author 7-81
 *
 */
@ControllerAdvice
public class ShiroExceptionHandlerAdvice {

	/**
	 * 没有权限异常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseBody
	public Map<String, Object> handler(Exception e) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "unauthorized");
		returnMap.put("data", e.toString());
		returnMap.put("msg", "没有权限");
		return returnMap;
	}

}

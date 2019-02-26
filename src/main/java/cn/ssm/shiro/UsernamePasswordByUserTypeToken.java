package cn.ssm.shiro;

import java.io.Serializable;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 对UsernamePasswordToken进行了简单的封装，用于判断该用户是需要用哪个realm进行登录
 * 
 * @author 7-81
 *
 */
public class UsernamePasswordByUserTypeToken extends UsernamePasswordToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 用户类型 admin:后台管理员 user:普通用户
	 */
	private String userType;

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public UsernamePasswordByUserTypeToken(String username, String password, String userType) {
		super(username, password);
		this.userType = userType;
	}
}

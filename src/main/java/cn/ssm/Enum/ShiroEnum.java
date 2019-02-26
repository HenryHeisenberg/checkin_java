package cn.ssm.Enum;

public enum ShiroEnum {

	STRINGFORMAT("shiro_%s", "加前缀"), SESSION_USER_INFO("userInfo", "session中存放的用户信息"), WX_USER("WxUser",
			"登录类型：微信用户"), ADMIN("Admin", "登录类型：管理员");

	private String code;
	private String name;

	private ShiroEnum(String code, String name) {
		this.setCode(code);
		this.setName(name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

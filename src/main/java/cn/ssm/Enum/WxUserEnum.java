package cn.ssm.Enum;

public enum WxUserEnum {


	ADMIN("admin", "管理员"), ORDINARY("ordinary", "普通用户"), UNNORMAL("0", "不正常用户"), NORMAL("1", "正常用户"), BANNED("2",
			"禁止用户"), OPENID("openId", "openId"), SESSION_KEY("session_key", "session_key");

	private String code;
	private String name;

	private WxUserEnum(String code, String name) {
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

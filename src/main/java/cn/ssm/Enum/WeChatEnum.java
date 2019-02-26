package cn.ssm.Enum;

public enum WeChatEnum {

	STRINGFORMAT("wechat_%s", "加前缀"),
	ACCESS_TOKEN("access_token", "access_token:获取到的凭证"), 
	EXPIRES_IN("expires_in", "凭证有效时间，单位：秒"), 
	APPID("APPID", "APPID"), 
	SECRET("SECRET", "SECRET"), 
	MY("my","小程序标识"), 
	ERROR_MSG("errormsg", "错误消息"), 
	ERROR_CODE("errcode", "错误码"), 
	WECHAT_KEY_PREFIX("WeChar:", "微信前缀");
	

	private String code;
	private String name;

	private WeChatEnum(String code, String name) {
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

package cn.ssm.Enum;

public enum HttpEnum {

	CONTENT("Content", "内容"), CODE("Code", "验证码"), COOKIE("Cookie", "Cookie"), HOST("Host", "Host"), UA("User-Agent",
			"User-Agent"), CONTENTTYPE("Content-Type",
					"Content-Type"), REFERER("Referer", "Referer"), CONNECTION("Connection", "Connection");

	private String code;
	private String name;

	private HttpEnum(String code, String name) {
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

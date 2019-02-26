package cn.ssm.model.dto;

import com.alibaba.fastjson.JSONArray;

public class WxUserDTO {

	private String encryptedData;
	private String errMsg;
	private String iv;
	private String rawData;
	private String signature;
	private UserInfoDTO userInfo;

	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public UserInfoDTO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfoDTO userInfo) {
		this.userInfo = userInfo;
	}

	public static WxUserDTO JSON2Object(String json) {
		WxUserDTO parseObject = JSONArray.parseObject(json, WxUserDTO.class);
		return parseObject;
	}

}

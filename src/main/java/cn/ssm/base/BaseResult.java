package cn.ssm.base;

/**
 * 统一返回结果类
 */
public class BaseResult {

	/**
	 * 状态码
	 */
	public static final String SUCCESS_STATUS = "success";

	private static final String FAIL_STATUS = "fail";

	/**
	 * 消息
	 */
	public static final String SUCCESS_MSG = "success";

	/**
	 * 状态码：success成功，其他为失败
	 */
	public String status;

	/**
	 * 成功为success，其他为失败原因
	 */
	public String msg;

	/**
	 * 数据结果集
	 */
	public Object data;

	public BaseResult(String status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static BaseResult success(Object data) {
		return new BaseResult(SUCCESS_STATUS, null, data);
	}

	public static BaseResult fail(String msg) {
		return new BaseResult(FAIL_STATUS, msg, null);
	}

}

package util;

public class SimpleResult {

	/**
	 * 返回结果成功标识
	 */
	private boolean success;

	private long resultCode=-1l;

	private String message;
	/**
	 * 返回信息主体数据
	 */
	private Object data;

	/**
	 *构造方法
	 */
	public SimpleResult(boolean success, Object data){
		this.success = success;
		this.data = data;
	}

	/**
	 *构造方法
	 */
	public SimpleResult( boolean success,String message, Object data){
		this.message = message;
		this.success = success;
		this.data = data;
	}

	public SimpleResult(boolean success,String message,long resultCode, Object data){
		this.message = message;
		this.resultCode = resultCode;
		this.success = success;
		this.data = data;
	}

	/**
	 * 返回成功信息
	 * @param data 信息数据
	 * @return 成功信息
	 */
	public static SimpleResult successResult(Object data){
		return new SimpleResult(true,data);
	}
	
	/**
	 * 返回成功信息
	 * @param data 信息数据
	 * @return 成功信息
	 */
	public static SimpleResult successResult(String message, Object data){
		return new SimpleResult(true,message,data);
	}
	
	public static SimpleResult successResult(long resultCode, String message, Object data){
		return new SimpleResult(true, message,resultCode,data);
	}
	
	/**
	 * 返回失败信息
	 * @param data 信息数据
	 * @return 失败信息
	 */
	public static SimpleResult failureResult(Object data){
		return new SimpleResult(false,data);
	}
	
	/**
	 * 返回成功信息
	 * @param data 信息数据
	 * @return 成功信息
	 */
	public static SimpleResult failureResult(String message, Object data){
		return new SimpleResult(false,message,data);
	}
	
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

package util;


import net.sf.json.JSONObject;

/**
 * @author by zhangchanghong on 15/12/8.
 */
public class JsonResult {
    private static final String SUC_MSG = "ok";
    private boolean success;
    private int responseCode;
    private String message;
    private Object data;
    private String jsonStr;


    public JsonResult(boolean success, int responseCode, String message, Object data) {
        this.success = success;
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
        
		JSONObject ruturnJson = new JSONObject();
		ruturnJson.accumulate("success", success);
		ruturnJson.accumulate("data", data);
		ruturnJson.accumulate("message", message);
		ruturnJson.accumulate("responseCode", responseCode);
		this.jsonStr = ruturnJson.toString();
    }

    public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public static JsonResult failed() {
        return new JsonResult(false, 0, "", null);
    }

    public static JsonResult failed(String message){
        return new JsonResult(false, -2, message, null);
    }

    public static JsonResult failed(int returnCode, String message, Object data) {
        return new JsonResult(false, returnCode, message, data);
    }

    public static JsonResult failed(int returnCode, String message){
        return new JsonResult(false, returnCode, message, null);
    }

    public static JsonResult success(){
        return new JsonResult(true, 0, SUC_MSG, null);
    }

    public static JsonResult success(Object data){
        return new JsonResult(true, 0, SUC_MSG, data);
    }

    public static JsonResult success(String message){
        return new JsonResult(true, 0, message, null);
    }

    public static JsonResult success(String message, Object data){
        return new JsonResult(true, 0, message, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

package vo;

import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.util.CommonUtil;

public class OpenApiPo {
	/** 获取access_token填写client_credential **/
	private String grantType;
	/** 第三方用户唯一凭证 **/
	private String appid;
	/** 第三方用户唯一凭证密钥，即appsecret **/
	private String secret;
	
	public OpenApiPo(HttpRequest httpRequest) {
		super();
		try {
			grantType = CommonUtil.getParam(httpRequest, "grant_type");
			appid = CommonUtil.getParam(httpRequest, "appid");
			secret = CommonUtil.getParam(httpRequest, "secret");			
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (grantType == null || grantType.isEmpty())
			grantType = "";
		if (appid == null || appid.isEmpty())
			appid = "";
		if (secret == null || secret.isEmpty())
			secret = "";
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return "";
	}

}

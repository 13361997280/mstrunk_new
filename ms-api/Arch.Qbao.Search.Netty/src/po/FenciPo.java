package po;

public class FenciPo {
	private String token;
	private String offset;
	private String type;

	public FenciPo(String token, String offset, String type) {
		super();
		this.token = token;
		this.offset = offset;
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

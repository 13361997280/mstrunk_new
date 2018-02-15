package util;

public class TokenData {

	// 系统时间
	private Long st;

	// 用户名字
	private String uId;
	
	// 过期时间
	private Long epr;
	
	// 随机数
	private Integer r;

	public Long getSt() {
		return st;
	}

	public void setSt(Long st) {
		this.st = st;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public Long getEpr() {
		return epr;
	}

	public void setEpr(Long epr) {
		this.epr = epr;
	}

	public Integer getR() {
		return r;
	}

	public void setR(Integer r) {
		this.r = r;
	}

	
}

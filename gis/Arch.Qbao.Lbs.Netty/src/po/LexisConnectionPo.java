package po;

import java.io.Serializable;

public class LexisConnectionPo implements Serializable {
	private static final long serialVersionUID = 8833112696801905822L;
	/**
	 * wordId
	 * **/
	private String wordId;
	/**
	 * 词，一个词可以出现在多个行业字典中,字典合并时只会留下一个
	 * **/
	private String wordText;
	/**
	 * 关联词
	 * **/
	private String wordconnect;
	/**
	 * typeId
	 * **/
	private String typeId;
	/**
	 * status
	 * **/
	private String status;
	/**
	 * pinyin
	 * **/
	private String pinyin;
	/**
	 * cityid
	 * **/
	private String cityid;
	/**
	 * 使用的频率
	 * **/
	private Integer freq;
	/**
	 * 所属项目字典编号
	 * **/
	private String sysNo;
	/**
	 * 项目权重
	 * **/
	private int sysWeight;
	/**
	 * 词的类型
	 * **/
	private String type;
	/**
	 * 词的类型权重
	 * **/
	private String typeWeight;
	/**
	 * 是否双向关联
	 * **/
	private String isbothway;
	/**
	 * 是否双向关联
	 * **/
	private String checkInDate;
	public String getWordId() {
		return wordId;
	}
	public void setWordId(String wordId) {
		this.wordId = wordId;
	}
	public String getWordText() {
		return wordText;
	}
	public void setWordText(String wordText) {
		this.wordText = wordText;
	}
	public String getWordconnect() {
		return wordconnect;
	}
	public void setWordconnect(String wordconnect) {
		this.wordconnect = wordconnect;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public Integer getFreq() {
		return freq;
	}
	public void setFreq(Integer freq) {
		this.freq = freq;
	}
	public String getSysNo() {
		return sysNo;
	}
	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}
	public int getSysWeight() {
		return sysWeight;
	}
	public void setSysWeight(int sysWeight) {
		this.sysWeight = sysWeight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeWeight() {
		return typeWeight;
	}
	public void setTypeWeight(String typeWeight) {
		this.typeWeight = typeWeight;
	}
	public String getIsbothway() {
		return isbothway;
	}
	public void setIsbothway(String isbothway) {
		this.isbothway = isbothway;
	}
	public String getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

}

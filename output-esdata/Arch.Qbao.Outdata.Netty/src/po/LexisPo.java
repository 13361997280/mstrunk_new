package po;

import java.io.Serializable;
import java.util.Date;

public class LexisPo implements Serializable {
	private static final long serialVersionUID = 8833112696801905822L;

	/**
	 * id
	 * **/
	private int wordId;
	/**
	 * 词，一个词可以出现在多个行业字典中,字典合并时只会留下一个
	 * **/
	private String wordText;
	/**
	 * typeId
	 * **/
	private int typeId;
	/**
	 * 经纬度
	 * **/
	private float LON;
	/**
	 * 经纬度
	 * **/
	private float LAT;
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
	private int cityId;
	/**
	 * 使用的频率
	 * **/
	private long freq;
	/**
	 * 本次运算的增加频率数目
	 * **/
	private long updatefreq;
	/**
	 * 频率更新比率:updatefreq/freqX100%
	 * **/
	private float freqPerent;
	/**
	 * 所属项目字典编号
	 * **/
	private int sysNo;
	/**
	 * 项目权重
	 * **/
	private int sysWeight;
	/**
	 * 词的类型
	 * **/
	private String type;
	/**
	 * 类型的权重
	 * **/
	private int typeWeight;

	/**
	 * 更新时间
	 * **/
	private Date checkInDate;

	
	
	public LexisPo(int wordId) {
		super();
		this.wordId = wordId;
	}

	public LexisPo(String wordText, Date checkInDate) {
		super();
		this.wordText = wordText;
		this.checkInDate = checkInDate;
	}

	public LexisPo(int wordId, String wordText, int typeId, float lON, float lAT, String status,
			String pinyin, int cityId, long freq, int sysNo, int sysWeight, String type,
			int typeWeight, Date checkInDate) {
		super();
		this.wordId = wordId;
		this.wordText = wordText;
		this.typeId = typeId;
		LON = lON;
		LAT = lAT;
		this.status = status;
		this.pinyin = pinyin;
		this.cityId = cityId;
		this.freq = freq;
		this.sysNo = sysNo;
		this.sysWeight = sysWeight;
		this.type = type;
		this.typeWeight = typeWeight;
		this.checkInDate = checkInDate;
	}

	public LexisPo() {

	}

	public int getWordId() {
		return wordId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public String getWordText() {
		return wordText;
	}

	public void setWordText(String wordText) {
		this.wordText = wordText;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public float getLON() {
		return LON;
	}

	public void setLON(float lON) {
		LON = lON;
	}

	public float getLAT() {
		return LAT;
	}

	public void setLAT(float lAT) {
		LAT = lAT;
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

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public long getFreq() {
		return freq;
	}

	public void setFreq(long freq) {
		this.freq = freq;
	}

	public int getSysNo() {
		return sysNo;
	}

	public void setSysNo(int sysNo) {
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

	public int getTypeWeight() {
		return typeWeight;
	}

	public void setTypeWeight(int typeWeight) {
		this.typeWeight = typeWeight;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	
	public long getUpdatefreq() {
		return updatefreq;
	}

	public void setUpdatefreq(long updatefreq) {
		this.updatefreq = updatefreq;
	}

	public float getUpFreqPerent() {
		return freqPerent;
	}

	public void setUpFreqPerent(float upFreqPerent) {
		this.freqPerent = upFreqPerent;
	}

	@Override
	public String toString() {
		return "LexisPo [wordId=" + wordId + ", wordText=" + wordText + ", typeId=" + typeId
				+ ", LON=" + LON + ", LAT=" + LAT + ", status=" + status + ", pinyin=" + pinyin
				+ ", cityId=" + cityId + ", freq=" + freq +  ", updatefreq=" + updatefreq + ", preqPerent=" + freqPerent + ", sysNo=" + sysNo + ", sysWeight="
				+ sysWeight + ", type=" + type + ", typeWeight=" + typeWeight + ", checkInDate="
				+ checkInDate.toLocaleString()  + "]";
	}

}

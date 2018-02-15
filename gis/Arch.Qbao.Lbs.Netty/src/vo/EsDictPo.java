package vo;

import com.alibaba.fastjson.JSONArray;

public class EsDictPo {

	private Integer id;// 标签id

	private String parentName;// 所属类别

	private String labelName;// 标签名称

	private JSONArray option;// 选项组合json字段

	private String grafType;// 图形结构

	private String labelId;// 标签code

	private Integer labelType;// 标签类型

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JSONArray getOption() {
		return option;
	}

	public void setOption(JSONArray option) {
		this.option = option;
	}

	public String getGrafType() {
		return grafType;
	}

	public void setGrafType(String grafType) {
		this.grafType = grafType;
	}

	public Integer getLabelType() {
		return labelType;
	}

	public void setLabelType(Integer labelType) {
		this.labelType = labelType;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	@Override
	public String toString() {
		return "EsDictPo [id=" + id + ", parentName=" + parentName + ", labelName=" + labelName + ", option=" + option
				+ ", grafType=" + grafType + ", labelId=" + labelId + ", labelType=" + labelType + "]";
	}

}

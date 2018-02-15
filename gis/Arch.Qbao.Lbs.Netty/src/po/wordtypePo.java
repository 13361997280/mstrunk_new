package po;

import java.io.Serializable;

public class wordtypePo implements Serializable {
	private static final long serialVersionUID = -1674164995763031909L;
	/**
	 * typeId
	 * **/
	private int typeId;
	/**
	 * typeName
	 * **/
	private String typeName;
	/**
	 * sysScore
	 * **/
	private int sysScore;
	/**
	 * searchType
	 * **/
	private String searchType;
	
	

	public wordtypePo() {
		super();
	}

	public wordtypePo(int typeId) {
		super();
		this.typeId = typeId;
	}

	

	public wordtypePo(int typeId, String typeName, int sysScore, String searchType) {
		super();
		this.typeId = typeId;
		this.typeName = typeName;
		this.sysScore = sysScore;
		this.searchType = searchType;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getSysScore() {
		return sysScore;
	}

	public void setSysScore(int sysScore) {
		this.sysScore = sysScore;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	@Override
	public String toString() {
		return this.getTypeId() + this.getTypeName() + this.getSysScore() + this.getSearchType();
	}
}

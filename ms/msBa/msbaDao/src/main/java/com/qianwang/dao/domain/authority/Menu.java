package com.qianwang.dao.domain.authority;

import java.io.Serializable;
import java.util.List;
/**
 * 系统菜单
 * @author zou
 *
 */
public class Menu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6087673501229028809L;
	/**
	 * 菜单id
	 */
	private long id;
	/**
	 * 菜单名称
	 */
	private String text;
	
	/**
	 * 菜单链接
	 */
	private String url;
	/**
	 * 菜单父节点id
	 */
	private long parentId;
	
	/**
	 * 菜单是否是子节点
	 */
	private boolean leaf;
	
	/**
	 * 节点是否选中
	 */
	private Boolean checked;
	
	/**
	 * 节点是否加载
	 */
	private boolean loaded ;

	/**
	 * 子节点集合
	 */
	private List<Menu> children;
	
	
	private int sort;
	 
	/**
	 * @return the sort
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}

	public Menu(){
		
	}
	
	public Menu(long id,long parentId,String text){
		this.id = id;
		this.parentId = parentId;
		this.text = text;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the checked
	 */
	public Boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the children
	 */
	public List<Menu> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	/**
	 * @return the loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * @param loaded the loaded to set
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

}

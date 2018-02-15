package com.qianwang.mapper.authority;

import java.util.List;
import java.util.Map;

import com.qianwang.dao.domain.authority.Authority;

/**
 * 权限管理数据映射
 * @author zou
 *
 */
public interface AuthorityMapper {
	/**
	 * 查询权数据集合
	 * @return
	 */
	List<Authority> findAuthorities(); 
	
	/**
	 * 新增权限记录
	 * @return
	 */
	void addAuthority(Authority authority); 
	
	/**
	 * 修改权限记录
	 * @return
	 */
	void updateAuthority(Authority authority); 
	
	int hasMenuAuthorities(Map<String,Object> param);
	
	/**
	 * 删除权限记录
	 * @return
	 */
	void delAuthority(String authorityCode); 
	
	/**
	 * 是否已存在相同编码的权限
	 * @param authority
	 * @return
	 */
	long hasAuthority(Authority authority);
}

package com.qianwang.service.authority;

import com.qianwang.dao.domain.authority.Authority;
import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.authority.MenuAuthority;

import java.util.List;

/**
 * 权限管理服务接口
 * 
 * @author zou
 * 
 */

public interface AuthorityService {
	/**
	 * 查询权限集合
	 * 
	 * @return
	 */
	List<Authority> findAuthorities();
	
	Boolean hasMenuAuthorities(String url, String authorityCode);
	
	Boolean hasMenus(String url);

	/**
	 * 新增权限记录
	 * 
	 * @return
	 */
	void addAuthority(Authority authority);

	/**
	 * 修改权限记录
	 * 
	 * @return
	 */
	void updateAuthority(Authority authority);

	/**
	 * 删除权限记录
	 * 
	 * @return
	 */
	void delAuthority(String authorityCode);
	
	/**
	 * 根据父节点查询子节点
	 * @param parentId
	 * @return
	 */
	List<Menu> findChildMenus(int parentId);
	
	/**
	 *保存菜单信息
	 * @param menu
	 * @return 菜单id
	 */
	long saveMenu(Menu menu);
	
	/**
	 * 根据菜单id删除菜单
	 * @param id
	 */
	void deleteMenu(Menu menu);
	
	/**
	 * 根据权限编码查询菜单树
	 * @param authorityCode
	 * @return json格式的树结构数据
	 */
	 List<Menu> findMenuTreeByAuthority(String authorityCode);
	 
	 /**
	 * 保存菜单权限关系
	 * @param menuAuthority
	 * @return
	 */
	 void saveMenuAuthority(MenuAuthority menuAuthority);
	 
	 /**
	 * 保存菜单权限关系
	 * @param nodes 以逗号分隔的菜单id字符串
	 * @param authorityCode权限编码
	 * @return
	 */
	 void saveMenuAuthority(String nodes, String authorityCode);
}

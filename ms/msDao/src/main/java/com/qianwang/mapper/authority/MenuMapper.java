package com.qianwang.mapper.authority;

import java.util.List;
import java.util.Map;

import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.authority.MenuAuthority;
import com.qianwang.mapper.BaseMapper;
/**
 * 菜单映射关系
 * @author zou
 *
 */
public interface MenuMapper extends BaseMapper{
	/**
	 * 根据父节点查询子节点
	 * @param parentId
	 * @return
	 */
	List<Menu> findChildMenus(int parentId);
	
	/**
	 * 新增菜单信息
	 * @param menu
	 * @return
	 */
	void addMenu(Menu menu);
	
	/**
	 * 修改菜单信息
	 * @param menu
	 * @return
	 */
	void updateMenu(Menu menu);
	
	int hasMenus(String url);
	
	/**
	 * 更新菜单是否是子节点
	 * @param id
	 */
	void updateMenuLeaf(Map<String,Object> param);
	
	/**
	 * 根据菜单id删除菜单
	 * @param id
	 * @return
	 */
	void deleteMenu(long id);
	
	/**
	 * 根据权限编码查询菜单树
	 * @param authorityCode
	 * @return
	 */
	List<Menu> findMenuTreeByAuthority(String authorityCode);
	
	/**
	 * 保存菜单权限关系
	 * @param menuAuthority
	 * @return
	 */
	void saveMenuAuthority(MenuAuthority menuAuthority);
	
	/**
	 *  根据菜单Id删除权限关系 
	 * @param menuId
	 */
	void deleteMenuAuthorityByMenu(long menuId);
	
	/**
	 *  根据权限编码删除菜单关系
	 * @param authorityCode
	 */
	void deleteMenuAuthorityByAuthority(String authorityCode);
	
	/**
	 * 查询菜单是否有子菜单
	 * @param id
	 * @return
	 */
	long countChildMenus(long id);
}

package com.qianwang.service.authority.impl;

import com.qianwang.dao.domain.authority.Authority;
import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.authority.MenuAuthority;
import com.qianwang.dao.util.IDUtil;
import com.qianwang.mapper.authority.AuthorityMapper;
import com.qianwang.mapper.authority.MenuMapper;
import com.qianwang.mapper.user.UsersMapper;
import com.qianwang.service.authority.AuthorityService;
import com.qianwang.util.json.JsonTreeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限管理服务接口实现类
 * @author zou
 *
 */
@Service("authorityService")
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityMapper authorityMapper;
	
	/**
	 * 菜单数据访问接口
	 */
	@Autowired
	private MenuMapper menuMapper;
	
	/**
	 * 菜单数据访问接口
	 */
	@Autowired
	private UsersMapper userMapper;
	
	/**
	 * 查询权限集合
	 * @return
	 */
	@Override
	public List<Authority> findAuthorities() {
		return authorityMapper.findAuthorities();
	}
	
	
	/**
	 * 新增权限记录
	 * @return
	 */
	@Override
	public void addAuthority(Authority authority){
		
		authorityMapper.addAuthority(authority);
	}
	
	/**
	 * 修改权限记录
	 * @return
	 */
	@Override
	public void updateAuthority(Authority authority){
		authorityMapper.updateAuthority(authority);
	}

	@Override
	public void delAuthority(String authorityCode) {

	}


//	/**
//	 * 是否已存在相同编码的权限
//	 * @param authority
//	 * @return
//	 */
//	private boolean hasAuthority(Authority authority){
//		return authorityMapper.hasAuthority(authority) > 0;
//	}
//	
	/**
	 * 根据父节点查询子节点
	 * @param parentId
	 * @return
	 */
	@Override
	public List<Menu> findChildMenus(int parentId){
		return menuMapper.findChildMenus(parentId);
	}
	
	
	/**
	 * 新增菜单信息
	 * @param menu
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public long saveMenu(Menu menu){
		if(menu.getId() > 0){
			menuMapper.updateMenu(menu);
		}else{
			menu.setLeaf(true);
			menu.setId(IDUtil.getNextLongID());
			menuMapper.addMenu(menu);
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("leaf", false);
			param.put("id", menu.getParentId());
			menuMapper.updateMenuLeaf(param);
		}
		return menu.getId();
	}
	
	/**
	 * 根据菜单id删除菜单
	 * @param
	 * @return
	 */
	@Override
	public void deleteMenu(Menu menu){
		menuMapper.deleteMenu(menu.getId());
		menuMapper.deleteMenuAuthorityByMenu(menu.getId());
		Map<String,Object> param = new HashMap<String,Object>();
		if(menuMapper.countChildMenus(menu.getParentId()) == 0){
			param.put("leaf", true);
			param.put("id", menu.getParentId());
			menuMapper.updateMenuLeaf(param);
		}
	}
	
	/**
	 * 根据权限编码查询菜单树
	 * @param authorityCode
	 * @return
	 */
	@Override
	public List<Menu> findMenuTreeByAuthority(String authorityCode){
		List<Menu> l = menuMapper.findMenuTreeByAuthority(authorityCode);
		JsonTreeUtil.formatTreeList(l, "id", "parentId");
		return l;
	}
	
	
	/**
	 * 保存菜单权限关系
	 * @param menuAuthority
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void saveMenuAuthority(MenuAuthority menuAuthority){
		menuMapper.saveMenuAuthority(menuAuthority);
	}
	
	@Override
	public Boolean hasMenuAuthorities(String url, String authorityCode) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("url", url);
		param.put("authorityCode", authorityCode);
		return authorityMapper.hasMenuAuthorities(param) > 0;
	}

	
	@Override
	public Boolean hasMenus(String url){
		return menuMapper.hasMenus(url) > 0;
	}
	
	/**
	 * 保存菜单权限关系
	 * @param nodes 以逗号分隔的菜单id字符串
	 * @param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void saveMenuAuthority(String nodes,String authorityCode){
		menuMapper.deleteMenuAuthorityByAuthority(authorityCode);
		if(StringUtils.isEmpty(nodes)){
			return;
		}
		String[] nodeArr = nodes.split(",");
		for (String node : nodeArr) {
			MenuAuthority ma = new MenuAuthority();
			ma.setAuthorityCode(authorityCode);
			ma.setMenuId(Integer.parseInt(node));
			menuMapper.saveMenuAuthority(ma);
		}
	 } 
}

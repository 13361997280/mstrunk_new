package com.qianwang.mapper.user;

import java.util.List;
import java.util.Map;

import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.user.UserAuthority;
import com.qianwang.dao.domain.user.UserView;


/**
 * 用户数据库映射接口
 * @author zou
 *
 */
public interface UserMapper {
	/**
	 * 返回所有系统用户
	 * 注：数据量大的时候，查询需要优化
	 * @return
	 */
	List<UserView> selectAllUser();
	
	/**
	 * 保存用户信息
	 * @param user
	 */
	void insertUser(UserView user);
	
	/**
	 * 更新用户信息
	 * @param user
	 */
	void updateUser(UserView user);
	
	/**
	 * 根据用户名加载用户
	 * @param username
	 * @return
	 */
	public UserView loadUser(String username);
	
	
	/**
	 * 根据用户名删除用户
	 * @param username
	 * @return
	 */
	void deleteUser(String username);
	
	/**
	 * 根据用户名删除用户权限
	 * @param username
	 * @return
	 */
	void deleteUserAuthority(String username);
	
	
	/**
	 * 新增用户权限
	 * @param username
	 * @return
	 */
	void addUserAuthority(UserAuthority userAuthority);
	
	/**
	 * 根据权限名删除用户权限
	 * @param authorityCode
	 * @return
	 */
	void deleteUserAuthorityByAuthority(String authorityCode);
	
	/**
	 * 根据用户名查询权限菜单
	 * @param username用户名，parentId父节点
	 * @return
	 */
	List<Menu> findUserMenu(Map<String,Object> param);

	void bindMobile(UserView user);

	List<Map<String,String>> loadUserQbAccountInfo(String mobile);
}

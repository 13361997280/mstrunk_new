package com.qianwang.web.controller.authority;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianwang.dao.domain.authority.Authority;
import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.service.authority.AuthorityService;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;

/**
 * 权限管理控制器
 */
@Controller
@RequestMapping("/authority/**")
public class AuthorityController extends BaseController {
	/**
	 * 权限管理service
	 */
	@Autowired
	private AuthorityService authorityService;
	
	/**
	 * 查询系统所有权限
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findAuthorities")
	@ResponseBody
	public AjaxResult findAuthorities() {
		return AjaxResult.successResult(authorityService.findAuthorities());
	}
	
	/**
	 * 新增权限记录
	 * @return
	 */
	@RequestMapping(value = "/addAuthority")
	@ResponseBody
	public AjaxResult addAuthority(Authority authority){
		try {
			authority.setCreateTime(new Date());
			authority.setCreateUser(getCurrentUsername());
			authorityService.addAuthority(authority);
		} catch (Exception e) {
			return AjaxResult.failureResult(e.getMessage());
		}
		return AjaxResult.successResult("保存成功");
	}
	
	/**
	 * 修改权限记录
	 * @return
	 */
	@RequestMapping(value = "/updateAuthority")
	@ResponseBody
	public AjaxResult updateAuthority(Authority authority){
		authorityService.updateAuthority(authority);
		return AjaxResult.successResult("保存成功");
	}
	
	/**
	 * 删除权限记录
	 * 
	 */
	@RequestMapping(value = "/delAuthority")
	@ResponseBody
	public AjaxResult delAuthority(String authorityCode){
		authorityService.delAuthority(authorityCode);
		return AjaxResult.successResult("删除成功");
	}
	
	
	/**
	 * 查询菜单记录
	 * 
	 */
	@RequestMapping(value = "/findChildMenus")
	@ResponseBody
	public AjaxResult findChildMenus(int parentId){
		return AjaxResult.successResult(authorityService.findChildMenus(parentId));
	}
	
	/**
	 * 保存菜单记录
	 * 
	 */
	@RequestMapping(value = "/saveMenu")
	@ResponseBody
	public AjaxResult saveMenu(Menu menu){
		return AjaxResult.successResult(authorityService.saveMenu(menu));
	}
	
	/**
	 * 删除菜单记录
	 * 
	 */
	@RequestMapping(value = "/delMenu")
	@ResponseBody
	public AjaxResult delMenu(Menu menu){
		authorityService.deleteMenu(menu);
		return AjaxResult.successResult("删除成功");
	}
	
	/**
	 * 根据权限编码查询菜单树
	 * @param authorityCode
	 * @return
	 */
	@RequestMapping(value = "/findMenuTreeByAuthority")
	@ResponseBody
	public AjaxResult findMenuTreeByAuthority(String authorityCode){
		return AjaxResult.successResult(authorityService.findMenuTreeByAuthority(authorityCode));
	}
	
	/**
	 * 保存菜单权限关系
	 * @param nodes 以逗号分隔的菜单id字符串
	 * @param authorityCode权限编码
	 * @return
	 */
	@RequestMapping(value = "/saveMenuAuthority")
	@ResponseBody
	public AjaxResult saveMenuAuthority(String nodes,String authorityCode){
		authorityService.saveMenuAuthority(nodes, authorityCode);
		return AjaxResult.successResult("保存成功");
	}

}

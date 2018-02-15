package com.qianwang.web.controller.assistant;

import com.qianwang.dao.domain.assistant.Authorities;
import com.qianwang.dao.domain.assistant.AuthoritiesGroup;
import com.qianwang.dao.domain.assistant.Users;
import com.qianwang.service.assistant.AuthoritiesGroupService;
import com.qianwang.service.assistant.AuthoritiesService;
import com.qianwang.service.assistant.UsersService;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * Created by chenghaijiang on 17/08/21.
 */
@Controller
@RequestMapping(value = "/authorities")
public class AuthoritiesController extends BaseController {

    @Autowired
    AuthoritiesService authoritiesService;
    @Autowired
    AuthoritiesGroupService groupService;
    @Autowired
    UsersService usersService;

    /**
     * 查询
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list() {
        List<Authorities> list = authoritiesService.getAuthorities();
        int count = authoritiesService.getAuthoritiesCount();
        ModelAndView view = new ModelAndView("../ms/authorities/list.jsp");
        view.addObject("count",count);
        view.addObject("list",list);
        return view;
    }

    /**
     * 保存
     * @param id
     * @param userid
     * @param authority
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(required = false, defaultValue = "0") Integer id,
                                      @RequestParam(required = false, defaultValue = "1") Integer userid,
                                      @RequestParam(required = false, defaultValue = "1") String authority) {
    	Authorities entity = new Authorities();
        entity.setId(id);
        entity.setUserid(userid);
        entity.setAuthority(authority);
        if(id==0){
            authoritiesService.addAuthorities(entity);
        }else{
            authoritiesService.editAuthorities(entity);
        }

        ModelAndView view = new ModelAndView("redirect:list.do");
        return view;
    }
    /**
     * 修改页面跳转
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView add() {
        Authorities entity = new Authorities();
        entity.setId(0);
        List<AuthoritiesGroup> list = groupService.getList();
        ModelAndView view = new ModelAndView("../ms/authorities/edit.jsp");
        view.addObject("entity",entity);
        view.addObject("list",list);
        view.addObject("userList",usersService.getListForAuth(null));
        return view;
    }
    /**
     * 修改页面跳转
     * @param id
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam Integer id) {
        Authorities entity = authoritiesService.getAuthorities(id);
        List<AuthoritiesGroup> list = groupService.getList();
        ModelAndView view = new ModelAndView("../ms/authorities/edit.jsp");
        view.addObject("entity",entity);
        view.addObject("list",list);
        view.addObject("userList",usersService.getListForAuth(entity.getUserid()));
        return view;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "del",method = RequestMethod.GET)
    public ModelAndView removeAuthorities(@RequestParam Integer id) {
        authoritiesService.removeAuthorities(id);
        ModelAndView view = new ModelAndView("redirect:list.do");
        return view;
    }

}

package com.qianwang.web.controller.assistant;

import com.qianwang.dao.domain.assistant.Authorities;
import com.qianwang.dao.domain.assistant.AuthoritiesGroup;
import com.qianwang.dao.domain.assistant.AuthoritiesResource;
import com.qianwang.service.assistant.AuthoritiesGroupService;
import com.qianwang.service.assistant.AuthoritiesResourceService;
import com.qianwang.service.assistant.AuthoritiesService;
import com.qianwang.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * Created by chenghaijiang on 17/08/21.
 */
@Controller
@RequestMapping(value = "/authoritiesgroup")
public class AuthoritiesGroupController extends BaseController {

    @Autowired
    AuthoritiesGroupService service;
    @Autowired
    AuthoritiesResourceService resource;

    /**
     * 查询
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getList() {
        List<AuthoritiesGroup> list = service.getList();
        int count = service.getCount();
        ModelAndView view = new ModelAndView("../ms/authoritiesgroup/list.jsp");
        view.addObject("count",count);
        view.addObject("list",list);
        return view;
    }

    /**
     * 保存
     * @param id
     * @param name
     * @param resouceIds
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(required = false, defaultValue = "0") Integer id,
                                      @RequestParam(required = false, defaultValue = "1") String name,
                                      @RequestParam(required = false, defaultValue = "1") String resouceIds) {
    	AuthoritiesGroup entity = new AuthoritiesGroup();
        entity.setId(id);
        entity.setName(name);
        entity.setResouceIds(resouceIds);
        if(id==0){
            service.add(entity);
        }else{
            service.edit(entity);
        }
        ModelAndView view = new ModelAndView("redirect:list.do");
        return view;
    }
    /**
     * 修改页面跳转
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView addAuthorities() {
        AuthoritiesGroup entity = new AuthoritiesGroup();
        entity.setId(0);
        ModelAndView view = new ModelAndView("../ms/authoritiesgroup/edit.jsp");
        view.addObject("entity",entity);
        view.addObject("list",resource.getList());
        return view;
    }
    /**
     * 修改页面跳转
     * @param id
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView editAuthorities(@RequestParam Integer id) {
        AuthoritiesGroup entity = service.getEntity(id);
        ModelAndView view = new ModelAndView("../ms/authoritiesgroup/edit.jsp");
        view.addObject("entity",entity);
        view.addObject("list",resource.getList());
        return view;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "del",method = RequestMethod.GET)
    public ModelAndView removeAuthorities(@RequestParam Integer id) {
        service.delete(id);
        ModelAndView view = new ModelAndView("redirect:list.do");
        return view;
    }

}

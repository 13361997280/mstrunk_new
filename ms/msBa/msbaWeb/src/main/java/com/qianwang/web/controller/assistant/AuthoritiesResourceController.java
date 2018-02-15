package com.qianwang.web.controller.assistant;

import com.qianwang.dao.domain.assistant.AuthoritiesGroup;
import com.qianwang.dao.domain.assistant.AuthoritiesResource;
import com.qianwang.service.assistant.AuthoritiesGroupService;
import com.qianwang.service.assistant.AuthoritiesResourceService;
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
@RequestMapping(value = "/authoritiesresource")
public class AuthoritiesResourceController extends BaseController {

    @Autowired
    AuthoritiesResourceService service;

    /**
     * 查询
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getList() {
        List<AuthoritiesResource> list = service.getList();
        int count = service.getCount();
        ModelAndView view = new ModelAndView("../ms/authoritiesresource/list.jsp");
        view.addObject("count",count);
        view.addObject("list",list);
        return view;
    }

    /**
     * 保存
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(required = false, defaultValue = "0") Integer id,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String remark,
                                      @RequestParam(required = false) String key,
                                      @RequestParam(required = false) String value,
                                      @RequestParam(required = false) String enable
        ) {
    	AuthoritiesResource entity = new AuthoritiesResource();
        entity.setId(id);
        entity.setName(name);
        entity.setRemark(remark);
        entity.setKey(key);
        entity.setValue(value);
        entity.setEnable(Byte.parseByte(enable));
        if(id==0){
            service.add(entity);
        }else{
            service.edit(entity);
        }
        ModelAndView view = new ModelAndView("redirect:list.do");
        return view;
    }
    /**
     * 新增页面跳转
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView add() {
        AuthoritiesResource entity = new AuthoritiesResource();
        entity.setId(0);
        ModelAndView view = new ModelAndView("../ms/authoritiesresource/edit.jsp");
        view.addObject("entity",entity);
        return view;
    }
    /**
     * 修改页面跳转
     * @param id
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView editAuthorities(@RequestParam Integer id) {
        AuthoritiesResource entity = service.getEntity(id);
        ModelAndView view = new ModelAndView("../ms/authoritiesresource/edit.jsp");
        view.addObject("entity",entity);
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

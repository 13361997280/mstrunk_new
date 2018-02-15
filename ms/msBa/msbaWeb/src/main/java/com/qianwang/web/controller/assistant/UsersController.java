package com.qianwang.web.controller.assistant;

import com.alibaba.fastjson.JSONObject;
import com.qianwang.dao.domain.assistant.Users;
import com.qianwang.service.assistant.UsersService;
import com.qianwang.util.RandomString;
import com.qianwang.web.controller.BaseController;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by chenghaijiang on 17/08/21.
 */
@Controller
@RequestMapping(value = "/users")
public class UsersController extends BaseController {

    @Autowired
    UsersService service;
    @RequestMapping(value = "exist")
    @ResponseBody
    public Boolean existUsername(@RequestParam(required = true) String username){
        JSONObject jsonObject = new JSONObject();
        if(service.existUserName(username)>0){
            return true;
        }
        return false;
    }
    /**
     * 查询
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getList(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        Integer start = 0;
        int count = service.getCount();
        int pageSum = 0;
        if(count%size>0){
            pageSum = count/size+1;
        }else{
            pageSum = count/size;
        }
        if(pageNo>pageSum){
            pageNo = pageSum;
        }
        if(pageNo>1){
            start = (pageNo-1)*size;
        }else if(pageNo==0){
            pageNo = 1;
        }
        List<Users> list = service.getList(start,size);
        ModelAndView view = new ModelAndView("../ms/users/list.jsp");
        view.addObject("list",list);
        view.addObject("count",count);
        view.addObject("pageNo",pageNo);
        view.addObject("pageSum",pageSum);
        return view;
    }

    /**
     * 保存
     * @param id
     * @param username
     * @param enabled
     * @param mobile
     * @param groupIds
     * @param email
     * @param openapiStatus
     * @return
     */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(@RequestParam(required = false, defaultValue = "0") Integer id,
			@RequestParam(required = false) String username, @RequestParam(required = false) String enabled,
			@RequestParam(required = false) String mobile, @RequestParam(required = false) String groupIds,
			@RequestParam(required = false) String email, @RequestParam(required = false) String openapiStatus) {
		Users entity = new Users();
		if (id == 0) {
			entity.setUsername(username);
			entity.setEnabled(Boolean.parseBoolean(enabled));
			entity.setMobile(mobile);
			entity.setGroupIds(groupIds);
			entity.setEmail(email);
			// 用户appId&secret生成策略
			RandomString random = new RandomString();
			entity.setAppid(random.generateAppId());
			entity.setSecret(random.getRandomString(6));
			entity.setOpenapiStatus(Integer.parseInt(openapiStatus));
            entity.setPassword("96e79218965eb72c92a549dd5a330112");
            entity.setGroupIds("1");
			entity.setUpdateTime(new Date());
			entity.setCreateTime(new Date());
			service.add(entity);
		} else {
			entity = service.getEntity(id);
			if (null == entity) {
				return null;
			}
			if (StringUtils.isEmpty(entity.getAppid()) || StringUtils.isEmpty(entity.getSecret())) {
				// 用户appId&secret生成策略
				RandomString random = new RandomString();
				entity.setAppid(random.generateAppId());
				entity.setSecret(random.getRandomString(6));
			}
			entity.setUsername(username);
			entity.setEnabled(Boolean.parseBoolean(enabled));
			entity.setMobile(mobile);
			entity.setEmail(email);
			entity.setOpenapiStatus(Integer.parseInt(openapiStatus));
			entity.setUpdateTime(new Date());
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
        Users entity = new Users();
        entity.setId(0);
        ModelAndView view = new ModelAndView("../ms/users/edit.jsp");
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
        Users entity = service.getEntity(id);
        ModelAndView view = new ModelAndView("../ms/users/edit.jsp");
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

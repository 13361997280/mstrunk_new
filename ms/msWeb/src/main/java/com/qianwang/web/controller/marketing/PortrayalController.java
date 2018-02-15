package com.qianwang.web.controller.marketing;

import com.qianwang.dao.domain.UserSelectGroup;
import com.qianwang.mapper.UserSelectGroupMapper;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author song.j
 * @create 2017-05-19 11:11:05
 **/
@Controller
@RequestMapping("/portrayal")
public class PortrayalController extends BaseController{

    @Autowired
    UserSelectGroupMapper userSelectGroupMapper;

    @RequestMapping("/collection/allList")
    @ResponseBody
    public AjaxResult getList() {

        Integer userId = getCurrentUserId();

        List<UserSelectGroup> list = userSelectGroupMapper.selectAll(userId);

        return AjaxResult.successResult(list);
    }
}

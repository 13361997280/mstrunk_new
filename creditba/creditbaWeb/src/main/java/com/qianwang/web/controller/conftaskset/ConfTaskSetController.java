package com.qianwang.web.controller.conftaskset;

import com.qianwang.credit.entity.ConfBus;

import com.qianwang.credit.entity.ConfTaskSet;
import com.qianwang.credit.util.page.Page;
import com.qianwang.dao.domain.advertisement.SysConfig;
import com.qianwang.service.conftaskset.ConfTaskSetService;
import com.qianwang.service.sysconfig.SysconfigService;
import com.qianwang.util.lang.BeanHelper;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by YcY_YcY on 2017/11/16
 */
@Controller
@RequestMapping(value = "/bus/taskset")
public class ConfTaskSetController extends BaseController {

    @Autowired
    private ConfTaskSetService confTaskSetService;

   /* @Value("${sysconfig.account.prefix}")
    private String accountsPrefix;*/
    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "getConfTaskSet", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getList() {
        List<ConfTaskSet> list = confTaskSetService.firstQuery();
        return AjaxResult.successResult(list);
    }

    /**
     *
     */
    @RequestMapping("/getHisTaskSet")
    @ResponseBody
    public AjaxResult list(String orderBy, String orderType){
        Page page = this.getPage(10);
        List<ConfTaskSet> list = confTaskSetService.pageQuery(page, orderBy, "asc".equals(orderType));
        return this.getPageResult(page, list);
    }


    /**
     * 任务基础配置
     */
    @ResponseBody
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public AjaxResult save(){
        ConfTaskSet confTask = new ConfTaskSet();
        this.bindParams(confTask);
        if(confTask.getId()!=null){
            //将状态改为历史
            confTask.setStatus(1);
            this.confTaskSetService.update(confTask);
        }
        this.setOwner(confTask);
        confTask.setStatus(ConfTaskSet.STATUS_NORMAL);
        confTask.setId(null);
        this.confTaskSetService.save(confTask);
        return this.getSuccessResult();
    }
}

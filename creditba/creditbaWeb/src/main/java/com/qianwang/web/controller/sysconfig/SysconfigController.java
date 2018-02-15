package com.qianwang.web.controller.sysconfig;

import com.qianwang.dao.domain.advertisement.SysConfig;
import com.qianwang.service.sysconfig.SysconfigService;
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
@RequestMapping(value = "/sysconfig")
public class SysconfigController extends BaseController {

    @Autowired
    private SysconfigService sysconfigService;

    @Value("${sysconfig.account.prefix}")
    private String accountsPrefix;
    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "getSysconfigs", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getList() {
        List<SysConfig> list = sysconfigService.getSysconfig();
        return AjaxResult.successResult(list);
    }


    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "getAccounts", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getAccounts() {
        List<String> set = sysconfigService.getNameListByPerfix(accountsPrefix);
        return AjaxResult.successResult(set);
    }


    /**
     * 新增系统配置
     *
     * @param config
     */
    @RequestMapping(value = "addSysconfig", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addSysconfig(@ModelAttribute SysConfig config) {
        String userName =getCurrentUsername();
        int flag = sysconfigService.addSysconfig(config,userName);
        if (flag != 0) {
            return AjaxResult.successResult("保存成功！");
        }
        return AjaxResult.failureResult("保存失败！");
    }

    /**
     * 更新系统配置
     *
     * @param config
     */
    @RequestMapping(value = "editSysconfig", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult editSysconfig(@ModelAttribute SysConfig config) {
       String userName =getCurrentUsername();
        int flag = sysconfigService.editSysconfig(config,userName);
        if (flag != 0) {
            return AjaxResult.successResult("更新成功！");
        }
        return AjaxResult.failureResult("更新失败！");
    }


    /**
     * 删除系统配置
     *
     * @param config
     */
    @RequestMapping(value = "delSysconfig", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delSysconfig(@ModelAttribute SysConfig config) {
        int flag = sysconfigService.delSysconfig(config);
        if (flag != 0) {
            return AjaxResult.successResult("删除成功！");
        }
        return AjaxResult.failureResult("删除失败！");
    }


}

package com.qianwang.web.controller.confratio;

import com.qianwang.dao.domain.advertisement.ConfRatio;
import com.qianwang.service.confratio.ConfRatioService;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YcY_YcY on 2017/11/20
 */
@Controller
@RequestMapping(value = "/confratio")
public class ConfRatioController extends BaseController {
    @Resource
    private ConfRatioService ratioService;


    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "getRatios", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getList() {
        List<ConfRatio> list = ratioService.getAll();
        return AjaxResult.successResult(list);
    }

    /**
     * 新增系统配置
     *
     * @param
     */
    @RequestMapping(value = "addConfRatio", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addConfRatio(@RequestBody List<ConfRatio> ratios) {
        try {
            String userName = getCurrentUsername();
            int flag = ratioService.addConfRatio(ratios, userName);
            if (flag != 0) {
                return AjaxResult.successResult("保存成功！");
            }
        } catch (Exception e) {
            return AjaxResult.failureResult("保存失败！");
        }
        return AjaxResult.failureResult("保存失败！");
    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "getListBylistName", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getListBylistName(ConfRatio ratio) {
        List<ConfRatio> list = ratioService.getListBylistName(ratio);
        return AjaxResult.successResult(list);
    }


    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "getDis", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getDis() {
        List<ConfRatio> list = ratioService.getDis();
        return AjaxResult.successResult(list);
    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "upConfRatio", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult upConfRatio(@RequestBody ConfRatio ratios) {
        String userName = getCurrentUsername();
        int flag = ratioService.upConfRatio(ratios, userName);
        if (flag != 0) {
            return AjaxResult.successResult("更新成功！");
        }
        return AjaxResult.failureResult("更新失败！");
    }

}

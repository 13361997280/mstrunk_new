package com.qianwang.web.controller.advertisement;

import com.alibaba.fastjson.JSON;
import com.qianwang.dao.domain.advertisement.Advertisement;
import com.qianwang.dao.domain.advertisement.Customer;
import com.qianwang.service.assistant.AdvertisementService;
import com.qianwang.service.assistant.CustomerService;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import com.qianwang.web.util.QiniuUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * Created by songjie on 16/10/11.
 */
@Controller
@RequestMapping(value = "/advertisement")
public class AdvertisementController extends BaseController {

    @Autowired
    AdvertisementService service;
    @Autowired
    CustomerService customerService;

    /**
     * 查询
     *
     * @return
     */
	@RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getList() {
        List<Advertisement> list = service.selectByCondi();
        return AjaxResult.successResult(list);
    }
    /**
     * 单个查询
     *
     * @return
     */
	@RequestMapping(value = "/{id}/get", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getList(@PathVariable Integer id) {
        Advertisement entity = service.selectByPrimaryKey(id);
        return AjaxResult.successResult(entity);
    }
    /**
     * 初始化投放客户数据
     *
     * @return
     */
	@RequestMapping(value = "/getCust", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getCust() {
        List<Customer> entity = customerService.selectAll();
        return AjaxResult.successResult(entity);
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult edit(Advertisement entity,@RequestParam("release") Integer release) {
        Boolean success = false;
        entity.setRelease(release);
        try {
            entity.validate();
        } catch (IllegalArgumentException e) {
            return AjaxResult.failureResult(e.getMessage());
        }
        entity.setStatus(0);
        if(release!=null&&release==1){
            entity.setStatus(1);
        }
        if(entity.getId()==null){
            Date now = new Date();
            entity.setCreateStmp(now);
            entity.setUpdateStmp(now);
            entity.setOperator(getCurrentUsername());
            entity.setDeleteFlag(0);
            success = service.insertSelective(entity) == 1;
        }else {
            entity.setUpdateStmp(new Date());
            entity.setOperator(getCurrentUsername());
            success = service.updateByPrimaryKeySelective(entity) == 1;
        }
        if (!success) {
            return AjaxResult.failureResult("保存失败!");
        }
        return AjaxResult.successResult("保存成功!");
    }
    /**
     * 发布/下架
     *
     * @return
     */
    @RequestMapping(value = "/{id}/updateStatus", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult release(@PathVariable Integer id,@RequestParam("status") Integer status) {
        Advertisement entity = new Advertisement();
        entity.setId(id);
        entity.setStatus(status);
        Boolean success = service.updateByPrimaryKeySelective(entity)==1;
        if (!success) {
            return AjaxResult.failureResult("修改失败!");
        }
        return AjaxResult.successResult("修改成功!");
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/delete",method = RequestMethod.DELETE)
    @ResponseBody
    public AjaxResult removeAssPromotion(@PathVariable Integer id) {
        Boolean success = service.deleteByPrimaryKey(id)==1;
        if (!success){
            AjaxResult.failureResult("操作失败!");
        }
        return AjaxResult.successResult("操作成功！");
    }
       
}

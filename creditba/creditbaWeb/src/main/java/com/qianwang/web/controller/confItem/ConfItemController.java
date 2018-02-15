package com.qianwang.web.controller.confItem;
import com.qianwang.dao.domain.confitem.ConfItem;
import com.qianwang.service.confitem.ConfItemService;
import com.qianwang.service.dto.confitem.ItemPageDto;
import com.qianwang.service.vo.confitem.ItemQueryAllVO;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * Created by fun
 * on 2017/11/16.
 */

@Controller
@RequestMapping(value = "/ratioScore")
public class ConfItemController extends BaseController {
    protected static final Logger LOG = LoggerFactory.getLogger(ConfItemController.class);
    @Resource
    private ConfItemService confItemService;
    /**
     * 查询
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getList( ItemPageDto  dto) {
        ItemQueryAllVO itemQueryAllVO =  confItemService.selectAll(dto);
        if (itemQueryAllVO != null){
            return new AjaxResult(itemQueryAllVO.getCount(),true,itemQueryAllVO.getItems());
        }
        return AjaxResult.failureResult("查询失败！");
    }


    @RequestMapping(value = "querybyid", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult queryById(@RequestParam("id") Integer id) {
        ConfItem confItem =  confItemService.queryByid(id);
        if (confItem != null){
            return AjaxResult.successResult(confItem);
        }
        return AjaxResult.failureResult("查询失败！");
    }


    /**
     * 新增/修改
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@RequestBody ConfItem confItem) {
        int count ;
        String userName = getCurrentUsername();
        confItem.setOperator(userName);
        if(null == confItem.getId()){
            count = confItemService.insertConfItem(confItem);
        }else {
            count= confItemService.updateByPrimaryKey(confItem);
        }
        if(count==1){
            return AjaxResult.successResult("保存成功！");
        }else {
            return AjaxResult.failureResult("保存失败！");
        }
    }


    /**
     * 发布/下架
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult release(@RequestBody ConfItem confItem) {
        String userName = getCurrentUsername();
        confItem.setOperator(userName);
         int count = confItemService.updateByPrimaryKey(confItem);
        if (count==1) {
            return AjaxResult.successResult("修改成功!");
        }
        return AjaxResult.failureResult("修改失败!");
    }



}

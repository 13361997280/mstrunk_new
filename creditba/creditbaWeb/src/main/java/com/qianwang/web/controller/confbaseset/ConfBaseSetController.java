package com.qianwang.web.controller.confbaseset;

import com.qianwang.dao.domain.confbaseset.ConfBaseSet;
import com.qianwang.service.confbaseset.ConfBaseSetService;
import com.qianwang.service.dto.confbaseset.BaseSetPageDto;
import com.qianwang.service.vo.confbaseset.BaseSetQueryAllVO;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * Created by fun
 * on 2017/11/17.
 */
@Controller
@RequestMapping(value = "/set")
public class ConfBaseSetController extends BaseController {


    @Resource
    private ConfBaseSetService confBaseSetService;

    /**
     * 查询
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getList(BaseSetPageDto dto) {
        String userName = getCurrentUsername();
        BaseSetQueryAllVO baseSetQueryAllVO = confBaseSetService.selectAll(dto);
        if(baseSetQueryAllVO != null){
            return new AjaxResult(baseSetQueryAllVO.getCount(),true,baseSetQueryAllVO);
        }
        return AjaxResult.failureResult("查询失败");
    }

    /**
     * 新增/修改
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult update(@RequestBody ConfBaseSet confBaseSet) {
        String userName = getCurrentUsername();
        confBaseSet.setOperator(userName);
        int count = 0;
        try {
            count = confBaseSetService.save(confBaseSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count == 1){
              return AjaxResult.successResult("保存成功！");
          }
            return AjaxResult.failureResult("保存失败!");
    }


}

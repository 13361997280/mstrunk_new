package com.qianwang.web.controller.es;

import com.qianwang.mapper.UserLabelMapper;
import com.qianwang.web.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author song.j
 * @create 2017-07-12 10:10:38
 **/

@Controller
@RequestMapping("/userlabel")
public class UserlabelController {

    @Autowired
    UserLabelMapper userLabelMapper;

    @RequestMapping("/profession")
    @ResponseBody
    public AjaxResult getUserProfession(Integer userId){

        if (userId == null){
            return AjaxResult.success();
        }

        Integer profession = userLabelMapper.selectUserProfession(userId.toString());

        return AjaxResult.successResult(profession);
    }
}

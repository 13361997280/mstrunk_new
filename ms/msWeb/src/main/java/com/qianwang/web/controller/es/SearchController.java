package com.qianwang.web.controller.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qianwang.dao.domain.UserSelectGroup;
import com.qianwang.service.properties.UrlProperties;
import com.qianwang.service.user.UserSelectGroupService;
import com.qianwang.util.http.HttpUtils;
import com.qianwang.web.auth.CheckEnum;
import com.qianwang.web.auth.MsAuth;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private UserSelectGroupService userSelectGroupService;

    @Autowired
    UrlProperties urlProperties;

    /**
     * 用户标签查询
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/calculation", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult calculation(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String url = urlProperties.esAddr + "/calLabel";
        JSONArray jsonArray = jsonObject.getJSONArray("selectList");
        String paramStr = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            String fieldName = object.getString("code");
            String fieldValue = object.getString("value");
            paramStr = paramStr + fieldName + "=" + fieldValue + "&";
        }
        paramStr = paramStr + "showList=" + jsonObject.getString("showList")
                + "&userId=" + getCurrentUserId() + "&page=" + jsonObject.getString("page")
                + "&size=" + jsonObject.getString("size");
        String returnStr = null;
        try {
            returnStr = HttpUtils.get(url + "?" + paramStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.successResult(JSON.parseObject(returnStr));
    }

    /**
     * 用户字典查询
     *
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public AjaxResult getList() {
        String url = urlProperties.esAddr + "/dict";
        String returnStr = null;
        try {
            returnStr = HttpUtils.get(url);
            log.info("netty dict get result = {}", returnStr);
        } catch (Exception e) {
            log.error("netty dict get result error message = {}", returnStr, e);
        }
        return AjaxResult.successResult(JSON.parseObject(returnStr));
    }

    /**
     * 用户手机或id搜索
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/labelList")
    @ResponseBody
    @MsAuth(role = CheckEnum.CHECK)
    public AjaxResult labelList(String id) {
        String url = urlProperties.esAddr + "/getEntityByUserId";
        String username = getCurrentUserName();
        String returnStr = null;
        try {
            returnStr = HttpUtils.get(url + "?id=" + id + "&username=" + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSON.parseObject(returnStr);

        return AjaxResult.successResult(jsonObject);
    }

    /**
     * 收藏本次筛选
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    @ResponseBody
    @MsAuth(role = CheckEnum.CHECK)
    public AjaxResult collection(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String url = urlProperties.esAddr + "/updateUserId";
        JSONArray jsonArray = jsonObject.getJSONArray("selectList");
        String paramStr = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            String fieldName = object.getString("code");
            String fieldValue = object.getString("value");
            if (null == fieldValue || "".equals(fieldValue)) continue;
            paramStr = paramStr + fieldName + "=" + fieldValue + "&";
        }
        if (!"".equals(paramStr)) {
            paramStr = paramStr.substring(0, paramStr.length() - 1);
        }
        String name = jsonObject.getString("name");
        String remark = jsonObject.getString("remark");
        Integer number = jsonObject.getInteger("number");
        Integer userId = getCurrentUserId();
        // 保存到数据库
        String id = UUID.randomUUID().toString();
        UserSelectGroup entity = new UserSelectGroup();
        entity.setCreateTime(new Date());
        entity.setDesc(remark);
        entity.setName(name);
        entity.setNum(number);
        entity.setUserId(userId);
        entity.setUserSelectId(id);
        userSelectGroupService.insert(entity);
        //保存到es
        String returnStr = null;
        try {
            returnStr = HttpUtils.get(url + "?id=" + id + "&userId=" + userId + "&" + paramStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.successResult(JSON.parseObject(returnStr));
    }

    /**
     * 查看用户群列表
     *
     * @param size
     * @param page
     * @return
     */
    @RequestMapping(value = "/usergroup")
    @ResponseBody
    @MsAuth(role = CheckEnum.CHECK)
    public AjaxResult usergroup(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer
            size) {
        //最多一次只能查50条
        if (size > 50)
            size = 50;
        // TODO: 17/5/19 getUserId
        Map pageData = userSelectGroupService.getResultByPage(page, size, getCurrentUserId());

        return AjaxResult.successResult(pageData);
    }

    /**
     * 查看用户群查询条件
     *
     * @return
     */
    @RequestMapping(value = "/usergroupdetail")
    @ResponseBody
    public AjaxResult usergroupdetail(String id) {
        String url = urlProperties.esAddr + "/getSearchConstForFront";
        url = url + "?id=" + id;
        String returnStr = null;
        try {
            returnStr = HttpUtils.get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.successResult(JSON.parseObject(returnStr));
    }
}

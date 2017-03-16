package com.cus.cms.admin.action.wen;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.constants.ErrorCode;
import com.cus.cms.common.model.wen.StaticConfig;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.service.wen.StaticConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Andy 
 * @description
 */
@Controller
public class StaticConfigAction extends BaseAction {
    @Autowired
    private StaticConfigService staticConfigService;

    
    /**
     * 查询Config列表
     *
     * @param searchText Config名称
     */
    @RequestMapping("/config/queryConfigs")
    @ResponseBody
    public void queryConfigs(String searchText) {
        JSONObject jsonObject = new JSONObject();
        List<StaticConfig> list = staticConfigService.getConfigWithPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", staticConfigService.getCountWithPage(searchText));
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/config/getConfigById")
    @ResponseBody
    public void getConfigById(Long id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {
            StaticConfig staticConfig = staticConfigService.getStaticConfigById(id);
            if (staticConfig != null) {
                jsonObject.put("entity", staticConfig);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/config/saveConfig")
    @ResponseBody
    public void saveConfig(StaticConfig staticConfig, boolean isEdit) {
        int result = -1;
        if (staticConfig != null) {
            try {
                result = staticConfigService.saveConfig(staticConfig, isEdit);
            } catch (Exception e) {
                m_logger.warn("saveConfig fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/config/delConfigs")
    @ResponseBody
    public void delConfigs(Long id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                staticConfigService.delConfigById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                m_logger.warn("delConfigs fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }


    @RequestMapping("/config/getConfigByCode")
    @ResponseBody
    public String getConfigByCode(String code, Boolean hasNull) {
        if (BlankUtil.isBlank(code)) {
            return null;
        }
        List<StaticConfig> list = staticConfigService.getConfigByCode(code);
        JSONArray array = new JSONArray();
        if (hasNull != null && hasNull) {
            JSONObject obj = new JSONObject();
            obj.put("id", " ");
            obj.put("text", "-- 请选择 --");
            array.add(obj);
        }
        for (StaticConfig dict : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", dict.getCval());
            obj.put("text", dict.getCkey());
            array.add(obj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("items", array);
        return jsonObject.toJSONString();
    }
}

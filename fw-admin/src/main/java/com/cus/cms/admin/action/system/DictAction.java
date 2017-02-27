package com.cus.cms.admin.action.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.constants.ErrorCode;
import com.cus.cms.common.model.system.AdminDict;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.wrapper.Combobox;
import com.cus.cms.service.system.AdminDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 
 * @description
 */
@Controller
public class DictAction extends BaseAction {
    @Autowired
    private AdminDictService adminDictService;

    
    /**
     * 查询Dict列表
     *
     * @param searchText Dict名称
     */
    @RequestMapping("/admin/queryDicts")
    @ResponseBody
    public void queryDicts(String searchText) {
        JSONObject jsonObject = new JSONObject();
        List<AdminDict> list = adminDictService.getDictWithPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", adminDictService.getCountWithPage(searchText));
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/getDictById")
    @ResponseBody
    public void getDictById(Long id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {
            AdminDict adminDict = adminDictService.getAdminDictById(id);
            if (adminDict != null) {
                jsonObject.put("entity", adminDict);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/saveDict")
    @ResponseBody
    public void saveDict(AdminDict adminDict, boolean isEdit) {
        int result = -1;
        if (adminDict != null) {
            try {
                result = adminDictService.saveDict(adminDict, isEdit);
            } catch (Exception e) {
                m_logger.warn("saveDict fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/admin/delDicts")
    @ResponseBody
    public void delDicts(Long id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                adminDictService.delDictById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                m_logger.warn("delDicts fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }


    @RequestMapping("/admin/getDictByCode")
    @ResponseBody
    public String getDictByCode(String code, Boolean hasNull) {
        if (BlankUtil.isBlank(code)) {
            return null;
        }
        List<AdminDict> list = adminDictService.getDictByCode(code);
        JSONArray array = new JSONArray();
        if (hasNull != null && hasNull) {
            JSONObject obj = new JSONObject();
            obj.put("id", " ");
            obj.put("text", "-- 请选择 --");
            array.add(obj);
        }
        for (AdminDict dict : list) {
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

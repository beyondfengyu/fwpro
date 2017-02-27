package com.cus.cms.admin.action.wen;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.constants.ErrorCode;
import com.cus.cms.common.model.system.AdminDict;
import com.cus.cms.common.model.wen.FwDir;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.wrapper.Combobox;
import com.cus.cms.service.wen.FwDirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy
 */
@Controller
public class FwDirAction extends BaseAction {


    @Autowired
    private FwDirService fwDirService;

    @RequestMapping("/wen/indexDir")
    public String indexDir(Model model, String oneDir) {
        if (!BlankUtil.isBlank(oneDir)) {
            model.addAttribute("oneDir", oneDir);
        }
        return "/module/fw_dir";
    }
    /**
     * 查询列表
     *
     * @param searchText
     */
    @RequestMapping("/wen/getFwDirs")
    @ResponseBody
    public void getFwDirs(String searchText, String oneDir, int dirType) {
        JSONObject jsonObject = new JSONObject();
        List<FwDir> list = fwDirService.getFwDirs(searchText, oneDir, dirType, getPageNumber(), getPageSize());
        if (!BlankUtil.isBlank(list)) {
            Map<String, String> parentMap = getLastNames();
            for (FwDir fwDir : list) {
                if (BlankUtil.isBlank(fwDir.getLastCode()) || "#".equals(fwDir.getLastCode())) {
                    fwDir.setLastName("#");
                } else {
                    fwDir.setLastName(parentMap.get(fwDir.getLastCode()));
                }
            }
            jsonObject.put("total", fwDirService.getFwDirCount(searchText, oneDir, dirType));
        }
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/wen/getFwDirById")
    @ResponseBody
    public void getFwDirById(long id) {
        JSONObject jsonObject = new JSONObject();
        FwDir fwDir = fwDirService.getFwDirById(id);
        if (fwDir != null) {
            jsonObject.put("entity", fwDir);
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/wen/saveFwDir")
    @ResponseBody
    public void saveFwDir(FwDir fwDir, boolean isEdit) {
        int result = -1;
        if (fwDir != null) {
            try {
                result = fwDirService.saveFwDir(fwDir, isEdit);
            } catch (Exception e) {
                m_logger.warn("saveFwDir fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/wen/delFwDirs")
    @ResponseBody
    public void delFwDirs(long id) {
        int result = 1;
        try {
            fwDirService.delFwDirById(id);
        } catch (Exception e) {
            result = ErrorCode.SERVER_ERROR;
            m_logger.warn("delFwDirs fail,cause by " + e.getMessage(), e);
        }
        writeJsonResult(result);
    }

    @RequestMapping("/wen/getOneDirs")
    @ResponseBody
    public String getOneDirs(Boolean hasNull) {
        List<FwDir> list = fwDirService.getOneDirs();
        JSONArray array = new JSONArray();
        if (hasNull != null && hasNull) {
            JSONObject obj = new JSONObject();
            obj.put("id", " ");
            obj.put("text", "-- 请选择 --");
            array.add(obj);
        }
        for (FwDir fwDir : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", fwDir.getDirCode());
            obj.put("text", fwDir.getDirName());
            array.add(obj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("items", array);
        return jsonObject.toJSONString();
    }

    @RequestMapping("/wen/getTwoDirByOne")
    @ResponseBody
    public String getTwoDirByOne(String oneDir, Boolean hasNull) {
        List<FwDir> list = fwDirService.getTwoDirByOne(oneDir);
        JSONArray array = new JSONArray();
        if (hasNull != null && hasNull) {
            JSONObject obj = new JSONObject();
            obj.put("id", " ");
            obj.put("text", "-- 请选择 --");
            array.add(obj);
        }
        for (FwDir fwDir : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", fwDir.getDirCode());
            obj.put("text", fwDir.getDirName());
            array.add(obj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("items", array);
        return jsonObject.toJSONString();
    }

    private Map<String, String> getLastNames() {
        Map<String, String> map = new HashMap<String, String>();
        List<FwDir> list = fwDirService.getParentFwDirs();
        for (FwDir fwDir : list) {
            map.put(fwDir.getDirCode(), fwDir.getDirName());
        }
        return map;
    }
}


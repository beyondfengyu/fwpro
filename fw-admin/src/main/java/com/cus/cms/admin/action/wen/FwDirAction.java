package com.cus.cms.admin.action.wen;

import com.alibaba.fastjson.JSONObject;
import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.constants.ErrorCode;
import com.cus.cms.common.model.wen.FwDir;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.wrapper.Combobox;
import com.cus.cms.service.wen.FwDirService;
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
 */
@Controller
public class FwDirAction extends BaseAction {


    @Autowired
    private FwDirService fwDirService;

    /**
     * 查询列表
     *
     * @param searchText
     */
    @RequestMapping("/wen/getFwDirs")
    @ResponseBody
    public void getFwDirs(String searchText) {
        JSONObject jsonObject = new JSONObject();
        List<FwDir> list = fwDirService.getFwDirs(searchText);
        if (!BlankUtil.isBlank(list)) {
            Map<String, String> parentMap = getLastNames();
            for (FwDir fwDir : list) {
                if (BlankUtil.isBlank(fwDir.getLastCode()) || "#".equals(fwDir.getLastCode())) {
                    fwDir.setLastName("#");
                } else {
                    fwDir.setLastName(parentMap.get(fwDir.getLastCode()));
                }
            }
            jsonObject.put("total", fwDirService.getFwDirCount(searchText));
        }
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/wen/getFwDirById")
    @ResponseBody
    public void getFwDirById(String id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {
            FwDir fwDir = fwDirService.getFwDirById(id);
            if (fwDir != null) {
                jsonObject.put("entity", fwDir);
            }
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
    public void delFwDirs(String id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                fwDirService.delFwDirById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                m_logger.warn("delFwDirs fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    /**
     * 获取所有父级菜单，转换成组合框结构
     */
    @RequestMapping("/wen/getParentFwDirs")
    @ResponseBody
    public void getParentFwDirs() {
        String result = "[{txt:'--  无  --',val:'#'}]";
        try {
            List<FwDir> list = fwDirService.getParentFwDirs();
            result = JSONObject.toJSONString(tranToCombobox(list));
        } catch (Exception e) {
            m_logger.warn("getParentFwDirs fail,cause by " + e.getMessage(), e);
        }
        writeJson(result);
    }

    private List<Combobox> tranToCombobox(List<FwDir> list) {
        List<Combobox> boxs = new ArrayList<Combobox>();
        Combobox box = null;
        box = new Combobox();
        box.setOtxt("--  无  --");
        box.setOval("#");
        boxs.add(box);
        if (!BlankUtil.isBlank(list))
            for (FwDir menu : list) {
                box = new Combobox();
                box.setOtxt(menu.getDirName());
                box.setOval(menu.getDirCode());
                boxs.add(box);
            }
        return boxs;
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


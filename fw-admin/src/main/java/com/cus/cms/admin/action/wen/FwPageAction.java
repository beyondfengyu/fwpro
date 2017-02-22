package com.cus.cms.admin.action.wen;

import com.alibaba.fastjson.JSONObject;
import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.constants.ErrorCode;
import com.cus.cms.common.model.wen.FwPage;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.wrapper.Combobox;
import com.cus.cms.service.wen.FwPageService;
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
public class FwPageAction extends BaseAction {


    @Autowired
    private FwPageService fwPageService;

    /**
     * 查询列表
     *
     * @param searchText
     */
    @RequestMapping("/wen/getFwPages")
    @ResponseBody
    public void getFwPages(String searchText) {
        JSONObject jsonObject = new JSONObject();
        List<FwPage> list = fwPageService.getFwPages(searchText, getPageNumber(), getPageSize());
        if (!BlankUtil.isBlank(list)) {
            jsonObject.put("total", fwPageService.getFwPageCount(searchText));
        }
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 查询列表
     *
     * @param searchText
     */
    @RequestMapping("/wen/getFwPageByDir")
    @ResponseBody
    public void getFwPageByDir(String searchText, String oneDir) {
        JSONObject jsonObject = new JSONObject();
        List<FwPage> list = fwPageService.getFwPages(searchText, oneDir,getPageNumber(), getPageSize());
        if (!BlankUtil.isBlank(list)) {
            jsonObject.put("total", fwPageService.getFwPageCount(searchText, oneDir));
        }
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/wen/getFwPageById")
    @ResponseBody
    public void getFwPageById(Long id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {
            FwPage fwPage = fwPageService.getFwPageById(id);
            if (fwPage != null) {
                jsonObject.put("entity", fwPage);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/wen/saveFwPage")
    @ResponseBody
    public void saveFwPage(FwPage fwPage, boolean isEdit) {
        int result = -1;
        if (fwPage != null) {
            try {
                result = fwPageService.saveFwPage(fwPage, isEdit);
            } catch (Exception e) {
                m_logger.warn("saveFwPage fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/wen/delFwPages")
    @ResponseBody
    public void delFwPages(Long id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                fwPageService.delFwPageById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                m_logger.warn("delFwPages fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

}


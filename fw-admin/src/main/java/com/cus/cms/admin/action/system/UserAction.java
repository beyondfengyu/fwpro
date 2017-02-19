package com.cus.cms.admin.action.system;

import com.alibaba.fastjson.JSONObject;
import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.admin.util.ImageType;
import com.cus.cms.common.constants.ErrorCode;
import com.cus.cms.common.model.AdminRefUserRole;
import com.cus.cms.common.model.AdminRole;
import com.cus.cms.common.model.AdminUser;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.admin.util.AdminUtil;
import com.cus.cms.common.util.DateTimeUtil;
import com.cus.cms.service.system.AdminRoleService;
import com.cus.cms.service.system.AdminUserService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author Andy
 * @version 1.0
 * @date 2016/10/17
 */
@Controller
public class UserAction extends BaseAction {

    private final static int IMG_SIZE_MAX = 2 * 1024 * 1024;//每张图片的最大值，2M
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminRoleService adminRoleService;

    /**
     * 查询用户列表
     *
     * @param searchText 用户名称
     */
    @RequestMapping("/admin/getUsers")
    @ResponseBody
    public void getUsers(String searchText) {
        JSONObject jsonObject = new JSONObject();
        List<AdminUser> list = adminUserService.getUserWithPage(searchText, getPageNumber(), getPageSize());
        if (!BlankUtil.isBlank(list)) {
            jsonObject.put("total", adminUserService.getCountWithPage(searchText));
        }
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/getUser")
    @ResponseBody
    public void getAdminUser(Long uid) {
        JSONObject jsonObject = new JSONObject();
        if (!BlankUtil.isBlank(uid)) {
            try {
                AdminUser adminUser = adminUserService.getAdminUserById(uid);
                if (adminUser != null) {
                    jsonObject.put("entity", adminUser);
                }
            } catch (Exception e) {
                m_logger.warn("getAdminUser fail,cause by " + e.getMessage(), e);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/saveUser")
    @ResponseBody
    public void saveUser(AdminUser adminUser, boolean isEdit) {
        int result = -1;
        if (adminUser != null) {
            try {
                adminUser.setOptUid(getAdminId());
                adminUser.setUpdateTime(DateTimeUtil.getCurrentTime());
                result = adminUserService.saveUser(adminUser, isEdit);
            } catch (Exception e) {
                m_logger.warn("saveUser fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/admin/delUsers")
    @ResponseBody
    public void delUsers(Long id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                adminUserService.delUserById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                m_logger.warn("delUser fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    /**
     * 上传图像，使用的是Spring的CommonsFileUpload组件，
     * 需要在mvc配置文件中配置一个MultipartResolver解析器实例
     *
     * @param uploadFile 上传的文件对象
     * @return
     */
    @RequestMapping("/admin/upload/headimg")
    @ResponseBody
    public void uploadImage(@RequestParam("uploadFile") MultipartFile uploadFile) {
        String msg = null;//记录上传过程中的提示信息
        JSONObject jsonObject = new JSONObject();
        if (!uploadFile.isEmpty()) {
            msg = filterImgFile(uploadFile.getOriginalFilename(), uploadFile.getSize());
            //msg为空，说明上传的图像符合要求
            if (BlankUtil.isBlank(msg)) {
                //构建图像路径，存储在数据库的路径，非绝对路径
                String filePath = "/static/headimg/" + uploadFile.getOriginalFilename();
                try {
                    //使用Thumbnails工具编辑图片后写入磁盘
                    Thumbnails.of(uploadFile.getInputStream())
                            .scale(1.0f)//压缩的比例
                            .toFile(new File(AdminUtil.getAbsolutePath(getRequest(), filePath)));//把文件流写入磁盘
                    jsonObject.put("path", filePath);
                } catch (Exception e) {
                    m_logger.error("upload fail，" + e.getMessage());
                    msg = "上传失败，I/O流异常！";
                }
            }
        } else {
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg", msg);
        //代码执行到这里，说明上传已经失败
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/getRoleByUser")
    @ResponseBody
    public void getRoleByUser(Long uid) {
        JSONObject jsonObject = new JSONObject();
        if (uid != null && uid > 0) {
            List<AdminRole> list = adminRoleService.getAllRole();
            List<AdminRefUserRole> refUserRoles = adminRoleService.getRoleByAdminId(uid);
            for (AdminRole adminRole : list) {
                adminRole.setRole(false);
                for (AdminRefUserRole adminRefUserRole : refUserRoles) {
                    if (adminRefUserRole.getRoleId() == adminRole.getId()) {
                        adminRole.setRole(true);
                        break;
                    }
                }
            }
            jsonObject.put("result", 0);
            jsonObject.put("list", list);
        } else {
            jsonObject.put("result", 1);
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/saveUserRole")
    @ResponseBody
    public void saveUserRole(Long uid, Long[] roles) {
        int result = -1;
        if (uid != null && roles != null) {
            try {
                result = adminRoleService.saveUserRole(uid, roles);
            } catch (Exception e) {
                m_logger.warn("saveUserRole fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    /**
     * 过滤文件
     *
     * @return
     */
    private String filterImgFile(String name, long size) {
        String msg = null;
        //过滤大小不符合要求的图片
        if (size < IMG_SIZE_MAX) {
            String type = AdminUtil.getFileType(name);
            //过滤类型不符合要求的图片
            if (!ImageType.contains(type)) {
                msg = "上传失败，图片类型不符合要求!";
            }
        } else {
            msg = "上传失败，图片大小超过允许的最大值2M!";
        }
        return msg;
    }
}
